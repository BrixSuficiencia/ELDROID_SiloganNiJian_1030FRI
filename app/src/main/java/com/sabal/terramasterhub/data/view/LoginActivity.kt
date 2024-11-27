package com.sabal.terramasterhub.data.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.viewmodel.AuthViewModel
import com.sabal.terramasterhub.databinding.ActivityLoginBinding
import com.sabal.terramasterhub.util.AuthListener

import com.sabal.terramasterhub.util.PrefManager
import com.sabal.terramasterhub.util.hide
import com.sabal.terramasterhub.util.show
import com.sabal.terramasterhub.util.toast
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), AuthListener {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        val viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.authListener = this

        val btnGoToRegister: TextView = findViewById(R.id.btnGoToRegister)

        progressBar = findViewById(R.id.progress_bar)

        btnGoToRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStarted() {
        progressBar.show()
    }

    override fun onSuccess(response: LiveData<String>, isRegister: Boolean) {
        response.observe(this, Observer { res ->
            progressBar.hide()

            if (isRegister) {
                toast("Registration Successful: $res")
            } else {
                val jsonResponse = JSONObject(res)
                val token = jsonResponse.getString("token")
                val user = jsonResponse.getJSONObject("user")
                val userType = user.getString("user_type")
                // Save token and user info to SharedPreferences
                PrefManager.saveToken(this, token)
                PrefManager.saveUserInfo(this, user.toString())
                PrefManager.saveUserType(this, userType)

                toast("Login Successful: $res")

                finish() // Close LoginActivity
            }
        })
    }

    override fun onFailure(message: String, isRegister: Boolean) {
        progressBar.hide()
        if (isRegister) {
            toast("Registration Failed: $message")
        } else {
            toast("Login Failed: $message")
        }
    }
}
