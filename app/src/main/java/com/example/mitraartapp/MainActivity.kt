package com.example.mitraartapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import java.util.Base64


class MainActivity : AppCompatActivity() {
    /*private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LotAdapter // Объект Adapter*/
    private val lotService: LotService // Объект LotService
        get() = (applicationContext as App).lotService
    lateinit var lots: List<Lot>
    lateinit var bottomNav : BottomNavigationView
    private var dbHandler: DBHandler? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        dbHandler = DBHandler(this@MainActivity);
        //dbHandler!!.deleteTable()
        //dbHandler!!.createTable()
        //val flag = dbHandler!!.tableExists()
        //dbHandler!!.setImage("")

        // Carousel(each 3 sec changes)
        val carousel: ImageCarousel = findViewById(R.id.carousel)
        val list = mutableListOf<CarouselItem>()
        carousel.setClipToOutline(true)

        list.add(
            CarouselItem(
                imageDrawable = R.drawable.c4c4396881322968b97131915f2c7c6d
            )
        )
        list.add(
            CarouselItem(
                imageDrawable = R.drawable.f780d31f62d0ef50f3c3363ca7f07117
            )
        )
        carousel.addData(list)



        /*// LayoutManager
        adapter = LotAdapter() // Создание объекта
        adapter.data = lotService.getLots()// Заполнение данными
        // Назначение LayoutManager для RecyclerView
        binding.promolotsRecyclerView.adapter = adapter
        binding.promolotsRecyclerView.layoutManager = LinearLayoutManager(this)*/

        // RecylerView(promolots)
        val rvLots1 = findViewById<View>(R.id.promolots1_RecyclerView) as RecyclerView
        lots = lotService.getLots()
        val adapter1 = LotAdapter(lots)
        rvLots1.adapter = adapter1
        rvLots1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val rvLots2 = findViewById<View>(R.id.promolots2_RecyclerView) as RecyclerView
        val adapter2 = LotAdapter(lots)
        rvLots2.adapter = adapter2
        rvLots2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



        // Bottom menu
        loadFragment(HomeFragment())
        bottomNav = findViewById(R.id.bottom_menu) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.more -> {
                    loadFragment(MoreFragment())
                    true
                }
                R.id.cart -> {
                    loadFragment(CartFragment())
                    true
                }
                R.id.account -> {
                    loadFragment(AccountFragment())
                    var intent = Intent()
                    // cleaning DB for testing registration screen
                    //dbHandler!!.clearTable()
                    val isActivatedAcc = dbHandler!!.tableExists()
                    if (isActivatedAcc) {
                        intent = Intent(this@MainActivity, RegisteredAccountActivity::class.java)
                    }
                    else{
                        intent = Intent(this@MainActivity, FirstEntryActivity::class.java)
                    }
                    startActivity(intent)
                    true
                }


                else -> {true}
            }
        }
    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        //transaction.replace(R.id.ll2,fragment)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        bottomNav = findViewById(R.id.bottom_menu) as BottomNavigationView
        bottomNav.setSelectedItemId(R.id.home);
    }
}