package it.cubicworldsimulator.engine;

import it.cubicworldsimulator.engine.graphic.Material;
import it.cubicworldsimulator.engine.graphic.PointLight;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

	private final int programId;
    private final Map<String, Integer> uniforms;
    private int vertexShaderId;
    private int fragmentShaderId;

	public ShaderProgram() throws Exception {
        this.programId = glCreateProgram();
        this.uniforms = new HashMap<>();
        if (this.programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception {
        this.vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        this.fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(this.programId,
                uniformName);
        if (uniformLocation < 0) {
            //throw new Exception("Could not find uniform:" + uniformName);
        }
        this.uniforms.put(uniformName, uniformLocation);
    }

    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }
    
    public void setUniform(String uniformName, int value) {
        glUniform1i(this.uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(this.uniforms.get(uniformName), false,
                    value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".colour", pointLight.getColor() );
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColour());
        setUniform(uniformName + ".diffuse", material.getDiffuseColour());
        setUniform(uniformName + ".specular", material.getSpecularColour());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }
    
    public void setUniform(String uniformName, float value) {
    	glUniform1f(uniforms.get(uniformName), value);
    }
    
    public void setUniform(String uniformName, Vector3f value) {
    	glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }
    
    public void setUniform(String uniformName, Vector4f value) {
    	glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }
    
    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(this.programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(this.programId);
        if (glGetProgrami(this.programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(this.programId, 1024));
        }
        if (this.vertexShaderId != 0) {
            glDetachShader(this.programId, this.vertexShaderId);
        }
        if (this.fragmentShaderId != 0) {
            glDetachShader(this.programId, this.fragmentShaderId);
        }
    }

    public void validateProgram() {
        glValidateProgram(this.programId);
        if (glGetProgrami(this.programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(this.programId, 1024));
        }
    }
    
    
    public void bind() {
        glUseProgram(this.programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (this.programId != 0) {
            glDeleteProgram(this.programId);
        }
    }
}
