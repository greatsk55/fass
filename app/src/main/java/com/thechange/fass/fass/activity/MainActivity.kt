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
import com.thechange.fass.fass.databinding.ActivityMainBinding
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.ClipboardService
import io.realm.Realm

/**
 * Created by user on 2017. 4. 22..
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryList: ArrayList<Item>

    override fun onResume() {
        super.onResume()

        initUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // ClipboardService 시작
        val intent = Intent(this, ClipboardService::class.java)
        startService(intent)


        initUI()
    }

    fun initUI() {

        val realm = Realm.getDefaultInstance()
        val unique = realm.where(Item::class.java).distinct("category")

        categoryList = ArrayList<Item>()

        categoryList.addAll(unique.toList())
        val adapter = MainCategoryAdapter(R.layout.item_category_wide, categoryList)
        adapter.emptyView = View.inflate(this, R.layout.item_empty, null)
        binding.categoryList.adapter = adapter
        binding.categoryList.layoutManager = LinearLayoutManager(this)
        (findViewById(R.id.categoryList) as RecyclerView).addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = adapter.getItem(position) as Item
                val intent = Intent(this@MainActivity, SubActivity::class.java)
                intent.putExtra("category", item.category)
                startActivity(intent)

            }
        })


    }
}
