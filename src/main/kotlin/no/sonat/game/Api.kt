package no.sonat.game

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class PlayerResponseMessage(
        @Polymorphic val response : PlayerResponse
)

sealed class PlayerResponse

@Serializable
data class JoinResponse(val id : String) : PlayerResponse();

@Serializable
data class GameEnd(val id : String) : PlayerResponse();

@Serializable
data class GameError(val id : String,val errorMsg : String) : PlayerResponse();


@Serializable
data class DiceRoll(val value: Int) : PlayerResponse();


sealed class PlayerAction

@Serializable
data class JoinRoomAction(val id : String, val name : String) : PlayerAction();

@Serializable
data class PlacementAction(val id : String, val name : String, val placement: Placement) : PlayerAction();


@Serializable data class PlayerMessage(
        @Polymorphic val action : PlayerAction
)