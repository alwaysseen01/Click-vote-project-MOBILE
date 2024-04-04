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
    private lateinit var electionWrapper: LinearLayout
    private lateinit var electionCandidateBox: LinearLayout

    private val url = "http://10.0.2.2:8081/elections/active/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_elections, container, false)

        electionsContainer = view.findViewById(R.id.electionsContainer)

        fetchData()

        return view
    }

    private fun fetchData() {
        Log.d("MainElectionsFragment", "Starting fetchData")

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
                    Log.d("MainElectionsFragment", "Response is successful")
                    val electionList = response.body()
                    if (electionList != null) {
                        Log.d("MainElectionsFragment",
                            "Election list is not null, updating election info: $electionList"
                        )
                        updateElectionInfo(electionList)
                    } else {
                        Log.d("MainElectionsFragment", "Election list is null")
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
        Log.d("MainElectionsFragment", "Starting updateElectionInfo")

        // Очистить контейнер перед добавлением
        electionsContainer.removeAllViews()
        Log.d("MainElectionsFragment", "Cleared electionsContainer")

        if (electionList != null) {
            Log.d("MainElectionsFragment", "Election list is not null")
            for (election in electionList) {
                Log.d("MainElectionsFragment", "Processing election: ${election.title}")

                val electionWrapper = LayoutInflater.from(context)
                    .inflate(R.layout.election_box, electionsContainer, false)

                val electionTitleView = electionWrapper.findViewById<TextView>(R.id.electionTitle)
                electionTitleView.text = election.title
                Log.d("MainElectionsFragment", "Set election title to: ${election.title}")

                for (candidate in election.options) {
                    Log.d("MainElectionsFragment", "Processing candidate: ${candidate.firstName} ${candidate.lastName}")

                    val electionCandidateBox = LayoutInflater.from(context)
                        .inflate(R.layout.election_candidate_box, null, false)

                    val candidatePhotoView = electionCandidateBox.findViewById<ImageView>(R.id.electionCandidateImage)
                    val candidatePhotoURL = candidate.photoUrl
                    Picasso.get()
                        .load(candidatePhotoURL)
                        .into(candidatePhotoView);
                    Log.d("MainElectionsFragment", "Loaded candidate photo from URL: $candidatePhotoURL")

                    // Full name
                    val candidateNameView = electionCandidateBox.findViewById<TextView>(R.id.electionCandidateFullNameField)
                    val candidateName = "${candidate.firstName} ${candidate.lastName}"
                    candidateNameView.text = candidateName
                    Log.d("MainElectionsFragment", "Set candidate name to: $candidateName")

                    // Age
                    val candidateAgeView = electionCandidateBox.findViewById<TextView>(R.id.electionCandidateAgeField)
                    val candidateAge = candidate.dateOfBirth
                    candidateAgeView.text = candidateAge
                    Log.d("MainElectionsFragment", "Set candidate age to: $candidateAge")

                    // Short description
                    val candidateShortDescriptionView = electionCandidateBox.findViewById<TextView>(R.id.electionCandidateShortDescriptionField)
                    val candidateShortDescription = candidate.shortDescription


                    // Добавить candidateView в контейнер
                    val candidateContainer = electionWrapper.findViewById<LinearLayout>(R.id.electionCandidateContainer)
                    candidateContainer.addView(electionCandidateBox)
                }

                electionsContainer.addView(electionWrapper)
            }
        } else {
            Log.d("MainElectionsFragment", "Election list is null")
        }
    }
}
