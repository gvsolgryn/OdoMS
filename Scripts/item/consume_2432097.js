var enter = "\r\n";
var seld = -1;

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
		cm.dispose();
		cm.gainItem(2432097,-1);
		cm.getPlayer().addKV("HAS_PREMIUM_PASS", "0");
		cm.sendOk("프리미엄 패스가 추가되었습니다.");
	}
}