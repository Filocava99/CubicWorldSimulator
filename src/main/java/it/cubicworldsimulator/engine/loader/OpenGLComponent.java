package it.cubicworldsimulator.engine.loader;

/**
 * A generic OpenGL component
 */
public interface OpenGLComponent {
    int getId();
    
    class Vao implements OpenGLComponent {
        int vaoId;

        public Vao(int id) {
            vaoId = id;
        }

        @Override
        public int getId() {
            return vaoId;
        }
    }

    class Vbo implements OpenGLComponent {
        int vboId;

        public Vbo(int id) {
            vboId = id;
        }

        @Override
        public int getId() {
            return vboId;
        }
    }
}

