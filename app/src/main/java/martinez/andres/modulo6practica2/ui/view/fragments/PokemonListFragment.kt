package martinez.andres.modulo6practica2.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import martinez.andres.modulo6practica2.R
import martinez.andres.modulo6practica2.data.model.Pokemon
import martinez.andres.modulo6practica2.databinding.FragmentPokemonListBinding
import martinez.andres.modulo6practica2.ui.view.adapters.PokemonAdapter
import martinez.andres.modulo6practica2.ui.viewmodel.PokemonListViewModel

class PokemonListFragment : Fragment() {

    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!

    private val pokemonListViewModel: PokemonListViewModel by viewModels()

    private var pokemonList = emptyList<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonListViewModel.onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recycler view init
        adapter = PokemonAdapter(pokemonList) {pokemon -> onItemClicked(pokemon) }
        binding.rvPokemon.apply {
            adapter = this@PokemonListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        pokemonListViewModel.apply {
            pokemonList.observe(viewLifecycleOwner) {
                this@PokemonListFragment.pokemonList = it.toList()
                adapter.updateList(this@PokemonListFragment.pokemonList)
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.loading.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
            }

            error.observe(viewLifecycleOwner) {message ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ErrorFragment.newInstance(message))
                    .commit()
            }
        }
    }

    private fun onItemClicked(pokemon: Pokemon) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PokemonDetailFragment.newInstance(pokemon.id))
            .addToBackStack("DETAIL")
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}