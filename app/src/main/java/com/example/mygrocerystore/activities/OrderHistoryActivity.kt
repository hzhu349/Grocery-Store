package com.example.mygrocerystore.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mygrocerystore.R
import com.example.mygrocerystore.adapters.AdapterOrderHistory
import com.example.mygrocerystore.app.Endpoints
import com.example.mygrocerystore.helpers.SessionManager
import com.example.mygrocerystore.models.FinalOrder
import com.example.mygrocerystore.models.OrderResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order_history.*
import kotlinx.android.synthetic.main.app_bar.*

class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    var mList:ArrayList<FinalOrder> = ArrayList()
    private var adapterOrderHistory: AdapterOrderHistory? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        sessionManager = SessionManager(this)
        init()
    }

    private fun setupToolbar(){
        var toolbar = bar
        toolbar.title = "Order History"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun init() {
        setupToolbar()
        getData()
        adapterOrderHistory = AdapterOrderHistory(this, mList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapterOrderHistory
    }

    private fun getData() {
        var userId = sessionManager.getUserId()
        var request = StringRequest(
            Request.Method.GET,
            userId?.let { Endpoints.getOrder(it) },
            {
                val gson = Gson()
                var orderResponse = gson.fromJson(it.toString(), OrderResponse::class.java)
                mList.addAll(orderResponse.data)
                adapterOrderHistory!!.setData(mList)

            },
            {
                Toast.makeText(this, "Error getting order history", Toast.LENGTH_SHORT).show()

            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }
}