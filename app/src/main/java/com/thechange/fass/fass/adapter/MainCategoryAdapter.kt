package com.thechange.fass.fass.adapter

import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.thechange.fass.fass.R
import com.thechange.fass.fass.model.Item

/**
 * Created by user on 2017. 4. 22..
 */


class MainCategoryAdapter(layoutResId: Int, data: List<Item>) : BaseQuickAdapter<Item, BaseViewHolder>(layoutResId, data) {


    override fun convert(helper: BaseViewHolder, item: Item) {
        //helper.setText(R.id.tag, item)

        val image = helper.convertView.findViewById(R.id.imageUrl) as ImageView

        helper.setText(R.id.urlTitle, item.category)
        Glide.with(mContext).load(item.urlImage).placeholder(R.mipmap.fass).into(image)


        helper.addOnClickListener(R.id.item)
    }

}
