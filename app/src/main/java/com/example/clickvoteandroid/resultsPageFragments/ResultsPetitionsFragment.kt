package com.example.clickvoteandroid.resultsPageFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.clickvoteandroid.R
import com.example.clickvoteandroid.api.PetitionsApi
import com.example.clickvoteandroid.dataClasses.Petition
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultsPetitionsFragment : Fragment() {
    private lateinit var petitionsContainer: LinearLayout
    private val url = "http://10.0.2.2:8081/petitions/completed/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_results_petitions, container, false)

        petitionsContainer = view.findViewById(R.id.petitionsContainer)

        fetchData()

        return view
    }

    private fun fetchData() {
        Log.d("ResultsPetitionsFragment", "Starting fetchData")

        // Retrofit
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(PetitionsApi::class.java)

        val call = api.getCompletedPetitions() // Assuming endpoint returns a list

        call.enqueue(object : Callback<List<Petition>> {
            override fun onResponse(call: Call<List<Petition>>, response: Response<List<Petition>>) {
                if (response.isSuccessful) {
                    Log.d("ResultsPetitionsFragment", "Response is successful")
                    val petitionList = response.body()
                    if (petitionList != null) {
                        Log.d("ResultsPetitionsFragment",
                            "petition list is not null, updating petition info: $petitionList"
                        )
                        updatePetitionInfo(petitionList)
                    } else {
                        Log.d("ResultsPetitionsFragment", "petition list is null")
                    }
                } else {
                    Log.e("ResultsPetitionsFragment", "Error fetching data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Petition>>, t: Throwable) {
                Log.e("ResultsPetitionsFragment", "Error fetching data: $t")
            }
        })
    }

    private fun updatePetitionInfo(petitionList: List<Petition>?) {
        Log.d("MainPetitionsFragment", "Starting updatePetitionInfo")

        // Очистить контейнер перед добавлением
        petitionsContainer.removeAllViews()
        Log.d("ResultsPetitionsFragment", "Cleared petitionsContainer")

        if (petitionList != null) {
            Log.d("ResultsPetitionsFragment", "Petition list is not null")
            for (petition in petitionList) {
                Log.d("ResultsPetitionsFragment", "Processing petition: ${petition.title}")

                val petitionWrapper = LayoutInflater.from(context)
                    .inflate(R.layout.completed_petition_box, petitionsContainer, false)

                val petitionTitleView = petitionWrapper.findViewById<TextView>(R.id.petitionTitle)
                petitionTitleView.text = petition.title
                Log.d("ResultsPetitionsFragment", "Set petition title to: ${petition.title}")

                val petitionVotesCountView = petitionWrapper.findViewById<TextView>(R.id.petitionVotesCount)
                petitionVotesCountView.text = petition.votesCount.toString()

                val petitionShortDescriptionView = petitionWrapper.findViewById<TextView>(R.id.petitionShortDescription)
                petitionShortDescriptionView.text = petition.shortDescription
                Log.d("ResultsPetitionsFragment", "Set petition title to: ${petition.shortDescription}")

                petitionsContainer.addView(petitionWrapper)
            }
        } else {
            Log.d("ResultsPetitionsFragment", "Petition list is null")
        }
    }
}