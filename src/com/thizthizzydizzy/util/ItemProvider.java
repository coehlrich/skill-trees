package com.thizthizzydizzy.util;
import org.bukkit.Color;
import org.bukkit.Material;
public class ItemProvider{
    public static ItemBuilder icon(Icon icon){
        return new ItemBuilder(Material.CLOCK)
                .setDisplayName(icon.name)
                .setCustomModelData(icon.data)
                .addLore(icon.lore);
    }
    public static ItemBuilder get(int customModelData){
        return get(Material.CLOCK, customModelData);
    }
    public static ItemBuilder get(Material mat, int customModelData){
        return new ItemBuilder(mat).setCustomModelData(customModelData);
    }
    public static ItemBuilder getLine(boolean n, boolean ne, boolean e, boolean se, boolean s, boolean sw, boolean w, boolean nw, Color color){
        String str = "";
        str+=n?"1":"0";
        str+=ne?"1":"0";
        str+=e?"1":"0";
        str+=se?"1":"0";
        str+=s?"1":"0";
        str+=sw?"1":"0";
        str+=w?"1":"0";
        str+=nw?"1":"0";
        return getLine(Integer.parseInt(str, 2), color);
    }
    public static ItemBuilder getLine(int i, Color color){
        return new ItemBuilder(Material.LEATHER_HELMET)
                .setDisplayName(" ")
                .setCustomModelData(10001+i)
                .dye(color);
    }
    public static enum Icon{
        CONNECT("Connect skills", 10001),
        CONNECT_AND("Require ALL connected skills", 10002),
        CONNECT_OR("Require ANY connected skill", 10003),
        LINK("Link skills", 10004),
        NUMBER("Number", 10005),
        REQUIREMENTS("Requirements", 10006),
        PERMISSION("Permission", 10007),
        EFFECTS("Effects", 10008),
        UNLOCKED("Unlocked", 10009),
        LOCKED("Locked", 10010),
        MOVE("Move", 10011),
        VISIBLE("Visible", 10012),
        HIDDEN("Hidden", 10013);
        private final String name;
        private final int data;
        private final String[] lore;
        private Icon(String name, int data, String... lore){
            this.name = name;
            this.data = data;
            this.lore = lore;
        }
    }
}