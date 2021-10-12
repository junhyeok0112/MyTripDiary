package org.techtown.mtd.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import org.techtown.mtd.R
import org.techtown.mtd.SaveActivity
import org.techtown.mtd.databinding.FragmentHomeBinding



class HomeFragment : Fragment() ,OnMapReadyCallback {

    lateinit var binding: FragmentHomeBinding
    private lateinit var locationSource: FusedLocationSource    //최적의 위치를 반환하는 구현체
    private lateinit var naverMap: NaverMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }
        //locationSource 정의 , 권한처리를 위해 액티비티나 프래그먼트 필요 ->this로 지정
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        //mapFragment는 지도에 대한 뷰 역할만을 담당 ,API를 호출하려면 인터페이스 역할을 하는  NaverMap객체가 필요합니다.
        //NaverMap 객체가 준비되면 onMapReady 호출
        mapFragment.getMapAsync(this)

        binding.homeAddFab.setOnClickListener {
            //내 위치에 마커 추가
            //테스트로 위도 경도 리턴 , fusedlocationsource의 현재 위치는 lastLocation과 동일
            val cameraPosition = naverMap.cameraPosition
//            Toast.makeText(context ,
//            "위도 : ${cameraPosition.target.latitude} 경도 : ${cameraPosition.target.longitude}" , Toast.LENGTH_SHORT).show()
            Toast.makeText(context ,
                "위도 : ${locationSource.lastLocation?.latitude} 경도 : ${locationSource.lastLocation?.longitude}" , Toast.LENGTH_SHORT).show()

            plusMarker(locationSource.lastLocation?.latitude!! , locationSource.lastLocation?.longitude!!)

            val intent = Intent(context, SaveActivity::class.java)
            intent.putExtra("latitude" , locationSource.lastLocation?.latitude)
            intent.putExtra("longitude", locationSource.lastLocation?.longitude)
            startActivity(intent)

        }

        return binding.root

    }

    override fun onMapReady(naverMap: NaverMap) {
        //내 위치 추적 구현하기

        //내 위치로 이동하기 버튼 활성화 -> XML에서 가능
        this.naverMap = naverMap
        naverMap.locationSource = locationSource

        //위치 오버레이 띄우기 구현
        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true

        //위치 추적 모드를 Face로 설정 ->위치 추적이 활성화되고, 현위치 오버레이, 카메라의 좌표, 베어링이 사용자의 위치 및 방향을 따라 움직입니다. API나 제스처를 사용해 임의로 카메라를 움직일 경우 모드가 NoFollow로 바뀝니다.
        naverMap.locationTrackingMode = LocationTrackingMode.Face



    }

    fun plusMarker(latitude:Double , longitude:Double){

        val marker = Marker()
        marker.position = LatLng(latitude, longitude)
        marker.map = naverMap
    }



    //위치 권한 받는 코드
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions,
                grantResults)) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }


}