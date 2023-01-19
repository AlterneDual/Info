package main.run.hellorealm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import main.run.hellorealm.databinding.LoggedActivityBinding

class Logged : AppCompatActivity() {

    private lateinit var binding: LoggedActivityBinding

    var PREFS_KEY = "prefs"
    var EMAIL_KEY = "email"
    var email = ""

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoggedActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Realm.init(this)

        val realmName = "ProyectoPrueba"
        val config =
            RealmConfiguration.Builder().name(realmName).deleteRealmIfMigrationNeeded()
                .schemaVersion(0).allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)

        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        email = sharedPreferences.getString(EMAIL_KEY, null)!!
        binding.idTVUserName.text = "Bienvenido \n $email"

        binding.idBtnLogOut.setOnClickListener {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val i = Intent(this, Login::class.java)
            startActivity(i)
            finish()
        }

        binding.idBtnPieChart.setOnClickListener {
            val i = Intent(this, PieChart::class.java)
            startActivity(i)
            finish()
        }


    }
}