package com.thechange.fass.fass.service

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

/**
 * Created by user on 2017. 6. 23..
 */

class RxBus {

    companion object{
        private var subject = PublishSubject.create<Any>()
        private var instance: RxBus = RxBus()
    }

    fun instanceOf(): RxBus {
        if( subject.hasComplete()){
            subject = PublishSubject.create<Any>()
        }else{

        }
        return instance
    }

    fun send( data: Any) {
        subject.onNext(`data`)
    }
    fun getEvents(): Observable<Any> {
        return subject
    }
    fun hasObservers(): Boolean {
        return subject.hasObservers()
    }

    fun complete(){
        subject.onComplete()
    }


}