[toc]
# Sekiro

## 简介

模仿只狼的mc小插件一般用于UHC当然也可以自己用着玩hhhh

有错误请及时反馈=_=

注入了愚下**四天**的心血

~~本来都写好了的结果不小心删了只能第二天重写一遍~~

~~还因此上午萎靡不振请假回家睡觉QwQ~~

所以请对它好点!(bushi

这次的插件算是至今以来我做过功能最多的插件~~(虽然代码也不超过一页就是了)~~（现在超过了）

由于愚下没有玩过只狼，有什么错误还请多多包涵

说起来大概是这样的:

- 只有拿着含有**指定**附魔且等级正好和config里enchantedlvl参数相同的盾才会触发插件(默认等级是4，附魔为锋利)(附魔类型和附魔等级分别在config里为enchantment和enchantedlvl)

- 举盾而未举时可以**弹反**（应该是有音效的...应该吧？），伤害倍率在config里设置magnification参数（应该是这个吧）

- 举起盾之后再受到**被盾挡住的**伤害时会受到一部分**惩罚伤害**，倍率在config里设置punishment参数

  且惩罚伤害**分三层**，分别是弹反范围内的惩罚伤害，格挡范围内的惩罚伤害和背后的惩罚伤害

- 前两层惩罚伤害**不会致死**，至少会留1hp

- 当因为没防住导致血量被打到6hp以下时显示响起音效并显示**危**和tips_wei（死前看到的嘲讽？）tips_wei可在config里设置

- 死亡时有音效并显示**死**和tips_si，tips_si可在config里设置

- 举盾时被从**背后**打到的惩罚伤害力度最大

- 弹反对远程射击无效

- 举盾时被远程射击仍然会受到惩罚伤害

## 命令列表
~~(只有一个命令也能叫列表吗!)~~

/getshield 得到一个附有可以达到弹反要求的附魔的盾牌