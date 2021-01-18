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

import java.util.Observable
import javafx.collections.ObservableList
import javafx.scene.Node
import scalafx.scene.shape.Circle

//basic abstract class that Defines a single State

abstract class State {
  var name:String
  var transitions:List[(String,State)]
  var action:String
  var initialState: Boolean
  var proposition:String
  var x:Double
  var y:Double
  var guiContent:List[Node]
  var c:Circle
  var remove:Boolean
  var linking:Boolean

  def isAccepted:Boolean
  def getName:String = this.name
  def getTransition(action: String)
  def getTransitions:List[(String,State)] = this.transitions
  def toggleIcons():Unit

  //not sure about this class, may need some work/not sure on its use yet
  /*def getTransition(action: String): (String, State) = {
    var transitionFound:(String,State) = null
    for (t <- this.transitions) {
      if (t._1 == action) {
        transitionFound = t
      }
    }
    transitionFound
  }*/

  def isInitialState:Boolean = this.initialState

  def toggleInitial(bool:Boolean):Unit = this.initialState = bool

  def changeName(newName:String):Unit = this.name = newName

  //abstract methods
  def addTransition(transition:(String,State)):Unit = transitions :+ transition
  def duplicate:Unit
}
