package app.kawaishiryu.jiujitsu.domain

import app.kawaishiryu.jiujitsu.core.Resource
import app.kawaishiryu.jiujitsu.data.model.Dojos

interface LocationDojoRepo {

    suspend fun getLocationDojo(): Resource<List<Dojos>>

}