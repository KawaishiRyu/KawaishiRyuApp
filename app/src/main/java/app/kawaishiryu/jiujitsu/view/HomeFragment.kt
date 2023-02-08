package app.kawaishiryu.jiujitsu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        Toast.makeText(context,"Estoy aqui",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}