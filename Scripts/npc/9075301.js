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
var status = -1;
var enter = "\r\n";

별 = "#fUI/GuildMark.img/Mark/Pattern/00004014/11#"

빨강 = "#fc0xFFFF0000#";
파랑 = "#fc0xFF0000CD#";
분홍 = "#fc0xFFDB7093#";
노랑 = "#fc0xFF8B0000#";
보라 = "#fc0xFF800080#";

마스터티어 = 1142732;
레전드티어 = 1142257;
다이아 = 1142914;
핑크 = 1143810;
퍼플 = 1143811;
블루 = 1143813;
레드 = 1143203;
블랙 = 1143814;
마스터 = 1143243;
로얄 = 1142623;

function start() {
    status = -1;
    action(1, 0, 0);
}
var a = 0;
function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        var sel = 0;
        훈장 = cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(-21) == null ? 10 : cm.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(-21).getItemId();
        var msg = "#fs11#안녕하세요. #b#h0##k님 #rNERO 티어 사냥터#k 이동 서비스입니다." + enter + enter;
        if (훈장 == 마스터티어 || 훈장 == 레전드티어 || 훈장 == 다이아 || 훈장 == 핑크 || 훈장 == 퍼플 || 훈장 == 블루 || 훈장 == 레드 || 훈장 == 블랙 || 훈장 == 마스터 || 훈장 == 로얄) {
            msg += "#b#h0##k님이 갈 수 있는 사냥터 목록입니다." + enter + enter;
            switch (훈장) {
                case 로얄:
                case 마스터:
                case 블랙:
                case 레드:
                    msg += "#L6#" + 별 + 빨강 + "  #r레드 티어 사냥터#l" + enter + enter;
                case 블루:
                    msg += "#L5#" + 별 + 파랑 + "  #b블루 티어 사냥터#l" + enter + enter;
                case 퍼플:
                case 핑크:
                    msg += "#L4#" + 별 + 분홍 + "  핑크 티어 사냥터#l" + enter + enter;
                case 다이아:
                case 레전드티어:
                    msg += "#L3#" + 별 + 노랑 + "  레전드 티어 사냥터#l" + enter + enter;
                case 마스터티어:
                    msg += "#L2#" + 별 + 보라 + "  마스터 티어 사냥터#l" + enter + enter;
            }
        } else {
            msg += "#b#h0##k님은 이동할 수 없습니다." + enter + enter;
        }
        cm.sendOk(msg);
    } else if (status == 1) {
        if (selection == 2) {
            cm.warp(selection);
            cm.dispose();
        } else if (selection == 3) {
            cm.warp(selection);
            cm.dispose();
        } else if (selection == 4) {
            cm.warp(selection);
            cm.dispose();
        } else if (selection == 5) {
            cm.warp(selection);
            cm.dispose();
        } else if (selection == 6) {
            cm.warp(selection);
            cm.dispose();
        }
    }
}