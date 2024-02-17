package com.example.tourism_app.data

import android.os.Parcel
import android.os.Parcelable

// class representing the "Lieu" object in our Firebase database
data class Activity(
    val name : String ?= null,
    var address: String? = null,
    var description: String? = null,
    var transport: List<List<String>> ?= null,
    var hours: String? = null,
    var category: String ?= null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList()?.map { it.split(",") },
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(description)
        parcel.writeStringList(transport?.flatten())
        parcel.writeString(hours)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Activity> {
        override fun createFromParcel(parcel: Parcel): Activity {
            return Activity(parcel)
        }

        override fun newArray(size: Int): Array<Activity?> {
            return arrayOfNulls(size)
        }
    }

    fun getArrondissement(): String? {
        val pattern = Regex("\\b750(\\d{2})\\b")
        val matchResult = pattern.find(address.toString())

        val arrondissementNumber = matchResult?.groupValues?.get(1)?.toIntOrNull()

        return when {
            arrondissementNumber != null && arrondissementNumber in 1..20 -> {
                "$arrondissementNumber${getOrdinalSuffix(arrondissementNumber)}"
            }
            else -> null
        }
    }

    private fun getOrdinalSuffix(number: Int): String {
        return when {
            number in 11..13 -> "th"
            number % 10 == 1 -> "st"
            number % 10 == 2 -> "nd"
            number % 10 == 3 -> "rd"
            else -> "th"
        }
    }
}