package com.clarkstoro.androidencryptionexamples.presentation.encrypt_decrypt

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
import com.clarkstoro.androidencryptionexamples.presentation.CommonViewModel
import com.clarkstoro.androidencryptionexamples.presentation.common.ActionButtons
import com.clarkstoro.androidencryptionexamples.presentation.common.CopyToClipboardButton
import com.clarkstoro.androidencryptionexamples.presentation.common.InputEncryptionDecryption
import com.clarkstoro.androidencryptionexamples.presentation.common.IvModeSelector
import com.clarkstoro.androidencryptionexamples.presentation.common.ResultInput
import com.clarkstoro.androidencryptionexamples.presentation.common.TitleScreen

@Composable
fun EncryptDecryptScreen(viewModel: CommonViewModel) {
    val textResult = viewModel.cipherTextResultFlow.collectAsStateWithLifecycle().value

    val modesAvailable = CommonViewModel.CryptMode.entries

    var selectedMode: CommonViewModel.CryptMode? by remember {
        mutableStateOf(modesAvailable.firstOrNull())
    }

    var textToEncryptDecrypt by remember {
        mutableStateOf("")
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
            TitleScreen(title = stringResource(id = R.string.bottom_nav_page1))
            Spacer(modifier = Modifier.height(20.dp))

            IvModeSelector(selectedMode, onModeSelected = {
                selectedMode = it
            })

            InputEncryptionDecryption(
                textToEncryptDecrypt = textToEncryptDecrypt,
                onValueChange = { textToEncryptDecrypt = it },
            )
            Spacer(modifier = Modifier.height(10.dp))

            ActionButtons(
                selectedMode = selectedMode,
                onEncryptAppendMode = {
                    viewModel.encryptTextAppendingMode(textToEncryptDecrypt)
                },
                onDecryptAppendMode = {
                    viewModel.decryptAppendingMode(textToEncryptDecrypt)
                },
                onEncryptByteArrayMode = {
                    viewModel.encryptTextArrayMode(textToEncryptDecrypt)
                },
                onDecryptByteArrayMode = {
                    viewModel.decryptArrayMode(textToEncryptDecrypt)
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

