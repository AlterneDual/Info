package main.run.hellorealm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Gasto : RealmObject() {


    @PrimaryKey
    var id: Int = 0

    var cantidad: Double = 0.0

    var usuario_asociado: Usuario? = null

    override fun toString(): String {
        var user = usuario_asociado?.info()
        return ("$id -> $cantidad. Con el usuario asociado $user")
    }

    fun info(): String {
        return ("$id -> $cantidad")
    }
}