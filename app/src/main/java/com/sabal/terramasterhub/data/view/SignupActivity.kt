package com.sabal.terramasterhub.data.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.viewmodel.AuthViewModel
import com.sabal.terramasterhub.databinding.ActivitySignupBinding
import com.sabal.terramasterhub.util.AuthListener
import com.sabal.terramasterhub.util.hide
import com.sabal.terramasterhub.util.show
import com.sabal.terramasterhub.util.toast

class SignupActivity : AppCompatActivity(), AuthListener {

    private lateinit var progressBar: ProgressBar
    private lateinit var userTypeSpinner: Spinner
    private lateinit var certificationIdEditText: EditText
    private lateinit var licenseNumberEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up data binding
        val binding: ActivitySignupBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_signup)
        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding.viewmodel = viewModel

        // Set the AuthListener for ViewModel
        viewModel.authListener = this

        // Initialize UI components
        progressBar = findViewById(R.id.progress_bar)
        userTypeSpinner = findViewById(R.id.et_user_type)
        certificationIdEditText = findViewById(R.id.et_certification_id)
        licenseNumberEditText = findViewById(R.id.et_license_number)

        // Initialize user types
        val userTypes = arrayOf("Finder", "expert", "surveyor")

        // Set up the Spinner with an ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, userTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userTypeSpinner.adapter = adapter

        // Set an ItemSelectedListener on the Spinner
        userTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedUserType = userTypes[position]
                viewModel.user_type = selectedUserType

                // Show or hide EditTexts based on the selected user type
                if (selectedUserType == "Finder") {
                    certificationIdEditText.visibility = View.GONE
                    licenseNumberEditText.visibility = View.GONE
                } else {
                    certificationIdEditText.visibility = View.VISIBLE
                    licenseNumberEditText.visibility = View.VISIBLE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Optional: handle the case when no item is selected
            }
        }
    }

    override fun onStarted() {
        progressBar.show()
    }

    override fun onSuccess(response: LiveData<String>, isRegister: Boolean) {
        response.observe(this, Observer { message ->
            progressBar.hide()
            if (isRegister) {
                toast("Registration Successful: $message")
                finish()
            }
        })
    }

    override fun onFailure(message: String, isRegister: Boolean) {
        progressBar.hide()
        if (isRegister) {
            toast("Registration Failed: $message")
        }
    }
}
