package io.github.zaxarner.mc.smileymote.listeners

import io.github.zaxarner.mc.smileymote.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * Created on 7/15/2017.
 */
object ChatListener : Listener{

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if(!event.player.hasPermission("smileymote.smiley")) return
        var message = event.message

        val smileySection = plugin.config.getConfigurationSection("smileys") ?: return


        for(s in smileySection.getKeys(false)) {
            val input = smileySection.getString("$s.input") ?: continue
            if(message.contains(input, false)) {
                val output = smileySection.getString("$s.output") ?: continue

                message = message.replace(input, output, false)
            }
        }

        event.message = message
    }
}