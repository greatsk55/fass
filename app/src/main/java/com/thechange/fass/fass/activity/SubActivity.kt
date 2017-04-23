package com.thechange.fass.fass.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.thechange.fass.fass.R
import com.thechange.fass.fass.adapter.MainCategoryAdapter
import com.thechange.fass.fass.adapter.SubItemAdapter
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
        val adapter = SubItemAdapter(R.layout.item_item, itemList)
        binding.itemList.adapter = adapter
        binding.itemList.setLayoutManager(GridLayoutManager(this,2))


        binding.itemList.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

                when(view.id){
                    R.id.item->{
                        val url = (adapter.getItem(position) as Item).url
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                    R.id.option->{
                        //TODO 옵션 다이알로그
                    }
                }

            }
        })
    }

    fun onButtonClick(v:View){
        when(v.id){
            R.id.cancel->finish()
        }
    }
}
