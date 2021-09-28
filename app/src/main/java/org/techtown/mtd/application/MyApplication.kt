package org.techtown.mtd.application

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MyApplication : MultiDexApplication() {

    companion object{
        lateinit var db :FirebaseFirestore
        lateinit var auth : FirebaseAuth
        //로그인 되어 있으면 자동으로 로그인
        var email : String? = null
        fun checkAuth() : Boolean{                      //입력한 이메일이 인증 되어있는 이메일인지 확인하는 메소드
            val currentUser = auth.currentUser
            return currentUser?.let{                    //let 은 null이 아니면 실행
                email = currentUser.email
                if(currentUser.isEmailVerified){
                    true
                } else{
                    false
                }
            } ?: let{
                false
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth
    }
}