package com.thizthizzydizzy.util;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
public class ItemBuilder{
    public static ItemStack setDisplayName(Material item, String name){
        return setDisplayName(new ItemStack(item), name);
    }
    public static ItemStack setDisplayName(ItemStack item, String name){
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack dye(Material item, Color color){
        return dye(new ItemStack(item), color);
    }
    public static ItemStack dye(ItemStack item, Color color){
        ItemMeta meta = item.getItemMeta();
        if(meta instanceof LeatherArmorMeta){
            ((LeatherArmorMeta) meta).setColor(color);
            item.setItemMeta(meta);
        }
        return item;
    }
    protected final Material type;
    protected String displayName = null;
    protected ArrayList<String> lore = new ArrayList<>();
    protected Color color = null;
    protected Integer customModelData = null;
    protected ArrayList<ItemFlag> flags = new ArrayList<>();
    public ItemBuilder(Material type){
        this.type = type;
    }
    public ItemBuilder setDisplayName(String name){
        if(!name.startsWith(ChatColor.RESET.toString())){
            name = ChatColor.RESET.toString()+name;
        }
        displayName = name;
        return this;
    }
    public ItemBuilder addLore(String str){
        lore.add(str);
        return this;
    }
    public ItemBuilder addLore(Iterable<String> strs){
        for(String str : strs)addLore(str);
        return this;
    }
    public ItemBuilder addLore(String[] strs){
        for(String str : strs)addLore(str);
        return this;
    }
    public ItemBuilder dye(Color color){
        this.color = color;
        return this;
    }
    public ItemBuilder setCustomModelData(int data){
        this.customModelData = data;
        return this;
    }
    public ItemBuilder addFlag(ItemFlag flag){
        flags.add(flag);
        return this;
    }
    public ItemStack build(){
        ItemStack stack = new ItemStack(type);
        ItemMeta meta = stack.getItemMeta();
        if(displayName!=null)meta.setDisplayName(displayName);
        if(!lore.isEmpty())meta.setLore(lore);
        if(customModelData!=null){
            meta.setCustomModelData(customModelData);
        }
        if(color!=null&&meta instanceof LeatherArmorMeta){
            ((LeatherArmorMeta) meta).setColor(color);
        }
        for(ItemFlag flag : flags){
            meta.addItemFlags(flag);
        }
        stack.setItemMeta(meta);
        return stack;
    }
}