package com.example.rickandmortyapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.rickandmortyapp.ui.theme.RickandmortyappTheme

@Composable
fun RickAndMortyApp(){
    RickandmortyappTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController){
            RickAndMortyActions(navController)
        }
        RickAndMortyNavGraph(
            navController = navController,
            navigateToHome = navigationActions.navigateToHome,
            navigateToDetail = navigationActions.navigateToDetail
        )
    }
}