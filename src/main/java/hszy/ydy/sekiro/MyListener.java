package hszy.ydy.sekiro;

import org.bukkit.Location;
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
import org.bukkit.util.Vector;

public class MyListener implements Listener {
    /*抽取tips*/
    public static String RandomStr(){
        int random_index = (int) (Math.random()*Sekiro.tips.size());
        return Sekiro.tips.get(random_index);
    }

    /*判断弹反角度*/
    public static boolean isAllowBlock(Location e1,Location e2,Location eyeLocation,int angle){
        /*e1是自己的坐标e2是别人的坐标*/
        //视线方向矢量
        Vector sight = eyeLocation.getDirection();
        //对方相对于自己矢量
        Vector dmger = e2.subtract(e1).toVector();
        //检测夹角
        return (dmger.angle(sight)<=angle);
    }

    /*判断惩罚伤害*/
    public static double punish(Location e1,Location e2,Location eye){
        double re;
        if(isAllowBlock(e1,e2,eye,Sekiro.angle))re=Sekiro.punishment1;
        else if(isAllowBlock(e1,e2,eye,90))re=Sekiro.punishment2;
        else re=Sekiro.punishment3;
        return re;
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
                    //flag负责限定弹反范围
                    boolean flag = isAllowBlock(p1.getLocation(),p2.getLocation(),p1.getEyeLocation(),Sekiro.angle);
                    //弹反成功
                    if(p1.isHandRaised() && !p1.isBlocking() && flag)
                    {
                        ((Damageable) p2).damage(dmg * Sekiro.magnification,p1);
                        p1.playSound(p1.getLocation(), Sound.ENTITY_ITEM_BREAK,(float) 1.0,(float) 1);
                        if(p2 instanceof Player)((Player) p2).playSound(p2.getLocation(), Sound.ENTITY_ITEM_BREAK,(float) 1.0,(float) 1);
                        e.setCancelled(true);
                    }/*格挡成功*/
                    else if(p1.isBlocking()){
                        double punishdmg=dmg*punish(p1.getLocation(),p2.getLocation(),p1.getEyeLocation());
                        //总不能格挡的时候给震死吧
                        if(e.getFinalDamage()<0.1) {//挡住了(两种情况)
                            if (p1.getHealth()-punishdmg > 0) {
                                p1.setHealth(p1.getHealth()-punishdmg);
                            }
                            e.setCancelled(true);
                        }else {//没挡住(背后)
                            e.setDamage(punishdmg);
                            //低血量特效
                            if(p1.getHealth() - e.getFinalDamage() <= 6){
                                p1.sendTitle("§4§l危",RandomStr(),30,40,30);
                                p1.playSound(p1.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
                                if(p2 instanceof Player)((Player) p2).playSound(p2.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
                            }
                        }
                    }
                    else {
                        //低血量特效
                        if(p1.getHealth() - e.getFinalDamage() <= 6){
                            p1.sendTitle("§4§l危",RandomStr(),30,40,30);
                            p1.playSound(p1.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
                            if(p2 instanceof Player)((Player) p2).playSound(p2.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
                        }
                    }
                }
            }
        }
    }
}

