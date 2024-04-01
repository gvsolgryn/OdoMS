var itmelist= [
    [5068300, 1000, 1], //원더베리
    [5069100, 2000, 1], //루나 크리스탈
    [5062006, 1000, 1], //플래티넘 언리미티드 큐브
    [5062002, 2000, 1], //에디셔널 언리미티드 큐브
    [5060048, 1500, 1], //애플
    [2049360, 3000, 1], //놀장강
    [2047950, 3000, 1], // [닉스]방어구 주문서
    [2046076, 3000, 1], // [후원] 한손무기 공격력 주문서
    [2046077, 3000, 1], // [후원] 한손무기 마   력 주문서
    [2046150, 3000, 1], // [후원] 두손무기 공격력 주문서
    [2048047, 4000, 1], // 후원] 펫장비 공격력 주문서
    [2048048, 4000, 1], // [후원] 펫장비 마   력 주문서
    [2046340, 5000, 1], // [후원] 악세서리 공격력 주문서
    [2046341, 5000, 1], // [후원] 악세서리 마   력 주문서
    [2435899, 5000, 1], //위대한 소울 선택상자
    [2430030, 2000, 1], //보스 입장 초기화+
    [2049704, 500, 1], //레전드리 잠재 부여 주문서
    [2430034, 3000, 1], // 안드로이드 각인서
    [4034803, 15000, 1], // 닉네임 변경권
    [2430068, 30000, 1], //루나쁘띠 1~4기 펫
    [2430028, 10000, 1], //스네이크 헌터 악세서리
    [2633621, 20000, 1], // 극한성장의 비약
    [2430031, 20000, 1], //아케인심볼 : 여로 20렙
    [2430032, 20000, 1], // 아케인심볼 : 츄츄 20렙
    [2430033, 20000, 1], // 아케인심볼 : 레헬른 20렙
    [2430049, 20000, 1], // 아케인심볼 : 아르카나 20렙
    [2430051, 20000, 1], // 아케인심볼 : 모라스 20렙
    [2430052, 20000, 1], // 아케인심볼 : 에스페라 20렙
    [2633616, 50000, 1], // 어센틱심볼 : 세르니움 11렙
    [2633336, 50000, 1], // 어센틱심볼 : 아르크스 11렙
    [2049376, 26900, 1], // 스타포스 20성 강화권
    [4310021, 3000, 1], // 스타포스 10% 확률 업 티켓
	];
	
	function start() {
		status = -1;
		action(1, 0, 0);
	}
	
	function action(mode, type, selection) {
		if (mode == -1) {
			cm.dispose();
		} else {
			if (mode == 0) {
				cm.dispose();
				return;
			}
			if (mode == 1)
				status++;
			else
				status--;
		if (status == 0) {
	
					  
			 var a = "#fs11##fc0xFFFF3366##h0# #fc0xFF000000#님의 도네이션 포인트 : #fc0xFFFF3366#"+cm.getPlayer().getDonationPoint()+" P#k#n\r\n"; 
			for(var i = 0; i<18; i++){
				a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 개\r\n               #fc0xFF000000#도네이션 포인트#k #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n";
			}
			for(var i = 18; i<24; i++){
				a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 개\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n               #r강화비용 별도\r\n";
			}
			for(var i = 24; i<itmelist.length; i++){
				a += "#L"+i+"##i"+itmelist[i][0]+"# #d#z"+itmelist[i][0]+"##l#k#r "+itmelist[i][2]+" 개\r\n               #fc0xFF000000#도네이션 포인트#fc0xFF000000# #e#fc0xFFFF3366#"+itmelist[i][1]+" P#k#n\r\n               #r강화1비용 별도\r\n";
			}
			cm.sendSimple(a);
	
			} else if (selection >= 0 && selection <= itmelist.length) {
			if (cm.getPlayer().getDonationPoint() >= itmelist[selection][1]) {
				if (cm.canHold(itmelist[7][0]) || cm.canHold(itmelist[8][0])) {
					cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 개#k 를 구입 하셨습니다.");
					cm.dispose();
				}
				if (cm.canHold(itmelist[selection][0])) {
					cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 개#k 를 구입 하셨습니다.");
					cm.dispose();
				}
					cm.getPlayer().gainDonationPoint(-itmelist[selection][1]);
					cm.gainItem(itmelist[selection][0], itmelist[selection][2]);
					cm.sendOk("#b후원 포인트#k 로 #i"+itmelist[selection][0]+"# #r "+itmelist[selection][2]+" 개#k 를 구입 하셨습니다.");
					cm.dispose();
			} else {
				cm.sendOk("#fs11##b후원 포인트#k 가 부족합니다.");
				cm.dispose();
			}
	
			}
		}
	}