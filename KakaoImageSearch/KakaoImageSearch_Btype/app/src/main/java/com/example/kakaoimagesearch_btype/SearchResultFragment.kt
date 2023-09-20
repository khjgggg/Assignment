package com.example.kakaoimagesearch_btype

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kakaoimagesearch_btype.databinding.FragmentSearchResultBinding
import com.example.kakaoimagesearch_btype.retrofit.NetWorkClient
import com.example.kakaoimagesearch_btype.utils.SharedPref
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import java.lang.Exception

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchResultFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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

        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        binding.svSearch.isSubmitButtonEnabled = true
        binding.svSearch.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    SharedPref.setString(requireContext(),"prevSearch", it)

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

        staggeredListAdapter.itemClick = object : StaggeredGridAdapter.ItemClick {
            override fun onClick(position: Int) { }

            override fun onLongClick(position: Int) { }

            override fun onClickAddFolder(doc: ImageData.Document) {

                //저장된 목록을 프리퍼런스에서 가져온다
                val prevSaveList = SharedPref.getString(requireContext(), "addFolder", "")
                //가져온 목록을 리스트객체로 변환
                val prevList = convertToObject(prevSaveList)
                if (prevList.isEmpty()) {
                    //리스트가 비어있으면 리스트생성 후 추가
                    val saveList = mutableListOf<ImageData.Document>()
                    saveList.add(doc)
                    SharedPref.setString(requireContext(), "addFolder", convertToString(saveList))

                    Toast.makeText(requireContext(),"내 보관함에 저장.", Toast.LENGTH_SHORT).show()
                }
                else {
                    //리스트가 비이있지 않으면
                    if (!prevList.contains(doc)) {
                        //중복체크 후, 중복이 아니면 추가
                        prevList.add(doc)
                        Toast.makeText(requireContext(),"내 보관함에 저장.", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        //중복이면 중복 토스트 표시
                        Toast.makeText(requireContext(),"이미 저장되어 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                    //추가한 목록을 저장한다
                    SharedPref.setString(requireContext(), "addFolder", convertToString(prevList))
                }

            }
        }

        //저장해둔 이전 검색어를 자동으로 입력해서 검색한다.
        val prevText = SharedPref.getString(requireContext(), "prevSearch", "")
        if (prevText.isNotEmpty()) {
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

    private fun convertToString(imgDataList: MutableList<ImageData.Document>): String {
        //프리퍼런스에 저장하려면 객체를 String으로 변환해서 저장해야 한다.
        //해당 함수는 객체를 String으로 변환하는 함수
        return GsonBuilder()
            .disableHtmlEscaping()
            .create()
            .toJson(imgDataList)
    }

    private fun convertToObject(jsonStr: String): MutableList<ImageData.Document> {
        return try {
            GsonBuilder().create().fromJson(jsonStr, Array<ImageData.Document>::class.java).toMutableList()
        } catch (e: Exception) {
            mutableListOf()
        }
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