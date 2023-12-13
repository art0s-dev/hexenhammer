# Overview

## Modules

### Core
The core contains all parts needed for implementing the combat mechanics.
Contains mostly entities which can be combined to simulate an attack.

#### Combat
The Combat package should combine the functionality to make an attack against the 
enemy unit. Should be used as a controller

#### DicePool
contains all necessary rules for determining combat damage

## Development
The Software is developed via TDD. We use Junit and Mockito in order to provide meaningful tests.
You can find the test suite in the corresponding maven test directory.

## Code Conventions
We go along with the typical Java conventions. We don't use the standart formatter
because sometimes the formatter breaks enums up into long lists. So partial 
formatting is ok. 80 characters width for the editing space shall be given 
as a convention but overstepping it sometimes is OK, if most of the
code is behind the 80 characters.