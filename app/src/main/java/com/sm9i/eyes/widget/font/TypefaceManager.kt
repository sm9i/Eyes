package com.sm9i.eyes.widget.font

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.sm9i.eyes.App
import com.sm9i.eyes.R
import java.util.jar.Attributes


/**
 * 字体工具管理类
 */
object TypefaceManager {

    private val mTypeFaceMap: MutableMap<FontType, Typeface> = mutableMapOf()
    //默认字体
    private var mTypeFaceIndex: Int = FontType.NORMAL.index

    /**
     * 设置text 字体，如果参数中有字体，就设置
     */
    fun setTextTypeFace(context: Context, attributes: AttributeSet?, textView: TextView) {
        if (textView.typeface != null && textView.typeface.style != 0) {
            return
        }
        val typeArray = context.obtainStyledAttributes(attributes, R.styleable.CustomFontTextView)
        mTypeFaceIndex =
            typeArray.getInteger(R.styleable.CustomFontTextView_font_name, mTypeFaceIndex)
        if (mTypeFaceIndex in 0..FontType.values().size) {
            textView.typeface = getTypeFace(FontType.values()[mTypeFaceIndex])
        }
        typeArray.recycle()
    }

    /**
     * 根据字体设置textview的显示字体
     */
    fun setTextTypeFace(textView: TextView, fontType: FontType?) {
        val localTypeFace = getTypeFace(fontType)
        textView.typeface = localTypeFace
    }

    /**
     * 根据名称获取字体类型
     */
    fun getFontTypeByName(fontName: String?): FontType? {
        return FontType.values().firstOrNull { it.fontName == fontName }

    }

    /**
     * 根据字体类型获取字体
     */
    fun getTypeFace(fontType: FontType?): Typeface? {
        return fontType?.let {
            var typeFace = mTypeFaceMap[fontType]
            if (typeFace == null) {
                typeFace = Typeface.createFromAsset(App.INSTANCE.assets, fontType.path)
                mTypeFaceMap[fontType] = typeFace
            }
            return typeFace
        }
    }
}