package org.neuroph.core.events;

import org.neuroph.core.NeuralNetwork;

/**
 * 
 * @author zoran
 */
public class NNCalculatedEvent extends NeuralNetworkEvent {

	private static final long serialVersionUID = 382266205247974277L;

	public NNCalculatedEvent(NeuralNetwork source) {
		super(source);
	}

}
