package com.laioffer.spotify

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.laioffer.spotify.network.NetworkApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var api: NetworkApi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("lifecycle", "We are at onCreate()")
        setContentView(R.layout.activity_main)

        // findElementByClass/Id
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_graph)

        NavigationUI.setupWithNavController(navView, navController)

        // https://stackoverflow.com/questions/70703505/navigationui-not-working-correctly-with-bottom-navigation-view-implementation
        navView.setOnItemSelectedListener{
            NavigationUI.onNavDestinationSelected(it, navController)
            navController.popBackStack(it.itemId, inclusive = false)
            true
        }

        GlobalScope.launch(Dispatchers.IO) {
            // val retrofit = NetworkModule.provideRetrofit()
            // val api = retrofit.create(NetworkApi::class.java)

            val response = api.getHomeFeed().execute().body()
            Log.d("NetworkApi", response.toString())
        }
    }
}