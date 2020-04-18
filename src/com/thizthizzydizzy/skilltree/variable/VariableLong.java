package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableLong extends Variable<Long>{
    public VariableLong(String name, long defaultValue){
        super(name, defaultValue);
    }
    public ItemBuilder getIcon(){
        return new ItemBuilder(Material.PAPER);
    }
    @Override
    public void edit(Menu menu){
        menu.openAnvilGUI(getValue().toString(), "Edit "+name, (plyr, string) -> {
            long i = getValue();
            string = string.trim();
            try{
                i = Long.parseLong(string);
            }catch(NumberFormatException ex){}
            setValue(i);
        });
    }
    @Override
    public Object getSavableValue(){
        return (long)getValue();
    }
    @Override
    public void setSavableValue(Object value){
        setValue((long)value);
    }
}