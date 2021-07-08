package com.example.mygrocerystore.activities

import android.content.Intent
import android.net.ParseException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.mygrocerystore.R
import com.example.mygrocerystore.adapters.AdapterProduct
import com.example.mygrocerystore.app.Endpoints
import com.example.mygrocerystore.database.DBHelper
import com.example.mygrocerystore.helpers.SessionManager
import com.example.mygrocerystore.helpers.SessionManagerAddress
import com.example.mygrocerystore.models.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_order.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class OrderDetailsActivity : AppCompatActivity() {
    lateinit var sessionManagerAddress: SessionManagerAddress
    lateinit var sessionManager: SessionManager

    lateinit var adapterProduct: AdapterProduct
    lateinit var orderData: OrderData
    lateinit var dbHelper: DBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        init()
    }

    private fun init() {
        orderData = intent.getSerializableExtra(OrderData.DATA) as OrderData
        loadUI()

        //Generate list of Products
        adapterProduct =
            AdapterProduct(this)
        adapterProduct.mList = orderData.products

        //TODO: Setup Recyclerview
        recycler_view.adapter = adapterProduct
        recycler_view.layoutManager = LinearLayoutManager(this)

        button_submit_order.setOnClickListener {
            postOrder()
            startActivity(Intent(this, ConfirmationActivity::class.java))
            finish()
        }
    }

    private fun postOrder() {
        val gson = Gson()
        val postOrder = gson.toJson(createPaymentResponse())
        var jsonObject = JSONObject(postOrder)
        var request = JsonObjectRequest(
            Request.Method.POST,
            Endpoints.postOrder(),
            jsonObject,
            {
                Toast.makeText(this, "Posted Order", Toast.LENGTH_SHORT).show()
                dbHelper.deleteAll()
            },
            {
                Toast.makeText(this, "Error posting order", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(this).add(request)

    }

    private fun loadUI() {
        txt_order_date.text = orderData.date

//        txt_user_name.text = orderData.user.name
        txt_user_email.text = orderData.user.email
        txt_user_mobile.text = "Mobile: " + orderData.user.mobile

        txt_address_type.text = orderData.shippingAddress.type
        txt_address_houseNo.text = orderData.shippingAddress.houseNo
        txt_address_street.text = orderData.shippingAddress.streetName
        txt_address_city.text = orderData.shippingAddress.city + ", "
        txt_adress_zipcode.text = "ZipCode: " + orderData.shippingAddress.pincode

        txt_order_ourPrice.text = "Our Price: " +
                "$ " + String.format("%.2f", orderData.orderSummary.ourPrice)
        txt_order_amount.text = "Order Amount: " +
                "$ " + String.format("%.2f", orderData.orderSummary.orderAmount)
        txt_order_discount.text = "Discount: " +
                "$ " + String.format("%.2f", orderData.orderSummary.discount)
        txt_order_delivery.text = "Delivery Charges: " +
                "$ " + String.format("%.2f", orderData.orderSummary.deliveryCharges)
        txt_order_total.text = "Total Amount: " +
                "$ " + String.format("%.2f", orderData.orderSummary.totalAmount)
    }

    fun convertDate(date: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("MMM d, yyyy")
        try {
            val finalStr: String = outputFormat.format(inputFormat.parse(date))
            println(finalStr)
            return finalStr
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }
    private fun createPaymentResponse(): PaymentResponse {

        var address = sessionManagerAddress.getAddress()

        val products = dbHelper.getAllCartItems()
        val date = LocalDateTime.now()
        val orderStatus = "Confirmed"
        var orderSummary = dbHelper.getAllProductOrder()
        val payment = Payment("Cash", "Pending")
        val shippingAddress = ShippingAddress(
            address.city!!,
            address.houseNo!!,
            address.pincode!!,
            address.streetName!!
        )
        val user = PaymentUser(
            sessionManager.getUserId().toString(),
            sessionManager.getEmail().toString(),
            sessionManager.getMobile().toString(),
            sessionManager.getUser().toString()
        )
        val userId = sessionManager.getUserId()
        return PaymentResponse(
            date.toString(),
            orderStatus,
            orderSummary,
            payment,
            products,
            shippingAddress,
            user,
            userId.toString()
        )
    }
}