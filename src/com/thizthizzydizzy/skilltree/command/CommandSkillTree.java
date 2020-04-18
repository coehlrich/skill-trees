package com.thizthizzydizzy.skilltree.command;
import com.thizthizzydizzy.skilltree.SkillTree;
import com.thizthizzydizzy.skilltree.SkillTreeCore;
import com.thizthizzydizzy.skilltree.menu.MenuSkillTreeInstance;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class CommandSkillTree implements TabExecutor{
    private final SkillTreeCore plugin;
    public CommandSkillTree(SkillTreeCore plugin){
        this.plugin = plugin;
    }
    private final ArrayList<SkillTreeCommand> commands = new ArrayList<>();
    {
        commands.add(new SkillTreeCommand("create"){
            @Override
            protected boolean run(CommandSender sender, Command command, String label, String[] args){
                if(args.length!=2){
                    sender.sendMessage("Usage: /skilltree create <name>\n<name> Should be a namespaced ID comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                String name = args[1];
                for(char c : name.toCharArray()){
                    if(c=='_'||c==':')continue;
                    if(Character.isDigit(c))continue;
                    if(Character.isLetter(c)&&Character.isLowerCase(c))continue;
                    sender.sendMessage("Invalid name!\nProvide a namespaced identifier comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                String[] strs = name.split(":");
                if(strs.length>2){
                    sender.sendMessage("Invalid name!\nProvide a namespaced identifier comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                if(strs.length==1)strs = new String[]{SkillTreeCore.INSTANCE.globalPrefix,strs[0]};
                if(plugin.hasSkillTree(strs[0],strs[1])){
                    sender.sendMessage("Skill tree "+strs[0]+":"+strs[1]+" already exists!");
                    return true;
                }
                if(sender instanceof Player){
                    plugin.createAndModifySkillTree(strs[0], strs[1], (Player)sender);
                }else{
                    plugin.createSkillTree(strs[0],strs[1]);
                }
                return true;
            }
            @Override
            protected String getUsage(){
                return "create <name>";
            }
        });
        commands.add(new SkillTreeCommand("delete"){
            @Override
            protected boolean run(CommandSender sender, Command command, String label, String[] args){
                if(!(sender instanceof Player)){
                    sender.sendMessage("You are not a player!");
                    return true;
                }
                if(args.length!=2){
                    sender.sendMessage("Usage: /skilltree delete <name>\n<name> Should be a namespaced ID comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                String name = args[1];
                for(char c : name.toCharArray()){
                    if(c=='_'||c==':')continue;
                    if(Character.isDigit(c))continue;
                    if(Character.isLetter(c)&&Character.isLowerCase(c))continue;
                    sender.sendMessage("Invalid name!\nProvide a namespaced identifier comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                String[] strs = name.split(":");
                if(strs.length>2){
                    sender.sendMessage("Invalid name!\nProvide a namespaced identifier comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                if(strs.length==1)strs = new String[]{SkillTreeCore.INSTANCE.globalPrefix,strs[0]};
                SkillTree tree = plugin.getSkillTree(strs[0],strs[1]);
                if(tree==null){
                    sender.sendMessage("Skill tree "+strs[0]+":"+strs[1]+" does not exist!");
                    return true;
                }
                if(tree.skills.size()>1){
                    sender.sendMessage("Skill tree is not empty! Please delete all skills first!");
                    return true;
                }
                plugin.deleteSkillTree(strs[0],strs[1]);
                sender.sendMessage("Deleted "+strs[0]+":"+strs[1]);
                return true;
            }
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
                ArrayList<String> skillTrees = new ArrayList<>();
                for(SkillTree tree : plugin.getSkillTrees())skillTrees.add(tree.namespace+":"+tree.name);
                return autoTabComplete(sender, command, label, args, skillTrees);
            }
            @Override
            protected String getUsage(){
                return "delete <name>";
            }
        });
        commands.add(new SkillTreeCommand("modify"){
            @Override
            protected boolean run(CommandSender sender, Command command, String label, String[] args){
                if(!(sender instanceof Player)){
                    sender.sendMessage("You are not a player!");
                    return true;
                }
                if(args.length!=2){
                    sender.sendMessage("Usage: /skilltree modify <name>\n<name> Should be a namespaced ID comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                String name = args[1];
                for(char c : name.toCharArray()){
                    if(c=='_'||c==':')continue;
                    if(Character.isDigit(c))continue;
                    if(Character.isLetter(c)&&Character.isLowerCase(c))continue;
                    sender.sendMessage("Invalid name!\nProvide a namespaced identifier comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                String[] strs = name.split(":");
                if(strs.length>2){
                    sender.sendMessage("Invalid name!\nProvide a namespaced identifier comprised of lowercase alphanumeric characters and underscores\nIf no namespace is provided, a default namespace of "+SkillTreeCore.INSTANCE.globalPrefix+" will be used");
                    return true;
                }
                if(strs.length==1)strs = new String[]{SkillTreeCore.INSTANCE.globalPrefix,strs[0]};
                if(!plugin.hasSkillTree(strs[0],strs[1])){
                    sender.sendMessage("Skill tree "+strs[0]+":"+strs[1]+" does not exist!");
                    return true;
                }
                plugin.modifySkillTree(strs[0],strs[1], (Player) sender);
                return true;
            }
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
                ArrayList<String> skillTrees = new ArrayList<>();
                for(SkillTree tree : plugin.getSkillTrees())skillTrees.add(tree.namespace+":"+tree.name);
                return autoTabComplete(sender, command, label, args, skillTrees);
            }
            @Override
            protected String getUsage(){
                return "modify <name>";
            }
        });
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(args.length<1){
            sender.sendMessage("Usage: /skilltree create <name> | modify <name>");
            return true;
        }
        for(SkillTreeCommand cmd : commands){
            if(args[0].equals(cmd.command)){
                return cmd.onCommand(sender, command, label, trim(args, 1), args);
            }
        }
        sender.sendMessage("Usage: /skilltree create <name> | modify <name>");
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        ArrayList<String> strs = new ArrayList<>();
        if(args.length==1){
            for(SkillTreeCommand cmd : commands){
                if(cmd.command.substring(0, cmd.command.length()-1).startsWith(args[0])&&cmd.hasPermission(sender))strs.add(cmd.command);
            }
        }
        if(args.length>1){
            for(SkillTreeCommand cmd : commands){
                if(args[0].equals(cmd.command))return cmd.onTabComplete(sender, command, label, trim(args, 1));
            }
        }
        return strs;
    }
    public String[] trim(String[] data, int beginning){
        if(data==null)return null;
        String[] newData = new String[Math.max(0,data.length-beginning)];
        for(int i = 0; i<newData.length; i++){
            newData[i] = data[i+beginning];
        }
        return newData;
    }
}