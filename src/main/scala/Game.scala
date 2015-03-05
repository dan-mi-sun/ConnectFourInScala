/* When testing, you may want to comment out all the above statements
      */

/* -------------------------------------------------------------- */

/* ABOUT TESTING.
 * Testing is difficult using a JUnit testing class because it is difficult to
 * get at many of the fields and methods from that class. So you may want to
 * put some testing methods in this class.
 *
 * In testing various methods, you may want to use boards with certain
 * layouts of your choosing. For example, a board with column 0 all filled.
 * To do this, you can
 *
 *     1. Create a board b
 *     2. Call makeMove several times to put pieces where you want them.
 *     3. Play the game.
 *
 * For example, you can write the following to set up an initial board and
 * then play the game
connect4.java.Board
connect4.java.Board     Board b= new Board();
connect4.java.Move connect4.java.PlayerakeMove(new Move(Player.RED, 4));
connect4.java.Move connect4.java.PlayerakeMove(new Move(Player.YELLOW, 3));
connect4.java.Move connect4.java.PlayerakeMove(new Move(Player.REDconnect4.java.Game);
connect4.java.Game    Game game= new Game(p1, p2, b, false);
 *
 * This code places a red piece in column 4, a yellow piece in column 3,
 * and a red piece in column 5. Then it runs the game.
 *
 * We give you also procedure fillColumn at the end of this file to help
 * out in initializing a board. Study it. Note that it is static.
 *
 * Suppose you want to test a methodconnect4.java.Board your wrote, like Board.getPossibleMoves.
 * Thus, you want to do the following.
 *
 *     1. Create a board b
 *     2. Call makeMove several times to put pieces where you want them.
 *     3. Call the method you want to test.
 *     4. Check the result, if any.
 *
 * You can check the result by using println statements to print out things and
 * looking at the output. You are testing by eyeballing the output. This is OK as
 * long as you are careful. Here is how you could print out the results of a
 * call to getPossibleMoves:
connect4.java.Board
connect4.java.Board     Board b= new Board();
connect4.java.Player   fillColumn(b, Player.RED, 0);  // fill coconnect4.java.Move 0
 *      Move[] movconnect4.java.PlayergetPossibleMoves(Player.RED)connect4.java.Move       *      for (Move m : moves) {
 *           System.out.println(m);
 *
 * If you are having real trouble, the above may not help. Here is what you
 * can do to connect4.java.Board  very basics of Board.getPossibleMoves:
connect4.java.Board
connect4.java.Board     Board b= new Board();
connect4.java.Player   fillColumn(b, Player.RED, 0);  // fill connect4.java.Move 0
 *      Move[] movconnect4.java.Board.connect4.java.PlayersibleMoves(Board.Player.RED);
 *   connect4.java.Board (moves.length != Board.NUM_COLS-1) {
 *          System.out.println("Error in getPossibleMoves with 1 col filled. array size is wrong: " +  moves.length);
 *      }
 *      if (moves[0].getColumn() != 1) {
 *          System.out.println(s + "First col is filled, second isn't but moves[0] is " + moves[0]);
 *      }
 *
 * We suggest you write a static method to test getPossibleMoves
 * (and perhaps other methods for other tests).
 * Make it a method so that you can call it or not from method main, depending on
 * your needs. It doesn't have to test ALL possible cases, but it has to check
 * enough that so you are positive the method works.
 *
 *
}
 *
 * */

/* ********* Put any testing methods that you write here.  *******
 * *** We will not look at this class anyway ***/

  object Game extends App {

  private val SLEEP_INTERVAL = 10

  val p1 = new Dummy(RED)
  val p2 = new Dummy(YELLOW)
  val game = new Game(p1, p2)
  game.runGame()
}

class Game(private var activePlayer: Solver, private var player2: Solver) {

  private var board: Board = new Board()

  private var gui: GUI = _

  private var winner: Player = _

  /** ************** Do not change anything below here ***************/
  /**
   * Construct a new Game with p1 as the first player, p2 as the second player,
   * connect4.java.Board b as the current Board state, and with it being p's turn to play
   * true means player 1, false means player 2).
   */
  /**
   * Constructor: a new Game with p1 as the first player and p2 as the second player.
   * It is p1's turn to play.
   * Precondition: p1 and p2 are different - this constructor was only in Java
   */



  def this(p1: Solver,
    p2: Solver,
    b: Board,
    p: Boolean) {
    this(p1, p2)
    board = b
    activePlayer = (if (p) p1 else p2)
  }

  def setGUI(gui: GUI) {
    this.gui = gui
  }

  /**
   * Notify this Game that column col has been clicked by a user.
   */

  def columnClicked(col: Int) {
    if (activePlayer.isInstanceOf[Human]) {
      activePlayer.asInstanceOf[Human].columnClicked(col)
    }
  }

  /**
   * Run the GUI until finished. If GUI is not initialized, the output will be
   * sent to the console.
   */

  def runGame() {
    while (!isGameOver) {
      var moveIsSafe = false
      var nextMove: Move = null
      while (!moveIsSafe) {
        val bestMoves = activePlayer.getMoves(board)
        if (bestMoves.length == 0) {
          gui.setMsg("Game cannot continue until a Move is produced.")
          //continue
        } else {
          nextMove = bestMoves(0)
        }
        if (board.getTile(0, nextMove.column) == null) {
          moveIsSafe = true
        } else {
          gui.setMsg("Illegal Move: Cannot place disc in full column. Try again.")
        }
      }
      board.makeMove(nextMove)
      if (gui == null) {
        println(nextMove)
        println(board)
      } else {
        gui.updateGUI(board, nextMove)
      }
      val temp = activePlayer
      activePlayer = player2
      player2 = temp
      try {
        Thread.sleep(Game.SLEEP_INTERVAL)
      } catch {
        case e: InterruptedException => e.printStackTrace()
      }
    }
    if (gui == null) {
      if (winner == null) {
        println("Tie game!")
      } else {
        println(winner + " won the game!!!")
      }
    } else {
      gui.notifyGameOver(winner)
    }
  }

  /**
   * Return true if this game is over. If the game
   * is over, set the winner field to the winner; if no winner
   * set the winner to null.
   */

  def isGameOver(): Boolean = {
    winner = board.hasConnectFour()
    if (winner != null) return true
    var r = 0
    while (r < Board.NUM_ROWS) {
      var c = 0
      while (c < Board.NUM_COLS) {
        if (board.getTile(r, c) == null) return false
        c = c + 1
      }
      r = r + 1
    }
    true
  }
}

