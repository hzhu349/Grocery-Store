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
import com.example.mygrocerystore.R
import com.example.mygrocerystore.adapters.AdapterCart
import com.example.mygrocerystore.database.DBHelper
import com.example.mygrocerystore.helpers.SessionManager
import com.example.mygrocerystore.models.Product
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_category.recycler_view
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.layout_menu_cart.view.*

class CartActivity : AppCompatActivity() {
    var mList:ArrayList<Product> = ArrayList()
    private var adapterCart:AdapterCart? = null
    lateinit var dbHelper: DBHelper
    var textViewCartCount: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        dbHelper = DBHelper(this)
        init()
    }
    private fun setupToolbar(){
        var toolbar = bar
        toolbar.title = "Shopping Cart"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun init() {
        setupToolbar()

        mList = dbHelper.getAllCartItems()
        adapterCart = AdapterCart(this, mList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapterCart
        adapterCart?.setData(mList)


        text_view_total.text = "$" + dbHelper.getRetailPrice().toString()
        text_view_discount.text = "$" + dbHelper.getDiscount().toString()
        text_view_subtotal.text = "$" +  dbHelper.getOurPrice().toString()

        button_checkout.setOnClickListener {
            var sessionManager = SessionManager(this)
            if(sessionManager.isLoggedIn()){
                startActivity(Intent(this, AddressActivity::class.java))
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        var count = dbHelper.getCartQuantityTotal()
        if(count == 0){
            text_view_empty.visibility = View.VISIBLE
            image_view_cart.visibility = View.VISIBLE
            relative_layout_totals.visibility = View.INVISIBLE
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        var item = menu.findItem(R.id.action_cart)
        MenuItemCompat.setActionView(item, R.layout.layout_menu_cart)
        var view = MenuItemCompat.getActionView(item)
        textViewCartCount = view.text_view_cart_count

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> finish()
//            R.id.action_cart -> Toast.makeText(applicationContext, "Cart", Toast.LENGTH_SHORT).show()
//            R.id.action_profile -> Toast.makeText(applicationContext, "Profile", Toast.LENGTH_SHORT).show()
//            R.id.action_setting -> Toast.makeText(applicationContext, "Setting", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        mList = dbHelper.getAllCartItems()
        adapterCart?.setData(mList)
    }



}