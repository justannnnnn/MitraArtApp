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
import me.relex.circleindicator.CircleIndicator2
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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