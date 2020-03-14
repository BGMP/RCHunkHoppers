package cl.bgmp.rchunkhoppers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SortItemStackArray {
    public static ItemStack[] sort(ItemStack[] items) {
        int air = 0;
        ItemStack[] r = new ItemStack[items.length];
        try {
            for(int i = 0; i< items.length; i++) {
                if(items[i] == null || items[i].getType().equals(Material.AIR)) {

                    air--;
                }else {
                    r[i+air] = items[i];
                }
            }

        }catch(ArrayIndexOutOfBoundsException e) {

        }
        for(int i = 0; i< items.length; i++) {
            if(r[i] == null)
                r[i] = new ItemStack(Material.AIR);
        }
        return r;
    }
}
