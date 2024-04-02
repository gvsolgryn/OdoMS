
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
	if (cm.getMap().getAllMonstersThreadsafe().size() <= 0) {
		cm.sendOk("#r(!)#k 현재 맵에는 등록된 몬스터 정보가 없습니다.");
		cm.dispose();
		return;
	}
	var selStr = "현재 위치하신 #r["+cm.getPlayer().getMap().getMapName()+"]#k의 몬스터 목록입니다.\r\n아래 중 드롭정보를 확인하고 싶으신 몬스터를 선택해주세요.\r\n#b";
	var iz = cm.getMap().getAllUniqueMonsters().iterator();
	while (iz.hasNext()) {
		var zz = iz.next();
		selStr += "#L" + zz + "##o" + zz + "##l\r\n";
	} 
	cm.sendSimple(selStr);
    } else if (status == 1) {
	cm.sendNext(cm.checkDrop(selection));
	cm.dispose();
    }
}

