# Progetto di Ingegneria del Software 2020
# ing-sw-2020-cojocaru-colasanti-d-angelo

## Gruppo AM17

- ### Cojocaru - Cristian - 868087 - 10537857 - <br>cristian.cojocaru@mail.polimi.it
- ### Colasanti - Luca - 891653 - 10627030 - <br>luca.colasanti@mail.polimi.it
- ### D'Angelo - Stefano - 890033 - 10607664 - <br>stefano4.dangelo@mail.polimi.it

### Launch instructions
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
| Basic rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Advanced Gods | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Undo | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

###### Bug report
A few bugs could show up when all the players disconnect (due to an incomplete implementation of an unrequested requirement) or when (in the GUI) the player is requested to choose his color. Also, in the GUI there's an incomplete functionality, that allows to visualize every player in the game together with their respective card (everything works fine in the CLI).
