package com.thechange.fass.fass.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.thechange.fass.fass.R
import com.thechange.fass.fass.adapter.MainCategoryAdapter
import com.thechange.fass.fass.adapter.SubItemAdapter
import com.thechange.fass.fass.databinding.ActivitySubBinding
import com.thechange.fass.fass.dialog.OptionDialog
import com.thechange.fass.fass.model.Item
import io.realm.Realm

class SubActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubBinding
    private lateinit var itemList: ArrayList<Item>
    private lateinit var category : String
    private lateinit var listAdapter : SubItemAdapter

    override fun onResume() {
        super.onResume()

        initUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub)
        binding.activity = this

        binding.itemList.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

                when(view.id){
                    R.id.item->{
                        val url = listAdapter.getItem(position).url
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                    R.id.option->{
                        val dialog = OptionDialog(this@SubActivity, listAdapter.getItem(position))
                        dialog.setOnDismissListener {
                            initUI()
                        }
                        dialog.show()
                    }
                }

            }
        })

    }

    fun initUI(){
        category = intent.getStringExtra("category")
        binding.title.text = category

        val realm = Realm.getDefaultInstance()
        val items = realm.where(Item::class.java)
                .equalTo("category", category)
                .findAll()


        itemList = ArrayList<Item>()

        itemList.addAll(items.toList())
        listAdapter = SubItemAdapter(R.layout.item_item, itemList)
        binding.itemList.adapter = listAdapter
        binding.itemList.setLayoutManager(GridLayoutManager(this,2))


    }

    fun onButtonClick(v:View){
        when(v.id){
            R.id.cancel-> finish()
        }
    }
}
