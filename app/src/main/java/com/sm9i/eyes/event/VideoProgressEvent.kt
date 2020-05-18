package com.sm9i.eyes.event


/**
 * 视频播放进度event
 */
data class VideoProgressEvent(
    var duration: Int,//总时间
    var currentPosition: Int,//当前播放时间
    var progress: Int,//播放进度
    var secondaryProgress: Int //缓存进度
)