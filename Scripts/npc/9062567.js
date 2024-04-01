


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    닉스 에 의해 만들어 졌습니다.

    엔피시아이디 : 9110007

    엔피시 이름 : 로보

    엔피시가 있는 맵 : 몬스터파크 : 몬스터파크 (951000000)

    엔피시 설명 : 라면 요리사


*/
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
var items = [
	[[1032330, 1], 20], // [[아이템 코드, 개수], 확률]
	[[1012757, 1], 20], 
	[[1113316, 1], 19], 
	[[1122443, 1], 18], 
	[[2430030, 1], 18],

	[[1113130, 1], 1],
	[[1113131, 1], 1],
	[[1113132, 1], 1],
	[[1113133, 1], 1],
	[[1122151, 1], 1],
];
var amount = 1;
var coin = 4031217; // 코인
var coinA = 5; // 코인 양
var 최대확률 = 100; // 테스트용 건들 ㄴㄴ

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
        말 = "#fs11##fc0xFF5F00FF##e[HEINZ] 황금 상자#n \r\n#fc0xFF191919# 황금빛 열쇠를 이용해 희귀한 아이템를 획득 할 수 있습니다!#k\r\n\r\n#fs11#"
        말 += "상자를 열기 위해선 #i4031217# #r#z4031217# 5개#k가 필요합니다.\r\n";
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        말 += "#L0##fc0xFF0054FF#황금빛 상자를 열어 아이템을 뽑고 싶어요.\r\n\r\n"
        말 += "#L1##fc0xFF8041D9#상자에 등장하는 아이템을 확인하고 싶어요.\r\n\r\n"
        cm.sendSimpleS(말, 0x04, 9062567);
    } else if (status == 1) {
        if (selection == 0) {
            if (!cm.haveItem(4031217, 5)) {
                cm.sendOkS("#fs11#황금상자를 열기위한 위한 열쇠가 부족합니다.", 0x04, 9062567);
                cm.dispose();
                return;
            }
	for (var i = 0; i < items.length; i++) {
		if (!cm.canHold(items[i][0][0])) {
                		cm.sendOk("#fs11#인벤토리에 공간이 부족합니다.");
                		cm.dispose();
			break;
		}
	}
	for (var i = 0; i < amount; i++) {
		var percent = 0;
		var chance = Randomizer.rand(0, 최대확률 * 10);
		suc = false;
		for (var j = 0; j < items.length; j++) {
			percent += items[j][1] * 10;
			if (percent >= chance) {
				cm.dispose();
				cm.sendOk("#fs11#축하합니다. 아래와 같은 아이템을 획득 했습니다!!\r\n\r\n획득한 아이템 : #i" + items[j][0][0] + "##z" + items[j][0][0] + "#");
           			if (j > 1) Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 황금 상자에서 " + cm.getItemName(items[j][0][0]) + "을(를) 찾았습니다."));
				cm.gainItem(items[j][0][0], 1);
				cm.gainItem(coin, -coinA);
				suc = true;
				break;
			}
		}
	}
	if(suc == false) {
		cm.sendOk("#fs11#아무것도 당첨되지 않았습니다..");
		cm.gainItem(coin, -coinA);
		cm.dispose();
	}
        } else if (selection == 1) {
            말 = "#fs11#룰렛 아이템 리스트 입니다.\r\n\r\n"
            for (var a = 0; a < items.length; a++) {
                말 += "#fs11##i"+items[a][0][0]+"# #b#z"+items[a][0][0]+"##k  #Cgray#확률 : " + items[a][1] + "%\r\n"
                if (a == 30) break;
            }
	noItemR = 100;
	for (var i = 0; i < items.length; i++) {
		noItemR -= items[i][1]
	}
	말 += "#fs11#\r\n#d         꽝 확률 : " + noItemR.toFixed(1) + "%";
            cm.sendOkS(말, 0x04, 9062567);
            cm.dispose();
        } else if (selection == 99) {
            cm.dispose();
        }
    }
}