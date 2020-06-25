package it.cubicworldsimulator.engine.graphic;

public interface Observable {
	
	/**
	 * Insert a new observer in the list
	 * @param observer 
	 */
	public void attach(Observer observer);
}
