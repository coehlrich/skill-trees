package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import java.util.Objects;
import org.bukkit.Material;
public class VariablePlaceholder extends Variable<Object>{
    public VariablePlaceholder(){
        super(null, null);
    }
    @Override
    public ItemBuilder getIcon(){
        return new ItemBuilder(Material.BARRIER);
    }
    @Override
    public void edit(Menu menu){}
    @Override
    public Object getSavableValue(){
        return getValue();
    }
    @Override
    public void setSavableValue(Object value){
        setValue(value);
    }
}