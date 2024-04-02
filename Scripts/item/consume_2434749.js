var status;
itemlist = [[2432970,1,5,10000], // 스페셜 명예의 훈장
[1182196,1,1,1500]] // 뱃지 오브 도나르
prob = 0;
plus = 0;
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
		for (i=0; i<itemlist.length; i++) {
			prob += itemlist[i][3];
		}
		rd = Math.floor(Math.random() * prob);
		for (i=0; i<itemlist.length; i++) {
			plus += itemlist[i][3]
			if (plus >= rd) {
				cm.gainItem(2434749, -1);
				cm.gainItem(itemlist[i][0], Packages.server.Randomizer.rand(itemlist[i][1], itemlist[i][2]));
				cm.gainItem(4310020, Packages.server.Randomizer.rand(5,20));
				cm.dispose();
				return;
			}
		}
		
	}
}
