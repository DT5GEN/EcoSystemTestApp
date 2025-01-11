package com.dt5gen.ecosystem.domain.models

fun BinInfoResponse.toHistoryItem(bin: String): BinHistoryItem {
    return BinHistoryItem(
        bin = bin,
        numberLength = this.number?.length,
        luhn = this.number?.luhn,
        scheme = this.scheme,
        type = this.type,
        brand = this.brand,
        prepaid = this.prepaid,
        countryName = this.country?.name,
        countryAlpha2 = this.country?.alpha2,
        countryEmoji = this.country?.emoji,
        currency = this.country?.currency,
        latitude = this.country?.latitude,
        longitude = this.country?.longitude,
        bankName = this.bank?.name,
        bankUrl = this.bank?.url,
        bankPhone = this.bank?.phone,
        bankCity = this.bank?.city
    )
}
