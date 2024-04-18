package com.example.itcenterproject.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.itcenterproject.R
import com.example.itcenterproject.adapter.ImageAdapter
import com.example.itcenterproject.databinding.ActivityWelcomeBinding
import com.example.itcenterproject.model.ModelImage
import java.util.UUID

class WelcomeActivity : AppCompatActivity() {
 lateinit var binding: ActivityWelcomeBinding
    lateinit var pageChangeList: ViewPager2.OnPageChangeCallback
    private lateinit var imageChangeRunnable: Runnable
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageList = arrayListOf(
            ModelImage(
                UUID.randomUUID().toString(),
                R.drawable.img
            ),
            ModelImage(
                UUID.randomUUID().toString(),
                R.drawable.ahmad_aka
            ),
            ModelImage(
                UUID.randomUUID().toString(),
                R.drawable.img
            )
        )

        val imageAdapter = ImageAdapter()
        binding.viewPager2?.adapter = imageAdapter
        imageAdapter.submitList(imageList)

        val dotsImage = Array(imageList.size) { ImageView(this) }

        dotsImage.forEach {
            it.setImageResource(
                R.drawable.non_active_dot
            )
            binding.dot?.addView(it)
        }
        dotsImage[0].setImageResource(R.drawable.active_dot)


        imageChangeRunnable = object : Runnable {
            override fun run() {
                val nextIndex = (binding.viewPager2!!.currentItem + 1) % imageList.size
                binding.viewPager2?.currentItem = nextIndex
                if (nextIndex == imageList.size - 1) {
                    handler.postDelayed({
                        startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                        finish()
                    }, 3000)
                } else {
                    handler.postDelayed(this, 3000) // Change image every 3 seconds
                }
            }
        }

        handler.postDelayed(imageChangeRunnable, 3000)


        pageChangeList = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotsImage.forEachIndexed { index, imageView ->
                    imageView.setImageResource(
                        if (position == index) R.drawable.active_dot else R.drawable.non_active_dot
                    )
                }
                super.onPageSelected(position)
            }
        }
        binding.tvskip!!.setOnClickListener {
            handler.removeCallbacks(imageChangeRunnable)
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
            finish()
        }
        binding.viewPager2?.registerOnPageChangeCallback(pageChangeList)
    }
}