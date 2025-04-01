package android.example.com.audiorecorder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var audioFilePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStartRecording = findViewById<Button>(R.id.btnStartRecording)
        val btnStopRecording = findViewById<Button>(R.id.btnStopRecording)
        val btnPlayRecording = findViewById<Button>(R.id.btnPlayRecording)

        // Kiểm tra quyền
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 0)
        }

        // Lưu đường dẫn file ghi âm
        audioFilePath = externalCacheDir?.absolutePath + "/recorded_audio.3gp"

        // Bắt đầu ghi âm
        btnStartRecording.setOnClickListener {
            startRecording()
            btnStartRecording.visibility = android.view.View.GONE
            btnStopRecording.visibility = android.view.View.VISIBLE
        }

        // Dừng ghi âm
        btnStopRecording.setOnClickListener {
            stopRecording()
            btnStopRecording.visibility = android.view.View.GONE
            btnPlayRecording.visibility = android.view.View.VISIBLE
            btnStartRecording.visibility = android.view.View.VISIBLE
        }

        // Phát lại file ghi âm
        btnPlayRecording.setOnClickListener {
            playRecording()
        }
    }

    // Hàm bắt đầu ghi âm
    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)  // Lấy âm thanh từ micro
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)  // Định dạng file
            setOutputFile(audioFilePath)  // Lưu file tại đường dẫn
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)  // Bộ mã hóa âm thanh
            try {
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Hàm dừng ghi âm
    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    // Hàm phát lại file ghi âm
    private fun playRecording() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioFilePath)
                prepare()
                start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
