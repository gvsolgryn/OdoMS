var status;
itemlist = [[4001832,100,300,10000], // 주문의 흔적
[2049700,1,1,10000], // 에픽 잠재능력 부여 주문서
[5062009,10,100,10000], // 레드 큐브
[5062010,5,50,10000], // 블랙 큐브
[5062500,5,35,10000], // 에디셔널 큐브
[5062503,5,15,10000], // 화이트 에디셔널 큐브
[1182194,1,1,1500]] // 뱃지 오브 치우
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
				cm.gainItem(2434747, -1);
				cm.gainItem(itemlist[i][0], Packages.server.Randomizer.rand(itemlist[i][1], itemlist[i][2]));
				cm.gainItem(4310020, Packages.server.Randomizer.rand(5,20));
				cm.dispose();
				return;
			}
		}
		
	}
}
