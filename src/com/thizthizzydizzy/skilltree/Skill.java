package com.thizthizzydizzy.skilltree;
import com.thizthizzydizzy.skilltree.effect.Effect;
import com.thizthizzydizzy.skilltree.effect.EffectPlaceholder;
import com.thizthizzydizzy.skilltree.requirement.Requirement;
import com.thizthizzydizzy.skilltree.requirement.RequirementPlaceholder;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
public class Skill{
    public final SkillTree tree;
    public int x;
    public int y;
    public String name;
    public Material icon;
    public boolean and = true;
    public ArrayList<Skill> prerequisites = new ArrayList<>();
    public ArrayList<Integer> prerequisitesI = new ArrayList<>();
    public ArrayList<Skill> linkedSkills = new ArrayList<>();
    public ArrayList<Integer> linkedSkillsI = new ArrayList<>();
    public int linkedLimit = 1;//only this many of the linked skills may be bought
    private ArrayList<Requirement> requirements = new ArrayList<>();
    private ArrayList<Effect> effects = new ArrayList<>();
    public boolean autoUnlock = false;
    public boolean hidden = false;
    public Skill(SkillTree tree, int x, int y, String name, Material icon){
        this.tree = tree;
        this.x = x;
        this.y = y;
        this.name = ChatColor.RESET+name;
        this.icon = icon;
    }
    public String getName(){
        return name;
    }
    public List<String> getLore(){
        ArrayList<String> lore = new ArrayList<>();
        return lore;
    }
    public void connect(Skill skill){
        if(prerequisites.contains(skill)){
            prerequisites.remove(skill);
        }else{
            prerequisites.add(skill);
        }
    }
    public void link(Skill skill){
        if(linkedSkills.contains(skill)){
            linkedSkills.remove(skill);
        }else{
            linkedSkills.add(skill);
        }
        if(linkedLimit>linkedSkills.size())linkedLimit = linkedSkills.size();
        linkedLimit = Math.min(linkedSkills.size()+1, Math.max(1, linkedLimit));
    }
    @Override
    public String toString(){
        return tree.namespace+":"+tree.name+" "+x+" "+y+" ("+name+")";
    }
    Config save(Config cfg){
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("name", name);
        cfg.set("icon", icon.name());
        cfg.set("and", and);
        cfg.set("autoUnlock", autoUnlock);
        cfg.set("hidden", hidden);
        cfg.set("linkedLimit", linkedLimit);
        ConfigList requirementsCfg = new ConfigList();
        for(Requirement r : requirements){
            requirementsCfg.add(r.save(Config.newConfig()));
        }
        cfg.set("requirements", requirementsCfg);
        
        ConfigList effectsCfg = new ConfigList();
        for(Effect e : effects){
            effectsCfg.add(e.save(Config.newConfig()));
        }
        cfg.set("effects", effectsCfg);
        
        ConfigList prerequisitesCfg = new ConfigList();
        for(Skill s : prerequisites){
            prerequisitesCfg.add(tree.skills.indexOf(s));
        }
        cfg.set("prerequisites", prerequisitesCfg);
        ConfigList linkedCfg = new ConfigList();
        for(Skill s : linkedSkills){
            linkedCfg.add(tree.skills.indexOf(s));
        }
        cfg.set("linked", linkedCfg);
        return cfg;
    }
    static Skill load(SkillTree tree, Config cfg){
        Skill skill = new Skill(tree, cfg.get("x"), cfg.get("y"), cfg.get("name"), Material.matchMaterial(cfg.get("icon")));
        skill.and = cfg.get("and");
        skill.autoUnlock = cfg.get("autoUnlock", skill.autoUnlock);
        skill.hidden = cfg.get("hidden", skill.hidden);
        skill.linkedLimit = cfg.get("linkedLimit");
        ConfigList requirementsCfg = cfg.get("requirements");
        for(int i = 0; i<requirementsCfg.size(); i++){
            Config c = requirementsCfg.get(i);
            Requirement r = Requirement.load(skill, c);
            skill.requirements.add(r);
        }
        
        ConfigList effectsCfg = cfg.get("effects");
        for(int i = 0; i<effectsCfg.size(); i++){
            Config c = effectsCfg.get(i);
            Effect e = Effect.load(skill, c);
            skill.effects.add(e);
        }
        
        ConfigList prerequisitesCfg = cfg.get("prerequisites");
        for(int i = 0; i<prerequisitesCfg.size(); i++){
            skill.prerequisitesI.add(prerequisitesCfg.get(i));
        }
        ConfigList linkedCfg = cfg.get("linked");
        for(int i = 0; i<linkedCfg.size(); i++){
            skill.linkedSkillsI.add(linkedCfg.get(i));
        }
        return skill;
    }
    public boolean isRoot(){
        return this==tree.root;
    }
    public void move(int x, int y){
        if(tree.getSkill(x, y)!=null){
            throw new IllegalArgumentException("That space is occupied!");
        }
        this.x = x;
        this.y = y;
    }
    public ArrayList<Effect> getEffects(){
        for(int i = 0; i<effects.size(); i++){
            Effect e = effects.get(i);
            if(e instanceof EffectPlaceholder){
                effects.set(i, Effect.load(this, ((EffectPlaceholder)e).config));
            }
        }
        return effects;
    }
    public ArrayList<Requirement> getRequirements(){
        for(int i = 0; i<requirements.size(); i++){
            Requirement e = requirements.get(i);
            if(e instanceof RequirementPlaceholder){
                requirements.set(i, Requirement.load(this, ((RequirementPlaceholder)e).config));
            }
        }
        return requirements;
    }
    public void reload(Effect effect){
        tree.reload(this, effect);
    }
    public void reload(Effect oldEffect, Effect newEffect){
        if(oldEffect.equals(newEffect)){
            reload(newEffect);
        }else{
            tree.reload(this, oldEffect, newEffect);
        }
    }
    public void removeEffect(Effect effect){
        getEffects().remove(effect);
        tree.removeEffect(this, effect);
    }
}