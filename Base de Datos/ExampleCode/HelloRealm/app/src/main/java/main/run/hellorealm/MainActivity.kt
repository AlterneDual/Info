package main.run.hellorealm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where


open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)
        val realmName: String = "ProyectoPrueba"
        val config =
            RealmConfiguration.Builder().name(realmName).allowWritesOnUiThread(true).build()
        val backThreadRealm: Realm = Realm.getInstance(config)

//        var key = (backThreadRealm.where<Usuario>().max("id")?.toInt() ?: -1) + 1
        var user1 = Usuario()
        user1.id = 5
        user1.nombre = "jeje"
        user1.pss = "1234"

//        key = (backThreadRealm.where<Usuario>().max("id")?.toInt() ?: -1) + 1
        var user2 = Usuario()
        user1.id = 6
        user1.nombre = "jusnillo"
        user1.pss = "5678"

        var usuarios = mutableListOf<Usuario>()
        usuarios.add(user1)
        usuarios.add(user2)

        for (us in usuarios) {
            backThreadRealm.executeTransaction { transactionRealm ->
                transactionRealm.insert(us)
            }
        }

        val user_list = backThreadRealm.where<Usuario>().findAll()

        println("----------------------------------------------------")
        for (user in user_list) {
            println(user.toString())
        }
        println("----------------------------------------------------")

    }


}