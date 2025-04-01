package vn.edu.tlu.ex4lab5

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUrl = findViewById<EditText>(R.id.editTextUrl)
        val buttonLoad = findViewById<Button>(R.id.buttonLoad)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        buttonLoad.setOnClickListener {
            val url = editTextUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                DownloadImageTask(imageView, progressBar).execute(url)
            } else {
                editTextUrl.error = "Vui lòng nhập URL ảnh"
            }
        }
    }
}