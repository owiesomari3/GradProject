package com.example.graduationproject

import com.example.graduationproject.enums.UserType
import com.example.graduationproject.models.User

object CacheManager {
    private val users: ArrayList<User> = arrayListOf(
        User("Raneem", "chef@gmail.com", "Pass@1234", UserType.CHEF),
        User("Raneem", "hungry@gmail.com", "Pass@1234", UserType.HUNGRY)
    )

    fun addNewUser(user: User) {
        users.add(user)
    }

    fun getUserByEmail(email: String): User {
        return users.first { it.email == email }
    }

    fun getUserByEmailAndPassword(email: String, password: String): User? {
        val user = users.filter { it.email.equals(email, true) && it.password == password }
        return if (user.isEmpty()) null else user[0]
    }
}