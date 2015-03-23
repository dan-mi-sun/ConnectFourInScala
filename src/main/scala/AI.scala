

//remove if not needed

class AI(private var player: Player, private var depth: Int) extends Solver {


  override def getMoves(b: Board): Array[Move] = {


    override def getMoves(b: Board): Array[Move] =
    {

      val rootState = new State(player, b, null) //last move is null because last move of root tree is unknown
    var bestMoves = Array[Move]()

      //if b is null
      if (!Option(b).isDefined) {
        throw new IllegalArgumentException("A valid board is required")
        System.exit(1)
      }

      //if depth is 0 - Exception not thrown up to GUI as we were instructed to not change the Game class. Caught by
      if (depth == 0) {
        println("No depth of 0 allowed.")
        System.exit(1)
      }

      try {
        AI.createGameTree(rootState, depth)
      } catch {
        case iae: IllegalArgumentException => println("An Illegal Argument was passed as the depth parameter.")
        case e: Exception => println("An exception was thrown in the AI createGameTree method.")
      }
      minimax(rootState)
      rootState.children.foreach { child => {
        if (child.value == rootState.value) bestMoves = bestMoves.:+(child.getLastMove)
      }
      }
      bestMoves
    }


    def minimax(s: State): Unit = {

      if (s.getChildren.length == 0) {
        s.setValue(evaluateBoard(s.getBoard))
      } else {
        for (child <- s.getChildren) {
          //need some logic in here for min and max values
          //we also need to return the value of the state...
          minimax(child)
        }
        // here we can create a logic to decide which max or min to pass up the heirerarcy to the parent
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

    ///helper method to to calculate the minimum value of the the children values for minmax
    private def min(array: Array[State]): Int =
    {
      //need to think of better variable name than x
      var x: Array[Int] = Array[Int]()
      array.foreach { state => x = x.:+(state.value)}
      x.min
    }

    ///helper method to to calculate the maximum value of the the children values for minmax
    private def max(array: Array[State]): Int =
    {
      //need to think of better variable name than x
      var x: Array[Int] = Array[Int]()
      array.foreach { state => x = x.:+(state.value)}
      x.max
    }
  }

  object AI {

    def createGameTree(s: State, d: Int): Unit = {
      //no need to preserve state as initially commented
      //actually a tree is created by keeping a reference to the root state and
      //recursively creating the children

      if (d > 0) {
        s.initializeChildren()
        s.children.foreach { child => createGameTree(child, d - 1)}
      }
    }

    def minimax(ai: AI, s: State) {
      ai.minimax(s)
    }


  }

