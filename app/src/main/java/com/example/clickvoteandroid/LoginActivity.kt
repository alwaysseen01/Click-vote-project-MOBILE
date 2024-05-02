package com.example.clickvoteandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clickvoteandroid.api.LoginApi
import com.example.clickvoteandroid.dataClasses.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {
    private var usernameField: EditText? = null
    private var passwordField: EditText? = null
    private var loginUrl = "http://10.0.2.2:8081/auth/login/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameField = findViewById(R.id.usernameField)
        passwordField = findViewById(R.id.passwordField)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameField!!.getText().toString()
            val password = passwordField!!.getText().toString()

            Log.d("LoginActivity", "Username: $username, Password: $password")

            // Example using Retrofit
            val retrofit = Retrofit.Builder()
                .baseUrl(loginUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            Log.d("LoginActivity", "URL: $loginUrl")

            val service =
                retrofit.create(LoginApi::class.java)

            service.login(LoginRequest(username, password))
                .enqueue(object : Callback<LoginResponse?> {
                    override fun onResponse(
                        call: Call<LoginResponse?>,
                        response: Response<LoginResponse?>
                    ) {
                        Log.d("LoginActivity", "Response code: ${response.code()}, Response body: ${response.body()}")

                        if (response.code() == 200) {
                            // Successful login
                            response.body()?.let { storeUserData(it) }
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    MainActivity::class.java
                                )
                            )
                        } else {
                            // Handle login error
                            Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                        // Handle network error
                        Log.d("LoginActivity", "Network error: ${t.message}")
                        Toast.makeText(this@LoginActivity, "Network error", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }

    private fun storeUserData(response: LoginResponse) {
        val preferences = getSharedPreferences("user_data", MODE_PRIVATE)
        preferences.edit()
            .putString("first_name", response.firstName)
            .putString("last_name", response.lastName)
            .putString("username", response.username)
            .apply()
    }

    class LoginResponse {
        val firstName: String? = null
        val lastName: String? = null

        // Getters and setters
        val username: String? = null

    }
}
