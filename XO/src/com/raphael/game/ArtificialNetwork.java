package com.raphael.game;

import java.util.ArrayList;

public class ArtificialNetwork {

	ArrayList<ArtificialLayer> layers;
	ArtificialLayer inputLayer;
	ArtificialLayer outputLayer;

	public ArtificialNetwork(int... neurons) {
		layers = new ArrayList<ArtificialLayer>();
		inputLayer = new ArtificialLayer(neurons[0], null);
		ArtificialLayer previous = inputLayer;
		// run and build network
		for (int i = 1; i < neurons.length-1; i++) {
			ArtificialLayer current =new ArtificialLayer(neurons[i], previous); 
			layers.add(current);
			previous = current;
		}
		outputLayer = new ArtificialLayer(neurons[neurons.length - 1], previous);
	}
	
	public float[] run(float[] inputs){
		// run input layer
		inputLayer.set(inputs);
		ArtificialLayer previous = inputLayer;
		// propagate through all hidden layers
		for (ArtificialLayer layer : layers) {
			layer.compute(layer.weighInputs(previous.outputs));
			previous = layer;
		}
		// and get the final outputs
		outputLayer.compute(outputLayer.weighInputs(previous.outputs));
		// return outputs
		return outputLayer.outputs; 
	}

}
