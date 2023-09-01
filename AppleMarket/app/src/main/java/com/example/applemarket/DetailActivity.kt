package com.example.applemarket

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.applemarket.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    var detail: ItemGoods? = null
    var position: Int = -1
    var isFavor = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        detail = intent.getParcelableExtra("DATA")
        position = intent.getIntExtra("POS", -1)
        if (detail == null) {
            Toast.makeText(this, "데이타가 없습니다.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        //판매자, 주소, 아이템, 글내용, 가격등을 화면에 표시
        binding.itemImgDetail.setImageResource(detail?.aIcon!!)
        binding.tvName.text = detail?.aSeller
        binding.tvAddress.text = detail?.aAddress
        binding.tvContentMiddleTitle.text = detail?.aName
        binding.tvContentMiddleDes.text = detail?.aIntro
        binding.tvDetailPriceBottom.text = addCommaIncludeWon(detail?.aPrice)
        binding.chDetailLike.isChecked = detail?.isFavor!!

        //백키 누르면 종료
        binding.imageViewBack.setOnClickListener {
            resultFinish()
        }
        //하트클릭이벤트
        binding.chDetailLike.setOnCheckedChangeListener { buttonView, isChecked ->
            isFavor = isChecked
            if (isChecked) {
                Snackbar.make(buttonView, "관심 목록에 추가되었습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    fun resultFinish() {
        val i = Intent().apply {
            putExtra("POS", position)
            putExtra("ISFAVOR", isFavor)
        }
        setResult(Activity.RESULT_OK, i)
        finish()
    }
    override fun onBackPressed() {
        resultFinish()
    }
}