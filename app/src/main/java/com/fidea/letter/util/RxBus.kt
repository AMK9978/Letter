package com.fidea.letter.util

import com.fidea.letter.models.Board
import com.fidea.letter.models.Item
import io.reactivex.subjects.BehaviorSubject


class RxBus {
    companion object {

        private val behaviorSubject = BehaviorSubject.create<ArrayList<Item>>()

        public fun getNewItems(): BehaviorSubject<ArrayList<Item>> {
            return behaviorSubject
        }

        private val boardSubject = BehaviorSubject.create<ArrayList<Board>>()

        public fun getNewBoards(): BehaviorSubject<ArrayList<Board>> {
            return boardSubject
        }
    }
}