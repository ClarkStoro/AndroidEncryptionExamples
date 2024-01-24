package com.clarkstoro.domain.usecases

import com.clarkstoro.domain.repositories.DataStoreManagerRepository
import javax.inject.Inject

class StoreEncryptedDataUseCase @Inject constructor(private val repository: DataStoreManagerRepository) {
    suspend operator fun invoke(encryptedData: String): Unit =
        try {
            repository.storeEncryptedData(encryptedData)
        } catch (e: Exception) {

        }
}