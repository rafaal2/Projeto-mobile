// ConsultaDao.kt
package com.ifpe.projetomobile.deolhonaconsulta.data.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class ConsultaDao(context: Context) {
    private val dbHelper = DB(context)

    fun insert(codSolicitacao: String): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("cod_solicitacao", codSolicitacao)
        }
        val result = db.insert("consulta", null, values)
        db.close()
        return result
    }

    fun getAll(): List<Consulta> {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<Consulta>()
        val cursor: Cursor = db.query(
            "consulta",
            arrayOf("id", "cod_solicitacao"),
            null,
            null,
            null,
            null,
            "id DESC"  // Exemplo: últimos primeiro
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val cod = cursor.getString(cursor.getColumnIndexOrThrow("cod_solicitacao"))
            list.add(Consulta(id, cod))
        }
        cursor.close()
        db.close()
        return list
    }

    fun exists(codSolicitacao: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            "consulta",
            arrayOf("id"),
            "cod_solicitacao = ?",
            arrayOf(codSolicitacao),
            null,
            null,
            null
        )
        val exists = cursor.moveToFirst()
        cursor.close()
        db.close()
        return exists
    }
}
