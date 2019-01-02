package com.mviana.crudapp.users

class UserFilter{

    fun getFilteredUsers(userList: ArrayList<User>, query: String): ArrayList<User> {
        var filteredUsers = ArrayList<User>()
        if(!query.isEmpty()){
            for (i in 0 until userList.size-1) {
                if (userList[i].name.toLowerCase().contains(query.toLowerCase())) {
                    var filteredUser = User()
                    filteredUser.name = userList[i].name
                    filteredUser.id = userList[i].id
                    filteredUser.birthDate = userList[i].birthDate
                    filteredUsers.add(filteredUser)
                }
            }
        }
        else {
            for (i in 0 until userList.size - 1) {
                var filteredUser = User()
                filteredUser.name = userList[i].name
                filteredUser.id = userList[i].id
                filteredUser.birthDate = userList[i].birthDate
                filteredUsers.add(filteredUser)
            }
        }

        return filteredUsers

    }

}