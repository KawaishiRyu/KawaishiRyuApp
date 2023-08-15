package app.kawaishiryu.jiujitsu.util

import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.data.model.tecnicas.SubItemModelTec

object ListTec {

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
        SubItemModelTec("Hiza Waza","Rodilla"),
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
    private val nageWazaList = mutableListOf(
        SubItemModelTec("Ashi Waza", "Tecnica de pierna"),
        SubItemModelTec("Koshi Waza", "Tecnica de cadera"),
        SubItemModelTec("Kata Waza", "Tecnica de hombro"),
        SubItemModelTec("Te Waza", "Tecnica de brazo"),
        SubItemModelTec("Sutemi Waza", "Tecnica de sacrificio"),
    )
    private val shimeWazaList = mutableListOf(
        SubItemModelTec("1° Serie", "1° Serie"),
        SubItemModelTec("2° Serie", "2° Serie"),
    )
    private val kansetsuWazaList = mutableListOf(
        SubItemModelTec("1° Posicion", "1° Posicion"),
        SubItemModelTec("2° Posicion","2° Posicion"),
        SubItemModelTec("3° Posicion","3° Posicion"),
        SubItemModelTec("4° Posicion","4° Posicion"),
        SubItemModelTec("5° Posicion","5° Posicion"),
        SubItemModelTec("6° Posicion","6° Posicion"),
    )

    val collectionTec = listOf(
        MainModelTec("Shisei", "Posturas"),
        MainModelTec("Happo no Sabaki", "Ocho desplazamientos"),
        MainModelTec("Happo no Kusushi", "Ocho desequilibrios"),
        MainModelTec("Kata", "Forma prediseñada", kataList),
        MainModelTec("Atemi Waza", "Tecnica de golpeo", atemiWazaList),
        MainModelTec("Atsu Komi Waza", "Tecnica de presion", atzuKomiWazaList),
        MainModelTec("Yubi Basami Waza", "Tecnica de pinzamiento"),
        MainModelTec("Fumi Waza", "Tecnica de pisoton"),
        MainModelTec("Ukemi Waza", "Tecnica de caida"),
        MainModelTec("Kumi Kata", "Forma de agarre"),
        MainModelTec("Nage Waza", "Tecnica de proyeccion", nageWazaList),
        MainModelTec("Osea Komi Waza", "Tecnica de retencion"),
        MainModelTec("Shime Waza", "Tecnica de estrangulacion", shimeWazaList),
        MainModelTec("Kansetsu Waza", "Tecnica de palanca", kansetsuWazaList),
    )


}