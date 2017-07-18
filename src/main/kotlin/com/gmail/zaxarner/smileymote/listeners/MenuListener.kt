package com.gmail.zaxarner.smileymote.listeners

import com.gmail.zaxarner.smileymote.extensions.getDisplayName
import com.gmail.zaxarner.smileymote.menu.Menu
import com.gmail.zaxarner.smileymote.menu.PlayerSelectMenu
import com.gmail.zaxarner.smileymote.menus
import com.gmail.zaxarner.smileymote.plugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

/**
 * Created on 7/15/2017.
 */
object MenuListener : Listener {


    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory
        val player = event.view.player as Player

        val slot = event.rawSlot

        if (inventory.holder is Menu) {
            event.isCancelled = true

            for (menu in menus) {
                if (inventory.holder == menu) {
                    menu.menuItems[slot]?.onClick(player)
                }
            }
        }

        if(inventory.holder is PlayerSelectMenu) {
            val item = inventory.getItem(slot)
            event.isCancelled = true

            val emote = plugin.playerSelectMenu.playersSelecting[player] ?: return

            for(p in Bukkit.getOnlinePlayers()) {
                if(item.getDisplayName().contains(p.name)) {
                    Bukkit.dispatchCommand(player, "emote $emote ${p.name}")
                    player.closeInventory()
                }
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory = event.inventory
        val player = event.player

        if(inventory.holder is PlayerSelectMenu) {
            plugin.playerSelectMenu.playersSelecting.remove(player)
        }
    }
}