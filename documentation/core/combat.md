# Combat

The combat module implements the behaviour of the attack interface of the attack method
from the unit class. It should not be called from somewhere else. The only exception maybe unit testing. 

The Module itself contains 2 types of entities. A dice roll and a dice pool.
The dice pool contains just informations and shall be readonly. 
The dice roll implements the behavior how the dice pool shall be handled.
For example: We want to roll to hit. We have 10 attacks so a total dice pool of 10.
We habe to set the according informations like unit and enemy etc.
We take that dice pool and pass it to a dice roll like the hit roll.
The hit roll then decides how the dice pool is handled and produces another dice pool,
which can later be consumed by further steps.

The Idea behind this is to reduce Complexity by divide and conquer the
battle mechanic and also to make this more debuggable by not mutating anything during 
calculations.

 