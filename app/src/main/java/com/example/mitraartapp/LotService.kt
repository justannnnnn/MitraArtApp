package com.example.mitraartapp

class LotService {
    private var lots = mutableListOf<Lot>() // Все пользователи

        init {

            lots = (1..7).map {
                Lot(
                    id = it.toLong(),
                    name = "Name" + it.toString(),
                    author  = "Author" + (it % 5).toString(),
                    price = it*10000.0,
                    image = IMAGES[it]
                )
            }.toMutableList()
        }

        companion object {
            private val IMAGES = mutableListOf(
                "images/image 15263.png",
                "images/image 15264.png",
                "images/image 15267.png",
                "images/image 15268.png",
                "images/Донбасс-израненная-земля-1080-762x1024 1.png",
                "images/Донбасс-израненная-земля-1080-762x1024 2.png",
                "images/Слой_22_xA0_Изображение.png"
            )
        }
}