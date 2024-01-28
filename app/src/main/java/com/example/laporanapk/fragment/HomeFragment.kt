package com.example.laporanapk.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.laporanapk.LoginActivity
import com.example.laporanapk.adapter.DataKegiatanAdapter
import com.example.laporanapk.data.DataReport
import com.example.laporanapk.databinding.FragmentHomeBinding
import com.example.laporanapk.model.ResponseGetData
import com.example.laporanapk.pref.PrefManager
import com.example.laporanapk.utils.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
private  var _binding : FragmentHomeBinding? = null
    private val  binding get() = _binding
    private val lisData = ArrayList<DataReport> ()
    private lateinit var adapter: DataKegiatanAdapter
   private lateinit var pref : PrefManager
    private var page = 1
    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = PrefManager(requireContext())
        initRecyclerview()
        adapter = DataKegiatanAdapter(lisData)
        binding?.rcData?.adapter = adapter
        getData()


    }

    private fun initRecyclerview() {
        binding?.rcData?.setHasFixedSize(true)
        binding?.rcData?.layoutManager = LinearLayoutManager(context)

        binding?.rcData?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && totalItemCount <= (firstVisibleItemPosition + visibleItemCount)) {
                        page++
                        getData()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        lisData.clear()
        getData()
        page = 1
        isLoading = false

    }


    private fun getData()
    {
        isLoading = true
        val tokenAuth = pref.getToken()
        binding?.progressBar?.visibility = View.VISIBLE
        RetrofitClient.instances.getDataLatest(tokenAuth,page).enqueue(object : Callback<ResponseGetData> {

            override fun onResponse(call: Call<ResponseGetData>, response: Response<ResponseGetData>) {
                isLoading = false
                if (response.isSuccessful) {
                    response.body()?.let {
                        lisData.addAll(it.data)
                        adapter.notifyDataSetChanged()
                    }

                    binding?.progressBar?.visibility = View.INVISIBLE
                } else {

                    val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
                    val messageError = JSONObject(jsonObject.getString("errors"))
                    if (messageError.getString("message").isNotEmpty()) {
                        val errorMessage = messageError.getString("message")
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(context, LoginActivity::class.java))


                    }
                }
            }

            override fun onFailure(call: Call<ResponseGetData>, t: Throwable) {
                isLoading = false
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}