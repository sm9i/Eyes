package com.sm9i.eyes.rx

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.lang.RuntimeException
import java.util.concurrent.ConcurrentHashMap


/**
 * eventbus
 */
object RxBus {

    private val mBus = PublishSubject.create<Any>().toSerialized()
    private val mSubjects = ConcurrentHashMap<String, CompositeDisposable>()


    /**
     * 发送一个事件
     */
    @JvmStatic
    fun post(o: Any) = mBus.onNext(o)

    /**
     * 根据事件传递的eventType 返回特定类型的 被观察者
     */
    private fun <T> toObservable(eventType: Class<T>): Observable<T> {
        return mBus.ofType(eventType)
    }


    /**
     * 订阅
     */
    private fun <T> doSubscribe(
        eventType: Class<T>,
        action: Consumer<T>,
        error: Consumer<Throwable>
    ): Disposable {
        return toObservable(eventType).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(action, error)
    }

    /**
     * 保存订阅后的disposable
     */
    private fun addDisposable(any: Any, disposable: Disposable) {
        val key = any.javaClass.name
        if (mSubjects[key] != null) {
            mSubjects[key]?.add(disposable)
        } else {
            val disposables = CompositeDisposable()
            disposables.add(disposable)
            mSubjects[key] = disposables
        }
    }


    /**
     * 注册事件
     */
    fun <T> register(subscribe: Any, event: Class<T>, action: Consumer<T>) {
        val disposable = doSubscribe(
            event,
            action,
            Consumer { throwable -> throw RuntimeException(throwable.message) })
        addDisposable(subscribe, disposable)
    }


    /**
     * 注销监听
     */
    fun unRegister(subscribe: Any) {
        val key = subscribe.javaClass.name

        if (mSubjects.containsKey(key) && mSubjects[key] != null) {
            mSubjects[key]?.dispose()
        }
        mSubjects.remove(key)
    }


}