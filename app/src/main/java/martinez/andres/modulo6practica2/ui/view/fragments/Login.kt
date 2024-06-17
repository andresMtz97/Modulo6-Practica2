package martinez.andres.modulo6practica2.ui.view.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import martinez.andres.modulo6practica2.R
import martinez.andres.modulo6practica2.databinding.FragmentLoginBinding

class Login : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            goToPokemonList()
        }

        initListeners()
    }

    private fun goToPokemonList() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PokemonListFragment())
            .commit()
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            if (!validateFields()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //autenticación de usuario
            authenticateUser(email, password)
        }

        binding.btnRegistrarse.setOnClickListener {
            if(!validateFields()) return@setOnClickListener

            binding.progressBar.visibility = View.VISIBLE

            //Registramos al usuario en firebase
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {authResult ->

                if(authResult.isSuccessful){
                    Toast.makeText(
                        this@Login.requireContext(),
                        "Usuario creado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    goToPokemonList()
                }else{
                    binding.progressBar.visibility = View.GONE
                    handleErrors(authResult)
                }

            }

        }

        binding.tvRestablecerPassword.setOnClickListener {
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            AlertDialog.Builder(it.context)
                .setTitle("Restablecer contraseña")
                .setMessage("Ingrese su correo para recibir el enlace para restablecer")
                .setView(resetMail)
                .setPositiveButton("Enviar") { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                this@Login.requireContext(),
                                "El enlace para restablecer la contraseña ha sido enviado a su correo",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                this@Login.requireContext(),
                                "El enlace no se ha podido enviar: ${it.message}",
                                Toast.LENGTH_SHORT
                            )
                                .show() //it tiene la excepción
                        }
                    } else {
                        Toast.makeText(
                            this@Login.requireContext(),
                            "Favor de ingresar la dirección de correo",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }.setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }
    }

    private fun validateFields(): Boolean {
        email = binding.tietEmail.text.toString().trim()
        password = binding.tietContrasenia.text.toString().trim()

        if (email.isEmpty()) {
            binding.tietEmail.error = "El email es obligatorio"
            binding.tietEmail.requestFocus()
            return false
        }

        if (password.isEmpty() || password.length < 6) {
            binding.tietContrasenia.error = "La contraseña es obligatoria"
            binding.tietContrasenia.requestFocus()
            return false
        }

        return true
    }

    private fun handleErrors(task: Task<AuthResult>) {
        var errorCode = ""

        try {
            errorCode = (task.exception as FirebaseAuthException).errorCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        when (errorCode) {
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(
                    requireContext(),
                    "Error: El correo electrónico no tiene un formato correcto",
                    Toast.LENGTH_SHORT
                ).show()
                binding.tietEmail.error =
                    "Error: El correo electrónico no tiene un formato correcto"
                binding.tietEmail.requestFocus()
            }

            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(requireContext(), "Error: La contraseña no es válida", Toast.LENGTH_SHORT).show()
                binding.tietContrasenia.error = "La contraseña no es válida"
                binding.tietContrasenia.requestFocus()
                binding.tietContrasenia.setText("")

            }

            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(
                    requireContext(),
                    "Error: Una cuenta ya existe con el mismo correo, pero con diferentes datos de ingreso",
                    Toast.LENGTH_SHORT
                ).show()
            }

            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(
                    requireContext(),
                    "Error: el correo electrónico ya está en uso con otra cuenta.",
                    Toast.LENGTH_LONG
                ).show()
                binding.tietEmail.error =
                    ("Error: el correo electrónico ya está en uso con otra cuenta.")
                binding.tietEmail.requestFocus()
            }

            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(
                    requireContext(),
                    "Error: La sesión ha expirado. Favor de ingresar nuevamente.",
                    Toast.LENGTH_LONG
                ).show()
            }

            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(
                    requireContext(),
                    "Error: No existe el usuario con la información proporcionada.",
                    Toast.LENGTH_LONG
                ).show()
            }

            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(requireContext(), "La contraseña porporcionada es inválida", Toast.LENGTH_LONG)
                    .show()
                binding.tietContrasenia.error =
                    "La contraseña debe de tener por lo menos 6 caracteres"
                binding.tietContrasenia.requestFocus()
            }

            "NO_NETWORK" -> {
                Toast.makeText(
                    requireContext(),
                    "Red no disponible o se interrumpió la conexión",
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                Toast.makeText(
                    requireContext(),
                    "Error. No se pudo autenticar exitosamente.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun authenticateUser(usr: String, psw: String) {
        firebaseAuth.signInWithEmailAndPassword(usr, psw)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    Toast.makeText(this@Login.requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    goToPokemonList()
                } else {
                    binding.progressBar.visibility = View.GONE
                    handleErrors(authResult)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}