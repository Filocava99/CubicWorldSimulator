package it.cubicworldsimulator.game.world.block;

import org.joml.Vector2f;

public class BlockTexture {

    private final static int CUBE_FACES = 6;

    private final FaceTexture topFace;
    private final FaceTexture botFace;
    private final FaceTexture leftFace;
    private final FaceTexture rightFace;
    private final FaceTexture frontFace;
    private final FaceTexture backFace;
    private final float step;

    /**
     *
     * @param step
     * @param coords 0 -> top | 1 -> bot | 2 -> left | 3 -> right | 4 -> front | 5 -> back
     */
    public BlockTexture(float step, Vector2f[] coords) {
        if(coords.length != CUBE_FACES){
            throw new IllegalArgumentException("Coords array must be of exactly 6 elements!");
        }
        this.step = step;
        this.topFace = new FaceTexture(new Vector2f(coords[0].x*step,coords[0].y*step),new Vector2f(coords[0].x*step+step,coords[0].y*step),new Vector2f(coords[0].x*step,coords[0].y*step+step),new Vector2f(coords[0].x*step+step,coords[0].y*step+step));
        this.botFace = new FaceTexture(new Vector2f(coords[1].x*step,coords[1].y*step),new Vector2f(coords[1].x*step+step,coords[1].y*step),new Vector2f(coords[1].x*step,coords[1].y*step+step),new Vector2f(coords[1].x*step+step,coords[1].y*step+step));
        this.leftFace = new FaceTexture(new Vector2f(coords[2].x*step,coords[2].y*step),new Vector2f(coords[2].x*step+step,coords[2].y*step),new Vector2f(coords[2].x*step,coords[2].y*step+step),new Vector2f(coords[2].x*step+step,coords[2].y*step+step));
        this.rightFace = new FaceTexture(new Vector2f(coords[3].x*step,coords[3].y*step),new Vector2f(coords[3].x*step+step,coords[3].y*step),new Vector2f(coords[3].x*step,coords[3].y*step+step),new Vector2f(coords[3].x*step+step,coords[3].y*step+step));
        this.frontFace = new FaceTexture(new Vector2f(coords[4].x*step,coords[4].y*step),new Vector2f(coords[4].x*step+step,coords[4].y*step),new Vector2f(coords[4].x*step,coords[4].y*step+step),new Vector2f(coords[4].x*step+step,coords[4].y*step+step));
        this.backFace = new FaceTexture(new Vector2f(coords[5].x*step,coords[5].y*step),new Vector2f(coords[5].x*step+step,coords[5].y*step),new Vector2f(coords[5].x*step,coords[5].y*step+step),new Vector2f(coords[5].x*step+step,coords[5].y*step+step));
    }

    private BlockTexture(float step, FaceTexture topFace, FaceTexture botFace, FaceTexture leftFace, FaceTexture rightFace, FaceTexture frontFace, FaceTexture backFace) {
        this.topFace = topFace;
        this.botFace = botFace;
        this.leftFace = leftFace;
        this.rightFace = rightFace;
        this.frontFace = frontFace;
        this.backFace = backFace;
        this.step = step;
    }

    public FaceTexture getTopFace() {
        return topFace;
    }

    public FaceTexture getBotFace() {
        return botFace;
    }

    public FaceTexture getLeftFace() {
        return leftFace;
    }

    public FaceTexture getRightFace() {
        return rightFace;
    }

    public FaceTexture getFrontFace() {
        return frontFace;
    }

    public FaceTexture getBackFace() {
        return backFace;
    }

    public static class FaceTexture{
        private final Vector2f topLeft;
        private final Vector2f topRight;
        private final Vector2f botLeft;
        private final Vector2f botRight;

        public FaceTexture(Vector2f topLeft, Vector2f topRight, Vector2f botLeft, Vector2f botRight) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.botLeft = botLeft;
            this.botRight = botRight;
        }

        public Vector2f getTopLeft() {
            return topLeft;
        }

        public Vector2f getTopRight() {
            return topRight;
        }

        public Vector2f getBotLeft() {
            return botLeft;
        }

        public Vector2f getBotRight() {
            return botRight;
        }
    }

}
