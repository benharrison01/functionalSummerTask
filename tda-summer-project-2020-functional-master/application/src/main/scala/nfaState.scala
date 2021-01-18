/* LTLModelChecker - check LTS and NFA models and compute product

Copyright (C) 2020  Isaac Horry, Laura Cope and Ben Harrison

   WARNING - All rights on Arrow.java are reserved to the original author 
  <https://gist.github.com/kn0412/2086581e98a32c8dfa1f69772f14bca4> 
  Arrow.java would need to be re-written for future use of this application.

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <https://www.gnu.org/licenses/>.*/

import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Line
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color.{Black, Red, Transparent}
import scalafx.scene.shape.Circle

class nfaState(stateName:String,initial:Boolean,accept:Boolean,xCoord:Double,yCoord:Double) extends State {
  var name: String = stateName
  //var proposition: String ---> this needs passing as a parameter and should probably be named something else
  var transitions: List[(String, State)] = List()
  //actions = alphabet
  var action: String = "e"
  var proposition:String = "e"
  var initialState: Boolean = initial
  var acceptState:Boolean = accept
  var x:Double = xCoord
  var y:Double = yCoord
  var remove:Boolean = false
  var linking:Boolean = false
  var guiContent: List[Node] = List()
  var icons:Boolean = false
  def isAccepted:Boolean = acceptState

  def toggleAccepted(bool:Boolean):Unit = acceptState = bool


  override def duplicate: Unit = ???


  override def getTransition(action: String): Unit = ???

  //drawing variables
  var c:Circle = Circle(x,y,25)
  val cAccept = Circle(x, y, 20)
  val lblNodeName = new Label(name)

 /* val lblAtomicProp = new Label("")
  if (proposition.length > 0){
    lblAtomicProp.text = "{" + proposition + "}"
  } */
 //commented out until proposition variable is sorted, then needs adding to guiContent



  val a = new Arrow(x - 45, y, x-25, y, 5)
  a.setVisible(false)

  c.fill = Transparent
  c.stroke = Black
  c.strokeWidth = 1

  cAccept.fill = Transparent
  cAccept.stroke = Black
  cAccept.strokeWidth = 2


  // val xPos = e.x.toInt + 50//lblTest.width.toInt
  lblNodeName.layoutX = x - 10
  lblNodeName.layoutY = y + 15
  lblNodeName.style = "-fx-font-weight: bold; -fx-font-size: 9pt;"
  lblNodeName.setTextFill(Red)
  lblNodeName.toFront()

  //add and delete buttons

  import java.io.FileInputStream

  var input = new FileInputStream("images/delete.png")
  var image = new Image(input)
  var imageView = new ImageView(image)

  val delete = new Button("", imageView)
  delete.setVisible(false)
  delete.layoutX = this.x+40
  delete.layoutY = this.y-70
  delete.onMousePressed = (e:MouseEvent) => {
    remove = true
  }


  input = new FileInputStream("images/add.png")
  image = new Image(input)
  imageView = new ImageView(image)

  val add = new Button("", imageView)
  add.setVisible(false)
  add.layoutX = this.x-75
  add.layoutY = this.y-70
  add.onMousePressed = (e:MouseEvent) => {
    linking = true

  }


  if (initialState) {
    a.setVisible(true)
  }

  if (acceptState) {
    guiContent = List(c, cAccept,a,lblNodeName,add,delete)
  }
  else {
    guiContent = List(c,a,lblNodeName,add,delete)
  }




  //  if (cbDelete.selected.apply() == false) {
  c.fill = Transparent
  c.stroke = Black
  c.strokeWidth = 1



  if (initialState) {
    a.setVisible(true)

  }

  def toggleIcons():Unit = {
    if (icons == false) {
      delete.setVisible(true)
      add.setVisible(true)
      icons = true
    } else {
      delete.setVisible(false)
      add.setVisible(false)
      icons = false
    }

  }
}
