package hszy.ydy.sekiro;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.security.Key;

public class MyCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (command.getName().equalsIgnoreCase("getshield")) { // 判断输入的指令是否是 /getshield
            if (!(sender instanceof Player)) { // 判断输入者的类型 为了防止出现 控制台或命令方块 输入的情况
                sender.sendMessage("你必须是一名玩家!");
                return true; // 这里返回true只是因为该输入者不是玩家,并不是输入错指令,所以我们直接返回true即可
            }
            // 如果我们已经判断好sender是一名玩家之后,我们就可以将其强转为Player对象,把它作为一个"玩家"来处理
            Player player = (Player) sender;
            ItemStack shield;
            shield = new ItemStack(Material.SHIELD,1);
            if(!Sekiro.enchantment.equals("")){
                Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(Sekiro.enchantment));
                if(ench == null) {
                    player.sendMessage("请检查config里的附魔类型是否正确");
                }
                else{
                    if (Sekiro.enchantedlvl > 0) shield.addUnsafeEnchantment(ench, Sekiro.enchantedlvl);
                    else shield.addUnsafeEnchantment(ench, 1);
                }
            }

            player.getInventory().addItem(shield);
            return true; // 返回true防止返回指令的usage信息
        }
        return false;
    }
}
