package com.example.kakaoimagesearch_btype

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.kakaoimagesearch_btype.retrofit.NetWorkClient
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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

        communicateNetWork(setUpImageParameter("뉴진스")) //text는 입력된 검색어
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    private fun communicateNetWork(param: HashMap<String, String>) = lifecycleScope.launch{
        val responseData = NetWorkClient.kakaoNetWork.getImage(NetWorkClient.API_AUTHKEY, param)

        Log.d("로그", responseData.toString())
    }
    private fun setUpImageParameter(searchText: String) : HashMap<String, String>{

        return  hashMapOf(
            "query" to searchText,
            "sort" to "recency", // accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
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