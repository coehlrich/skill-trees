package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
public class VariableLong extends Variable<Long>{
    private final long min;
    private final long max;
    public VariableLong(String name, long defaultValue){
        this(name, defaultValue, Long.MIN_VALUE);
    }
    public VariableLong(String name, long defaultValue, long min){
        this(name, defaultValue, min, Long.MAX_VALUE);
    }
    public VariableLong(String name, long defaultValue, long min, long max){
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
            long i = getValue();
            string = string.trim();
            try{
                i = Long.parseLong(string);
            }catch(NumberFormatException ex){}
            i = Math.min(max,Math.max(min, i));
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