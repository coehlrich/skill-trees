package com.thizthizzydizzy.skilltree.requirement;
import com.thizthizzydizzy.skilltree.SkillInstance;
import com.thizthizzydizzy.skilltree.variable.VariableInteger;
import com.thizthizzydizzy.util.ItemProvider;
import com.thizthizzydizzy.util.ItemBuilder;
import org.bukkit.entity.Player;
public class RequirementTreeExperience extends Requirement{
    public RequirementTreeExperience(){
        super("skilltree", "tree_experience", new VariableInteger("Experience", 0));
    }
    @Override
    public boolean isMet(Player player, SkillInstance instance){
        return instance.tree.experience>=(int)variables[0].getValue();
    }
    @Override
    public Requirement newInstance(){
        return new RequirementTreeExperience();
    }
    @Override
    public ItemBuilder getIcon(){
        return ItemProvider.icon(ItemProvider.Icon.PERMISSION);
    }
    @Override
    public void unlock(Player player, SkillInstance instance){
        instance.tree.removeExperience((int)variables[0].getValue());
    }
}