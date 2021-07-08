package com.example.mygrocerystore.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View

import androidx.appcompat.widget.Toolbar
import com.example.mygrocerystore.R

import com.example.mygrocerystore.app.Config
import com.example.mygrocerystore.database.*
import com.example.mygrocerystore.helpers.SessionManager
import com.example.mygrocerystore.models.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.app_bar.*


class PaymentActivity : AppCompatActivity() {
    lateinit var session: SessionManager
    lateinit var dbHelper: DBHelper
    lateinit var selectedAddress: MyAddress
    lateinit var orderData: OrderData
    lateinit var intentConfirmation: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        init()
    }
    private fun init() {
        setupToolbar()
        initializeVariables()
        getPOSTData()
        setupUI()
    }

    private fun setupToolbar(){
        var toolbar: Toolbar = bar
        toolbar.title = "Payment"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
        }
        return true
    }

    private fun initializeVariables() {
        session = SessionManager(this)
        dbHelper = DBHelper(this)
        selectedAddress = intent.getSerializableExtra(MyAddress.KEY_ADDRESS) as MyAddress
        intentConfirmation = Intent(this, ConfirmationActivity::class.java)
    }
    private fun setupUI(){
        txt_error_msg.visibility = View.GONE

        txt_user_name.text = session.getUser()
        txt_user_email.text = orderData.user.email
        txt_user_mobile.text = "Mobile: "+ orderData.user.mobile

        txt_address_type.text = "Type: " + orderData.shippingAddress.type
        txt_address_houseNo.text = orderData.shippingAddress.houseNo +", "
        txt_address_street.text = orderData.shippingAddress.streetName
        txt_address_city.text = orderData.shippingAddress.city +", "
        txt_adress_zipcode.text = "ZipCode: "+ orderData.shippingAddress.pincode.toString()
    }
    private fun getPOSTData() {
        var retailPrice = dbHelper.getRetailPrice()
        var ourPrice = dbHelper.getOurPrice()
        var orderSummary = OrderSummary(
            null,
            Config.DELIVERY_FEE.toDouble(),
            (retailPrice - ourPrice).toDouble(),
            retailPrice,
            ourPrice,
            ourPrice + Config.DELIVERY_FEE
        )
        var orderPayment = Payment(

            "cash",
            "completed"
        )
        var orderProducts = dbHelper.getAllProductOrder()
        var orderAddress = MyAddress(
            city = selectedAddress.city!!,
            houseNo = selectedAddress.houseNo!!,
            pincode = selectedAddress.pincode!!,
            streetName = selectedAddress.streetName!!,
            type = selectedAddress.type!!
        )
        var orderUser =
            User(
                _id = session.getUserId()!!,
                email = session.getEmail()!!,
                mobile = session.getMobile() ?: ""
            )

        orderData = OrderData(
            null, null, null,
            orderSummary,
            orderPayment,
            orderProducts,
            orderAddress,
            orderUser,
            session.getUserId()!!
        )
        intentConfirmation.putExtra(OrderData.DATA, orderData)
    }

    fun btnPayCash_onClick(view: View){
        startActivity(intentConfirmation)
    }
}