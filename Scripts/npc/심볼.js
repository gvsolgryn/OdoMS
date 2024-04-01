/*
 만렙 아케인 심볼 / 어센틱 심볼 구매 스크립트 -made by Voltex#3239
*/
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools);

var status = 0;
var status = -1;
var enter = "\r\n";

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
		if (cm.getPlayer().getLevel() < 260) {
			choose = "#fs11#안녕하세요 #b#h0##k님, 원하시는 심볼을 선택해주세요.\r\n#Cgray#260 레벨 미만 캐릭터는 어센틱심볼이 보이지 않습니다.\r\n";
			choose += "#L0##i1712001##b #z1712001# #r(Lv.Max)#k#l\r\n";
			choose += "#L1##i1712002##b #z1712002# #r(Lv.Max)#k#l\r\n";
			choose += "#L2##i1712003##b #z1712003# #r(Lv.Max)#k#l\r\n";
			choose += "#L3##i1712004##b #z1712004# #r(Lv.Max)#k#l\r\n";
			choose += "#L4##i1712005##b #z1712005# #r(Lv.Max)#k#l\r\n";
			choose += "#L5##i1712006##b #z1712006# #r(Lv.Max)#k#l\r\n";
			cm.sendSimple(choose);
		} else {
			choose = "#fs11#안녕하세요 #b#h0##k님, 원하시는 심볼을 선택해주세요.\r\n#Cgray#260 레벨 이상 캐릭터는 어센틱심볼이 목록에서 추가됩니다.\r\n";
			choose += "#L0##i1712001##b #z1712001# #r(Lv.Max)#k#l\r\n";
			choose += "#L1##i1712002##b #z1712002# #r(Lv.Max)#k#l\r\n";
			choose += "#L2##i1712003##b #z1712003# #r(Lv.Max)#k#l\r\n";
			choose += "#L3##i1712004##b #z1712004# #r(Lv.Max)#k#l\r\n";
			choose += "#L4##i1712005##b #z1712005# #r(Lv.Max)#k#l\r\n";
			choose += "#L5##i1712006##b #z1712006# #r(Lv.Max)#k#l\r\n";
			choose += "#L100##i1713000##b #z1713000# #r(Lv.Max)#k#l\r\n";
			choose += "#L101##i1713001##b #z1713001# #r(Lv.Max)#k#l\r\n";
			choose += "#L102##i1713002##b #z1713002# #r(Lv.Max)#k#l\r\n";
			cm.sendSimple(choose);
		}
	} else if (status == 1) {
		if (selection == 0) {
			choose = "#fs11##i1712001##k (#r#z1712001##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L0##bSTR#k#l\r\n";
			choose += "#L1##bDEX#k#l\r\n";
			choose += "#L2##bINT#k#l\r\n";
			choose += "#L3##bLUK#k#l\r\n";
			choose += "#L50##bMaxHP#k#l\r\n";
			choose += "#L51##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 1) {
			choose = "#fs11##i1712002##k (#r#z1712002##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L4##bSTR#k#l\r\n";
			choose += "#L5##bDEX#k#l\r\n";
			choose += "#L6##bINT#k#l\r\n";
			choose += "#L7##bLUK#k#l\r\n";
			choose += "#L52##bMaxHP#k#l\r\n";
			choose += "#L53##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 2) {
			choose = "#fs11##i1712003##k (#r#z1712003##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L8##bSTR#k#l\r\n";
			choose += "#L9##bDEX#k#l\r\n";
			choose += "#L10##bINT#k#l\r\n";
			choose += "#L11##bLUK#k#l\r\n";
			choose += "#L54##bMaxHP#k#l\r\n";
			choose += "#L55##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 3) {
			choose = "#fs11##i1712004##k (#r#z1712004##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L12##bSTR#k#l\r\n";
			choose += "#L13##bDEX#k#l\r\n";
			choose += "#L14##bINT#k#l\r\n";
			choose += "#L15##bLUK#k#l\r\n";
			choose += "#L56##bMaxHP#k#l\r\n";
			choose += "#L57##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 4) {
			choose = "#fs11##i1712005##k (#r#z1712005##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L16##bSTR#k#l\r\n";
			choose += "#L17##bDEX#k#l\r\n";
			choose += "#L18##bINT#k#l\r\n";
			choose += "#L19##bLUK#k#l\r\n";
			choose += "#L58##bMaxHP#k#l\r\n";
			choose += "#L59##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 5) {
			choose = "#fs11##i1712006##k (#r#z1712006##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L20##bSTR#k#l\r\n";
			choose += "#L21##bDEX#k#l\r\n";
			choose += "#L22##bINT#k#l\r\n";
			choose += "#L23##bLUK#k#l\r\n";
			choose += "#L60##bMaxHP#k#l\r\n";
			choose += "#L61##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 100) {
			choose = "#fs11##i1713000##k (#r#z1713000##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L80##bSTR#k#l\r\n";
			choose += "#L81##bDEX#k#l\r\n";
			choose += "#L82##bINT#k#l\r\n";
			choose += "#L83##bLUK#k#l\r\n";
			choose += "#L84##bMaxHP#k#l\r\n";
			choose += "#L85##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 101) {
			choose = "#fs11##i1713001##k (#r#z1713001##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L86##bSTR#k#l\r\n";
			choose += "#L87##bDEX#k#l\r\n";
			choose += "#L88##bINT#k#l\r\n";
			choose += "#L89##bLUK#k#l\r\n";
			choose += "#L90##bMaxHP#k#l\r\n";
			choose += "#L91##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} else if (selection == 102) {
			choose = "#fs11##i1713002##k (#r#z1713002##k)의 주스텟을 설정해주세요.\r\n";
			choose += "#L92##bSTR#k#l\r\n";
			choose += "#L93##bDEX#k#l\r\n";
			choose += "#L94##bINT#k#l\r\n";
			choose += "#L95##bLUK#k#l\r\n";
			choose += "#L96##bMaxHP#k#l\r\n";
			choose += "#L97##b올스텟#k#l\r\n";
			cm.sendSimple(choose);
		} 
	} else if (status == 2) {
		select = selection;
		if (selection == 0) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1712001, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 1) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1712001, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 2) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1712001, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 3) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1712001, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 4) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1712002, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 5) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1712002, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 6) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1712002, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 7) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1712002, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 8) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1712003, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 9) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1712003, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 10) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1712003, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 11) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1712003, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 12) {
				if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1712004, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 13) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1712004, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 14) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1712004, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 15) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1712004, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 16) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1712005, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 17) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1712005, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 18) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1712005, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 19) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1712005, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 20) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1712006, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 21) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1712006, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 22) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1712006, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 23) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1712006, 2200, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 50) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1712001, 3850, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 51) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1712001, 858, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 52) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1712002, 3500, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 53) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1712002, 858, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 54) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1712003, 3500, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 55) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1712003, 858, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 56) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1712004, 3500, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 57) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1712004, 858, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 58) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1712005, 3500, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 59) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1712005, 858, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 60) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1712006, 3500, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 61) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(17120206, 858, 20, 220, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b아케인 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#아케인심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 80) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1713000, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 81) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1713000, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 82) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1713000, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 83) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1713000, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 84) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1713000, 4375, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 85) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1713000, 975, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 86) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1713001, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 87) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1713001, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 88) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1713001, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 89) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1713001, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 90) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1713001, 4375, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 91) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1713001, 975, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();

                                    }
                         } else if (selection == 92) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemSTR(1713002, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 93) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemDEX(1713001, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 94) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemINT(1713002, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 95) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemLUK(1713002, 2500, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 96) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemHP(1713002, 4375, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();
			}
		} else if (selection == 97) {
			if (cm.getPlayer().getMeso() >= 1) {
				gainItemXENON(1713002, 975, 11, 110, 0)
				cm.gainMeso(-1);
				cm.sendOk("#fs11##b어센틱 심볼#k을 구매하셨습니다.");
				cm.dispose();
			} else {
				cm.sendOk("#fs11#어센틱심볼을 구매하기 위한 #r메소#k가 부족합니다.");
				cm.dispose();

                                    }			
}		
}	
}
}


function gainItemSTR(itemid, allstat, level, arc, exp) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
             //힘
	item.setStr(allstat);
            // 심볼 레벨
	item.setArcLevel(level);
            // 아케인/어센틱 포스 수치
	item.setArc(arc);
            // 심볼 경험치
	item.setArcEXP(exp);
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function gainItemDEX(itemid, allstat, level, arc, exp) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
             //덱스
	item.setDex(allstat);
            // 심볼 레벨
	item.setArcLevel(level);
            // 아케인/어센틱 포스 수치
	item.setArc(arc);
            // 심볼 경험치
	item.setArcEXP(exp);
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function gainItemINT(itemid, allstat, level, arc, exp) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
             //인트
	item.setInt(allstat);
            // 심볼 레벨
	item.setArcLevel(level);
            // 아케인/어센틱 포스 수치
	item.setArc(arc);
            // 심볼 경험치
	item.setArcEXP(exp);
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function gainItemLUK(itemid, allstat, level, arc, exp) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
             //럭
	item.setLuk(allstat);
            // 심볼 레벨
	item.setArcLevel(level);
            // 아케인/어센틱 포스 수치
	item.setArc(arc);
            // 심볼 경험치
	item.setArcEXP(exp);
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function gainItemHP(itemid, allstat, level, arc, exp) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
             //최대 체력
	item.setHp(allstat);
            // 심볼 레벨
	item.setArcLevel(level);
            // 아케인/어센틱 포스 수치
	item.setArc(arc);
            // 심볼 경험치
	item.setArcEXP(exp);
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function gainItemXENON(itemid, allstat, level, arc, exp) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
             //올스텟
	item.setStr(allstat);
	item.setDex(allstat);
	item.setLuk(allstat);
            // 심볼 레벨
	item.setArcLevel(level);
            // 아케인/어센틱 포스 수치
	item.setArc(arc);
            // 심볼 경험치
	item.setArcEXP(exp);
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}