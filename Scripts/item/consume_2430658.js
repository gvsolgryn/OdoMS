var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);

아이템 = 2430658;

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
	var msg = "오토루팅 스킬을 획득했습니다."
	cm.sendSimple(msg);
	cm.getPlayer().setKeyValue(12345, "AutoRoot", "1");
	cm.gainItem(아이템,-1);
	cm.dispose();
	}
}
