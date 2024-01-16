package com.example.mitraartapp


import java.time.LocalDate

class ECPService {
    private var ecps = mutableListOf<ECP>()

        init {
            ecps.add(ECP("Юридическое лицо", "ООО \"Сын собаки\"", LocalDate.of(2023, 10, 1),
                LocalDate.of(2024, 10, 1), "Ген. директор Иванов И. И."))
        }

    fun getECPs(): MutableList<ECP> = ecps
}