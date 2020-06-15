package com.fidea.letter.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fidea.letter.R
import com.fidea.letter.RxBus
import com.fidea.letter.Util.Companion.getCacheDir
import com.fidea.letter.Util.Companion.getToken
import com.fidea.letter.adapters.BoardsAdapter
import com.fidea.letter.models.Board
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.boards_fragment.view.*

class BoardFragment : Fragment() {

    private var arrayList = arrayListOf<Board>()
    private lateinit var boards : RecyclerView
    private var adapter: BoardsAdapter? = null
    private lateinit var disposable: Disposable
    private lateinit var boardViewModel: BoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.boards_fragment, container, false)
        boards = view.boardsRecycler
        initBoards()
        return view
    }

    private fun initBoards() {
        boardViewModel = BoardViewModel(getToken(context!!), getCacheDir(context!!))
        adapter =
            BoardsAdapter(
                context!!,
                arrayList
            )
        boards.adapter = adapter
        boards.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    }

    override fun onResume() {
        super.onResume()
        disposable = RxBus.getNewBoards().subscribeWith(object :
            DisposableObserver<ArrayList<Board>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ArrayList<Board>) {
                adapter?.itemsList!!.addAll(t)
                adapter?.notifyDataSetChanged()
                boardViewModel.boards.value?.addAll(t)
            }

            override fun onError(e: Throwable) {
            }

        })
    }

}