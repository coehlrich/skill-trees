package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableInteger extends Variable<Integer>{
    private final int min;
    private final int max;
    public VariableInteger(String name, int defaultValue){
        this(name, defaultValue, Integer.MIN_VALUE);
    }
    public VariableInteger(String name, int defaultValue, int min){
        this(name, defaultValue, min, Integer.MAX_VALUE);
    }
    public VariableInteger(String name, int defaultValue, int min, int max){
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
            int i = getValue();
            string = string.trim();
            try{
                i = Integer.parseInt(string);
            }catch(NumberFormatException ex){}
            i = Math.min(max,Math.max(min, i));
            setValue(i);
        });
    }
    @Override
    public Object getSavableValue(){
        return (int)getValue();
    }
    @Override
    public void setSavableValue(Object value){
        setValue((int)value);
    }
}