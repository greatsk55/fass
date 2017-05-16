package com.thechange.fass.fass.activity

import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.SimpleClickListener
import com.thechange.fass.fass.R
import com.thechange.fass.fass.adapter.MainCategoryAdapter
import com.thechange.fass.fass.databinding.ActivityMainBinding
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.PersistentService
import com.thechange.fass.fass.service.RestartService
import io.realm.Realm

/**
 * Created by user on 2017. 4. 22..
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryList: ArrayList<Item>
    private lateinit var listAdapter : MainCategoryAdapter
    private lateinit var receiver : RestartService

    override fun onResume() {
        super.onResume()
        initUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // ClipboardService 시작
        /*
        val intent = Intent(this, ClipboardService::class.java)
        startService(intent)
        */

        val intent = Intent(this, PersistentService::class.java)

        // 리시버 등록
        receiver = RestartService()

        try
        {
            // xml에서 정의해도 됨?
            // 이것이 정확히 무슨 기능을 하지는지?
            val mainFilter = IntentFilter("com.thechange.fass.fass.service")

            // 리시버 저장
            registerReceiver(receiver, mainFilter)

            // 서비스 시작
            startService(intent)

        } catch (e:Exception) {
            e.printStackTrace()
        }




        binding.categoryList.addOnItemTouchListener(object: SimpleClickListener(){
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            }

            override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val item = listAdapter.getItem(position)
                val intent = Intent(this@MainActivity, SubActivity::class.java)
                intent.putExtra("category", item.category)
                startActivity(intent)

            }

            override fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            }
        })
    }

    fun initUI() {
        val realm = Realm.getDefaultInstance()
        val unique = realm.where(Item::class.java).distinct("category")

        categoryList = ArrayList<Item>()
        categoryList.addAll(unique.toList())

        listAdapter = MainCategoryAdapter(R.layout.item_category_wide, categoryList)

        listAdapter.emptyView = View.inflate(this, R.layout.item_empty, null)
        binding.categoryList.adapter = listAdapter
        binding.categoryList.layoutManager = LinearLayoutManager(this)

    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }
}
