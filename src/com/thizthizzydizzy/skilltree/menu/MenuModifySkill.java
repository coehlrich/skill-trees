package com.thizthizzydizzy.skilltree.menu;
import com.thizthizzydizzy.skilltree.requirement.Requirement;
import com.thizthizzydizzy.skilltree.Skill;
import com.thizthizzydizzy.skilltree.effect.Effect;
import com.thizthizzydizzy.util.Button;
import com.thizthizzydizzy.util.Component;
import com.thizthizzydizzy.util.ItemBuilder;
import com.thizthizzydizzy.util.ItemProvider;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
public class MenuModifySkill extends Menu{
    public MenuModifySkill(Menu parent, Plugin plugin, Player player, Skill skill){
        super(parent, plugin, player, "Modify Skill: "+skill.tree.namespace+":"+skill.tree.name+"/"+skill.name, 9*5);
        add(new Component(0){
            @Override
            public ItemStack draw(){
                return new ItemBuilder(skill.icon).setDisplayName(skill.getName()).addLore("Click to modify icon").addLore("Shift-click to rename").build();
            }
            @Override
            public void onClick(ClickType type){
                switch(type){
                    case LEFT:
                        open(new MenuModifySkillIcon(MenuModifySkill.this, plugin, player, skill));
                        break;
                    case SHIFT_LEFT:
                        openAnvilGUI(skill.name, "Rename Skill", (plyr, string) -> {
                            skill.name = string;
                        });
                }
            }
        });
        if(!skill.isRoot()){
            add(new Button(1, ItemProvider.icon(ItemProvider.Icon.CONNECT).build(), (type) -> {
                if(type==ClickType.LEFT)open(new MenuSkillTree(this, plugin, player, skill.tree, MenuSkillTree.Type.CONNECT, skill));
            }));
            add(new Component(2){
                @Override
                public ItemStack draw(){
                    return ItemProvider.icon(skill.and?ItemProvider.Icon.CONNECT_AND:ItemProvider.Icon.CONNECT_OR).build();
                }
                @Override
                public void onClick(ClickType type){
                    if(type==ClickType.LEFT){
                        skill.and = !skill.and;
                        updateInventory();
                    }
                }
            });
            add(new Component(3){
                @Override
                public ItemStack draw(){
                    return ItemProvider.icon(ItemProvider.Icon.LINK).addLore(skill.linkedSkills.size()+" Linked Skills").build();
                }
                @Override
                public void onClick(ClickType type){
                    if(type==ClickType.LEFT)open(new MenuSkillTree(MenuModifySkill.this, plugin, player, skill.tree, MenuSkillTree.Type.LINK, skill));
                }
            });
            add(new Component(4){
                @Override
                public ItemStack draw(){
                    return ItemProvider.icon(ItemProvider.Icon.NUMBER).setDisplayName("Linked Skill Limit").addLore("Limit: "+skill.linkedLimit).build();
                }
                @Override
                public void onClick(ClickType type){
                    if(type==ClickType.LEFT){
                        skill.linkedLimit++;
                        if(skill.linkedLimit>skill.linkedSkills.size()+1)skill.linkedLimit = 1;
                        updateInventory();
                    }
                }
            });
            add(new Button(6, ItemProvider.icon(ItemProvider.Icon.MOVE).build(), (type) -> {
                if(type==ClickType.LEFT)open(new MenuSkillTree(this, plugin, player, skill.tree, MenuSkillTree.Type.MOVE, skill));
            }));
        }
        add(new Component(7){
            @Override
            public ItemStack draw(){
                return ItemProvider.icon(skill.hidden?ItemProvider.Icon.HIDDEN:ItemProvider.Icon.VISIBLE).setDisplayName(skill.hidden?"Hidden":"Visible").build();
            }
            @Override
            public void onClick(ClickType type){
                if(type==ClickType.LEFT){
                    skill.hidden = !skill.hidden;
                    updateInventory();
                }
            }
        });
        add(new Component(8){
            @Override
            public ItemStack draw(){
                return ItemProvider.icon(skill.autoUnlock?ItemProvider.Icon.UNLOCKED:ItemProvider.Icon.LOCKED).setDisplayName("Auto-Unlock").addLore(skill.autoUnlock?"Enabled":"Disabled").build();
            }
            @Override
            public void onClick(ClickType type){
                if(type==ClickType.LEFT){
                    skill.autoUnlock = !skill.autoUnlock;
                    updateInventory();
                }
            }
        });
        add(new Button(9, ItemProvider.icon(ItemProvider.Icon.REQUIREMENTS).addLore("Click to add requirement").build(), (type) -> {
            if(type==ClickType.LEFT)open(new MenuNewRequirement(this, plugin, player, skill));
        }));
        for(int i = 0; i<8; i++){
            final int j = i;
            add(new Component(i+10){
                @Override
                public ItemStack draw(){
                    Requirement r = null;
                    try{
                        r = skill.getRequirements().get(j);
                    }catch(IndexOutOfBoundsException ex){}
                    return r==null?null:r.getIcon().setDisplayName(r.toString()).addLore(r.getLore()).build();
                }
                @Override
                public void onClick(ClickType type){
                    Requirement r = null;
                    try{
                        r = skill.getRequirements().get(j);
                    }catch(IndexOutOfBoundsException ex){}
                    if(r==null)return;
                    if(type==ClickType.LEFT)open(new MenuRequirement(MenuModifySkill.this, plugin, player, skill, r));
                }
            });
        }
        add(new Button(18, ItemProvider.icon(ItemProvider.Icon.EFFECTS).addLore("Click to add effect").build(), (type) -> {
            if(type==ClickType.LEFT)open(new MenuNewEffect(this, plugin, player, skill));
        }));
        for(int i = 0; i<8; i++){
            final int j = i;
            add(new Component(i+19){
                @Override
                public ItemStack draw(){
                    Effect r = null;
                    try{
                        r = skill.getEffects().get(j);
                    }catch(IndexOutOfBoundsException ex){}
                    return r==null?null:r.getIcon().setDisplayName(r.toString()).addLore(r.getLore()).build();
                }
                @Override
                public void onClick(ClickType type){
                    Effect r = null;
                    try{
                        r = skill.getEffects().get(j);
                    }catch(IndexOutOfBoundsException ex){}
                    if(r==null)return;
                    if(type==ClickType.LEFT)open(new MenuEffect(MenuModifySkill.this, plugin, player, skill, r));
                }
            });
        }
        if(!skill.isRoot()){
            add(new Button(size-1, new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.RESET+""+ChatColor.DARK_RED+ChatColor.BOLD+"Delete Skill").addLore(""+ChatColor.ITALIC+ChatColor.GRAY+"Shift-click to confirm deletion").build(), (type) -> {
                if(type==ClickType.SHIFT_LEFT){
                    skill.tree.deleteSkill(skill);
                    open(parent);
                }
            }));
        }
    }
    @Override
    public void onClick(int slot, ClickType click){
        if(inventory.getItem(slot)==null&&click==ClickType.RIGHT)open(parent);
    }
}