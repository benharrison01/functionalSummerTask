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
import javafx.scene.input.MouseEvent
import javafx.scene.shape.Line
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.{CheckBox, Label, RadioButton, TextField, ToggleGroup}
import scalafx.scene.paint.Color.{Black, Transparent}
import scalafx.scene.shape.Arc
import scala.collection.mutable.ListBuffer

/**
 * Class for creating and operating a LTS diagram
 */
class LTS extends BaseModel() {
  var states:ListBuffer[State] = new ListBuffer[State]()
  var actions:ListBuffer[String] = ListBuffer[String]()
  var transitions:ListBuffer[Transition] = ListBuffer[Transition]()
  var initialStates:List[State] = List()
  var propositions:ListBuffer[String] = ListBuffer[String]()

  var addNode:Boolean = true
  var startText:String = ""
  var cbStart:CheckBox = new CheckBox("Is start node?")
  var cbDelete:CheckBox = new CheckBox("Delete nodes and/or transitions")
  var lblAtomic:Label = new Label("AP: ")
  var lblAction:Label = new Label("Action: ")
  var tfAtomic:TextField = new TextField
  var tfAction:TextField = new TextField
  val toggleGroup = new ToggleGroup
  var guiContent:ListBuffer[Node] = new ListBuffer[Node]()//(rb1,rb2,cbStart,lblAtomic,lblInfo,lblName,tfAtomic,tfName)
  /**
   * Builds the UI of the diagrams components
   */
  def buildScene():Unit = {

    cbStart.layoutX = 20
    cbStart.layoutY = 30
    cbStart.onAction = (e:ActionEvent) => {
      if (cbStart.selected.apply() == true) {
        startText = "start"
        cbDelete.selected.value = false
      }
      else {
        startText = ""
        cbDelete.selected.value = true
      }

    }

    lblAtomic.layoutX = 5
    lblAtomic.layoutY = 5

    tfAtomic.layoutX = 40
    tfAtomic.layoutY = 5
    tfAtomic.promptText = "Atomic Proposition: "

    lblAction.layoutX = 210
    lblAction.layoutY = 5
    tfAction.layoutX = 290
    tfAction.layoutY = 5
    tfAction.promptText = "Transition action: "

    guiContent.appendAll(List(cbStart,lblAtomic,tfAtomic,tfAction,lblAction))
  }
  /**
   *
   * @return the list of all the propositions used in this diagram so far
   */
  def getAP:ListBuffer[String] = propositions

  /**
   *
   * @param ap new proposition to be added
   */
  def addAP(ap:String):Unit = propositions :+ ap

  /**
   *
   * @param ap currently existing proposition to be removed
   */
  def removeAP(ap:String):Unit = propositions = propositions.filter(_ != ap)

  /**
   * Overridden as the propositions need to be managed when a new state is added, checking if the
   * proposition for that state is new or currently existing, hence determining whether it needs to be added or not
   * @param state new state to be added to the diagram
   */
  override def addState(state: State): Unit = {
    states += state
      if (!propositions.contains(state.proposition)) {
        propositions.addOne(state.proposition)
      }

      if (state.proposition.split(",").length > 1) {
        for (char <-  state.proposition.split(",")) {
          if (!propositions.contains(state.proposition)) {
            propositions.addOne(state.proposition)
          }
        }
      }


  }

  /**
   * Overridden to check if the states proposition needs to be removed as well
   * Will be removed if its unique in the propositions list
   * @param state currently existing state to be removed from the diagram
   */
  override def removeState(state: State): Unit = {
    var found = false
    states.remove(states.indexOf(state))
    for (elements <- states) {
      if (elements.proposition == state.proposition) {
        found = true
      }
    }
    if (!found) {
      propositions.filter(_ != state.proposition)
    }
  }




}