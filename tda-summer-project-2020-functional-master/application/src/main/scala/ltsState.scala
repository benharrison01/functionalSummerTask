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

import javafx.event.EventHandler
import java.util.Observable

import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.shape.Line
import scalafx.Includes._
import scalafx.event.{ActionEvent, EventHandlerDelegate}
import scalafx.scene.control.{Button, CheckBox, Label}
import javafx.scene.input.MouseEvent
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color.{Black, Red, Transparent, White}
import scalafx.scene.shape.Circle

class ltsState(stateName:String,initial:Boolean,ap:String,xCoord:Double,yCoord:Double) extends State {
  var name: String = stateName
  var transitions: List[(String, State)] = List()
  var proposition:String = ap
  var initialState: Boolean = initial
  var action: String = "e"
  var x:Double = xCoord
  var y:Double = yCoord
  var remove:Boolean = false
  var linking:Boolean = false
  var icons:Boolean = false

  def isAccepted:Boolean = ???


  //drawing variables
  var c:Circle = Circle(x,y,25)

  val lblNodeName = new Label(name)


  val lblAtomicProp = new Label("")
  if (proposition.length > 0){
    lblAtomicProp.text = "{" + proposition.toString() + "}"
  }



  val a = new Arrow(x - 25, y - 25, x, y, 5)
  a.setVisible(false)

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

  //  if (cbDelete.selected.apply() == false) {
  c.fill = Transparent
  c.stroke = Black
  c.strokeWidth = 1

  // val xPos = e.x.toInt + 50//lblTest.width.toInt
  lblNodeName.layoutX = x - 10
  lblNodeName.layoutY = y + 15
  lblNodeName.style = "-fx-font-weight: bold; -fx-font-size: 9pt;"
  lblNodeName.setTextFill(Red)
  lblNodeName.toFront()

  //  val xPos = e.x.toInt + 50//lblTest.width.toInt
  lblAtomicProp.layoutX = x - 10
  lblAtomicProp.layoutY = y - 55
  lblAtomicProp.style = "-fx-font-weight: bold; -fx-font-size: 9pt;"
  lblAtomicProp.setTextFill(Red)
  lblAtomicProp.toFront()

  if (initialState) {
    a.setVisible(true)

  }

  var guiContent:List[Node] = List(c,a,lblNodeName,lblAtomicProp,delete,add)


  /*
  def getLabel:String = label
  def setLabel(bool:Boolean):Unit = _
  def changeLabel(newLabel:String):Unit = label = newLabel
  */


  /*
   addCircle(x : Double, y : Double)

   Takes the x and y coordinates of the mouse click position as parameters and creates
   a circle of radius 50 with centre (x, y).
    */

  override def duplicate: Unit = ???

  override def getTransition(action: String): Unit = ???

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
