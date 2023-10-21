package online.desidev.onestate.ui.uistate

import online.desidev.onestate.ui.model.Product

data class CartScreenState(
    val isLoading: Boolean = true,
    val items: List<Product> = emptyList()
)