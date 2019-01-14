# my-first-ANN
A tic-tac-toe android game where the computer's AI is based on a pre-trained ANN

The ANN is a simple implementation based on the coursera course of Andrew Ng (https://www.coursera.org/learn/machine-learning/home/welcome), using Java (as part of the android app)

Training was done using matlab (by https://github.com/aviadkl, code not provided), however the learning was not very successful (training set was not very good at the time, ~2015) so the AI is not particularly intelligent.

Also, game appearance is not very impressive, because it was not the focus of this project.

Do note that weights can be provided by the player, and they will be automatically loaded by the game on start-up.

Example weights are given in the Theta folder.

ANN uses a sigmoid activation function, and has a 9 45 9 architecture (9 inputs and 9 outputs).  
For the inputs, +1 is player's squares and -1 for enemy squares, 0 for empty. Output is which square is the best choice.
