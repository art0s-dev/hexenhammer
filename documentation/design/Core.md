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

So That means Units are just a profile and a bunch of weapons with a nice
abstraction. 

The profile on the other hand is the sheet the weapons and the units are compared against.
This is why the Profile sheets are named "enemy" or the name of the enemy unit.

So at the core we just have 3 Entities:
- Unit (The Unit we want to eval)
- Weapon (The Damage attached to the unit)
- Profile (enemy sheet)

### But the implementation is sooo loooooooong
The implementation of the battle Sequence is so long because 
the core mechanics shall be encapsulated in one method so we don't have to 
maintain many methods and they are not spreaded anywhere.
Also: it's nice and simple `Unit.attacks(enemy)` isn't it?
Still not convinced? Please read John Ousterhout's principles of software design.
[John Ousterhout's Book](https://web.stanford.edu/~ouster/cgi-bin/book.php) 

## About the special rules

Yeah i know, Wargames are niche. And the Tomes the rules come in are heavy.
So we try to keep it simple and have 4 Groups of special Rules:
- Unit (These Special Rules apply to the unit at a global scale - these affect all weapons)
- Weapon (This Ruleset only applies to the Weapon fired during combat)
- Profile (These Rules apply to the enemy profile and always apply to the battle sequence)
- Unit Specific (These apply to only one type of unit and i will hesitate to implement these.
I want the user to model his units with the upper 3 Special rules)

