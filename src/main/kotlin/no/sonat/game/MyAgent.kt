package no.sonat.game

import org.slf4j.LoggerFactory

const val HOST = "ubuntu@ec2-13-53-130-144.eu-north-1.compute.amazonaws.com"
const val NAME = "MegaMan"
const val ROOM = "Sonat"
const val WS_URI = "ws://$HOST:8443/test"

fun main() {

    val strategy = Strategy()

    AgentClient(
            wsUri = WS_URI,
            room = ROOM,
            name = NAME,
            strategy = { it -> strategy.handleRoll(it) })
}

val logger = LoggerFactory.getLogger("$NAME-$ROOM")


class GameState {
    var hundreds : Int = 0
    var tens : Int = 0
    var ones : Int = 0

    var sum : Int = 0

    fun addHundred(number : Int) {
        hundreds++
        sum += (number * 100)
    }

    fun addTens(number : Int) {
        tens++
        sum += (number * 10)
    }

    fun addOnes(number : Int) {
        ones++
        sum += number
    }
}

class Strategy {

    val state : GameState = GameState()

    fun handleRoll(roll : DiceRoll) : Placement {
        logger.info("Current die shows ${roll.value}")

        if(state.hundreds < 3) {
            state.addHundred(roll.value)
            return Placement.HUNDREDS
        }

        if(state.tens < 3) {
            state.addTens(roll.value)
            return Placement.TENS
        }

        state.addOnes(roll.value)
        return Placement.ONES

    }
}