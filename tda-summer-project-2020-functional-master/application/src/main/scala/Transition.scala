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

import javafx.scene.Node
import scalafx.scene.control.Label

import scala.collection.mutable.ListBuffer

abstract class Transition {
  var startState:State
  var endState:State
  var action:String
  var actions:List[String]
  var guiContent:ListBuffer[Node]
  var startX:Double
  var startY:Double
  var endX:Double
  var endY:Double
  var remove:Boolean
  var lblAction:Label
  var expression:String
  //var actions:ListBuffer[String]

}
