package hszy.ydy.sekiro;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MyListener implements Listener {
    public static String RandomStr(){
        int random_index = (int) (Math.random()*Sekiro.tips.size());
        return Sekiro.tips.get(random_index);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e)// 监听消耗物品事件
    {
        if(e.getEntity() instanceof Player){//是玩家
            //检测手里盾的附魔
            if((((Player) e.getEntity()).getInventory().getItemInMainHand().getType() == Material.SHIELD)||(((Player) e.getEntity()).getInventory().getItemInOffHand().getType() == Material.SHIELD))
            {
                if((((Player) e.getEntity()).getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == Sekiro.enchantedlvl)||(((Player) e.getEntity()).getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.DAMAGE_ALL) == Sekiro.enchantedlvl))
                {
                    Player p1 = (Player) e.getEntity();
                    Entity p2 = e.getDamager();
                    double dmg = (e.getDamage());
                    dmg = dmg<0?dmg*(-1):dmg;
                    //弹反成功
                    if(p1.isHandRaised() && !p1.isBlocking())
                    {
                        ((Damageable) p2).damage(dmg * Sekiro.magnification,p1);
                        p1.playSound(p1.getLocation(), Sound.ENTITY_ITEM_BREAK,(float) 1.0,(float) 0.7);
                        if(p2 instanceof Player)((Player) p2).playSound(p2.getLocation(), Sound.ENTITY_ITEM_BREAK,(float) 1.0,(float) 0.7);
                        e.setCancelled(true);
                    }else if(p1.isBlocking() && e.getFinalDamage() < 0.1){
                        if(p1.getHealth() - dmg*Sekiro.punishment <= 0){
                            p1.setHealth(2.0);
                        }else {
                            p1.setHealth(p1.getHealth() - dmg*Sekiro.punishment);
                        }
                        e.setCancelled(true);
                    }else if(p1.getHealth() - e.getFinalDamage() <= 6){
                        p1.sendTitle("§4§l危",RandomStr(),30,40,30);
                        p1.playSound(p1.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
                        if(p2 instanceof Player)((Player) p2).playSound(p2.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
                    }
                }
            }
        }
    }
}

