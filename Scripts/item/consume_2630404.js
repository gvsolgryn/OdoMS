﻿box = 2630404;
var seld = -1;

function start() {
    St = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        St++;
    }
	if (St == 0) {
	  var text = "#fs11#교환하고싶은 #b아케인 심볼#k 을 선택해주세요. \r\n동일한 아이템으로  1개가 지급됩니다.\r\n#b[장비탭을 확인후 사용해주세요]\r\n#r복구해드리지않습니다.\r\n";
	  text += "#L0##b#i1712001##z1712001# 1개\r\n";
	  text += "#L1##b#i1712002##z1712002# 1개\r\n";
	  text += "#L2##b#i1712003##z1712003# 1개\r\n";

	  text += "#L3##b#i1712004##z1712004# 1개\r\n";
	  text += "#L4##b#i1712005##z1712005# 1개\r\n";
	  text += "#L5##b#i1712006##z1712006# 1개\r\n";
	  cm.sendYesNo(text);
	} else if (St == 1) {
		seld = selection;
		cm.sendGetNumber("사용하실 아이템 갯수를 선택해주세요.", 1, 1, 100);
	} else if (St == 2) {
		if (!cm.canHold(1712001+seld, 5*selection)) {
			cm.sendOk("죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b장비#k탭의 인벤토리 공간을 비워주세요.");
			cm.dispose();
			return;
		}
		if (!cm.haveItem(box, selection)) {
			cm.sendOk("사용할 아이템이 부족합니다.");
			cm.dispose();
			return;
		}
		cm.gainItem(1712001+seld,1*selection);
		cm.gainItem(box,-1*selection);
		cm.sendOk("교환이 완료되었습니다.");
		cm.dispose();
	}
}