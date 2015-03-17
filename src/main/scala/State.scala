import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.UnsupportedEncodingException
import State._
import scala.beans.BeanProperty
import scala.collection.mutable.ArrayBuffer

object State {

  val length0 = Array[State]()
}

class State(@BeanProperty var player: Player, @BeanProperty var board: Board, @BeanProperty var lastMove: Move)
  extends Comparable[Any] {

  @BeanProperty
  var children: Array[State] = length0

  @BeanProperty
  var value: Int = 0

  def initializeChildren(): Unit = {

    val childrenBuffer = ArrayBuffer[State]()

    //if we are rootstate, it's current player's turn
    //not exactly sure why, but without this, each both player's
    //returned yellow
    val p = if(lastMove == null) player else player.opponent

    for(m <- board.getPossibleMoves(p)) {
      //update of this method has new Board instance
      //generated each time with the move applied
      //this is needed to be able to evaluate the Board
      //i cannot see how its possible to use the "yield"
      //ability whilst performing addition methods (makeMove(s))

      val s = new State(p,new Board(board),m)
      s.getBoard.makeMove(m)
      childrenBuffer.+=:(s)
    }
    children =childrenBuffer.toArray
  }

  def writeToFile() {
    try {
      var writer = new PrintWriter("output.txt", "UTF-8")
      writer.println(this)
      java.awt.Toolkit.getDefaultToolkit.beep()
    } catch {
      case e @ (_: FileNotFoundException | _: UnsupportedEncodingException) => e.printStackTrace()
    }
  }

  override def toString(): String = {
    println("State.toString printing")
    toStringHelper(0, "")
  }

  private def toStringHelper(d: Int, ind: String): String = {
    var str = ind + player + " to play\n"
    str = if(value != null) str + ind + "Value: " + value + "\n" else "\n"
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

  override def compareTo(ob: Any): Int = 0

}

