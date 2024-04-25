package com.example.clickvoteandroid

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.clickvoteandroid.mainPageFragments.MainElectionsFragment
import com.example.clickvoteandroid.mainPageFragments.MainPetitionsFragment
import com.example.clickvoteandroid.mainPageFragments.MainSurveysFragment
import com.example.clickvoteandroid.resultsPageFragments.ResultsElectionsFragment
import com.example.clickvoteandroid.resultsPageFragments.ResultsPetitionsFragment
import com.example.clickvoteandroid.resultsPageFragments.ResultsSurveysFragment
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var mainToolbar: Toolbar
    private lateinit var secondaryToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainToolbar = findViewById(R.id.mainToolbar)
        secondaryToolbar = findViewById(R.id.secondaryToolbar)

        val mainPageButton = findViewById<MaterialButton>(R.id.mainPageButton)
        val resultsPageButton = findViewById<MaterialButton>(R.id.resultsPageButton)
        val aboutUsPageButton = findViewById<MaterialButton>(R.id.aboutUsPageButton)
        val profilePageButton = findViewById<MaterialButton>(R.id.profilePageButton)

        val electionsSectionButton = findViewById<MaterialButton>(R.id.electionsSectionButton)
        val petitionsSectionButton = findViewById<MaterialButton>(R.id.petitionsSectionButton)
        val surveysSectionButton = findViewById<MaterialButton>(R.id.surveysSectionButton)

        updatePageButtonState(mainPageButton, true)
        updatePageButtonState(resultsPageButton, false)

        replaceFragment(MainElectionsFragment())

        updateSectionButtonState(electionsSectionButton, true)
        updateSectionButtonState(surveysSectionButton, false)
        updateSectionButtonState(petitionsSectionButton, false)

        mainPageButton.setOnClickListener {
            updatePageButtonState(mainPageButton, true)
            updatePageButtonState(resultsPageButton, false)
            updatePageButtonState(aboutUsPageButton, false)
            updatePageButtonState(profilePageButton, false)

            secondaryToolbar.visibility = View.VISIBLE

            if (electionsSectionButton.isSelected) {
                replaceFragment(MainElectionsFragment())
            } else if (petitionsSectionButton.isSelected) {
                replaceFragment(MainPetitionsFragment())
            } else if (surveysSectionButton.isSelected) {
                replaceFragment(MainSurveysFragment())
            }
        }

        resultsPageButton.setOnClickListener {
            updatePageButtonState(resultsPageButton, true)
            updatePageButtonState(mainPageButton, false)
            updatePageButtonState(aboutUsPageButton, false)
            updatePageButtonState(profilePageButton, false)

            secondaryToolbar.visibility = View.VISIBLE

            if (electionsSectionButton.isSelected) {
                replaceFragment(ResultsElectionsFragment())
            } else if (petitionsSectionButton.isSelected) {
                replaceFragment(ResultsPetitionsFragment())
            } else if (surveysSectionButton.isSelected) {
                replaceFragment(ResultsSurveysFragment())
            }
        }

        aboutUsPageButton.setOnClickListener {
            updatePageButtonState(aboutUsPageButton, true)
            updatePageButtonState(mainPageButton, false)
            updatePageButtonState(resultsPageButton, false)
            updatePageButtonState(profilePageButton, false)

            replaceFragment(AboutUsFragment())
            secondaryToolbar.visibility = View.GONE
        }

        profilePageButton.setOnClickListener {
            updatePageButtonState(profilePageButton, true)
            updatePageButtonState(mainPageButton, false)
            updatePageButtonState(resultsPageButton, false)
            updatePageButtonState(aboutUsPageButton, false)

            replaceFragment(ProfileFragment())
            secondaryToolbar.visibility = View.GONE
        }

        electionsSectionButton.setOnClickListener {
            updateSectionButtonState(electionsSectionButton, true)
            updateSectionButtonState(surveysSectionButton, false)
            updateSectionButtonState(petitionsSectionButton, false)

            if (isMainPageSelected()) {
                replaceFragment(MainElectionsFragment())
            } else if (isResultsPageSelected()) {
                replaceFragment(ResultsElectionsFragment())
            }
        }

        petitionsSectionButton.setOnClickListener {
            updateSectionButtonState(petitionsSectionButton, true)
            updateSectionButtonState(surveysSectionButton, false)
            updateSectionButtonState(electionsSectionButton, false)

            if (isMainPageSelected()) {
                replaceFragment(MainPetitionsFragment())
            } else if (isResultsPageSelected()) {
                replaceFragment(ResultsPetitionsFragment())
            }
        }

        surveysSectionButton.setOnClickListener {
            updateSectionButtonState(surveysSectionButton, true)
            updateSectionButtonState(petitionsSectionButton, false)
            updateSectionButtonState(electionsSectionButton, false)

            if (isMainPageSelected()) {
                replaceFragment(MainSurveysFragment())
            } else if (isResultsPageSelected()) {
                replaceFragment(ResultsSurveysFragment())
            }
        }
    }

    private fun isMainPageSelected(): Boolean {
        val mainPageButton = findViewById<MaterialButton>(R.id.mainPageButton)
        return mainPageButton.isSelected
    }

    private fun isResultsPageSelected(): Boolean {
        val resultsPageButton = findViewById<MaterialButton>(R.id.resultsPageButton)
        return resultsPageButton.isSelected
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun updateSectionButtonState(button: MaterialButton, isSelected: Boolean) {
        button.backgroundTintList = if (isSelected) {
            ColorStateList.valueOf(Color.parseColor("#0077B6"))
        } else {
            ColorStateList.valueOf(Color.WHITE)
        }

        button.setTextColor(if (isSelected) Color.WHITE else Color.BLACK)
        button.isSelected = isSelected
    }

    private fun updatePageButtonState(button: MaterialButton, isSelected: Boolean) {
        if (isSelected) {
            button.strokeWidth = 7
            button.strokeColor = ColorStateList.valueOf(Color.BLACK)
        } else {
            button.strokeWidth = 0
        }
        button.isSelected = isSelected
    }
}




