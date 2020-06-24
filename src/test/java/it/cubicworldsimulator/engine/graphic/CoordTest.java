package it.cubicworldsimulator.engine.graphic;

import static org.junit.Assert.assertEquals;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.junit.Test;

public class CoordTest {

	Player player = new Player(new Vector3f(6,35,-12));
	Vector3i playerCoord = new Vector3i((int)Math.floor(this.player.getPosition().x) , 
			(int)Math.floor(this.player.getPosition().y), (int)Math.floor(this.player.getPosition().z));
	
	@Test
	public void checkPlayerCoordToBlockCoord() {
		Vector3i result = this.player.playerCoordToBlockCoord(this.playerCoord);
		assertEquals(new Vector3i(6,35,3), result);
	}
	
	@Test
	public void checkPlayerCoordToChunkCoord() {
		Vector3i result = this.player.playerCoordToChunkCoord(this.player.getPosition());
		assertEquals(new Vector3i(0,2,-1), result);
	}
	
}
