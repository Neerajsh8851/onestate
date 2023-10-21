package online.desidev.onestate.ui.navigation

import online.desidev.onestate.StateAccessor
import online.desidev.onestate.ui.uistate.HomeScreenState

sealed class Screen {

    data class HomeScreen(
        val state: StateAccessor<HomeScreenState>
    ): Screen()
}