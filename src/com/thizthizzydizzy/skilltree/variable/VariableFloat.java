package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableFloat extends Variable<Float>{
    public VariableFloat(String name, float defaultValue){
        super(name, defaultValue);
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
            }catch(NumberFormatException ex){}
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