package online.desidev.onestate

import kotlin.reflect.KClass

// Wrapper for a state.
class StateAccessor<T : Any>(
    private var stateHolder: StateManager.StateHolder<T>?,
    private val manager: StateManager,
    private val stateKey: Pair<KClass<*>, Any>
) {
    val state
        get() = stateHolder?.state
            ?: throw IllegalStateException("You cannot access a state from a disposed StateAccessor")

    // Sends an update to the state.
    fun send(update: (T) -> T) {
        manager.update(stateKey, update)
    }

    // Registers an observer of the state.
    @Suppress("UNCHECKED_CAST")
    fun observe(observer: (T) -> Unit) {
        manager.addObserver(stateKey) { observer(it as T) }
    }

    // Disposes of the state, releasing it from the manager.
    fun dispose() {
        stateHolder = null
        manager.releaseState(stateKey)
    }
}