package com.example.mygrocerystore.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mygrocerystore.R

class StartActivity : AppCompatActivity() {

    private val delayedTime: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        var handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, CategoryActivity::class.java))
        }, delayedTime)
    }
}
