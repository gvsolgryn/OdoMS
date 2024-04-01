
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
        if (!cm.haveItem(2430043, 3)) {
            cm.sendOk("#fs11#"+검정+"#i2430043# #b#z2430043##k"+검정+" 아이템이 #r3개#k"+검정+"가 있어야 교환이 가능하네.");
            cm.dispose();
            return;
        }
        cm.gainItem(2430043, -3);
        cm.gainItem(2630127, 1);
        cm.sendOk("#fs11#"+검정+"#i2630127# #b#z2630127##k"+검정+" 아이템이 지급되었다네!");
        cm.dispose();
    }
}
