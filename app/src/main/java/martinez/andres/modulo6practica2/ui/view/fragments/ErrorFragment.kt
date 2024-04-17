package martinez.andres.modulo6practica2.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import martinez.andres.modulo6practica2.R
import martinez.andres.modulo6practica2.databinding.FragmentErrorBinding

private const val ERROR = "error"

class ErrorFragment : Fragment() {
    private var error: String? = null

    private var _binding: FragmentErrorBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            error = it.getString(ERROR)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvError.text = error

        binding.tryAgain.setOnClickListener {
            parentFragmentManager.popBackStackImmediate("DETAIL", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, PokemonListFragment())
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(error: String) =
            ErrorFragment().apply {
                arguments = Bundle().apply {
                    putString(ERROR, error)
                }
            }
    }
}