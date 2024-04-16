package martinez.andres.modulo6practica2.data.model

import com.google.gson.annotations.SerializedName

data class BaseStats(
    @SerializedName("hp") val hp: Int,
    @SerializedName("attack") val attack: Int,
    @SerializedName("defense") val defense: Int,
    @SerializedName("sp_atk") val spAtk: Int,
    @SerializedName("sp_def") val spDef: Int,
    @SerializedName("speed") val speed: Int,
    @SerializedName("total") val total: Int,
)
