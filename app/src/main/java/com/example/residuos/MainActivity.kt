package com.example.residuos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.residuos.databinding.ActivityMainBinding
import com.example.residuos.localdata.database.AppDatabase
import com.example.residuos.localdata.entity.User
import com.example.residuos.rankings.RankingsRepository
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        lifecycleScope.launch {

            // Insertar usuario
            userDao.insert(User(name = "Hernán"))

            // Leer usuarios
            val users = userDao.getAll()

            // Mostrar en Log
            Log.d("ROOM_TEST", users.toString())

            val result = RankingsRepository(this@MainActivity).getGlobalRanking()

            result.onSuccess {
                Log.d("RANKING", it.toString())
            }.onFailure {
                Log.e("RANKING", it.message ?: "Error")
            }
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        // Esconder la barra de navegacion si el usuario esta parado en el login o en el register
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment,
                R.id.signupFragment -> {
                    binding.bottomNav.visibility = View.GONE
                }
                else -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
            }
        }
    }
}