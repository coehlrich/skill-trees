package com.thizthizzydizzy.skilltree.command;
import com.thizthizzydizzy.skilltree.SkillTreeCore;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
public abstract class SkillTreeCommand{
    public final String command;
    private final String permission;
    private final SkillTreeCommand[] subcommands;
    public SkillTreeCommand(String command, SkillTreeCommand... subcommands){
        this(command, command, subcommands);
    }
    public SkillTreeCommand(String command, String permission, SkillTreeCommand... subcommands){
        if(permission!=null&&permission.isEmpty())permission = null;
        if(permission!=null){
            permission = SkillTreeCore.INSTANCE.globalPrefix+"."+permission;
        }
        this.command = command;
        this.permission = permission;
        this.subcommands = subcommands;
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args, String[] fullArgs){
        if(!hasPermission(sender)){
            sender.sendMessage(ChatColor.RED+"Unknown Command");
            return true;
        }
        if(subcommands.length==0||args.length<1)return run(sender, command, label, fullArgs);
        for(SkillTreeCommand cmd : subcommands){
            if(args[0].equals(cmd.command)){
                return cmd.onCommand(sender, command, label, trim(args, 1), fullArgs);
            }
        }
        sender.sendMessage("Usage: "+getUsage());
        return true;
    }
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args){
        ArrayList<String> strs = new ArrayList<>();
        if(!hasPermission(sender))return strs;
        if(args.length==1){
            for(SkillTreeCommand cmd : subcommands){
                if(cmd.command.substring(0, cmd.command.length()-1).startsWith(args[0])&&cmd.hasPermission(sender))strs.add(cmd.command);
            }
        }
        if(args.length>1){
            for(SkillTreeCommand cmd : subcommands){
                if(args[0].equals(cmd.command))return cmd.onTabComplete(sender, command, label, trim(args, 1));
            }
        }
        return strs;
    }
    public List<String> autoTabComplete(CommandSender sender, Command command, String label, String[] args, Iterable<String> values){
        ArrayList<String> strs = new ArrayList<>();
        if(!hasPermission(sender))return strs;
        for(String s : values){
            if(s.substring(0, s.length()-1).startsWith(args[0]))strs.add(s);
        }
        return strs;
    }
    protected abstract boolean run(CommandSender sender, Command command, String label, String[] args);
    public String[] trim(String[] data, int beginning){
        if(data==null)return null;
        String[] newData = new String[Math.max(0,data.length-beginning)];
        for(int i = 0; i<newData.length; i++){
            newData[i] = data[i+beginning];
        }
        return newData;
    }
    public boolean hasPermission(CommandSender sender){
        if(permission==null)return true;
        return sender.hasPermission(permission);
    }
    protected abstract String getUsage();
}