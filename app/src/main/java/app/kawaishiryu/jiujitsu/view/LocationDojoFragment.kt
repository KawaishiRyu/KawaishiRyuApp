package app.kawaishiryu.jiujitsu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentLocationDojoBinding

class LocationDojoFragment : Fragment() {

    private var binding: FragmentLocationDojoBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLocationDojoBinding.bind(view)

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}