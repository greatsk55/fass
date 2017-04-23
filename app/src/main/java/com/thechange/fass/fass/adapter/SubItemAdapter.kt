package com.thechange.fass.fass.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.thechange.fass.fass.R
import com.thechange.fass.fass.model.Item

/**
 * Created by user on 2017. 4. 23..
 */



class SubItemAdapter(layoutResId: Int, data: List<Item>) : BaseQuickAdapter<Item, BaseViewHolder>(layoutResId, data) {


    override fun convert(helper: BaseViewHolder, item: Item) {

        val image = helper.convertView.findViewById(R.id.imageUrl) as ImageView

        helper.setText(R.id.urlTitle, item.urlTitle)
        helper.setText(R.id.date, item.date)
        Glide.with(mContext).load(item.urlImage).into(image)


        helper.addOnClickListener(R.id.item)
        helper.addOnClickListener(R.id.date)
    }

}