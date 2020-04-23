package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableShort extends Variable<Short>{
    private final short min;
    private final short max;
    public VariableShort(String name, short defaultValue){
        this(name, defaultValue, Short.MIN_VALUE);
    }
    public VariableShort(String name, short defaultValue, short min){
        this(name, defaultValue, min, Short.MAX_VALUE);
    }
    public VariableShort(String name, short defaultValue, short min, short max){
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
            short i = getValue();
            string = string.trim();
            try{
                i = Short.parseShort(string);
            }catch(NumberFormatException ex){}
            i = (short)Math.max(min, Math.min(max, i));
            setValue(i);
        });
    }
    @Override
    public Object getSavableValue(){
        return (int)getValue();//can't save short directly to configs
    }
    @Override
    public void setSavableValue(Object value){
        setValue((short)value);
    }
}