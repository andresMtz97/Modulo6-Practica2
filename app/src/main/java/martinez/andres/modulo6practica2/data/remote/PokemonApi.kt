package martinez.andres.modulo6practica2.data.remote

import martinez.andres.modulo6practica2.data.model.Pokemon
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface PokemonApi {
    @GET("pokemon")
    suspend fun getAllPokemon(): Call<Array<Pokemon>>
}