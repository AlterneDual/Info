package main.run.hellorealm.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Gasto : RealmObject() {

    @PrimaryKey
    var id: Int = 0

    var cantidad: Double = 0.0
    var usuarioId: Usuario? = null

    override fun toString(): String {
        var user = usuarioId?.info()
        return ("$id -> $cantidad. Con el usuario asociado $user")
    }

    fun info(): String {
        return ("$id -> $cantidad")
    }
}