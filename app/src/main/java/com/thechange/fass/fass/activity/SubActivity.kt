package com.thechange.fass.fass.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.thechange.fass.fass.R
import com.thechange.fass.fass.adapter.MainCategoryAdapter
import com.thechange.fass.fass.databinding.ActivitySubBinding
import com.thechange.fass.fass.model.Item
import io.realm.Realm

class SubActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubBinding
    private lateinit var itemList: ArrayList<Item>
    private lateinit var category : String

    override fun onResume() {
        super.onResume()

        initUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub)

    }

    fun initUI(){
        category = intent.getStringExtra("category")

        val realm = Realm.getDefaultInstance()
        val items = realm.where(Item::class.java)
                .equalTo("category", category)
                .findAll()


        itemList = ArrayList<Item>()

        itemList.addAll(items.toList())
        val adapter = MainCategoryAdapter(R.layout.item_category_wide, itemList)
        binding.itemList.adapter = adapter
        binding.itemList.layoutManager = LinearLayoutManager(this)


        binding.itemList.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = adapter.getItem(position) as Item
                val intent = Intent(this@SubActivity, SubActivity::class.java)
                intent.putExtra("category", item.category)
                startActivity(intent)

            }
        })


    }
}
