# Android Kotlin Encryption and Decryption Examples

This project is an Android application built to demonstrate encryption and decryption examples using AndroidKeyStore in Kotlin. The project utilizes Compose for UI, MVVM architecture, and Hilt for dependency injection.

## Features

The application consists of a bottom bar with four tabs:

1. **AES Encryption/Decryption**: Allows users to choose between AES/CBC/PKCS7 or AES/GCM/NOPADDING encryption methods. Users can also choose how to save the Initialization Vector (IV) of encryption: Append Mode or Byte Array Mode (utilizes CryptoManager class).

2. **AES Encryption/Decryption with DataStore**: Similar to the first tab but with the option to save the final result in Android DataStore (utilizes CryptoManager class).

3. **AES Encryption/Decryption with Biometrics**: Similar to the first tab but with the addition of biometric authentication during key configuration (utilizes BiometricCryptoManager class).

4. **RSA Encryption/Decryption with OAEPP**: Allows users to perform encryption/decryption using RSA and Optimal Asymmetric Encryption Padding (OAEPP). Users can choose which public key to use for encryption based on a provided Public Key in string format (utilizes AsymmetricCryptoManager class).

## Class Separation

The classes used to handle encryption and decryption are intentionally separated based on their purpose to make the example easier to understand.

## Installation

Clone the repository and open the project in Android Studio. Build and run the application on an Android device or emulator.

## Contributing

Feel free to contribute to this project by forking the repository, making your changes, and creating a pull request.

## License

This project is licensed under the [MIT License](https://github.com/clarkstoro/encryptionexample/blob/main/LICENSE).

## Presentation Slides

Slides related to this project will be available on GitHub at [Presentation Slides](https://github.com/ClarkStoro/AndroidEncryptionExamples/tree/main/slides).

## Contact

For any questions or feedback, please contact [Emanuele Maso](https://github.com/clarkstoro).
