package main.run.hellorealm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*


open class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)

        val realmName = "ProyectoPrueba"
        val config =
            RealmConfiguration.Builder().name(realmName).deleteRealmIfMigrationNeeded()
                .schemaVersion(0).allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)

        var uc = UsuarioController()
        //Limpiar Base de Datos
        uc.cleanAllUser()

        // Insertar Usuario
        uc.addUser("UsuarioA", "12345678")
        uc.addUser("UsuarioB", "87654321")
        uc.addUser("UsuarioC", "ABCD")
        uc.addUser("UsuarioD", "EFGH")
        println("Insertando ...")
        cleanDot(3)

        // Listar All User
        var user_list = uc.getAllUser()
        println("----------------------------------------------------")
        println("Usuarios de la base de Datos Actualmente: ")
        for (user in user_list) {
            println(user.toString())
        }
        println("----------------------------------------------------")
        cleanDot(3)

        // Listar Usuario con Id:1
        println("Usuario con ID = 1")
        println(uc.getUser(1).toString())
        cleanDot(3)


        //Eliminar Usuario con Id:1
        println("Eliminando usuario con id=1 ...")
        uc.deleteUser(1)
        user_list = uc.getAllUser()
        println("------------------------------------------")
        println("Listado Usuarios despues de la eliminacion")
        for (user in user_list) {
            println(user.toString())
        }
        println("------------------------------------------")
        cleanDot(5)

        // Editar Usuario con Id=3
        var usuario = uc.getUser(3)
        (if (usuario != null) {
            usuario.pss?.let { uc.updateUser(usuario.id, "Cambio de Nombre de Usuario", it) }
        })

        var usuario2 = uc.getUser(0)
        (if (usuario2 != null) {
            usuario2.nombre?.let { uc.updateUser(usuario2.id, it, "SusanitaTieneUnRaton") }
        })

        println("------------------------------------------")
        user_list = uc.getAllUser()
        println("Listado Usuarios despues de la Actualizacion")
        for (user in user_list) {
            println(user.toString())
        }
        println("------------------------------------------")
        cleanDot(5)

        // Limpiar todos los Usuarios
        uc.cleanAllUser()
        user_list = uc.getAllUser()
        println("------------------------------------------")
        println("Listado Usuarios(Listado debe estar vacio)")
        for (user in user_list) {
            println(user.toString())
        }
        println("------------------------------------------")
        cleanDot(1)

    }

    fun cleanDot(v: Int) {
        for (i in 0..v) {
            println(" ")
        }
    }


}



