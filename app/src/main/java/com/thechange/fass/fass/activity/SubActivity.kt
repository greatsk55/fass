package com.thechange.fass.fass.activity

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.SimpleClickListener
import com.thechange.fass.fass.R
import com.thechange.fass.fass.adapter.SubItemAdapter
import com.thechange.fass.fass.databinding.ActivitySubBinding
import com.thechange.fass.fass.dialog.OptionDialog
import com.thechange.fass.fass.model.DeleteData
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.realm.Realm
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit

class SubActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubBinding
    private lateinit var itemList: ArrayList<Item>
    private lateinit var category : String
    private lateinit var listAdapter : SubItemAdapter
    private var flag = false


    override fun onResume() {
        super.onResume()


        RxBus().instanceOf().getEvents()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { flag ->

                    Log.d("AAA", "did +  " + (flag as Boolean))
                    if( flag as Boolean ) {
                        initUI()
                    }
                }



        initUI()
    }

    override fun onPause() {
        super.onPause()


        if( RxBus().hasObservers() ){
            Log.d("AAA", "has observer")
            RxBus().complete()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub)
        binding.activity = this


        binding.itemList.addOnItemTouchListener(object:SimpleClickListener(){
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                //Log.d("AAA", " Click")

            }

            override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                //Log.d("AAA", " long Click")

            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                //Log.d("AAA", " child Click")
                if( !flag ) {
                    when (view?.id) {
                        R.id.item -> {
                            val url = listAdapter.getItem(position).url
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                        R.id.option -> {
                            val dialog = OptionDialog(this@SubActivity, listAdapter.getItem(position))
                            dialog.setOnDismissListener {
                                initUI()
                            }
                            dialog.show()
                        }
                    }
                }else{
                    listAdapter.selectItem.put(position, true)
                    listAdapter.notifyItemChanged(position)
                }
            }

            override fun onItemChildLongClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                flag = true
                listAdapter.selectItem.put(position, true)
                listAdapter.notifyItemChanged(position)
                binding.move.visibility = View.VISIBLE
                binding.del.visibility = View.VISIBLE
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
            R.id.cancel-> {
                if(flag){
                    refresh()
                }else {
                    finish()
                }
            }
            R.id.move->{
                val intent = Intent(this, MultiMoveActivity::class.java)
                val list = ArrayList<DeleteData>()

                for( s in 0..listAdapter.data.size){
                    if( listAdapter.selectItem.get(s) != null && listAdapter.selectItem.get(s)==true){
                        val item = DeleteData(listAdapter.data[s].category,
                                listAdapter.data[s].url,
                                listAdapter.data[s].urlImage,
                                listAdapter.data[s].urlTitle,
                                listAdapter.data[s].date)
                        list.add(item)
                    }
                }
                intent.putExtra("list", list)
                startActivity(intent)
                refresh()
            }
            R.id.del->{
                listAdapter.deleteItem()
                refresh()
            }
        }
    }

    fun refresh(){
        binding.move.visibility = View.GONE
        binding.del.visibility = View.GONE
        listAdapter.selectItem = HashMap<Int,Boolean>()
        listAdapter.notifyDataSetChanged()
        flag = false
    }

    override fun onBackPressed() {
        if(flag){
            refresh()
        }else {
            super.onBackPressed()
        }
    }
}
