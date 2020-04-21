package com.thizthizzydizzy.skilltree.requirement;
import com.thizthizzydizzy.skilltree.Skill;
import com.thizthizzydizzy.skilltree.SkillInstance;
import com.thizthizzydizzy.skilltree.SkillTreeCore;
import com.thizthizzydizzy.skilltree.variable.Variable;
import com.thizthizzydizzy.util.ItemBuilder;
import java.util.ArrayList;
import java.util.Objects;
import org.bukkit.entity.Player;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
public abstract class Requirement{
    public final String namespace;
    public final String name;
    public final Variable[] variables;
    public Requirement(String namespace, String name, Variable... variables){
        this.namespace = namespace;
        this.name = name;
        this.variables = variables;
    }
    public abstract boolean isMet(Player player, SkillInstance instance);
    public abstract void unlock(Player player, SkillInstance instance);
    public abstract Requirement newInstance();
    public abstract ItemBuilder getIcon();
    /**
     * @return false if skills should be visible even when this requirement is not met
     */
    public boolean requiredForVisibility(){
        return true;
    }
    @Override
    public String toString(){
        return namespace+":"+name;
    }
    public ArrayList<String> getLore(){
        ArrayList<String> lore = new ArrayList<>();
        for(Variable v : variables){
            lore.add(Objects.toString(v.getValue()));
        }
        return lore;
    }
    public Config save(Config cfg){
        cfg.set("namespace",namespace);
        cfg.set("name",name);
        ConfigList vars = new ConfigList();
        for(Variable v : variables){
            vars.add(v.getSavableValue());
        }
        cfg.set("variables", vars);
        return cfg;
    }
    public static Requirement load(Skill skill, Config cfg){
        String namespace = cfg.get("namespace");
        String name = cfg.get("name");
        Requirement requirement = null;
        for(Requirement r : SkillTreeCore.INSTANCE.requirements){
            if(r.namespace.equals(namespace)&&r.name.equals(name)){
                requirement = r;
                break;
            }
        }
        if(requirement==null){
            requirement = new RequirementPlaceholder(cfg);
        }else{
            requirement = requirement.newInstance();
        }
        ConfigList vars = cfg.get("variables");
        for(int i = 0; i<vars.size(); i++){
            Object o = vars.get(i);
            requirement.variables[i].setSavableValue(o);
        }
        return requirement;
    }
    /**
     * Gets the line of lore to display on the skill in SkillTree
     * @return a single line that describes this requirement in a friendly way, or <code>null</code> if it should not be displayed
     */
    public abstract String getFriendlyLore();
}