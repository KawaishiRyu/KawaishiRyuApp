package app.kawaishiryu.jiujitsu.ui.dialog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.kawaishiryu.jiujitsu.databinding.ActivitySplashScreenBinding
import app.kawaishiryu.jiujitsu.viewmodel.auth.LoginScreenViewModel
import app.kawaishiryu.jiujitsu.ui.MainActivity
import app.kawaishiryu.jiujitsu.ui.MainMenuHostActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    //Instanciamos la clase del viewModel
    private val viewModel by viewModels<LoginScreenViewModel>()
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000L)
            isUserLoggedIn()
            startFlow()
            finish()
        }
        setContentView(view)
    }


    private fun startFlow() {

        lifecycleScope.launch  {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.prueba.collect(){
                    if(it){
                        //Tiramos el intent
                        val intent = Intent(this@SplashScreenActivity, MainMenuHostActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this@SplashScreenActivity, "Bienvenido de nuevo :)", Toast.LENGTH_SHORT).show()

                    }else{
                        //Le mandamos al login
                        navigationUp()
                    }

                }
            }
        }

    }

    private fun navigationUp() {
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(this@SplashScreenActivity, "Registrate)", Toast.LENGTH_SHORT).show()
    }


    //Verificamos si existe una cuenta
    private fun isUserLoggedIn() {
        //----------------------------------------------------------------------
        viewModel.userLogged()
        //----------------------------------------------------------------------
    }
}

