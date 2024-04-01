
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
        cm.gainItem(2631527, 1);
        cm.gainItem(4310291, 100);
        if (cm.getClient().getKeyValue("RecommendPoint") == null) {
            cm.getClient().setKeyValue("RecommendPoint", "0");
        }
        cm.getClient().setKeyValue("RecommendPoint", (parseInt(cm.getClient().getKeyValue("RecommendPoint")) + 1) +"");
        cm.gainItem(2430042, -1);
        cm.dispose();
    }
}
