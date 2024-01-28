package com.clarkstoro.encryptionexample.presentation.asymmetric_encrypt_decrypt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.clarkstoro.encryptionexample.R
import com.clarkstoro.encryptionexample.presentation.common.ActionButtons
import com.clarkstoro.encryptionexample.presentation.common.CommonInputText
import com.clarkstoro.encryptionexample.presentation.common.CopyToClipboardButton
import com.clarkstoro.encryptionexample.presentation.common.InputEncryptionDecryption
import com.clarkstoro.encryptionexample.presentation.common.ResultInput
import com.clarkstoro.encryptionexample.presentation.common.TitleScreen

@Composable
fun AsymmetricEncryptDecryptScreen(viewModel: AsymmetricCryptoViewModel) {

    val personalPublicKey = viewModel.getPersonalPublicKey()
    val textResult = viewModel.cipherTextResultFlow.collectAsState().value

    var textToEncryptDecrypt by remember {
        mutableStateOf("")
    }
    var publicKeyForEncryption by remember {
        mutableStateOf(personalPublicKey) // use the user's public key just for example purpose
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            TitleScreen(title = stringResource(id = R.string.bottom_nav_page4))
            Spacer(modifier = Modifier.height(20.dp))

            CommonInputText(
                isReadOnly = true,
                label = stringResource(id = R.string.personal_public_key_hint),
                value = personalPublicKey
            )
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
        }

    }
}
