package com.thechange.fass.fass.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.thechange.fass.fass.R
import com.thechange.fass.fass.model.Item

/**
 * Created by user on 2017. 4. 20..
 */


class CategoryAdapter(layoutResId: Int, data: List<Item>) : BaseQuickAdapter<Item, BaseViewHolder>(layoutResId, data) {


    override fun convert(helper: BaseViewHolder, item: Item) {
        //helper.setText(R.id.tag, item)

        if(helper.adapterPosition == 0 ){
            helper.setText(R.id.urlTitle, "")
            helper.setImageResource(R.id.imageUrl,R.drawable.ic_plus)
        }else{
            helper.setText(R.id.urlTitle, item.urlTitle)
            Glide.with(mContext).load(item.urlImage)
        }

        helper.addOnClickListener(R.id.item)
    }

}
