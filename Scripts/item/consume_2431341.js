
var status;
var select = -1;
var itemid  = new Array(4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4021037,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4033151,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4036531,4031868);

function start() {
    status = -1;
    action(1, 1, 0);
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
		cm.sendYesNoS("안녕하세요! 강화석 랜덤 꾸러미를 이용해 주셔서 감사합니다. 예를 누르시면 랜덤으로 강화석 1개가 지급됩니다.",0x24);
	} else if (status == 1) {
		아이템1 = itemid[Math.floor(Math.random() * itemid.length)];
		cm.gainItem(아이템1, 1);
		cm.gainItem(2431341, -1);
		cm.cm.sendOkS("다음 아이템이 수령되었습니다:\r\n\r\n#i" + 아이템1 + "##z" + 아이템1 + "#",0x24);
		cm.dispose();
    	}
}
