import java.util.Random

class Dummy(private var myColour: Player) extends Solver {

  override def getMoves(b: Board): Array[Move] = {
    val rand = new Random()
    var column = rand.nextInt(Board.NUM_COLS)
    while (b.getTile(0, column) != null) {
      column = rand.nextInt(Board.NUM_COLS)
    }
    val move = Array(new Move(myColour, column))
    move
  }
}

