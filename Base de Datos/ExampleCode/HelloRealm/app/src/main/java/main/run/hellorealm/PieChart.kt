package main.run.hellorealm

import android.content.Intent
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
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import main.run.hellorealm.databinding.PieChartBinding

class PieChart : AppCompatActivity() {
    lateinit var pieChart: PieChart
    lateinit var binding: PieChartBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PieChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pieChart = binding.pieChart

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


        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(60f, "Casa"))
        entries.add(PieEntry(20f, "Dia"))
        entries.add(PieEntry(10f, "Noche"))


        val mapped = HashMap<String, Int>()
        mapped["Vacaciones"] = 200
        mapped["Alojamiento"] = 400
        mapped["Comida"] = 100
        mapped["Otros"] = 50
        mapped["Ocio"] = 400
        var labels: ArrayList<String> = ArrayList<String>()
        for (k in mapped.keys) {
            labels.add(k)
        }

        var entries_map: ArrayList<PieEntry> = ArrayList()
        for (ob in mapped) {
            entries_map.add(PieEntry(ob.value.toFloat(), ob.key))
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