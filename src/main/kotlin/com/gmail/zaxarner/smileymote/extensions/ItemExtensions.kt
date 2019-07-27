package com.gmail.zaxarner.smileymote.extensions

import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

/**
 * Created on 7/15/2017.
 */
fun Material.toItemStack(size: Int = 1, title: String? = null, lore: List<String>? = null) : ItemStack {
    val item = ItemStack(this, size)

    if (title != null) {
        item.setDisplayName(title)
    }

    if (lore != null) {
        item.setLore(lore)
    }


    return item
}

fun ItemStack.setDisplayName(displayName: String) : ItemStack {
    val meta = itemMeta ?: return this
    meta.setDisplayName(displayName)
    itemMeta = meta

    return this
}

fun ItemStack.getDisplayName() : String {
    val meta = itemMeta ?: return this.type.name.makePretty()
    if(meta.hasDisplayName()) {
        return meta.displayName
    } else {
        return this.type.name.makePretty()
    }
}

fun ItemStack.setLore(lore: List<String>) : ItemStack {
    val meta = itemMeta ?: return this
    meta.lore = lore
    itemMeta = meta

    return this
}

fun ItemStack.getLore() : MutableList<String> {
    val meta = itemMeta ?: return mutableListOf()
    val lore = meta.lore ?: return mutableListOf()
    return lore
}

fun ItemStack.addItemFlag(flag: ItemFlag) : ItemStack {
    val meta = itemMeta ?: return this
    meta.addItemFlags(flag)
    itemMeta = meta

    return this
}

fun ItemStack.addLore(string : String) : ItemStack {
    val meta = itemMeta ?: return this
    val lore = meta.lore ?: mutableListOf()
    lore.add(string)
    meta.lore = lore
    itemMeta = meta

    return this
}
