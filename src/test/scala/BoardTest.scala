package com.danielsan.connectfour

import org.scalatest.{Matchers, FunSpec}

class BoardTest extends FunSpec with Matchers {
  
  describe("A Game") {
    it("can allow two Humans to play a game of Connect Four") {
      val game = new Game(p1, p2);
      game.setGUI(new GUI(game, Board.NUM_COLS, Board.NUM_ROWS))
      game.runGame()
      
    }
    
  }

}
