package app.kawaishiryu.jiujitsu.ui.fragment.home

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.FragmentHomeBinding
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        // Register lifecycle. For activity this will be lifecycle/getLifecycle() and for fragment it will be viewLifecycleOwner/getViewLifecycleOwner().
        binding.carousel.registerLifecycle(lifecycle)
        val list = mutableListOf<CarouselItem>()

        // Image URL with caption
        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/564x/aa/59/66/aa59662edd4769d4e05cde07a80473c5.jpg"
                //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/564x/90/9e/7b/909e7b85b60eccdf4bb3ca63aa88c7dc.jpg"
                    //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/564x/d8/67/f9/d867f94e8ab32bdd37efc52742671e0b.jpg"
             //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        binding.carousel.setData(list)
        binding.carousel.infiniteCarousel = true

        // Carga la animación de desvanecimiento desde el archivo XML
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_three)

        // Aplica la animación al CardView
        //binding.cardViewPrueba.startAnimation(fadeInAnimation)


        binding.tvMoreJiuJitsu.setOnClickListener {
            toggleExpansion(binding.expansionLayout2)
        }

        binding.tvMoreJudo.setOnClickListener {
            toggleExpansion(binding.expansionLayout)
        }

    }

    private fun toggleExpansion(layout: View) {
        val isExpanding = layout.visibility != View.VISIBLE

        if (isExpanding) {
            binding.tvMoreJiuJitsu.text = "Leer menos..."
            layout.visibility = View.VISIBLE
        } else {
            binding.tvMoreJiuJitsu.text = "Leer mas..."
            layout.visibility = View.GONE
        }
    }

}