import scala.collection.mutable.ArrayBuffer
import scala.util.Random

//remove if not needed

class AI(private var player: Player, private var depth: Int) extends Solver {

  override def getMoves(b: Board): Array[Move] = {
    val originalRootState = new State(player, b, null)
    AI.createGameTree(originalRootState,depth)
    minimax(originalRootState)
    originalRootState.writeToFile()
    Array(getBestState(originalRootState, player).getLastMove)
  }


  def minimax(s: State): Unit = {
    s.setValue(evaluateBoard(s.getBoard))

    if(s.getChildren.length > 0)
      s.getChildren.foreach(child => minimax(child))
  }

  def evaluateBoard(b: Board): Int = {
    val winner = b.hasConnectFour()
    var value = 0
    if (winner == null) {
      val locs = b.winLocations()
      for (loc <- locs; p <- loc) {
        value += (if (p == player) 1 else if (p != null) -1 else 0)
      }
    } else {
      var numEmpty = 0
      var r = 0
      while (r < Board.NUM_ROWS) {
        var c = 0
        while (c < Board.NUM_COLS) {
          if (b.getTile(r, c) == null) numEmpty += 1
          c = c + 1
        }
        r = r + 1
      }
      value = (if (winner == player) 1 else -1) * 10000 * numEmpty
    }
    value
  }

  //used rubbish name "stateplayer" to avoid confusion with AI.player - to rename
  //refactoring is required here - this is too iterative
  //consider refactoring of helper method
  //consider other scala functions
  private def getBestState(node: State, stateplayer: Player):State = {
    if (node.getChildren.length == 0)  return node
    var chosenState = node
    if (stateplayer == player) {
      var statesToCompare: Array[State] = Array[State]()
      for (child <- node.getChildren) {
        child.setValue(getBestState(child, child.getPlayer).getValue)
        statesToCompare = statesToCompare.:+(child)
        chosenState = getMinMaxStateForNode(statesToCompare, "max")
      }
    } else {
      var chosenState = node
      var statesToCompare: Array[State] = Array[State]()
      for (child <- node.getChildren) {
        child.setValue(getBestState(child, child.getPlayer).getValue)
        statesToCompare = statesToCompare.:+(child)
        chosenState = getMinMaxStateForNode(statesToCompare, "min")
      }
    }
    chosenState

  }


//very poor design - refactoring could use function requiring a functional parameter
  def getMinMaxStateForNode(states: Array[State], operation: String):State = {
    var bestState = states(0)

    if (operation == "max"){
      for(i <- 1 until states.length){
        if(states(i).getValue > bestState.getValue) bestState = states(i)
      }
    }else if(operation == "min"){
      for(i <- 1 until states.length){
        if(states(i).getValue < bestState.getValue) bestState = states(i)
      }
    }
    return bestState
  }


}

object AI {

  def createGameTree(s: State, d: Int): Unit = {
    if (d > 0) {
      s.initializeChildren()
      s.children.foreach { child => createGameTree(child, d - 1)}
    }
  }

  def minimax(ai: AI, s: State) {
    ai.minimax(s)
  }

}

