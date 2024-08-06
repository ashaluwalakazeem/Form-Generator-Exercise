package com.milsat.capstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.milsat.capstone.ui.screens.form.FormScreen
import com.milsat.capstone.ui.screens.home.HomeScreen
import com.milsat.capstone.ui.theme.CapstoneTheme
import com.milsat.capstone.utils.ArgumentBundleKeys
import com.milsat.capstone.utils.Screens

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CapstoneTheme {
                RootNavigation()
            }
        }
    }
}

@Composable
fun RootNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Home()) {
        composable(
            route = Screens.Home(),
        ) {
            HomeScreen(
                onNavigateToRoute = { route: String ->
                    navController.navigate(route){
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = Screens.Form(),
            arguments = listOf(
                navArgument(
                    name = ArgumentBundleKeys.Form.FORM_ID,
                    builder = {
                        type = NavType.StringType
                    }
                ),
                navArgument(
                    name = ArgumentBundleKeys.Form.FORM_TYPE,
                    builder = {
                        type = NavType.StringType
                    }
                ),
            )
        ) { backStackEntry ->
            val formName = backStackEntry.arguments?.getString(ArgumentBundleKeys.Form.FORM_TYPE) ?: return@composable
            FormScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                formTitle = formName
            )
        }
    }
}