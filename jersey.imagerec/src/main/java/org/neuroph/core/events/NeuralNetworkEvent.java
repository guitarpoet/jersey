package org.neuroph.core.events;

import org.neuroph.core.NeuralNetwork;

/**
 * 
 * @author zoran
 */
public class NeuralNetworkEvent extends java.util.EventObject {

	private static final long serialVersionUID = -7287496378870278546L;

	public NeuralNetworkEvent(NeuralNetwork source) {
		super(source);
	}

}