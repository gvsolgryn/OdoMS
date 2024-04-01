importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var seldreward = -1;
var seld2 = -1;
var seld3 = -1;

var name, comment, etc;
var donation;
var firstdon = false;

var year, month, date2, date, day

var reward = 0;
var modify = "";
var modifychr;
var seldgrade = 0;

네오젬 = "#fUI/UIWindow4.img/pointShop/100712/iconShop#";
프리미엄 = "#fUI/UIWindow4.img/pointShop/501053/iconShop#"
파랑 = "#fc0xFF0054FF#";
연파 = "#fc0xFF6B66FF#";
연보 = "#fc0xFF8041D9#";
보라 = "#fc0xFF5F00FF#";
노랑 = "#fc0xFFEDD200#";
검정 = "#fc0xFF191919#";
분홍 = "#fc0xFFFF5AD9#";
빨강 = "#fc0xFFF15F5F#";

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
		var msg = "#fs11#현재 #b#h ##k님의 프리미엄 마일리지 : #b"+cm.getPlayer().getDonationPoint()+"P#k"+enter;;
		msg += "#L1#프리미엄 마일리지 교환하기#l"+enter+enter+enter;
		msg += "" + "#d─────── 프리미엄 상점 ───────#l\r\n";
		msg += "#L2#"+검정+프리미엄+" 장비상점#l　  　";
                msg += "#L3#"+연보+프리미엄+" 강화권상점#l"+enter;
		msg += "#L4#"+연파+프리미엄+" 큐브&주문서#l　";
                msg += "#L5#"+분홍+프리미엄+" 코인&컨텐츠#l"+enter+enter;
		msg += "" + "#d─────── 프리미엄 기능 ───────#l\r\n";
		msg += "#L100#"+빨강+네오젬+" 검색 캐시#l　　　";
		msg += "#L20#"+노랑+네오젬+" 스킬 상점#l"+enter;
		msg += "#L12#"+분홍+네오젬+" 펫장비 옵션부여#l";
		msg += "#L11#"+연파+네오젬+" 장비 옵션부여#l"+enter;
		msg += "#L10#"+파랑+네오젬+" 잠재능력 추가#l　";
		msg += "#L13#"+빨강+네오젬+" 커플/우정링#l"+enter;
		cm.sendSimple(msg);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 9062619, "프마");
			break;
			case 2:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 9062619, "프마장비상점");
			break;
			case 3:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 9062619, "프마강화권상점");
			break;
			case 4:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 9062619, "프마소비상점");
			break;
			case 5:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 9062619, "프마기타상점");
			break;
			case 10:
				cm.dispose();
                                cm.openNpc(9062629);
			break;
			case 11:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 3004434, "BallEn");
			break;
			case 20:
				cm.dispose();
				cm.openNpc(9062609);
                        break;
			case 13:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 9062619, "1540859");
			break;
			case 12:
				cm.dispose();
                                cm.openNpcCustom(cm.getClient(), 9062619, "9050001");
			break;
			case 100:
				cm.dispose();
				cm.openNpc(3005560, "dSearch");
			break;
		}
	}
}


