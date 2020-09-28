# Tic Tac Toe
This example app uses a Play framework backend and [Scala.js](https://www.scala-js.org/) / [html.scala](https://github.com/GlasslabGames/html.scala) frontend. 
The project structure utilizes a multi-project Scala.js setup and shares code (API contracts, data transfer object) from the ./shared project folder between client and server. 

## Running
- From the project root, run `sbt` from a command line
- Enter the command `server/run`
- Open a browser to http://localhost:9000