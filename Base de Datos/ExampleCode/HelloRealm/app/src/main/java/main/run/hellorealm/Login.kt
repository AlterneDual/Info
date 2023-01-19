package main.run.hellorealm

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import main.run.hellorealm.databinding.ActivityMainBinding
import java.util.prefs.AbstractPreferences

class Login : AppCompatActivity() {
    // Variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    // Preferencias
    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var PWD_KEY = "pwd"

    var email = ""
    var pwd = ""


    @SuppressLint("CommitPrefEdits")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Realm.init(this)

        val realmName = "ProyectoPrueba"
        val config =
            RealmConfiguration.Builder().name(realmName).deleteRealmIfMigrationNeeded()
                .schemaVersion(0).allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)

        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(EMAIL_KEY, "").toString()
        pwd = sharedPreferences.getString(PWD_KEY, "").toString()

        binding.idBtnLogin.setOnClickListener {
//            Toast.makeText(this, "Pulsado boton de Loggin", Toast.LENGTH_LONG).show()
            if (binding.idEdtEmail.text.isEmpty() && binding.idEdtPassword.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Introduzca usuario y contraseña para loggearse",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(EMAIL_KEY, binding.idEdtEmail.text.toString())
                editor.putString(PWD_KEY, binding.idEdtPassword.text.toString())
                editor.apply()

                val i = Intent(this, Logged::class.java)
                startActivity(i)
                finish()
            }
        }


    }

    override fun onStart() {
        super.onStart()
        if (!email.equals("") && !pwd.equals("")) {
            val i = Intent(this, Logged::class.java)
            startActivity(i)
            finish()
        }
    }
}