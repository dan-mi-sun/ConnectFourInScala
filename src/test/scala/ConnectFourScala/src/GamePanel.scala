import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Color, Dimension, Graphics, Graphics2D}
import java.util.concurrent.Semaphore
import javax.swing.{JPanel, Timer}

import scala.beans.BeanProperty

class GamePanel(private var window: GUI, b: Board) extends JPanel {

  private val emptyColor = new Color(208, 208, 208)
  private val backColor = new Color(0, 123, 255)
  private val sepColor = new Color(0, 123, 255)
  private val yellowColor = new Color(255, 255, 0)
  private val redColor = new Color(255, 0, 0)
  private val cellSize = 62
  private val sepSize = 3
  private val wid: Int = Board.NUM_COLS * cellSize + (Board.NUM_COLS + 1) * sepSize
  private val hght: Int = Board.NUM_ROWS * cellSize + (Board.NUM_ROWS + 1) * sepSize

  @BeanProperty
  var board = Board(b)

  private val waitSema: Semaphore = new Semaphore(0)
  private var timer: Timer = _

  var animation: Boolean = _

  private var animationColor: Color = _
  private val animDelay = 75
  private var colIndex: Int = _
  private var rowIndex: Int = _
  private var stopRow: Int = _

  private def startAnimation(col: Int) {
    colIndex = col
    rowIndex = -1
    animation = true
    stopRow = 0
    while (!(stopRow >= Board.NUM_ROWS || board.getTile(stopRow, colIndex) != null)) {
      stopRow += 1
    }

    val taskPerformer = new ActionListener() {
      def actionPerformed(evt: ActionEvent) {
        if (animation) {
          if (rowIndex + 1 < stopRow) {
            rowIndex += 1
            repaint()
          } else {
            repaint()
            animation = false
            waitSema.release()
            timer.stop()
          }
        }
      }
    }

    timer = new Timer(animDelay, taskPerformer)
    timer.start()
  }

  def playColumn(p: Player, col: Int) {
    window.setMsg("Run playColumn with column " + col + ".")
    animationColor = if (p == YELLOW) yellowColor else redColor
    startAnimation(col)
    try {
      waitSema.acquire()
    } catch {
      case e: InterruptedException => e.printStackTrace()
    }
  }

  def updateBoard(b: Board) {
    board = Board(b)
  }

  override def getPreferredSize(): Dimension = new Dimension(wid, hght)

  override def paintComponent(g: Graphics) {
    super.paintComponent(g)
    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setColor(backColor)
    g2d.fillRect(0, 0, wid, hght)
    g2d.setColor(sepColor)
    var y = 0
    var i = 0
    while (i <= Board.NUM_ROWS) {
      g2d.fillRect(0, y, wid, sepSize)
      y = y + sepSize + cellSize
      i = i + 1
    }
    var x = 0
    i = 0
    while (i <= Board.NUM_COLS) {
      g2d.fillRect(x, 0, sepSize, hght)
      x = x + sepSize + cellSize
      i = i + 1
    }

    x = sepSize
    for (i <- 0 until Board.NUM_COLS) {
      y = sepSize
      for (j <- 0 until Board.NUM_ROWS) {
        if (board.getTile(j, i) == YELLOW) {
          g2d.setColor(yellowColor)
          g2d.fillOval(x, y, cellSize, cellSize)
        } else if (board.getTile(j, i) == RED) {
          g2d.setColor(redColor)
          g2d.fillOval(x, y, cellSize, cellSize)
        } else {
          g2d.setColor(emptyColor)
          g2d.fillOval(x, y, cellSize, cellSize)
        }
        y = y + sepSize + cellSize
      }
      x = x + sepSize + cellSize
    }
    if (animation) {
      g2d.setColor(animationColor)
      val xLoc = colIndex * (sepSize + cellSize) + sepSize
      val yLoc = rowIndex * (sepSize + cellSize) + sepSize
      g2d.fillOval(xLoc, yLoc, cellSize, cellSize)
    }
  }
}