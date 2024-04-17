package martinez.andres.modulo6practica2.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import martinez.andres.modulo6practica2.data.PokemonRepository
import martinez.andres.modulo6practica2.data.model.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonDetailViewModel: ViewModel() {
    private val repository = PokemonRepository.getInstance()

    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> = _pokemon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun onCreate(id: Int?) {
        _isLoading.postValue(true)
        if (id != null) {
            viewModelScope.launch {
                val call = repository.getPokemon(id)
                call.enqueue(object: Callback<Pokemon> {
                    override fun onResponse(p0: Call<Pokemon>, r: Response<Pokemon>) {
                        _isLoading.postValue(false)
                        r.body()?.let {
                            _pokemon.postValue(it)
                        }
                    }

                    override fun onFailure(p0: Call<Pokemon>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
            }

        }
    }

}