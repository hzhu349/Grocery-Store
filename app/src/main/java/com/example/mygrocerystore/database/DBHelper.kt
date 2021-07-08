package com.example.mygrocerystore.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getIntOrNull
import com.example.mygrocerystore.app.Config
import com.example.mygrocerystore.models.Product



class DBHelper(var mContext: Context) :
    SQLiteOpenHelper(mContext, Config.DATABASE_NAME, null, Config.DATABASE_VERSION)
{
    //read&write
    private var db_write = this.writableDatabase
    //read only
    private var db_read = this.readableDatabase

    companion object {
        const val TABLE_NAME = "cart"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_PRODUCT_NAME = "product_name"
        const val COLUMN_IMAGE = "product_image"
        const val COLUMN_PRICE = "price"
        const val COLUMN_MRP = "product_mrp"
        const val COLUMN_QUANTITY = "quantity"
        val COLUMNS = arrayOf(
            COLUMN_PRODUCT_ID,
            COLUMN_PRODUCT_NAME,
            COLUMN_IMAGE,
            COLUMN_PRICE,
            COLUMN_MRP,
            COLUMN_QUANTITY
        )
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var sql = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
                "$COLUMN_PRODUCT_ID CHAR(24) PRIMARY KEY, " +
                "$COLUMN_PRODUCT_NAME CHAR(50), " +
                "$COLUMN_IMAGE CHAR(250), " +
                "$COLUMN_PRICE REAL, " +
                "$COLUMN_MRP REAL, " +
                "$COLUMN_QUANTITY INTEGER " +
                ")"
        db?.execSQL(sql)
        Log.d("GroceryApp", "DBHelper: Table cart created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTable)
        onCreate(db)
    }

    fun isItemInCart(p: Product): Boolean{
        var sql = "SELECT $COLUMN_PRODUCT_ID FROM $TABLE_NAME WHERE $COLUMN_PRODUCT_ID = ?"
        var cursor = db_write.rawQuery(sql, arrayOf(p._id))
        var count = cursor.count
        return (count != 0)
    }

    fun addCartItem(p: Product): Long {
        var insertedID: Long = -1
        if(!isItemInCart(p)) {
            var values = ContentValues()
            values.put(COLUMN_PRODUCT_ID, p._id)
            values.put(COLUMN_PRODUCT_NAME, p.productName)
            values.put(COLUMN_IMAGE, p.image)
            values.put(COLUMN_PRICE, p.price)
            values.put(COLUMN_MRP, p.mrp)
            values.put(COLUMN_QUANTITY, 1)
            insertedID = db_write.insert(TABLE_NAME, null, values)
            Log.d("GroceryApp", "DBHelper: CART_TABLE item inserted id=$insertedID.")
        }else{
            Log.d("GroceryApp", "DBHelper: CART_TABLE already has the item.")
        }
        return insertedID
    }
    fun updateCartItem(p: Product): Boolean {
        var values = ContentValues()
        values.put(COLUMN_QUANTITY, p.quantity)
        //Safely preventing SQL Injection
        var whereClause = "$COLUMN_PRODUCT_ID = ?"
        var whereArgs = arrayOf(p._id)

        var rowsAffected = db_write.update(TABLE_NAME, values, whereClause, whereArgs)
        Log.d("myApp", "DBHelper: CART_TABLE update, $rowsAffected rowsAffected.")
        return (rowsAffected > 0)
    }
    fun deleteCartItem(p: Product): Boolean {
        //Safely preventing SQL Injection
        var whereClause = "$COLUMN_PRODUCT_ID = ?"
        var whereArgs = arrayOf(COLUMN_PRODUCT_ID)

        var rowsAffected = db_write.delete(TABLE_NAME, whereClause, whereArgs)
        Log.d("myApp", "DBHelper: CART_TABLE delete, $rowsAffected rowsAffected.")
        return (rowsAffected > 0)
    }
    fun deleteAll(){
        val dropTable = "DELETE FROM $TABLE_NAME"
        db_write.execSQL(dropTable)
    }
    fun getAllCartItems(): ArrayList<Product> {
        var prodList: ArrayList<Product> = ArrayList();
        var cursor = db_read.query(
            TABLE_NAME,
            COLUMNS, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                var product = Product()
                product._id = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID))
                product.productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME))
                product.image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                product.price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                product.mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))
                product.quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                prodList.add(product)
            } while (cursor.moveToNext())
        }
        return prodList
    }
    fun getAllProductOrder(): ArrayList<Product> {
        var prodList: ArrayList<Product> = ArrayList();
        var cursor = db_read.query(
            TABLE_NAME,
            COLUMNS, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            do {
                var _id = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID))
                var productName = cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME))
                var image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                var price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                var mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))
                var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                var product = Product(
                    _id,
                    image,
                    mrp,
                    price,
                    productName,
                    quantity
                )
                prodList.add(product)
            } while (cursor.moveToNext())
        }
        return prodList
    }
    fun getItemQuantity(p: Product):Int{
        var qnt = 0
        var sql = "SELECT $COLUMN_QUANTITY FROM $TABLE_NAME WHERE $COLUMN_PRODUCT_ID = ?"
        var cursor = db_read.rawQuery(sql, arrayOf(p._id))
        if(cursor.moveToFirst()){
            qnt = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
            cursor.close()
        }
        return qnt
    }
    fun getCartQuantityTotal():Int{
        var qnt = 0
        var sql = "SELECT SUM($COLUMN_QUANTITY) FROM $TABLE_NAME "
        var cursor = db_read.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            qnt = cursor.getIntOrNull(0) ?: 0
            cursor.close()
        }
        return qnt
    }
    fun getRetailPrice(): Double {
        var result = 0.0
        var sql = "SELECT SUM($COLUMN_MRP*$COLUMN_QUANTITY) FROM $TABLE_NAME "
        var cursor = db_read.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            result = cursor.getDoubleOrNull(0) ?: 0.0
            cursor.close()
        }
        return result
    }
    fun getOurPrice(): Double {
        var result = 0.0
        var sql = "SELECT SUM($COLUMN_PRICE*$COLUMN_QUANTITY) FROM $TABLE_NAME "
        var cursor = db_read.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            result = cursor.getDoubleOrNull(0) ?: 0.0
            cursor.close()
        }
        return result
    }
    fun getDiscount(): Double {
        var result = 0.0
        var sql = "SELECT SUM(($COLUMN_MRP-$COLUMN_PRICE)*$COLUMN_QUANTITY) FROM $TABLE_NAME "
        var cursor = db_read.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            result = cursor.getDoubleOrNull(0) ?: 0.0
            cursor.close()
        }
        return result
    }

}