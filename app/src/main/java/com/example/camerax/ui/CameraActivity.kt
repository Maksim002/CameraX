package com.example.camerax.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.camerax.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {
    private lateinit var db: AppDataBase
    private val simpleDateTime = SimpleDateFormat("yyyy.MM.dd h:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initClick()
        db = AppDataBase.instance(this)
    }

    private fun initClick() {
        btnPhoto.setOnClickListener {
            camera.captureImage { cameraKitView, bytes ->
                CoroutineScope(Dispatchers.IO).launch {
                    val currentDateAndTime: String = simpleDateTime.format(Date())
                    val model = DbModel(generateID(4).toInt(), currentDateAndTime,  bytes)
                    db.appDataBase().insert(model)
                }
                Glide
                    .with(applicationContext)
                    .load(bytes)
                    .into(imagePhotoCamera)
            }
        }

        imagePhotoCamera.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (db.appDataBase().getAllModel().isEmpty()) {
                    imagePhotoCamera.setImageBitmap(null)
                }
                val intent = Intent(this@CameraActivity, GalleryActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun generateID(size: Int): String {
        val source = "0123456789"
        return (source).map { it }.shuffled().subList(0, size).joinToString("")
    }

    override fun onStart() {
        super.onStart()
        camera.onStart()
    }

    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onPause() {
        camera.onPause()
        super.onPause()
    }

    override fun onStop() {
        camera.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}