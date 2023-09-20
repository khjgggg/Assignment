package com.example.kakaoimagesearch_btype

import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kakaoimagesearch_btype.databinding.FragmentMyArchiveBinding
import com.example.kakaoimagesearch_btype.utils.SharedPref
import com.google.gson.GsonBuilder
import java.lang.Exception

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MyArchiveFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMyArchiveBinding
    private val staggeredMyArchiveListAdapter by lazy {
        StaggeredMyArchiveGridAdapter()
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

            override fun onLongClick(doc: ImageData.Document) {
                var builder = AlertDialog.Builder(context!!)
                builder.setTitle("연락처 삭제")
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

            override fun onClickAddFolder(doc: ImageData.Document) {
            }
        }
    }
    private fun deleteDataRefrash(doc: ImageData.Document) {
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

    private fun convertToString(imgDataList: MutableList<ImageData.Document>): String {
        //프리퍼런스에 저장하려면 객체를 String으로 변환해서 저장해야 한다.
        //해당 함수는 객체를 String으로 변환하는 함수
        return GsonBuilder()
            .disableHtmlEscaping()
            .create()
            .toJson(imgDataList)
    }

    /**
     * json String을 객체로 변환하는 함수
     */
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
         * @return A new instance of fragment MyArchiveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyArchiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}