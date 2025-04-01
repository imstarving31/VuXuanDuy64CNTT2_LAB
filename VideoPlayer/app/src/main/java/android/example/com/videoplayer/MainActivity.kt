package android.example.com.videoplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.VideoView
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController

    // Sử dụng Activity Result API để thay thế startActivityForResult
    private val videoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val videoUri: Uri? = result.data?.data
                if (videoUri != null) {
                    playVideo(videoUri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        videoView = findViewById(R.id.videoView)
        val btnPickVideo = findViewById<Button>(R.id.btnPickVideo)
        val btnLoadUrl = findViewById<Button>(R.id.btnLoadUrl)
        val edtVideoUrl = findViewById<EditText>(R.id.edtVideoUrl)

        // Thêm MediaController để điều khiển phát video (play, pause, stop, tua)
        mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Chọn video từ thư viện
        btnPickVideo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "video/*"
            videoPickerLauncher.launch(intent)
        }

        // Phát video từ URL nhập vào
        btnLoadUrl.setOnClickListener {
            val url = edtVideoUrl.text.toString()
            if (url.isNotEmpty()) {
                playVideo(Uri.parse(url))
            }
        }
    }

    // Phát video từ URI
    private fun playVideo(videoUri: Uri) {
        videoView.setVideoURI(videoUri)
        videoView.start()
    }
}
