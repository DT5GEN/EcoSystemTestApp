package com.dt5gen.ecosystem.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class BinInfoResponse(
    val number: CardNumber?,
    val scheme: String?, // Тип карты (visa, mastercard и т.д.)
    val type: String?, // Тип карты (debit, credit)
    val brand: String?, // Бренд карты (например, Visa/Dankort)
    val prepaid: Boolean?, // Признак предоплаченной карты
    val country: Country?,
    val bank: Bank?
)

data class CardNumber(
    val length: Int?, // Длина номера карты
    val luhn: Boolean? // Проверка по алгоритму Луна
)

data class Country(
    val numeric: String?, // Числовой код страны
    val alpha2: String?, // Двухсимвольный код страны
    val name: String?, // Название страны
    val emoji: String?, // Эмодзи страны
    val currency: String?, // Валюта страны
    val latitude: Double?, // Широта
    val longitude: Double? // Долгота
)

data class Bank(
    val name: String?, // Название банка
    val url: String?, // Сайт банка
    val phone: String?, // Телефон банка
    val city: String? // Город банка
)


@Entity(tableName = "bin_history")
data class BinHistoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val bin: String,
    val numberLength: Int?,
    val luhn: Boolean?,
    val scheme: String?,
    val type: String?,
    val brand: String?,
    val prepaid: Boolean?,
    val countryName: String?,
    val countryAlpha2: String?,
    val countryEmoji: String?,
    val currency: String?,
    val latitude: Double?,
    val longitude: Double?,
    val bankName: String?,
    val bankUrl: String?,
    val bankPhone: String?,
    val bankCity: String?
)
