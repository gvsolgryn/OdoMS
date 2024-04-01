var status;
itemlist = [[2022741,1,10,10000], //당근주스
[2022740,1,10,10000], //왁스
[2022742,1,10,10000], //뱀탕
[2022743,1,10,10000], //손 세정제
[2022744,1,10,10000], //커피 한 잔
[2022745,1,10,10000], //향수
[1182194,1,1,1500]] // 뱃지 오브 보탄
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
				cm.gainItem(2434748, -1);
				cm.gainItem(itemlist[i][0], Packages.server.Randomizer.rand(itemlist[i][1], itemlist[i][2]));
				cm.gainItem(4310020, Packages.server.Randomizer.rand(5,20));
				cm.dispose();
				return;
			}
		}
		
	}
}
