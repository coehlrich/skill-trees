package com.thizthizzydizzy.skilltree.requirement;
import com.thizthizzydizzy.skilltree.SkillInstance;
import com.thizthizzydizzy.skilltree.variable.VariableString;
import com.thizthizzydizzy.util.ItemProvider;
import com.thizthizzydizzy.util.ItemBuilder;
import org.bukkit.entity.Player;
public class RequirementPermission extends Requirement{
    public RequirementPermission(){
        super("skilltree", "permission", new VariableString("Permission", "skilltree.permission"));
    }
    @Override
    public boolean isMet(Player player, SkillInstance instance){
        return player.hasPermission((String)variables[0].getValue());
    }
    @Override
    public Requirement newInstance(){
        return new RequirementPermission();
    }
    @Override
    public ItemBuilder getIcon(){
        return ItemProvider.icon(ItemProvider.Icon.PERMISSION);
    }
    @Override
    public void unlock(Player player, SkillInstance instance){}
}