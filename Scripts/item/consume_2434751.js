var status;
itemlist = [[2450064,1,1,10000], //경험치 2배 쿠폰
[1182199,1,1,1500], // 뱃지 오브 준나
[4023019,1,5,10000], //쥬니퍼베리 씨앗 오일
[4023020,1,5,10000], //쥬니퍼베리 꽃 오일
[4022021,1,5,10000], //히솝 꽃
[4011009,1,10,10000], //빛바랜 은
[4021004,1,10,10000], //오팔
[4021004,1,10,10000], //오팔
[4021022,1,1,1000], // 태초의 정수
[1182193,1,1,1500], // 뱃지 오브 마노
[4001832,100,300,10000], // 주문의 흔적
[2049700,1,1,10000], // 에픽 잠재능력 부여 주문서
[5062009,10,100,10000], // 레드 큐브
[5062010,5,50,10000], // 블랙 큐브
[5062500,5,35,10000], // 에디셔널 큐브
[5062503,5,15,10000], // 화이트 에디셔널 큐브
[1182194,1,1,1500], // 뱃지 오브 치우
[2022741,1,10,10000], //당근주스
[2022740,1,10,10000], //왁스
[2022742,1,10,10000], //뱀탕
[2022743,1,10,10000], //손 세정제
[2022744,1,10,10000], //커피 한 잔
[2022745,1,10,10000], //향수
[1182194,1,1,1500], // 뱃지 오브 보탄
[2432970,1,5,10000], // 스페셜 명예의 훈장
[1182196,1,1,1500], // 뱃지 오브 도나르
[1182197,1,1,1500]] // 뱃지 오브 프루바
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
				cm.gainItem(2434751, -1);
				cm.gainItem(itemlist[i][0], Packages.server.Randomizer.rand(itemlist[i][1], itemlist[i][2]));
				cm.gainItem(4310020, Packages.server.Randomizer.rand(5,20));
				cm.dispose();
				return;
			}
		}
		
	}
}
