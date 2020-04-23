package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableFloat extends Variable<Float>{
    private final float min;
    private final float max;
    public VariableFloat(String name, float defaultValue){
        this(name, defaultValue, Float.MIN_VALUE);
    }
    public VariableFloat(String name, float defaultValue, float min){
        this(name, defaultValue, min, Float.MAX_VALUE);
    }
    public VariableFloat(String name, float defaultValue, float min, float max){
        super(name, defaultValue);
        this.min = min;
        this.max = max;
    }
    public ItemBuilder getIcon(){
        return new ItemBuilder(Material.PAPER);
    }
    @Override
    public void edit(Menu menu){
        menu.openAnvilGUI(getValue().toString(), "Edit "+name, (plyr, string) -> {
            float i = getValue();
            string = string.trim();
            try{
                i = Float.parseFloat(string);
                if(!Float.isFinite(i))i = min;
            }catch(NumberFormatException ex){}
            i = Math.min(max,Math.max(min, getValue()));
            setValue(i);
        });
    }
    @Override
    public Object getSavableValue(){
        return (float)getValue();
    }
    @Override
    public void setSavableValue(Object value){
        setValue((float)value);
    }
}