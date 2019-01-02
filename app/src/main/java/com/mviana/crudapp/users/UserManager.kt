package com.mviana.crudapp.users

interface UserManager {

    val THEME_MODE: String
    val THEME_NO_THEME : String
    val THEME_DAY: String
    val THEME_NIGHT: String

    fun onUserSelected(user: User) //Called when a user from the recyclerview is selected
    fun onAddUser() //Called to add a new user
    fun onCloseAddEdit() //Called to discard the
    fun onUserSubmitted() //Called when POST USER was successful
    fun onUserEditedDeleted(action: String)
    fun onFiltering(filter: CharSequence) //Called when user has entered a user filter
    fun onUserListUpdated()
    fun onRequestFailed(text: String)
    fun onChangeAppTheme(code: Int) //Called when user switches between day and night App mode
    fun onUndoSuccess()
}