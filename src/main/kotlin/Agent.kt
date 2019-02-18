import org.slf4j.LoggerFactory

const val NAME = "MegaMan"
const val ROOM = "418730"
const val WS_URI = "ws://localhost:8443/agent"

fun main() {

    AgentClient(
            wsUri = WS_URI,
            room = ROOM,
            name = NAME,
            strategy = ::myStrategy)

}

val logger = LoggerFactory.getLogger("$NAME-$ROOM")

fun myStrategy(roll : DiceRoll) : Placement {
    logger.info("Current die shows ${roll.value}")
    //TODO save me, I will currently attempt an illegal placement after 3 moves
    return Placement.HUNDREDS
}