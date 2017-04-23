package com.thechange.fass.fass.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.thechange.fass.fass.R
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.ogTag
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm


/**
 * Created by user on 2017. 4. 22..
 */


class CreateCategoryDialog : Dialog, View.OnClickListener {

    private lateinit var mBtnCancel: Button
    private lateinit  var mBtnSend: Button
    private lateinit var text : EditText
    private lateinit var link:String
    private lateinit var activity: Activity

    constructor(context: Activity) : super(context, android.R.style.Theme_Translucent_NoTitleBar)
    constructor(context: Activity, url:String) : super(context, android.R.style.Theme_Translucent_NoTitleBar){
        activity = context
        link = url
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

                Observable.fromCallable { ogTag(link) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                        .subscribe { data->
                            val realm = Realm.getDefaultInstance()
                            realm.beginTransaction()

                            val item = realm.createObject(Item::class.java, data.url) // 새 객체 만들기
                            item.category = text.text.toString()
                            item.urlImage = data.imageUrl
                            item.urlTitle = data.imageTitle
                            item.date = data.date
                            realm.commitTransaction()

                            Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_SHORT).show()
                            dismiss()
                            activity.finish()
                        }


            }

        }
    }
}