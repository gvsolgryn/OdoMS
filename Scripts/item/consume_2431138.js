
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
    action(1, 0, 0);
}
검정 = "#fc0xFF191919#"
var names = [["유니온 프리패스 이용권", 2430046, 1], ["메소", 0], ["명성치", 2432970, 10], ["블루밍 코인", 500], ["코어 젬스톤 100개", 2435719, 100], ["아케인 심볼", 2439301, 3], ["큐브", 2439301, 3], ["환생의 불꽃", 2439301, 3], ["보스 입장 초기화 티켓", 2430029, 10]]
var day = 7; //유효기간 옵션 설정(일)
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
        말 = "#fs11#테스트 서버 지원입니다.\r\n\r\n#b"
        말 += "#L999##r#e[성장 퀘스트]#n#k#b블루밍 레이스 선행 퀘스트 클리어\r\n\r\n"
        for (var a = 0; a < names.length; a++) {
            말 += "#L" + a + "#" + names[a][0] + " 받기 (200Lv 이상 계정당 1일 1회)\r\n"
        }
        cm.sendSimpleS(말, 4, 9010061);
    } else if (status == 1) {
        if (selection == 999) {
            cm.sendOkS("선행 퀘스트 완료!", 4, 9010061);
            if (cm.getPlayer().getQuestStatus(50006) == 1) {
                cm.getPlayer().setKeyValue(50006, "1", "1");
            }
            cm.dispose();
        } else {
            var keyvalue = "Tester" + selection;

            if (cm.getClient().getKeyValue(keyvalue) == null) {
                cm.getClient().setKeyValue(keyvalue, "0");
            }

            if (parseInt(cm.getClient().getKeyValue(keyvalue)) == 1) {
                cm.sendOkS("오늘은 #r" + names[selection][0] + "#k을 받아가셨어요.", 4, 9010061);
                cm.dispose();
                return;
            }
            if (selection == 1) {
                //메소
                cm.gainMeso(1000000000);
            } else if (selection == 3) {
                //블루밍
                cm.getPlayer().setKeyValue(100794, "point", (cm.getPlayer().getKeyValue(100794, "point") + 500) + "");
            } else if (selection == 6) {
                //큐브
                cm.gainItem(5062009, 100);
                cm.gainItem(5062010, 100);
                cm.gainItem(5062500, 100);
            } else if (selection == 7) {
                //환생의 불꽃
                cm.gainItem(2048716, 100);
                cm.gainItem(2048717, 100);
                cm.gainItem(2048753, 100);
            } else {
                cm.gainItem(names[selection][1], names[selection][2]);
            }
            cm.getClient().setKeyValue(keyvalue, "1");
            cm.sendOkS("#b" + names[selection][0] + "#k을 지급해드렸어요.", 4, 9010061);
            cm.dispose();
        }


    }
}
