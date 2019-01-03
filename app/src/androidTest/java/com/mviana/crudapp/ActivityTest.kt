package com.mviana.crudapp

import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith;
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.mviana.crudapp.ui.main.ActionsFragment
import com.mviana.crudapp.ui.main.UsersFragment
import org.hamcrest.Matchers.instanceOf
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat
import org.junit.Before





@MediumTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ActivityTest {


    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var actionFragment: ActionsFragment
    private lateinit var usersFragment: UsersFragment



    @Before
    fun init() {
        actionFragment = activityRule.activity.supportFragmentManager.findFragmentById(R.id.actions_fragment) as ActionsFragment
        usersFragment = UsersFragment.newInstance()
    }

    @Test
    @Throws(Exception::class)
    fun ensureViewsArePresent() {
        val activity = activityRule.activity
        val containerView = activity.findViewById<ConstraintLayout>(R.id.container)
        assertThat(containerView, notNullValue())
        assertThat(containerView, instanceOf(ConstraintLayout::class.java))

        assertThat(actionFragment, notNullValue())

        activityRule.activity.supportFragmentManager.beginTransaction()
            .add(R.id.users_display, usersFragment, null).commit()

        var retrievedUsersFragment = activity.supportFragmentManager.findFragmentById(R.id.users_display)
        assertThat(retrievedUsersFragment, instanceOf(UsersFragment::class.java))

    }


}