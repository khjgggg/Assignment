package com.example.kakaoimagesearch_btype

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.kakaoimagesearch_btype.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.pager.adapter = ViewPagerAdapter(this)
        binding.pager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.bottomNavigationView.menu.getItem(position).isChecked = true

                    //선택된 프래그먼트를 가져와서
                    val frag = supportFragmentManager.findFragmentByTag("f$position")
                    // 프래그먼트가 MyArchiveFragment이면 리스트를 업데이트
                    if (frag is MyArchiveFragment) {
                        frag.updateList()   //리스트 업데이트를 위한 함수
                    }
                }
            }
        )
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_one -> {
                    // ViewPager의 현재 item에 첫 번째 화면을 대입
                    binding.pager.currentItem = 0
                    true
                }
                R.id.item_two -> {
                    // ViewPager의 현재 item에 두 번째 화면을 대입
                    binding.pager.currentItem = 1
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onDestroy()
    }
}

