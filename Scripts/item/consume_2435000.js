var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		cm.sendAcceptDecline("수락을 누르시면 캐릭터 슬롯 1칸을 확장하실 수 있어요.\r\n\r\n#e지금 바로 캐릭터 슬롯을 확장 하시겠어요?#n");
	} else if (status == 1) {
		var haveitem = -1;
		if (!cm.haveItem(2435000, 1)) {
			cm.sendOk("errcode -31");
			cm.dispose();
			return;
		}
		if (cm.getClient().getCharacterSlots() < 48) {
			cm.gainItem(2435000, -1);
			cm.sendOk("캐릭터 슬롯이 1칸 확장되었습니다. 현재 보유하고 계신 캐릭터 슬롯은 #r" + cm.getClient().getCharacterSlots() + "개#k 입니다.");
			cm.getClient().getCharacterSlots();
		} else {
			cm.sendOk("캐릭터 슬롯이 최대치 이신거 같은데요?");
		}
		cm.dispose();
	}
}