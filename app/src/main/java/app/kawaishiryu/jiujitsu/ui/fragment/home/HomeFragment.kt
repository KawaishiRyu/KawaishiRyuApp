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
                imageUrl = "https://i.pinimg.com/564x/d6/e4/8c/d6e48ccad26a1821dbfa2d187aee3ab2.jpg"
                //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/564x/e1/0a/d9/e10ad96b5b0811a064e30b72d77e78af.jpg"
                    //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://i.pinimg.com/564x/d7/10/b5/d710b5e6ff6547ef0b406d17006a9471.jpg"
             //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        binding.carousel.setData(list)
        binding.carousel.infiniteCarousel = true

        binding.textViewDefinition
        // Carga la animación de desvanecimiento desde el archivo XML
        val fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_three)

        // Aplica la animación al CardView
        binding.cardViewPrueba.startAnimation(fadeInAnimation)
    }



}