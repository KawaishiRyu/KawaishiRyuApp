package app.kawaishiryu.jiujitsu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import app.kawaishiryu.jiujitsu.R
import app.kawaishiryu.jiujitsu.databinding.ActivityMainMenuHostBinding

class MainMenuHostActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainMenuHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.menuJiuJitsuFragment,
                R.id.menuJudoFragment,
                R.id.locationFragment,
                R.id.profileUserFragment,
                R.id.dialogSignOutUser
            ), binding.drawer
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        // Aplicar el selector de color a los elementos de menú en el NavigationView
        val navView = binding.navView
        val navMenu = navView.menu
        val navMenuItemColors = ContextCompat.getColorStateList(this, R.color.colorSelected)


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}