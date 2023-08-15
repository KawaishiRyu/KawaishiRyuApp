package app.kawaishiryu.jiujitsu.data.model.tecnicas

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubItemModelTec(
    val subItemTitle: String,
    val translateTitle: String?
) : Parcelable