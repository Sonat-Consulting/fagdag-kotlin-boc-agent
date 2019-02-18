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
            .setPingInterval(60*1000)
            .setPingPayloadGenerator { "PING".toByteArray() }
            .addListener(object : WebSocketAdapter() {

                override fun onConnected(websocket: WebSocket, headers: MutableMap<String, MutableList<String>>) {
                    val msg = PlayerMessage(JoinRoomAction(room,name))
                    val toSend = JSON.stringify(PlayerMessage.serializer(),msg)
                    logger.info(toSend)
                    websocket.sendText(toSend)
                }

                override fun onTextMessage(websocket: WebSocket, text: String) {
                    val deserialized = JSON.parse(PlayerResponseMessage.serializer(),text)
                    logger.info("Received: $text")

                    val response = deserialized.response;
                    when(response) {
                        is JoinResponse -> {
                            joinAction(response)
                        }
                        is DiceRoll -> {
                            val placement = strategy(response)

                            val placementMessage = JSON.stringify(
                                    PlayerMessage.serializer(),
                                    PlayerMessage(PlacementAction(room,name,placement))
                            )

                            logger.info("Sent: $placementMessage")
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

                override fun onPongFrame(websocket: WebSocket, frame: WebSocketFrame) {
                    logger.info("Got pong ? ${frame.payload}")
                }
            })
            .connect()

}
