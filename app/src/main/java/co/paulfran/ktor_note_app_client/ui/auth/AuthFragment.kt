package co.paulfran.ktor_note_app_client.ui.auth

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import co.paulfran.ktor_note_app_client.R
import co.paulfran.ktor_note_app_client.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : BaseFragment(R.layout.fragment_auth){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener {
            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToNotesFragment())
        }
    }
}