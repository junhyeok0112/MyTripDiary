package org.techtown.mtd


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import org.techtown.mtd.Fragment.BoardFragment
import org.techtown.mtd.Fragment.HomeFragment
import org.techtown.mtd.databinding.ActivityMainBinding
import org.techtown.mtd.util.myCheckPermission


class MainActivity : AppCompatActivity()  {

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        myCheckPermission(this)             //파일 접근 권한 허용

        binding.mainBottomNavigationBn.setOnItemSelectedListener {
            when(it.itemId){      //바텀 네비게이션 뷰에 있는 아이콘들을 클릭했을 때 메뉴의 아이디 정보가 넘어감
                R.id.notice_board->{
                    with(supportFragmentManager.beginTransaction()){
                        val boardFragment = BoardFragment()
                        replace(binding.mainContainerFl.id, boardFragment)
                        commit()
                    }
                    binding.mainBottomNavigationBn.menu.getItem(0).isChecked = true
                    return@setOnItemSelectedListener true
                }
                R.id.map->{
                    with(supportFragmentManager.beginTransaction()){
                        val homeFragment = HomeFragment()
                        replace(binding.mainContainerFl.id , homeFragment)
                        commit()
                    }
                    binding.mainBottomNavigationBn.menu.getItem(1).isChecked = true
                    return@setOnItemSelectedListener true

                }
            }
            return@setOnItemSelectedListener false
        }

    }

    fun initNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(binding.mainContainerFl.id , HomeFragment())
            .commitAllowingStateLoss()
    }

}