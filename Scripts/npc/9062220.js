var status = -1;

var reward = [ 
	[ 10, 10, 2430368, 2, 2437574, 2 ],
	[ 20, 20, 4001716, 1, 4001716, 10 ],
	[ 30, 40, 2439999, 3, 2439999, 20 ],
	[ 40, 40, 4310308, 500, 4310308, 1500 ],
	[ 50, 50, 4310248, 1000, 4310248, 10000 ],
	[ 60, 60, 2630782, 1, 1113132, 1 ],
	[ 70, 70, 4001716, 3, 4001716, 20 ],
	[ 80, 80, 2439999, 6, 2439999, 30 ],
	[ 90, 90, 4310248, 2000, 4310248, 15000 ],
	[ 100, 100, 4310308, 700, 4310308, 2500 ],
	[ 130, 130, 2437573, 1, 2435712, 1 ],
	[ 150, 150, 4001716, 6, 4001716, 30 ],
	[ 170, 170, 2439999, 10, 2439999, 40 ],
	[ 200, 200, 4310248, 3000, 4310248, 20000 ],
	[ 230, 230, 4310308, 900, 4310308, 3000 ]
	];
var mine = [];

function start() {
    status = -1;
    action (1, 0, 0);
}

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
	var msg = "#fs11##fc0xFF000000#    현재 #fc0xFF6600CC##h0# #fc0xFF000000#님의 성장패스 레벨 :#fc0xFFFF3300# " + cm.getPlayer().getKeyValue(100592, "point") + "\r\n";
	msg += "#Cgray#――――――――――――――――――――――――――――――――――――――――";
	msg += "#L1##b성장패스 보상을 수령하겠습니다.\r\n";
	msg += "#L2#성장패스의 보상이 궁금합니다.";
	
	cm.sendSimple(msg);
    } else if (status == 1) {
	if (selection == 1) {
		var list = "#fs11##fc0xFF000000#자네가 받을 수 있는 보상은 아래와 같다네.#b\r\n";
		var level = cm.getPlayer().getKeyValue(100592, "point");
		for (var i = 0; i < reward.length; i++) {
			var isExist = false;
			if (level >= reward[i][1]) {
				list += "#L" + mine.length +"# ";
				if (cm.getPlayer().getKeyValue(1234, reward[i][0]+"-0") == -1) {
					list += "#fc0xFF990033#[" + reward[i][0] + "레벨 보상 받기]\r\n\r\n#b일반 보상 : #i" + reward[i][2] + "# #z" + reward[i][2] + "# " + reward[i][3] + "개\r\n";
					isExist = true;

				}
				if (cm.haveItem(4001760)) {
					if (cm.getPlayer().getKeyValue(1234, reward[i][0]+"-1") == -1) {
						if(reward[i][4] == 2437574) {
						list += "#fc0xFFFF3300#프리미엄 보상 : #i" + reward[i][4] + "# [HEINZ] 스네이크 장신구 상자 " + reward[i][5] + "개#l\r\n";
						} else {
						list += "#fc0xFFFF3300#프리미엄 보상 : #i" + reward[i][4] + "# #z" + reward[i][4] + "# " + reward[i][5] + "개#l\r\n";
						}
						list += "#Cgray#――――――――――――――――――――――――――――――――――――――――";
						isExist = true;
					}
				}
				if (isExist)
					mine.push(i);
				list += "";
			}
		}
		if (mine.length > 0) {
			cm.sendSimple(list);
		} else {
			cm.sendOk("#fs11##fc0xFF000000#받을 수 있는 보상이 없다네.");
			cm.dispose();
			return;
		}
	} else if (selection == 2) {
		var list = "";
		for (var i = 0; i < reward.length; i++) {
			
				if(reward[i][4] == 2437574) {
					list += "#fs11##fc0xFF990033##e [성장패스 " + reward[i][1] +" 레벨 보상]#n \r\n\r\n#b일반 보상 : #i" + reward[i][2] + "# #z" + reward[i][2] + "# " + reward[i][3] + "개 \r\n#fc0xFFFF3300#프리미엄 보상 : #i" + reward[i][4] + "# [HEINZ] 스네이크 장신구 상자 " + reward[i][5] + "개 \r\n#Cgray#――――――――――――――――――――――――――――――――――――――――";
				} else {
					list += "#fs11##fc0xFF990033##e [성장패스 " + reward[i][1] +" 레벨 보상]#n \r\n\r\n#b일반 보상 : #i" + reward[i][2] + "# #z" + reward[i][2] + "# " + reward[i][3] + "개 \r\n#fc0xFFFF3300#프리미엄 보상 : #i" + reward[i][4] + "# #z" + reward[i][4] + "# " + reward[i][5] + "개 \r\n#Cgray#――――――――――――――――――――――――――――――――――――――――";
				}
			}
		
		cm.sendOk(list);
            		cm.dispose();
           		return;
	}
    } else if (status == 2) {
	var sel = reward[mine[selection]];
	var msg = "";
	if (cm.getPlayer().getKeyValue(1234, sel[0]+"-0") == -1) {
		msg += "#b 일반 보상 : #i" + sel[2] + "# #z" + sel[2] + "# " + sel[3] + "개 획득\r\n";
		if (!cm.canHold(sel[2], sel[3])) {
			cm.sendOk("인벤토리에 공간이 부족하다네.");
			cm.dispose();
			return;
		}
		cm.gainItem(sel[2], sel[3]);
		cm.getPlayer().setKeyValue(1234, sel[0]+"-0", "1");
	}
	if (cm.haveItem(4001760)) {
		if (cm.getPlayer().getKeyValue(1234, sel[0]+"-1") == -1) {
			msg += "#fc0xFFFF3300#프리미엄 보상 : #i" + sel[4] + "# #z" + sel[4] + "# " + sel[5] + "개 획득 ";
			if (!cm.canHold(sel[4], sel[5])) {
				cm.sendOk("인벤토리에 공간이 부족하다네.");
				cm.dispose();
				return;
			}
			cm.gainItem(sel[4], sel[5]);
			cm.getPlayer().setKeyValue(1234, sel[0]+"-1", "1");
		}
	}
	cm.sendOk(msg);
            	cm.dispose();
           	return;
    }
}