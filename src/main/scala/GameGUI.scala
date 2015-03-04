object GameGUI extends App {
  /* -------------------------- Change these to play game differently. -------------------------- */

  /* p1 is the first player, p2 is the second player. A Solver
         * can be a Human, AI, or Dummy. Human and Dummy constructors have
         * a player parameter; the AI constructor has a player and depth
         * as parameters, with the a depth used to recurse when searching the
         * game space. */
  //Solver p1= new AI(Player.RED, 6);
  //Solver p2= new Human(Player.YELLOW);
  val p1 = new Dummy(RED);
  val p2 = new Dummy(YELLOW);
  //Solver p1 = new Human(Player.RED);
  //Solver p2 = new Dummy(Player.YELLOW);

  /* --------------------------------- Do not change below here. --------------------------------- */

  val game = new Game(p1, p2);
  game.setGUI(new GUI(game, Board.NUM_COLS, Board.NUM_ROWS));
  game.runGame();
}