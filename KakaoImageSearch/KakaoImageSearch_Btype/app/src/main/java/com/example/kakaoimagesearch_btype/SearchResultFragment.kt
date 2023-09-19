package com.example.kakaoimagesearch_btype

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kakaoimagesearch_btype.databinding.FragmentSearchResultBinding
import com.example.kakaoimagesearch_btype.retrofit.NetWorkClient
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
const val PREF_KEY = "PREF_KEY"

class SearchResultFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPref: SharedPreferences

    private lateinit var binding: FragmentSearchResultBinding
    private val staggeredListAdapter by lazy {
        StaggeredGridAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPref = requireActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        binding.svSearch.isSubmitButtonEnabled = true
        binding.svSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    with(sharedPref.edit()) {
                        putString("prevSearch", it)
                        apply()
                    }

                    communicateNetWork(setUpImageParameter(it))
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.srRecyclerview.adapter = staggeredListAdapter
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE;
        binding.srRecyclerview.layoutManager = staggeredGridLayoutManager

        //저장해둔 이전 검색어를 자동으로 입력해서 검색한다.
        val prevText = sharedPref.getString("prevSearch", "")
        if (prevText?.isNotEmpty() == true) {
            binding.svSearch.setQuery(prevText, true)
        }
    }

    private fun communicateNetWork(param: HashMap<String, String>) = lifecycleScope.launch {
        val responseData = NetWorkClient.kakaoNetWork.getImage(NetWorkClient.API_AUTHKEY, param)

        requireActivity().runOnUiThread {
            responseData.documents?.let {
                (binding.srRecyclerview.adapter as StaggeredGridAdapter).addItems(it)
            }
        }
    }

    private fun setUpImageParameter(searchText: String): HashMap<String, String> {
        return hashMapOf(
            "query" to searchText,
            "sort" to "accuracy", // accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
            "page" to "1",  //결과 페이지 번호, 1~50 사이의 값, 기본 값 1
            "size" to "80"  //한 페이지에 보여질 문서 수, 1~80 사이의 값, 기본 값 80
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}