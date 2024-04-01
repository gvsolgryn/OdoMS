var enter = "\r\n";
var seld = -2;

var key = "levelreward";
var selr;
var reward = [
	{'lvl' : 200, 'ap' : 200, 'item' : [
		{'itemid' : 2630127, 'qty' : 3}, //자석펫상자
		{'itemid' : 2450134, 'qty' : 2}, //경치3배
		{'itemid' : 4310320, 'qty' : 1500}, //사냥코인
		{'itemid' : 2048717, 'qty' : 50}, //영환불
		{'itemid' : 2434584, 'qty' : 10}, //조각
		{'itemid' : 2434585, 'qty' : 10}, //
		{'itemid' : 2434586, 'qty' : 10}, //
		{'itemid' : 2434587, 'qty' : 30}, //
	]},     
        {'lvl' : 210, 'ap' : 270, 'item' : [
                {'itemid' : 2049704, 'qty' : 2}, //레잠
                {'itemid' : 2450134, 'qty' : 3}, //경치3배
                {'itemid' : 2631527, 'qty' : 50}, //
                {'itemid' : 2048717, 'qty' : 150}, //영환불
                {'itemid' : 4001716, 'qty' : 3}, //10억
                {'itemid' : 4310320, 'qty' : 2000}, //사냥코인
        ]},
        {'lvl' : 220, 'ap' : 280, 'item' : [
                {'itemid' : 2450134, 'qty' : 5}, //경치3배
                {'itemid' : 4001716, 'qty' : 3}, //10억
                {'itemid' : 2048717, 'qty' : 150}, //영환불
                {'itemid' : 2049360, 'qty' : 5}, //놀장강
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
        ]},
	{'lvl' : 230, 'ap' : 300, 'item' : [
                {'itemid' : 2048717, 'qty' : 150}, //영환불
                {'itemid' : 4001716, 'qty' : 3}, //10억
                {'itemid' : 2049371, 'qty' : 1}, //17성
                {'itemid' : 2049360, 'qty' : 5}, //놀장강
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
        ]},
            {'lvl' : 240, 'ap' : 320, 'item' : [
	  	{'itemid' : 2048717, 'qty' : 200}, //영환불
                {'itemid' : 2049371, 'qty' : 5}, //17성
                {'itemid' : 5062006, 'qty' : 2}, //플미큐
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
        ]},
            {'lvl' : 250, 'ap' : 330, 'item' : [
	  	{'itemid' : 2048717, 'qty' : 300}, //영환불
	  	{'itemid' : 5062006, 'qty' : 3}, //플미큐
	  	{'itemid' : 4001716, 'qty' : 5}, //10억
                {'itemid' : 2432408, 'qty' : 1}, //칠흑뽑기
	  	{'itemid' : 4310320, 'qty' : 3000}, //사냥코인
	  	{'itemid' : 5060048, 'qty' : 1}, // 골드애플
                {'itemid' : 4310332, 'qty' : 10}, //마일
        ]},
            {'lvl' : 260, 'ap' : 350, 'item' : [
                {'itemid' : 4310330, 'qty' : 1}, //플사냥코인
                {'itemid' : 2048753, 'qty' : 20}, //검환불
                {'itemid' : 5062006, 'qty' : 3}, //플미큐
                {'itemid' : 5062503, 'qty' : 3}, //화에큐
                {'itemid' : 2049371, 'qty' : 5}, //17성
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
                {'itemid' : 5060048, 'qty' : 1}, //골드애플
            ]},
            {'lvl' : 270, 'ap' : 351, 'item' : [
                {'itemid' : 2048753, 'qty' : 30}, //검환불
                {'itemid' : 5062006, 'qty' : 3}, //플미큐
                {'itemid' : 5062503, 'qty' : 3}, //화에큐
                {'itemid' : 2049371, 'qty' : 5}, //17성
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
                {'itemid' : 5060048, 'qty' : 1}, //골드애플
                {'itemid' : 4001716, 'qty' : 5}, //10억
            ]},
            {'lvl' : 275, 'ap' : 370, 'item' : [
                {'itemid' : 2048753, 'qty' : 30}, //검환불
                {'itemid' : 4310333, 'qty' : 5}, //프리미엄마일
                {'itemid' : 5062006, 'qty' : 3}, //플미큐
                {'itemid' : 5062503, 'qty' : 3}, //화에큐
                {'itemid' : 2049376, 'qty' : 1}, //20성
                {'itemid' : 4310332, 'qty' : 10}, //마일
            ]},
            {'lvl' : 280, 'ap' : 370, 'item' : [
                {'itemid' : 4310330, 'qty' : 1}, //플사냥
                {'itemid' : 2048753, 'qty' : 40}, //검환불
                {'itemid' : 5062006, 'qty' : 5}, // 플미큐
                {'itemid' : 5062503, 'qty' : 5}, //화에큐
                {'itemid' : 4001716, 'qty' : 5}, //10억
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
                {'itemid' : 5060048, 'qty' : 1}, //골드애플
            ]},
            {'lvl' : 285, 'ap' : 370, 'item' : [
                {'itemid' : 2048753, 'qty' : 50}, //검환불
                {'itemid' : 5062006, 'qty' : 10}, // 플미큐
                {'itemid' : 5062503, 'qty' : 10}, //화에큐
                {'itemid' : 2049376, 'qty' : 1}, //20성
                {'itemid' : 4310333, 'qty' : 5}, //프리미엄마일
            ]},
            {'lvl' : 290, 'ap' : 370, 'item' : [
                {'itemid' : 4310330, 'qty' : 1}, //플사냥
                {'itemid' : 5062006, 'qty' : 10}, // 플미큐
                {'itemid' : 5062503, 'qty' : 10}, //화에큐
                {'itemid' : 2048753, 'qty' : 50}, //검환불
                {'itemid' : 4001716, 'qty' : 5}, //10억
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
                {'itemid' : 5060048, 'qty' : 1}, //골드애플
            ]},
            {'lvl' : 295, 'ap' : 370, 'item' : [
                {'itemid' : 4310333, 'qty' : 5}, //프리미엄마일
                {'itemid' : 5062006, 'qty' : 20}, // 플미큐
                {'itemid' : 5062503, 'qty' : 20}, //화에큐
                {'itemid' : 2048753, 'qty' : 60}, //검환불
                {'itemid' : 2049376, 'qty' : 1}, //20성
                {'itemid' : 4310320, 'qty' : 3000}, //사냥코인
                {'itemid' : 5060048, 'qty' : 2}, //골드애플
            ]},
            {'lvl' : 300, 'ap' : 370, 'item' : [
                {'itemid' : 4310333, 'qty' : 10}, //프리미엄 마일
                {'itemid' : 4310330, 'qty' : 3}, //플사냥
                {'itemid' : 2439629, 'qty' : 1}, //잠재설정
                {'itemid' : 2439630, 'qty' : 1}, //에디설정
                {'itemid' : 5062006, 'qty' : 50}, // 플미큐
                {'itemid' : 5062503, 'qty' : 50}, //화에큐
                {'itemid' : 4001716, 'qty' : 10}, //10억
                {'itemid' : 2049376, 'qty' : 2}, //20성
                {'itemid' : 4310320, 'qty' : 5000}, //사냥코인
                {'itemid' : 5060048, 'qty' : 3}, //골드애플

            ]},

         ]
function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, sel) {
	if (mode == -1) {
		cm.dispose();
	}
	if (mode == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1) {
		status++;
	}
	if (status == 0) {
		var msg = "#fs11# #d수령 가능한 레벨 보상은 아래와 같습니다.#k"+enter;




		if (cm.getPlayer().getLevel() >= 250) {
			check = getk(250) == -1 ? "수령 가능" : "수령 완료"
			//msg += "#L250##b250레벨 보상 #e("+check+")#n#k"+enter;
		} else {
			//msg += "#L250##r250레벨 보상 (수령불가)#k"+enter;
		}
		if (cm.getPlayer().getLevel() >= 275) {
			check = getk(275) == -1 ? "수령 가능" : "수령 완료"
			//msg += "#L275##b275레벨 보상 #e("+check+")#n#k"+enter;
		} else {
			//msg += "#L275##r275레벨 보상 (수령불가)#k"+enter;
		}
		for (i = 0; i < reward.length; i++) {

		      if (cm.getPlayer().getLevel() >= reward[i]['lvl']) {

		       if (cm.getPlayer().getKeyValue(888881, "levelRE_"+ reward[i]['lvl']) == reward[i]['lvl']  &&  cm.getPlayer().getClient().getKeyValue("levelrewardclear") > 0 ) {
                       check = getk(reward[i]['lvl']) == -1 ? "수령 완료" : "수령 완료"
                          msg += "#L"+i+"##r"+reward[i]['lvl']+"레벨 보상  - 타 캐릭터 수령 #e("+check+")#n#k"+enter;
                          } else {

                          check = getk(reward[i]['lvl']) == -1 ? "수령 가능" : "수령 완료"
                           msg += "#L"+i+"##b"+reward[i]['lvl']+"레벨 보상 #e("+check+")#n#k"+enter;

                          }

          } else{
            	msg += "#L"+i+"##r"+reward[i]['lvl']+"레벨 보상 (수령 불가)#k"+enter;
            }



       }

		cm.sendSimple(msg);
	} else if (status == 1) {


		seld = sel;

		if (seld > 200) {

			if (getk(seld) > -1) {
				cm.sendOk("#fs11##r이미 수령하신 보상입니다.");
				cm.dispose();
				return;
			}
			if (seld > cm.getPlayer().getLevel()) {
				cm.sendOk("#fs11##r레벨이 부족합니다.#k");
				cm.dispose();
				return;
			}
			if (seld == 250) {
				cm.forceCompleteQuest(6500);
				cm.forceCompleteQuest(12394);
				cm.forceCompleteQuest(12395);
				cm.forceCompleteQuest(12396);
				cm.setInnerStats(1);
				cm.setInnerStats(2);
				cm.setInnerStats(3);
			}
			if (seld == 275) {
				cm.getPlayer().gainAp(100); // 275레벨 스탯보상
				cm.getPlayer().levelUp();
			}
			setk(seld, "1");
			cm.sendOk("#fs11##r레벨업 달성보상이 지급되었습니다.");
			cm.dispose();
		} else {
			selr = reward[seld];

	         if (cm.getPlayer().getClient().getKeyValue("levelreward" + selr['lvl'] ) == selr['lvl']) {
	             cm.getPlayer().getClient().setKeyValue("levelrewardclear","1");
                 selStr = "levelreward 이미 1회 보상을 획득 하셨습니다.";

                 cm.sendOk(selStr);
                 cm.dispose();
                 return;
            }

			if (getk(selr['lvl']) > -1) {
				cm.sendOk("#fs11##r이미 수령하신 보상입니다."+enter+enter+getRewardList());
				cm.dispose();
				return;
			}
			if (selr['lvl'] > cm.getPlayer().getLevel()) {
				cm.sendOk("#fs11##r이 보상을 받기엔 레벨이 모자랍니다."+enter+enter+getRewardList());
				cm.dispose();
				return;
			}

			//cm.getPlayer().gainAp(selr['ap']);
			gainReward(selr['lvl']);
			setk(selr['lvl'], "1");
			setkk(selr['lvl'], "" + selr['lvl']);
			cm.sendOk("#fs11##r보상 지급이 완료되었습니다.");
			cm.dispose();
		}
		
	}
}

function gainReward(level) {
	for (p = 0; p < selr['item'].length; p++) {
		if (Math.floor(selr['item'][p]['itemid'] / 1000000) == 1) {
			gainItemall(selr['item'][p]['itemid'], selr['item'][p]['allstat'], selr['item'][p]['atk']);
		} else {
			cm.gainItem(selr['item'][p]['itemid'], selr['item'][p]['qty']);
		}
	}
}

function getRewardList() {
	var msg = "#b"+selr['lvl']+"#fs11#레벨#k 달성 보상리스트 입니다.#fs11#"+enter;
	msg += ""+enter;
	for (p = 0; p < selr['item'].length; p++) {
		if (Math.floor(selr['item'][p]['itemid'] / 1000000) == 1) {
			msg += "#i"+selr['item'][p]['itemid']+"##b#z"+selr['item'][p]['itemid']+"# "+selr['item'][p]['qty']+"개#k" +enter;
		} else {
			msg += "#i"+selr['item'][p]['itemid']+"##b#z"+selr['item'][p]['itemid']+"# "+selr['item'][p]['qty']+"개#k" + enter;
		}
	}
	return msg;
}

function gainItemall(itemid, allstat, atk) {
	item = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(itemid);
	/*item.setStr(allstat);
	item.setDex(allstat);
	item.setInt(allstat);
	item.setLuk(allstat);
	item.setWatk(atk);
	item.setMatk(atk);*/
	Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), item, false);
}

function getk(level) {

	return cm.getPlayer().getKeyValue(201820, key+"_"+level);
}

function setk(level, value) {
	cm.getPlayer().setKeyValue(201820, key+"_"+level, value);
}

function setkk(level, value) {
     cm.getPlayer().getClient().setKeyValue("levelreward" + level , value);
     cm.getPlayer().setKeyValue(888881 ,"levelRE_" + level , value);

}