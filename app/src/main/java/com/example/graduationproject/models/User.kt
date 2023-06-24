package com.example.graduationproject.models

import com.example.graduationproject.enums.UserType

data class User(val fullName: String,
                val email: String,
                val password: String,
                val type: UserType)
