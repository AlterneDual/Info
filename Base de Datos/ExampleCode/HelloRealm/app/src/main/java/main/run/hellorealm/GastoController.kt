package main.run.hellorealm

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.kotlin.where
import org.bson.types.ObjectId

class GastoController {

    val realm: Realm = Realm.getDefaultInstance()

    fun addGasto(cantidad: Double, usuario: Usuario) {

        realm.executeTransaction { r: Realm ->
            var gasto = r.createObject(Gasto::class.java, getNextKey())
            gasto.cantidad = cantidad
            gasto.usuario_asociado = usuario
            realm.insertOrUpdate(gasto)
        }

    }

    fun getGasto(id: Int): Gasto? {
        return realm.where(Gasto::class.java).equalTo("id", id).findFirst()
    }

    fun getAllGasto(usuario: Usuario): RealmList<Gasto> {

        val list = RealmList<Gasto>()
        val user_list =
            realm.where(Gasto::class.java).in("usuario_asociado", usuario).findAll()
        user_list?.subList(0, user_list.size)?.let { list.addAll(it) }
        return list
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
}