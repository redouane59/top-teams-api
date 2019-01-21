
# Welcome to TopTeams API
Using [AWS Lambda](https://aws.amazon.com/lambda/?nc1=h_ls) functions and [API Gateway](https://aws.amazon.com/api-gateway/?nc1=h_ls), the module TopTeams API allows you to execute different functions in order to **optimize your team sport activities**.

## Team composition generator
**Generate balanced teams** from a list of available players, each of them identified by a unique id and having an individual rating value between 0 & 100, representing his level. 
This module also let you the choice to define positions for players (*GK*, *DEF*, *ATT*) to balance the profiles in addition to the global level.

## Rating updates calculator
**Calculate the player rating modification** to apply after a game from a given composition and a score.
This algorithm takes into account the global level of the two teams, the final goal average, and the number of games played by each player.

# Swagger
https://app.swaggerhub.com/apis-docs/TopTeams/TopTeams/1.1.0

![Process Summary](https://drive.google.com/file/d/1nr2JZuetCbImPtZxpW57gIN3dGS4Usor/view?usp=sharing)
