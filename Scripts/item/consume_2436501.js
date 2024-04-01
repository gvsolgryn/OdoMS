
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
var day = 14; //유효기간 옵션 설정(일)
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
        말 = "#fs11#"+검정+"정말 이 캐릭터로 보상을 받으시겠습니까?\r\n"
        말 += "닉스 서버 오픈 선물 상자는 #r계정당 1회#k"+검정+"만 받을 수 있습니다! (꼭 인벤토리의 공간을 여유롭게남겨두세요)"
        cm.sendYesNoS(말, 0x04, 9401232);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot();
        leftslot1 = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 2 && leftslot1 < 2) {
           cm.sendOk("#fs11##r장비창과 기타창 2 칸 이상을 확보하게.");
           cm.dispose();
           return;
        }
        말 = "#fs11#"+검정+"선물이 지급되었습니다. 인벤토리를 확인해주세요.\r\n\r\n"
        cm.gainItem(5062009, 100); //레큐
        cm.gainItem(5062010, 100); //블큐
        cm.gainItem(5062500, 100); //에디
        cm.gainItem(2049704, 5); //레잠
        cm.gainItem(4001715, 20); //메소
        cm.gainItem(2435719, 500); //젬
        cm.gainItem(2437750, 200); //심볼
        cm.gainItem(1114324, 1); //플레임링
        cm.gainItem(2450064, 4); //경쿠
        cm.gainItem(5121104, 4); //경뿌
        cm.gainItem(2048716, 100); //강환불
        cm.gainItem(2048717, 100); //영환불
        cm.gainItem(2633912, 1); //무기
        cm.gainItem(2633913, 4); //방어구
        cm.gainItem(2633617, 2); //209비약
        cm.gainItem(2633618, 2); //219비약
        cm.gainItem(2633619, 2); //229비약
        cm.gainItem(2633620, 2); //239태성비
        cm.gainItem(2436501, -1); //회수
        cm.sendOkS(말, 0x04, 9401232);
        cm.dispose();
    }
}
