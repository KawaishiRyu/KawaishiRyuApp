package app.kawaishiryu.jiujitsu.domain

import app.kawaishiryu.jiujitsu.core.Resource
import app.kawaishiryu.jiujitsu.data.model.Dojos
import app.kawaishiryu.jiujitsu.data.remote.LocationDojoDataSource

class LocationDojoImpl(private val dataSource: LocationDojoDataSource) : LocationDojoRepo{

    override suspend fun getLocationDojo(): Resource<List<Dojos>> {
        TODO("Not yet implemented")
    }
}