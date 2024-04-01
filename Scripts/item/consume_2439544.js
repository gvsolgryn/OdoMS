
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
        말 = "#fs11#"+검정+"안녕하세요! #h #님\r\n"
        말 += "#fc0xFFFF5AD9##e285달성#n#k 보상을 수령하시겠습니까?"
        cm.sendYesNoS(말, 0x04, 9401232);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        if (leftslot < 2) {
           cm.sendOk("#fs11##r기타창 2 칸 이상을 확보해주세요.");
           cm.dispose();
           return;
        }
        말 = "#fs11#"+검정+"#fc0xFFFF5AD9##e285보상#n#k이 지급되었습니다!\r\n\r\n"	
        cm.gainItem(2049376, 2);
        cm.gainItem(2049360, 5);	
        cm.gainItem(2048753, 60);	
        cm.gainItem(5060048, 5);
        cm.gainItem(5062005, 30);		
        cm.gainItem(2439544, -1);		
        cm.sendOkS(말, 0x04, 9401232);
        cm.dispose();
    }
}
