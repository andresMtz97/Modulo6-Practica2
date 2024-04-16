package martinez.andres.modulo6practica2.ui.view.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.squareup.picasso.Picasso
import martinez.andres.modulo6practica2.R
import martinez.andres.modulo6practica2.data.model.Pokemon
import martinez.andres.modulo6practica2.databinding.PokemonElementBinding

class PokemonViewHolder(view: View) : ViewHolder(view) {

    private val binding = PokemonElementBinding.bind(view)
    fun render(pokemon: Pokemon) {
        binding.tvName.text = pokemon.name
        binding.tvPokedexNumber.text =
            itemView.context.getString(R.string.pokedex_number, pokemon.id)

        Picasso.get().load(pokemon.urlImage).into(binding.ivPokemon)
    }
}