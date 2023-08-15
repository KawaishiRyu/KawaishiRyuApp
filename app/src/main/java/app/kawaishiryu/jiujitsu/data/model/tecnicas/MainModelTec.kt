package app.kawaishiryu.jiujitsu.data.model.tecnicas

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainModelTec(
    val title: String,
    val translate: String,
    val subItemModel: List<SubItemModelTec>?= null
) : Parcelable {

}