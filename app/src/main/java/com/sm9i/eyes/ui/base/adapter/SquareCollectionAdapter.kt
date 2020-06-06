package com.sm9i.eyes.ui.base.adapter

import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.widget.font.FontType
import com.sm9i.eyes.widget.font.TypefaceManager


/**
 * 横向的list  全部排行
 */
class SquareCollectionAdapter(data: MutableList<Content>) :
    BaseQuickAdapter<Content, BaseViewHolder>(R.layout.item_square_collection, data) {
    override fun convert(helper: BaseViewHolder, item: Content) {
        val imageView = helper.getView<SimpleDraweeView>(R.id.iv_simple_image)
        val coverView = helper.getView<FrameLayout>(R.id.fl_cover)
        if (item.type != "actionCard") {
            imageView.setImageURI(item.data.image)
            helper.setText(R.id.tv_title, item.data.title)
            coverView.foreground =
                mContext?.resources?.getDrawable(R.drawable.selector_item_square_foreground, null)
        } else {
            imageView.setImageDrawable(
                mContext.resources.getDrawable(
                    R.drawable.shape_show_all_border,
                    null
                )
            )
            helper.setText(R.id.tv_title, item.data.text)
            helper.setTextColor(R.id.tv_title, mContext.resources.getColor(R.color.black))
            helper.setTypeface(R.id.tv_title, TypefaceManager.getTypeFace(FontType.NORMAL))
            coverView.foreground = null
        }
    }

}