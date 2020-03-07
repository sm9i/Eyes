package com.sm9i.eyes.widget.font

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * 自定义字体
 */
open class CustomFontView : TextView {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 根据字体类型设置字体
     */
    fun setFontType(fontType: FontType) {
        if (!isInEditMode) {
            TypefaceManager.setTextTypeFace(this, fontType)
        }
    }


    /**
     * 根据字体名称设置，如果没有找到就用默认
     */
    fun setFontType(fontName: String?, defaultFontType: FontType = FontType.BOLD) {
        if (!isInEditMode) {
            val fontType = TypefaceManager.getFontTypeByName(fontName)
            TypefaceManager.setTextTypeFace(this, fontType ?: defaultFontType)
        }
    }

}