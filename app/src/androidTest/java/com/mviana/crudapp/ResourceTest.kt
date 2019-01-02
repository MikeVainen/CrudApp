package com.mviana.crudapp

import android.content.Context
import android.content.res.Resources
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
/**
 * Created by mviana on 20/12/18.
 */
class ResourceTest {

    @Mock
    lateinit var testResources: Resources

    @Before
    fun setUpResources(){
        testResources = Mockito.mock<Resources>(Resources::class.java)
    }

    @Test
    private fun testGetResources(){

    }

    @After
    fun onFinish(){

    }
}