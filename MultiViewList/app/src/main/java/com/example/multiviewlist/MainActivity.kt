package com.example.multiviewlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.multiviewlist.databinding.ActivityMainBinding
import com.example.recyclerviewtest.MyAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //어댑터 생성 및 연결
        val adapter = MyAdapter(dataList)
        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)
//        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        val stickyLayoutManager = MyStickyLayoutManager(this, adapter)
        stickyLayoutManager.elevateHeaders(true) // Default elevation of 5dp

        binding.recyclerView.layoutManager = stickyLayoutManager

        adapter.itemClick = object : MyAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val size = dataList.size
                val name:String = (dataList[position]as MixItem.MyItem).aName
                Toast.makeText(this@MainActivity, "$name 선택!!!!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

