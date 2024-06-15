package com.clarkstoro.androidencryptionexamples.presentation.symmetric_storage_cryptography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.clarkstoro.androidencryptionexamples.presentation.SymmetricCryptographyCommonViewModel
import com.clarkstoro.androidencryptionexamples.presentation.common.ActionButtons
import com.clarkstoro.androidencryptionexamples.presentation.common.AppDefaultButton
import com.clarkstoro.androidencryptionexamples.presentation.common.CommonInputText
import com.clarkstoro.androidencryptionexamples.presentation.common.CopyToClipboardButton
import com.clarkstoro.androidencryptionexamples.presentation.common.InputEncryptionDecryption
import com.clarkstoro.androidencryptionexamples.presentation.common.IvModeSelector
import com.clarkstoro.androidencryptionexamples.presentation.common.ResultInput
import com.clarkstoro.androidencryptionexamples.presentation.common.TitleScreen

/**
 * TAB 2: Symmetric Encryption / Decryption with Datastore
 */
@Composable
fun SymmetricStorageScreen(viewModel: SymmetricCryptographyCommonViewModel) {

    val textResult = viewModel.cipherTextResultFlow.collectAsStateWithLifecycle().value
    val currentEncryptedStoredValue = viewModel.currentEncryptedStoredValueFlow.collectAsStateWithLifecycle().value

    val modesAvailable = SymmetricCryptographyCommonViewModel.CryptMode.entries

    var selectedMode: SymmetricCryptographyCommonViewModel.CryptMode? by remember {
        mutableStateOf(modesAvailable.firstOrNull())
    }

    var textToEncryptDecrypt by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.listenEncryptedDataStored()
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
            TitleScreen(title = stringResource(id = R.string.bottom_nav_page2))
            Spacer(modifier = Modifier.height(20.dp))

            CommonInputText(
                isReadOnly = true,
                label = stringResource(id = R.string.stored_value_hint),
                value = currentEncryptedStoredValue
            )

            Spacer(modifier = Modifier.height(10.dp))

            CopyToClipboardButton(textToCopy = currentEncryptedStoredValue)
            Spacer(modifier = Modifier.height(20.dp))

            IvModeSelector(
                selectedMode,
                onModeSelected = {
                    selectedMode = it
                }
            )

            InputEncryptionDecryption(
                textToEncryptDecrypt,
                onValueChange = {
                    textToEncryptDecrypt = it
                }
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

            ResultInput(
                value = textResult
            )
            Spacer(modifier = Modifier.height(10.dp))

            AppDefaultButton(
                textId = R.string.btn_store_encrypted_value
            ) {
                viewModel.saveEncryptedData(textResult)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}