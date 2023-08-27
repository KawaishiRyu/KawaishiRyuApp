package app.kawaishiryu.jiujitsu.ui.fragment.home

import android.os.Bundle
import android.view.View
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

        // Kotlin
        binding.carousel

// Register lifecycle. For activity this will be lifecycle/getLifecycle() and for fragment it will be viewLifecycleOwner/getViewLifecycleOwner().
        binding.carousel.registerLifecycle(lifecycle)

        val list = mutableListOf<CarouselItem>()

// Image URL with caption
        list.add(
            CarouselItem(
                imageUrl = "https://scontent.ftuc4-1.fna.fbcdn.net/v/t1.6435-9/109857163_1121906171514310_967881985443633530_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=e3f864&_nc_ohc=IrB97kYugLUAX_xqOFC&_nc_ht=scontent.ftuc4-1.fna&oh=00_AfCixvaO6q18PSIknfV4K0RC_tbQJZN9h3wQRDsZ9H3L0Q&oe=64D1ED5B"
                //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://scontent.ftuc4-2.fna.fbcdn.net/v/t39.30808-6/302166557_510331681093056_2421901716059370977_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=09cbfe&_nc_ohc=5eSZCcXm8RsAX82MEmo&_nc_ht=scontent.ftuc4-2.fna&oh=00_AfBmIHHTyZBUDa2H2_0EOJlMkYDDI64JjMOCf0lV8UPLlQ&oe=64AF2E49"
                    //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        list.add(
            CarouselItem(
                imageUrl = "https://scontent.ftuc4-1.fna.fbcdn.net/v/t1.6435-9/98351495_1074571476247780_381709931327258624_n.jpg?_nc_cat=101&ccb=1-7&_nc_sid=e3f864&_nc_ohc=vWEBbYCJS60AX8vTpKQ&_nc_ht=scontent.ftuc4-1.fna&oh=00_AfBJ4brgBjkC8YXN90sYNVlmNKYXxrBf3Iioq423WvBlug&oe=64D1D995"
                //caption = "Photo by Aaron Wu on Unsplash"
            )
        )

        binding.carousel.setData(list)
        binding.carousel.infiniteCarousel = true
    }

}