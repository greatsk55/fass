package com.thechange.fass.fass.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.thechange.fass.fass.R
import com.thechange.fass.fass.model.DeleteData
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.ogTag
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import java.util.concurrent.TimeUnit

/**
 * Created by user on 2017. 4. 24..
 */


class MultiMoveCategory : Dialog, View.OnClickListener {

    private lateinit var mBtnCancel: Button
    private lateinit  var mBtnSend: Button
    private lateinit var text : EditText
    private lateinit var activity: Activity

    constructor(context: Activity) : super(context, android.R.style.Theme_Translucent_NoTitleBar){
        activity = context
    }

    //constructor(context: Context, theme: Int) : super(context, theme) {}
    //protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.5f
        window!!.attributes = lpWindow

        setContentView(R.layout.dialog_category)

        text = findViewById(R.id.name) as EditText
        mBtnCancel = findViewById(R.id.cancel) as Button
        mBtnCancel.setOnClickListener(this)
        mBtnSend = findViewById(R.id.ok) as Button
        mBtnSend.setOnClickListener(this)


        text.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                //Enter key Action
                if (event.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Observable.fromCallable { okClicked() }
                            .subscribeOn(Schedulers.io())
                            .throttleFirst(3, TimeUnit.SECONDS)
                            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                            .subscribe{}

                    return true
                }
                return false
            }
        })

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancel ->{
                dismiss()
            }
            R.id.ok -> {
                okClicked()

            }

        }
    }

    fun okClicked(){
        Observable.fromCallable { }
                .subscribeOn(Schedulers.io())
                .throttleFirst(3, TimeUnit.SECONDS)
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe{
                    val list = activity.intent.getParcelableArrayListExtra<DeleteData>("list")

                    val realm = Realm.getDefaultInstance()
                    realm.beginTransaction()

                    for( temp in list ){
                        val item = realm.createObject(Item::class.java) // 새 객체 만들기
                        item.url = temp.url
                        item.category = text.text.toString()
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
                    Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
                    dismiss()

                    activity.finish()

                }


    }
}