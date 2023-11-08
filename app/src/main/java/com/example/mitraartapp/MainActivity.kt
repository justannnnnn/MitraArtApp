package com.example.mitraartapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mitraartapp.databinding.ActivityMainBinding
import me.relex.circleindicator.CircleIndicator2
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class MainActivity : AppCompatActivity() {
    /*private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LotAdapter // Объект Adapter*/
    private val lotService: LotService // Объект LotService
        get() = (applicationContext as App).lotService
    lateinit var lots: List<Lot>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        /*// RecyclerView(promolots) // LayoutManager
        adapter = LotAdapter() // Создание объекта
        adapter.data = lotService.getLots()// Заполнение данными
        // Назначение LayoutManager для RecyclerView
        binding.promolotsRecyclerView.adapter = adapter
        binding.promolotsRecyclerView.layoutManager = LinearLayoutManager(this)*/

        val rvLots = findViewById<View>(R.id.promolots_RecyclerView) as RecyclerView
        lots = lotService.getLots()
        val adapter = LotAdapter(lots)
        rvLots.adapter = adapter
        rvLots.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

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
    }}