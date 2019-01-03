package com.mviana.crudapp

import android.os.Bundle
import android.os.UserManager
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.mviana.crudapp.users.User

class FragmentTestActivity: FragmentActivity(), com.mviana.crudapp.users.UserManager {
    override val THEME_MODE = "appMode"
    override val THEME_NO_THEME = "noTheme"
    override val THEME_DAY = "day"
    override val THEME_NIGHT = "night"

    var onFilterListened = false


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = FrameLayout(this)
        content.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        content.id = R.id.users_display
        setContentView(content)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.users_display, fragment, "TEST")
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.users_display, fragment).commit()
    }

    override fun onUserSelected(user: User) {
    }

    override fun onAddUser() {
    }

    override fun onCloseAddEdit() {
    }

    override fun onUserSubmitted() {
    }

    override fun onUserEditedDeleted(action: String) {
    }

    override fun onFiltering(filter: CharSequence) {
    }

    override fun onUserListUpdated() {
    }

    override fun onRequestFailed(text: String) {
    }

    override fun onChangeAppTheme(code: Int) {
    }

    override fun onUndoSuccess() {
    }

}