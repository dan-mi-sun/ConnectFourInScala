import java.util.Random

import AI._

//remove if not needed

class AI(private var player: Player, private var depth: Int) extends Solver {


  override def getMoves(b: Board): Array[Move] = {

    val rootstate = new State(player, b, null)
    createGameTree(rootstate, depth)
    minimax(rootstate)
    rootstate.writeToFile()

    //TODO: traveral of tree (val - rootstate) in order to determine best its best
    //child - need to start from leaves and work up to nodes

    /* as the final step - (traversing the tree from leaves up to nodes to determine
     best step) has not yet been implemented, in order to test the
    creation of tree and assignment of values to each state,
    uncomment out below lines - it will allow human to play again AI opponent
    by having the AI opponent pick a random column from its possible moves */

    val rand = new Random()
    val moves = for (c <- rootstate.getChildren) yield c.getLastMove
    var randomColumn = rand.nextInt(moves.length)
    val m = moves(randomColumn)
    Array(m)

  }


  def minimax(s: State): Unit = {

    if (s.getChildren.length == 0) {
      s.setValue(evaluateBoard(s.getBoard))
    } else {
      for (child <- s.getChildren) {
        minimax(child)
      }
    }

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
}

object AI {

  def createGameTree(s: State, d: Int): Unit = {
    //no need to preserve state as initially commented
    //actually a tree is created by keeping a reference to the root state and
    //recursively creating the children


    if (d > 0) {
      s.initializeChildren()
      s.children.foreach { x => createGameTree(???)}
      //we should come up with a better name than x
    }
  }
  
  def minimax(ai: AI, s: State) {
    ai.minimax(s)
  }


  //    s.initializeChildren()
  //
  //    if (d > 1)
  //      s.getChildren.foreach(x => {
  //        if (x.getBoard.hasConnectFour() == null) {
  //          createGameTree(x, d - 1)
  //        }
  //      })
  //  }
  //

}

