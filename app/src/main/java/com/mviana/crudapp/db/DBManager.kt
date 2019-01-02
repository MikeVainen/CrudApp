package com.mviana.crudapp.db

import android.content.Context
import com.mviana.crudapp.users.User

class DBManager(var context: Context) {

    private var db = UserDBHelper(context, null, null, 1)

    fun getCurrentUsers(): ArrayList<User>{
        return db.getAllUsers()
    }

    fun deleteCurrentUsers(){
        db.cleanUserTable()
    }

    fun addUsers( newusers :ArrayList<User> ){
        db.addUsers(newusers)
    }

    fun editUsers(editedUsers: ArrayList<User>){
        db.editUsers(editedUsers)
    }

}