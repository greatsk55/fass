package com.thechange.fass.fass.adapter

import android.support.v4.content.ContextCompat
import android.widget.ImageView
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

        val image = helper.convertView.findViewById(R.id.imageUrl) as ImageView

        if(helper.adapterPosition == 0 ){
            helper.setText(R.id.urlTitle, "")
            helper.setImageResource(R.id.imageUrl,R.drawable.ic_plus)
            helper.setBackgroundColor(R.id.imageUrl, ContextCompat.getColor(mContext,R.color.colorPrimaryDark))
            helper.setBackgroundColor(R.id.dim, ContextCompat.getColor(mContext,R.color.alphaWhite))
        }else{
            helper.setBackgroundColor(R.id.imageUrl, ContextCompat.getColor(mContext,R.color.alphaWhite))
            helper.setText(R.id.urlTitle, item.category)
            Glide.with(mContext).load(item.urlImage).into(image)
            helper.setBackgroundRes(R.id.dim, R.drawable.dim)

        }
        helper.addOnClickListener(R.id.item)
    }

}
