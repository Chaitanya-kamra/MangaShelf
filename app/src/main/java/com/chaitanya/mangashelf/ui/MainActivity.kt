package com.chaitanya.mangashelf.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chaitanya.mangashelf.ui.theme.MangaShelfTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalSharedTransitionApi::class)
class MainActivity : ComponentActivity() {

    val viewModel: MangaViewModel by viewModels()

    enum class Screen{
        MangaList,
        MangaDetail,
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangaShelfTheme {
                val navController = rememberNavController()
                val uiState by viewModel.uiState.collectAsState()
                SharedTransitionLayout {
                    NavHost(navController = navController, startDestination = Screen.MangaList.name) {

                        composable(Screen.MangaList.name) {
                            MangaListScreen(
                                uiState = uiState,
                                onFavoriteToggle = viewModel::toggleFavorite,
                                updateSelectionOnScroll = viewModel::updateSelectedOnScroll,
                                onSelectYear = viewModel::selectYear,
                                onMangaSelected = { manga->
                                    viewModel.updateSelectedManga(manga)
                                    navController.navigate("${Screen.MangaDetail.name}/${manga.id}")
                                },
                                animatedVisibilityScope = this,
                                onRefresh = viewModel::fetchMangas,
                                onSortSelected = viewModel::updateSortType
                            )
                        }

                        composable(
                            route = "${Screen.MangaDetail.name}/{mangaId}",
                            arguments = listOf(navArgument("mangaId"){
                                type = NavType.StringType
                            })
                        ) {
                            val mangaId = it.arguments?.getString("mangaId")
                            MangaDetailScreen(
                                mangaId = mangaId,
                                uiState = uiState,
                                onBack = {
                                    navController.popBackStack()
                                },
                                onFavoriteToggle = viewModel::toggleFavoritefromDetail,
                                animatedVisibilityScope = this,
                                onMarksAsRead = viewModel::markAsRead,
                                onSimilarSelected = { manga->
                                    viewModel.updateSelectedManga(manga)
                                    navController.navigate(Screen.MangaDetail.name+"/${manga.id}")
                                }
                            )
                        }
                    }
                }

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MangaShelfTheme {
    }
}