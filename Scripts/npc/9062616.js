﻿importPackage(Packages.server);
importPackage(Packages.database);
importPackage(Packages.client);
importPackage(java.lang);

var enter = "\r\n";
var seld = -1;
var seldreward = -1;
var seld2 = -1;
var seld3 = -1;
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
var hasSelect = false;

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
        var msg = "#fs11#안녕하세요. #e갈매기#n 일반 마일리지 도우미 입니다.\r\n " + enter;
        msg += "현재 #b#h ##k님의 일반 마일리지 : #b" + cm.getPlayer().getHPoint() + "P#k" + enter;
        msg += "#L1#마일리지 교환하기#l" + enter+enter+enter;
	msg += "" + "#d───────── 마일리지 상점 ─────────#l\r\n";
	msg += "#L2#"+검정+프리미엄+" 장비상점#l";
        msg += "#L3#"+연보+프리미엄+" 큐브&강화#l";
	msg += "#L4#"+연파+프리미엄+" 코인&컨텐츠#l"+enter;
	msg += " ";
        cm.sendSimple(msg);
    } else if (status == 1) {
        seld = sel;
        switch (sel) {
            case 1:
		cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9062616, "마일");
		break;
            case 2:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9062616, "마일장비");
                break;  
            case 3:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9062616, "마일강화");
                break;
            case 4:
                cm.dispose();
                cm.openNpcCustom(cm.getClient(), 9062616, "마일기타");
                break;
		}
	}
}


