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

import scala.collection.mutable.ListBuffer

//abstract model for both the lts and nfa as they share many parameters
/**
 * Abstract class for the LTS and NFA classes
 * Essentially contains all that is needed to create and operate a diagram
 * we initially came up with the abstract structure, but as the program developed this structure lost its potency
 */
abstract class BaseModel() {
  var states:ListBuffer[State]
  var actions:ListBuffer[String]
  var transitions:ListBuffer[Transition]
  var initialStates:List[State]


  //getters
  /**
   *
   * @return list of states contained in the diagram
   */
  def getStates: ListBuffer[State] = this.states

  /**
   *
   * @return list of transition actions
   */
  def getActions: ListBuffer[String] = this.actions

  /**
   *
   * @return list of all transitions in the diagram
   */
  def getTransitions: ListBuffer[Transition] = this.transitions

  /**
   *
   * @return list of just the states that are initial
   */
  def getInitialStates: List[State] = this.initialStates

  //setters and removers
  /**
   *
   * @param state new state to be added to the diagram
   */
  def addState(state: State): Unit = states += state


  /**
   *
   * @param state currently existing state to be removed from the diagram
   */
  def removeState(state: State): Unit = states.remove(states.indexOf(state))



  /**
   *  adds a new transition as well as its action if its not already found in the diagram
   * @param transition a new transition to be added to the diagram
   */
  def addTransition(transition:Transition):Unit = {
    transitions.addOne(transition)
    if (actions.indexOf(transition.expression) == -1) {
      actions.addOne(transition.expression)
    }
  }

  /**
   * removing a specific transition
   * @param target a currently existing transition that has been requested to be removed
   */
  def removeTransition(target:Transition):Unit = {
    transitions.remove(transitions.indexOf(target))
  }

  /**
   * Method that removes all transitions related to a particular state
   * called when a state is removed usually
   * @param target state who's related transitions need to be removed
   * @return not sure
   */
  def findandRemoveTransitions(target:State): ListBuffer[Transition] = {
    var tempList = ListBuffer[Transition]()
    for(transition <- this.transitions) {
      if (transition.startState == target || transition.endState == target) {
        transitions.remove(transitions.indexOf(transition))
        tempList.addOne(transition)
      }
    }
    tempList
  }

}
