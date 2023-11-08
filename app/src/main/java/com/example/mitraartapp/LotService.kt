package com.example.mitraartapp

class LotService {
    private var lots = mutableListOf<Lot>() // Все пользователи

        init {

            lots = (0..6).map {
                Lot(
                    id = it.toLong(),
                    name = "Name" + it.toString(),
                    author  = "Author" + (it % 5).toString(),
                    price = it*10000.0,
                    imageRes = IMAGES[it]
                )
            }.toMutableList()
        }

    fun getLots(): List<Lot> = lots

        companion object {
            private val IMAGES = mutableListOf(R.drawable.lot_1, R.drawable.lot_2, R.drawable.lot_3, R.drawable.lot_4, R.drawable.lot_5, R.drawable.lot_6, R.drawable.lot_7)
        }
}