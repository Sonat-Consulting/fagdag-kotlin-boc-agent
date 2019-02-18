package no.sonat.game
import kotlinx.serialization.json.JSON
import org.junit.Test
import kotlin.test.assertTrue


class TestSerialization {

    @Test
    fun testPlayerApi() {
        val out  = JSON.stringify(PlayerMessage.serializer(),PlayerMessage(JoinRoomAction("12345","weee")));
        val bt = JSON.parse(PlayerMessage.serializer(),out)
        assertTrue(bt.action is JoinRoomAction)
    }

}