package com.thechange.fass.fass.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import com.thechange.fass.fass.dialog.MultiMoveCategory
import com.thechange.fass.fass.model.DeleteData
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.ogTag
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.concurrent.TimeUnit

class MultiMoveActivity : AppCompatActivity() {


    private lateinit var categoryList : ArrayList<Item>
    private var url: String? = null
    private var flag = false

    private lateinit var selectList : ArrayList<DeleteData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_multi_move)
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_down )

        selectList = intent.getParcelableArrayListExtra<DeleteData>("list")


        (findViewById(R.id.categoryList) as RecyclerView).addOnItemTouchListener(object: OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                if(position==0){
                    MultiMoveCategory(this@MultiMoveActivity).show()
                }
                else{
                    val category = adapter.getItem(position) as Item

                    Observable.fromCallable {  }
                            .subscribeOn(Schedulers.io())
                            .throttleFirst(3, TimeUnit.SECONDS)
                            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                            .subscribe {
                                val realm = Realm.getDefaultInstance()
                                realm.beginTransaction()

                                for( temp in selectList ){
                                    val item = realm.createObject(Item::class.java) // 새 객체 만들기
                                    item.url = temp.url
                                    item.category = category.category
                                    item.urlImage = temp.urlImage
                                    item.urlTitle = temp.urlTitle
                                    item.date = temp.date


                                    val deleteItem = realm.where(Item::class.java)
                                            .equalTo("category", temp.category)
                                            .equalTo("date", temp.date)
                                            .equalTo("url", temp.url)
                                            .findFirst()
                                    deleteItem.deleteFromRealm()
                                }

                                realm.commitTransaction()

                                Toast.makeText(this@MultiMoveActivity, getString(R.string.success), Toast.LENGTH_SHORT).show()
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
