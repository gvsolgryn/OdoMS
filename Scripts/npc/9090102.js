importPackage(java.sql);
importPackage(java.lang);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);

전사 = [1302355, 1312213, 1322264, 1402268, 1412189, 1422197, 1432227, 1442285];
궁수 = [1452266, 1462252];
마법사 = [1382274, 1372237];
도적 = [1472275, 1332289];
해적 = [1482232, 1482245];

아델 = [1213022];
루미 = [1212129];
카인 = [1214022];
엔젤릭버스터 = [1222122];
데벤 = [1232122];
제논 = [1242141];
키네시스 = [1262051];
카데나 = [127040];
호영 = [1292022];
팬텀 = [1362149];
메르세데스 = [1522152];
캐슈 = [1532157];
블래 = [1582044];
패스파인더 = [1592022];

var purple = "#fc0xFF7401DF#";

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
	var text = "#fn나눔고딕#" + purple + "<화이트 제네시스 무기 선택>#k#n\r\n\r\n";
	text += "#fUI/UIWindow2.img/QuestIcon/3/0#\r\n";
	switch (cm.getPlayer().getJob()) {
	    case 100:
	    case 110:
	    case 111:
	    case 112:
	    case 120:
	    case 121:
	    case 130:
	    case 131:
	    case 132:
	    case 1100:
	    case 1110:
	    case 1111:
	    case 1112:
	    case 2100:
	    case 2110:
	    case 2111:
	    case 2112:
	    case 3700:
	    case 3710:
	    case 3711:
	    case 3712:
	    case 3100:
	    case 3110:
	    case 3111:
	    case 3112:
	    case 3101:
	    case 3120:
	    case 3121:
	    case 3122:
	    case 5100:
	    case 5110:
	    case 5111:
	    case 5112:
	    case 6100:
	    case 6110:
	    case 6111:
	    case 6112:
        	for (i = 0; i < 전사.length; i++) {
		text += "#b#L"+ 전사[i] +"# #i"+ 전사[i] +"# #z"+ 전사[i] +"# #k#l\r\n";
		}
		break;
	     case 15100:
	     case 15110:
	     case 15111:
	     case 15112:
        	for (i = 0; i < 아델.length; i++) {
		text += "#b#L"+ 아델[i] +"# #i"+ 아델[i] +"# #z"+ 아델[i] +"# #k#l\r\n";
		}
		break;
	    case 200:
	    case 210:
	    case 211:
	    case 212:
	    case 220:
	    case 221:
	    case 222:
	    case 230:
	    case 232:
	    case 1200:
	    case 1210:
	    case 1211:
	    case 1212:
	    case 2200:
	    case 2210:
	    case 2211:
	    case 2212:
	    case 2213:
	    case 2214:
	    case 2215:
	    case 2216:
	    case 2217:
	    case 2218:
	    case 3200:
	    case 3210:
	    case 3211:
	    case 3212:
        	for (i = 0; i < 마법사.length; i++) {
		text += "#b#L"+ 마법사[i] +"# #i"+ 마법사[i] +"# #z"+ 마법사[i] +"# #k#l\r\n";
		}
		break;
	    case 2700:
	    case 2710:
	    case 2711:
	    case 2712:
        	for (i = 0; i < 루미.length; i++) {
		text += "#b#L"+ 루미[i] +"# #i"+ 루미[i] +"# #z"+ 루미[i] +"# #k#l\r\n";
		}
		break;
	    case 14200:
	    case 14210:
	    case 14211:
	    case 14212:
        	for (i = 0; i < 키네시스.length; i++) {
		text += "#b#L"+ 키네시스[i] +"# #i"+ 키네시스[i] +"# #z"+ 키네시스[i] +"# #k#l\r\n";
		}
		break;

	    case 300:
	    case 301:
	    case 310:
	    case 311:
	    case 312:
	    case 320:
	    case 321:
	    case 322:
	    case 1300:
	    case 1310:
	    case 1311:
	    case 1312:
	    case 3300:
	    case 3310:
	    case 3311:
	    case 3312:
        	for (i = 0; i < 궁수.length; i++) {
		text += "#b#L"+ 궁수[i] +"# #i"+ 궁수[i] +"# #z"+ 궁수[i] +"# #k#l\r\n";
		}
		break;
	    case 2300:
	    case 2310:
	    case 2311:
	    case 2312:
        	for (i = 0; i < 메르세데스.length; i++) {
		text += "#b#L"+ 메르세데스[i] +"# #i"+ 메르세데스[i] +"# #z"+ 메르세데스[i] +"# #k#l\r\n";
		}
		break;
	    case 330:
	    case 331:
	    case 332:
        	for (i = 0; i < 패스파인더.length; i++) {
		text += "#b#L"+ 패스파인더[i] +"# #i"+ 패스파인더[i] +"# #z"+ 패스파인더[i] +"# #k#l\r\n";
		}
		break;
                case 6003:
                case 6300:
                case 6310:
                case 6311:
                case 6312:
        	for (i = 0; i < 카인.length; i++) {
		text += "#b#L"+ 카인[i] +"# #i"+ 카인[i] +"# #z"+ 카인[i] +"# #k#l\r\n";
		}
		break;
	    case 400:
	    case 410:
	    case 411:
	    case 412:
	    case 420:
	    case 421:
	    case 422:
	    case 430:
	    case 431:
	    case 432:
	    case 433:
	    case 434:
	    case 1400:
	    case 1410:
	    case 1411:
	    case 1412:
        	for (i = 0; i < 도적.length; i++) {
		text += "#b#L"+ 도적[i] +"# #i"+ 도적[i] +"# #z"+ 도적[i] +"# #k#l\r\n";
		}
		break;
	    case 2400:
	    case 2410:
	    case 2411:
	    case 2412:
        	for (i = 0; i < 팬텀.length; i++) {
		text += "#b#L"+ 팬텀[i] +"# #i"+ 팬텀[i] +"# #z"+ 팬텀[i] +"# #k#l\r\n";
		}
		break;
	    case 6400:
	    case 6410:
	    case 6411:
	    case 6412:
        	for (i = 0; i < 카데나.length; i++) {
		text += "#b#L"+ 카데나[i] +"# #i"+ 카데나[i] +"# #z"+ 카데나[i] +"# #k#l\r\n";
		}
		break;
	    case 16400:
	    case 16410:
	    case 16411:
	    case 16412:
        	for (i = 0; i < 호영.length; i++) {
		text += "#b#L"+ 호영[i] +"# #i"+ 호영[i] +"# #z"+ 호영[i] +"# #k#l\r\n";
		}
		break;
	    case 500:
	    case 510:
	    case 511:
	    case 512:
	    case 520:
	    case 521:
	    case 522:
	    case 1500:
	    case 1510:
	    case 1511:
	    case 1512:
	    case 3500:
	    case 3510:
	    case 3511:
	    case 3512:
        	for (i = 0; i < 해적.length; i++) {
		text += "#b#L"+ 해적[i] +"# #i"+ 해적[i] +"# #z"+ 해적[i] +"# #k#l\r\n";
		}
		break;
	    case 501:
	    case 530:
	    case 531:
	    case 532:
        	for (i = 0; i < 캐슈.length; i++) {
		text += "#b#L"+ 캐슈[i] +"# #i"+ 캐슈[i] +"# #z"+ 캐슈[i] +"# #k#l\r\n";
		}
		break;
	    case 6500:
	    case 6510:
	    case 6511:
	    case 6512:
        	for (i = 0; i < 엔젤릭버스터.length; i++) {
		text += "#b#L"+ 엔젤릭버스터[i] +"# #i"+ 엔젤릭버스터[i] +"# #z"+ 엔젤릭버스터[i] +"# #k#l\r\n";
		}
		break;
	    case 2500:
	    case 2510:
	    case 2511:
	    case 2512:
	    case 15500:
	    case 15510:
	    case 15511:
	    case 15512:
        	for (i = 0; i < 해적.length; i++) {
		text += "#b#L"+ 해적[i] +"# #i"+ 해적[i] +"# #z"+ 해적[i] +"# #k#l\r\n";
		}
		break;
	    case 3600:
	    case 3610:
	    case 3611:
	    case 3612:
        	for (i = 0; i < 제논.length; i++) {
		text += "#b#L"+ 제논[i] +"# #i"+ 제논[i] +"# #z"+ 제논[i] +"# #k#l\r\n";
		}
		break;
	}
	cm.sendSimple(text);
	} else if (status == 1) {
	    var a = "#fn나눔고딕#" + purple + "<화이트 제네시스 무기선택>#k#n\r\n\r\n";
	    a += "#e#b#h0##k#n 님에게 딱 맞는 장비를 지급해드렸어요!\r\n그리고 장비의 능력치를 살짝 강화시켜드렸어요.\r\n#e";
	    a += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
	    a += "#b#i"+ selection +"# #z"+ selection +"##k #e#r(선택한 아이템)#k#n\r\n";
	    var inz = MapleItemInformationProvider.getInstance().getEquipById(selection);
	    inz.setReqLevel(-90);
	inz.setStr(777);
	inz.setDex(777);
	inz.setInt(777);
	inz.setLuk(777);
	inz.setWatk(777);
	inz.setMatk(777);
            inz.setReqLevel(-100);
	inz.setBossDamage(100);
	inz.setTotalDamage(100);
	inz.setUpgradeSlots(0);
	inz.setEnhance(25);
	inz.setUpgradeSlots(0);
	    MapleInventoryManipulator.addbyItem(cm.getClient(), inz);
	    cm.sendSimple(a);
	    cm.dispose();
	}
}