package martinez.andres.modulo6practica2.ui.viewmodel

import android.util.Log
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

class PokemonDetailViewModel: ViewModel() {
    private val repository = PokemonRepository.getInstance()

    private val _pokemon = MutableLiveData<Pokemon>()
    val pokemon: LiveData<Pokemon> = _pokemon

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun onCreate(id: Int?) {
        _isLoading.postValue(true)
        if (id != null) {
            viewModelScope.launch {
                val call = repository.getPokemon(id)
                call.enqueue(object: Callback<Pokemon> {
                    override fun onResponse(p0: Call<Pokemon>, r: Response<Pokemon>) {
                        if (r.body() == null) {
                            _error.postValue("Error connecting with API.")
                        } else {
                            _isLoading.postValue(false)
                            r.body().let {
                                Log.d("Pokemon", it.toString())
                                _pokemon.postValue(it)
                            }
                        }
                    }

                    override fun onFailure(p0: Call<Pokemon>, t: Throwable) {
                        _error.postValue(t.message)
                    }

                })
            }

        }
    }

}