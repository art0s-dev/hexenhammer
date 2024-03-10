# Core Concepts

The Core is an implementation of the WH40k Combat Mechanic. The Goal of this
implementation is to provide the user with a precise damage calculation
with an interface that matches the domain language. The idea here is
to emulate the battle sequence as close as possible. Equip units,
attack enemy and so forth. 

To get familiar with the Warhammer 40k Battle sequence i strongly
suggest to play a game of 40k in your local gaming score. With that 
you get enough domain knowledge. 

If you dont have the time or you don't have a friendly local
gaming store you can check this address out.
[WH40k Rulebook](https://www.warhammer-community.com/2023/06/02/download-the-new-warhammer-40000-rules-for-free-right-here/)

## The Battle Sequence
The Battle Sequence is the heart of the application.
In Real Warhammer40k you have a unit and the unit can have cerain equipment.
Like you have a troup of 5 guys and one can get a flamethrower.
BUT in the battle step it all boils down to: 
- you select your unit
- you select a weapon from the unit
- you shoot at a target
- and you repeat until all weapons are resolved

So That means Units are just a bunch of weapons withg some special rules and a nice
abstraction. 

The profile on the other hand is the sheet the weapons and the units are compared against.
This is why the Profile sheets are named "enemy" or the name of the enemy unit.

So at the core we just have 3 Entities at the surface level:
- Unit (The Unit we want to eval)
- Weapon (The Damage attached to the unit)
- Profile (enemy unit sheet)

## About the special rules

Yeah i know, Wargames are niche. And the tomes the rules come in are heavy.
So we try to keep it simple and have 4 Groups of special Rules:
- Unit (These Special Rules apply to the unit at a global scale - these affect all weapons)
- Weapon (This Ruleset only applies to the Weapon fired during combat)
- Profile (These Rules apply to the enemy profile and always apply to the battle sequence)
- Unit Specific (These apply to only one type of unit and i will hesitate to implement these.
I want the user to model his units with the upper 3 Special rules)


