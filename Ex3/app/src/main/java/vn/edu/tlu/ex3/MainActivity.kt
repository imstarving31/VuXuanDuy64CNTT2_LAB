package vn.edu.tlu.ex3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var btnSignIn: Button
    private lateinit var btnShowData: Button // Thêm nút Hiển thị dữ liệu
    private lateinit var tvUserInfo: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        btnSignIn = findViewById(R.id.btnSignIn)
        btnShowData = findViewById(R.id.btnShowData) // Khởi tạo nút Hiển thị dữ liệu
        tvUserInfo = findViewById(R.id.tvUserInfo)

        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Xử lý nút Đăng ký
        btnSignUp.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        // Lưu thông tin người dùng vào Realtime Database
                        val userId = user?.uid ?: ""
                        val userRef = database.getReference("users").child(userId)
                        userRef.child("email").setValue(email)
                        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                        loadUserInfo()
                    } else {
                        Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Xử lý nút Đăng nhập
        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                        loadUserInfo()
                    } else {
                        Toast.makeText(this, "Đăng nhập thất bại: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // Xử lý nút Hiển thị dữ liệu
        btnShowData.setOnClickListener {
            val user = auth.currentUser
            if (user != null) {
                val userId = user.uid
                val userRef = database.getReference("users").child(userId)
                userRef.get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val email = snapshot.child("email").getValue(String::class.java)
                        tvUserInfo.text = "Dữ liệu từ Database:\nEmail: $email"
                    } else {
                        tvUserInfo.text = "Không có dữ liệu cho người dùng này"
                    }
                }.addOnFailureListener {
                    tvUserInfo.text = "Không thể đọc dữ liệu: ${it.message}"
                }
            } else {
                tvUserInfo.text = "Vui lòng đăng nhập trước"
                Toast.makeText(this, "Vui lòng đăng nhập để hiển thị dữ liệu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserInfo() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userRef = database.getReference("users").child(userId)
            userRef.get().addOnSuccessListener { snapshot ->
                val email = snapshot.child("email").getValue(String::class.java)
                tvUserInfo.text = "Người dùng: $email"
            }.addOnFailureListener {
                tvUserInfo.text = "Không thể tải thông tin người dùng"
            }
        } else {
            tvUserInfo.text = "Chưa đăng nhập"
        }
    }
}