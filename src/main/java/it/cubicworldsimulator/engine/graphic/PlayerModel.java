package it.cubicworldsimulator.engine.graphic;

import org.joml.Vector3f;

import it.cubicworldsimulator.engine.GameItem;

public class PlayerModel extends GameItem implements Observer {

	@Override
	public void update(Vector3f position, Vector3f rotation) {
		this.setPosition(position);
		this.setRotation(rotation);
	}
}
