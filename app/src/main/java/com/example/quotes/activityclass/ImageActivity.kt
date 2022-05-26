package com.example.quotes.activityclass

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.quotes.R
import com.example.quotes.adapter.ImagesAdapter
import com.example.quotes.adapter.ImagesPageAdapter
import com.example.quotes.apimodels.DataItem
import com.example.quotes.apimodels.ItemsResponse
import com.example.quotes.databinding.ActivityImageBinding
import com.example.quotes.retrofit.ItemClick
import com.example.quotes.retrofit.RetrofitInstance
import com.example.quotes.retrofit.RetrofitServices
import com.example.quotes.retrofit.ShareClick
import com.example.quotes.sqlitedatabase.SqliteDataBase
import com.google.android.material.imageview.ShapeableImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ImageActivity : AppCompatActivity(), ItemClick, ShareClick {

    var dialog: Dialog? = null
    var binding: ActivityImageBinding? = null
    var adapter: ImagesAdapter? = null
    var adapterPage: ImagesPageAdapter? = null
    lateinit var list: List<DataItem>
    var image: String? = null
    var sqliteDataBase: SqliteDataBase? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var loading: Boolean = true
    var page: Int = 1
    var id: Int = 0
    var imageId: Int = 0
    private var position: Int = 0
    val handler = Handler(Looper.getMainLooper())
    var pastVisibleItems: Int? = null
    var visibleItemCount: Int? = null
    var totalItemCount: Int? = null
    var MY_PREFS_NAME = "save"
    var editor: SharedPreferences.Editor? = null
    var prefs: SharedPreferences? = null
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences(this.MY_PREFS_NAME, 0)

        this.prefs = sharedPreferences

        if (sharedPreferences!!.getBoolean("isfirst", true)) {

            acceptdIPrivacy();

        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_image)

        linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        sqliteDataBase = SqliteDataBase(this, null)



        if (intent != null) {

            image = intent.getStringExtra("image")
            val name = intent.getStringExtra("name")

            id = intent.getIntExtra("id", 0)
            imageId = intent.getIntExtra("imageId", 0)
            position = intent.getIntExtra("pos", 0)
//            page = intent.getIntExtra("page",0)
            list = intent.extras?.get("imageList") as List<DataItem>

            binding?.mText?.text = name
            binding?.mMenu?.setOnClickListener { finish() }

        }

        binding?.mRecycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            @SuppressLint("NotifyDataSetChanged")
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                if (dy > 0) {

                    visibleItemCount = linearLayoutManager!!.childCount
                    totalItemCount = linearLayoutManager!!.itemCount
                    pastVisibleItems = linearLayoutManager!!.findFirstVisibleItemPosition()

                    if (loading) {

                        if ((visibleItemCount!! + pastVisibleItems!!) >= totalItemCount!!) {
                            loading = false
                            quotesPagerView(id)
                            adapter?.notifyDataSetChanged()
                            loading = true

                        }

                    }

                }

            }

        })

//        binding?.mRecycler?.scrollToPosition(position)

        quotesView(id)

    }

    private fun acceptdIPrivacy() {

        dialog = Dialog(this)

        dialog?.setContentView(R.layout.swipe)

        dialog?.setCanceledOnTouchOutside(false)

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog?.window?.attributes?.windowAnimations = android.R.style.Animation_Dialog

        val button: Button? = dialog?.findViewById(R.id.mButton)

        button?.setOnClickListener {

            val ageSelectActivity: ImageActivity = this@ImageActivity
            ageSelectActivity.editor = prefs!!.edit()
            this@ImageActivity.editor?.putBoolean("isfirst", false)
            this@ImageActivity.editor?.apply()
            dialog?.dismiss()


        }

        dialog?.show()

    }

    fun quotesView(id: Int) {

        val retrofitService =
            RetrofitInstance.getRetrofitInstance().create(RetrofitServices::class.java)

        val call = retrofitService.quotesItems("list/5/$id", page)

        call.enqueue(object : Callback<ItemsResponse> {

            override fun onResponse(call: Call<ItemsResponse>, response: Response<ItemsResponse>) {

                Log.e("TAG", "onResponse: " + response.body())

                if (response.isSuccessful) {

                    var itemList: ItemsResponse? = response.body()

                    binding?.mRecycler?.layoutManager = linearLayoutManager
                    adapterPage = ImagesPageAdapter(
                        this@ImageActivity,
                        itemList?.quotes?.data as List<DataItem>,
                        image.toString(),
                        sqliteDataBase!!,
                        imageId,
                        id,
                        this@ImageActivity,
                        this@ImageActivity
                    )

                    var snapHelper: SnapHelper = PagerSnapHelper()
                    binding?.mRecycler?.onFlingListener
                    snapHelper.attachToRecyclerView(binding?.mRecycler)
                    binding?.mRecycler?.adapter = adapterPage
                    binding?.animationView?.visibility = View.GONE

                }

            }

            override fun onFailure(call: Call<ItemsResponse>, t: Throwable) {

            }

        })

    }

    fun quotesPagerView(id: Int) {

        page++

        val retrofitService =
            RetrofitInstance.getRetrofitInstance().create(RetrofitServices::class.java)

        val call = retrofitService.quotesItems("list/5/$id", page)

        call.enqueue(object : Callback<ItemsResponse> {

            override fun onResponse(call: Call<ItemsResponse>, response: Response<ItemsResponse>) {

                Log.e("TAG", "onResponse: " + response.body())

                if (response.isSuccessful) {

                    var itemList: ItemsResponse? = response.body()

                    adapter?.addAll(itemList?.quotes?.data as List<DataItem>)

                }

            }

            override fun onFailure(call: Call<ItemsResponse>, t: Throwable) {

            }

        })

    }

    private fun permission(): Boolean {

        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun requestPermission() {

        var permission = mutableListOf<String>()

        if (!permission()) {

            permission.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }

        if (permission.isNotEmpty()) {

            ActivityCompat.requestPermissions(this, permission.toTypedArray(), 0)

        }

    }

    override fun onClick(imageView: ShapeableImageView) {

        if (!permission()) {

            requestPermission()

        } else {

            val fileOutputStream: FileOutputStream

            val file: File = getDisk()

            if (!file.exists()) {
                file.mkdirs()
            }

            val simpleDateFormat = SimpleDateFormat("yyyysshhmmss")

            val date = simpleDateFormat.format(Date())

            val name = "IMG$date.jpg"

            val fileName = file.absolutePath + "/" + name

            val newFile = File(fileName)

            try {

                val bitmap: Bitmap = viewToBitmap(imageView, imageView.width, imageView.height)

                fileOutputStream = FileOutputStream(newFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                Toast.makeText(this, "save image successfully", Toast.LENGTH_SHORT).show()
                fileOutputStream.flush()
                fileOutputStream.close()

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            refreshGallery(newFile)

        }

    }

    override fun shareClick(imageView: ShapeableImageView) {

        if (!permission()) {

            requestPermission()

        } else {
            
            val intent = Intent(Intent.ACTION_SEND).setType("image/*")

            val bitmap = imageView.drawable.toBitmap() // your imageView here.

            val bytes = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

            val path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, "", null)

            val uri = Uri.parse(path)

            intent.putExtra(Intent.EXTRA_STREAM, uri)

            startActivity(intent)
        }

    }

    private fun getDisk(): File {

        val file: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File(file, "Quotes")

    }

    private fun refreshGallery(file: File) {

        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = Uri.fromFile(file)
        sendBroadcast(intent)

    }

    private fun viewToBitmap(view: View, width: Int, height: Int): Bitmap {

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap

    }

//    private fun savePrefData(){
//        pref = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
//        val editor = pref!!.edit()
//        editor.putBoolean("isFirstTimeRun", false)
//        editor.apply()
//    }
//
//    private fun restorePrefData():Boolean{
//        pref = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
//        return pref!!.getBoolean("isFirstTimeRun", false)
//    }

}