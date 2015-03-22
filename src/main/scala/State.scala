import java.io.{FileNotFoundException, PrintWriter, UnsupportedEncodingException}

import State._

import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

class State(@BeanProperty var player: Player, @BeanProperty var board: Board, @BeanProperty var lastMove: Move)
  extends Comparable[Any] {

  @BeanProperty
  var children: Array[State] = length0

  @BeanProperty
  var value: Int = 0


  def initializeChildren(): Unit = {

    val childrenBuffer = ArrayBuffer[State]()
    /*
    * We need to get the possible moves and instantiate these children.
    *
    */

//    board.getPossibleMoves(player).foreach(move => {
//      children = children.:+(new State(player.opponent, new Board(board, move), move))
//    })

    children =
      for(move <- board.getPossibleMoves(player))
      yield new State(player.opponent, new Board(board,move), move)


  }

  /*
  *Method to stdout print to output.txt so that you can visually see the results ie we can see the State and
  *children
   */

  def writeToFile() {
    try {
      var writer = new PrintWriter("output.txt", "UTF-8")
      writer.println(this)
      writer.close()
      java.awt.Toolkit.getDefaultToolkit.beep()
    } catch {
      case e@(_: FileNotFoundException | _: UnsupportedEncodingException) => e.printStackTrace()
    }
  }

  override def toString(): String = {
    println("State.toString printing")
    toStringHelper(0, "")
  }

  override def compareTo(ob: Any): Int = 0

  /*
  * This method prints out an ASCII representation of the board and all its children to the predetermined depth (d)
   */

  private def toStringHelper(d: Int, ind: String): String = {
    var str = ind + player + " to play\n"
    str = if (value != null) str + ind + "Value: " + value + "\n" else "\n"
    str = str + "turn: " + lastMove + "\n"
    str = str + board.toString(ind) + "\n"
    if (children != null && children.length > 0) {
      str = str + ind + "Children at depth " + (d + 1) + ":\n" + ind +
        "----------------\n"
      for (s <- children) {
        str = str + s.toStringHelper(d + 1, ind + "   ")
      }
    }
    str
  }

}

/**
 * An instance represents the current _state_ of the game of Connect4
 */

object State {

  val length0 = Array[State]()
}



