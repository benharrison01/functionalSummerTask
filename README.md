Please note: This repository has been extracted from a group project on gitlab from Summer 2021. This version serves only as part of my public portfolio.

Our full report for this project can be found on Google Docs with the following link:
https://docs.google.com/document/d/1plzH1enzzQ2AqbZkKaiNjUTG5GhDtTGHBpWN-yDea74/edit?usp=sharing


# Functional Programming Task


## June - August 2020

University of Birmingham

Completed as part of the PwC Summer Engagement Activities.

Special thanks to Vincent Rahli (University of Birmingham) for helping us along the way.

[https://git.cs.bham.ac.uk/rahliv/tda-summer-project-2020-functional](https://git.cs.bham.ac.uk/rahliv/tda-summer-project-2020-functional)

The Team: 

Isaac Horry

Laura Cope

Ben Harrison

**Table of Contents**



# Overview

The goal of this project was to learn about functional programming while building a useful piece of code. We were given the freedom to implement this program in any functional language we found most suitable such as OCaml, Haskell, or Scala. 

There are advantages to each of the languages: Ocaml has a simple syntax; we use Haskell in our Agda-based lectures; Scala is similar to Java and allows using Java classes. 

The project proposed was to build a tool that would help illustrate how model-checking works. Model checking is about formally verifying that abstract models of real-world systems satisfy given properties using mathematical formalisms and logic.

However, the logical aspect of model-checking will not be important in this project, as the goal will be to implement the algorithms and provide visually convenient ways to learn and teach them. Two of the most prominent logical languages used for model-checking are LTL and CTL, which are both described for example in [https://mitpress.mit.edu/books/principles-model-checking](https://mitpress.mit.edu/books/principles-model-checking).

_This overview was taken from the PDF provided to us which described the project._


# Scala and ScalaFx


## Why Scala?

We opted to use Scala because Scala allows you to use Java libraries within its programs. This meant that there was an abundance of documentation online as many tasks could be researched and solved more easily as due to the popularity of Java.

Scala is also functional and object oriented simultaneously, allowing us to create a much more structured solution by creating different classes, e.g. for each model type and state.


## ScalaFx vs Swing

When gathering our initial ideas for the project, we needed to agree upon a library for the user interface. Our main two options for Scala were ScalaFx and Swing. The similarities between these two libraries is that ScalaFx is essentially a wrapper for JavaFx and Scala Swing is essentially a wrapper for Java Swing.

The benefit of Swing is that it has some good documentation since it has existed for longer than ScalaFx. However, there is a question as to its long-term stability. Another minor disadvantage is that Swing can look outdated and ‘ugly’ in comparison to ScalaFx.

On the other hand, ScalaFX is newer and can be used in combination with JavaFx. This is beneficial since if a UI library does not exist in ScalaFx, it may exist in JavaFx instead. An example of this in our project was using an Arrow.java class for making a visual representation of transitions. It is clear that Scala has bindings to the JavaFx packages. A disadvantage however is that ScalaFx’s documentation amounts to just a few tutorials. Being new to Scala and JavaFX, this was slightly problematic, though the advantages far outweigh the disadvantages and as a result, we agreed upon the use of ScalaFx.


# Planning


## Class Diagram


## 



# Setbacks and Alterations

**Here we can discuss anything which slowed us down or we found tricky and how we branched away from the initial project description slightly.**

While working on our project, we experienced a few setbacks which slowed us down or meant that we were unable to complete the project perfectly.

First, our initial plan was quite ambitious. It included the ability to right click and have a pop up menu when settings for different nodes. These hopes were partially inspired by the webapp Draw.io: 

Due to time constraints, we were unable to add these features and instead compromised. When you left click an existing node, you are provided with the option to either delete node (X symbol) or create a transition (link symbol).

As a result of this simplification, we brought the project down to a more basic and achievable level to ensure we covered the specification.

Another setback we faced was based on the UI. Early on, we did not plan a clear user interface architecture meaning it was difficult to change at a later stage.

Although we created class diagrams, we did not consider all the necessities for the complete project and this slowed us down, especially when creating the product function.

In the early stages,we missed some programming principles e.g. naming conventions for classes which was also difficult to change at a later stage.

Furthermore, due to time constraints, we were unable to create a drag and drop feature.. This feature would have allowed us to move states around the screen rather than have them fixed in one place once created. Although this feature would have been a nice touch, it was not necessary for the requirements of the task.


# Project Summary


## GUI

The graphical user interface is the class where all the user interaction happens. The code within this section is minimal. We declare instances of our classes here, so that their visual representations can be viewed on screen: 




The size and layout for the stage are all defined here too. Many user interface controls, such as buttons and input boxes are defined within the lts and nfa classes themselves such that when instantiated, the controls appear on the GUI.


## State

State is an abstract class extended by ltsState and nfaState. Its attributes include the state name, transitions belonging to it, actions, a boolean to determine whether a state is an initial state and its x,y coordinates for the GUI.




getName: A getter method which returns the name of a state. This is particularly useful when generating the product later on, as the names are combined. For example, a state named s0 paired with a state named q0 could be combined to be named s0q0.

getTransition: A getter method which returns a transition belonging to the state.

getTransitions: A getter method which returns a state’s transitions as a list. The list is String, State where the String is used to hold the transition label, and State holds the connecting state.

isInitialState: A boolean which determines whether a state is a starting state.

changeName: A setter method which changes the name of a state. A typical name would be s1.

addTransition: A function which accepts a transition and adds it to a list of transitions for that state.


## ltsState

ltsState is a class which represents a node in an LTS diagram. It extends State.





## nfaState

nfaState is a class which represents a node in an NFA diagram. It extends State.





## ltsTransition

ltsTransition is a class used to represent transitions within the labelled transition system.




It contains a start and end state, action, and coordinated for the line to be drawn onto the GUI.


## LTS

We have created a class for a labelled transition system to be represented by. The class contains attributes for its states, actions and transitions to states, any states defined as initial states, and atomic propositions. As discussed previously, we define relevant controls here such as a checkbox to dictate if a node should be a start node, and a label to change a node’s atomic proposition.

This class extends BaseModel which combines all shared attributes and methods between the LTS class and NFA class.



getAP: A getter method which gets all atomic propositions in the LTS and returns them in a list of type string.

addAP: A setter method which appends a string atomic proposition to the list of propositions.

removeAP: A setter method which removes an atomic proposition from the list.

addTransition: Takes a transition from the Transition class as a parameter and adds it to the listBuffer belonging to the LTS class.

removeTransition: Takes a transition as a parameter and removes it from the LTS.

findAndRemoveTransitions: Takes a state as a parameter and removes all connecting transitions. This method is used when removing a state to ensure there are no stray edges remaining. 


## NFA

We have created a class for an NFA system to be represented by. The class contains attributes for its states, actions and transitions to states, any states defined as initial states, and atomic propositions. As discussed previously, we define relevant controls here such as a checkbox to dictate if a node should be a start node, and a label to change a node’s atomic proposition.




This class also extends BaseModel which combines all shared attributes and methods between the LTS class and NFA class.




Some methods behave in the same way to those in LTS, as above, and some are unique to NFA:



getAcceptStates: A getter method which returns all the accepting states as a list.

addAS: A setter method which adds an accepting state to the NFA.

removeAS: A setter method which removes an accepting state from the NFA.


## Product Function

The product function performs the synchronization of the transitions of the NFA with the states of the LTS. Therefore the algorithm will examine each lts state and its transitions and see whether there is a corresponding nfa transition for the current nfa state that accepts the states atomic proposition. The purpose of this is to see whether the LTS satisfies the system specification proposed by the NFA, if so the accepting state of the NFA will be reachable in the product and this will be shown in our program by highlighting the various routes to a terminal state.




# Our Completed Solution

**Here we can add screenshots and maybe a hyperlink to a video.**


# Next Steps

One limiting factor is that the LTS currently only accepts one value as an atomic proposition. For example, generating a product would fail if a state’s atomic proposition was a set such as {A, B}.

Furthermore, due to a lack of time towards the end of the project, we were unable to complete robust testing. This would be necessary to confirm that the program is stable and still works with large numbers of nodes.

It is also hard to see where the lines are coming from in some product diagrams. Increasing the spacing between states would help but it is not a long term solution as increasing the number of product states would still allow this issue to arise. 
