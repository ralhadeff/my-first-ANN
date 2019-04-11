# my-first-ANN
A tic-tac-toe android game where the computer's AI is based on a pre-trained ANN, implemented in Java.

The ANN is an implementation based on the Coursera course of prof. Andrew Ng (https://www.coursera.org/learn/machine-learning/home/welcome), using Java (as part of the android app).

Training was done using MATLAB (by user https://github.com/aviadkl; code is not provided here), however the learning was not very successful (training set was not very good at the time, ~2015) so the AI is not particularly intelligent.

Also, the game's visual appearance is not very impressive, because it was not the focus for this project.

Note that the weights can be provided by the player, and they will be read and loaded automatically by the game on start-up.

Example weights are given in the `Theta` folder.

The ANN uses a sigmoid activation function, and has a 9 45 9 architecture (9 inputs and 9 outputs).  
For the inputs, +1 is a player's square occupied and -1 is an enemy's square occupied; 0 is an empty square on the board. Output is which square is the best choice for the next move.
