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
import scalafx.event.ActionEvent
import scalafx.scene.control.{CheckBox, ChoiceBox, ComboBox, Label, TextField, ToggleGroup}
import scalafx.Includes._
import scalafx.scene.input.InputEvent

import scala.collection.mutable.ListBuffer
import scala.util.matching.Regex

/**
 * Class for creating and operating a NFA
 */
class NFA extends BaseModel {
  var states:ListBuffer[State] = new ListBuffer[State]()
  var transitions:ListBuffer[Transition] = ListBuffer[Transition]()
  var actions:ListBuffer[String] = ListBuffer[String]()
  var initialStates:List[State] = List()
  var propositions:ListBuffer[String] = ListBuffer[String]()
  var validStates:List[String] = List()

  var acceptStates:List[State] = List()

  var addNode:Boolean = true
  var startText:String = ""
  var cbStart:CheckBox = new CheckBox("Initial State")
  var cbAccept:CheckBox = new CheckBox("Accepting State")
  var lblAction:Label = new Label("Expression: ")
  var tfExp:TextField = new TextField

  var lblValid: Label = new Label("Your AP is empty.")
  var guiContent:ListBuffer[Node] = new ListBuffer[Node]()//(rb1,rb2,cbStart,lblAtomic,lblInfo,lblName,tfAtomic,tfName)
  var enabled:Boolean = true

  /**
   * Update function used to continuosly to update real time changing values
   */
  def update(lts:LTS):Unit = {
    propositions = lts.propositions
    enabled = true
    val currentExpText = tfExp.getText()
    if (!currentExpText.isEmpty) {
      validStates = getValidStates(currentExpText)
      if (enabled) {
        lblValid.text = "Valid Expression"
      }

    } else {
      enabled = false
    }



  }

  /**
   * Builds the UI of the diagrams components
   */
  def buildScene():Unit = {

    cbStart.layoutX = 20
    cbStart.layoutY = 5

    cbAccept.layoutX = 20
    cbAccept.layoutY = 30

    lblValid.layoutX = 210
    lblValid.layoutY = 35

    lblAction.layoutX = 210
    lblAction.layoutY = 5
    tfExp.layoutX = 290
    tfExp.layoutY = 5
    tfExp.promptText = "Logical Expression: "





    guiContent.appendAll(List(cbStart,cbAccept,tfExp,lblAction, lblValid))
  }



  /**
   *
   * @return a list of terminal states in the diagram
   */
  def getAcceptStates:List[State] = acceptStates

  /**
   * adding a state that is terminal
   * @param as
   */
  def addAS(as:State):Unit = acceptStates :+ as

  /**
   *
   * @param as currently existing state to be removed as an accepting state
   */
  def removeAS(as:State):Unit = acceptStates = acceptStates.filter(_ != as)

  def validate(text: String): Boolean = {

    val NOT = '¬'
    val OR = '∨'
    val AND = '∧'

    val characters = text.toArray
    for (i <- 0 to characters.length -1){
      println(characters(i))
    }

    //check for wellbracketedness
    var bracketStack: ListBuffer[Char] = ListBuffer()
    for (c <- characters) {
      if (c == '('){
        bracketStack.addOne('(')
      }
      else if (c == ')') {
        if (bracketStack.length == 0) {
          println("( and ) pairs do not match.")
          lblValid.text = "( and ) pairs do not match."
          return false
        }
        else {
          bracketStack.dropRight(1)
        }
      }
    }
    if (bracketStack.length != 0) {
      println("( and ) pairs do not match.")
      lblValid.text = "( and ) pairs do not match."
      return false
    }

    //check for invalid patterns
    for (i <- 0 to characters.length - 2){
      if (characters(i).equals(NOT)){
        if (characters(i+1) == AND){
          println("NOT symbol cannot be followed by an AND symbol.")
          lblValid.text = "NOT symbol cannot be followed by an AND symbol."
          return false
        }
        else if (characters(i+1) == OR){
          println("NOT symbol cannot be followed by an OR symbol.")
          lblValid.text = "NOT symbol cannot be followed by an OR symbol."
          return false
        }
      }

      if (characters(i) == AND){
        if (characters(i+1) == AND){
          println("AND symbol cannot be followed by an AND symbol.")
          lblValid.text = "AND symbol cannot be followed by an AND symbol."
          return false
        }
        else if (characters(i+1) == OR){
          println("AND symbol cannot be followed by an OR symbol.")
          lblValid.text = "AND symbol cannot be followed by an OR symbol."
          return false
        }
      }

      if (characters(i) == OR){
        if (characters(i+1) == AND){
          println("OR symbol cannot be followed by an AND symbol.")
          lblValid.text = "OR symbol cannot be followed by an AND symbol."
          return false
        }
        else if (characters(i+1) == OR){
          println("OR symbol cannot be followed by an OR symbol.")
          lblValid.text = "OR symbol cannot be followed by an OR symbol."
          return false
        }
      }
    }

    //check if the last character begins to be an unfinished expression

    if (characters.last == NOT) {
      println("Trailing character cannot be a NOT symbol.")
      lblValid.text = "Trailing character cannot be a NOT symbol."
      return false
    }
    else if (characters.last == AND) {
      println("last is " + characters.last)
      println("Trailing character cannot be an AND symbol.")
      lblValid.text = "Trailing character cannot be an AND symbol."
      return false
    }
    else if (characters.last == OR) {
      println("Trailing character cannot be an OR symbol.")
      lblValid.text = "Trailing character cannot be an OR symbol."
      return false
    }

    //else return true if you have made it this far
    true
  }


/** Converts an NFA label expression String (e.g. 'A ∨ B') into a List of acceptable LTS State labels (e.g. 'A','B')
 *  e.g. ('A ∧ B') returns ('A','B','A,B')
 *
 *  @param expStr the expression to be evaluated
 *  @return list of acceptable LTS State labels
 */
 
  def getValidStates(expStr:String):List[String]={
    val ltsAPSet = propositions.toSet
    val expStrLower = expStr.toLowerCase().toString
    val expStrClean = expStrLower.replaceAll("\\s", "")

    val conj: Regex = "^(\\S+)(∧|·|&|and)(\\S+)*$".r
    val disj: Regex = "^(\\S+)(∨|\\+|∥|or)(\\S+)*$".r
    val neg: Regex = "^(¬|not|˜)(.+)*$".r
    val nothing: Regex ="^(false|nil)$".r
    val all: Regex = "^(true)$".r


    if (conj.matches(expStrClean)){//AND

      val conj1 = conj.findFirstMatchIn(expStrClean).get.group(1)
      val conj2 = conj.findFirstMatchIn(expStrClean).get.group(3)
      val conj1Set = getValidStates(conj1).toSet
      val conj2Set = getValidStates(conj2).toSet

      val intersectConj = conj1Set ++ conj2Set
      var str = ""
      for (char <- intersectConj) {
        if (str == "") {
          str += char
        } else {
          str += ","+char
        }

      }

      return List(str)
    }
    if (disj.matches(expStrClean)){//OR

      val disj1 = disj.findFirstMatchIn(expStrClean).get.group(1)
      val disj2 = disj.findFirstMatchIn(expStrClean).get.group(3)
      val disj1Set = getValidStates(disj1).toSet
      val disj2Set = getValidStates(disj2).toSet
      val unionDisj = disj1Set.union(disj2Set)
      return unionDisj.toList
    }
    if (neg.matches(expStrClean)){//NOT
      val neg1 = neg.findFirstMatchIn(expStrClean).get.group(2)
      val neg1Set = getValidStates(neg1).toSet
      val diffNeg = ltsAPSet.diff(neg1Set)
      return diffNeg.toList
    }
    if (all.matches(expStrClean)){
      return propositions.toList
    }
    if (ltsAPSet.contains(expStrClean)){
      List(expStrClean)
    }
    else{
      if (!nothing.matches(expStrClean)){
        lblValid.text = "Error Invalid expression"
        enabled = false
      }
      List.empty[String]
    }
  }
}
