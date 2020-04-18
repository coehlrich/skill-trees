package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableDouble extends Variable<Double>{
    public VariableDouble(String name, double defaultValue){
        super(name, defaultValue);
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
            }catch(NumberFormatException ex){}
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