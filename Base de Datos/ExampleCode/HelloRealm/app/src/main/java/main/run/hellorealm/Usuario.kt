package main.run.hellorealm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class Usuario() : RealmObject() {


    @PrimaryKey
    var id: Int = 0

    @Required
    var nombre: String? = ""

    @Required
    var pss: String? = ""

    override fun toString(): String {
        return ("$id -> Usuario: $nombre; Password: $pss ")
    }


}
