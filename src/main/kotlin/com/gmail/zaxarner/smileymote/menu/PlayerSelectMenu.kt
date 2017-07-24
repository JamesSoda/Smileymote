package com.gmail.zaxarner.smileymote.menu

import com.gmail.zaxarner.smileymote.extensions.toItemStack
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

/**
 * Created on 7/15/2017.
 */
object PlayerSelectMenu {


    val playersSelecting = mutableMapOf<Player, String>()

    fun updatePlayerSelectMenu() {
        val playersInMenu = mutableListOf<Player>()

        for(p in Bukkit.getOnlinePlayers()) {
            if(p.openInventory.topInventory.name == "${ChatColor.DARK_AQUA}Select a Player") {
                playersInMenu.add(p)
            }
        }

        for(p in playersInMenu) {
            openInventory(p)
        }
    }

    fun openInventory(player: Player) {
        val inv = Bukkit.createInventory(null, 54, "${ChatColor.DARK_AQUA}Select a Player")

        val playerItems = mutableMapOf<Int, ItemStack>()

        playerItems.clear()
        var offset = 0
        for(i in 0..Bukkit.getOnlinePlayers().size - 1) {
            val p = Bukkit.getOnlinePlayers().toList()[i] ?: continue

            if(p != player) {

                val skull = Material.SKULL_ITEM.toItemStack(1, 3, "${ChatColor.BLUE}${p.name}", listOf("${ChatColor.DARK_AQUA}Select this player?"))
                val meta = skull.itemMeta as SkullMeta
                meta.owner = p.name
                skull.itemMeta = meta

                playerItems.put(i - offset, skull)
            } else {
                offset++
            }
        }

        if(playerItems.isNotEmpty()) {
            playerItems.forEach { inv.setItem(it.key, it.value) }
        }

        for (i in 0..inv.size - 1) {
            if(inv.getItem(i) == null || inv.getItem(i).type == Material.AIR) {
                inv.setItem(i, Material.STAINED_GLASS_PANE.toItemStack(1, title = "..."))
            }
        }

        player.openInventory(inv)
    }
}