var status;
importPackage(Packages.server);
importPackage(Packages.client.inventory);
importPackage(Packages.server);
importPackage(Packages.server.items);
one = Math.floor(Math.random() * 5) + 1 // 최소 10 최대 35 , 혼테일
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
				cm.forceCompleteQuest(12394);
				cm.forceCompleteQuest(12395);
				cm.forceCompleteQuest(12396);
				cm.setInnerStats(1);
				cm.setInnerStats(2);
				cm.setInnerStats(3);
		cm.gainItem(2439538, -1);
		cm.dispose();
	}
}
