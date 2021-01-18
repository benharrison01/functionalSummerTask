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

import javafx.scene.shape.Line
import scalafx.scene.paint.Color.{Black, Transparent}
import scalafx.scene.shape.Arc

import scala.collection.mutable.ListBuffer
import javafx.scene.Node
import scalafx.scene.control.Label

class ltsTransition(start:State,act:String,end:State) extends Transition {
  var startState:State = start
  var endState:State = end
  var action:String = act
  var guiContent:ListBuffer[Node] = ListBuffer[Node]()
  var startX:Double = startState.x
  var startY:Double = startState.y
  var endX:Double= endState.x
  var endY:Double = endState.y
  var remove:Boolean = false
  var lblAction:Label = new Label(action)
  var expression:String = "e"
  var actions:List[String] = List("E")

  if (startState == endState){
    val x = 50/math.sqrt(2)
    val arc = Arc(startX, startY - 20, 30, 20, -40, 260)
    //these numbers hold no true meaning, it was a bit of trial and error
    arc.fill = Transparent
    arc.strokeWidth = 1
    arc.setStroke(Black)
    //Arc(x center, y center, radius x, radius y, start angle, angle span)
    val a = new Arrow(endX + 27, endY - 8, endX+ 27, endY - 8, 5) //these numbers hold no true meaning, it was a bit of trial and error
    //  guiContent.append(a)
    guiContent.append(a)
    guiContent.append(arc)
    a.toBack()
    a.toBack()

    lblAction.layoutX = startState.x
    lblAction.layoutY = startState.y-60
    guiContent.addOne(lblAction)
  }
  else {
    println("drawing line")
    val l = new Line(startX, startY, endX, endY)
    l.setStrokeWidth(1)
    l.toBack
    l.toBack
    val a = new Arrow(startX, startY, endX, endY, 5)
    guiContent.addOne(a)
    guiContent.addOne(l)
    a.toBack()
    a.toBack()
    lblAction.layoutX =  endX + ((startX-endX)/2)
    lblAction.layoutY = endY + ((startY-endY)/2)
    guiContent.addOne(lblAction)
  }

}
