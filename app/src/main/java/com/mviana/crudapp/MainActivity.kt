package com.mviana.crudapp

import android.app.Activity
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import androidx.fragment.app.FragmentActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentTransaction
import com.mviana.crudapp.api.RequestPerformer
import com.mviana.crudapp.api.ServiceRequest
import com.mviana.crudapp.db.UserDBHelper
import com.mviana.crudapp.ui.main.UsersFragment
import com.mviana.crudapp.ui.main.AddUserFragment
import com.mviana.crudapp.ui.main.UserManageFragment
import com.mviana.crudapp.users.User
import com.mviana.crudapp.users.UserManager
import kotlinx.android.synthetic.main.main_activity.*
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener


class MainActivity : FragmentActivity(), UserManager  {

    var MAIN_ACT_TAG = "MainActivity"
    var TRANSITION_FAB = "add_transition"
    var TRANSITION_INITIAL = "init_transition"
    var TRANSITION_UPDATE = "upd_transition"

    override val THEME_MODE = "appMode"
    override val THEME_NO_THEME = "noTheme"
    override val THEME_DAY = "day"
    override val THEME_NIGHT = "night"

    val PREFS_SHOW_GUIDE = "showGuide"
    val PREFS_DISMISS_GUIDE = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
        when(prefs.getString(THEME_MODE, THEME_NO_THEME)){
            THEME_DAY ->{
                setTheme(R.style.AppTheme_COFFEE_LIGHT)
            }
            THEME_NIGHT ->{
                setTheme(R.style.AppTheme_COFFEE_DARK)
            }
            else -> prefs.edit().putString(THEME_MODE, THEME_DAY).apply()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.users_display, UsersFragment.newInstance())
                .commitNow()
        }
    }

    override fun onStart() {
        super.onStart()
        val prefs = getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
        if(prefs.getBoolean(PREFS_SHOW_GUIDE, true )){
            showCaseView(resources.getString(R.string.welcome),
                resources.getString(R.string.guide), R.id.container, 1, -80f)
        }
    }

    override fun onChangeAppTheme(code: Int){
        Log.println(Log.ERROR,"onChangeAppTheme",code.toString())
        val prefs = getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
        when(code){
            0 ->{
                prefs.edit().putString(THEME_MODE, THEME_DAY ).apply()
                finish()
                startActivity(intent)
            }
            1 ->{
                prefs.edit().putString(THEME_MODE, THEME_NIGHT ).apply()
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onFiltering(filter: CharSequence) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.abc_shrink_fade_out_from_bottom,R.anim.abc_grow_fade_in_from_bottom)
            .replace(R.id.users_display, UsersFragment.newInstance(filter.toString()))
            .commitNow()
    }

    override fun onUserSelected(user: User) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.users_display)
        if (currentFragment != null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.setCustomAnimations(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top)
            ft.replace(R.id.users_display,UserManageFragment.newInstance(user))
            ft.commit()
        }
        else Log.println(Log.ERROR,"onUserSelected","No existe ese fragmento")
    }

    override fun onAddUser() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(R.id.users_display, AddUserFragment.newInstance(1))
            .commit()
    }

    override fun onCloseAddEdit() {
        supportFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(R.id.users_display, UsersFragment.newInstance())
            .commitNow()
    }

    override fun onUserSubmitted() {
        val context = applicationContext
        runOnUiThread{
                Toast.makeText(context,resources.getString(R.string.add_user)+ " " + resources.getString(R.string.successful),
                    Toast.LENGTH_LONG).show()
            }
    }

    override fun onUserListUpdated(){
        val context = applicationContext
        runOnUiThread{
            Toast.makeText(context,resources.getString(R.string.update_users)+ " " + resources.getString(R.string.successful),
                Toast.LENGTH_LONG).show()
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.users_display, UsersFragment.newInstance())
                .commitNow()
        }

    }

    override fun onUserEditedDeleted(action: String) {
        val context = applicationContext
        runOnUiThread {
            Toast.makeText(
                context, action + " " + resources.getString(R.string.successful),
                Toast.LENGTH_LONG
            ).show()
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.users_display, UsersFragment.newInstance())
                .commitNow()

        }
    }
    override fun onRequestFailed(text: String){
        val context = applicationContext
        runOnUiThread {
            Toast.makeText(
                context, text + ". " + resources.getString(R.string.failed),
                Toast.LENGTH_LONG
            ).show()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.abc_slide_out_top,R.anim.abc_slide_in_bottom)
                .replace(R.id.users_display, UsersFragment.newInstance())
                .commitNow()
        }
    }

    override fun onUndoSuccess() {
        val context = applicationContext
        runOnUiThread{
            Toast.makeText(context,resources.getString(R.string.undo)+ " " + resources.getString(R.string.successful),
                Toast.LENGTH_LONG).show()
        }

    }

    private fun showCaseView(title:String,text: String, viewId:Int, type:Int, height: Float){


        GuideView.Builder(this)
            .setTitle(title)
            .setContentText(text)
            .setTargetView(findViewById(viewId))
            .setGravity(Gravity.center)
            .setIndicatorHeight(height)
            .setDismissType(DismissType.anywhere)
            .setGuideListener(object: GuideListener {
                override fun onDismiss(p0: View?) {
                    when (type) {
                        1 -> {
                            showCaseView(
                                resources.getString(R.string.actions_title),
                                resources.getString(R.string.actions_guide),
                                R.id.actions_fragment,
                                2, 80f
                            )
                        }
                        2 -> {
                            showCaseView(
                                resources.getString(R.string.users_title),
                                resources.getString(R.string.users_guide),
                                R.id.users_display, 3,-80f
                            )
                        }
                        3 ->{showCaseView(
                            resources.getString(R.string.add_user),resources.getString(R.string.adduser_guide),
                                R.id.add, 4, 80f)

                        }
                        4 ->{showCaseView(
                            resources.getString(R.string.update_users), resources.getString(R.string.updateusers_guide),
                            R.id.updateUsersButton, 5,80f)

                        }
                        5 -> {
                            showCaseView(
                                resources.getString(R.string.thatsall_title),
                                resources.getString(R.string.thatsall_guide), R.id.container, 6, -80f)
                            getSharedPreferences(resources.getString(R.string.PREFS_NAME), 0)
                                .edit().putBoolean(PREFS_SHOW_GUIDE, PREFS_DISMISS_GUIDE).apply()

                        }
                    }
                }

            })
            .build()
            .show()

    }





}