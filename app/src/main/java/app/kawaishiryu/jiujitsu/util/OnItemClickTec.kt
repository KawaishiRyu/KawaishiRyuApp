package app.kawaishiryu.jiujitsu.util

import app.kawaishiryu.jiujitsu.data.model.movimientos.MoviemientosModel

interface OnItemClickTec {

    fun deleteTec(tec: MoviemientosModel)

    fun editTec(tec: MoviemientosModel)

}