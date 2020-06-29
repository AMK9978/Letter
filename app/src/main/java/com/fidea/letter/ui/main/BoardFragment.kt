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
        val dramma = Board()
        dramma.imagePath = "https://literatelai.files.wordpress.com/2012/05/666px-p_culture_violet.png"
        dramma.title =  "درام"

        val commedy = Board()
        commedy.imagePath = "https://lh3.googleusercontent.com/proxy/k99lee5RYiDRR3an-XiHkV7v3clWj2vxEW2xWgFeePX_IdzRqn6_RRn1VEVbj_kZIttKLMlpjpMIv-D6_bXJQ9NwyIideQBD4EsYrzYj0y5UwKOq1fI4NtjNb2Og2yeEWHfBHQ"
        commedy.title =  "کمدی"

        val crime = Board()
        crime.imagePath = "https://image.slidesharecdn.com/crimegenre-140126060700-phpapp02/95/crime-genre-1-638.jpg?cb=1390716461"
        crime.title =  "جنایی"

        arrayList.add(dramma)
        arrayList.add(commedy)
        arrayList.add(crime)
        adapter =
            BoardsAdapter(
                context!!,
                arrayList
            )
        boards.adapter = adapter
        boards.layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)

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