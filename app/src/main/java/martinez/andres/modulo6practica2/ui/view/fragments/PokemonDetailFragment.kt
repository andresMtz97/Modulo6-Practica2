package martinez.andres.modulo6practica2.ui.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import martinez.andres.modulo6practica2.R
import martinez.andres.modulo6practica2.data.model.BaseStats
import martinez.andres.modulo6practica2.data.model.Pokemon
import martinez.andres.modulo6practica2.databinding.FragmentPokemonDetailBinding
import martinez.andres.modulo6practica2.ui.viewmodel.PokemonDetailViewModel


private const val POKEMON_ID = "pokemonId"

class PokemonDetailFragment : Fragment(), OnMapReadyCallback {
    private var id: Int? = null

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    private val pokemonDetailViewModel: PokemonDetailViewModel by viewModels()

    private lateinit var map: GoogleMap
    private lateinit var location: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getInt(POKEMON_ID)
        }
        //pokemonDetailViewModel.onCreate(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        pokemonDetailViewModel.apply {
            pokemon.observe(viewLifecycleOwner) { pokemon ->
                Picasso.get().load(pokemon.urlImage).into(binding.ivPokemon)
                binding.apply {
                    tvName.text = pokemon.name
                    tvNumber.text = requireContext().getString(R.string.pokedex_number, pokemon.id)
                    tvDescription.text = pokemon.description
                    setBaseStats(pokemon.baseStats)
                    tvType.text = pokemon.type?.joinToString(", ") ?: ""
                    tvWeakTo.text = pokemon.weakTo?.joinToString(", ") ?: ""
                    tvSkills.text = pokemon.skills?.joinToString(", ") ?: ""
                    initVideoView(pokemon)
                    if (pokemon.location != null) {
                        location = LatLng(pokemon.location[0], pokemon.location[1])
                        map.addMarker(
                            MarkerOptions()
                            .position(location)
                            .title(pokemon.name)
                            .snippet(pokemon.id.toString())
                            .icon(bitmapFromVector(requireContext(), R.drawable.ic_pokeball)))
                        map.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(location, 18f),
                            2000,
                            null
                        )
                    }
                }
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.loading.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
                binding.clData.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            }

            error.observe(viewLifecycleOwner) {message ->
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ErrorFragment.newInstance(message))
                    .commit()
            }
        }
    }

    private fun setBaseStats(baseStats: BaseStats?) {
        binding.apply {
            if (baseStats != null) {
                tvHp.text = baseStats.hp.toString()
                tvAttack.text = baseStats.attack.toString()
                tvDefense.text = baseStats.defense.toString()
                tvSpAtk.text = baseStats.spAtk.toString()
                tvSpDef.text = baseStats.spDef.toString()
                tvSpeed.text = baseStats.speed.toString()

                vHp.layoutParams.width = dpToPx(baseStats.hp)
                vHp.setBackgroundColor(getBackgrounColor(baseStats.hp))
                vAttack.layoutParams.width = dpToPx(baseStats.attack)
                vAttack.setBackgroundColor(getBackgrounColor(baseStats.attack))
                vDefense.layoutParams.width = dpToPx(baseStats.defense)
                vDefense.setBackgroundColor(getBackgrounColor(baseStats.defense))
                vSpAtk.layoutParams.width = dpToPx(baseStats.spAtk)
                vSpAtk.setBackgroundColor(getBackgrounColor(baseStats.spAtk))
                vSpDef.layoutParams.width = dpToPx(baseStats.spDef)
                vSpDef.setBackgroundColor(getBackgrounColor(baseStats.spDef))
                vSpeed.layoutParams.width = dpToPx(baseStats.speed)
                vSpeed.setBackgroundColor(getBackgrounColor(baseStats.speed))
            }
        }
    }

    private fun getBackgrounColor(stat: Int): Int {
        return if (stat <= 40)
            resources.getColor(R.color.red_700, null)
        else if (stat in 41..80)
            resources.getColor(R.color.orange_400, null)
        else if (stat in 8..100)
            resources.getColor(R.color.yellow_300, null)
        else if (stat in 101..120)
            resources.getColor(R.color.green_500, null)
        else
            resources.getColor(R.color.teal_400, null)
    }
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return Math.round(dp * density)
    }

    private fun initVideoView(pokemon: Pokemon) {
        binding.vvVideo.setVideoPath(pokemon.urlVideo)
        val mc = MediaController(requireContext())
        mc.setAnchorView(binding.vvVideo)
        binding.vvVideo.setMediaController(mc)
        binding.vvVideo.setOnPreparedListener { it.start() }
        binding.vvVideo.setOnCompletionListener { it.start() }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(id: Int) =
            PokemonDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(POKEMON_ID, id)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        pokemonDetailViewModel.onCreate(id)
    }

    private fun bitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}