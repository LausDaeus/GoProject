# Interactive Go Life & Death Player

This project is an interactive client that allows you to play "Life and Death" puzzles in the game of [Go](https://en.wikipedia.org/wiki/Go_(game)). It features several types of AIs that use various game trees such as minimax, alpha-beta and optimised variants of these, together with some pattern matching. It is currently capable of handling small to mid-sized puzzles.

![example](doc/example.png)

## Running the Player

The latest version of the player can be played by running the JAR in the main directory like so:

```
java -jar GoProblemSolver.jar
```

To actually play a game you can create a puzzle or load an existing one under:
```
src/saveData/boards/
```
You can then choose to either have human-vs-human, AI-vs-human or AI-vs-AI play. In AI-vs-AI play you can watch the AIs try outplay each other move by move.
Lastly it is possible to configure the AIs game trees and their parameters in the player to observe their behavioural changes.


### Prerequisites

To run the player you will need the JRE version >=7.

If you want to recompile the player from source, you will also need [Ant](http://ant.apache.org/) and then execute the following command in the base directory on "build.xml":
```
ant compile jar
```

## Running player's tests

If you want to run the player's unit tests (to check for correctness) you use the following Ant command:
```
ant junit
```
or for a more verbose variant:
```
ant junitreport
```

## Authors

* [NiklasZ](https://github.com/NiklasZ)
* [eilidhbagel](https://github.com/eilidhbagel)
* [Scott Hood](https://github.com/hoodscott)
* [Kiril](https://github.com/kirilhristov91)
* [jamie00170](https://github.com/jamie00170)
