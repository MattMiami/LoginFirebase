package com.example.firebase_antonio

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)

        //Para google analytics cada vez que se ejecute la app

        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("Hi!", "You are in!")
        //analytics.logEvent("InitScreen", bundle)

        session()
        setup()
        acceder()
        salir()


    }

    //Crea un usuario que se añade a la base de datos de FireBase
    private fun setup(){
        btRegistrar.setOnClickListener{
            if(etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.text.toString(), etPass.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        //Registro ok, entramos a la app
                        showOk()
                        persistencia()
                        session()
                    }else{
                        showError()
                    }
                }
            }else{
                Toast.makeText(this, "No dejes campos vacíos", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun showOk(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Correcto!")
        builder.setMessage("Ok!")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog = builder.create()
        dialog.show()
    }

    private fun showError(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Incorrecto!")
        builder.setMessage("Error!")
        builder.setPositiveButton("Aceptar", null)
        val dialog:AlertDialog = builder.create()
        dialog.show()

    }

    //Verifica si el usuario ya existe en Firebase
    private fun acceder(){
        btAcceder.setOnClickListener{
            if(etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(etEmail.text.toString(), etPass.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        //Aqui haríamos que entrase a la aplicacion
                        showOk()
                        persistencia()
                        session()
                    }else{
                        showError()
                    }
                }
            }else{
                Toast.makeText(this, "No dejes campos vacíos", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Para hacer un log-out de la app, si estamos dentro
    private fun salir(){
        btSalir.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val prefs:SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
            session()
        }
    }

    //Para guardar en el fichero preferencias el email y clave
    private fun persistencia(){
        val prefs:SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", etEmail.text.toString())
        prefs.putString("password", etPass.text.toString())
        prefs.apply()
    }

    private fun session(){
        val prefs:SharedPreferences = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        //val password = prefs.getString("password", null)
        if(email == null){
            btSalir.isEnabled=false
            btAcceder.isEnabled=true
            btRegistrar.isEnabled=true
        }
        else{
            btSalir.isEnabled=true
            btAcceder.isEnabled=false
            btRegistrar.isEnabled=false
        }

    }
}