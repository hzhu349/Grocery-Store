package com.example.mygrocerystore.models

import java.io.Serializable

data class OrderResponse(
    val count: Int,
    val `data`: List<FinalOrder>,
    val error: Boolean
):Serializable

data class OrderData(
    val __v: Int? = null,
    val _id: String? = null,
    val date: String? = null,
    val orderSummary: OrderSummary,
    val payment: Payment,
    val products: ArrayList<Product>,
    val shippingAddress: MyAddress,
    val user: User,
    val userId: String
): Serializable {
    companion object{
        const val DATA = "ORDER_DATA"
    }
}

data class FinalOrder(
    val __v: Int,
    val _id: String,
    val date: String,
    val orderStatus: String,
    val orderSummary: OrderSummary,
    val payment: Payment,
    val products: List<Any>,
    val shippingAddress: ShippingAddress,
    val user: User,
    val userId: String
):Serializable

data class OrderSummary(
    val _id: String? = null,
    val deliveryCharges: Double,
    val discount: Double,
    val orderAmount: Double? = null,
    val ourPrice: Double,
    val totalAmount: Double
):Serializable

data class Payment(
    val paymentMode: String,
    val paymentStatus: String
):Serializable

data class ShippingAddress(
    //val _id: String,
    val city: String,
    val houseNo: String,
   // val mobile: String,
   // val name: String,
    val pincode: Int,
    val streetName: String,
   // val type: String
):Serializable

data class PaymentUser(
    val _id: String,
    val email: String,
    val mobile: String,
    val name: String
):Serializable

data class PaymentResponse(
    var date: String,
    var orderStatus: String,
    var orderSummary: ArrayList<Product>,
    var payment: Payment,
    var products: ArrayList<Product>,
    var shippingAddress: ShippingAddress,
    var user: PaymentUser,
    var userId: String
)

//data class OrderData(
//    val __v: Int? = null,
//    val _id: String? = null,
//    val date: String? = null,
//    val orderSummary: OrderSummary,
//    val payment: Payment,
//    val products: ArrayList<Product>,
//    val shippingAddress: MyAddress,
//    val user: User,
//    val userId: String
//): Serializable {
//    companion object{
//        const val DATA = "ORDER_DATA"
//    }
//}