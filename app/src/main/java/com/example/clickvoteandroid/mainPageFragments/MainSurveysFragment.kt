package com.example.clickvoteandroid.mainPageFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.clickvoteandroid.R
import com.example.clickvoteandroid.dataClasses.Survey
import com.example.clickvoteandroid.api.SurveyApi
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainSurveysFragment : Fragment() {

    private lateinit var surveysContainer: LinearLayout

    private val url = "http://172.20.10.6:8081/surveys/active/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_surveys, container, false)

        surveysContainer = view.findViewById(R.id.surveysContainer)

        fetchData()

        return view
    }

    private fun fetchData() {
        Log.d("MainSurveysFragment", "Starting fetchData")

        // Retrofit
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(SurveyApi::class.java)

        val call = api.getActiveSurveys() // Assuming endpoint returns a list

        call.enqueue(object : Callback<List<Survey>> {
            override fun onResponse(call: Call<List<Survey>>, response: Response<List<Survey>>) {
                if (response.isSuccessful) {
                    Log.d("MainSurveysFragment", "Response is successful")
                    val surveyList = response.body()
                    if (surveyList != null) {
                        Log.d("MainSurveysFragment",
                            "Survey list is not null, updating survey info: $surveyList"
                        )
                        updateSurveyInfo(surveyList)
                    } else {
                        Log.d("MainSurveysFragment", "Survey list is null")
                    }
                } else {
                    Log.e("MainSurveysFragment", "Error fetching data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Survey>>, t: Throwable) {
                Log.e("MainSurveysFragment", "Error fetching data: $t")
            }
        })
    }


    private fun updateSurveyInfo(surveyList: List<Survey>?) {
        Log.d("MainSurveysFragment", "Starting updateSurveyInfo")

        // Очистить контейнер перед добавлением
        surveysContainer.removeAllViews()
        Log.d("MainSurveysFragment", "Cleared surveysContainer")

        if (surveyList != null) {
            Log.d("MainSurveysFragment", "Survey list is not null")
            for (survey in surveyList) {
                Log.d("MainSurveysFragment", "Processing survey: ${survey.title}")

                val surveyWrapper = LayoutInflater.from(context)
                    .inflate(R.layout.survey_box, surveysContainer, false)

                val surveyTitleView = surveyWrapper.findViewById<TextView>(R.id.surveyTitle)
                surveyTitleView.text = survey.title
                Log.d("MainSurveysFragment", "Set survey title to: ${survey.title}")

                for (option in survey.options) {
                    Log.d("MainSurveysFragment", "Processing option: ${option.text}")

                    // Создаем новый экземпляр surveyOptionBox
                    val surveyOptionBox = LayoutInflater.from(context)
                        .inflate(R.layout.survey_option_box, null)

                    // Option text
                    val optionView = surveyOptionBox.findViewById<TextView>(R.id.surveyOptionButton)
                    val optionText = option.text
                    optionView.text = optionText
                    Log.d("MainSurveysFragment", "Set option text to: $optionText")

                    val surveyOptionContainer = surveyWrapper.findViewById<LinearLayout>(R.id.surveyOptionContainer)
                    surveyOptionContainer.addView(surveyOptionBox)
                }

                surveysContainer.addView(surveyWrapper)
            }
        } else {
            Log.d("MainSurveysFragment", "Survey list is null")
        }
    }
}
