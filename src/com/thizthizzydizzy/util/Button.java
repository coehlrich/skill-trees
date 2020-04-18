package com.thizthizzydizzy.util;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
public class Button extends Component{
    public final ItemStack label;//You don't need to change this- make a non-button component instead
    private final ClickListener listener;
    public Button(int index, ItemStack label, ClickListener listener){
        super(index);
        this.label = label;
        this.listener = listener;
    }
    @Override
    public final ItemStack draw(){//You don't need to override this- make a non-Button component instead
        return label;
    }
    @Override
    public final void onClick(ClickType type){
        listener.onClick(type);
    }
}