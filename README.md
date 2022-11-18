# GwentStone (POO-Homework-01)

This repository is intended to store a complex card game simulator. It is part of a computer
science assignment (2nd year, 1st semester).

Full completion date: 18 Nov. 2022

Deadline: 24 Nov. 2022

This project will be available <a href="https://github.com/w1bb/POO-Homework-01">on GitHub</a> once the deadline passes.
The original homework is available on <a href="https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema">our OCW</a>, but a
copy of the text will be provided in the repo.
## License

Once the deadline passes, this project will be available under the MIT license. For more info about the author of the
code, please check out <a href="https://v-vintila.com">my personal website</a>.

## Coding style choices

There are a few design choices I want to address:
* The methods and variables will follow
<a href="https://www.oracle.com/java/technologies/javase/codeconventions-fileorganization.html">the Oracle</a> file
organization convention, so:
  1) Class (static) variables: First the public class variables, then the protected, and then the private.
  2) Instance variables:First public, then protected, and then private.
  3) Constructors
  4) Methods: These methods should be grouped by functionality rather than by scope or accessibility. For example, a
  private class method can be in between two public instance methods. The goal is to make reading and understanding the
  code easier.
* Cards should to follow this naming convention:
  * The original Card class is just that, Card
  * Types of Card should be named TypeCard, as in MinionCard, HeroCard or EnvironmentCard;
  * Variants of types of cards should be named TypeCard_Variant, as in MinionCard_TheCursedOne or HeroCard_LordRoyce.
  
  This naming convention WILL raise an error by the checkstyle, but it makes the code so much easier to follow.

## Documentation

In short, the control is given to an interpreter that "understands" the commands and translates them to actual gameplay.

Everytime the interpreter is asked to follow a request, the current game, players and used cards are the ones being
manipulated, calling the true methods (e.g.: giving the "useHeroAbility" command will result in the card's
tryUseAbility() method to be called).

If there are any errors, an instance of ErrorType will be returned. Every error can be converted to a string that will
represent a brief description of the issue. Some problems are marked CRITICAL - these were generated by me in order to
extend future functionality possibilities.

This makes the flow of the program very easy to read and understand. For more info, please take a quick look at the
code, as it is **very** well documented.

### execution.Interpreter

The interpreter passes these commands to various Game, Player and Card instances, bridging text and functionality.

#### Important public methods:
* Interpreter(): The constructor parses parts of the given input and stores them for later use.
* runGames(): This method interprets each game's commands.
* getOutput(): Once all games have been run, an output can be produced.
#### Important private methods:
* runGame(): This method interprets a single game's commands.
* Each command has its own unique method (e.g.: the "cardUsesAttack" command is bound to the "interpretCardUsesAttack"
method).

### execution.Game

A Game instance also holds information about the current state of the board, meaning all the cards that were placed
before are stored here.

#### Important methods:
* Game(): Every game begins by creating a Game instance.
* endTurn(): Called once one of the players signals to end his turn.
* pushOnBoardRow(): Pushes a card on a given row if and only if it has enough space to accommodate it.

#### Output methods

There are output methods that have been created to compute various json output formats. These include boardToArrayNode()
and boardFrozenToArrayNode().

### execution.Player

Players are the main component of any game - you <a href="https://en.wikipedia.org/wiki/Zero-player_game">usually</a>
need at least a player in order to have a fun and entertaining game. The players will be created once enough data is
read from the input, meaning once the decks of cards are known.

#### Important methods:
* Player(): Every player can be instantiated, but, in the context of this game, only two such instances will ever be
  created. If a new game begins, no other player will be instantiated - the current players will be reset instead.
* reset(): This method resets the basic values of a Player when a new game begins.
* placeCardOnBoard(): This method allows any player to place a certain minion card from their hand on the board. Of
  course, many exceptions are treated as well.

#### Output methods

There are output methods that have been created to compute various json output formats. These include
currentHandToArrayNode() and currentHandEnvironmentToArrayNode().

### execution.Deck

This represents a deck of cards. They can be shuffled and drawn as per players' needs.
#### Important methods:
* Deck(): A deck can be instantiated if there is a known array of cards that should be considered a pack.
* shuffle(): This method shuffles the cards based on a shuffle seed.
* drawCard(): This method smartly draws a card (it doesn't actually remove it, an index is increased instead).

#### Output methods

There are output methods that have been created to compute various json output formats. These include toArrayNode().

### execution.cards.Card
...and all of its extensions, including HeroCard, MinionCard and EnvironmentCard, as well as their sub-extensions
(HeroCard_EmpressThorina, HeroCardGeneralKocioraw, ..., MinionCard_Berserker, ..., EnvironmentCard_Firestorm, ...).

#### Important methods:
* The most interesting methods are part of the extended classes, and they are unique to the card they represent.
* Please check out the code for more info.

#### Output methods

There are output methods that have been created to compute various json output formats. These include toObjectNode().
