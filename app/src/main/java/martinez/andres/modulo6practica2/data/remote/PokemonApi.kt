package martinez.andres.modulo6practica2.data.remote

import martinez.andres.modulo6practica2.data.model.Pokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApi {
    @GET("pokemon")
    fun getAllPokemon(): Call<Array<Pokemon>>

    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<Pokemon>
}