package com.example.lightcalculator.data

import android.util.Log
import com.example.lightcalculator.data.sampleLights
import com.google.firebase.firestore.FirebaseFirestore

val sampleLights = listOf(
    /* Robe - Pointe */
    Light(brand = "Robe", model = "Pointe", power = 470, DMXmode = 16, dmxmodeDes = "16"),
    Light(brand = "Robe", model = "Pointe", power = 470, DMXmode = 24, dmxmodeDes = "24"),
    Light(brand = "Robe", model = "Pointe", power = 470, DMXmode = 30, dmxmodeDes = "30"),

    /* Robe - MegaPointe */
    Light(brand = "Robe", model = "MegaPointe", power = 670, DMXmode = 34, dmxmodeDes = "34"),
    Light(brand = "Robe", model = "MegaPointe", power = 470, DMXmode = 39, dmxmodeDes = "39"),

    /* Robe - Spiider */
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 27, dmxmodeDes = "27"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 33, dmxmodeDes = "33"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 47, dmxmodeDes = "47"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 49, dmxmodeDes = "49"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 90, dmxmodeDes = "90"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 91, dmxmodeDes = "91"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 104, dmxmodeDes = "104"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 110, dmxmodeDes = "110"),
    Light(brand = "Robe", model = "Spiider", power = 660, DMXmode = 123, dmxmodeDes = "123"),

    /*Clay Paky - Mythos2 */
    Light(brand = "Clay Paky", model = "Mythos2", power = 700, DMXmode = 30, dmxmodeDes = "30"),
    Light(brand = "Clay Paky", model = "Mythos2", power = 700, DMXmode = 34, dmxmodeDes = "34"),

    /* GLP - Impression x4s */
    Light(brand = "GLP", model = "Impression x4s", power = 150, DMXmode = 14, dmxmodeDes = "Compressed"),
    Light(brand = "GLP", model = "Impression x4s", power = 150, DMXmode = 18, dmxmodeDes = "Normal"),
    Light(brand = "GLP", model = "Impression x4s", power = 150, DMXmode = 19, dmxmodeDes = "Highres"),

    /* GLP - Impression x4 */
    Light(brand = "GLP", model = "Impression x4", power = 350, DMXmode = 14, dmxmodeDes = "Compressed"),
    Light(brand = "GLP", model = "Impression x4", power = 350, DMXmode = 20, dmxmodeDes = "Normal"),
    Light(brand = "GLP", model = "Impression x4", power = 350, DMXmode = 21, dmxmodeDes = "Highres"),

    /* GLP - Impression x4L */
    Light(brand = "GLP", model = "Impression x4L", power = 900, DMXmode = 21, dmxmodeDes = "Compressed"),
    Light(brand = "GLP", model = "Impression x4L", power = 900, DMXmode = 29, dmxmodeDes = "Normal"),
    Light(brand = "GLP", model = "Impression x4L", power = 900, DMXmode = 31, dmxmodeDes = "Highres"),
    Light(brand = "GLP", model = "Impression x4L", power = 900, DMXmode = 169, dmxmodeDes = "Spix"),

    /* GLP - Impression x4 bar 20 */
    Light(brand = "GLP", model = "Impression x4 bar", power = 450, DMXmode = 20, dmxmodeDes = "Compressed"),
    Light(brand = "GLP", model = "Impression x4 bar", power = 450, DMXmode = 34, dmxmodeDes = "Normal"),
    Light(brand = "GLP", model = "Impression x4 bar", power = 450, DMXmode = 35, dmxmodeDes = "Highres"),
    Light(brand = "GLP", model = "Impression x4 bar", power = 450, DMXmode = 48, dmxmodeDes = "DPix"),
    Light(brand = "GLP", model = "Impression x4 bar", power = 450, DMXmode = 49, dmxmodeDes = "DPixH"),
    Light(brand = "GLP", model = "Impression x4 bar", power = 450, DMXmode = 88, dmxmodeDes = "Spix"),
    Light(brand = "GLP", model = "Impression x4 bar", power = 450, DMXmode = 89, dmxmodeDes = "SpixH"),

    /* SMG - Q-7 */
    Light(brand = "SMG", model = "Q-7", power = 465, DMXmode = 3, dmxmodeDes = "3"),
    Light(brand = "SMG", model = "Q-7", power = 465, DMXmode = 4, dmxmodeDes = "Raw"),
    Light(brand = "SMG", model = "Q-7", power = 465, DMXmode = 6, dmxmodeDes = "Raw"),
    Light(brand = "SMG", model = "Q-7", power = 465, DMXmode = 6, dmxmodeDes = "CTC"),
    Light(brand = "SMG", model = "Q-7", power = 465, DMXmode = 8, dmxmodeDes = "Raw 16 bit"),
    Light(brand = "SMG", model = "Q-7", power = 465, DMXmode = 8, dmxmodeDes = "Emulation"),
    Light(brand = "SMG", model = "Q-7", power = 465, DMXmode = 10, dmxmodeDes = "CTC")
)

fun populateSampleLightsIfEmpty() {
    val db = FirebaseFirestore.getInstance()

    db.collection("lights").get()
        .addOnSuccessListener { result ->
            if (result.isEmpty) {
                sampleLights.forEach { light ->
                    db.collection("lights").add(light)
                }
                Log.d("Firestore", "Sample svetlá boli pridané do databázy.")
            } else {
                Log.d("Firestore", "Databáza už obsahuje dáta, nepridávam nič.")
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Chyba pri čítaní databázy", e)
        }
}