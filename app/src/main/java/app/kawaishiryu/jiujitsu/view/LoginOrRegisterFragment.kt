package app.kawaishiryu.jiujitsu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.findNavController
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentLoginOrRegisterBinding


class LoginOrRegisterFragment : Fragment(R.layout.fragment_login_or_register) {
    private lateinit var binding: FragmentLoginOrRegisterBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginOrRegisterBinding.bind(view)

        binding.btnRegister.setOnClickListener {
            navigationRegister()
        }
        binding.btnLogin.setOnClickListener {
            navigationSignIn()
        }
    }

    private fun navigationSignIn() {
        requireView().findNavController().navigate(R.id.action_loginOrRegisterFragment_to_loginFragment)
    }

    private fun navigationRegister() {
        requireView().findNavController().navigate(R.id.action_loginOrRegisterFragment_to_registerFragment)
    }
}