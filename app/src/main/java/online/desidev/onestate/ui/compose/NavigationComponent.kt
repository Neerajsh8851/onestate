package online.desidev.onestate.ui.compose

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import online.desidev.onestate.StateAccessor
import online.desidev.onestate.ui.navigation.NavigationState
import online.desidev.onestate.ui.navigation.Screen

@Composable
fun RootNavigationComponent(
    navigationState: StateAccessor<NavigationState>
) {
    val currentScreen = navigationState.state.current

    Crossfade(currentScreen, label = "") { screen ->
        when (screen) {
            is Screen.HomeScreen -> {

            }
        }
    }
}