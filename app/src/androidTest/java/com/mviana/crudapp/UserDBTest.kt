package com.mviana.crudapp

import android.content.Context
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import androidx.test.core.app.*

import com.mviana.crudapp.db.UserDBHelper
import com.mviana.crudapp.users.User
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4ClassRunner::class)
class SimpleEntityReadWriteTest {

    private lateinit var db: UserDBHelper
    private val USER_UNIT_TEST_JSON: String ="{\n" +
            "  \"name\": \"Jessica Hyde\",\n" +
            "  \"birthdate\": \"2018-01-02T00:21:25\",\n" +
            "  \"id\": 24\n" +
            "}"
    private val USER_NAME = "Jessica Hyde"

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = UserDBHelper(context, null, null, 1)

    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user: User = User().setWithJSON(USER_UNIT_TEST_JSON)
        var userList = ArrayList<User>()
        userList.add(user)
        assert(userList.isNotEmpty())
        db.cleanUserTable()
        assert( db.getAllUsers().isEmpty() )
        db.addUsers(userList)
        val retrievedUsers = db.getAllUsers()
        assert(retrievedUsers.isNotEmpty())
        var sampleUser = retrievedUsers[0]
        assert(sampleUser.name == USER_NAME)

    }

    @Test
    @Throws
    fun testEditUser(){
        var user: User = User().setWithJSON(USER_UNIT_TEST_JSON)
        var userList = ArrayList<User>()
        userList.add(user)
        assert(userList.isNotEmpty())
        db.cleanUserTable()
        assert( db.getAllUsers().isEmpty() )
        db.addUsers(userList)
        val retrievedUsers = db.getAllUsers()
        assert( retrievedUsers.isNotEmpty() )
        db.editUsers(userList)
        assert(db.getAllUsers().size == userList.size)

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


}