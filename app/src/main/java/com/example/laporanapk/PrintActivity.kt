package com.example.laporanapk
import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.widget.Toast
import com.example.laporanapk.data.DataReport
import com.example.laporanapk.databinding.ActivityPrintBinding
import com.example.laporanapk.model.ResponseDataByDate
import com.example.laporanapk.pref.PrefManager
import com.example.laporanapk.utils.RetrofitClient
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PrintActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPrintBinding
    private lateinit var pref : PrefManager




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val toolbar = binding.toolbar
        pref = PrefManager(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnPrint.setOnClickListener {


            getData()


        }

        binding.txtDateStartLabel.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.txtDateStart.text = dateTostring(year, month, dayOfMonth)
            }
            dateDialog(this, datePicker).show()
        }

        binding.txtDateEndLabel.setOnClickListener {
            val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                binding.txtDateEnd.text = dateTostring(year, month, dayOfMonth)
            }
            dateDialog(this, datePicker).show()
        }
    }


    private fun getData() {
        val startDate = binding.txtDateStart.text.toString()
        val endDate = binding.txtDateEnd.text.toString()

        val  tokenAuth = pref.getToken()
        RetrofitClient.instances.getDataByDate(tokenAuth,startDate,endDate).enqueue(object : Callback<ResponseDataByDate>{
            override fun onResponse(
                call: Call<ResponseDataByDate>,
                response: Response<ResponseDataByDate>
            ) {
                if (response.isSuccessful)
                {
                    val data = response.body()?.data
                    convertDataToPDF(data)
                }

            }

            override fun onFailure(call: Call<ResponseDataByDate>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun convertDataToPDF(data: List<DataReport>?) {
        val file = "/laporan"
        val pdfDate = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + file

        val dir = File(path)
        if (!dir.exists())
        {
            dir.mkdir()
            Toast.makeText(this, "Membuat Folder", Toast.LENGTH_SHORT).show()
        }
        val archive = File(dir, "laporan$pdfDate.pdf")

        try {
            val fos = FileOutputStream(archive)
            val document = Document()
            PdfWriter.getInstance(document, fos)
            document.open()

            val  title = Paragraph(
                "Laporan Bulan Ini\n\n",
                FontFactory.getFont("arial", 18f, Font.BOLD,BaseColor.BLACK )
            )
            document.add(title)


            // Create a table with 6 columns
            val table = PdfPTable(6)
            table.widthPercentage = 100f

            // Add table headers
            table.addCell("Nagari Kunjungan")
            table.addCell("Kegiatan")
            table.addCell("Hasil")
            table.addCell("Langkah")
            table.addCell("Rekomendasi")
            table.addCell("Tanggal")

            // Add data to the table
            data?.forEach {
                table.addCell(it.nkunjungan)
                table.addCell(it.kegiatan)
                table.addCell(it.hasil)
                table.addCell(it.langkah)
                table.addCell(it.rekomendasi)
                table.addCell(dateFormat(it.tanggal))
            }

            // Add the table to the document
            document.add(table)

            // Close the document
            document.close()
            fos.close()
            Toast.makeText(this, "berhasil data tersimpan di\n $path", Toast.LENGTH_SHORT).show()
        } catch (e : Exception)
        {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }


    }

    private fun dateFormat(timestamp : String): String? {
        // Parse the timestamp and format it as desired
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(timestamp)
        return date?.let { outputFormat.format(it) }
    }

    private fun dateTostring(year: Int, month: Int, dayOfMonth: Int): String {

        return year.toString()+"-"+(month+1)+"-"+dayOfMonth.toString()

    }

    private fun dateDialog(context: Context, datePicker: DatePickerDialog.OnDateSetListener): DatePickerDialog {

            val calender = Calendar.getInstance()
           return DatePickerDialog(
            context, datePicker,
            calender[Calendar.YEAR],
            calender[Calendar.MONTH],
            calender[Calendar.DAY_OF_MONTH],
        )
    }


}