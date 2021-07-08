package com.example.mygrocerystore.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mygrocerystore.R

class ConfirmationActivity : AppCompatActivity() {
    private val delayedTime: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        var handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, delayedTime)
    }


}