package martinez.andres.modulo6practica2.data.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("urlImage") val urlImage: String,
    @SerializedName("urlVideo") val urlVideo: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("type") val type: Array<String>?,
    @SerializedName("weak_to") val weakTo: Array<String>?,
    @SerializedName("skills") val skills: Array<String>?,
    @SerializedName("base_stats") val baseStats: BaseStats?,
    @SerializedName("location") val location: Array<Double>?
)
