﻿
var status = -1;
importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
function start() {
    status = -1;
    action (1, 0, 0);
}
검정 = "#fc0xFF191919#"
var day = 7; //유효기간 옵션 설정(일)
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs11#"+검정+"정말 이 캐릭터로 보상을 받을텐가?\r\n"
        말 += "선물 상자는 #r계정당 1회#k"+검정+"만 지급받는거라네!"
        cm.sendYesNoS(말, 0x04, 9401232);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2) {
           cm.sendOk("#fs11##r기타창과 설치창 2 칸 이상을 확보하게.");
           cm.dispose();
           return;
        }
        말 = "#fs11#"+검정+"선물을 지급했네! 인벤토리를 확인해보게나. 크크\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
        말 += "#i3014005# #b#z3014005# 1개#k\r\n"
        말 += "#i5068300# #b#z5068300# 20개#k\r\n"
        말 += "#i5060048# #b#z5060048# 20개#k\r\n"
        말 += "#i5069100# #b#z5069100# 5개#k\r\n"
        말 += "#i5062006# #b#z5062006# 20개#k\r\n"
        말 += "#i5062002# #b#z5062002# 10개#k\r\n"
        말 += "#i2430030# #b#z2430030# 5개#k\r\n"
        말 += "#i4310291# #b#z4310291# 5,000개#k\r\n"
        cm.gainItem(3014005, 1);
        cm.gainItem(5068300, 20);
        cm.gainItem(5060048, 20);
        cm.gainItem(5069100, 5);
        cm.gainItem(5062006, 20);
        cm.gainItem(5062002, 10);
        cm.gainItem(2430030, 5);
        cm.gainItem(4310291, 5000);
        cm.gainItem(2430020, -1);
        cm.sendOkS(말, 0x04, 9401232);
        cm.dispose();
    }
}