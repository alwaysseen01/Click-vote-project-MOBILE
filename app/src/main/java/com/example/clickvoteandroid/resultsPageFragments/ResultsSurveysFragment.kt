package com.example.clickvoteandroid.resultsPageFragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.clickvoteandroid.R
import com.example.clickvoteandroid.api.SurveyApi
import com.example.clickvoteandroid.dataClasses.Survey
import com.example.clickvoteandroid.dataClasses.SurveyOption
import com.google.android.material.button.MaterialButton
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultsSurveysFragment : Fragment() {

    private lateinit var surveysContainer: LinearLayout

    private val url = "http://10.0.2.2:8081/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_results_surveys, container, false)

        surveysContainer = view.findViewById(R.id.surveysContainer)

        fetchData()

        return view
    }

    private fun fetchData() {
        Log.d("ResultsSurveysFragment", "Starting fetchData")

        // Retrofit
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(SurveyApi::class.java)

        val call = api.getCompletedSurveys() // Assuming endpoint returns a list

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
                        Log.d("ResultsSurveysFragment", "Survey list is null")
                    }
                } else {
                    Log.e("ResultsSurveysFragment", "Error fetching data: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Survey>>, t: Throwable) {
                Log.e("ResultsSurveysFragment", "Error fetching data: $t")
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
                    .inflate(R.layout.finished_survey_box, surveysContainer, false)

                val surveyTitleView = surveyWrapper.findViewById<TextView>(R.id.surveyTitle)
                surveyTitleView.text = survey.title
                Log.d("MainSurveysFragment", "Set survey title to: ${survey.title}")

                val gson = GsonBuilder()
                    .setLenient()
                    .create()


                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

                val api = retrofit.create(SurveyApi::class.java)

                api.getCompletedSurveyWinner(survey.id)?.enqueue(object : Callback<SurveyOption> {
                    override fun onResponse(
                        call: Call<SurveyOption>,
                        response: Response<SurveyOption>
                    ) {
                        if (response.isSuccessful) {
                            val winnerOption = response.body()
                            for (option in survey.options) {
                                Log.d("MainSurveysFragment", "Processing option: ${option.text}")

                                // Создаем новый экземпляр surveyOptionBox
                                val surveyOptionBox = LayoutInflater.from(context)
                                    .inflate(R.layout.finished_survey_option_box, null)

                                // Option text
                                val optionView =
                                    surveyOptionBox.findViewById<MaterialButton>(R.id.surveyOptionButton)
                                val optionText = option.text
                                optionView.text = optionText
                                Log.d("MainSurveysFragment", "Set option text to: $optionText")

                                // Если это опция победителя, изменить цвет фона
                                if (option == winnerOption) {
                                    optionView.setBackgroundColor(Color.GREEN) // Измените на желаемый цвет
                                }

                                val surveyOptionContainer =
                                    surveyWrapper.findViewById<LinearLayout>(R.id.surveyOptionContainer)
                                surveyOptionContainer.addView(surveyOptionBox)
                            }
                        } else {
                            Log.e(
                                "ResultsSurveysFragment",
                                "Error fetching winner: ${response.code()}"
                            )
                        }
                    }

                    override fun onFailure(p0: Call<SurveyOption>, t: Throwable) {
                        Log.e("ResultsSurveysFragment", "Error fetching winner: $t")
                    }
                })

                surveysContainer.addView(surveyWrapper)
            }
        } else {
            Log.d("MainSurveysFragment", "Survey list is null")
        }
    }
}
