package com.thechange.fass.fass.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.thechange.fass.fass.R
import com.thechange.fass.fass.adapter.CategoryAdapter
import com.thechange.fass.fass.dialog.CreateCategoryDialog
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.ogTag
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.concurrent.TimeUnit


class SaveActivity : AppCompatActivity() {

    private lateinit var categoryList : ArrayList<Item>
    private var url: String? = null
    private var flag = false

    private var itemCategory:String?=null
    private var itemDate:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*
        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.5f
        window!!.attributes = lpWindow
*/
        setContentView(R.layout.activity_save)
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_down )

        url = intent.getStringExtra("link")

        if( url == null ){
            url = intent.getStringExtra("link2")
            itemCategory = intent.getStringExtra("category")
            itemDate = intent.getStringExtra("date")
            flag = true
        }

        (findViewById(R.id.categoryList) as RecyclerView).addOnItemTouchListener(object: OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if(position==0){
                    CreateCategoryDialog(this@SaveActivity, url!!, flag).show()
                }
                else{
                    val category = adapter.getItem(position) as Item

                    Observable.fromCallable { ogTag(url!!) }
                            .subscribeOn(Schedulers.io())
                            .throttleFirst(3, TimeUnit.SECONDS)
                            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                            .subscribe { data->
                                val realm = Realm.getDefaultInstance()

                                realm.beginTransaction()
                                val item = realm.createObject(Item::class.java) // 새 객체 만들기
                                item.url = url
                                item.category = category.category
                                item.urlImage = data.imageUrl
                                item.urlTitle = data.imageTitle
                                item.date = data.date

                                if (flag) {
                                    val deleteItem = realm.where(Item::class.java)
                                            .equalTo("category", itemCategory?:"")
                                            .equalTo("date", itemDate?:"1111-11-11")
                                            .equalTo("url", url)
                                            .findFirst()
                                    deleteItem.deleteFromRealm()
                                    item.date = itemDate
                                }

                                realm.commitTransaction()

                                Toast.makeText(this@SaveActivity, getString(R.string.success), Toast.LENGTH_SHORT).show()
                                finish()
                            }

                }
            }
        })


        initUI()
    }

    fun initUI(){
        categoryList = ArrayList<Item>()
        categoryList.add(Item())


        val realm = Realm.getDefaultInstance()
        val unique = realm.where(Item::class.java).distinct("category")

        categoryList.addAll(unique.toList())

        (findViewById(R.id.categoryList) as RecyclerView).adapter = CategoryAdapter(R.layout.item_category, categoryList)

        val layoutManager = FlexboxLayoutManager()
        layoutManager.flexWrap  = FlexWrap.WRAP
        (findViewById(R.id.categoryList) as RecyclerView).layoutManager = layoutManager


    }

}