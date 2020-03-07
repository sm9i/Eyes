package com.sm9i.eyes.widget.font

/**
 * 字体枚举类，
 */
enum class FontType(var index: Int, var fontName: String, val path: String) {

    NORMAL(0, "Normal", "fonts/FZLanTingHeiS-L-GB-Regular.TTF"),//方正兰亭细黑简体
    BOLD(1, "Bold", "fonts/FZLanTingHeiS-DB1-GB-Regular.TTF"),//方正兰亭中粗黑简体
    FUTURE(2, "Future", "fonts/Futura-CondensedMedium.ttf"),//拉丁
    LOBSTER(3, "Lobster", "fonts/Lobster-1.4.otf");//龙虾字体


}