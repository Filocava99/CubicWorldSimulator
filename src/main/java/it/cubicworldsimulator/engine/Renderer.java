package it.cubicworldsimulator.engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private ShaderProgram shaderProgram;
    /* TODO pasare i nomi delle shader come argomenti del costruttore? Credo sia effettivamente meglio. Fare una interfaccia per il renderer? In questo modo non occorre passare le shader per argomento */
    public Renderer() {
    }

    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.loadResource("/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("/fragment.fs"));
        shaderProgram.link();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Mesh mesh) {
        clear();

        //TODO Spostarlo all'interno della classe Window? Qualcosa del tipo, ad ogli ciclo di rendering, quindi ogni volta che viene chiamato il metodo render() della classe Game, chiamare un metodo di window tipo window.update(), il quale ridimensiona la finestra. Viva l'italiano
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Draw the mesh
        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}