package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableInteger extends Variable<Integer>{
    public VariableInteger(String name, int defaultValue){
        super(name, defaultValue);
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