package com.mviana.crudapp

import android.util.Log
import org.junit.Test
import com.mviana.crudapp.users.User
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UserUnitTest {

    var USER_UNIT_TEST_TAG: String = "UserUnitTest"

    @Test
    fun testUserCreation(){
        var testname= "Jessica Hyde"
        var testid: Int = Math.random().toInt()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        var testbirthdate = LocalDateTime.now()

        var testuser = User()
        testuser.name = testname
        testuser.id = testid
        testuser.birthDate = testbirthdate

        //Test testuser fields are set to expected
        assertEquals(testuser.name,testname)
        //Log.println(Log.INFO,USER_UNIT_TEST_TAG,"User name test passed")
        assertEquals(testuser.id,testid)
        //Log.println(Log.INFO,USER_UNIT_TEST_TAG,"User ID test passed")
        assertEquals(testuser.birthDate,testbirthdate)
        //Log.println(Log.INFO,USER_UNIT_TEST_TAG,"User birthdate test passed")

        //Test JSON


    }

}