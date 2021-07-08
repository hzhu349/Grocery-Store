package com.example.mygrocerystore.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygrocerystore.R
import com.example.mygrocerystore.activities.ProductDetailActivity
import com.example.mygrocerystore.app.Config
import com.example.mygrocerystore.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_product_adapter.view.*

class AdapterProduct(var mContext: Context) : RecyclerView.Adapter<AdapterProduct.MyViewHolder>() {

    var mList: ArrayList<Product> = ArrayList()
    var countViewHolders = 0

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, position: Int) {
//            Log.d("myApp","ProductID:${product._id}, ProductCat:${product.catId}")
            itemView.txt_prod_name.text = product.productName
            itemView.txt_prod_price.text =  "$ " + String.format("%.2f", product.price)

            Picasso.get()
                .load(Config.IMAGE_URL + product.image )
                .into(itemView.img_product)

            itemView.setOnClickListener {
                var intent = Intent(mContext, ProductDetailActivity::class.java)
                intent.putExtra(Product.DATA, product)
                mContext.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view =
            LayoutInflater.from(mContext).inflate(R.layout.row_product_adapter, parent, false)
        countViewHolders +=1
//        Log.d("myApp","View Holder count:$countViewHolders")
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product, position)
    }

    fun setData(data: java.util.ArrayList<Product>) {
        mList = data
        notifyDataSetChanged()
    }
}