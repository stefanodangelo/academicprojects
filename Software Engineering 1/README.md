# Software Engineering Project 2020
# ing-sw-2020-cojocaru-colasanti-d-angelo

## AM17 Group

- ### Cojocaru Cristian <br>cristian.cojocaru@mail.polimi.it
- ### Colasanti Luca <br>luca.colasanti@mail.polimi.it
- ### D'Angelo Stefano <br>stefano4.dangelo@mail.polimi.it

### Launch instructions
###### YOU NEED TO INSTALL jdk IN YOUR PC OR LAPTOP TO EXECUTE THE .jar FILES
###### Both client.jar and server.jar file are located in */deliverables/jar*

To launch the client .jar from command prompt use this syntax:
```
java -jar client.jar [-ip] <ip address> [-ip] <port> [-v] {cli | gui}
```
N.B.: Use the commands in brackets only if you know the server's ip address and port.

To launch the server .jar from command prompt use this syntax:
```
java -jar server.jar [-ip] <port>
```
### Implementation table
| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![GREEN](http://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](http://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](http://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](http://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](http://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![RED](http://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![RED](http://placehold.it/15/f03c15/f03c15)](#) |
| Advanced Gods | [![GREEN](http://placehold.it/15/44bb44/44bb44)](#) |
| Undo | [![GREEN](http://placehold.it/15/44bb44/44bb44)](#) |

<!--
[![RED](http://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](http://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](http://placehold.it/15/44bb44/44bb44)](#)
-->

###### Bug report
A few bugs could show up when all the players disconnect (due to an incomplete implementation of an unrequested requirement) or when (in the GUI) the player is requested to choose his color. Also, in the GUI there's an incomplete functionality, that allows to visualize every player in the game together with their respective card (everything works fine in the CLI).

### Copyright
This project has been developed in collaboration with [Politecnico di Milano](https://polimi.it/) and [Cranio Creations](http://www.craniocreations.it/) without any profit purpose.
