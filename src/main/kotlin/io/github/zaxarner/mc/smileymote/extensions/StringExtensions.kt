package io.github.zaxarner.mc.smileymote.extensions

import org.apache.commons.lang.WordUtils
import org.bukkit.ChatColor
import java.util.*

/**
 * Created on 7/15/2017.
 */
fun String.addLineBreaks(length: Int = 40, color: ChatColor = ChatColor.RESET) : List<String> {
    var string = ChatColor.stripColor(this.trim()) ?: return arrayListOf()
    if(string.length >= length) {
        string = WordUtils.wrap(ChatColor.stripColor(this.trim()), length)
        val stringArray = string.split(System.lineSeparator().toRegex()).toTypedArray()
        val list = ArrayList<String>()
        for (s in stringArray) {
            if (s != "") {
                list.add("$color$s")
            }
        }
        return list
    } else {
        return listOf("$color$this")
    }
}

fun String.makePretty(): String {
    val res = this.replace('_', ' ')
    return WordUtils.capitalizeFully(res.substring(0, 1).toUpperCase() + res.substring(1).toLowerCase())
}