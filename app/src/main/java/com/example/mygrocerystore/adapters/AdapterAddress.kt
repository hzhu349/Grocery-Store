package com.example.mygrocerystore.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mygrocerystore.R
import com.example.mygrocerystore.activities.PaymentActivity
import com.example.mygrocerystore.app.Endpoints
import com.example.mygrocerystore.helpers.SessionManagerAddress
import com.example.mygrocerystore.models.MyAddress
import kotlinx.android.synthetic.main.row_address_adapter.view.*

class AdapterAddress(var mContext: Context, var mList:ArrayList<MyAddress>):RecyclerView.Adapter<AdapterAddress.MyViewHolder>(){

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(address: MyAddress){
            itemView.text_view_pincode.text = address.pincode.toString()
            itemView.text_view_street.text = address.streetName.toString()
            itemView.text_view_city.text = address.city.toString()
            itemView.text_view_houseno.text = address.houseNo.toString()
            itemView.text_view_type.text = address.type.toString()

            itemView.setOnClickListener {
                var sessionManager = SessionManagerAddress(mContext)
                sessionManager.saveAddress(address)
                mContext.startActivity(Intent(mContext, PaymentActivity::class.java))

            }
            itemView.button_delete_address.setOnClickListener{
                val request = StringRequest(
                    Request.Method.DELETE,
                    Endpoints.deleteAddress(address._id!!),
                    {

                    },
                    {

                    }


                )
                Volley.newRequestQueue(mContext).add(request)
                notifyItemRemoved(mList.indexOf(address))
                mList.remove(address)

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_address_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var address = mList[position]
        holder.bind(address)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(l:ArrayList<MyAddress>){
        mList = l
        notifyDataSetChanged()
    }
}