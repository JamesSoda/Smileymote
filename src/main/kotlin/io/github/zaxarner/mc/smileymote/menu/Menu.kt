package io.github.zaxarner.mc.smileymote.menu

import io.github.zaxarner.mc.smileymote.extensions.toItemStack
import io.github.zaxarner.mc.smileymote.menus
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

/**
 * Created on 7/15/2017.
 */
class Menu constructor(val name: String, val size: Int) : InventoryHolder{

    val menuItems = mutableMapOf<Int, MenuItem>()

    init {
        menus.add(this)
    }

    fun addMenuItem(slot: Int, menuItem: MenuItem) : Boolean {
        if(slot < 0 || slot >= size) return false
        if(menuItems.keys.contains(slot)) return false
        menuItems.put(slot, menuItem)
        return true
    }

    override fun getInventory(): Inventory {
        val inventory = Bukkit.createInventory(this, size, name)

        menuItems.forEach { inventory.setItem(it.key, it.value.getItem()) }

        for (i in 0..inventory.size - 1) {
            if(inventory.getItem(i) == null || inventory.getItem(i)?.type == Material.AIR) {
                inventory.setItem(i, Material.BLACK_STAINED_GLASS_PANE.toItemStack(title = "..."))
            }
        }

        return inventory
    }
}