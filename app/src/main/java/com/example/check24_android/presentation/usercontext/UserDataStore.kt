package com.example.check24_android.presentation.usercontext

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.userDataStore by preferencesDataStore(name = "user_context")

data class UserContext(val userId: Int, val userName: String)

class UserContextRepo(context: Context) {

  private val dataStore = context.userDataStore

  private val KEY_USER_ID = intPreferencesKey("user_id")
  private val KEY_USER_NAME = stringPreferencesKey("userName")

  // Flow for live updates (Dashboard reacts automatically)
  val userFlow: Flow<UserContext> = dataStore.data.map { prefs ->
    UserContext(
      userId = prefs[KEY_USER_ID] ?: 123,
      userName = prefs[KEY_USER_NAME] ?: "Ahmed"
    )
  }

  suspend fun getOrInit(): UserContext {
    val prefs = dataStore.data.first()
    val id = prefs[KEY_USER_ID]
    val name = prefs[KEY_USER_NAME]

    if (id == null || name == null) {
      val default = UserContext(userId = 123, userName = "Ahmed")
      dataStore.edit {
        it[KEY_USER_ID] = default.userId
        it[KEY_USER_NAME] = default.userName
      }
      return default
    }

    return UserContext(userId = id, userName = name)
  }

  suspend fun switchUser(userId: Int, userName: String) {
    dataStore.edit {
      it[KEY_USER_ID] = userId
      it[KEY_USER_NAME] = userName
    }
  }
}
