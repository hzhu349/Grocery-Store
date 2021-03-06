package com.example.mygrocerystore.models

data class User(
    var firstName:String? = null,
    var email:String? = null,
    var password:String? = null,
    val mobile:String? = null,
    val token:String? = null,
    val _id:String? = null
)

data class LoginResponse(
    var token:String?,
    var user:User
)