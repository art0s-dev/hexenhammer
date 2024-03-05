# Overview

## Modules

### Core
The core contains all parts needed for implementing the combat mechanics.
Contains mostly entities which can be combined to simulate an attack.

#### Combat
The Combat package should combine the functionality to make an attack against the 
enemy unit.

### Arch
This is the scaffolding of the application. It's mainly filled with
Interfaces which get implemented in the namespaces enemy, user and weapon.

### Specific Packages
These Packages are enemy, user and weapon. They all Implement the 
interfaces given in the arch package. Each Package consists of a view,
which extends the base view and gets a controller and a repository.

### Utils
This package mainly consists of stuff that is genreally useful and 
removes some boilerplate code

## Development
The Software is developed via TDD. We use Junit and Mockito in order to provide meaningful tests.
You can find the test suite in the corresponding maven test directory.

## Code Conventions
We go along with the typical Java conventions. We don't use the standart formatter
because sometimes the formatter breaks enums up into long lists. So partial 
formatting is ok. 80 characters width for the editing space shall be given 
as a convention but overstepping it sometimes is OK, if most of the
code is behind the 80 characters.

For small operations like switching over a var, the tenary operator is fine.
If its just one var or the other we use tenarys. For everything bigger
we try to use the appropriate language constructs.

