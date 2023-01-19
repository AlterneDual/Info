package main.run.hellorealm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import main.run.hellorealm.Control.GastoController
import main.run.hellorealm.Control.UsuarioController
import main.run.hellorealm.databinding.PieChartBinding
import main.run.hellorealm.model.Gasto

class PieChart : AppCompatActivity() {
    lateinit var pieChart: PieChart
    lateinit var binding: PieChartBinding
    lateinit var uc:UsuarioController
    lateinit var gc:GastoController
    private lateinit var sharedPreferences: SharedPreferences

    private val PREFS_KEY = "prefs"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Para acceder a las ids de manera mas efectiva
        binding = PieChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Realm.init(this)
        val realmName = "ProyectoPrueba"
        val config =
            RealmConfiguration.Builder().name(realmName).deleteRealmIfMigrationNeeded()
                .schemaVersion(0).allowWritesOnUiThread(true).allowQueriesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)

        gc=GastoController()
        uc= UsuarioController()

        sharedPreferences = getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
        var id=sharedPreferences.getInt("id",-1)


        // Asociar la Grafica
        pieChart = binding.pieChart

        // Valores porcentuales (Tambien pueden establecerse en bruto
        pieChart.setUsePercentValues(false)
        pieChart.description.isEnabled = false

        pieChart.setExtraOffsets(0f, 0f, 0f, 0f)

        pieChart.dragDecelerationFrictionCoef = 10f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleAlpha(0)
        pieChart.transparentCircleRadius = 40F
        pieChart.holeRadius = 35F

        pieChart.setDrawCenterText(true)
        pieChart.centerText = "Hola"
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = false
        pieChart.isHighlightPerTapEnabled = false
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.isEnabled = true
        pieChart.setEntryLabelColor(ContextCompat.getColor(this, R.color.transparent))
        pieChart.setEntryLabelTextSize(12f)


//        val entries: ArrayList<PieEntry> = ArrayList()
//        entries.add(PieEntry(60f, "Casa"))
//        entries.add(PieEntry(20f, "Dia"))
//        entries.add(PieEntry(10f, "Noche"))


//        val mapped = HashMap<String, Int>()
//        mapped["Vacaciones"] = 200
//        mapped["Alojamiento"] = 400
//        mapped["Comida"] = 100
//        mapped["Otros"] = 50
//        mapped["Ocio"] = 400

        var mapped:RealmList<Gasto>?
        var labels: ArrayList<Gasto> = ArrayList<Gasto>()
        if(id>=0){
            mapped=uc.getAllGastoByUserId(id)

            if (mapped != null) {
                for(map in mapped){
                    labels.add(map)
                }
            }
        }
//
//        for (k in mapped.keys) {
//            labels.add(k)
//        }

        var entries_map: ArrayList<PieEntry> = ArrayList()
        for (ob in labels) {
            entries_map.add(PieEntry(ob.cantidad.toFloat(), ob.id.toString()))
        }


        val dataSet = PieDataSet(entries_map, "")
        dataSet.setDrawIcons(true)
        dataSet.setDrawValues(false)
        dataSet.valueTextSize = 10f

        // Ajustes de Slice
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(10f, 40f)
        dataSet.selectionShift = 10f

        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(this, R.color.vidrian_green))
        colors.add(ContextCompat.getColor(this, R.color.orange))
        colors.add(ContextCompat.getColor(this, R.color.red))
        colors.add(ContextCompat.getColor(this, R.color.dark_liver))
        colors.add(ContextCompat.getColor(this, R.color.green))

        dataSet.colors = colors

        val data = PieData(dataSet)
        data.setDrawValues(false)
        data.setValueFormatter(null)
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.SANS_SERIF)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

//        pieChart.highlightValues(null)
        pieChart.invalidate()

        binding.backLog.setOnClickListener {
            val i = Intent(this, Logged::class.java)
            startActivity(i)
            finish()
        }

    }
}