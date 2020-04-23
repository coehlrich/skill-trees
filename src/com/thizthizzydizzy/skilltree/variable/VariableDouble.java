package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableDouble extends Variable<Double>{
    private final double min;
    private final double max;
    public VariableDouble(String name, double defaultValue){
        this(name, defaultValue, Double.MIN_VALUE);
    }
    public VariableDouble(String name, double defaultValue, double min){
        this(name, defaultValue, min, Double.MAX_VALUE);
    }
    public VariableDouble(String name, double defaultValue, double min, double max){
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
            double i = getValue();
            string = string.trim();
            try{
                i = Double.parseDouble(string);
                if(!Double.isFinite(i))i = min;
            }catch(NumberFormatException ex){}
            i = Math.min(max,Math.max(min, getValue()));
            setValue(i);
        });
    }
    @Override
    public Object getSavableValue(){
        return (double)getValue();
    }
    @Override
    public void setSavableValue(Object value){
        setValue((double)value);
    }
}