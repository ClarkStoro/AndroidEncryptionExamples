package com.clarkstoro.androidencryptionexamples.utils

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import timber.log.Timber
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.KeyStore.SecretKeyEntry
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec


class BiometricCryptoManager {


    companion object {

        private const val KEYSTORE = "AndroidKeyStore"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES

        private const val GCM_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val GCM_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val GCM_TAG_LENGTH = 128

        private const val CBC_BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val CBC_PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7

        private const val CURRENT_BLOCK_MODE = GCM_BLOCK_MODE
        private const val CURRENT_PADDING = GCM_PADDING

        private const val TRANSFORMATION = "$ALGORITHM/$CURRENT_BLOCK_MODE/$CURRENT_PADDING"

        private const val ALIAS_KEY = "BIOMETRIC_ENCRYPTION_ALIAS_KEY"
        private const val KEY_SIZE = 256

        private const val APPEND_SEPARATOR = "|||"
    }


    private val keystore = KeyStore.getInstance(KEYSTORE).apply {
        load(null)
    }

    /**
     * @return Cipher for Encryption with biometric
     */
    fun getEncryptCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, getKey())
        }
    }

    /**
     * @return Cipher for Decryption with biometric by obtaining the IV from the text encrypted
     */
    fun getDecryptCipherFromStringAppendMode(encryptedText: String): Cipher? {
        return splitEncryptedDataAppendMode(encryptedText)?.iv?.let { iv ->
            getDecryptCipherForIv(iv)
        }
    }

    fun getDecryptCipherFromStringByteArrayMode(encryptedText: String): Cipher? {
        return splitEncryptedDataArrayMode(encryptedText)?.iv?.let { iv ->
            getDecryptCipherForIv(iv)
        }
    }

    private fun getDecryptCipherForIv(initializationVector: ByteArray): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            when(CURRENT_BLOCK_MODE) {
                GCM_BLOCK_MODE -> {
                    val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, initializationVector)
                    init(Cipher.DECRYPT_MODE, getKey(), gcmParameterSpec)
                }
                CBC_BLOCK_MODE -> {
                    init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(initializationVector))
                }
                else -> {
                    throw Exception()
                }
            }
        }
    }

    private fun getKey(): SecretKey {
        return getValidKeyOrNull() ?: generateKey()
    }

    private fun getValidKeyOrNull(): SecretKey? {
        return try {
            val existingKey = keystore.getEntry(ALIAS_KEY, null) as? SecretKeyEntry
            existingKey?.secretKey?.also { sk ->
                Cipher.getInstance(TRANSFORMATION).apply {
                    init(Cipher.ENCRYPT_MODE, sk)
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun isKeyValid(): Boolean {
        return getValidKeyOrNull() != null
    }

    private fun generateKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    ALIAS_KEY,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                .setKeySize(KEY_SIZE)
                .setBlockModes(CURRENT_BLOCK_MODE)
                .setEncryptionPaddings(CURRENT_PADDING)
                .apply {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        setUserAuthenticationRequired(true)
                        setInvalidatedByBiometricEnrollment(false)
                        setUserAuthenticationParameters(
                            0 /* duration */,
                            KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL
                        )
                    } else { // API <= Q
                        // parameter "0" defaults to AUTH_BIOMETRIC_STRONG | AUTH_DEVICE_CREDENTIAL
                        // parameter "-1" default to AUTH_BIOMETRIC_STRONG
                        // source: https://cs.android.com/android/platform/superproject/+/android-11.0.0_r3:frameworks/base/keystore/java/android/security/keystore/KeyGenParameterSpec.java;l=1236-1246;drc=a811787a9642e6a9e563f2b7dfb15b5ae27ebe98
                        setUserAuthenticationValidityDurationSeconds(0)
                    }
                }
            .build()
            )
        }.generateKey()
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Append Mode
     */

    fun encryptStringAppendMode(encryptCipher: Cipher, plainText: String): String? {
        return try {
            val cipherText = encryptCipher.doFinal(plainText.toByteArray())
            val cipherTextBase64 = Base64.encodeToString(cipherText, Base64.DEFAULT)
            val ivBase64 = Base64.encodeToString(
                encryptCipher.iv,
                Base64.DEFAULT
            )

            return "$ivBase64$APPEND_SEPARATOR$cipherTextBase64"
        } catch (e: Exception) {
            Timber.e("Error: failed to encrypt string - Append Mode:\n$e")
            null
        }
    }

    fun decryptStringAppendMode(decryptCipher: Cipher, strToDecode: String): String? {
        return try {
            val cipherText = splitEncryptedDataAppendMode(strToDecode)?.cipherText
            val plainText = decryptCipher.doFinal(cipherText)

            return String(plainText, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Timber.e("Error: failed to decrypt string - Append Mode:\n$e")
            null
        }
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Byte Array Mode
     */

    fun encryptToStringByteArrayMode(encryptCipher: Cipher, bytes: ByteArray): String? {
        return try {
            val cipherText = encryptCipher.doFinal(bytes)
            val outputStream = ByteArrayOutputStream()
            outputStream.use {
                it.write(encryptCipher.iv.size)
                it.write(encryptCipher.iv)
                it.write(cipherText.size)
                it.write(cipherText)
            }

            Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        } catch (e: Exception) {
            Timber.e("Error: failed to encrypt string - Byte Array Mode:\n$e")
            null
        }
    }

    fun decryptFromStringByteArrayMode(decryptCipher: Cipher, stringToDecode: String): ByteArray? {
        return try {
            val cipherTextBytes = splitEncryptedDataArrayMode(stringToDecode)?.cipherText
            decryptCipher.doFinal(cipherTextBytes)
        } catch (e: Exception) {
            Timber.e("Error: failed to decrypt string - Byte Array Mode:\n$e")
            null
        }
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Utils
     */

    private fun splitEncryptedDataAppendMode(encryptedText: String): EncryptedData? {
        return try {
            val ivAndCipherTextSplit = encryptedText.split(APPEND_SEPARATOR)
            val iv = Base64.decode(ivAndCipherTextSplit[0], Base64.DEFAULT)
            val cipherText = Base64.decode(ivAndCipherTextSplit[1], Base64.DEFAULT)

            return EncryptedData(iv, cipherText)
        } catch (e: Exception) {
            null
        }
    }


    private fun splitEncryptedDataArrayMode(encryptedText: String): EncryptedData? {
        return try {
            val decodedString = Base64.decode(encryptedText, Base64.DEFAULT)
            val inputStream = ByteArrayInputStream(decodedString)
            return inputStream.use {
                val ivSize = it.read()
                val iv = ByteArray(ivSize)
                it.read(iv)

                val cipherTextBytesSize = it.read()
                val cipherTextBytes = ByteArray(cipherTextBytesSize)
                it.read(cipherTextBytes)

                EncryptedData(iv, cipherTextBytes)
            }
        } catch (e: Exception) {
           null
        }
    }


    data class EncryptedData(
        val iv: ByteArray,
        val cipherText: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as EncryptedData

            if (!iv.contentEquals(other.iv)) return false
            return cipherText.contentEquals(other.cipherText)
        }

        override fun hashCode(): Int {
            var result = iv.contentHashCode()
            result = 31 * result + cipherText.contentHashCode()
            return result
        }
    }
}