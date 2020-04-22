package com.thizthizzydizzy.skilltree;
import com.thizthizzydizzy.skilltree.requirement.RequirementPermission;
import com.thizthizzydizzy.skilltree.requirement.Requirement;
import com.thizthizzydizzy.skilltree.effect.Effect;
import com.thizthizzydizzy.skilltree.command.CommandSkillTree;
import com.thizthizzydizzy.skilltree.menu.MenuSkillTree;
import com.thizthizzydizzy.skilltree.requirement.RequirementTreeExperience;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import simplelibrary.config2.Config;
import simplelibrary.config2.ConfigList;
/**
 * @author ThizThizzyDizzy
 */
public class SkillTreeCore extends JavaPlugin{
    public static SkillTreeCore INSTANCE;
    public String globalPrefix = "skilltree";
    public String requirementsPrefix = ChatColor.DARK_GREEN+""+ChatColor.BOLD+"Requirements:";
    public String requirementFormat = " "+ChatColor.DARK_RED+"X"+ChatColor.RESET+" {0}";
    public String requirementMetFormat = " "+ChatColor.DARK_GREEN+"O"+ChatColor.RESET+" {0}";
    public String effectsPrefix = ChatColor.DARK_GREEN+""+ChatColor.BOLD+"Effects:";
    public String effectFormat = " {0}";
    public HashMap<String, ArrayList<SkillTree>> skillTrees = new HashMap<>();
    public ArrayList<Requirement> requirements = new ArrayList<>();
    public ArrayList<Effect> effects = new ArrayList<>();
    public void registerRequirement(Requirement requirement){
        for(Requirement r : requirements){
            if(r.namespace.equals(requirement.namespace)&&r.name.equals(requirement.name)){
                throw new IllegalArgumentException("Requirement "+r.namespace+":"+r.name+" Has already been registered!");
            }
        }
        requirements.add(requirement);
    }
    public void registerEffect(Effect effect){
        for(Effect e : effects){
            if(e.namespace.equals(effect.namespace)&&e.name.equals(effect.name)){
                throw new IllegalArgumentException("Effect "+e.namespace+":"+e.name+" Has already been registered!");
            }
        }
        effects.add(effect);
    }
    @Override
    public void onEnable(){
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = getLogger();
        //<editor-fold defaultstate="collapsed" desc="Register Events">
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Events(this), this);
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Config">
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        //</editor-fold>
        globalPrefix = getConfig().getString("prefix", globalPrefix);
        requirementsPrefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("requirements-prefix", requirementsPrefix));
        requirementFormat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("requirement-format", requirementFormat));
        requirementMetFormat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("requirement-met-format", requirementMetFormat));
        effectsPrefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("effects-prefix", effectsPrefix));
        effectFormat = ChatColor.translateAlternateColorCodes('&', getConfig().getString("effect-format", effectFormat));
        INSTANCE = this;
        registerRequirements();
        getCommand("skilltree").setExecutor(new CommandSkillTree(this));
        load();
        logger.log(Level.INFO, "{0} has been enabled! (Version {1}) by ThizThizzyDizzy", new Object[]{pdfFile.getName(), pdfFile.getVersion()});
    }
    private void registerRequirements(){
        registerRequirement(new RequirementPermission());
        registerRequirement(new RequirementTreeExperience());
    }
    @Override
    public void onDisable(){
        PluginDescriptionFile pdfFile = getDescription();
        Logger logger = getLogger();
        save();
        logger.log(Level.INFO, "{0} has been disabled! (Version {1}) by ThizThizzyDizzy", new Object[]{pdfFile.getName(), pdfFile.getVersion()});
    }
    public SkillTree getSkillTree(String namespace, String name){
        if(!skillTrees.containsKey(namespace))return null;
        ArrayList<SkillTree> trees = skillTrees.get(namespace);
        for(SkillTree tree : trees){
            if(tree.name.equals(name))return tree;
        }
        return null;
    }
    public boolean hasSkillTree(String namespace, String name){
        return getSkillTree(namespace, name)!=null;
    }
    public SkillTree getSkillTree(String namespacedID){
        String[] strs = namespacedID.split(":");
        if(strs.length!=2)throw new IllegalArgumentException(namespacedID+" is not a namespaced ID!");
        return getSkillTree(strs[0],strs[1]);
    }
    public boolean hasSkillTree(String namespacedID){
        return getSkillTree(namespacedID)!=null;
    }
    public void createSkillTree(String namespace, String name){
        if(hasSkillTree(namespace, name))throw new IllegalArgumentException("Skill tree "+namespace+":"+name+" already exists!");
        SkillTree tree = new SkillTree(namespace, name);
        addSkillTree(namespace, tree);
    }
    private void addSkillTree(String namespace, SkillTree tree){
        if(skillTrees.containsKey(namespace)){
            skillTrees.get(namespace).add(tree);
        }else{
            ArrayList<SkillTree> trees = new ArrayList<>();
            trees.add(tree);
            skillTrees.put(namespace, trees);
        }
    }
    public void createAndModifySkillTree(String namespace, String name, Player player){
        createSkillTree(namespace, name);
        modifySkillTree(namespace, name, player);
    }
    public void modifySkillTree(String namespace, String name, Player player){
        SkillTree tree = getSkillTree(namespace, name);
        if(tree==null)throw new IllegalArgumentException("Skill tree "+namespace+":"+name+" does not exist!");
        new MenuSkillTree(null, this, player, tree, MenuSkillTree.Type.MODIFY).openInventory();
    }
    public SkillTreeInstance getSkillTreeInstance(UUID uid){
        for(SkillTree tree : getSkillTrees()){
            for(SkillTreeInstance instance : tree.instances){
                if(instance.uid.equals(uid))return instance;
            }
        }
        return null;
    }
    public ArrayList<SkillTree> getSkillTrees(){
        ArrayList<SkillTree> trees = new ArrayList<>();
        for(ArrayList<SkillTree> trs : skillTrees.values()){
            for(SkillTree tree : trs){
                trees.add(tree);
            }
        }
        return trees;
    }
    void save(){
        //<editor-fold defaultstate="collapsed" desc="Saving skill trees">
        File file = new File(getDataFolder(), "skilltrees.dat");
        File temp = new File(getDataFolder(), "skilltrees-saving.dat");
        if(temp.exists()){
            if(!file.exists()){
                temp.renameTo(file);
            }else{
                temp.delete();
            }
        }
        Config config = Config.newConfig(temp);
        for(String namespace : skillTrees.keySet()){
            ConfigList namespaceTrees = new ConfigList();
            for(SkillTree tree : skillTrees.get(namespace)){
                namespaceTrees.add(tree.save(Config.newConfig()));
            }
            config.set(namespace, namespaceTrees);
        }
        config.save();
        file.delete();
        temp.renameTo(file);
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Saving instances">
        file = new File(getDataFolder(), "instances.dat");
        temp = new File(getDataFolder(), "instances-saving.dat");
        if(temp.exists()){
            if(!file.exists()){
                temp.renameTo(file);
            }else{
                temp.delete();
            }
        }
        config = Config.newConfig(temp);
        for(String namespace : skillTrees.keySet()){
            ConfigList namespaceTrees = new ConfigList();
            for(SkillTree tree : skillTrees.get(namespace)){
                Config cfg = Config.newConfig();
                cfg.set("namespace", tree.namespace);
                cfg.set("name", tree.name);
                ConfigList instances = new ConfigList();
                for(SkillTreeInstance instance : tree.instances){
                    instances.add(instance.save(Config.newConfig()));
                }
                cfg.set("instances", instances);
                namespaceTrees.add(cfg);
            }
            config.set(namespace, namespaceTrees);
        }
        config.save();
        file.delete();
        temp.renameTo(file);
//</editor-fold>
    }
    void load(){
        //<editor-fold defaultstate="collapsed" desc="Loading skilltrees">
        File file = new File(getDataFolder(), "skilltrees.dat");
        File temp = new File(getDataFolder(), "skilltrees-saving.dat");
        if(temp.exists()){
            if(!file.exists()){
                temp.renameTo(file);
            }else{
                temp.delete();
            }
        }
        if(!file.exists()){
            getLogger().log(Level.WARNING, "There's nothing to load! (Skill Trees)");
        }
        Config config = Config.newConfig(file);
        config.load();
        skillTrees.clear();
        for(String namespace : config.properties()){
            ConfigList namespaceTrees = config.get(namespace);
            for(int i = 0; i<namespaceTrees.size(); i++){
                Config cfg = namespaceTrees.get(i);
                SkillTree tree = SkillTree.load(cfg);
                if(tree==null){
                    getLogger().log(Level.SEVERE, "Failed to load skill tree {0} #{1}!", new Object[]{namespace, i});
                    continue;
                }
                addSkillTree(namespace, tree);
            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Loading instances">
        file = new File(getDataFolder(), "instances.dat");
        temp = new File(getDataFolder(), "instances-saving.dat");
        if(temp.exists()){
            if(!file.exists()){
                temp.renameTo(file);
            }else{
                temp.delete();
            }
        }
        if(!file.exists()){
            getLogger().log(Level.WARNING, "There's nothing to load! (Instances)");
        }
        config = Config.newConfig(file);
        config.load();
        for(String namespace : config.properties()){
            ConfigList namespaceTrees = config.get(namespace);
            for(int i = 0; i<namespaceTrees.size(); i++){
                Config cfg = namespaceTrees.get(i);
                SkillTree tree = getSkillTree(cfg.get("namespace"), cfg.get("name"));
                if(tree==null){
                    getLogger().log(Level.SEVERE, "Failed to load skill tree {0} #{1} for instances!", new Object[]{namespace, i});
                    continue;
                }
                ConfigList instances = cfg.get("instances");
                for(int j = 0; j<instances.size(); j++){
                    Config instance = instances.get(j);
                    tree.instances.add(SkillTreeInstance.load(tree, instance));
                }
            }
        }
//</editor-fold>
    }
    public void deleteSkillTree(String namespace, String name){
        deleteSkillTree(getSkillTree(namespace, name));
    }
    public void deleteSkillTree(SkillTree tree){
        if(tree==null)return;
        if(skillTrees.containsKey(tree.namespace)){
            ArrayList<SkillTree> trees = skillTrees.get(tree.namespace);
            trees.remove(tree);
            if(trees.isEmpty()){
                skillTrees.remove(tree.namespace);
            }
        }
    }
    public void copySkillTree(String fromNamespace, String fromName, String toNamespace, String toName){
        SkillTree from = getSkillTree(fromNamespace, fromName);
        Config cfg = from.save(Config.newConfig());
        cfg.set("namespace", toNamespace);
        cfg.set("name", toName);
        addSkillTree(toNamespace, SkillTree.load(cfg));
    }
}