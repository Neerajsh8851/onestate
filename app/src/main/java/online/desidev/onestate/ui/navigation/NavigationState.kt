package online.desidev.onestate.ui.navigation

import java.util.Stack

data class NavigationState(
    val current: Screen,
    val stashed: Stack<Screen>,
)