package com.thizthizzydizzy.skilltree.variable;
import com.thizthizzydizzy.skilltree.SkillTreeCore;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
public class VariableOptions extends Variable<String>{
    private final Option[] options;
    public VariableOptions(String name, Option defaultValue, Option... options){
        super(name, defaultValue.text);
        this.options = options;
    }
    public ItemBuilder getIcon(){
        Option o = null;
        for(Option op : options){
            if(op.text.equals(getValue()))o = op;
        }
        return new ItemBuilder(o==null?Material.PAPER:o.icon);
    }
    @Override
    public void edit(Menu menu){
        int size = 0;
        while(options.length>size)size+=9;
        if(size<1||size>9*6){
            menu.openAnvilGUI(getValue(), "Edit "+name, (plyr, string) -> {
                string = string.trim();
                for(Option o : options){
                    if(o.text.equalsIgnoreCase(string))setValue(o.text);
                }
            });
        }
        menu.open(new Menu(menu, SkillTreeCore.INSTANCE, menu.player, "Choose "+name, size){
            @Override
            protected void updateInventory(){
                for(int i = 0; i < options.length; i++){
                    Option option = options[i];
                    inventory.setItem(i, option.getIcon().build());
                }
            }
            @Override
            public void onClick(int slot, ClickType click){
                super.onClick(slot, click);
                if(click==ClickType.LEFT){
                    if(slot>=0&&slot<options.length)setValue(options[slot].text);
                    open(parent);
                }
            }
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
    public static class Option{
        private final String text;
        private final Material icon;
        public Option(String text, Material icon){
            this.text = text;
            this.icon = icon;
        }
        private ItemBuilder getIcon(){
            return new ItemBuilder(icon).setDisplayName(text);
        }
    }
}