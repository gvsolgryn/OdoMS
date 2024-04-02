


/*

	* 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

	* (Guardian Project Development Source Script)

	화이트 에 의해 만들어 졌습니다.

	엔피시아이디 : 2004

	엔피시 이름 : 토드

	엔피시가 있는 맵 : The Black : Night Festival (100000000)

	엔피시 설명 : MISSINGNO


*/
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
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

function start() {
    status = -1;
    action (1, 0, 0);
}

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
        if (cm.getClient().getQuestStatus(50003) == 1 && cm.getClient().getCustomKeyValue(50003, "1") != 1) {
            cm.getClient().setCustomKeyValue(50003, "1", "1");
        }
        cm.Entertuto(true);
    } else if (status == 1) {
        cm.sendScreenText("                  #fs30##fc0xFF4641D9#닉스#k 승급 제도 안내", false);
    } else if (status == 2) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F#         첫번째#k, 닉스 슈피겔만한테 칭호를 받아주세요.", false);
    } else if (status == 3) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F#       두번째#k, 승급에 필요한 레벨 조건을 달성해주세요.", false);
    } else if (status == 4) {
        cm.sendScreenText("#fs22##fc0xFFF15F5F#      세번째#k, 아래의 재료를 승급 조건에 맞게 모아주세요.", false);
    } else if (status == 5) {
        cm.sendScreenText("　　     #fs15##i4310010# #fc0xFF4374D9##z4310010##k　#i4310291# #fc0xFF4374D9##z4310291##k　#i5200002# #fc0xFF4374D9#승급에 필요한 메소#k\r\n", false);
    } else if (status == 6) {
        cm.sendScreenText("#fs22#             위 조건을 모두 만족한다면 승급이 가능합니다.", true);
    } else if (status == 7) {
        cm.sendScreenText("#fs30##fc0xFF4641D9#승급이란?#k\r\n", false);
    } else if (status == 8) {
        cm.sendScreenText("#fs22#닉스 페스티벌 칭호는 총 #fc0xFFCC723D#3가지#k로 분류되어 있습니다.", false);
    } else if (status == 9) {
        cm.sendScreenText("#fc0xFFF15F5F#첫번째#k, 닉스 페스티벌 뉴비.", false);
    } else if (status == 10) {
        cm.sendScreenText("#fc0xFFF15F5F#두번째#k, 닉스 페스티벌 적응자.", false);
    } else if (status == 11) {
        cm.sendScreenText("#fc0xFFF15F5F#세번째#k, 닉스 페스티벌 마스터.", false);
    } else if (status == 12) {
        cm.sendScreenText("#fc0xFFCC3D3D#한단계#k씩 승급이 가능하며, 승급 시 기존보다 성능이 더 높은 #fc0xFF6B66FF#옵션#k의 칭호를 획득하실 수 있습니다.", true);
    } else if (status == 13) {
        cm.Endtuto()
        cm.dispose();
    }
}
