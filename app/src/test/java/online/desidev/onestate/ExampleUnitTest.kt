package online.desidev.onestate

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    // A specific state class
    data class Counter(var count: Int)


    @Test fun incrementTest() {
        val stateManager = StateManager().apply {
            configure {
                stateFactory(type = Counter::class) { Counter(0) }
                // ... any other state factories.
            }
        }

        val counterState: StateAccessor<Counter> = stateManager.getState(type = Counter::class, key = "mainCounter")

        // Observe state changes.
        counterState.observe { newValue ->
            println("Counter changed: ${newValue.count}")
        }

        // Update state.
        counterState.send {
            it.count++
            it
        }

        // Dispose when done with it.
        counterState.dispose()
    }
}