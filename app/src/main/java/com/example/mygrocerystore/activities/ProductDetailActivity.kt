package com.example.mygrocerystore.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuItemCompat
import com.example.mygrocerystore.R
import com.example.mygrocerystore.app.Config
import com.example.mygrocerystore.database.DBHelper
import com.example.mygrocerystore.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.layout_menu_cart.view.*

class ProductDetailActivity : AppCompatActivity(), View.OnClickListener {
    var product: Product? = null
    lateinit var dbHelper: DBHelper
    var textViewCartCount: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        product = intent.getSerializableExtra(Product.DATA) as Product
        dbHelper = DBHelper(this)


        init()
    }

    private fun setupToolbar(){
        var toolbar = bar
        toolbar.title = product!!.productName
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun updateUI(){
        var quantity = dbHelper.getCartQuantityTotal()
        if(quantity > 0){
            button_add.visibility = View.INVISIBLE
            product_add_layout.visibility = View.VISIBLE
            product_count.text = quantity.toString()
        }else{
            button_add.visibility = View.VISIBLE
            product_add_layout.visibility = View.INVISIBLE

        }
    }

    private fun init() {
        setupToolbar()
        updateUI()
        setData(product!!)
        button_add.setOnClickListener(this)
        product_add_sign.setOnClickListener(this)
        product_sub_sign.setOnClickListener(this)

    }

    private fun setData(product: Product){

        text_view_name.text = product?.productName
        text_view_price.text = "Price: " + product?.price.toString()
        text_view_desc.text = product?.description
        Picasso.get()
            .load(Config.IMAGE_URL + product?.image)
            .into(image_view)


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
        when (item.itemId){
            android.R.id.home -> finish()

        }
        return true
    }

    override fun onClick(view: View) {
        when(view){
            button_add -> {
                product?.quantity = 1
                dbHelper.addCartItem(product!!)
                updateUI()
            }
            product_add_sign -> {
                var qnt = dbHelper.getItemQuantity(product!!)
                product?.quantity = qnt + 1
                dbHelper.updateCartItem(product!!)
                updateUI()

            }
            product_sub_sign -> {
                var qnt = dbHelper.getItemQuantity(product!!)
                product?.quantity = qnt - 1
                if(product?.quantity==0){
                    //Delete item
                    dbHelper.deleteCartItem((product!!))
                }else {
                    dbHelper.updateCartItem(product!!)
                }
                updateUI()


            }
        }
    }

}


