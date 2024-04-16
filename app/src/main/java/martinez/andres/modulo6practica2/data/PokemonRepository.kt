package martinez.andres.modulo6practica2.data

import martinez.andres.modulo6practica2.data.model.Pokemon
import martinez.andres.modulo6practica2.data.remote.PokemonApi
import martinez.andres.modulo6practica2.data.remote.RetrofitHelper
import retrofit2.Call

class PokemonRepository {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val pokemonApi: PokemonApi = retrofit.create(PokemonApi::class.java)

    fun getAllPokemon(): Call<Array<Pokemon>> = pokemonApi.getAllPokemon()

    companion object {
        @Volatile
        private var instance: PokemonRepository? = null
        fun getInstance(): PokemonRepository = instance ?: synchronized(this) {
            instance ?: PokemonRepository().also { instance = it }
        }
    }
}