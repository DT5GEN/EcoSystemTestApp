package com.dt5gen.ecosystem.domain.models

data class BinInfoResponse(
    val country: Country?,
    val bank: Bank?,
    val scheme: String?,
    val type: String?,
    val brand: String?
)

data class Country(
    val name: String?,
    val latitude: Double?,
    val longitude: Double?
)

data class Bank(
    val name: String?,
    val url: String?,
    val phone: String?,
    val city: String?
)
