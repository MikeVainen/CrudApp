package com.mviana.crudapp.db

import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.mviana.crudapp.users.User
import java.io.File
import java.time.LocalDateTime

/**
 * Extends MySQLiteDBHelper
 */


@TargetApi(28)
class UserDBHelper (context: Context, name: String?,
                    factory: SQLiteDatabase.CursorFactory?,
                    version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    companion object {

        private val DATABASE_NAME = "usersDB.db"
        private val DATABASE_VERSION: Int = 1
        private val DATABASE_DIR = "BDD" + File.separator
        private val USER_TABLE_NAME = "Users"
        private val USER_COLUMN_NAME = "name"
        private val USER_COLUMN_ID = "id"
        private val USER_COLUMN_BDAY = "birthdate"

    }

    private lateinit var userDBInstance: UserDBHelper
    private val DB_LOCATION = File(context.filesDir, DATABASE_DIR + DATABASE_NAME)


    fun getUserDBInstance(ctx: Context): UserDBHelper{
        if (userDBInstance==null){
            userDBInstance = UserDBHelper(ctx, DATABASE_NAME,null, DATABASE_VERSION)
        }
        return userDBInstance
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.println(Log.ERROR,"DB onCreate","llamado")
        db.beginTransaction()
        val CREATE_DATA_TABLE = "CREATE TABLE " + USER_TABLE_NAME +
                "( " +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                USER_COLUMN_NAME + " TEXT NOT NULL, " +
                USER_COLUMN_BDAY + " TEXT NOT NULL" +

                " )"
        db.execSQL(CREATE_DATA_TABLE)
        db.setTransactionSuccessful()
        db.endTransaction()
        Log.println(Log.ERROR,"DB onCreate","Finalizado")


    }

    override fun onUpgrade(
        db: SQLiteDatabase, oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME)
        onCreate(db)

    }

    //return the number of stored users
    fun addUsers(users: ArrayList<User>): Int{
        val storedUsers = 0
        val userValues = ContentValues()
        val db = this.writableDatabase
        for (user in users){
            db.beginTransaction()
            userValues.put(USER_COLUMN_NAME,user.name)
            userValues.put(USER_COLUMN_BDAY,user.birthDate.toString())
            userValues.put(USER_COLUMN_ID,user.id)
            db.insert(USER_TABLE_NAME, null, userValues)
            db.setTransactionSuccessful()
            db.endTransaction()
            storedUsers.inc()
        }
        db.close()
        Log.println(Log.ERROR,"addUsers", "usuarios introducidos")
        return storedUsers

    }

    fun editUsers(users: ArrayList<User>): Int{
        val editedUsers = 0
        val userValues = ContentValues()
        val db = this.writableDatabase
        for (user in users){
            db.beginTransaction()
            userValues.put(USER_COLUMN_NAME,user.name)
            userValues.put(USER_COLUMN_BDAY,user.birthDate.toString())
            userValues.put(USER_COLUMN_ID,user.id)
            db.replaceOrThrow(USER_TABLE_NAME, null, userValues)
            db.setTransactionSuccessful()
            db.endTransaction()
            editedUsers.inc()
        }
        db.close()
        Log.println(Log.ERROR,"addUsers", "usuarios editados")
        return editedUsers

    }

    fun deleteUsers(users: ArrayList<User>): Int{
        val deletedUsers = 0
        val userValues = ContentValues()
        val db = this.writableDatabase
        for (user in users){
            db.beginTransaction()
            userValues.put(USER_COLUMN_NAME,user.name)
            userValues.put(USER_COLUMN_BDAY,user.birthDate.toString())
            userValues.put(USER_COLUMN_ID,user.id)
            db.delete(USER_TABLE_NAME," ",null)
            db.setTransactionSuccessful()
            db.endTransaction()
            deletedUsers.inc()
        }
        db.close()
        Log.println(Log.ERROR,"addUsers", "usuarios borrados")
        return deletedUsers
    }

    fun getAllUsers():ArrayList<User>{
        var allUsers = ArrayList<User>()
        val db = this.writableDatabase
        db.beginTransaction()
        var cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME,null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val cursorUser = User()
                cursorUser.id = cursor.getInt(cursor.getColumnIndex(USER_COLUMN_ID))
                cursorUser.name = cursor.getString(cursor.getColumnIndex(USER_COLUMN_NAME))
                cursorUser.birthDate = LocalDateTime.parse(cursor.getString(cursor.getColumnIndex(USER_COLUMN_BDAY)))
                allUsers.add(cursorUser)
                cursor.moveToNext()
            }
        }
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
        return allUsers
    }

    fun cleanUserTable(){
        val db = this.writableDatabase
        db.beginTransaction()
        db.execSQL("DELETE FROM "+ USER_TABLE_NAME)
        db.setTransactionSuccessful()
        db.endTransaction()
        db.close()
    }
}

