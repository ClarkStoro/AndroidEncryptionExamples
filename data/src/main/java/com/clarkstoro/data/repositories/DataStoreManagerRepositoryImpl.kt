package com.clarkstoro.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.clarkstoro.domain.repositories.DataStoreManagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManagerRepositoryImpl @Inject constructor(
    val context: Context
) : DataStoreManagerRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTINGS)

    override suspend fun storeEncryptedData(encryptedData: String) {
        val encryptedDataKey = stringPreferencesKey(ENCRYPTED_DATA_KEY)
        context.dataStore.edit { preferences -> preferences[encryptedDataKey] = encryptedData }
    }

    override suspend fun getEncryptedData(): Flow<String?> {
        val encryptedDataKey = stringPreferencesKey(ENCRYPTED_DATA_KEY)
        return context.dataStore.data.map { it[encryptedDataKey] }
    }

    companion object {
        private const val SETTINGS = "settings"
        private const val ENCRYPTED_DATA_KEY = "encrypted_data"
    }
}