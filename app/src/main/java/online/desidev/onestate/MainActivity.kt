package online.desidev.onestate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import online.desidev.onestate.ui.compose.RootNavigationComponent
import online.desidev.onestate.ui.navigation.NavigationState
import online.desidev.onestate.ui.navigation.Screen
import online.desidev.onestate.ui.theme.OnestateTheme
import online.desidev.onestate.ui.uistate.CartScreenState
import online.desidev.onestate.ui.uistate.HomeScreenState
import java.util.Stack

val stateManager = StateManager()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureStateManager()

        setContent {
            OnestateTheme {
                RootNavigationComponent(navigationState = stateManager.getState(NavigationState::class))
            }
        }
    }


    private fun configureStateManager() {
        stateManager.configure {
            stateFactory(HomeScreenState::class) {
                HomeScreenState()
            }

            stateFactory(CartScreenState::class) {
                CartScreenState()
            }

            stateFactory(NavigationState::class) {
                val homeScreenState = stateManager.getState(HomeScreenState::class)
                NavigationState(
                    current = Screen.HomeScreen(homeScreenState),
                    stashed = Stack()
                )
            }
        }
    }
}
