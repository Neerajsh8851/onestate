package online.desidev.onestate.ui.uistate

import online.desidev.onestate.ui.model.Product

class HomeScreenState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList()
)