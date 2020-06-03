package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector3f;

import it.cubicworldsimulator.engine.GameItem;

public class PlayerModel extends GameItem implements Observer {
	
	public PlayerModel(Mesh mesh) {
		super(mesh);
	}
	
	public PlayerModel() {
		super();
	}
	
	@Override
	public void update(Vector3f position, Vector3f rotation) {
		this.setPosition(position);
		//this.setRotation(rotation);
	}
}
