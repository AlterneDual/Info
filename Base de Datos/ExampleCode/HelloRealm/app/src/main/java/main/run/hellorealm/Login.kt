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
import io.realm.RealmList
import main.run.hellorealm.Control.GastoController
import main.run.hellorealm.Control.UsuarioController
import main.run.hellorealm.databinding.ActivityMainBinding
import main.run.hellorealm.model.Gasto

class Login : AppCompatActivity() {
    // Variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lc: LoginController

    private lateinit var gc: GastoController
    private lateinit var uc: UsuarioController

    // Preferencias
    private val PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var PWD_KEY = "pwd"
    var ID_KEY="id"

    var email: String = ""
    var pwd: String = ""


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

        lc = LoginController()

        binding.idBtnLogin.setOnClickListener {

            val mail: String? = binding.idEdtEmail.text.toString()
            val pass: String? = binding.idEdtPassword.text.toString()
            val id: Int? = lc.checkUser(mail,pass)
            if (binding.idEdtEmail.text.isEmpty() && binding.idEdtPassword.text.isEmpty()) {
                Toast.makeText(
                    this,
                    "Introduzca usuario y contraseña para loggearse",
                    Toast.LENGTH_LONG
                ).show()

            } else if (id != null) {
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(EMAIL_KEY, mail)
                editor.putString(PWD_KEY, pass)
                editor.putInt(ID_KEY,id)
                editor.apply()

                val i = Intent(this, Logged::class.java)
                startActivity(i)
                finish()
            } else {

                Toast.makeText(
                    this,
                    "Usuario o Contraseña incorrectos",
                    Toast.LENGTH_LONG
                ).show()

            }
        }

        binding.logDatabase.setOnClickListener {
            cargar()
        }
    }

    override fun onStart() {
        super.onStart()
        if (lc.checkUser(email, pwd)!=null) {
            val i = Intent(this, Logged::class.java)
            startActivity(i)
            finish()
        }
    }

    fun cargar() {

        gc = GastoController()
        uc = UsuarioController()

        // Limpiamos todos los Usuarios
        uc.cleanAllUser()
        gc.cleanAll()

        // Creamos e insertamos un Usuario y un listado (RealmList) de gastos
        var last_updated = uc.addUser("Juan", "1234567890", null)
        println("Añadido usuario con ID: $last_updated")
        println(uc.getUser(last_updated)?.info())
        var user_stored = uc.getAllUser()

        // Creamos y vinculamos los gastos al usuario
        gc.addGasto(100.0, user_stored[last_updated])
        gc.addGasto(300.0, user_stored[last_updated])
        gc.addGasto(50.5, user_stored[last_updated])
        gc.addGasto(30.0, user_stored[last_updated])

        last_updated = uc.addUser("Benita", "33423345", null)
        println("Añadido usuario con ID: $last_updated")
        println(uc.getUser(last_updated)?.info())
        user_stored = uc.getAllUser()

        gc.addGasto(20.2, user_stored[last_updated])
        gc.addGasto(10.1, user_stored[last_updated])
        gc.addGasto(750.42, user_stored[last_updated])

        var gastos = gc.getAllGasto()
        for (us in user_stored) {
            var r_list = RealmList<Gasto>()
            for (gas in gastos) {
                if (gas.usuarioId?.id == us.id) {
                    r_list.add(gas)
                }
            }
            us.nombre?.let { us.pss?.let { it1 -> uc.updateUser(us.id, it, it1, r_list) } }
        }

        user_stored = uc.getAllUser()
        for (us in user_stored) {
            println(us.info())
            var gastos_usuario = us.gastos
            if (gastos_usuario != null) {
                for (gas in gastos_usuario) {
                    println(gas.info())
                }
            }
        }

        println("------------------------------------------")
        var all_gastos = gc.getAllGasto()
        for (gs in all_gastos) {
            println(gs.toString())

        }


    }
}