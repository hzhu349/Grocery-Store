package com.example.mygrocerystore.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygrocerystore.R
import com.example.mygrocerystore.activities.CartActivity
import com.example.mygrocerystore.app.Config
import com.example.mygrocerystore.database.DBHelper
import com.example.mygrocerystore.models.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_adapter_cart.view.*


class AdapterCart(var mContext: Context, var mList: ArrayList<Product>) :
    RecyclerView.Adapter<AdapterCart.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, position: Int) {
            itemView.text_view_cart_name.text = product.productName
            // itemView.text_view_quant.text = product.quantity.toString()
            itemView.text_view_item_quantity.text = product.quantity.toString()
            itemView.text_view_price.text =  product.price.toString()
            var dbHelper = DBHelper(mContext)

            Picasso.get()
                .load(Config.IMAGE_URL + product.image)
                .into(itemView.image_view)

            itemView.button_remove.setOnClickListener {
                dbHelper.deleteAll()
                mList.removeAt(position)
                notifyItemRemoved(position)
            }
            itemView.button_add.setOnClickListener {
                product.quantity = product.quantity?.plus(1)
                dbHelper.updateCartItem(product)
                notifyDataSetChanged()

                restart()
            //                Toast.makeText(mContext,"Added",Toast.LENGTH_LONG).show()
                }
            itemView.button_dec.setOnClickListener {
                product.quantity = product.quantity?.minus(1)
                dbHelper.updateCartItem(product)

//                Toast.makeText(mContext,"Removed",Toast.LENGTH_LONG).show()
                // dbHelper.sub1(product)
                notifyDataSetChanged()
                restart()

            }



        }

        private fun restart() {
            val activity: CartActivity = mContext as CartActivity
            activity.finish()
            activity.overridePendingTransition(0, 0);
            activity.startActivity(Intent(activity, CartActivity::class.java))
            activity.overridePendingTransition(0, 0);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_adapter_cart, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var product = mList[position]
        holder.bind(product, position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(l: ArrayList<Product>) {
        mList = l
        notifyDataSetChanged()
    }


}