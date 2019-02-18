# fagdag-kotlin-boc-agent
Battle of code, kotlin agent base

### The game we are playing

The game we are playing is called Get1000.

The game consists of a series of rounds, each with 9 consecutive dice rolls.

For each roll players simultaneously place the number of eyes in a 3x3 grid.

When all nine rolls have been placed, the round is finished, and the sum  of the grid is calculated
as seen below.


| |  | H | T | O |
|---|---|---|----|----| 
| |  |1 |1 |2| |
| | +| 3|4 |2| |
| | +| 2| 3| 4 |
| |  |   |  |  |
|=|  |  6| 8 | 8| |

The winner of a round, is the player with the smallest distance to 1000.

In this case the distance would be |1000 - 688| = 312.

| |  | H | T | O |
|---|---|---|----|----|
| |  |4 |1 |2| |
| | +| 3|4 |2| |
| | +| 2| 3| 1 |
| |  |   |  |  |
|=|  |  9| 8 | 5| |

Another player placed the same sequence of values like this, and got the distance |1000 - 985| = 15, and 
is therefore the winner of this round.


### Time to respond and Illegal moves

For each roll, the value is delivered to all connected agents over websocket, 
then each agent has at most one second to respond. Using less then half a second should be safe.

If an agent fails to respond in time, or attempts an illegal placement, the agent forfeits the entire match.
An illegal placement would for example be to attempts to place the current value in the full hundreds (H) column in the grid below. 

| |  | H | T | O |
|---|---|---|----|----|
| |  |1 |1 |2| |
| | +| 3| | | |
| | +| 2| |  |
| |  |   |  |  |
|=|  |  6| 1 | 2| | 

The winner is the first agent to 20 wins.


### Let's get coding
Open the file MyAgent.kt

Add your teams name to the _NAME_ constant.
The _ROOM_ and _WS_URI_ constants can be ignored for now.

For each dice roll the function ```fun myStrategy(roll : DiceRoll) : Placement``` will be in invoked.

Remember:

* It is up to the agent to keep track of the current state of the game.
* It is up to the agent to figure out legal placements.
* It is up to the agent to respond in time with a legal placement.
* When a round is over, the next starts immediately with a new dice roll. The agent will therefore also have to keep track of when the grid is full and a new round starts.

Good Luck!


### Useful kotlin resources
[Data classes](https://kotlinlang.org/docs/reference/data-classes.html)

[Control flow](https://kotlinlang.org/docs/reference/control-flow.html)

[Functions](https://kotlinlang.org/docs/reference/functions.html)

[Collections](https://kotlinlang.org/docs/reference/collections.html)


### I Want to develop in language X instead

You will then need to implement the WebSocket client.
See ```AgentClient.kt``` for the kotlin equivalent.

Then you need to use these JSON messages:

First to join a room, send:

```{"action":["no.sonat.game.JoinRoomAction",{"id":"Sonat","name":"MegaMan"}]}```

On success you will get response:

```{"response":["no.sonat.game.JoinResponse",{"id":"Sonat"}]}```


Then you have to wait, and once game starts you will get dice rolls which looks like this:

```{"response":["no.sonat.game.DiceRoll",{"value":2}]}```

Which you need to respond to like this:

```{"action":["no.sonat.game.PlacementAction",{"id":"Sonat","name":"MegaMan","placement":"HUNDREDS"}]}```

