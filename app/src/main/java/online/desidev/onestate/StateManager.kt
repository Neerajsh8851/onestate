package online.desidev.onestate

import kotlin.reflect.KClass

class StateManager {
    // A map for storing factories for creating initial states.
    private val stateFactories = mutableMapOf<Any, () -> Any>()

    // A map of currently existing states.
    private val states = mutableMapOf<Any, StateHolder<*>>()

    // A map for keeping track of how many wrappers are using a state.
    private val usageCounts = mutableMapOf<Pair<KClass<*>, Any>, Int>()

    // A map of sets of observers, keyed by state.
    private val observers = mutableMapOf<Pair<KClass<*>, Any>, MutableSet<(Any) -> Unit>>()


    class StateHolder<T: Any>(
        var state: T,
    )

    inner class Configure {
        // Registers a state factory.
        fun <T : Any> stateFactory(type: KClass<T>, factory: () -> T) {
            stateFactories[type] = factory as () -> Any
        }
    }

    // Configures state factories.
    fun configure(block: Configure.() -> Unit) {
        Configure().block()
    }


    // Retrieves or creates a state.
    @Synchronized
    fun <T : Any> getState(type: KClass<T>, key: Any = Unit): StateAccessor<T> {
        val stateKey = Pair(type, key)

        // Get or create state.
        val holder = states.getOrPut(stateKey) {
            stateFactories[stateKey.first]?.invoke()?.run {
                usageCounts[stateKey] = 1
                StateHolder(this)
            } ?: throw IllegalStateException("No factory provided for this state type: $type")
        }

        if (!type.isInstance(holder.state)) throw IllegalArgumentException("Invalid state type")

        @Suppress("UNCHECKED_CAST")
        return StateAccessor(holder as StateHolder<T>, this, stateKey)
    }

    // Releases a state, decrementing its usage count and possibly removing it.
    @Synchronized
    fun releaseState(stateKey: Pair<KClass<*>, Any>) {
        val currentCount = usageCounts[stateKey] ?: return // If null, no-op.
        if (currentCount == 1) {
            // If this was the last reference, remove the state and observers.
            states.remove(stateKey)
            observers.remove(stateKey)
            usageCounts.remove(stateKey)
        } else {
            usageCounts[stateKey] = currentCount - 1
        }
    }

    // Adds an observer to a state.
    @Synchronized
    internal fun addObserver(stateKey: Pair<KClass<*>, Any>, observer: (Any) -> Unit) {
        observers.computeIfAbsent(stateKey) { mutableSetOf() }.add(observer)
    }

    // Notifies observers of a state change.
    @Synchronized
    private fun notifyStateChange(stateKey: Any, newState: Any) {
        observers[stateKey]?.forEach { it(newState) }
    }


    @Suppress("UNCHECKED_CAST")
    internal fun <T : Any> update(stateKey: Any, update: (T) -> T) {
        (states[stateKey] as? StateHolder<T>)?.apply {
            state = update(state)
            notifyStateChange(stateKey, state)
        }
    }
}
