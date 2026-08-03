USMAN AND DWYNE - SYST 17796 BLACKJACK PROJECT
================================================

PROJECT OVERVIEW
This NetBeans Java project expands the SYST 17796 starter code into a playable
one-player Blackjack game against a dealer. The player begins with $100.00 and
can place a wager, hit, stand, split one pair into two hands, continue playing,
or cash out after a completed round. The dealer hits below 17 and stands on 17
or higher. A natural Blackjack pays 3:2.

RUN THE GAME IN NETBEANS
1. Open the Usman_Dwyne_Blackjack project folder in NetBeans.
2. Select Run Project.
3. The main class is ca.sheridancollege.project.BlackjackApplication.

RUN FROM A TERMINAL WITH ANT
1. Open a terminal in this project folder.
2. Run: ant clean jar
3. Run: java -jar dist/Usman_Dwyne_Blackjack.jar

RUN THE INCLUDED TESTS
Compile the source and test folders, then run:
- ca.sheridancollege.project.BlackjackModelTest
- ca.sheridancollege.project.ScriptedGameDemo

The model test checks deck size and order, ace values, bust boundaries, pair
recognition, betting, splitting, and dealer behaviour. The scripted demo runs a
repeatable full round that includes betting, splitting, hitting, standing,
dealer play, result comparison, balance settlement, and cashing out.
