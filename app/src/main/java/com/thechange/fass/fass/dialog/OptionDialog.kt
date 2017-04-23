package com.thechange.fass.fass.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.thechange.fass.fass.R
import com.thechange.fass.fass.activity.SaveActivity
import com.thechange.fass.fass.model.Item
import com.thechange.fass.fass.service.ogTag
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import android.support.v4.content.ContextCompat.startActivity



/**
 * Created by user on 2017. 4. 23..
 */


class OptionDialog : Dialog, View.OnClickListener {

    private lateinit var activity: Activity
    private lateinit var item : Item
    constructor(context: Activity) : super(context, android.R.style.Theme_Translucent_NoTitleBar)
    constructor(context: Activity, temp:Item) : super(context, android.R.style.Theme_Translucent_NoTitleBar){
        activity = context
        item = temp
    }

    //constructor(context: Context, theme: Int) : super(context, theme) {}
    //protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.5f
        window!!.attributes = lpWindow

        setContentView(R.layout.dialog_option)


        findViewById(R.id.categoryMove).setOnClickListener(this)
        findViewById(R.id.itemRemove).setOnClickListener(this)
        findViewById(R.id.share).setOnClickListener(this)
        findViewById(R.id.cancel).setOnClickListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.categoryMove ->{

                val intent = Intent(activity, SaveActivity::class.java)
                intent.putExtra("link2", item.url)
                intent.putExtra("category", item.category)
                intent.putExtra("date", item.date)
                activity.startActivity(intent)

                dismiss()
            }
            R.id.itemRemove -> {
                val realm = Realm.getDefaultInstance()
                realm.beginTransaction()
                val deleteItem = realm.where(Item::class.java)
                        .equalTo("category", item.category)
                        .equalTo("date", item.date)
                        .findFirst()
                deleteItem.deleteFromRealm()

                realm.commitTransaction()
                dismiss()
            }
            R.id.share->{
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, item.urlTitle)
                intent.putExtra(Intent.EXTRA_TEXT, item.url)


                val chooser = Intent.createChooser(intent, "공유")
                activity.startActivity(chooser)
                dismiss()
            }
            R.id.cancel->{
                dismiss()
            }

        }
    }

}