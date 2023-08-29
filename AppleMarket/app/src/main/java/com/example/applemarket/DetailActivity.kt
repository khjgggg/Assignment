package com.example.applemarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.applemarket.databinding.ActivityDetailBinding
import com.example.applemarket.databinding.ActivityMainBinding

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    //    val TAG = this.javaClass.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var detail: ItemGoods? = intent.getParcelableExtra("DATA")
        if (detail == null) {
            Toast.makeText(this, "데이타가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
//        Log.d(TAG, "########"+ detail.toString())

        //판매자, 주소, 아이템, 글내용, 가격등을 화면에 표시
        binding.itemImgDetail.setImageResource(detail.aIcon)
        binding.tvName.text = detail.aSeller
        binding.tvAddress.text = detail.aAddress
        binding.tvContentMiddleTitle.text = detail.aName
        binding.tvContentMiddleDes.text = detail.aIntro
        binding.tvDetailPriceBottom.text = addCommaIncludeWon(detail.aPrice)

        val btnBack = findViewById<ImageView>(R.id.imageViewBack)
        btnBack.setOnClickListener {
            finish()
        }

    }

}