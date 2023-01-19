package main.run.hellorealm.Control

import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import main.run.hellorealm.model.Gasto
import main.run.hellorealm.model.Usuario
import main.run.hellorealm.utils.Encrypter

class UsuarioController: Encrypter() {

    val realm: Realm = Realm.getDefaultInstance()

    fun addUser(name: String, pss: String, gastos: RealmList<Gasto>?): Int {
        var pss_encrypt = encrypt(pss)
        var key = getNextKey()

        realm.executeTransaction { r: Realm ->
            var user = r.createObject(Usuario::class.java, key)
            user.nombre = name
            user.pss = pss_encrypt
            if (gastos != null) {
                user.gastos = gastos
            }
            realm.insertOrUpdate(user)
        }
        return key
    }

    fun getUser(id: Int): Usuario? {
        return realm.where(Usuario::class.java).equalTo("id", id).findFirst()
    }

    fun getAllUser(): MutableList<Usuario> {
        val list = mutableListOf<Usuario>()
        val user_list = realm.where(Usuario::class.java).findAll()
        user_list?.subList(0, user_list.size)?.let { list.addAll(it) }

        return list
    }

    fun getNextKey(): Int {
        return try {
            val number: Number? = realm.where<Usuario>().max("id")
            if (number != null) {
                number.toInt() + 1
            } else {
                0
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            0
        }
    }

    fun updateUser(id: Int, name: String, pss: String, gasto_stored: RealmList<Gasto>?) {
        val tgt_user = realm.where(Usuario::class.java).equalTo("id", id).findFirst()
        var pss_checked: String = ""
        if (tgt_user != null) {
            if (tgt_user.pss?.let { decrypt(it).equals(decrypt(pss)) } == true) {
                pss_checked = tgt_user.pss!!
            } else {
                pss_checked = encrypt(pss).toString()
            }
        }
        realm.executeTransaction {
            tgt_user?.nombre = name
            tgt_user?.pss = pss_checked
            tgt_user?.gastos = gasto_stored
            realm.insertOrUpdate(tgt_user)

        }
    }

    fun deleteUser(id: Int) {
        val user = realm.where(Usuario::class.java).equalTo("id", id).findFirst()
        realm.executeTransaction {
            user!!.deleteFromRealm()
        }
    }

    fun cleanAllUser() {
        realm.executeTransaction { r: Realm ->
            r.delete(Usuario::class.java)
        }
    }



}