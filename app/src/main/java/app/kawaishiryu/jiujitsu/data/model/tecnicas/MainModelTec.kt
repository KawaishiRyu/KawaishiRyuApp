package app.kawaishiryu.jiujitsu.data.model.tecnicas

data class MainModelTec(
    val title: String,
    val subItemModel: List<SubItemModelTec>?= null
)