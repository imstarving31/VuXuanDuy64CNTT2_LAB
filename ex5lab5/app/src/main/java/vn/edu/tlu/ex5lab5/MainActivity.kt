package vn.edu.tlu.ex5lab5

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTimer: TextView
    private var seconds = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var timerRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewTimer = findViewById(R.id.textViewTimer)

        timerRunnable = object : Runnable {
            override fun run() {
                seconds++
                val timeString = formatTime(seconds)
                textViewTimer.text = timeString
                handler.postDelayed(this, 1000) // Cập nhật sau mỗi 1 giây
            }
        }

        startTimer()
    }

    override fun onStop() {
        super.onStop()
        stopTimer()
    }

    private fun startTimer() {
        handler.post(timerRunnable)
    }

    private fun stopTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun formatTime(totalSeconds: Int): String {
        val seconds = totalSeconds % 60
        val minutes = (totalSeconds / 60) % 60
        val hours = totalSeconds / 3600
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}