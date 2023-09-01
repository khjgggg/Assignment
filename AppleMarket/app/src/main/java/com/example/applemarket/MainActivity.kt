package com.example.applemarket

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ActivityMainBinding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkPermission()

        //어댑터 생성 및 연결
        val adapter = MyAdapter(dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.itemClick = object : MyAdapter.ItemClick {
            override fun onClick(position: Int) {

                val i = Intent(this@MainActivity, DetailActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra("DATA", dataList[position])
                    putExtra("POS", position)
                }
                resultLauncher.launch(i)
            }

            //롱클릭시 삭제 여부를 묻는 다이얼로그를 띄우고 확인을 선택시 해당 항목을 삭제하고 리스트를 업데이트
            override fun onLongClick(position: Int) {
                var builder = AlertDialog.Builder(this@MainActivity)

                builder.setTitle("상품 삭제")
                builder.setMessage("상품을 정말로 삭제하시겠습니까?")
                builder.setIcon(R.drawable.chat_image)

                val listener = object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        when (p1) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                dataList.removeAt(position)
                                adapter.notifyDataSetChanged()
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
        //플로팅버튼 애니메이션 효과
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 500 }
        var isTop = true
        //클릭시 리스트의 0번째 최상단으로 이동
        binding.btnFloating.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }
        /**RecyclerView의 Scroll 상태 변화에 따라 변화를 주어야 하므로 이를 감지하기 위하여 RecyclerView.OnScrollListener 리스너를 재정의*/
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                /**현재 리스트의 최상단을 검사하기 위해 if문 사용
                canScrollVertically(-1) : 최상단일 경우 false 값 return
                canScrollVertically(1) : 최하단일 경우 false 값 return
                RecyclerView.SCROLL_STATE_IDLE은 현재 스크롤되지 않는 상태임을 나타내며, 이를 조건에 추가해주는 이유는 스크롤에 인한 중복 발생을 방지*/
                if (!binding.recyclerView.canScrollVertically(-1)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    binding.btnFloating.startAnimation(fadeOut)
                    binding.btnFloating.visibility = View.GONE
                    isTop = true
                } else {
                    if (isTop) {
                        binding.btnFloating.visibility = View.VISIBLE
                        binding.btnFloating.startAnimation(fadeIn)
                        isTop = false
                    }
                }
            }
        })
        //상단 종모양 아이콘을 누르면 Notification을 생성
        binding.imageNoti.setOnClickListener {
            notification()
        }
    }

    //상세페이지에서 전달받은 포지션,좋아요 여부 값을 받아서 데이터랑 리스트 갱신
    @SuppressLint("NotifyDataSetChanged")
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val intent = it.data
                val pos = intent?.getIntExtra("POS", -1)
                val isFavor = intent?.getBooleanExtra("ISFAVOR", false)

                if (pos != -1 && pos != null && isFavor != null) {
                    (binding.recyclerView.adapter as MyAdapter).dataList[pos].isFavor = isFavor
                    //리스트 데이타 갱신
                    (binding.recyclerView.adapter as MyAdapter).notifyDataSetChanged()
                }
            }
        }

    //뒤로가기(BACK)버튼 클릭시 종료하시겠습니까? [확인][취소] 다이얼로그
    override fun onBackPressed() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("종료")
        builder.setMessage("정말 종료하시겠습니까?")
        builder.setIcon(R.drawable.chat_image)
        // 버튼 클릭시에 무슨 작업을 할 것인가!
        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        finish()
                    }
                    DialogInterface.BUTTON_NEUTRAL -> {}
                }
            }
        }
        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", listener)
        builder.show()
    }

    fun notification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 26 버전 이상
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                // 채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)

            // 채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(this, channelId)

        } else {
            // 26 버전 이하
            builder = NotificationCompat.Builder(this)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // 알림의 기본 정보
        builder.run {
            setSmallIcon(R.mipmap.ic_launcher)
            setWhen(System.currentTimeMillis())
            setContentTitle("키워드 알림")
            setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")
            setContentIntent(pendingIntent)
            priority = NotificationCompat.PRIORITY_HIGH

//            setStyle(NotificationCompat.BigPictureStyle()
//                    .bigPicture(bitmap)
//                    .bigLargeIcon(null))  // hide largeIcon while expanding
            addAction(R.mipmap.ic_launcher, "Action", pendingIntent)
        }
        manager.notify(11, builder.build())
    }

    private val ACTIVITY_REQUEST_PERMISSION = 1

    private fun checkPermission() {
        val permissions = getPermissions()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(permissions, ACTIVITY_REQUEST_PERMISSION)
                return
            }
        }
    }

    private fun getPermissions(): Array<String> {
        val permissions = ArrayList<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        val arrPermission = arrayOfNulls<String>(permissions.size)
        return permissions.toArray(arrPermission)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isEmpty()) {
            Toast.makeText(this, "권한 결과 데이타가 없음", Toast.LENGTH_SHORT).show()
            return
        }

        if (requestCode == ACTIVITY_REQUEST_PERMISSION) {
            var isGrantedNoti = false
            for (index in grantResults.indices) {
                if (permissions[index] == android.Manifest.permission.POST_NOTIFICATIONS
                    && grantResults[index] == PackageManager.PERMISSION_GRANTED
                ) {
                    isGrantedNoti = true
                }
            }

            if (!isGrantedNoti) {
                Toast.makeText(this, "알림 권한이 필요합니다. 설정에서 알림 권한을 켜주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


//천단위 콤마처리(1)
fun addCommaIncludeWon(str: String?): String {
    if (str == null || str.trim { it <= ' ' }.isEmpty()) {
        return "0"
    }
    val money = str
        .trim { it <= ' ' }
        .replace("[^0-9]".toRegex(), "")
    if (money.isEmpty()) {
        return "0"
    }
    val moneyFormat = DecimalFormat("###,###,###")
    return moneyFormat.format(money.toDouble()) + "원"
}