package it.cubicworldsimulator.engine.graphic.light;

import org.joml.Vector3f;

public class SceneLight {

    private final DirectionalLight directionalLight;
    private final PointLight[] pointLights;
    private final SpotLight[] spotLights;
    private final Vector3f ambientLight;
    private final float specularPower;

    public SceneLight(DirectionalLight directionalLight, PointLight[] pointLights, SpotLight[] spotLights, Vector3f ambientLight, float specularPower) {
        this.directionalLight = directionalLight;
        this.pointLights = pointLights;
        this.spotLights = spotLights;
        this.ambientLight = ambientLight;
        this.specularPower = specularPower;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public PointLight[] getPointLights() {
        return pointLights;
    }

    public SpotLight[] getSpotLights() {
        return spotLights;
    }

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public float getSpecularPower() {
        return specularPower;
    }
}
