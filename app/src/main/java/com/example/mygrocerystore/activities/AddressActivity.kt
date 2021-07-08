package com.example.mygrocerystore.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mygrocerystore.R
import com.example.mygrocerystore.adapters.AdapterAddress
import com.example.mygrocerystore.app.Endpoints
import com.example.mygrocerystore.database.DBHelper
import com.example.mygrocerystore.helpers.SessionManager
import com.example.mygrocerystore.models.AddressResponse
import com.example.mygrocerystore.models.MyAddress
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_address.*
import kotlinx.android.synthetic.main.activity_category.recycler_view
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.layout_menu_cart.view.*

class AddressActivity : AppCompatActivity() {
    lateinit var sessionManager: SessionManager
    var mList:ArrayList<MyAddress> = ArrayList()
    private var adapterAddress:AdapterAddress? = null
    var textViewCartCount: TextView? = null
    lateinit var dbHelper: DBHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)

        init()
    }

    private fun setupToolbar(){
        var toolbar = bar
        toolbar.title = "Addresses"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }


    private fun init() {
        setupToolbar()

        button_add.setOnClickListener {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }

        getData()
        adapterAddress = AdapterAddress(this, mList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapterAddress

        button_payment.setOnClickListener {
            startActivity(Intent(this, PaymentActivity::class.java))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        var item = menu.findItem(R.id.action_cart)
        MenuItemCompat.setActionView(item, R.layout.layout_menu_cart)
        var view = MenuItemCompat.getActionView(item)
        textViewCartCount = view.text_view_cart_count
        view.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        updateCartCount()

        return true
    }
    private fun updateCartCount(){
        var count = dbHelper.getCartQuantityTotal()
        if(count == 0 ){
            textViewCartCount?.visibility = View.GONE
        }else{
            textViewCartCount?.visibility = View.VISIBLE
            textViewCartCount?.text = count.toString()

        }
    }

    private fun getData() {
        var userId = sessionManager.getUserId()
        var request = StringRequest(
            Request.Method.GET,
            Endpoints.getAddress(userId.toString()),
            {
                val gson = Gson()
                var addressRespone = gson.fromJson(it.toString(), AddressResponse::class.java)
                mList.addAll(addressRespone.data)
                adapterAddress!!.setData(mList)
            },
            {

            }


        )
        Volley.newRequestQueue(this).add(request)
    }


}