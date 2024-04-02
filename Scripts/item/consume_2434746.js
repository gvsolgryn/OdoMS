var status;
itemlist = [[4023019,1,5,10000], //쥬니퍼베리 씨앗 오일
[4023020,1,5,10000], //쥬니퍼베리 꽃 오일
[4022021,1,5,10000], //히솝 꽃
[4011004,1,10,10000], //빛바랜 은
[4021004,1,10,10000], //오팔
[4021004,1,10,10000], //오팔
[4021022,1,1,1000], // 태초의 정수
[1182193,1,1,1500]] // 뱃지 오브 마노
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
				cm.gainItem(2434746, -1);
				cm.gainItem(itemlist[i][0], Packages.server.Randomizer.rand(itemlist[i][1], itemlist[i][2]));
				cm.gainItem(4310020, Packages.server.Randomizer.rand(5,20));
				cm.dispose();
				return;
			}
		}
		
	}
}
