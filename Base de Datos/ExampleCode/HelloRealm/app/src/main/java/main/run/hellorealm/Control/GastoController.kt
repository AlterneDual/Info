package main.run.hellorealm.Control

import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import main.run.hellorealm.model.Gasto
import main.run.hellorealm.model.Usuario

class GastoController {

    val realm: Realm = Realm.getDefaultInstance()

    fun addGasto(cantidad: Double, usuario: Usuario) {

        realm.executeTransaction { r: Realm ->
            var gasto = r.createObject(Gasto::class.java, getNextKey())
            gasto.cantidad = cantidad
            gasto.usuarioId = usuario
            realm.insertOrUpdate(gasto)
        }

    }

    fun getGasto(id: Int): Gasto? {
        return realm.where(Gasto::class.java).equalTo("id", id).findFirst()
    }




    fun getAllGasto(): RealmList<Gasto> {
        val list = RealmList<Gasto>()
        val user_list = realm.where(Gasto::class.java).findAll()
        user_list?.subList(0, user_list.size)?.let { list.addAll(it) }
        return list
    }

    open fun getNextKey(): Int {
        return try {
            val number: Number? = realm.where<Gasto>().max("id")
            if (number != null) {
                number.toInt() + 1
            } else {
                0
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            0
        }
    }

    fun cleanAll() {
        realm.executeTransaction { r: Realm ->
            r.delete(Gasto::class.java)
        }
    }

    fun getAllGastoByUserId(id: Int): RealmList<Gasto>? {
        val user_list = realm.where(Usuario::class.java).equalTo("id", id).findFirst()
        var list = user_list?.gastos
        return list
    }
}