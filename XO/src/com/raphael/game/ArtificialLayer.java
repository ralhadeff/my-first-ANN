package com.raphael.game;

public class ArtificialLayer {

	// weights[i][j] is the weight of the output of i going into j
	// weights include bias (index 0)
	float[][] weights;
	float[] outputs;
	// size excludes bias
	int size;

	public ArtificialLayer(int size, ArtificialLayer previous) {
		// a network is built forward
		this.size = size;
		// size+1 for bias, previous.size+1 is only so that the numbering
		// doesn't start from 0 and will be confusing
		if (previous != null){
			weights = new float[size + 1][previous.size + 1];
			// populate weights with random numbers between 0 and 1
			for (int i = 0; i<size + 1; i++){
				for (int j = 0 ; j< previous.size +1 ; j++){
					weights[i][j] = World.random.nextFloat()-1;
				}
			}
		}
		// create array for outputs
		outputs = new float[size+1];
		// if previous is null, this is the input layer and it doesn't need weights
	}

	public void compute(float[] weightedInputs) {
		// take weighted inputs and apply the sigmoid function to them
		// bias
		outputs[0] = 1;
		// neurons
		for (int i = 1; i<=size; i++){
			outputs[i] = (float) (1/(1+Math.exp(-weightedInputs[i])));
		}
	}
	
	public void set(float[] inputs) {
		// take weighted inputs and apply the sigmoid function to them
		// bias
		// neurons
		for (int i = 0; i<=size; i++){
			outputs[i] = inputs[i];
		}
	}
		
	public float[] weighInputs(float[] inputs){
		// array of weighted inputs, start from 1 for convenience
		float[] weightedInputs = new float[size+1];
		// loop for neurons in THIS layer:
		for (int i = 1; i<=size; i++){
			weightedInputs[i] = 0;
			// loop for neurons in PREVIOUS layer (including bias)
			for (int j = 0 ; j<inputs.length; j++){
				weightedInputs[i]+=inputs[j]*weights[i][j];
			}
		}
		return weightedInputs;
	}

}
