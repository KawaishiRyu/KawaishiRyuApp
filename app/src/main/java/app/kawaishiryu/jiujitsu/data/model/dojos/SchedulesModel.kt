package app.kawaishiryu.jiujitsu.data.model.dojos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SchedulesModel(
    var day: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var activity: String = ""
) : Parcelable {

    companion object {
        const val CLOUD_FIRE_STORE_PATH = "SCHEDULES"
        const val DAY_KEY = "DAY"
        const val START_TIME_KEY = "START_TIME"
        const val END_TIME_KEY = "END_TIME"
        const val ACTIVITY_KEY = "ACTIVITY"
    }

    fun toDictionary(): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()
        map[DAY_KEY] = day
        map[START_TIME_KEY] = startTime
        map[END_TIME_KEY] = endTime
        map[ACTIVITY_KEY] = activity
        return map
    }

    fun parse(map: MutableMap<String, Any>) {
        day = map[DAY_KEY] as String
        startTime = map[START_TIME_KEY] as String
        endTime = map[END_TIME_KEY] as String
        activity = map[ACTIVITY_KEY] as String
    }
}