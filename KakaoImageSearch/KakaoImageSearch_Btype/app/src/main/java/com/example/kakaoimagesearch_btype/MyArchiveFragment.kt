package com.example.kakaoimagesearch_btype

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kakaoimagesearch_btype.databinding.FragmentMyArchiveBinding
import com.example.kakaoimagesearch_btype.utils.SharedPref
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import java.lang.Exception

class MyArchiveFragment : Fragment() {

    private lateinit var binding: FragmentMyArchiveBinding
    private val staggeredMyArchiveListAdapter by lazy {
        StaggeredMyArchiveGridAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyArchiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myArchiveRecyclerview.adapter = staggeredMyArchiveListAdapter
        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE;
        binding.myArchiveRecyclerview.layoutManager = layoutManager

        updateList()    //화면을 생성하면 데이타를 업데이트한다.

        staggeredMyArchiveListAdapter.itemClick = object : StaggeredMyArchiveGridAdapter.ItemClick {
            override fun onClick(position: Int) { }

            override fun onLongClick(doc: KakaoCommonData) {
                var builder = AlertDialog.Builder(context!!)
                builder.setTitle("내 보관함에서 삭제")
                builder.setMessage("정말로 삭제하시겠습니까?")
                builder.setIcon(R.drawable.ic_baseline_delete_outline_24)
                val listener = object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, p1: Int) {
                        when (p1) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                deleteDataRefrash(doc)
                            }
                            DialogInterface.BUTTON_NEGATIVE -> {
                            }
                        }
                    }
                }
                builder.setPositiveButton("확인", listener)
                builder.setNegativeButton("취소", listener)
                builder.show()
            }
        }
    }
    private fun deleteDataRefrash(doc: KakaoCommonData) {
        //저장된 목록을 프리퍼런스에서 가져온다
        val prevSaveList = SharedPref.getString(requireContext(), "addFolder", "")
        //가져온 목록을 리스트객체로 변환
        val prevList = convertToObject(prevSaveList)

        if (prevList.contains(doc)) {
            prevList.remove(doc)
        }
        //추가한 목록을 저장한다
        SharedPref.setString(requireContext(), "addFolder", convertToString(prevList))
        updateList()
    }
    fun updateList() {
        //프리퍼런스에서 검색프래그먼트에서 저장했던 목록을 가져온다
        val prevSaveList = SharedPref.getString(requireContext(), "addFolder", "")
        //String으로 저장했던 Json String을 MutableList로 변환하고
        val prevList = convertToObject(prevSaveList)
        //어댑터에 추가한다
        staggeredMyArchiveListAdapter.addItems(prevList)
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