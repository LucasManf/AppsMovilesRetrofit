package com.example.clase9appsmovilesii

import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BreedsAdapter
    private var imagesByBreedsList = mutableListOf<String>()
    private lateinit var spinner: Spinner
    private var breedsList = mutableListOf<String>()

    private val viewModel:BreedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        spinner = findViewById(R.id.spinner)

        adapter = BreedsAdapter(imagesByBreedsList)
        recyclerView.adapter = adapter

        getListOfBreeds()

    }

    private fun getListOfBreeds() {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getListOfBreeds("breeds/list/all")
            val response: BreedsResponse? = call.body()

            runOnUiThread {
                if(call.isSuccessful) {
                    val breeds = response?.message

                    breeds?.keys?.forEach {
                        breedsList.add(it)
                    }

                    setSpinner()
                }

            }
        }
    }

    private fun getImagesByBreed(breed: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getImagesByBreed("breed/$breed/images")
            val response: BreedResponse? = call.body()

            runOnUiThread {
                if(call.isSuccessful) {
                    val images = response?.images ?: emptyList()

                    imagesByBreedsList.clear()

                    imagesByBreedsList.addAll(images)

                    adapter.notifyDataSetChanged()
                }

            }

        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun setSpinner() {
        val spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, breedsList)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                getImagesByBreed(breedsList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}