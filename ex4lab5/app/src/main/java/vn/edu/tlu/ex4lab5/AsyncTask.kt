package vn.edu.tlu.ex4lab5

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(
    private val imageView: ImageView,
    private val progressBar: ProgressBar
) : AsyncTask<String, Integer, Bitmap>() {

    override fun onPreExecute() {
        super.onPreExecute()
        progressBar.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg urls: String?): Bitmap? {
        val imageUrl = urls[0]
        var bitmap: Bitmap? = null
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    override fun onProgressUpdate(vararg values: Integer?) {
        super.onProgressUpdate(*values)
        // Bạn có thể cập nhật ProgressBar ở đây nếu muốn hiển thị tiến trình chi tiết hơn
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        progressBar.visibility = View.GONE
        if (result != null) {
            imageView.setImageBitmap(result)
        } else {
            // Xử lý lỗi khi tải ảnh không thành công (ví dụ: hiển thị ảnh mặc định hoặc thông báo lỗi)
            imageView.setImageResource(R.drawable.baseline_error_24) // Tạo một drawable ic_error
        }
    }
}