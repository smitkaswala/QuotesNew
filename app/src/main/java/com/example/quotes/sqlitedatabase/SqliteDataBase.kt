package com.example.quotes.sqlitedatabase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.quotes.models.QuotesItems

class SqliteDataBase(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context,DATABASE_NAME, factory ,DATABASE_VERSION) {

    companion object{

        private const val DATABASE_NAME = "QuotesData.DB"

        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "favorite_Image"

        const val ID_COL = "id"

        const val FULL_IMAGE = "image"

        const val CATEGORY_ID = "category_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                FULL_IMAGE + " TEXT," +
                CATEGORY_ID + " TEXT" + ")")

        db?.execSQL(query)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addName(id : Int , url : String, category : Int ){

        val db = this.writableDatabase

        val values = ContentValues()

        values.put(ID_COL,id)
        values.put(FULL_IMAGE, url)
        values.put(CATEGORY_ID, category)

        db.insert(TABLE_NAME, null, values)

        db.close()

    }

    @SuppressLint("Recycle")
    fun checkIfUserExit(id : String) : Boolean{

        val db = this.writableDatabase

        val where = "$ID_COL LIKE '%$id%'"

        val c : Cursor = db.query(true, TABLE_NAME, null,where, null, null, null, null, null)

        return c.count > 0

    }

    @SuppressLint("Recycle")
    fun deleteQuotes(id : String): Boolean {

        val db = this.writableDatabase

        val c : Cursor = db.rawQuery("Select *  from favorite_Image where id = ?",  arrayOf(id))

        return if (c.count > 0){

            val result : Long = db.delete(TABLE_NAME,"id=?",  arrayOf(id)).toLong()

            result == -1L

        }else{

            false

        }

    }

    @SuppressLint("Recycle")
    fun favoriteQuotes() : List<QuotesItems>{

        val db = this.readableDatabase

        val favoriteList : MutableList<QuotesItems> = ArrayList()

        val cursor : Cursor = db.rawQuery("select * from $TABLE_NAME", null,null)

        if (cursor.moveToFirst()){
            cursor.moveToFirst()
            do {
                val quotesItems = QuotesItems(cursor.getInt(0),cursor.getString(1),cursor.getInt(2))
                favoriteList.add(quotesItems)

            }
            while (cursor.moveToNext())

        }

        return favoriteList

    }

}