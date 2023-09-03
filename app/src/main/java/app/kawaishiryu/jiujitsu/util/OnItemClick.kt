package app.kawaishiryu.jiujitsu.util

import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel


interface OnItemClick {

    fun setOnItemClickListener(dojo: DojosModel)
    fun onDeleteClick(dojosModel: DojosModel)
    fun onEditClick(dojosModel: DojosModel)

}