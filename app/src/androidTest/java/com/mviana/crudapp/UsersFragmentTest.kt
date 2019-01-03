package com.mviana.crudapp

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.mviana.crudapp.ui.main.ActionsFragment
import com.mviana.crudapp.ui.main.UsersFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class UsersFragmentTest {

    @get:Rule
    val testActivityRule = ActivityTestRule(FragmentTestActivity::class.java, true, true)

    lateinit var usersFrag: UsersFragment
    lateinit var actionsFraq: ActionsFragment


    @Before
    fun testSetUp() {
        usersFrag = UsersFragment.newInstance()
        actionsFraq = ActionsFragment.newInstance(0)
        testActivityRule.activity.setFragment(actionsFraq)
        testActivityRule.activity.setFragment(usersFrag)
    }

    @Test
    fun testUserFragment(){


        val userFilter = "An"
        //verify that fragment can be replaced/updated when requested
        usersFrag = UsersFragment.newInstance(userFilter)
        testActivityRule.activity.replaceFragment(usersFrag)
        val userFragArgs = usersFrag.arguments!!
        assert(userFilter == userFragArgs["filter"].toString())
    }

    @Test
    fun testActionsFragment(){

    }



}