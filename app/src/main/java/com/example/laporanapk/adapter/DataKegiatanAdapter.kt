package com.example.laporanapk.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laporanapk.data.DataReport
import com.example.laporanapk.databinding.ItemDataKegiatanBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DataKegiatanAdapter (

    private val listData : ArrayList<DataReport> ):RecyclerView.Adapter<DataKegiatanAdapter.DataKegiatanViewHolder>()
{
        inner class DataKegiatanViewHolder(item:ItemDataKegiatanBinding):RecyclerView.ViewHolder(item.root){
            private val binding = item
            fun bind(dataReport: DataReport){

                with(binding)
                {
                    txtTitle.text = dataReport.nkunjungan
                    txtDesc.text = dataReport.kegiatan
                    txtDate.text = dateFormat(dataReport.tanggal)


                }
            }

            private fun dateFormat(timestamp : String): String? {
                // Parse the timestamp and format it as desired
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd\n\nMMM\n\nyyyy", Locale.getDefault())

                val date = inputFormat.parse(timestamp)
                return date?.let { outputFormat.format(it) }
            }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataKegiatanViewHolder {
        return DataKegiatanViewHolder(ItemDataKegiatanBinding.inflate(LayoutInflater.from(parent.context),
        parent, false
        ))
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: DataKegiatanViewHolder, position: Int) {
        holder.bind(listData[position])
    }








}