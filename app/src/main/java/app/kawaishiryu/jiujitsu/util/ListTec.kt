package app.kawaishiryu.jiujitsu.util

import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.data.model.tecnicas.SubItemModelTec

object ListTec {

    private val hapoNoSabaki = mutableListOf(
        SubItemModelTec("Sono ichi", "Primera forma"),
        SubItemModelTec("Sono ni", "Segunda forma"),
        SubItemModelTec("Sono san", "Tercera forma")
    )

    private val kataList = mutableListOf(
        SubItemModelTec("Kata Bukinobu", "Con armas"),
        SubItemModelTec("Kata Toshunobu", "Sin armas")
    )

    private val atemiWazaList = mutableListOf(
        SubItemModelTec("Ken waza", "Puño"),
        SubItemModelTec("Kaisho Waza", "Mano abierta"),
        SubItemModelTec("Yubi Waza", "Dedos"),
        SubItemModelTec("Wanto Waza", "Antebrazo"),
        SubItemModelTec("Hiji Waza", "Codo"),
        SubItemModelTec("Ashi Waza", "Pierna"),
        SubItemModelTec("Hiza Waza", "Rodilla"),
        SubItemModelTec("Kobore Waza", "Tibia"),
        SubItemModelTec("Atama Waza", "Cabeza"),
    )
    private val atzuKomiWazaList = mutableListOf(
        SubItemModelTec("Ude Atsu", "Presion con brazo"),
        SubItemModelTec("Ken Atsu", "Presion con mano"),
        SubItemModelTec("Yubi Atsu", "Presion con dedos"),
        SubItemModelTec("Yubi Kansetsu Atsu", "Presion con falanges"),
        SubItemModelTec("Ten Atsu", "Presion con manos"),
        SubItemModelTec("Ashi Atsu", "Presion con pierna")
    )

    //JUDO
    private val kumiKataList = mutableListOf(
        SubItemModelTec("Hon kumi kata", "Forma de agarre fundamental"),
        SubItemModelTec("Kuzure kumi kata", "Forma de agarre alternativa ")
    )
    private val shimeWazaList = mutableListOf(
        SubItemModelTec("SAISHO NO SHIRĪZU", "Primera serie"),
        SubItemModelTec("NIBANME NO SHIRĪZU", "Segunda serie"),
    )
    private val kansetsuWazaList = mutableListOf(
        SubItemModelTec("1° Posicion", "1° Posicion"),
        SubItemModelTec("2° Posicion", "2° Posicion"),
        SubItemModelTec("3° Posicion", "3° Posicion"),
        SubItemModelTec("4° Posicion", "4° Posicion"),
        SubItemModelTec("5° Posicion", "5° Posicion"),
        SubItemModelTec("6° Posicion", "6° Posicion"),
    )

    val collectionTec = listOf(
        MainModelTec("SHISEI", "Posturas del cuerpo", "姿勢", R.string.Shisei_def),
        MainModelTec("SHINTAI WAZA", "Técnicas de Desplazamiento", "身体技", R.string.SHINTAI_WAZA),
        MainModelTec(
            "HAPPO NO SABAKI",
            "Ocho desplazamientos",
            "八方の捌き",
            R.string.HAPPO_NO_SABAKI,
            hapoNoSabaki
        ),
        MainModelTec("KAMAE", "Guardias", "構え", R.string.KAMAE),
        MainModelTec("KATA", "Formas", "型", R.string.KATA, kataList),
        MainModelTec("UKE WAZA", "Tecnica de bloqueo", "受け技", R.string.UKE_WAZA),
        MainModelTec(
            "ATEMI WAZA",
            "Técnicas de golpeo",
            "当て身技",
            R.string.ATEMI_WAZA,
            atemiWazaList
        ),
        MainModelTec(
            "ATSU KOMI WAZA",
            "Técnicas de presión",
            "圧込技",
            R.string.ATSU_KOMI_WAZA,
            atzuKomiWazaList
        ),
        MainModelTec("FUMI WAZA", "Tecnica de pisoton", "踏み技", R.string.FUMI_WAZA),
        MainModelTec("KANSETSU WAZA", "Tecnica de palanca", "関節技", R.string.KANSETSU_WAZA),
        MainModelTec("UKEMI WAZA", "Tecnica de caida", "受け身技", R.string.UKE_WAZA),
    )

    val collectionTecJudo = listOf(
        MainModelTec("SHISEI", "Posturas del cuerpo", "姿勢", R.string.Shisei_def),
        MainModelTec("SHINTAI WAZA", "Técnicas de Desplazamiento", "身体技", R.string.SHINTAI_WAZA),
        MainModelTec(
            "HAPPO NO KUSUSHI",
            "Ocho desplazamientos",
            "八方の捌き",
            R.string.HAPPO_NO_SABAKI,
            hapoNoSabaki
        ),
        MainModelTec("KATA", "Formas", "型", R.string.KATA, kataList),
        MainModelTec("KUMI KATA", "Forma de agarre", "組み", R.string.KUMI_KATA, kumiKataList),
        MainModelTec("NAGE WAZA", "Tecnicas de proyeccion", "投げ技", R.string.NAGE_WAZA),//Falta
        MainModelTec(
            "OSAE KOMI WAZA",
            "Tecnicas de retencion",
            "押し込み技",
            R.string.OSEA_KOMI_WAZA
        ),//Falta
        MainModelTec(
            "SHIME WAZA",
            "Tecnicas de estrangulacion",
            "絞め技",
            R.string.SHIME_WAZA,
            shimeWazaList
        ),//Falta
        MainModelTec("KANSETSU WAZA", "Tecnicas de palanca","関節技", R.string.KANSETSU_WAZA, kansetsuWazaList)
    )


    val cinturones = arrayOf(
        "Cinturon blanco", "Cinturon amarillo", "Cinturon naranja",
        "Cinturon verde", "Cinturon azul", "Cinturon marron", "Cinturon negro"
    )

}