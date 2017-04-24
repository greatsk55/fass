package com.thechange.fass.fass.adapter

import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.thechange.fass.fass.R
import com.thechange.fass.fass.model.Item
import io.realm.Realm

/**
 * Created by user on 2017. 4. 23..
 */



class SubItemAdapter(layoutResId: Int, data: List<Item>) : BaseQuickAdapter<Item, BaseViewHolder>(layoutResId, data) {

    public var selectItem : HashMap<Int,Boolean> = HashMap<Int,Boolean>()


    override fun convert(helper: BaseViewHolder, item: Item) {

        val image = helper.convertView.findViewById(R.id.imageUrl) as ImageView

        if( selectItem.get(helper.adapterPosition) == null ) {
            helper.setBackgroundColor(R.id.layout, ContextCompat.getColor(mContext, R.color.white))
        }else if( selectItem.get(helper.adapterPosition) == true ){
            helper.setBackgroundColor(R.id.layout, ContextCompat.getColor(mContext, R.color.alpha))
        }

        helper.setText(R.id.urlTitle, item.urlTitle)
        helper.setText(R.id.date, item.date)
        Glide.with(mContext).load(item.urlImage).into(image)
        helper.addOnClickListener(R.id.item)
        helper.addOnClickListener(R.id.option)
        helper.addOnLongClickListener(R.id.item)
    }

    public fun deleteItem(){

        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()

        for(s in 0..data.size){
            if( selectItem.get(s) != null && selectItem.get(s) == true){
                val tempItem = data[s]
                data.remove(tempItem)
                val deleteItem = realm.where(Item::class.java)
                        .equalTo("category", tempItem.category)
                        .equalTo("date", tempItem.date)
                        .equalTo("url",tempItem.url)
                        .findFirst()
                deleteItem.deleteFromRealm()
            }
        }




        realm.commitTransaction()
    }

}