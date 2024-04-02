var status = -1;
var sel = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	if (cm.getPlayer().getProfessionLevel(92010000) <= 0) { // 스킬을 안배웠을때
	    cm.sendSimple("그래, 채광의 달인인 이 #b노붐#k님에게 원하는것이 무엇인가?\r\n\r\n#b#L0##e채광#n에 대한 설명을 듣는다.\r\n#L1##e채광#n을 배운다.");
	} else { // 스킬을 배웠을때
	    cm.sendSimple("그래, 채광의 달인인 이 #b노붐#k님에게 원하는것이 무엇인가?\r\n\r\n#b#L1##e채광#n의 레벨을 초기화한다.\r\n#L3##e원석의 파편#n을 교환한다.");
	}
    } else if (status == 1) {
	sel = selection;
	if (sel == 0 ) {
		cm.sendNext("채광은 필드 곳곳에 있는 광석을 채집 하는 스킬이야. 이렇게 채집한 원석을 라피니르트가 판매하는 거푸집에 담아 제련하게 되면 장비, 장신구, 연금술 등에 필요한 재료가 되지.");
		cm.dispose();
	} else if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92010000) > 0) {
		cm.sendYesNo("채광을 배우지 않는 상태로 초기화를 할거야? 지금까지 쌓아온 숙련도와 레벨이 모두 초기화 된다구.");
	    } else {
		cm.sendYesNo("#b채광#k을 배우게 된다. 정말 이대로 배울거지? 약간의 비용이 드는데 #b5000메소#k야. 그 정도 돈은 있는거지?");
	    }
	} else if (sel == 3) {
	    if (!cm.haveItem(4011010, 100)) {
		cm.sendOk("원석의 파편을 100개이상 갖고 있지 않은것 같습니다. 다시 확인후 이용해주세요.");
 	    } else if (!cm.canHold(2028067, 1)) {
		cm.sendOk("Please make some USE space.");
	    } else {
		cm.sendOk("원석의 파편 교환이 완료되었습니다.");
		cm.gainItem(2028067, 1);
		cm.gainItem(4011010, -100);
	    } 
         }
    } else if (status == 2) {
	if (sel == 1) {
	    if (cm.getPlayer().getProfessionLevel(92010000) > 0) {
		cm.sendOk("채광기술이 초기화 되었습니다.");
		cm.teachSkill(92010000, 0, 0);
	    	cm.dispose();
	    } else {
		cm.sendOk("채광기술을 배웠습니다.");
		cm.teachSkill(92010000, 0x1000000, 0); //00 00 00 01
		if (cm.canHold(1512000,1)) {
			cm.gainItem(1512000,1);
		}
	    	cm.dispose();
                }
	}
    }
}