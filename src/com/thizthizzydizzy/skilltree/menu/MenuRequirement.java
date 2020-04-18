package com.thizthizzydizzy.skilltree.menu;
import com.thizthizzydizzy.skilltree.requirement.Requirement;
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
public class MenuRequirement extends Menu{
    public MenuRequirement(Menu parent, Plugin plugin, Player player, Skill skill, Requirement requirement){
        super(parent, plugin, player, "Modify Requirement", 9*5);
        for(int i = 0; i<requirement.variables.length; i++){
            Variable var = requirement.variables[i];
            final int slot = i;
            add(new Component(i){
                @Override
                public ItemStack draw(){
                    return var.getIcon().setDisplayName(var.name).addLore(var.getValueS()).build();
                }
                @Override
                public void onClick(ClickType type){
                    requirement.variables[slot].edit(MenuRequirement.this);
                }
            });
        }
        add(new Button(size-1, new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.DARK_RED+""+ChatColor.BOLD+"Delete Requirement").addLore(""+ChatColor.ITALIC+ChatColor.GRAY+"Shift-click to confirm deletion").build(), (type) -> {
            if(type==ClickType.SHIFT_LEFT){
                skill.getRequirements().remove(requirement);
                open(parent);
            }
        }));
    }
    @Override
    public void onClick(int slot, ClickType click){
        if(click==ClickType.RIGHT){
            if(parent instanceof MenuNewRequirement)open(parent.parent);
            else open(parent);
        }
    }
}