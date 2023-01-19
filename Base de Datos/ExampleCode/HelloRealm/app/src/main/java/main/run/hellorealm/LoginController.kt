package main.run.hellorealm

import android.content.Context
import android.content.SharedPreferences
import io.realm.Realm
import main.run.hellorealm.model.Usuario
import main.run.hellorealm.utils.Encrypter

open class LoginController : Encrypter() {
    val realm: Realm = Realm.getDefaultInstance()

    fun checkUser(username: String?, pass: String?): Int? {
        return if (username != null && pass != null) {
            exist(username, pass)

        } else {
            0
        }
    }

    private fun exist(name: String, pass: String): Int? {
        val users_list =
            name.let {
                realm.where(Usuario::class.java).contains("nombre", it).findAll()
            }
        if (users_list != null) {
            for (us in users_list) {

                if (us.pss.toString() == encrypt(pass)) {

                    return us.id
                }
            }
            return null
        } else {
            return null
        }
    }


}