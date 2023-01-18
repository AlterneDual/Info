package main.run.hellorealm

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration


open class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
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
        var gc = GastoController()
        var ugc = UsuarioGastoController()
//        //Limpiar Base de Datos
//        uc.cleanAllUser()
//
//        // Insertar Usuario
//        uc.addUser("UsuarioA", "12345678")
//        uc.addUser("UsuarioB", "87654321")
//        uc.addUser("UsuarioC", "ABCD")
//        uc.addUser("UsuarioD", "EFGH")
//        println("Insertando ...")
//        cleanDot(3)
//
//        // Listar All User
//        var user_list = uc.getAllUser()
//        println("----------------------------------------------------")
//        println("Usuarios de la base de Datos Actualmente: ")
//        for (user in user_list) {
//            println(user.toString())
//        }
//        println("----------------------------------------------------")
//        cleanDot(3)
//
//        // Listar Usuario con Id:1
//        println("Usuario con ID = 1")
//        println(uc.getUser(1).toString())
//        cleanDot(3)
//
//
//        //Eliminar Usuario con Id:1
//        println("Eliminando usuario con id=1 ...")
//        uc.deleteUser(1)
//        user_list = uc.getAllUser()
//        println("------------------------------------------")
//        println("Listado Usuarios despues de la eliminacion")
//        for (user in user_list) {
//            println(user.toString())
//        }
//        println("------------------------------------------")
//        cleanDot(5)
//
//        // Editar Usuario con Id=3
//        var usuario = uc.getUser(3)
//        (if (usuario != null) {
//            usuario.pss?.let { uc.updateUser(usuario.id, "Cambio de Nombre de Usuario", it) }
//        })
//
//        var usuario2 = uc.getUser(0)
//        (if (usuario2 != null) {
//            usuario2.nombre?.let { uc.updateUser(usuario2.id, it, "SusanitaTieneUnRaton") }
//        })
//
//        println("------------------------------------------")
//        user_list = uc.getAllUser()
//        println("Listado Usuarios despues de la Actualizacion")
//        for (user in user_list) {
//            println(user.toString())
//        }
//        println("------------------------------------------")
//        cleanDot(5)
//
//        // Limpiar todos los Usuarios
//        uc.cleanAllUser()
//        user_list = uc.getAllUser()
//        println("------------------------------------------")
//        println("Listado Usuarios(Listado debe estar vacio)")
//        for (user in user_list) {
//            println(user.toString())
//        }
//        println("------------------------------------------")
//        cleanDot(1)
//
//    }
//
//    fun cleanDot(v: Int) {
//        for (i in 0..v) {
//            println(" ")
//        }


//        var g1 = Gasto()
//        var nombre = "Juanillo"
//        var pss = "Prueba"
//        var ec = Encrypter()
//        var uc = UsuarioController()
//
//        uc.cleanAllUser()
//        var pss_encrypt = ec.encrypt(pss)
//
//        println("Nombre de usuario: ${nombre}")
//        println("Contraseña: $pss")
//        println("Contraseña Encryptada: $pss_encrypt")
//        println("------------------------------------")
//
//        uc.addUser(nombre, pss_encrypt!!)
//
//        var user: Usuario = uc.getAllUser().first()
//        println("ID de usuario recogido de la BD: ${user.id}")
//        println("Nombre de usuario recogido de la BD: ${user.nombre}")
//        println("Contraseña recogida de la BD  ${user.pss}")
//        var pss_decrypt = ec.decrypt(user.pss!!)
//        println("Contraseña recogida de la BD (Desencriptada): $pss_decrypt")

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
        var gasto_stored = ugc.getAllGasto(user_stored[last_updated])

        user_stored[last_updated].nombre?.let {
            user_stored[last_updated].pss?.let { it1 ->
                uc.updateUser(
                    user_stored[last_updated].id, it, it1, gasto_stored
                )
            }
        }


        last_updated = uc.addUser("Benita", "33423345", null)
        println("Añadido usuario con ID: $last_updated")
        println(uc.getUser(last_updated)?.info())
        user_stored = uc.getAllUser()

        gc.addGasto(20.2, user_stored[last_updated])
        gc.addGasto(10.1, user_stored[last_updated])
        gc.addGasto(750.42, user_stored[last_updated])
        gasto_stored = ugc.getAllGasto(user_stored[last_updated])


        user_stored[last_updated].nombre?.let {
            user_stored[last_updated].pss?.let { it1 ->
                uc.updateUser(
                    user_stored[last_updated].id, it, it1, gasto_stored
                )
            }
        }

        user_stored = uc.getAllUser()
        for (us in user_stored) {
            println(us.toString())
        }
        var all_gastos = gc.getAllGasto()
        for (gs in all_gastos) {
            println(gs.toString())

        }

    }
}



