package it.cubicworldsimulator.game.utility.yaml;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class YAMLComponent {

    private final Map<String, Object> map;

    /**
     * @param object any primitive type or another YAMLComponent
     * @throws ClassCastException
     */
    public YAMLComponent(Object object) throws ClassCastException{
        this.map = (Map<String, Object>)object;
    }

    /**
     * Returns the string value of a field
     * @param field field name
     * @return string value
     */
    public String getString(String field){
        return map.get(field).toString();
    }

    /**
     * Returns the int value of a field
     * @param field field name
     * @return int value
     */
    public int getInt(String field){
        return Integer.parseInt(getString(field));
    }

    /**
     * Returns the double value of a field
     * @param field field name
     * @return double value
     */
    public double getDouble(String field){
        return Double.parseDouble(getString(field));
    }

    /**
     * Returns the float value of a field
     * @param field field name
     * @return float value
     */
    public float getFloat(String field){
        return Float.parseFloat(getString(field));
    }

    /**
     * Returns the byte value of a field
     * @param field field name
     * @return byte value
     */
    public byte getByte(String field){
        return Byte.parseByte(getString(field));
    }

    /**
     * Returns the boolean value of a field
     * @param field field name
     * @return boolean value
     */
    public boolean getBoolean(String field){
        return Boolean.parseBoolean(getString(field));
    }

    /**
     * Returns the long value of a field
     * @param field field name
     * @return long value
     */
    public long getLong(String field){
        return Long.parseLong(getString(field));
    }

    /**
     * Return an object value from a field
     * @param field field name
     * @return object value
     */
    public Object getObject(String field){
        return map.get(field);
    }

    /**
     * Returns a YAMLComponent value of a field
     * @param field field name
     * @return YAMLComponent value
     */
    public YAMLComponent getYAMLComponent(String field){
        if(getObject(field) != null){
            return new YAMLComponent(getObject(field));
        }else{
            return null;
        }
    }

    /**
     * Returns the fields (or keys) names
     * @return keys/fields names
     */
    public Set<String> getKeys(){
        return map.keySet();
    }

    /**
     * Returns a collection of values of the YAMLComponent
     * @return collection of objects
     */
    public Collection<Object> getValues(){
        return map.values();
    }

    /**
     * Returns a set of entries
     * @return Set of entries (string key, object value)
     */
    public Set<Map.Entry<String, Object>> getEntrySet(){
        return map.entrySet();
    }

}
