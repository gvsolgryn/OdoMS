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
		cm.gainItem(2432096,-1);
		cm.getPlayer().removeV("Serenity_Premium_Pass_Complete");
		cm.getPlayer().removeV("Serenity_Normal_Pass_Complete");
		cm.getPlayer().removeV("Clear_Pass_Premium_Kill_Monster_Amount");
		cm.getPlayer().removeV("Clear_Pass_Kill_Monster_Amount");
		cm.getPlayer().removeV("Pass_kill_Monster_amount");
		cm.sendOk("끼룩 패스를 초기화했습니다..");
	}
}