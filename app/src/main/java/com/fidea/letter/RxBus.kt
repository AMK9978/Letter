package com.fidea.letter

import com.fidea.letter.models.Item
import io.reactivex.subjects.BehaviorSubject


class RxBus {
    companion object {

        private val behaviorSubject = BehaviorSubject.create<ArrayList<Item>>()

        public fun getNewItems(): BehaviorSubject<ArrayList<Item>> {
            return behaviorSubject
        }
    }
}