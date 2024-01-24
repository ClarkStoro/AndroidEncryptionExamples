package com.clarkstoro.encryptionexample.presentation.save_retrieve

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.clarkstoro.encryptionexample.R
import com.clarkstoro.encryptionexample.presentation.CommonViewModel
import com.clarkstoro.encryptionexample.presentation.common.ActionButtons
import com.clarkstoro.encryptionexample.presentation.common.BoilerplateDefaultButton
import com.clarkstoro.encryptionexample.presentation.common.CopyToClipboardButton
import com.clarkstoro.encryptionexample.presentation.common.InputEncryptionDecryption
import com.clarkstoro.encryptionexample.presentation.common.IvModeSelector
import com.clarkstoro.encryptionexample.presentation.common.ReadOnlyInput
import com.clarkstoro.encryptionexample.presentation.common.TitleScreen

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SaveRetrieveScreen(viewModel: CommonViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val textResult = viewModel.cipherTextResultFlow.collectAsState().value
    val currentEncryptedStoredValue = viewModel.currentEncryptedStoredValueFlow.collectAsState().value

    var dropdownExpandedState by remember { mutableStateOf(false) }

    val modesAvailable = CommonViewModel.CryptMode.entries

    var selectedMode: CommonViewModel.CryptMode? by remember {
        mutableStateOf(modesAvailable.firstOrNull())
    }

    var textToEncryptDecrypt by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.listenEncryptedDataStored()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleScreen(title = stringResource(id = R.string.bottom_nav_page2))
        Spacer(modifier = Modifier.height(20.dp))

        ReadOnlyInput(
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

        ReadOnlyInput(
            value = textResult
        )
        Spacer(modifier = Modifier.height(10.dp))

        BoilerplateDefaultButton(
            textId = R.string.btn_store_encrypted_value
        ) {
            viewModel.saveEncryptedData(textResult)
        }
    }
}