package com.thizthizzydizzy.skilltree.requirement;

import com.thizthizzydizzy.skilltree.SkillInstance;
import com.thizthizzydizzy.skilltree.variable.Variable;
import com.thizthizzydizzy.skilltree.variable.VariablePlaceholder;
import com.thizthizzydizzy.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;

/**
 * Other plugins' requirements don't exist upon load
 * Thus, placeholders.
 */
public class RequirementPlaceholder extends Requirement{
    public Config config;
    public RequirementPlaceholder(Config config){
        super(config.get("namespace"), config.get("name"), generatePlaceholders(((ConfigList)config.get("variables")).size()));
        this.config = config;
    }
    private static Variable[] generatePlaceholders(int size){
        Variable[] placeholders = new Variable[size];
        for(int i = 0; i<placeholders.length; i++){
            placeholders[i] = new VariablePlaceholder();
        }
        return placeholders;
    }
    @Override
    public ItemBuilder getIcon(){
        return new ItemBuilder(Material.BARRIER);
    }
    @Override
    public boolean isMet(Player player, SkillInstance instance){
        return true;
    }
    @Override
    public void unlock(Player player, SkillInstance instance){}
    @Override
    public Requirement newInstance(){
        return new RequirementPlaceholder(config);
    }
}