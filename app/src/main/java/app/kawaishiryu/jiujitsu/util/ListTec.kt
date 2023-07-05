package app.kawaishiryu.jiujitsu.util

import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec
import app.kawaishiryu.jiujitsu.data.model.tecnicas.SubItemModelTec

object ListTec {
    private val flowerList = mutableListOf(
        SubItemModelTec("Rose"),
        SubItemModelTec("Daisy"),
        SubItemModelTec("Lily"),
    )

    private val pitoList = mutableListOf(
        SubItemModelTec("pito1"),
        SubItemModelTec("pito2"),
        SubItemModelTec("pito3"),
        )

    val collectios = listOf(
        MainModelTec("All Flowers", flowerList),
        MainModelTec("Want to buy", flowerList.reversed()),
        MainModelTec("Popular Flowers", flowerList.shuffled()),
        MainModelTec("Pito list", )
    )

}