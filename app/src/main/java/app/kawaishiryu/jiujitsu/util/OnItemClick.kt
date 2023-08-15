package app.kawaishiryu.jiujitsu.util

import app.kawaishiryu.jiujitsu.data.model.dojos.DojosModel
import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel
import app.kawaishiryu.jiujitsu.data.model.tecnicas.MainModelTec

interface OnItemClick {

    fun setOnItemClickListener(dojo: DojosModel) {
    }
    fun onDeleteClick(dojosModel: DojosModel)

}