package io.github.skeletonxf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.skeletonxf.ui.screens.InformationScreen
import io.github.skeletonxf.ui.screens.MainContentScreen
import io.github.skeletonxf.ui.screens.Screen
import io.github.skeletonxf.ui.strings.LocalStrings
import io.github.skeletonxf.ui.theme.CRCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val viewModel: MainActivityViewModel by viewModels()
        setContent {
            CRCalculatorTheme {
                val navController = rememberNavController()
                val state by viewModel.state
                val strings = LocalStrings.current

                NavHost(navController = navController, startDestination = Screen.MainContent) {
                    composable<Screen.MainContent> {
                        SideEffect {
                            window.setTitle(strings.calculator.title)
                        }
                        MainContentScreen(
                            state = state,
                            onSetPlayerQuantity = viewModel::setPlayerQuantity,
                            onSetPlayerLevel = viewModel::setPlayerLevel,
                            onRemovePlayerRow = viewModel::removePlayerRow,
                            onAddPlayerRow = viewModel::addPlayerRow,
                            onSetMonsterQuantity = viewModel::setMonsterQuantity,
                            onSetMonsterChallengeRating = viewModel::setMonsterChallengeRating,
                            onRemoveMonsterRow = viewModel::removeMonsterRow,
                            onAddMonsterRow = viewModel::addMonsterRow,
                            onInfo = { navController.navigate(Screen.Information) }
                        )
                    }
                    composable<Screen.Information>(
                        enterTransition = {
                            slideInVertically { it }
                        },
                        exitTransition = {
                            slideOutVertically { it }
                        },
                    ) {
                        SideEffect {
                            window.setTitle(strings.information.title)
                        }
                        InformationScreen(
                            onClose = { navController.popBackStack() }
                        )
                    }
                }

                val darkScrim = MaterialTheme.colorScheme.primary
                SideEffect {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(darkScrim.toArgb()),
                        navigationBarStyle = SystemBarStyle.light(
                            Color.Transparent.toArgb(),
                            Color.Transparent.toArgb(),
                        )
                    )
                }
            }
        }
    }
}
