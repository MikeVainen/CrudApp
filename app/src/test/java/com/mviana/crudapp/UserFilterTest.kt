package com.mviana.crudapp

import android.content.Context
import android.content.res.Resources
import android.util.Log

import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import org.mockito.Mockito.`when`
import org.mockito.Mockito





@RunWith(MockitoJUnitRunner::class)
class UserFilterTest {

    private val TEST_USER_NAMES = arrayOf("Ana","Berto","Carla", "Diego","Elena","Fernando","Gemma","Hugo")
    private val TEST_USER_FILTER = "an"
    private val TEST_FILTERED_NAMES = arrayOf("Ana","Fernando")


    /**
     * The following test expects to filter the members in TEST_USER_NAMES and preserve only two (Ana & Fernando)
     * which contain the TEST_USER_FILTER Query condition
     */
    @Test
    fun filterSampleUsers() {

        var resources = Mockito.mock<Resources>(Resources::class.java)
        `when`(resources.getStringArray(R.array.sample_users)).thenReturn(TEST_USER_NAMES)

        var sampleNames = TEST_USER_NAMES.toCollection(ArrayList())

        var filteredUsers = ArrayList<String>()
        if (!TEST_USER_FILTER.isEmpty()) {
            for (name in sampleNames) {
                if (name.toLowerCase().contains(TEST_USER_FILTER.toLowerCase())) {
                    filteredUsers.add(name)
                }
            }
        }
        else filteredUsers = sampleNames.toCollection(ArrayList())
        assert(filteredUsers.isNotEmpty())
        assert(filteredUsers.size>1)
        assert(filteredUsers.size == TEST_FILTERED_NAMES.size)
        for(filteredUser in filteredUsers){
            assert(TEST_FILTERED_NAMES.contains(filteredUser))
        }

    }




}