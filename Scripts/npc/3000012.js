var status = -1;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    }
    if (selection == -1) {
        cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendDimensionGate("#0# 여섯갈래 길#1# 헤네시스#2# 엘리니아#3# 페리온#4# 커닝시티#5# 리스항구#6# 슬리피우드#7# 노틸러스#8# 에레브#9# 리엔#10# 오르비스#11# 엘나스#12# 루디브리엄#13# 지구방위본부#14# 아랫마을#15# 아쿠아리움#16# 리프레#17# 무릉#18# 백초마을#19# 아리안트#20# 마가티아#21# 에델슈타인#22# 에우렐#23# 크리티아스#24# 헤이븐#25# 판테온 대신전#26# 버려진 야영지 #27#무법자들의 마을");
    } else if (status == 1) {
	switch (selection) {
       case 0:
      cm.warp(104020000);
           break;
       case 1:
      cm.warp(100000000);
           break;
       case 2:
      cm.warp(101000000);
           break;
       case 3:
      cm.warp(102000000);
           break;
       case 4:
      cm.warp(103000000);
           break;
       case 5:
      cm.warp(104000000);
           break;
       case 6:
      cm.warp(105000000);
           break;
       case 7:
      cm.warp(120000000);
           break;
       case 8:
      cm.warp(130000000);
           break;
       case 9:
      cm.warp(140000000);
           break;
       case 10:
      cm.warp(200000000);
           break;
       case 11:
      cm.warp(211000000);
           break;
       case 12:
      cm.warp(220000000);
           break;
       case 13:
      cm.warp(221000000);
           break;
       case 14:
      cm.warp(224000000);
           break;
       case 15:
      cm.warp(230000000);
           break;
       case 16:
      cm.warp(240000000);
           break;
       case 17:
      cm.warp(250000000);
           break;
       case 18:
      cm.warp(251000000);
           break;
       case 19:
      cm.warp(260000000);
           break;
       case 20:
      cm.warp(261000000);
           break;
       case 21:
      cm.warp(310000000);
           break;
       case 22:
      cm.warp(101050000);
           break;
       case 23:
      cm.warp(241000100);
           break;
       case 24:
      cm.warp(310070000);
           break;
       case 25:
      cm.warp(400000001);
           break;
       case 26:
      cm.warp(105300000);
           break;   
       case 27:
      cm.warp(402000000);
           break;   
    case 28:
        cm.warp(410000500);
        break;
    case 29:
        cm.warp(410003000);
        break;   
	}
	cm.dispose();
    }
}