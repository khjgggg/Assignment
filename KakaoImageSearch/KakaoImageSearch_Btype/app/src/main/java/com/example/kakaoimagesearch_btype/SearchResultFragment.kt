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
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.Exception


class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private val staggeredListAdapter by lazy {
        StaggeredGridAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

                    communicateNetWork(setUpImageParameter(it), setUpVideoParameter(it))
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

            override fun onClickAddFolder(doc: KakaoCommonData) {

                //저장된 목록을 프리퍼런스에서 가져온다
                val prevSaveList = SharedPref.getString(requireContext(), "addFolder", "")
                //가져온 목록을 리스트객체로 변환
                val prevList = convertToObject(prevSaveList)
                if (prevList.isEmpty()) {
                    //리스트가 비어있으면 리스트생성 후 추가
                    val saveList = mutableListOf<KakaoCommonData>()
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

    private fun communicateNetWork(imgParam: HashMap<String, String>, videoParam: HashMap<String, String>)
    = lifecycleScope.launch {
        val responseData = NetWorkClient.kakaoNetWork.getImage(NetWorkClient.API_AUTHKEY, imgParam)
        val responseVideoData = NetWorkClient.kakaoNetWork.getVideo(NetWorkClient.API_AUTHKEY, videoParam)

        val combinedList = mutableListOf<KakaoCommonData>()
        responseData.documents?.let { combinedList.addAll(it) }
        responseVideoData.documents?.let { combinedList.addAll(it) }
        combinedList.sortBy { it.datetime }

        requireActivity().runOnUiThread {
            (binding.srRecyclerview.adapter as StaggeredGridAdapter).addItems(combinedList)
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
    private fun setUpVideoParameter(searchText: String): HashMap<String, String> {
        return hashMapOf(
            "query" to searchText,
            "sort" to "accuracy", // accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
            "page" to "1",  //결과 페이지 번호, 1~15 사이의 값
            "size" to "15"  //한 페이지에 보여질 문서 수, 1~30 사이의 값, 기본 값
        )
    }

    private fun convertToString(list: MutableList<KakaoCommonData>): String {
        val gsonBuilder = GsonBuilder().disableHtmlEscaping()
        gsonBuilder.registerTypeAdapter(KakaoCommonData::class.java, KakaoCommonDataTypeAdapter())
        val gson = gsonBuilder.create()
        val jsonArray = JsonArray()
        for (item in list) {
            jsonArray.add(gson.toJsonTree(item, KakaoCommonData::class.java))
        }
        return jsonArray.toString()
    }

    private fun convertToObject(jsonStr: String): MutableList<KakaoCommonData> {
        return try {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(KakaoCommonData::class.java, KakaoCommonDataTypeAdapter())
            val gson = gsonBuilder.create()
            gson.fromJson(jsonStr, object : TypeToken<MutableList<KakaoCommonData>>() {}.type)
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}