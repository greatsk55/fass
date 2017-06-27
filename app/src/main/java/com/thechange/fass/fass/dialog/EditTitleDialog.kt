package com.thechange.fass.fass.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.thechange.fass.fass.R
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.RxBus
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.realm.Realm

/**
 * Created by user on 2017. 6. 23..
 */


class EditTitleDialog : Dialog, View.OnClickListener {

    private var item :Item
    private lateinit var editText : EditText


    constructor(context: Context, item:Item) : super(context, android.R.style.Theme_Translucent_NoTitleBar){
        this.item = item
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.5f
        window!!.attributes = lpWindow

        setContentView(R.layout.dialog_title_change)

        findViewById(R.id.cancel).setOnClickListener(this)
        findViewById(R.id.ok).setOnClickListener(this)

        editText = findViewById(R.id.title) as EditText
        editText.setText(item.urlTitle)
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
                val realm = Realm.getDefaultInstance()
                realm.beginTransaction()
                val data = realm.where(Item::class.java)
                        .equalTo("category", item.category)
                        .equalTo("date", item.date)
                        .equalTo("url",item.url)
                        .findFirst()
                data.urlTitle = editText.text.toString()

                realm.commitTransaction()

                RxBus().send(true)

                dismiss()
            }
        }
    }
}