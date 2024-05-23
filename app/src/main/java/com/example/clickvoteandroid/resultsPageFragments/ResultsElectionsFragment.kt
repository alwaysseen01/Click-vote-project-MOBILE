package com.example.clickvoteandroid.resultsPageFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.example.clickvoteandroid.R
import com.example.clickvoteandroid.api.ElectionsApi
import com.example.clickvoteandroid.dataClasses.Election
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultsElectionsFragment : Fragment() {
    private lateinit var electionsContainer: LinearLayout

    private val completedElectionsUrl = "http://172.20.10.6:8081/elections/completed/"
    private val completedElectionsResultsUrl = "http://172.20.10.6:8081/{election_id}/results/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_results_elections, container, false)

        electionsContainer = view.findViewById(R.id.electionsContainer)

        fetchData()

        return view
    }

    private fun fetchData() {
        Log.d("ResultsElectionsFragment", "Starting fetchData")

        // Retrofit
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(completedElectionsUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(ElectionsApi::class.java)

        val call = api.getCompletedElections() // Assuming endpoint returns a list

        call.enqueue(object : Callback<List<Election>> {
            override fun onResponse(call: Call<List<Election>>, response: Response<List<Election>>) {
                if (response.isSuccessful) {
                    Log.d("ResultsElectionsFragment", "Response is successful")
                    val electionList = response.body()
                    if (electionList != null) {
                        Log.d("ResultsElectionsFragment",
                            "Election list is not null, updating election info: $electionList"
                        )
                        updateElectionInfo(electionList)
                    } else {
                        Log.d("ResultsElectionsFragment", "Election list is null")
                    }
                } else {
                    Log.e("ResultsElectionsFragment", "Error fetching data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Election>>, t: Throwable) {
                Log.e("ResultsElectionsFragment", "Error fetching data: $t")
            }
        })
    }

    private fun updateElectionInfo(electionList: List<Election>?) {
        Log.d("ResultsElectionsFragment", "Starting updateElectionInfo")

        electionsContainer.removeAllViews()
        Log.d("ResultsElectionsFragment", "Cleared electionsContainer")

        if (electionList != null) {
            Log.d("ResultsElectionsFragment", "Election list is not null")
            for (election in electionList) {
                Log.d("ResultsElectionsFragment", "Processing election: ${election.title}")

                val electionWrapper = LayoutInflater.from(context)
                    .inflate(R.layout.finished_election_box, electionsContainer, false)

                val electionTitleView = electionWrapper.findViewById<TextView>(R.id.electionTitle)
                electionTitleView.text = election.title
                Log.d("ResultsElectionsFragment", "Set election title to: ${election.title}")

                // Fetch election results and winner
                fetchElectionResults(election, electionWrapper)

                electionsContainer.addView(electionWrapper)
            }
        } else {
            Log.d("ResultsElectionsFragment", "Election list is null")
        }
    }

    private fun fetchElectionResults(election: Election, electionWrapper: View) {
        Log.d("ResultsElectionsFragment", "Starting fetchElectionResults")

        // Замените {election_id} на реальный electionId в URL
        val completedElectionsResultsUrl = completedElectionsResultsUrl.replace("{election_id}", election.id.toString())
        Log.d("ResultsElectionsFragment", "ELECTION ID: $election.id")

        // Retrofit
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(completedElectionsResultsUrl)  // Используйте базовый URL здесь
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(ElectionsApi::class.java)

        val call = api.getCompletedElectionResults(election.id)

        call.enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    Log.d("ResultsElectionsFragment", "Response is successful")
                    val electionOptionsAndPercentsList = response.body()
                    if (electionOptionsAndPercentsList != null) {
                        Log.d("ResultsElectionsFragment",
                            "electionOptionsAndPercentsList is not null, updating election info: $electionOptionsAndPercentsList"
                        )
                        updateElectionResults(election, electionOptionsAndPercentsList, electionWrapper)
                    } else {
                        Log.d("ResultsElectionsFragment", "electionOptionsAndPercentsList is null")
                    }
                } else {
                    Log.e("ResultsElectionsFragment", "Error fetching results data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                Log.e("ResultsElectionsFragment", "Error fetching results data: $t")
            }
        })
    }

    private fun updateElectionResults(election: Election, electionResults: List<Map<String, Any>>?, electionWrapper: View) {
        Log.d("ResultsElectionsFragment", "Starting updateElectionResults")

        val electionResultsContainer = electionWrapper.findViewById<LinearLayout>(R.id.electionCandidatesContainer)

        electionResults?.forEach { result ->
            val candidateWrapper = LayoutInflater.from(context)
                .inflate(R.layout.finished_election_candidate_box, electionResultsContainer, false)

            val candidatePercentageView = candidateWrapper.findViewById<TextView>(R.id.finishedElectionCandidatePrecentage)
            val candidateNameView = candidateWrapper.findViewById<TextView>(R.id.finishedElectionCandidateFullNameField)
            val candidatePhotoView = candidateWrapper.findViewById<ImageView>(R.id.finishedElectionCandidateImage)

            // Set the percentage text color based on win/lose status
            val percentage = result["percentage"] as Double
            val roundedPercentage = String.format("%.2f", percentage).toDouble()
            candidatePercentageView.text = "${roundedPercentage}%"

            // Find the corresponding ElectionOption
            val optionId = result["optionId"] as Double
            val option = election.options.find { it.id.toInt().toDouble() == optionId }
            if (option != null) {
                // Set the candidate image
                val candidatePhotoUrl = option.photoUrl
                Picasso.get()
                    .load(candidatePhotoUrl)
                    .into(candidatePhotoView)
                Log.d("MainElectionsFragment", "Loaded candidate photo from URL: $candidatePhotoUrl")

                // Set the candidate name
                val candidateName = "${option.firstName} ${option.lastName}"
                candidateNameView.text = candidateName
            }

            electionResultsContainer.addView(candidateWrapper)
        }
    }
}
