//  implicit def convertValue(v: Value): Player = v.asInstanceOf[Player]

trait Player {
  def opponent: Player
}

final case object RED extends Player {
  def opponent = YELLOW
}
final case object YELLOW extends Player {
  def opponent = RED
}