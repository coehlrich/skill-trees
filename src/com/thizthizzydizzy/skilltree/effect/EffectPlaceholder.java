package com.thizthizzydizzy.skilltree.effect;

import com.thizthizzydizzy.skilltree.SkillInstance;
import com.thizthizzydizzy.skilltree.variable.Variable;
import com.thizthizzydizzy.skilltree.variable.VariablePlaceholder;
import com.thizthizzydizzy.util.ItemBuilder;
import org.bukkit.Material;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;

/**
 * Other plugins' effects don't exist upon load
 * Thus, placeholders.
 */
public class EffectPlaceholder extends Effect{
    public Config config;
    public EffectPlaceholder(Config config){
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
    public void enable(SkillInstance instance){}
    @Override
    public void disable(SkillInstance instance){}
    @Override
    public void reload(SkillInstance instance){}
    @Override
    public Effect newInstance(){
        return new EffectPlaceholder(config);
    }
    @Override
    public ItemBuilder getIcon(){
        return new ItemBuilder(Material.BARRIER);
    }
}