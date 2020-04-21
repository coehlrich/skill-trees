package com.thizthizzydizzy.skilltree;
import com.thizthizzydizzy.skilltree.effect.Effect;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Material;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
public class SkillTree{
    public final String namespace;
    public final String name;
    public final ArrayList<Skill> skills = new ArrayList<>();
    public Skill root;
    public ArrayList<SkillTreeInstance> instances = new ArrayList<>();
    public SkillTree(String namespace, String name){
        this.namespace = namespace;
        this.name = name;
        Material[] materials = new Material[]{Material.OAK_WOOD,Material.BIRCH_WOOD,Material.SPRUCE_WOOD,Material.ACACIA_WOOD,Material.DARK_OAK_WOOD,Material.JUNGLE_WOOD};
        root = new Skill(this, 0, 0, "Root", materials[new Random().nextInt(materials.length)]);
        skills.add(root);
    }
    public Skill getSkill(int x, int y){
        for(Skill s : skills){
            if(s.x==x&&s.y==y)return s;
        }
        return null;
    }
    public Skill createSkill(int x, int y){
        Skill skill = getSkill(x, y);
        if(skill!=null)return skill;
        Material[] materials = new Material[]{Material.STICK};
        skills.add(skill = new Skill(this, x, y, "New Skill", materials[new Random().nextInt(materials.length)]));
        refreshInstances();
        return skill;
    }
    public void deleteSkill(Skill skill){
        if(skill==root)throw new IllegalArgumentException("Root skill cannot be deleted!");
        skills.remove(skill);
        for(Skill s : skills){
            if(s.prerequisites.contains(skill)){
                s.prerequisites.remove(skill);
            }
            if(s.linkedSkills.contains(skill)){
                s.linkedSkills.remove(skill);
            }
        }
        refreshInstances();
    }
    public SkillTreeInstance newInstance(UUID uid){
        SkillTreeInstance instance = SkillTreeInstance.newInstance(uid, this);
        instances.add(instance);
        return instance;
    }
    public SkillTreeInstance newInstance(){
        SkillTreeInstance instance = SkillTreeInstance.newInstance(this);
        instances.add(instance);
        return instance;
    }
    public SkillTreeInstance getInstance(UUID uid){
        for(SkillTreeInstance instance : instances){
            if(instance.uid.equals(uid))return instance;
        }
        return newInstance(uid);
    }
    public void refreshInstances(){
        for(SkillTreeInstance instance : instances){
            instance.refresh();
        }
    }
    Config save(Config cfg){
        cfg.set("namespace", namespace);
        cfg.set("name", name);
        ConfigList skillsCfg = new ConfigList();
        for(Skill s : skills){
            skillsCfg.add(s.save(Config.newConfig()));
        }
        cfg.set("skills", skillsCfg);
        return cfg;
    }
    static SkillTree load(Config cfg){
        SkillTree tree = new SkillTree(cfg.get("namespace"), cfg.get("name"));
        tree.skills.clear();
        ConfigList skillsCfg = cfg.get("skills");
        for(int i = 0; i<skillsCfg.size(); i++){
            Config c = skillsCfg.get(i);
            Skill s = Skill.load(tree, c);
            if(s.x==0&&s.y==0)tree.root = s;
            tree.skills.add(s);
        }
        for(Skill s : tree.skills){
            for(int i : s.linkedSkillsI){
                if(i==-1){
                    SkillTreeCore.INSTANCE.getLogger().log(Level.WARNING, "Removing invalid linked skill index from {0}:{1}", new Object[]{tree.namespace, tree.name});
                    continue;
                }
                s.linkedSkills.add(tree.skills.get(i));
            }
            for(int i : s.prerequisitesI){
                if(i==-1){
                    SkillTreeCore.INSTANCE.getLogger().log(Level.WARNING, "Removing invalid skill prerequisite index from {0}:{1}", new Object[]{tree.namespace, tree.name});
                    continue;
                }
                s.prerequisites.add(tree.skills.get(i));
            }
        }
        return tree;
    }
    public void reload(Skill skill, Effect effect){
        for(SkillTreeInstance instance : instances){
            effect.reload(instance.getSkill(skill));
        }
    }
    public void reload(Skill skill, Effect oldEffect, Effect newEffect){
        for(SkillTreeInstance instance : instances){
            oldEffect.disable(instance.getSkill(skill));
            oldEffect.reload(instance.getSkill(skill));
            newEffect.enable(instance.getSkill(skill));
            newEffect.reload(instance.getSkill(skill));
        }
    }
    public void removeEffect(Skill skill, Effect effect){
        for(SkillTreeInstance instance : instances){
            effect.disable(instance.getSkill(skill));
            effect.reload(instance.getSkill(skill));
        }
    }
}