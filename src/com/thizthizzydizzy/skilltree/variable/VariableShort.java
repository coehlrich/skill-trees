package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableShort extends Variable<Short>{
    public VariableShort(String name, short defaultValue){
        super(name, defaultValue);
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