package martinez.andres.modulo6practica2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import martinez.andres.modulo6practica2.data.PokemonRepository
import martinez.andres.modulo6practica2.data.model.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonListViewModel : ViewModel() {
    private val repository = PokemonRepository.getInstance()

    private val _pokemonList = MutableLiveData<Array<Pokemon>>()
    val pokemonList: LiveData<Array<Pokemon>> = _pokemonList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun onCreate() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val call: Call<Array<Pokemon>> = repository.getAllPokemon()
            call.enqueue(object : Callback<Array<Pokemon>> {
                override fun onResponse(p0: Call<Array<Pokemon>>, r: Response<Array<Pokemon>>) {
                    if (r.body() == null) {
                        _error.postValue("Error connecting with API.")
                    } else {
                        _isLoading.postValue(false)
                        r.body().let {
                            _pokemonList.postValue(it ?: emptyArray())
                        }
                    }
                }

                override fun onFailure(p0: Call<Array<Pokemon>>, t: Throwable) {
                    _error.postValue(t.message)
                }
            })
        }
    }
}