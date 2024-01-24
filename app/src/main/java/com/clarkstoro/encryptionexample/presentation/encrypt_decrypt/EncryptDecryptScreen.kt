package com.clarkstoro.encryptionexample.presentation.encrypt_decrypt

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.clarkstoro.encryptionexample.R
import com.clarkstoro.encryptionexample.presentation.CommonViewModel
import com.clarkstoro.encryptionexample.presentation.common.ActionButtons
import com.clarkstoro.encryptionexample.presentation.common.CopyToClipboardButton
import com.clarkstoro.encryptionexample.presentation.common.InputEncryptionDecryption
import com.clarkstoro.encryptionexample.presentation.common.IvModeSelector
import com.clarkstoro.encryptionexample.presentation.common.ReadOnlyInput
import com.clarkstoro.encryptionexample.presentation.common.TitleScreen
import com.clarkstoro.encryptionexample.ui.theme.Dimens
import com.clarkstoro.encryptionexample.ui.theme.Orange500
import com.clarkstoro.encryptionexample.ui.theme.Teal200

@Composable
fun EncryptDecryptScreen(viewModel: CommonViewModel) {
    val textResult = viewModel.cipherTextResultFlow.collectAsState().value

    val modesAvailable = CommonViewModel.CryptMode.entries

    var selectedMode: CommonViewModel.CryptMode? by remember {
        mutableStateOf(modesAvailable.firstOrNull())
    }

    var textToEncryptDecrypt by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleScreen(title = stringResource(id = R.string.bottom_nav_page1))
        Spacer(modifier = Modifier.height(20.dp))

        IvModeSelector(selectedMode, onModeSelected = {
            selectedMode = it
        })

        InputEncryptionDecryption(
            textToEncryptDecrypt = textToEncryptDecrypt,
            onValueChange = { textToEncryptDecrypt = it },
            //onClear = { textToEncryptDecrypt = it}
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

        ReadOnlyInput(value = textResult)
        Spacer(modifier = Modifier.height(10.dp))

        CopyToClipboardButton(textResult)
    }
}

