var status;
var select;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode < 0) {
        cm.dispose();
    return;
    } else {
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            var text = "#b어메이징 미라클 큐브 조각 #r#c2430759#개#k를 가지고 있습니다. \r\n하인즈코어는 #r50개에 100개#k,\r\n악마를 쫒는 부적은 #r50개에 1개#k,\r\n호신 부적은 #r50개에 1개#k를 사용하여 교환할 수 있습니다.\r\n\r\n";
                text += "#b#L0##i4319997# #z4319997##l\r\n";
		text += "#b#L1##i4319995# #z4319995##l\r\n";
		text += "#b#L2##i4319996# #z4319996##l";
            cm.sendSimple(text);
        } else if(status == 1) {
            if (selection == 0) {
                if (cm.haveItem(2430759, 50)) {
		    cm.sendYesNo("정말 #b#i4319997# #z4319997##k로 바꾸시겠습니까?");
		    select = 0;
                } else {
                    cm.sendNext("죄송하지만 #b#z2430759##k이 충분하지 않으신것 같네요.");
		    cm.dispose();
                }
            } else if (selection == 1) {
                if (cm.haveItem(2430759, 50)) {
		    cm.sendYesNo("정말 #b#i4319995# #z4319995##k로 바꾸시겠습니까?");
		    select = 1;
		} else {
                    cm.sendNext("죄송하지만 #b#z2430759##k이 충분하지 않으신것 같네요.");
		    cm.dispose();
		}
	    } else if (selection == 2) {
                if (cm.haveItem(2430759, 50)) {
		    cm.sendYesNo("정말 #b#i4319996# #z4319996##k로 바꾸시겠습니까?");
		    select = 2;
		} else {
                    cm.sendNext("죄송하지만 #b#z2430759##k이 충분하지 않으신것 같네요.");
		    cm.dispose();
		}        
            } else {
                cm.dispose();
            }
	} else if (status == 2) {
            if (select == 0) {
                if (cm.canHold(2049501)) {
                    cm.gainItem(2430759, -50);
                    cm.gainItem(4319997, 100);
		    cm.sendNext("교환이 완료되었습니다.");
                } else {
                    cm.sendNext("죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b소비#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();
            } else if (select == 1) {
		if (cm.canHold(2049300)) {
                    cm.gainItem(2430759, -50);
                    cm.gainItem(4319995, 1);
                } else {
                    cm.sendNext("죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b소비#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();
	    } else if (select == 2) {
		if (cm.canHold(2049701)) {
                    cm.gainItem(2430759, -50);
                    cm.gainItem(4319996, 1);
                } else {
                    cm.sendNext("죄송하지만 인벤토리 공간이 충분하지 않으신 것 같네요. #b캐시#k탭의 인벤토리 공간을 비워주세요.");
                }
                cm.dispose();        
            } else {
                cm.dispose();
            }
        } else { 
            cm.dispose();
        }
    }
}