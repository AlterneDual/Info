package main.run.hellorealm.model

import io.realm.*

import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required
import main.run.hellorealm.utils.Encrypter

@RealmClass
open class Usuario : RealmObject() {

    @Transient
    var ec = Encrypter()

    @PrimaryKey
    var id: Int = 0

    @Required
    var nombre: String? = ""

    @Required
    var pss: String? = ""

    var gastos: RealmList<Gasto>? = RealmList<Gasto>()


    fun info(): String {
        return ("$id -> Usuario: $nombre; Password: $pss")
    }

    override fun toString(): String {
        var gastos_tense: String? = ""

        if (gastos != null) {
            for (gast in gastos!!) {
                gastos_tense = "$gastos_tense |${gast.info()}"
            }
        }

        var decrypted_pass = pss?.let { ec.decrypt(it) }
        if (gastos?.isNotEmpty() == true) {
            return ("$id -> Usuario: $nombre; Password: $decrypted_pass " +
                    "\n Con los siguientes gastos: $gastos_tense ")
        } else {
            return ("$id -> Usuario: $nombre; Password: $decrypted_pass")
        }

    }


}
