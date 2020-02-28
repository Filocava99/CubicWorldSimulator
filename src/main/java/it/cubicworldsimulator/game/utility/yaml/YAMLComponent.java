package it.cubicworldsimulator.game.utility.yaml;

import org.lwjgl.system.CallbackI;

import java.util.Map;

public class YAMLComponent {

    private final Map<String, Object> map;

    public YAMLComponent(Object object) throws ClassCastException{
        this.map = (Map<String, Object>)object;
    }

    public String getString(String field){
        return map.get(field).toString();
    }

    public int getInt(String field){
        return Integer.parseInt(getString(field));
    }

    public double getDouble(String field){
        return Double.parseDouble(getString(field));
    }

    public float getFloat(String field){
        return Float.parseFloat(getString(field));
    }

    public byte getByte(String field){
        return Byte.parseByte(getString(field));
    }

    public boolean getBoolean(String field){
        return Boolean.parseBoolean(getString(field));
    }

    public long getLong(String field){
        return Long.parseLong(getString(field));
    }

    public Object getObject(String field){
        return map.get(field);
    }

}
