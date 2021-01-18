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

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Scene, input}
import scalafx.scene.shape.{Arc, Circle, Ellipse, Polygon, Rectangle, TriangleMesh}
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control.Button
import scalafx.scene.paint.Color._

import scala.collection.mutable.ListBuffer
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{Border, BorderPane, BorderStroke, BorderStrokeStyle, BorderWidths, CornerRadii, Pane}
import scalafx.scene.paint.Color

object myGUI extends JFXApp {
  stage = new PrimaryStage {
    title = "Functional Programming Application"
    scene = new Scene(900, 600) {
      root = new BorderPane {
        var lts = new LTS()
        var nfa = new NFA()
        var productDiagram = new LTS()

        var ltsCount = 0
        var nfaCount = 0

        var ltsPane = new Pane {

          //classes that manage the drawing of the whole diagram
          lts.buildScene()

          var ltsCanvas = Rectangle(0, 60, 450, 250)
          ltsCanvas.fill = Transparent
          ltsCanvas.stroke = Black
          children.add(ltsCanvas)

         ltsCanvas.onMouseClicked = (e: MouseEvent) => {
            //create State

            val newState = new ltsState("s"+ltsCount, lts.cbStart.selected.apply(), lts.tfAtomic.text(), e.x, e.y)
            lts.addState(newState)
            ltsCount += 1
            newState.c.onMousePressed = () => {
              var linked = false
              for (state <- lts.states) {
                if (state.linking == true) {
                  val newTransition = new ltsTransition(state, lts.tfAction.getText, newState)
                  lts.transitions.addOne(newTransition)
                  linked = true
                  state.linking = false
                  state.toggleIcons()
                }
              }
              if (linked == false) {
                newState.toggleIcons()
              }
            }
          }
        }
        left = ltsPane

        var nfaPane = new Pane {

          nfa.buildScene()

          var nfaCanvas = Rectangle(0, 60, 450, 250)
          nfaCanvas.fill = Transparent
          nfaCanvas.stroke = Black
          children.add(nfaCanvas)

          nfaCanvas.onMouseClicked = (e: MouseEvent) => {
            if (nfa.enabled) {
              //create State
              val newState = new nfaState("q"+nfaCount, nfa.cbStart.selected.apply(), nfa.cbAccept.selected.apply(), e.x, e.y)
              nfa.addState(newState)
              nfaCount += 1
              newState.c.onMousePressed = () => {

                var linked = false
                for (state <- nfa.states) {
                  if (state.linking == true) {
                    val newTransition = new nfaTransition(state, nfa.tfExp.getText, newState,nfa.validStates)
                    nfa.transitions.addOne(newTransition)

                    linked = true
                    state.linking = false
                    state.toggleIcons()
                  }
                }
                if (linked == false) {
                  newState.toggleIcons()
                }
              }
            } else {
              nfa.lblValid.text = "Invalid Current Expression"
            }

          }

        }
        right = nfaPane

        var productPane = new Pane {

          //var productCanvas = Rectangle(0, 0,60,45) //needs sorting
          //productCanvas.fill = Transparent
         // productCanvas.stroke = Black
        //  children.add(productCanvas)

          var productButton = new Button {
            text = "Compute Product"
            layoutX = 700
            layoutY = 5

          }

          productButton.resize(100,50)
          productButton.onAction = (e:ActionEvent) => {
            var yController = 0
            productDiagram = new LTS()
            for (s <- lts.states) {
              for (q <- nfa.states) {
                if (s.initialState == true && q.initialState == true) {
                  product(s,q,75,yController)
                  yController += 50
                }
              }
            }
          }
          children.add(productButton)




        }
        bottom = productPane


        val animateTimer = AnimationTimer { t =>
          nfa.update(lts)



          //update the lts guiContent
          for (state <- lts.states) {
            if (state.remove) {
              for (node <- state.guiContent) {
                ltsPane.children.remove(node)
              }

              lts.states.remove(lts.states.indexOf(state))
              val tempList = lts.findandRemoveTransitions(state)
              for (transition <- tempList) {
                for (node <- transition.guiContent) {
                  ltsPane.children.remove(node)
                }
              }

            } else {
              for (node <- state.guiContent) {
                ltsPane.children.remove(node)
                ltsPane.children.add(node)
              }
            }
          }

          for (transition <- lts.transitions) {
            if (transition.remove) { //should this only be done when delete is selected?
              lts.removeTransition(transition)
              for (node <- transition.guiContent) {
                ltsPane.children.remove(node)
              }

            } else {
              for (node <- transition.guiContent) {
                ltsPane.children.remove(node)
                ltsPane.children.add(node)
              }
            }
          }

          //update the nfa guiContent
          for (state <- nfa.states) {
            if (state.remove) {
              for (node <- state.guiContent) {
                nfaPane.children.remove(node)
              }

              nfa.states.remove(nfa.states.indexOf(state))
              val tempList = nfa.findandRemoveTransitions(state)
              for (transition <- tempList) {
                for (node <- transition.guiContent) {
                  nfaPane.children.remove(node)
                }
              }

            } else {
              for (node <- state.guiContent) {
                nfaPane.children.remove(node)
                nfaPane.children.add(node)
              }
            }
          }

          for (transition <- nfa.transitions) {
            if (transition.remove) { //should this only be done when delete is selected?
              nfa.removeTransition(transition)
              for (node <- transition.guiContent) {
                nfaPane.children.remove(node)
              }

            } else {
              for (node <- transition.guiContent) {
                nfaPane.children.remove(node)
                nfaPane.children.add(node)
              }
            }
          }

          //update product content
          for (state <- productDiagram.states ) {
            if (state.remove) {
              for (node <- state.guiContent) {
                productPane.children.remove(node)
              }

              productDiagram.states.remove(productDiagram.states.indexOf(state))
              val tempList = productDiagram.findandRemoveTransitions(state)
              for (transition <- tempList) {
                for (node <- transition.guiContent) {
                  productPane.children.remove(node)
                }
              }

            } else {
              for (node <- state.guiContent) {
                productPane.children.remove(node)
                productPane.children.add(node)
              }
            }
          }

          for (transition <- productDiagram.transitions) {
            if (transition.remove) { //should this only be done when delete is selected?
              productDiagram.removeTransition(transition)
              for (node <- transition.guiContent) {
                productPane.children.remove(node)
              }

            } else {
              for (node <- transition.guiContent) {
                productPane.children.remove(node)
                productPane.children.add(node)
              }
            }
          }

        }

        for (node <- lts.guiContent) {
          ltsPane.children.add(node)
        }

        for (node <- nfa.guiContent) {
          nfaPane.children.add(node)
        }

        animateTimer.start()

        //function for taking the current lts and nfa states and creating a new state
        //if the state has already been created from a previous transition then it will find and use that instead
        def merge(lts:State,nfa:State,x:Double,y:Double): State = {

          for(state <- productDiagram.states) {
            if (state.name == lts.name+nfa.name) {
              return state
            }
          }
          val newState = new nfaState(lts.name+nfa.name,lts.initialState,nfa.isAccepted,x,y)




          productDiagram.addState(newState)
          newState
        }

        def updateXCoord(x:Double): Double = {
          x
        }
        def updateYCoord(y:Double): Double = {
          y
        }


        //returns list buffer of all transitions needed
        def product(s:State,q:State,currentX:Double,currentY:Double):Unit = {
          var productCoordX = currentX
          var productCoordY = currentY
          var n = 1
          var transitions = new ListBuffer[ltsTransition]
          val sTransitions = lts.transitions.filter(_.startState == s)
          val qTransitions = nfa.transitions.filter(_.startState == q)
          for (ltsTransition <- sTransitions) {
            for (nfaTransition <- qTransitions) {
              if (nfaTransition.actions.contains(ltsTransition.startState.proposition)) {

                //for the transitions that loop back on themself, in this case we dont it calling product() again
                if (ltsTransition.startState == ltsTransition.endState && nfaTransition.startState == nfaTransition.endState) {
                  productDiagram.transitions.addOne(new ltsTransition(merge(s, q,productCoordX,productCoordY), "", merge(s,q,productCoordX,productCoordY)))
                } else {
                  //if the current state is terminal, Laura was right this needs to be in the loop in order to add the transition
                  if (nfaTransition.endState.isAccepted) {
                    productDiagram.transitions.addOne(new ltsTransition(merge(s, q,productCoordX,productCoordY), "", merge(ltsTransition.endState, nfaTransition.endState,productCoordX+75,productCoordY)))
                    productCoordX = updateXCoord(productCoordX+75)
                  } else {
                    productDiagram.transitions.addOne(new ltsTransition(merge(s, q,productCoordX,productCoordY), "", merge(ltsTransition.endState, nfaTransition.endState,productCoordX+75,productCoordY+(math.pow(-1,n)*n)*25)))
                    productCoordX = updateXCoord(productCoordX+75)
                    productCoordY += (math.pow(-1,n)*n)*25
                    n += 1
                    product(ltsTransition.endState, nfaTransition.endState,productCoordX,productCoordY)
                  }
                }
              }
            }
            //in the case that the current state has no transitions, nothing needs to be done as the state would have been added by the previous iteration
          }
        }

        //could result in being redundant
        //this function should be called on a button press labelled something like "Generate product"
        //creates a product diagram called productDiagram, in the productPane, this needs to be added

        /*
        def drawProduct(): Unit = {
          var productTransitions = computeProduct()
          var existingStates = new ListBuffer[State]

          for (transition <- productTransitions) {
            if (!existingStates.contains(transition.startState)) {
              println("hey")
              productDiagram.addState(transition.startState)
              existingStates.addOne(transition.startState)
              //draw start state to screen
              //add start state to existing states list
            }
            if (!existingStates.contains(transition.endState)) {
              productDiagram.addState(transition.endState)
              existingStates.addOne(transition.endState)
              //draw end state to screen
              //add end state to existing states list
            }
            productDiagram.addTransition(transition)
            //draw transition between startState and endState
            //we need to consider how to space out the states, should this be done when the states listbuffer is created in computeProduct .....attempted there but not sure if it works yet
          }
        }

         */

    }
  }
}
}
  