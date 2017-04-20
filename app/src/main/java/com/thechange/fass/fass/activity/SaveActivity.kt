package com.thechange.fass.fass.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener

import com.thechange.fass.fass.R
import com.thechange.fass.fass.adapter.CategoryAdapter
import com.thechange.fass.fass.model.Item
import io.realm.Realm


class SaveActivity : AppCompatActivity() {

    private lateinit var categoryList : ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        categoryList = ArrayList<Item>()
        //categoryList.add(Item())


        val realm = Realm.getDefaultInstance()
        val unique = realm.where(Item::class.java).distinct("category")

        categoryList.addAll(unique.toList())

        (findViewById(R.id.categoryList) as RecyclerView).adapter = CategoryAdapter(R.layout.item_category, categoryList)
        (findViewById(R.id.categoryList) as RecyclerView).setLayoutManager(GridLayoutManager(this,4))
        (findViewById(R.id.categoryList) as RecyclerView).addOnItemTouchListener(object: OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                //val item = adapter.getItem(position) as RequestProductDto
                //val intent = Intent(this@ActivityItemInfo, ActivityItemInfo::class.java
            }
        })

    }

    fun onButtonClick(v: View){

    }
}