package com.gmail.zaxarner.smileymote.menu

import com.gmail.zaxarner.smileymote.extensions.addLineBreaks
import com.gmail.zaxarner.smileymote.extensions.toItemStack
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Created on 7/15/2017.
 */
abstract class MenuItem constructor(val name: String, val material: Material, val data: Short, val description: String) {

    fun getItem() : ItemStack {
        return material.toItemStack(title = name, lore = description.addLineBreaks(color = ChatColor.DARK_AQUA))
    }

    abstract fun onClick(player: Player)

}