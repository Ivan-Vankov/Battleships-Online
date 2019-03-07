﻿# Battleships Online

## Правила на играта

-	Играта се играе от двама играчи.
-	Всеки играч разполага с игрално поле, което се състои от 10x10 клетки. Редовете са обозначени с буквите от A до J, а колоните са номерирани с числата от 1 до 10.
-	Всеки играч има на полето си:
    -	1 кораб, състоящ се от 5 клетки;
    -	2 кораба, състоящи се от 4 клетки;
    -	3 кораба, състоящи се от 3 клетки;
    -	4 кораба, състоящи се от 2 клетки;
-	В началото на играта, всеки играч разполага корабите си на полето, като те могат да са само в права линия (хоризонтално или вертикално)
-	Целта на всеки играч е да уцели корабите на противника си, като играчите се редуват и всеки има право на един изтрел на ход.
    -	Играчът на ход подава координатите на клетката, по която стреля, и като отговор получава индикация, дали е уцелил или не, и ако е уцелил, дали корабът е потопен.
    -	За да е потопен даден кораб, трябва да са уцелени всичките му клетки.
-	Играта приключва, когато някой от играчите остане без кораби.

## Game Server

Сървъра притежава следните функционалности:

-	Създаване на игра
-	Извеждане на списък с всички игри, активни в момента, с информация дали играта е започнала и броя на играчите в нея.
-	Присъединяване към вече създадена игра (всяка игра трябва да има уникален идентификатор), ако има свободно място.
-	Присъединяване към случайна игра, в която има място.
-	Запазване на състоянието на играта, в която сме в момента.
-	Извеждане на всички запазени игри, в които сме участвали.
-	Възстановяване на запазена игра и присъединяване към нея.
-	Изтриване на запазена игра.

## Game Client

-	Създаване на игра

```bash
$ java bg/sofia/uni/fmi/mjt/battleships/user/BattleshipUser gosho

# Извеждане на възможните команди
Available commands:
	create-game <game-name>
	join-game [<game-name>] // ако липсва името, присъединяване към случайна игра.
	saved-games
	load-game <game-name>
	delete-game
	...

menu> create-game my-game
Game <my-game> created, players 1/2
```

-	Присъединяване към игра

```bash
$ java bg/sofia/uni/fmi/mjt/battleships/user/Battleships tosho

# извеждане на възможните команди

menu> list-games
| NAME     | CREATOR | STATUS      | PLAYERS |
|----------+---------+-------------+---------|
| my-game  | pesho   | pending     | 1/2     |
| my-game-2| gosho   | in progress | 2/2     |

menu> join-game my-game
Successfully joined game <my-game>
Game is ready to start!
Type <start> to continue
```
-	Въвеждане на ход

```bash

       YOUR BOARD
   1 2 3 4 5 6 7 8 9 10
   _ _ _ _ _ _ _ _ _ _
A |_|*|_|-|_|_|_|*|*|_|                         Legend:
B |_|*|_|_|_|_|_|_|_|_|				* - ship field
C |_|*|_|_|_|_|_|_|_|_|				X - hit ship field
D |_|X|_|_|*|*|*|_|-|_|				О - hit empty field
E |_|_|_|_|_|_|_|_|_|_|
F |_|_|-|_|_|_|-|_|_|_|
G |_|_|_|_|_|_|_|_|_|_|
H |_|*|_|_|_|X|X|X|X|_|
I |_|*|_|_|_|_|_|_|_|_|
J |_|*|_|_|_|_|_|_|_|_|


      ENEMY BOARD
   1 2 3 4 5 6 7 8 9 10
   _ _ _ _ _ _ _ _ _ _
A |_|_|_|_|_|_|_|_|_|_|
B |_|_|_|_|_|_|_|_|-|_|
C |_|-|_|_|_|_|-|_|_|_|
D |_|_|_|_|_|_|_|_|_|_|
E |_|_|_|-|_|_|_|_|-|_|
F |_|_|_|_|_|_|_|_|_|_|
G |_|_|_|_|_|_|_|X|_|_|
H |_|_|X|X|X|_|_|X|_|_|
I |_|_|_|_|_|_|_|X|_|_|
J |_|_|_|_|_|_|_|X|_|_|

Enemy fired at: D9 and missed
```
