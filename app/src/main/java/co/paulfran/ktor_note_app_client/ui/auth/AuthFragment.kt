package co.paulfran.ktor_note_app_client.ui.auth

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import co.paulfran.ktor_note_app_client.R
import co.paulfran.ktor_note_app_client.data.remote.BasicAuthInterceptor
import co.paulfran.ktor_note_app_client.other.Constants.KEY_LOGGED_IN_EMAIL
import co.paulfran.ktor_note_app_client.other.Constants.KEY_LOGGED_IN_PASSWORD
import co.paulfran.ktor_note_app_client.other.Constants.NO_EMAIL
import co.paulfran.ktor_note_app_client.other.Constants.NO_PASSWORD
import co.paulfran.ktor_note_app_client.other.Status
import co.paulfran.ktor_note_app_client.ui.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_auth.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment(R.layout.fragment_auth){
    // get reference to the view model
    private val viewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var currentEmail: String? = null
    private var currentPassword: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isLoggedIn()) {
            authenticateApi(currentEmail ?: "", currentPassword ?: "")
            redirectLogin()
        }

        // disable landscape mode
        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
        subscribeTpObservers()
        btnRegister.setOnClickListener {
            val email = etRegisterEmail.text.toString()
            val password = etRegisterPassword.text.toString()
            val confirmPassword = etRegisterPasswordConfirm.text.toString()
            viewModel.register(email, password, confirmPassword)
        }

        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()

            currentEmail = email
            currentPassword = password

            viewModel.login(email, password)
        }

    }

    private fun isLoggedIn(): Boolean {
        currentEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        currentPassword = sharedPref.getString(KEY_LOGGED_IN_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD
        return currentEmail != NO_EMAIL && currentPassword != NO_PASSWORD
    }

    private fun authenticateApi(email: String, password: String) {
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }

    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.authFragment, true)
            .build()
        findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment(), navOptions)
    }

    private fun subscribeTpObservers() {
        viewModel.registerStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when(result.status) {
                    Status.SUCCESS -> {
                        registerProgressBar.visibility = View.GONE
                        showSnackBar(result.data ?: "Successfully registered an account")
                    }
                    Status.ERROR -> {
                        registerProgressBar.visibility = View.GONE
                        showSnackBar(result.message ?: "Unknown error occurred")
                    }
                    Status.LOADING -> {
                        registerProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS -> {
                        loginProgressBar.visibility = View.GONE
                        showSnackBar(result.data ?: "Successfully registered an account")
                        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, currentEmail).apply()
                        sharedPref.edit().putString(KEY_LOGGED_IN_PASSWORD, currentPassword).apply()
                        authenticateApi(currentEmail ?: "", currentPassword ?: "")
                        redirectLogin()
                    }
                    Status.ERROR -> {
                        loginProgressBar.visibility = View.GONE
                        showSnackBar(result.message ?: "Unknown error occurred")
                    }
                    Status.LOADING -> {
                        loginProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}