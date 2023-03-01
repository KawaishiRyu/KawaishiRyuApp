package app.kawaishiryu.jiujitsu.view

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.data.model.CurrentUser
import app.kawaishiryu.jiujitsu.databinding.FragmentProfileUserBinding
import app.kawaishiryu.jiujitsu.firebase.cloudfirestore.CloudFileStoreWrapper
import app.kawaishiryu.jiujitsu.presentation.auth.ProfileUserViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.launch


class ProfileUserFragment : Fragment(R.layout.fragment_profile_user) {

    private lateinit var binding: FragmentProfileUserBinding
    private val viewModel: ProfileUserViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileUserBinding.bind(view)

        startFlow()
    }

    private fun startFlow() {
        viewModel.comparationUserDb("3")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profileUserDb.collect() { user ->
                    bindUser(user)
                }

            }
        }
    }

    private fun bindUser(user: CurrentUser) {
        Log.i("contraseña","${user.password}")
        binding.apply {
            tvNameUser.text = user.name
            tvEmailUser.text = user.email
            /*
                  Glide.with(context)
                .load(user.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .circleCrop()
                .into(binding.iVperfil)
             */
            val base64String = user.pictureProfile
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            Glide.with(requireContext()).load(bitmap).diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop().circleCrop().into(ivProfileUserPhoto)

            btnEditProfileUser.setOnClickListener {
                navigateUserEditProfile(user)
            }
        }


    }

    private fun navigateUserEditProfile(user: CurrentUser) {
        Log.i("datos UUID antes","${user.id}")
        //Pasamos los datos a traves con safeArgs
        val directions = ProfileUserFragmentDirections.actionProfileUserFragmentToProfileUserModifiedFragment(user)
        findNavController().navigate(directions)

    }

}