package com.thizthizzydizzy.util;
import org.bukkit.inventory.ItemStack;
public class Label extends Component{
    private final ItemStack label;
    public Label(int index, ItemStack label){
        super(index);
        this.label = label;
    }
    @Override
    public ItemStack draw(){
        return label;
    }
}