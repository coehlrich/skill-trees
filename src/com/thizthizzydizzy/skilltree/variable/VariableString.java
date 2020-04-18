package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableString extends Variable<String>{
    public VariableString(String name, String defaultValue){
        super(name, defaultValue);
    }
    public ItemBuilder getIcon(){
        return new ItemBuilder(Material.PAPER);
    }
    @Override
    public void edit(Menu menu){
        menu.openAnvilGUI(getValue(), "Edit "+name, (plyr, string) -> {
            string = string.trim();
            setValue(string);
        });
    }
    @Override
    public Object getSavableValue(){
        return getValue();
    }
    @Override
    public void setSavableValue(Object value){
        setValue((String)value);
    }
}