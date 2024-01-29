package com.clarkstoro.androidencryptionexamples.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource


class AsymmetricCryptoManager {

    companion object {

        private const val KEYSTORE = "AndroidKeyStore"

        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_RSA

        private const val CURRENT_BLOCK_MODE = KeyProperties.BLOCK_MODE_ECB
        private const val CURRENT_PADDING = KeyProperties.ENCRYPTION_PADDING_RSA_OAEP
        private const val MD_NAME = "SHA-256"
        private const val MGF_NAME = "MGF1"

        private val OAEPP_SPEC = OAEPParameterSpec(
            MD_NAME,
            MGF_NAME,
            MGF1ParameterSpec.SHA1,
            PSource.PSpecified.DEFAULT
        )


        private const val TRANSFORMATION = "$ALGORITHM/$CURRENT_BLOCK_MODE/$CURRENT_PADDING"

        private const val ALIAS_KEY = "ASYMMETRIC_ENCRYPTION_ALIAS_KEY"
        private const val KEY_SIZE = 2048
    }


    private val keystore = KeyStore.getInstance(KEYSTORE).apply {
        load(null)
    }

    private fun getEncryptCipher(publicKey: PublicKey): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.ENCRYPT_MODE,
                publicKey,
                OAEPP_SPEC
            )
        }
    }

    private fun getDecryptCipher(): Cipher {
        return Cipher.getInstance(TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                getAsymmetricKeyPair().private,
                OAEPP_SPEC
            )
        }
    }

    private fun getAsymmetricKeyPair(): KeyPair {
        return (keystore.getKey(ALIAS_KEY, null) as? PrivateKey)?.let { privateKey ->
            (keystore.getCertificate(ALIAS_KEY)?.publicKey)?.let { publicKey ->
                KeyPair(publicKey, privateKey)
            } ?: run {
                generateKeyPair()
            }
        } ?: run {
            generateKeyPair()
        }
    }

    private fun generateKeyPair(): KeyPair {
        return KeyPairGenerator.getInstance(ALGORITHM).apply {
            initialize(
                KeyGenParameterSpec.Builder(
                    ALIAS_KEY,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                .setKeySize(KEY_SIZE)
                .setDigests(KeyProperties.DIGEST_SHA256)
                .setBlockModes(CURRENT_BLOCK_MODE)
                .setEncryptionPaddings(CURRENT_PADDING)
                .build()
            )
        }.generateKeyPair()
    }

    fun getPublicKey(): String {
        return Base64.encodeToString(getAsymmetricKeyPair().public.encoded, Base64.DEFAULT)
    }

    fun encryptString(plainText: String, strPublicKey: String): String? {
        return try {
            val publicKey = stringToPublicKey(strPublicKey)
            val encryptCipher = getEncryptCipher(publicKey)
            val cipherText = encryptCipher.doFinal(plainText.toByteArray())
            return Base64.encodeToString(cipherText, Base64.DEFAULT)
        } catch (e: Exception) {
            Timber.e("Error: failed to encrypt string:\n$e")
            null
        }
    }

    private fun stringToPublicKey(strPublicKey: String): PublicKey {
        val keyBytes: ByteArray = Base64.decode(strPublicKey, Base64.DEFAULT)
        val spec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        return keyFactory.generatePublic(spec)
    }

    fun decryptString(strToDecode: String): String? {
        return try {
            val cipherText = Base64.decode(strToDecode, Base64.DEFAULT)
            val plainText = getDecryptCipher().doFinal(cipherText)
            return String(plainText, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Timber.e("Error: failed to decrypt string:\n$e")
            null
        }
    }
}