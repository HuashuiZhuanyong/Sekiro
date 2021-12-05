package hszy.ydy.sekiro;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class MyListener implements Listener {
    /*抽取tips*/
    public static String RandomStr(List<String> s){
        int random_index = (int) (Math.random()*s.size());
        return s.get(random_index);
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

    /*危和死特效*/
    public static void weiordie(Player p1,Entity p2,double nowhealth){
        if(nowhealth <= 6 && nowhealth > 0){
            p1.sendTitle("§4§l危",RandomStr(Sekiro.tips),30,40,30);
            p1.playSound(p1.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
            if(p2 instanceof Player)((Player) p2).playSound(p2.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 1.5);
        }else if(nowhealth <= 0){
            p1.sendTitle("§4§l死",RandomStr(Sekiro.tips2),30,40,30);
            p1.playSound(p1.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 0.9);
            if(p2 instanceof Player)((Player) p2).playSound(p2.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN,(float) 1.0,(float) 0.9);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e)// 监听实体攻击事件
    {
        if(e.getEntity() instanceof Player){//是玩家
            Player p1 = (Player) e.getEntity();
            Entity p2 = e.getDamager();
            ItemStack shield_1,shield_2;
            shield_1=new ItemStack(Material.SHIELD);
            shield_2=new ItemStack(Material.SHIELD);
            boolean have_shield=false;
            if(p1.getInventory().getItemInMainHand().getType() == Material.SHIELD){
                shield_1=p1.getInventory().getItemInMainHand();
                have_shield=true;
            }
            if(p1.getInventory().getItemInOffHand().getType() ==Material.SHIELD){
                shield_2=p1.getInventory().getItemInOffHand();
                have_shield=true;
            }
            //检测手里盾的附魔
            if(have_shield)
            {
                boolean flag_ = false;//检测是否需要盾上有指定附魔
                if(Sekiro.enchantment.equals("")){flag_=true;}
                else {
                    Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(Sekiro.enchantment));
                    if(Sekiro.enchantedlvl >= 0) {
                        if ((shield_1.getEnchantmentLevel(ench) == Sekiro.enchantedlvl) || shield_2.getEnchantmentLevel(ench) == Sekiro.enchantedlvl) {
                            flag_ = true;
                        }
                    }else {
                        if ((shield_1.getEnchantmentLevel(ench) >= 1) || (shield_2.getEnchantmentLevel(ench) >= 1)) {
                            flag_ = true;
                        }
                    }
                }
                if(flag_){
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
                            weiordie(p1,p2,p1.getHealth() - e.getFinalDamage());
                        }
                    }
                    else {
                        //低血量特效
                        weiordie(p1,p2,p1.getHealth() - e.getFinalDamage());
                    }
                }
            }
        }
    }
}

