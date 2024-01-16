package com.example.mitraartapp

import java.time.LocalDate
import java.util.Date

data class ECP(
    val type: String, // тип(физ. или юр. лицо)
    val name: String, // ФИО или наименование организации
    val activationStart: LocalDate, // дата начала действительности ЭЦП
    val activationEnd: LocalDate, // дата конца действительности ЭЦП
    val director: String? = null // директор(для юр. лица)
)
