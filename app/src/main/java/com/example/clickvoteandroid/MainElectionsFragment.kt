package com.example.clickvoteandroid

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainElectionsFragment : Fragment() {

    private lateinit var electionsContainer: LinearLayout
    private lateinit var electionTitle: TextView

    private val url = "http://localhost:8081/elections/active/" // URL API-метода на сервере

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_elections, container, false)

        electionsContainer = view.findViewById(R.id.electionsContainer)
        electionTitle = view.findViewById(R.id.electionTitle)

        fetchData()

        return view
    }

    private fun fetchData() {
        // Retrofit
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(ElectionsApi::class.java)

        val call = api.getActiveElections() // Assuming endpoint returns a list

        call.enqueue(object : Callback<List<Election>> {
            override fun onResponse(call: Call<List<Election>>, response: Response<List<Election>>) {
                if (response.isSuccessful) {
                    val electionList = response.body()
                    if (electionList != null) {
                        updateElectionInfo(electionList)
                    }
                } else {
                    Log.e("MainElectionsFragment", "Error fetching data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Election>>, t: Throwable) {
                Log.e("MainElectionsFragment", "Error fetching data: $t")
            }
        })
    }

    private fun updateElectionInfo(electionList: List<Election>?) {
        // Очистить контейнер перед добавлением
        electionsContainer.removeAllViews()

        // Создать LinearLayout для каждого Election
        if (electionList != null) {
            for (election in electionList) {
                val electionWrapper = LayoutInflater.from(context)
                    .inflate(electionsContainer.findViewById(R.id.electionWrapper), electionsContainer, false)

                // Заполнить информацию о выборах (electionTitle)
                val electionTitleView = electionWrapper.findViewById<TextView>(R.id.electionTitle)
                electionTitleView.text = election.title

                // Создать LinearLayout для каждого кандидата
                val candidateContainer = electionWrapper.findViewById<LinearLayout>(R.id.electionCandidateBox) // Assuming id for candidate container
                candidateContainer.removeAllViews() // Clear candidates before adding

                for (candidate in election.options) {
                    val candidateView = LayoutInflater.from(context)
                        .inflate(electionsContainer.findViewById(R.id.electionCandidateBox), candidateContainer, false)

                    // Photo URL
                    val candidatePhotoView = candidateView.findViewById<ImageView>(R.id.electionCandidateImage)
                    val candidatePhotoURL = candidate.photoUrl
                    Picasso.get()
                        .load(candidatePhotoURL)
                        .into(candidatePhotoView);

                    // Full name
                    val candidateNameView = candidateView.findViewById<TextView>(R.id.electionCandidateFullNameField)
                    val candidateName = "${candidate.firstName} ${candidate.lastName}" // Assuming names are combined
                    candidateNameView.text = candidateName

                    // Age
                    val candidateAgeView = candidateView.findViewById<TextView>(R.id.electionCandidateAgeField)
                    val candidateAge = candidate.dateOfBirth
                    candidateAgeView.text = candidateAge

                    // Short description
                    val candidateShortDescriptionView = candidateView.findViewById<TextView>(R.id.electionCandidateShortDescriptionField)
                    val candidateShortDescription = candidate.shortDescription
                    candidateShortDescriptionView.text = candidateShortDescription

                    candidateContainer.addView(candidateView)
                }

                electionsContainer.addView(electionWrapper)
            }
        }
    }
}
