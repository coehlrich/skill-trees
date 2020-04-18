package com.thizthizzydizzy.skilltree.menu;
import com.thizthizzydizzy.skilltree.effect.Effect;
import com.thizthizzydizzy.skilltree.Skill;
import com.thizthizzydizzy.skilltree.SkillTreeCore;
import com.thizthizzydizzy.util.Button;
import com.thizthizzydizzy.util.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.plugin.Plugin;
public class MenuNewEffect extends Menu{
    public MenuNewEffect(Menu parent, Plugin plugin, Player player, Skill skill){
        super(parent, plugin, player, "New Effect", 9*5);
        for(int i = 0; i<Math.min(SkillTreeCore.INSTANCE.effects.size(), size); i++){
            Effect r = SkillTreeCore.INSTANCE.effects.get(i).newInstance();
            add(new Button(i, r.getIcon().setDisplayName(r.toString()).build(), (type) -> {
                if(type==ClickType.LEFT){
                    skill.getEffects().add(r);
                    open(new MenuEffect(parent, plugin, player, skill, r));
                }
            }));
        }
    }
    @Override
    public void onClick(int slot, ClickType click){
        if(inventory.getItem(slot)==null&&click==ClickType.RIGHT)open(parent);
    }
}