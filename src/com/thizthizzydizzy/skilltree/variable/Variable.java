package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import java.util.Objects;
public abstract class Variable<T>{
    private T value;
    public final String name;
    public Variable(String name, T defaultValue){
        this.value = defaultValue;
        this.name = name;
    }
    public T getValue(){
        return value;
    }
    public void setValue(T value){
        this.value = value;
    }
    public abstract ItemBuilder getIcon();
    public abstract void edit(Menu menu);
    /**
     * Get a savable form of the value
     * @return a Config, ConfigList, String, boolean, double, float, int, or long
     */
    public abstract Object getSavableValue();
    /**
     * Set the value from a loaded value from the config
     * @param value a Config, ConfigList, String, boolean, double, float, int, or long
     */
    public abstract void setSavableValue(Object value);
    public String getValueS(){
        return Objects.toString(getValue());
    }
}