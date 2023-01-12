package main.run.hellorealm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Usuario() : RealmObject() {


    @PrimaryKey
    var id: Int = 0

    @Required
    var nombre: String? = ""
    var pss: String? = ""

    override fun toString(): String {
        return ("$id -> Usuario: $nombre; Password: $pss ")
    }


}
