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
	if (status == 0) {
		var msg = "#fs11#어서오세요 #h0#님! 무엇을 이용하시겠어요?\r\n";
		msg += "후원 포인트 : " + cm.getPlayer().getDonationPoint() + "      |      홍보 포인트 : " + cm.getPlayer().getHPoint() + "\r\n";
		msg += "#L1# #b후원 시스템을 이용하고 싶어요.#n #k\r\n";
		msg += "#L2# #b홍보 시스템을 이용하고 싶어요.#n #k\r\n";

		cm.sendSimpleS(msg, 4);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001604, "3003168");
				break;
			case 2:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001604, "3003167");
				break;
		}
	}
}