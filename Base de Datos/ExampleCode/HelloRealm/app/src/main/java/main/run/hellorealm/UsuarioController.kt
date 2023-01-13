package main.run.hellorealm

import io.realm.Realm
import io.realm.kotlin.where

class UsuarioController {

    val realm: Realm = Realm.getDefaultInstance()


    fun addUser(name: String, pss: String) {

        realm.executeTransaction { r: Realm ->
            var user = r.createObject(Usuario::class.java, getNextKey())
//                user.id = getNextKey()
            user.nombre = name
            user.pss = pss
            realm.insertOrUpdate(user)
        }

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

    open fun getNextKey(): Int {
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

    open fun updateUser(id:Int,name:String,pss:String) {
        val tgt_user = realm.where(Usuario::class.java).equalTo("id", id).findFirst()

        realm.executeTransaction {
            tgt_user?.nombre = name
            tgt_user?.pss = pss
            realm.insertOrUpdate(tgt_user)

        }
    }

    open fun deleteUser(id: Int) {
        val user = realm.where(Usuario::class.java).equalTo("id", id).findFirst()
        realm.executeTransaction {
            user!!.deleteFromRealm()
        }
    }

    open fun cleanAllUser() {
        realm.executeTransaction { r: Realm ->
            r.delete(Usuario::class.java)
        }
    }

}