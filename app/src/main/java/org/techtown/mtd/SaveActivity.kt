package org.techtown.mtd

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import org.techtown.mtd.application.MyApplication
import org.techtown.mtd.databinding.ActivitySaveBinding
import org.techtown.mtd.util.dateToString
import java.io.File
import java.util.*

//이미지와 텍스트를 저장할 액티비티 ->Glide 이용
//Glide이용해서 이미지 불러오기
class SaveActivity : AppCompatActivity() {

    lateinit var binding : ActivitySaveBinding
    lateinit var registerLauncher: ActivityResultLauncher<Intent>
    lateinit var filePath : String
    var latitude : Double? = null
    var longitude : Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("latitude") && intent.hasExtra("longitude")){
            latitude = intent.getDoubleExtra("latitude" ,0.0)
            longitude = intent.getDoubleExtra("longitude" , 0.0)
            Log.d("SaveActivity" , "위도 : ${latitude}  경도 : ${longitude}")
        }
        setLauncher()       //ActivityForResult 셋팅

        binding.saveImageIv.setOnClickListener {
            getImage()      //갤러리에서 이미지 가져오기
        }

        binding.saveExitBtn.setOnClickListener {
            finish()
        }

        binding.saveRegisterBtn.setOnClickListener {
            //데이터 저장하기
            saveData()
        }

    }

    //갤러리에서 이미지 가져오기 
    fun getImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setDataAndType(      //가져올 데이터와 타입 지정 , 미디어 저장소에 접근
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,   //외부 사진 저장소에 접근할 수 있게 하는 대표 Uri
            "image/*"
        )
        registerLauncher.launch(intent)
    }

    //다른 액티비티에서 받아온 결과 처리
    fun setLauncher(){
        registerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            Glide.with(this)
                .load(result?.data?.data)   //가져온 데이터 띄우줌
                .fitCenter()
                .into(binding.saveImageIv)

            binding.saveTextAlarmTv.visibility = View.GONE      //보이는 텍스트 없애기
            //이미지 uri 가져오는 부분 , MediaStore.Images.Media.EXTERNAL_COTENT_URI 의 filePath 검색
            //실질적으로 쿼리하는 코드 ,API 인자에 우리가 찾고자 하는 데이터 정보를 넣음 , 리턴 타입은 cursor 이것을 이용해서 데이터 확인가능
            val cursor = contentResolver.query(result.data?.data as Uri,
                arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null);
            cursor?.moveToFirst().let { //커서로 순회 해서 uri 가져옴
                filePath=cursor?.getString(0) as String
            }
        }
    }

    fun saveData(){ //uid collection에 유저아이디 , 이미지 , 텍스트, 위도 ,경도 저장
        val data = mapOf(
            "uid" to MyApplication.auth.uid,
            "latitude" to latitude,
            "longitude" to longitude,
            "content" to binding.saveTextEt.text.toString(),
            "date" to dateToString(Date())
        )

        MyApplication.db.collection("${MyApplication.auth.uid}")    //collection을 uid로
            .add(data)
            .addOnSuccessListener {
                //collection 새로 생성되면서 document도 생성하면서 안에 data 넣음 documentRef 리턴
                //store에 먼저 저장 후 스토리지에 documentID 이용해서 이미지
                Log.d("registerData" , "데이터 저장")
                uploadImage(it.id)
            }
            .addOnFailureListener {
                Log.d("registerData" , "등록실패 , ${it.message.toString()}")
            }
    }

    fun uploadImage(docId: String){     //문서 id 전달 받아서 올려야함
        //스토리지에 업로드하기
        val storage = MyApplication.storage
        //storageRef
        val storageRef = storage.reference
        //imgaes 폴더에 docId.jpg로 저장
        val imgRef = storageRef.child("images/${docId}.jpg")
        val file = Uri.fromFile(File(filePath))         //갤러리에서 가져온 파일경로를 이용해서 파일의 URi 전달
        imgRef.putFile(file)
            .addOnSuccessListener {
                Log.d("registerData" , "이미지 저장")
                Toast.makeText(this, "데이터가 저장 되었습니다." , Toast.LENGTH_SHORT).show()
                finish()    // 저장 후 액태비티 종료
            }
            .addOnFailureListener{
                Log.d("jun" , "저장 실패 "+ it)
            }
    }
}