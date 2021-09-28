package org.techtown.mtd

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import org.techtown.mtd.application.MyApplication
import org.techtown.mtd.databinding.ActivityLoginBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(MyApplication.checkAuth()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        binding.loginLoginBtn.setOnClickListener {
            val id = binding.loginIdEt.text.toString()
            val pw = binding.loginPwEt.text.toString()
            Login(id,pw)

        }

        binding.loginRegisterBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun Login(id: String , pw :String){
        //모든 문서 가져오기
        MyApplication.auth.signInWithEmailAndPassword(id, pw)
            .addOnCompleteListener(this){task->
                if(task.isSuccessful){//로그인 성공
                    MyApplication.email = id        //전역변수의 이메일 갱신
                    Toast.makeText(this,"로그인에 성공하셨습니다." , Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else{
                    Toast.makeText(this,"로그인에 실패하셨습니다." , Toast.LENGTH_SHORT).show()
                    binding.loginIdEt.text.clear()
                    binding.loginPwEt.text.clear()
                }
            }
    }

}