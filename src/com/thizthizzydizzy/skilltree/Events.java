package com.thizthizzydizzy.skilltree;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
public class Events implements Listener{
    private final SkillTreeCore plugin;
    public Events(SkillTreeCore plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onWorldSave(WorldSaveEvent event){
        plugin.save();
    }
}