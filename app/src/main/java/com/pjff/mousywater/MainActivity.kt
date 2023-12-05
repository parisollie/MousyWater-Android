package com.pjff.mousywater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pjff.mousywater.databinding.ActivityMainBinding
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var user: FirebaseUser? = null
    private var userId: String? = null

    //***********************************************************************************
    //Para el lector Biometrico
    private var banderaEmailVerificado = true
    //private var banderaHuellaActiva = false
    private var psw = ""
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //Recupero el usuario que ya se autentico
        user = firebaseAuth?.currentUser
        userId = user?.uid

        //Me pone en la caja de texto el email y el nombre del usuario
        binding.tvUsuario.text = user?.email

        //Obteniendo el password desde el activity Login
        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            psw = bundle.getString("psw", "")
        }

        //revisamos si el email no está verificado

        if(user?.isEmailVerified != true){
            banderaEmailVerificado = false
            binding.tvCorreoNoVerificado.visibility = View.VISIBLE
            binding.btnReenviarVerificacion.visibility = View.VISIBLE

            binding.btnReenviarVerificacion.setOnClickListener {
                user?.sendEmailVerification()?.addOnSuccessListener {
                    Toast.makeText(this, "El correo de verificación ha sido enviado", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Toast.makeText(this, "Error: El correo de verificación no se ha podido enviar", Toast.LENGTH_SHORT).show()
                    Log.d("LOGS", "onFailure: ${it.message}")
                }
            }
        }

        //Para cerrar sesion
        binding.btnCerrarSesion.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}