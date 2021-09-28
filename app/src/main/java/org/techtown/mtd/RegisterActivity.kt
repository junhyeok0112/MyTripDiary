package org.techtown.mtd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import org.techtown.mtd.application.MyApplication
import org.techtown.mtd.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerRegisterBtn.setOnClickListener {
            val id = binding.registerIdEt.text.toString()
            val pw = binding.registerPwEt.text.toString()
            register(id, pw)
        }

        binding.registerExitBtn.setOnClickListener {
            finish()
        }

    }

    fun register(id:String , pw :String){
           MyApplication.auth.createUserWithEmailAndPassword(id,pw)
               .addOnCompleteListener(this){task->
                   if(task.isSuccessful){
                       val firebaseUser = MyApplication.auth.currentUser
                       val account = UserAccount()
                       account.idToken = firebaseUser?.uid
                       account.emailId = firebaseUser?.email
                       account.password = pw
                       MyApplication.db.collection("Users")
                           .document(firebaseUser!!.uid)
                           .set(account)
                           .addOnSuccessListener {
                               Toast.makeText(this,"회원가입에 성공하셨습니다." , Toast.LENGTH_SHORT).show()
                               finish()
                           }
                           .addOnFailureListener {
                                Toast.makeText(this,"회원가입에 실패하셨습니다." , Toast.LENGTH_SHORT).show()
                           }
                   } else{
                       Toast.makeText(this,"회원가입에 실패하셨습니다." , Toast.LENGTH_SHORT).show()
                   }

               }
    }
}