package app.kawaishiryu.jiujitsu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.kawaishiryu.jiujitsu.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}