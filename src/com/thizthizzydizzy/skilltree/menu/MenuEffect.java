package com.thizthizzydizzy.skilltree.menu;
import com.thizthizzydizzy.skilltree.effect.Effect;
import com.thizthizzydizzy.skilltree.Skill;
import com.thizthizzydizzy.skilltree.variable.Variable;
import com.thizthizzydizzy.util.Button;
import com.thizthizzydizzy.util.Component;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
public class MenuEffect extends Menu{
    private final Effect effect;
    private final Skill skill;
    private Effect originalEffect;
    public MenuEffect(Menu parent, Plugin plugin, Player player, Skill skill, Effect effect){
        super(parent, plugin, player, "Modify Effect", 9*5);
        originalEffect = effect.copy();
        for(int i = 0; i<effect.variables.length; i++){
            Variable var = effect.variables[i];
            final int slot = i;
            add(new Component(i){
                @Override
                public ItemStack draw(){
                    return var.getIcon().setDisplayName(var.name).addLore(var.getValueS()).build();
                }
                @Override
                public void onClick(ClickType type){
                    effect.variables[slot].edit(MenuEffect.this);
                }
            });
        }
        add(new Button(size-1, new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.DARK_RED+""+ChatColor.BOLD+"Delete Effect").addLore(""+ChatColor.ITALIC+ChatColor.GRAY+"Shift-click to confirm deletion").build(), (type) -> {
            if(type==ClickType.SHIFT_LEFT){
                skill.getEffects().remove(effect);
                open(parent);
            }
        }));
        this.skill = skill;
        this.effect = effect;
    }
    @Override
    public void onClick(int slot, ClickType click){
        if(click==ClickType.RIGHT){
            if(parent instanceof MenuNewEffect)open(parent.parent);
            else open(parent);
        }
    }
    @Override
    public void onOpen(){
        super.onOpen();
        skill.reload(originalEffect, effect);
        originalEffect = effect.copy();//update so multiple modifications will go through
    }
    @Override
    public void onClose(){
        super.onClose();
        skill.reload(originalEffect, effect);
    }
}