package com.clarkstoro.androidencryptionexamples.presentation.asymmetric_encrypt_decrypt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.clarkstoro.androidencryptionexamples.R
import com.clarkstoro.androidencryptionexamples.presentation.common.ActionButtons
import com.clarkstoro.androidencryptionexamples.presentation.common.CommonInputText
import com.clarkstoro.androidencryptionexamples.presentation.common.CopyToClipboardButton
import com.clarkstoro.androidencryptionexamples.presentation.common.InputEncryptionDecryption
import com.clarkstoro.androidencryptionexamples.presentation.common.ResultInput
import com.clarkstoro.androidencryptionexamples.presentation.common.TitleScreen

@Composable
fun AsymmetricEncryptDecryptScreen(viewModel: AsymmetricCryptoViewModel) {

    val personalPublicKey = viewModel.getPersonalPublicKey()
    val textResult = viewModel.cipherTextResultFlow.collectAsStateWithLifecycle().value

    var textToEncryptDecrypt by remember {
        mutableStateOf("")
    }
    var publicKeyForEncryption by remember {
        mutableStateOf(personalPublicKey) // use the user's public key just for example purpose
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            Spacer(modifier = Modifier.height(20.dp))
            TitleScreen(title = stringResource(id = R.string.bottom_nav_page4))
            Spacer(modifier = Modifier.height(20.dp))

            CommonInputText(
                isReadOnly = true,
                label = stringResource(id = R.string.personal_public_key_hint),
                value = personalPublicKey
            )
            Spacer(modifier = Modifier.height(10.dp))
            CopyToClipboardButton(personalPublicKey)

            Spacer(modifier = Modifier.height(20.dp))

            InputEncryptionDecryption(
                textToEncryptDecrypt = textToEncryptDecrypt,
                onValueChange = { textToEncryptDecrypt = it },
            )
            CommonInputText(
                isReadOnly = false,
                label = stringResource(id = R.string.public_key_to_encrypt),
                value = publicKeyForEncryption,
                onValueChange = {
                    publicKeyForEncryption = it
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            ActionButtons(
                onEncrypt = {
                    viewModel.encryptText(textToEncryptDecrypt, publicKeyForEncryption)
                },
                onDecrypt = {
                    viewModel.decryptText(textToEncryptDecrypt)
                }
            )
            Spacer(modifier = Modifier.height(18.dp))

            ResultInput(value = textResult)
            Spacer(modifier = Modifier.height(10.dp))

            CopyToClipboardButton(textResult)
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}

