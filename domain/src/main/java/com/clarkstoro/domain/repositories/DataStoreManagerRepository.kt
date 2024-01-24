package com.clarkstoro.domain.repositories

import kotlinx.coroutines.flow.Flow

interface DataStoreManagerRepository {
    suspend fun storeEncryptedData(encryptedData: String)

    suspend fun getEncryptedData(): Flow<String?>
}