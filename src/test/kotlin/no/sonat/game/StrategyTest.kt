package no.sonat.game
import org.junit.Test

class StrategyTest {

    @Test
    fun testGame_9_rolls() {

        val strategy = Strategy()
        for (i in 1..9) {
            strategy.handleRoll(DiceRoll(1))
        }

        assert(strategy.state.hundreds == 3)

    }



}