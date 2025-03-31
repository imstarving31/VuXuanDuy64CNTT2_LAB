package vn.edu.tlu.misscalls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class CallReceiver : BroadcastReceiver() {

    companion object {
        private var lastState: String? = null
        private var incomingNumber: String? = null
        private var callMissed: Boolean = false
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Kiểm tra hành động của intent
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            Log.d("CallReceiver", "State: $state, Number: $number")

            when (state) {
                TelephonyManager.EXTRA_STATE_RINGING -> {
                    incomingNumber = number
                    lastState = state
                    callMissed = true // Đánh dấu có thể là cuộc gọi nhỡ
                    Log.d("CallReceiver", "Cuộc gọi đến từ: $incomingNumber")
                }
                TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                    callMissed = false // Nếu nghe máy, không phải cuộc gọi nhỡ
                    lastState = state
                    Log.d("CallReceiver", "Cuộc gọi được nghe")
                }
                TelephonyManager.EXTRA_STATE_IDLE -> {
                    if (lastState == TelephonyManager.EXTRA_STATE_RINGING && callMissed) {
                        if (incomingNumber != null) {
                            Log.d("CallReceiver", "Cuộc gọi nhỡ từ: $incomingNumber")
                            sendSMS(context, incomingNumber!!)
                        } else {
                            Log.d("CallReceiver", "Không có số điện thoại để gửi SMS")
                        }
                    }
                    lastState = state
                    incomingNumber = null
                    callMissed = false
                    Log.d("CallReceiver", "Trạng thái IDLE")
                }
            }
        } else {
            Log.d("CallReceiver", "Intent action không phải PHONE_STATE: ${intent.action}")
        }
    }

    private fun sendSMS(context: Context, phoneNumber: String) {
        try {
            val smsManager = SmsManager.getDefault()
            val message = "Xin lỗi, tôi đang bận. Tôi sẽ gọi lại cho bạn sau!"
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d("CallReceiver", "Đã gửi SMS đến $phoneNumber")
            Toast.makeText(context, "Đã gửi SMS đến $phoneNumber", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("CallReceiver", "Lỗi khi gửi SMS: ${e.message}")
            Toast.makeText(context, "Lỗi khi gửi SMS: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}