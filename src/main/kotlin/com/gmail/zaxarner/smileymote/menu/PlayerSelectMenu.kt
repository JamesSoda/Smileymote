package com.gmail.zaxarner.smileymote.menu

import com.gmail.zaxarner.smileymote.extensions.toItemStack
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

/**
 * Created on 7/15/2017.
 */
object PlayerSelectMenu: InventoryHolder {

    val playerItems = mutableMapOf<Int, ItemStack>()

    val inv = Bukkit.createInventory(this, 54, "${ChatColor.DARK_AQUA}Select a Player")

    val playersSelecting = mutableMapOf<Player, String>()

    fun updatePlayerSelectMenu() {
        val playersInMenu = mutableListOf<Player>()

        for(p in Bukkit.getOnlinePlayers()) {
            if(p.openInventory.topInventory.holder == this) {
                playersInMenu.add(p)
            }
        }

        playerItems.clear()
        for(i in 0..Bukkit.getOnlinePlayers().size - 1) {
            val p = Bukkit.getOnlinePlayers().toList()[i] ?: continue

            playerItems.put(i, Material.SKULL_ITEM.toItemStack(1, 3, "${ChatColor.BLUE}${p.name}", listOf("${ChatColor.DARK_AQUA}Select this player?")))
        }

        for(p in playersInMenu) {
            p.openInventory(inventory)
        }
    }

    override fun getInventory(): Inventory {

        playerItems.clear()
        for(i in 0..Bukkit.getOnlinePlayers().size - 1) {
            val p = Bukkit.getOnlinePlayers().toList()[i] ?: continue

            playerItems.put(i, Material.SKULL_ITEM.toItemStack(1, 3, "${ChatColor.BLUE}${p.name}", listOf("${ChatColor.DARK_AQUA}Select this player?")))
        }

        if(playerItems.isNotEmpty()) {
            playerItems.forEach { inv.setItem(it.key, it.value) }
        }

        for (i in 0..inv.size - 1) {
            if(inv.getItem(i) == null || inv.getItem(i).type == Material.AIR) {
                inv.setItem(i, Material.STAINED_GLASS_PANE.toItemStack(1, title = "..."))
            }
        }

        return inv
    }
}