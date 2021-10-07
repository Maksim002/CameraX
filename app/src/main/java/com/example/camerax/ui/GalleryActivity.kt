package com.example.camerax.ui

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.camerax.R
import com.example.camerax.adapter.GalleryAdapter
import com.example.camerax.adapter.GalleryListener
import kotlinx.android.synthetic.main.activity_gallery.*
import com.example.camerax.model.GalleryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList
import java.util.zip.*
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import android.provider.MediaStore
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory


class GalleryActivity : AppCompatActivity(), GalleryListener {
    private lateinit var db: AppDataBase
    private val BUFFER = 1024
    private var adapter = GalleryAdapter(this)
    private var listIm: ArrayList<GalleryModel> = arrayListOf()
    private var listUrl: ArrayList<Uri> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        db = AppDataBase.instance(this)

        CoroutineScope(Dispatchers.IO).launch {
            val date = db.appDataBase().getAllModel()
            for (i in 1..date.size) {
                listIm.add(GalleryModel(date[i - 1].id, date[i - 1].data, date[i - 1].time))

                try {
                    val bmp = BitmapFactory.decodeByteArray(date[i - 1].data, 0, date[i - 1].data.size)
                    listUrl.add(getImageUri(this@GalleryActivity, bmp)!!)
                }catch (e: Exception){
                    e.printStackTrace()
                }

                if (i == date.size) {
                    zip(listUrl, "ZIP_FILE_NAME_HERE")
                    runOnUiThread(Runnable {
                        adapter.update(listIm)
                        recyclerView.adapter = adapter
                    })
                }
            }
        }
    }

    override fun onClickListener(id: Int, image: ByteArray, current: String, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.appDataBase().deleteWord(DbModel(id, current, image))
            runOnUiThread(Runnable {
                try {
                    adapter.items.removeAt(position)
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


    private fun zip(files: ArrayList<Uri>, zipFileName: String?) {
        try {
            var origin: BufferedInputStream? = null
            val dest = FileOutputStream(zipFileName)
            val out = ZipOutputStream(BufferedOutputStream(dest))
            val data = ByteArray(BUFFER)
            for (uri in files) {
                val stringUri = uri.toString()
                val fi = openFileInput(stringUri)
                origin = BufferedInputStream(fi, BUFFER)
                val entry = ZipEntry(stringUri.substring(stringUri.lastIndexOf("/") + 1))
                out.putNextEntry(entry)
                var count: Int
                while (origin.read(data, 0, BUFFER).also { count = it } != -1) {
                    out.write(data, 0, count)
                }
                origin.close()
            }
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}