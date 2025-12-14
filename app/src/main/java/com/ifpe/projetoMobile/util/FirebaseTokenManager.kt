package com.projetomobile.deolhonaconsulta.util

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseTokenManager {

    private const val PREFS_NAME = "AppPreferences"
    private const val TOKEN_KEY = "firebase_token"

    fun getToken(context: Context, onTokenReceived: (String?) -> Unit) {
        val savedToken = getSavedToken(context)
        if (savedToken != null) {
            onTokenReceived(savedToken)
        } else {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val token = task.result
                        saveToken(context, token)
                        onTokenReceived(token)
                    } else {
                        Log.e("FirebaseToken", "Erro ao obter o token", task.exception)
                        onTokenReceived(null)
                    }
                }
        }
    }

    private fun saveToken(context: Context, token: String?) {
        token?.let {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString(TOKEN_KEY, it).apply()
        }
    }
    fun updateToken(context: Context, token: String?) {
        saveToken(context, token)
        Log.d("FirebaseToken", "Token atualizado: $token")
    }

    private fun getSavedToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOKEN_KEY, null)
    }
}
