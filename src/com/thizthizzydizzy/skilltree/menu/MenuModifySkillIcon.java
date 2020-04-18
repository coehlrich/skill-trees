package com.thizthizzydizzy.skilltree.menu;
import com.thizthizzydizzy.skilltree.Skill;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.Label;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
public class MenuModifySkillIcon extends Menu{
    private final Skill skill;
    public MenuModifySkillIcon(Menu parent, Plugin plugin, Player player, Skill skill){
        super(parent, plugin, player, "Modify Skill Icon: "+skill.tree.namespace+":"+skill.tree.name+"/"+skill.name, 9);
        add(new Label(4, new ItemBuilder(skill.icon).setDisplayName(skill.getName()).addLore("Choose any item in your inventory as this skill's icon").build()));
        this.skill = skill;
    }
    @Override
    public void onInventoryClick(int slot, ClickType click){
        if(click==ClickType.LEFT){
            ItemStack stack = player.getInventory().getItem(slot);
            if(stack==null)return;
            skill.icon = stack.getType();
        }
        if(click==ClickType.LEFT||click==ClickType.RIGHT)open(parent);
    }
}