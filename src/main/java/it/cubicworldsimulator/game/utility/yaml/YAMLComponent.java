package it.cubicworldsimulator.game.utility.yaml;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

    public YAMLComponent getYAMLComponent(String field){
        if(getObject(field) != null){
            return new YAMLComponent(getObject(field));
        }else{
            return null;
        }
    }

    public Set<String> getKeys(){
        return map.keySet();
    }

    public Collection<Object> getValues(){
        return map.values();
    }

    public Set<Map.Entry<String, Object>> getEntrySet(){
        return map.entrySet();
    }

}
