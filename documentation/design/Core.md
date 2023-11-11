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

## About the special rules

Yeah i know, Wargames are niche. And the Tomes the rules come in are heavy.
So we try to keep it simple and have 4 Groups of special Rules:
- Unit (These Special Rules apply to the unit at a global scale - these affect all weapons)
- Weapon (This Ruleset only applies to the Weapon fired during combat)
- Profile (These Rules apply to the enemy profile and always apply to the battle sequence)
- Unit Specific (These apply to only one type of unit and i will hesitate to implement these.
I want the user to model his units with the upper 3 Special rules)


