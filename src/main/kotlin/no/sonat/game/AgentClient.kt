package no.sonat.game

import com.neovisionaries.ws.client.*
import kotlinx.serialization.json.JSON
import org.slf4j.LoggerFactory

enum class Placement {ONES,TENS,HUNDREDS}

/**
 * TODO add ping pong message to keep alive if long in start mode
 */
class AgentClient(
        val wsUri : String,
        val room : String,
        val name : String,
        val strategy : (roll : DiceRoll) -> Placement,
        val joinAction : (JoinResponse) -> Unit = {}) {

    val logger = LoggerFactory.getLogger("Agent")

    val agent = WebSocketFactory()
            .createSocket(wsUri)
            .addListener(object : WebSocketAdapter() {

                override fun onConnected(websocket: WebSocket, headers: MutableMap<String, MutableList<String>>) {
                    val msg = PlayerMessage(JoinRoomAction(room,name))
                    websocket.sendText(JSON.stringify(PlayerMessage.serializer(),msg))
                }

                override fun onTextMessage(websocket: WebSocket, text: String) {
                    val deserialized = JSON.parse(PlayerResponseMessage.serializer(),text)

                    val response = deserialized.response;
                    when(response) {
                        is JoinResponse -> {
                            logger.info("$response")
                            joinAction(response)
                        }
                        is DiceRoll -> {
                            val placement = strategy(response)

                            val placementMessage = JSON.stringify(
                                    PlayerMessage.serializer(),
                                    PlayerMessage(PlacementAction(room,name,placement))
                            )

                            websocket.sendText(placementMessage)
                        }
                    }

                }

                override fun onError(websocket: WebSocket, cause: WebSocketException) {
                    logger.error("Websocket failure",cause)
                }

                override fun onConnectError(websocket: WebSocket, exception: WebSocketException?) {
                    logger.error("Websocket failure",exception)
                }
            })
            .connect()

}
