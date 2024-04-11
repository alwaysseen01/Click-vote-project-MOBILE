package com.example.clickvoteandroid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainPetitionsFragment : Fragment() {
    private lateinit var petitionsContainer: LinearLayout

    private val url = "http://10.0.2.2:8081/petitions/active/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_petitions, container, false)

        petitionsContainer = view.findViewById(R.id.petitionsContainer)

        fetchData()

        return view
    }

    private fun fetchData() {
        Log.d("MainPetitionsFragment", "Starting fetchData")

        // Retrofit
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(PetitionsApi::class.java)

        val call = api.getActivePetitions() // Assuming endpoint returns a list

        call.enqueue(object : Callback<List<Petition>> {
            override fun onResponse(call: Call<List<Petition>>, response: Response<List<Petition>>) {
                if (response.isSuccessful) {
                    Log.d("MainPetitionsFragment", "Response is successful")
                    val petitionList = response.body()
                    if (petitionList != null) {
                        Log.d("MainPetitionsFragment",
                            "petition list is not null, updating petition info: $petitionList"
                        )
                        updatePetitionInfo(petitionList)
                    } else {
                        Log.d("MainPetitionsFragment", "petition list is null")
                    }
                } else {
                    Log.e("MainPetitionsFragment", "Error fetching data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Petition>>, t: Throwable) {
                Log.e("MainPetitionsFragment", "Error fetching data: $t")
            }
        })
    }

    private fun updatePetitionInfo(petitionList: List<Petition>?) {
        Log.d("MainPetitionsFragment", "Starting updatePetitionInfo")

        // Очистить контейнер перед добавлением
        petitionsContainer.removeAllViews()
        Log.d("MainPetitionsFragment", "Cleared petitionsContainer")

        if (petitionList != null) {
            Log.d("MainPetitionsFragment", "Petition list is not null")
            for (petition in petitionList) {
                Log.d("MainPetitionsFragment", "Processing petition: ${petition.title}")

                val petitionWrapper = LayoutInflater.from(context)
                    .inflate(R.layout.petition_box, petitionsContainer, false)

                val petitionTitleView = petitionWrapper.findViewById<TextView>(R.id.petitionTitle)
                petitionTitleView.text = petition.title
                Log.d("MainPetitionsFragment", "Set petition title to: ${petition.title}")

                val petitionShortDescriptionView = petitionWrapper.findViewById<TextView>(R.id.petitionShortDescription)
                petitionShortDescriptionView.text = petition.shortDescription
                Log.d("MainPetitionsFragment", "Set petition title to: ${petition.shortDescription}")

                petitionsContainer.addView(petitionWrapper)
            }
        } else {
            Log.d("MainPetitionsFragment", "Petition list is null")
        }
    }
}