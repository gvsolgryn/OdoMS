
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
        말 = "#fs11#"+검정+"정말 이 캐릭터로 보상을 받을텐가?\r\n"
        말 += "#z2430041#는 #r최초 1회#k"+검정+"밖에 지급받지 못한다네."
        cm.sendYesNoS(말, 0x04, 9401232);
    } else if (status == 1) {
        leftslot = cm.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot();
        if (leftslot < 6) {
           cm.sendOk("#fs11##r장비창 6 칸 이상을 확보하게.");
           cm.dispose();
           return;
        }
        말 = "#fs11#"+검정+"블랙 서버에 온걸 환영하네! 이건 나의 조그마한 선물이라네.\r\n\r\n"
        말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
        말 += "#i1143065# #b#z1143065##k\r\n"
        말 += "#i1113149# #i1012478# #i1022231# #i1032241# #b보스 장신구#k 1개#k\r\n"
        말 += "#i4310312# #b#z4310312# 30개#k\r\n"
        말 += "#i5062009# #b#z5062009# 300개#k\r\n"
        말 += "#i5062010# #b#z5062010# 50개#k\r\n"
        말 += "#i5062500# #b#z5062500# 50개#k\r\n"
        말 += "#i5200002# #b10,000,000 메소#k\r\n"
        var inz = MapleItemInformationProvider.getInstance().getEquipById(1143065);
        inz.setExpiration((new Date()).getTime() + (1000 * 60 * 60 * 24 * day));
        inz.setStr(50);
        inz.setDex(50);
        inz.setInt(50);
        inz.setLuk(50);
        inz.setWatk(50);
        inz.setMatk(50);
        inz.setHp(3000);
        MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
        cm.gainItem(1113149, 1);
        cm.gainItem(1012478, 1);
        cm.gainItem(1022231, 1);
        cm.gainItem(1032241, 1);
        cm.gainItem(5062009, 300);
        cm.gainItem(5062010, 50);
        cm.gainItem(5062500, 50);
        cm.gainItem(2633610, 30);
        cm.gainMeso(10000000);
        cm.gainItem(2430041, -1);
        cm.sendOkS(말, 0x04, 9401232);
        cm.dispose();
    }
}
