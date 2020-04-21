package com.thizthizzydizzy.skilltree.effect;
import com.thizthizzydizzy.skilltree.Skill;
import com.thizthizzydizzy.skilltree.SkillInstance;
import com.thizthizzydizzy.skilltree.SkillTreeCore;
import com.thizthizzydizzy.skilltree.variable.Variable;
import com.thizthizzydizzy.util.ItemBuilder;
import java.util.ArrayList;
import java.util.Objects;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
public abstract class Effect{
    public final String namespace;
    public final String name;
    public final Variable[] variables;
    public Effect(String namespace, String name, Variable... variables){
        this.namespace = namespace;
        this.name = name;
        this.variables = variables;
    }
    /**
     * Called when the skill is unlocked
     * @param instance the instance that was unlocked
     */
    public abstract void enable(SkillInstance instance);
    /**
     * Called when the skill is deleted or un-obtained
     * @param instance the instance that was removed
     */
    public abstract void disable(SkillInstance instance);
    /**
     * Called when an effect changes, or otherwise must be reloaded
     * This is always called when an effect enabled or disabled.
     * @param instance the instance that requires a reload
     */
    public abstract void reload(SkillInstance instance);
    public abstract Effect newInstance();
    public abstract ItemBuilder getIcon();
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
    public static Effect load(Skill skill, Config cfg){
        String namespace = cfg.get("namespace");
        String name = cfg.get("name");
        Effect effect = null;
        for(Effect e : SkillTreeCore.INSTANCE.effects){
            if(e.namespace.equals(namespace)&&e.name.equals(name)){
                effect = e;
                break;
            }
        }
        if(effect==null){
            effect = new EffectPlaceholder(cfg);
        }else{
            effect = effect.newInstance();
        }
        ConfigList vars = cfg.get("variables");
        for(int i = 0; i<vars.size(); i++){
            Object o = vars.get(i);
            effect.variables[i].setSavableValue(o);
        }
        return effect;
    }
    public Effect copy(){
        Effect copy = newInstance();
        for (int i = 0; i < variables.length; i++) {
            copy.variables[i].setValue(variables[i].getValue());
        }
        return copy;
    }
    @Override
    public boolean equals(Object obj){
        if(obj==null)return false;
        if(!(obj instanceof Effect))return false;
        Effect other = (Effect)obj;
        if(!other.namespace.equals(namespace))return false;
        if(!other.name.equals(name))return false;
        for(int i = 0; i<variables.length; i++){
            if(!Objects.equals(other.variables[i].getValue(), variables[i].getValue()))return false;
        }
        return true;
    }
    /**
     * Gets the line of lore to display on the skill in SkillTree
     * @return a single line that describes this requirement in a friendly way, or <code>null</code> if it should not be displayed
     */
    public abstract String getFriendlyLore();
}