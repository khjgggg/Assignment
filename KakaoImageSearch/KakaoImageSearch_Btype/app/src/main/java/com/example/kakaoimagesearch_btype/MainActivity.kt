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
}

