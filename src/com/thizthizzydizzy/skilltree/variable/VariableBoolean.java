package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableBoolean extends Variable<Boolean>{
    public VariableBoolean(String name, boolean defaultValue){
        super(name, defaultValue);
    }
    public ItemBuilder getIcon(){
        return new ItemBuilder(Material.PAPER);
    }
    @Override
    public void edit(Menu menu){
        setValue(!getValue());
        menu.open(menu);
    }
    @Override
    public Object getSavableValue(){
        return (boolean)getValue();
    }
    @Override
    public void setSavableValue(Object value){
        setValue((boolean)value);
    }
}