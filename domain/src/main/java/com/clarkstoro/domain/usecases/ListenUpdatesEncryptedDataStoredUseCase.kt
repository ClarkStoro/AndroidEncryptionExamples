package com.clarkstoro.domain.usecases

import com.clarkstoro.domain.repositories.DataStoreManagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ListenUpdatesEncryptedDataStoredUseCase @Inject constructor(private val repository: DataStoreManagerRepository) {
    suspend operator fun invoke(): Flow<String?> =
        try {
            repository.getEncryptedData()
        } catch (e: Exception) {
            flowOf(null)
        }
}