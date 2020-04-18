package com.thizthizzydizzy.skilltree;
import com.thizthizzydizzy.skilltree.effect.Effect;
import com.thizthizzydizzy.skilltree.requirement.Requirement;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import simplelibrary.config2.Config;
public class SkillInstance{
    public final SkillTreeInstance tree;
    public final Skill skill;
    private boolean unlocked = false;
    public SkillInstance(SkillTreeInstance tree, Skill skill){
        this.tree = tree;
        this.skill = skill;
    }
    public Availability getAvailability(){
        if(isUnlocked())return Availability.PURCHASED;
        int linkedGotten = 0;
        for(Skill s : skill.linkedSkills){
            SkillInstance i = tree.getSkill(s);
            if(i.isUnlocked())linkedGotten++;
        }
        if(linkedGotten>=skill.linkedLimit)return Availability.LOCKED_LINKED;
        boolean lock = !skill.and;
        boolean link = !skill.and;
        for(Skill s : skill.prerequisites){
            SkillInstance i = tree.getSkill(s);
            if(skill.and){
                if(i.getAvailability()==Availability.LOCKED_LINKED)link = true;
                if(!i.isUnlocked())lock = true;
            }else{
                if(i.isUnlocked())lock = false;
                if(i.getAvailability()!=Availability.LOCKED_LINKED)link = false;
            }
        }
        if(link)return Availability.LOCKED_LINKED;
        if(lock)return Availability.LOCKED;
        return Availability.UNLOCKED;
    }
    public boolean isUnlocked(){
        return unlocked;
    }
    /**
     * Checks if the skill is available
     * @param player The player to check against
     * @return <code>true</code> if the skill is available and not already unlocked
     */
    public boolean isAvailable(Player player){
        if(getAvailability()!=Availability.UNLOCKED)return false;
        for(Requirement r : skill.getRequirements()){
            if(!r.isMet(player, this))return false;
        }
        return true;
    }
    public boolean unlock(Player player){
        if(!isAvailable(player))return false;
        for(Requirement r : skill.getRequirements()){
            r.unlock(player, this);
        }
        unlocked = true;
        for(Effect e : skill.getEffects()){
            e.enable(this);
            e.reload(this);
        }
        return true;
    }
    void disable(){
        if(isUnlocked()){
            for(Effect e : skill.getEffects()){
                e.disable(this);
                e.reload(this);
            }
        }
    }
    public static enum Availability{
        PURCHASED(Color.GREEN),
        LOCKED(Color.BLACK),
        UNLOCKED(Color.YELLOW),
        LOCKED_LINKED(Color.fromRGB(127, 0, 0));
        public final Color lineColor;
        private Availability(Color lineColor){
            this.lineColor = lineColor;
        }
    }
    Config save(Config cfg){
        cfg.set("unlocked", unlocked);
        return cfg;
    }
    static SkillInstance load(SkillTreeInstance tree, Skill skill, Config cfg){
        SkillInstance instance = new SkillInstance(tree, skill);
        instance.unlocked = cfg.get("unlocked");
        return instance;
    }
}