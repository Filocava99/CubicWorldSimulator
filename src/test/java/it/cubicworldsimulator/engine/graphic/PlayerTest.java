package it.cubicworldsimulator.engine.graphic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joml.Vector3f;
import org.junit.Test;

public class PlayerTest {
	
	@Test
	public void checkCoordUpdate() {
		Player player = new Player(new Vector3f(0,0,0));
		Camera camera = new Camera(new Vector3f(0,0,0));
		player.attach(camera);
		player.movePosition(1, 1, 1);
		assertEquals(player.getPosition(), camera.getPosition());
	}
	
	@Test
	public void checkRotationUpdate() {
		Player player = new Player(new Vector3f(0,0,0));
		Camera camera = new Camera(new Vector3f(0,0,0));
		player.attach(camera);
		player.moveRotation(2, 2, 2);
		assertEquals(player.getRotation(), camera.getRotation());
	}
	
	@Test
	public void checkChangingChunk() {
		Player player = new Player(new Vector3f(0,0,0));
		player.movePosition(100, 100, 100);
		assertTrue(player.didPlayerChangedChunk());
	}
	
}
