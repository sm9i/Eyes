package com.sm9i.eyes.ui.video.presenter

import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.sm9i.eyes.ui.video.model.VideoDetailModel
import com.sm9i.eyes.ui.video.view.VideoDetailView
import com.uber.autodispose.autoDispose


class VideoDetailPresenter : BasePresenter<VideoDetailView>() {


    private var mVideoModel: VideoDetailModel = VideoDetailModel()


    /**
     * 获取相关视频
     */
    fun getRelatedVideo(id: String) {
        mVideoModel.getRelateVideoInfo(id).autoDispose(mScopeProvider).subscribe({
            mView?.getRelatedVideoInfoSuccess(it.itemList)
        }, {
            mView?.getRelatedVideoFail()
        })
    }
}