package main.run.hellorealm

import io.realm.Realm
import io.realm.RealmList

class UsuarioGastoController {
    val realm:Realm = Realm.getDefaultInstance()

    fun getAllGasto(us:Usuario): RealmList<Gasto> {
        val list = RealmList<Gasto>()
        val user_list = realm.where(Gasto::class.java).equalTo("usuario_asociado",us.id).findAll()
        user_list?.subList(0, user_list.size)?.let { list.addAll(it) }
        return list
    }
}