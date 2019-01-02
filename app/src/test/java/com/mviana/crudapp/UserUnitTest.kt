package com.mviana.crudapp

import org.junit.Test
import com.mviana.crudapp.users.User
import org.junit.Assert.assertEquals
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UserUnitTest {

    val USER_UNIT_TEST_TAG: String = "UserUnitTest"
    val USER_UNIT_TEST_JSON: String ="{\n" +
            "  \"name\": \"Jessica Hyde\",\n" +
            "  \"birthdate\": \"2018-01-02T00:21:25\",\n" +
            "  \"id\": 24\n" +
            "}"
    val testname= "Jessica Hyde"
    val testid: Int = 24
    val testBirthDate = LocalDateTime.parse("2018-01-02T00:21:25")

    @Test
    fun testUserCreation(){

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

        //Test JSON constructor
        testuser = testuser.setWithJSON(USER_UNIT_TEST_JSON)
        assertEquals(testuser.name,testname)
        //Log.println(Log.INFO,USER_UNIT_TEST_TAG,"User name test passed")
        assertEquals(testuser.id,testid)
        //Log.println(Log.INFO,USER_UNIT_TEST_TAG,"User ID test passed")
        assertEquals(testuser.birthDate,testbirthdate)

    }

}