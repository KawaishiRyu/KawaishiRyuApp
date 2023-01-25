package app.kawaishiryu.jiujitsu

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.kawaishiryu.jiujitsu.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

        binding.btnNavegar.setOnClickListener {

            //Largamos lo actividad
            /*val intent = Intent(requireContext(),MainMenuHostActivity::class.java)
            startActivity(intent)*/
            val intent = Intent(requireContext(), MainMenuHostActivity::class.java)
            startActivity(intent)

        }
        return binding.root
    }

}