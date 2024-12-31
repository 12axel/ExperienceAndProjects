| #   | Done | Description                                                                                                             |
|:----|:----:|:------------------------------------------------------------------------------------------------------------------------|
| 1   |  X   | Call placeCreature with placing a creature on an occupied hex                                                           |
| 2   |  X   | Call placeCreature with placing a creature that was never in a player's inventory                                       |
| 3   |  X   | Call placeCreature to place a creature that is out of stock from a player's inventory                                   |
| 4   |  X   | Call placeCreature to perform an invalid first blue player creature placement                                           |
| 5   |  X   | Call placeCreature to perform a valid first blue player creature placement                                              |
| 6   |  X   | Call placeCreature to perform an invalid first red player creature placement                                            |
| 7   |  X   | Call placeCreature to perform a valid first red player creature placement                                               |
| 8   |  X   | Call placeCreature to perform an invalid player creature placement beyond their first creature placement                |
| 9   |  X   | Call placeCreature to perform valid player creature placement beyond their first creature placement                     |
| 10  |  X   | Call placeCreature with untimely blue player butterfly placement                                                        |
| 11  |  X   | Call placeCreature with timely blue player butterfly placement                                                          |
| 12  |  X   | Call placeCreature with untimely red player butterfly placement                                                         |
| 13  |  X   | Call placeCreature with timely red player butterfly placement                                                           |
| 14  |  X   | Call placeCreature with a creature placement causing the blue butterfly to be surrounded                                |
| 15  |  X   | Call placeCreature with a creature placement causing the red butterfly to be surrounded                                 |
| 16  |  X   | Call placeCreature to place a creature with the butterfly already surrounded                                            |
| 17  |  X   | Call moveCreature to move a creature from a hex to the same hex                                                         |
| 18  |  X   | Call moveCreature to move a creature from an empty hex                                                                  |
| 19  |  X   | Call moveCreature to move a creature that is not the same as the one on the 'from' hex                                  |
| 20  |  X   | Call moveCreature to move a creature to an occupied hex                                                                 |
| 21  |  X   | Call moveCreature to move a creature that does not belong to the current player                                         |
| 22  |  X   | Call moveCreature to move a butterfly creature beyond its maximum distance                                              |
| 23  |  X   | Call moveCreature to move a butterfly creature to a hex where going to it would break connectedness along the way       |
| 24  |  X   | Call moveCreature to move a butterfly creature to a hex when it cannot be dragged there                                 |
| 25  |  X   | Call moveCreature to perform a valid butterfly creature move                                                            |
| 26  |  X   | Call moveCreature to move a walking creature beyond its maximum distance                                                |
| 27  |  X   | Call moveCreature to move a walking creature to a hex where going to it would break connectedness along the way         |
| 28  |  X   | Call moveCreature to move a walking creature to a hex when it cannot be dragged there                                   |
| 29  |  X   | Call moveCreature to perform a valid walking creature move                                                              |
| 30  |  X   | Call moveCreature to move a flying creature beyond its maximum distance                                                 |
| 31  |  X   | Call moveCreature to move a flying creature when all its neighbors are occupied                                         |
| 32  |  X   | Call moveCreature to move a flying creature to a hex where connectivity gets broken                                     |
| 33  |  X   | Call moveCreature to perform a valid flying creature move                                                               |
| 34  |  X   | Call moveCreature to move a jumping creature beyond its maximum distance                                                |
| 35  |  X   | Call moveCreature to move a jumping creature to a hex that is not linear to its current position                        |
| 36  |  X   | Call moveCreature to move a jumping creature to a hex where connectivity gets broken                                    |
| 37  |  X   | Call moveCreature to perform a valid jumping creature move                                                              |
| 38  |  X   | Call moveCreature to move a running creature along a path with a number of steps that do not equal its maximum distance |
| 39  |  X   | Call moveCreature to move a running creature to a hex where going to it would break connectedness along the way         |
| 40  |  X   | Call moveCreature to move a running creature to a hex when it cannot be dragged there                                   |
| 41  |  X   | Call moveCreature to perform a valid running creature move                                                              |
| 42  |  X   | Call moveCreature to perform a valid creature move when the blue player has not placed their butterfly timely           |
| 43  |  X   | Call moveCreature to perform a creature move when the blue player has placed their butterfly timely                     |
| 44  |  X   | Call moveCreature to perform a valid creature move when the red player has not placed their butterfly timely            |
| 45  |  X   | Call moveCreature to perform a valid creature move when the red player has placed their butterfly timely                |
| 46  |  X   | Call moveCreature with a creature movement causing the blue butterfly to be surrounded                                  |
| 47  |  X   | Call moveCreature with a creature movement causing the red butterfly to be surrounded                                   |
| 48  |  X   | Call moveCreature with a creature movement causing both players' butterflies to be surrounded                           |
| 49  |  X   | Call moveCreature to move a creature after a butterfly is already surrounded                                            |


Patterns I used throughout this project:

To begin, the first pattern that I used was the builder pattern in my HexAroundGameBuilder class.
The HexAroundGameBuilder class utilizes the Builder pattern to simplify the process of creating a HexAroundGameManager. 
Using the builder pattern there allows the set up of various game components like creature definitions, and player inventories that are only configured at runtime and may vary depending on the game's setup in a step-by-step manner. 
This approach not only enhances readability and maintainability, but also facilitates customization of the game setup. Another pattern that I used throughout this project was the factory pattern in my PlayerFactory and CreatureFactory classes.
Through the use of those factories within the buildGameManager method, the logic of creature creation and player inventory setup is delegated to these specific classes. 
Because of this, both classes only have to deal with a single responsibility which results in a streamlined game setup process and code being more maintainable.
The final pattern that I used throughout this project is the strategy pattern, which is used in my classes that relate to the creatures' movement types (Flying, Jumping, Running, and Walking).
Each of these movement types is encapsulated as a strategy, 
and this allows creatures to exhibit different movement behaviors. This implementation enhances flexibility and reusability due to the fact that new movement strategies can be seamlessly integrated or existing ones modified without requiring 
to modify the creature's core code.

The resources I used to assist me with the completion of this project:

https://refactorizando.com/en/java-supplier-interface-and-factory-pattern

https://medium.com/@dev.thiago/factory-pattern-with-map-and-lambda-in-java-70e5383b8f20/

https://www.geeksforgeeks.org/function-interface-in-java-with-examples

My solution to the coordi-nation assignment, with the modifications described in my HexCoordinate class

https://refactoring.guru/design-patterns/strategy/java/example

https://www.geeksforgeeks.org/strategy-pattern-set-1

https://medium.com/@akshatsharma0610/strategy-design-pattern-in-java-6ee96f87d807

https://www.redblobgames.com/grids/hexagons/

https://www.javatpoint.com/factory-method-design-pattern

https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/

https://www.baeldung.com/java-depth-first-search

https://refactoring.guru/design-patterns/builder/java/example

Office hour (13-Dec-2023)

https://www.geeksforgeeks.org/supplier-interface-in-java-with-examples/

https://www.linkedin.com/advice/0/what-benefits-using-factory-pattern-skills-software-engineering#:~:text=The%20Factory%20pattern%20can%20offer,a%20single%20responsibility%20and%20purpose

https://www.freecodecamp.org/news/a-beginners-guide-to-the-strategy-design-pattern/#:~:text=In%20simpler%20terms%2C%20the%20Strategy,changing%20the%20object's%20core%20code












