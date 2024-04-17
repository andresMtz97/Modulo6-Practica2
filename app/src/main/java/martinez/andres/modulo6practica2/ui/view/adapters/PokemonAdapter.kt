package martinez.andres.modulo6practica2.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import martinez.andres.modulo6practica2.R
import martinez.andres.modulo6practica2.data.model.Pokemon

class PokemonAdapter(
    private var list: List<Pokemon> = emptyList(),
    private val onItemClicked: (Pokemon) -> Unit
) : Adapter<PokemonViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PokemonViewHolder(layoutInflater.inflate(R.layout.pokemon_element, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.render(list[position], onItemClicked)
    }

    fun updateList(newList: List<Pokemon>) {
        list = newList
        notifyDataSetChanged()
    }
}