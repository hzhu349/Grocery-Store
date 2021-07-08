package com.example.mygrocerystore.helpers

import android.content.Context
import android.content.SharedPreferences
import com.example.mygrocerystore.models.MyAddress
import com.example.mygrocerystore.models.User

class SessionManager(var mContext: Context) {
    private val FILE_NAME = "my_file"
    private val KEY_USER_ID = "_id"
    private val KEY_NAME = "name"
    private val KEY_EMAIL = "email"
    private val KEY_PASSWORD = "password"
    private val KEY_MOBILE = "mobile"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"




    var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor

    init{
        sharedPreferences = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }


    fun saveUser(user: User) {
        editor.putString(KEY_NAME, user.firstName)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PASSWORD, user.password)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
    }

/*    fun register(name: String, mobile: String, email:String, password: String){
        editor.putString(KEY_NAME,name)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_MOBILE, mobile)
        editor.putString(KEY_PASSWORD, password)
        editor.putBoolean(KEY_IS_REGISTERED, true)
        editor.commit()
    }*/
    fun login(userId:String, name: String, mobile: String, email:String, password: String){
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_NAME,name)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_MOBILE, mobile)
        editor.putString(KEY_PASSWORD, password)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.commit()
    }
    fun verifyLogin(email: String, password: String): Boolean {
        var savedEmail = sharedPreferences.getString(KEY_EMAIL, "")
        var savedPassword = sharedPreferences.getString(KEY_PASSWORD, "")
        if( savedEmail.equals(email) && savedPassword.equals(password)){
            editor.putBoolean(KEY_IS_LOGGED_IN, true)
            return true
        }
        return false
    }
    fun getUser(): String?{
        return sharedPreferences.getString(KEY_NAME, null)
    }
    fun getUserId(): String?{
        return sharedPreferences.getString(KEY_USER_ID, null)
    }
    fun getEmail(): String?{
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun getMobile(): String?{
        return sharedPreferences.getString(KEY_MOBILE, null)
    }
    fun isLoggedIn(): Boolean{
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

/*    fun isRegistered(): Boolean{
        return sharedPreferences.getBoolean(KEY_IS_REGISTERED, false)
    }*/

    fun logout(){
        editor.remove(KEY_IS_LOGGED_IN)
        editor.commit()
    }

    fun clear(){
        editor.clear()
        editor.commit()
    }
}