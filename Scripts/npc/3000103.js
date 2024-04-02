importPackage(Packages.org.extalia.handling.channel.handler);

var enter = "\r\n";
var seld = -1, seld2 = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	var msg = "#fs11#"
	if (status == 0) {
		msg += "#fs11#어서오세요 #h0#님! 무엇을 이용하시겠어요?\r\n"; 
		msg += "#fs11##d현재 #h0#님의 티어는 [#r" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade"))) + "#n#k] 입니다\r\n"; 
		msg += "#L1# #b 티어 승급을 하고 싶어요.#n #k\r\n";
		msg += "#L2# #b 티어 훈장을 제작하고 싶어요.#n #k\r\n";
		cm.sendSimpleS(msg, 4);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.openNpc(9020016);
				break;
			case 2:
				cm.dispose();
				cm.openNpc(2022003);
				break;
		}
	}
}

function NullKeyValue() {
    if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == -1) {
        cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "0");
    }
}

function getTearRank(level) {
    switch (level) {
        case 9:
            name = "프로페셔널";
            break;
        case 8:
            name = "챌린저";
            break;
        case 7:
            name = "그랜드 마스터";
            break;
        case 6:
            name = "마스터";
            break;
        case 5:
            name = "다이아몬드";
            break;
        case 4:
            name = "플래티넘";
            break;
        case 3:
            name = "골드";
            break;
        case 2:
            name = "실버";
            break;
        case 1:
            name = "브론즈";
            break;
        default:
            name = "언랭크";
            break;
    }
    return name;
}