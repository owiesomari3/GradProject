package com.example.graduationproject

import com.example.graduationproject.enums.UserType

object CacheManager {

    private var userType: UserType = UserType.CHEF
    private var currentUser = ""

    fun setUserType(userType: UserType) {
        this.userType = userType
    }

    fun getUserType() = userType

    fun setCurrentUser(email:String){
        currentUser = email
    }

    fun getCurrentUser() = currentUser
}