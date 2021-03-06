package com.example.mygrocerystore.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mygrocerystore.R
import com.example.mygrocerystore.app.Endpoints
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        init()
    }

    private fun init() {
        button_register.setOnClickListener {
            var name = edit_text_name.text.toString()
            var email = edit_text_email.text.toString()
            var password = edit_text_password.text.toString()
            var phone = edit_text_phone.text.toString()

            if(name == "" || email == "" || password == "" || phone == ""){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }else if(password.length != 6){
                Toast.makeText(this, "Password must be 6 characters long", Toast.LENGTH_SHORT).show()
            } else{
                var params = HashMap<String, String>()
                params["firstName"] = name
                params["email"] = email
                params["password"] = password
                params["mobile"] = phone

                var jsonObject = JSONObject(params as Map<*, *>)

                var url = Endpoints.getRegister()

                var request = JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,
                    {
                        Toast.makeText(applicationContext, "Registered", Toast.LENGTH_SHORT).show()
                    },
                    {

                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

                    }
                )
                Volley.newRequestQueue(this).add(request)
                startActivity(Intent(this, LoginActivity::class.java))

            }

        }

        text_view_sign_in.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}