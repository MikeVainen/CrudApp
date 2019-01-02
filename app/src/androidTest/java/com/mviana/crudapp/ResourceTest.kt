package com.mviana.crudapp

import android.content.Context
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by mviana on 20/12/18.
 */
class ResourceTest {

    @Test
    fun testGetResources(){
        val ctx = Mockito.mock(Context::class.java)
        val mockedResources = ctx.resources
        var sampleNames: Array<String> = mockedResources.getStringArray(R.array.sample_users)
        System.out.println("Tamaño: " + sampleNames.size.toString())

    }
}