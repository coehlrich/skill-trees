package com.thizthizzydizzy.skilltree;
import com.thizthizzydizzy.skilltree.effect.Effect;
import com.thizthizzydizzy.skilltree.menu.MenuSkillTreeInstance;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.entity.Player;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
public class SkillTreeInstance{
    public final UUID uid;
    public final SkillTree tree;
    public int experience;
    private ArrayList<SkillInstance> skills = new ArrayList<>();
    public static SkillTreeInstance newInstance(SkillTree tree){
        return newInstance(UUID.randomUUID(), tree);
    }
    static SkillTreeInstance newInstance(UUID uid, SkillTree tree){
        return new SkillTreeInstance(uid, tree);
    }
    private SkillTreeInstance(UUID uid, SkillTree tree){
        this.uid = uid;
        this.tree = tree;
        for(Skill skill : tree.skills){
            skills.add(new SkillInstance(this, skill));
        }
    }
    public void openGUI(Player player){
        openGUI(player, 0, 0);
    }
    public void openGUI(Player player, int panX, int panY){
        MenuSkillTreeInstance menu = new MenuSkillTreeInstance(null, SkillTreeCore.INSTANCE, player, this);
        menu.openInventory();
        menu.pan(panX, panY);
    }
    public SkillInstance getSkill(Skill s){
        for(SkillInstance i : skills){
            if(i.skill==s)return i;
        }
        //uh-oh, something's not right!
        refresh();
        for(SkillInstance i : skills){
            if(i.skill==s)return i;
        }
        return null;
    }
    public void addExperience(int xp){
        experience+=xp;
    }
    public void removeExperience(int xp){
        experience-=xp;
    }
    public void setExperience(int xp){
        experience = xp;
    }
    public int getExperience(){
        return experience;
    }
    public boolean unlockSkill(Player player, Skill clickedSkill){
        return getSkill(clickedSkill).unlock(player);
    }
    public void refresh(){
        for(Iterator<SkillInstance> it = skills.iterator(); it.hasNext();){
            SkillInstance inst = it.next();
            if(!tree.skills.contains(inst.skill)){
                it.remove();
                inst.disable();
            }
        }
        SKILL:for(Skill s : tree.skills){
            for(SkillInstance in : skills){
                if(in.skill==s)continue SKILL;
            }
            skills.add(new SkillInstance(this, s));
        }
    }
    Config save(Config cfg){
        cfg.set("uid", uid.toString());
        cfg.set("experience", experience);
        ConfigList skillsCfg = new ConfigList();
        for(SkillInstance s : skills){
            skillsCfg.add(s.save(Config.newConfig()));
        }
        cfg.set("skills", skillsCfg);
        return cfg;
    }
    static SkillTreeInstance load(SkillTree tree, Config cfg){
        UUID uid = UUID.fromString(cfg.get("uid"));
        SkillTreeInstance instance = new SkillTreeInstance(uid, tree);
        instance.skills.clear();
        instance.experience = cfg.get("experience");
        ConfigList skillsCfg = cfg.get("skills");
        for(int i = 0; i<skillsCfg.size(); i++){
            Config c = skillsCfg.get(i);
            SkillInstance s = SkillInstance.load(instance, tree.skills.get(i), c);
            instance.skills.add(s);
        }
        return instance;
    }
    public ArrayList<Effect> getActiveEffects(){
        ArrayList<Effect> effects = new ArrayList<>();
        for(SkillInstance skill : skills){
            if(skill.isUnlocked()){
                effects.addAll(skill.skill.getEffects());
            }
        }
        return effects;
    }
}