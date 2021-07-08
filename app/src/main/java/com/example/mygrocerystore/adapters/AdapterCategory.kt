package com.example.mygrocerystore.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygrocerystore.R
import com.example.mygrocerystore.activities.SubCategoryActivity
import com.example.mygrocerystore.app.Config
import com.example.mygrocerystore.models.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_category_adapter.view.*

class AdapterCategory(var mContext: Context):RecyclerView.Adapter<AdapterCategory.ViewHolder>(){

    var mList:ArrayList<Category> = ArrayList()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(category:Category){
            Picasso.get()
                .load(Config.IMAGE_URL + category.catImage)
                .into(itemView.image_view)
            itemView.text_view_category_name.text = category.catName

            itemView.setOnClickListener {
                var intent = Intent(mContext, SubCategoryActivity::class.java)
                intent.putExtra(Category.KEY_CATEGORY, category)
                mContext.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_category_adapter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var category = mList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(l:ArrayList<Category>){
        mList = l
        notifyDataSetChanged()
    }
}