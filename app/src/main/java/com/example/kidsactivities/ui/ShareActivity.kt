package com.example.kidsactivities.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kidsactivities.R
import com.example.kidsactivities.databinding.ActivityShareBinding
import com.example.kidsactivities.model.Post
import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ShareActivity : AppCompatActivity(), View.OnClickListener, RequestListener<Drawable> {
    private val binding by lazy {
        ActivityShareBinding.inflate(layoutInflater)
    }

    private lateinit var post: Post

    companion object {
        const val SHARE_POST_CODE = "postToShare"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        post = intent.getSerializableExtra(SHARE_POST_CODE) as Post
        setPostData(post)
        binding.shareShareBtn.setOnClickListener(this)
        binding.shareBackBtn.setOnClickListener(this)
    }

    @SuppressLint("Range")
    private fun setPostData(post: Post) {
        Glide.with(this)
            .load(post.image)
            .listener(this)
            .into(binding.shareImage)
        binding.shareQuoteTv.apply {
            text = post.quote
        }
        binding.sharePostLinkTv.text = "Join us at : ${post.link}"
    }

    private fun createBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val drawable = view.background
        if (drawable != null) {
            drawable.draw(canvas)
        } else {
            canvas.drawColor(
                resources.getColor(
                    R.color.md_theme_light_secondaryContainer,
                    resources.newTheme()
                )
            )
        }
        view.draw(canvas)
        return bitmap
    }


    private fun createPngFile(view: View): File? {
        try {
            val bitmap = createBitmap(view)
            val pngFile = File(externalCacheDir, "post.png")
            compressBitmap(bitmap,pngFile)
            pngFile.setReadable(true, false)
            return pngFile
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            showToast("An error occurred, please try again")
        } catch (e: IOException) {
            e.printStackTrace()
            showToast("An error occurred, please try again")
        }
        return null
    }

    private fun compressBitmap(bitmap: Bitmap,pngFile:File){
        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
            val outStream = FileOutputStream(pngFile)
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outStream)
            outStream.flush()
            outStream.close()
        }
    }

    private fun sharePost(post:Post,view: View) {
        val pngFile = createPngFile(view)
        val uri = getPngUri(pngFile!!)
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
           putExtra(Intent.EXTRA_TEXT,"${post.quote} \n Join us at : ${post.link}")
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(intent, "Share Post"))
    }

    private fun getPngUri(pngFile: File) = FileProvider
        .getUriForFile(
            this, applicationContext
                .packageName + ".provider", pngFile
        )

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.share_shareBtn) {
            sharePost(post,binding.shareCl)
        } else {
            onBackPressed()
        }
    }

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        showToast("An error occurred")
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        binding.shareProgressBar.visibility = View.GONE
        binding.shareShareBtn.visibility = View.VISIBLE
        binding.shareCl.visibility = View.VISIBLE
        return false
    }
}