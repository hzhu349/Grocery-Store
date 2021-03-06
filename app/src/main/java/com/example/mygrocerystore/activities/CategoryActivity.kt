package com.example.mygrocerystore.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mygrocerystore.R
import com.example.mygrocerystore.database.DBHelper
import com.example.mygrocerystore.helpers.*
import com.example.mygrocerystore.adapters.AdapterCategory
import com.example.mygrocerystore.app.Endpoints
import com.example.mygrocerystore.models.Category
import com.example.mygrocerystore.models.CategoryResult
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.layout_menu_cart.view.*
import kotlinx.android.synthetic.main.nav_header.view.*

class CategoryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var sessionManager: SessionManager
    var textViewCartCount: TextView? = null
    lateinit var drawerLayout: DrawerLayout
    lateinit var dbHelper: DBHelper
    var mList: ArrayList<Category> = ArrayList()
    lateinit var navView:NavigationView
    lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sessionManager = SessionManager(this)
        dbHelper = DBHelper(this)

        init()
    }


    private fun setup() {
        var toolbar = bar
        toolbar.title = "Home"

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun init() {
    //    setup()
        getData()
        navView = nav_view
        drawerLayout = drawer_layout
        navView.setNavigationItemSelectedListener(this)

        var userName:String? = null
        var userEmail:String? = null
        if(sessionManager.isLoggedIn()){
            userName = sessionManager.getUser()
            userEmail = sessionManager.getEmail()
        }else{
            userName = "Guest!"
            userEmail = "You are not signed in"
        }

        var headerView = navView.getHeaderView(0)
        headerView.text_view_header_name.text = userName
        headerView.text_view_header_email.text = userEmail




        var toggle = ActionBarDrawerToggle( this, drawerLayout, bar, 0,0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        adapterCategory = AdapterCategory(this)
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = adapterCategory

    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()

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
        //var count = 10
        if(count == 0 ){
            textViewCartCount?.visibility = View.GONE
        }else{
            textViewCartCount?.visibility = View.VISIBLE
            textViewCartCount?.text = count.toString()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_cart -> startActivity(Intent(this, CartActivity::class.java))
            R.id.action_profile -> Toast.makeText(applicationContext, "Profile", Toast.LENGTH_SHORT)
                .show()
            R.id.action_setting -> Toast.makeText(applicationContext, "Setting", Toast.LENGTH_SHORT)
                .show()
            R.id.action_logout -> {
                if(sessionManager.isLoggedIn()){
                    sessionManager.logout()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                }else{
                    Toast.makeText(applicationContext, "Logout", Toast.LENGTH_SHORT).show()
                }

            }

        }
        return true
    }


    private fun getData() {
        var request = StringRequest(
            Request.Method.GET,
            Endpoints.getCategory(),
            {
                var gson = Gson()
                var categoryResult = gson.fromJson(it, CategoryResult::class.java)
                // Log.d("abc", categoryResult.data[0].catName)
                mList.addAll(categoryResult.data)
                adapterCategory.setData(mList)
                // progress_bar.visibility = View.GONE

            },
            {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

            }

        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun dialogueLogout() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes", object: DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                sessionManager.logout()
                startActivity(Intent(applicationContext, MainActivity::class.java))

            }

        })
        builder.setNegativeButton("No", object:DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }

        })
        var alertDialog = builder.create()
        alertDialog.show()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_logout -> {
                if(sessionManager.isLoggedIn()){
                    dialogueLogout()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }else{
                    Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.item_account -> Toast.makeText(this, "account", Toast.LENGTH_SHORT).show()
            R.id.item_settings -> Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
            R.id.item_orders -> startActivity(Intent(this, OrderHistoryActivity::class.java))

        }
        return true
    }



}
