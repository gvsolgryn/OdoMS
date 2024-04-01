﻿var status = -1;

var setting = [["Normal_Dusk", 1, 450009400, 245], ["Chaos_Dusk", 1, 450009450, 245], ["Extreme_Dusk", 1, 450009450, 245], ["Hell_Dusk", 1, 450009450, 245]];
var name = ["노멀", "카오스", "익스트림", "헬"];

var hour, minute;
var year, month, date2, date, day

function start() {
    status = -1;
    action(1, 0, 0);
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
        talk = "검은 마법사의 사념으로 이루어진 거대 괴수 더스크를 내버려 두어선 안된다.\r\n\r\n#L0##b공허의 눈(노멀 모드)으로 이동한다.#r(레벨 245이상)#k#l\r\n#L1##b공허의 눈(카오스 모드)으로 이동한다.#r(레벨 245이상)#k#l\r\n#L2##b공허의 눈(익스트림 모드)으로 이동한다.#r(레벨 245이상)#k#l\r\n#L3##b공허의 눈(헬 모드)으로 이동한다.#r(레벨 245이상)#k#l\r\n";
        cm.sendSimpleS(talk, 0x26);

    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOkS("1인 이상 파티를 맺어야만 입장할 수 있습니다.", 0x26);
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(450009400) >= 1 || cm.getPlayerCount(450009450) >= 1 ) {
            cm.sendOkS("이미 누군가가 더스크에 도전하고 있습니다.\r\n다른채널을 이용 해 주세요.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOkS("파티장만이 입장을 신청할 수 있습니다.", 0x26);
            cm.dispose();
            return;
        } else if (!cm.allMembersHere()) {
            cm.sendOk("모든 멤버가 같은 장소에 있어야 합니다.");
            cm.dispose();
            return;
        }

                        	getData();
                             	time = new Date();
                                	year = time.getFullYear() % 100;
                                	month2 = time.getMonth() + 1;
                                	month = time.getMonth() + 1 < 10 ?  "0"+month2 : month2;
                             	date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
                               	date = year+"/"+month+"/"+date2;
                               	if (cm.getPlayer().getV_boss(""+setting[st][0]+"_" +date) == null) {
                                 cm.getPlayer().addKV_boss(""+setting[st][0]+"_" +date, "0");
                                 }
                             	var it = cm.getPlayer().getParty().getMembers().iterator();
                             	while (it.hasNext()) {
                             	    var chr = it.next();
                             	    var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                             	    if (pchr.getPlayer().getV_boss(""+setting[st][0]+"_" +date) >= 1) {
                             	        cm.sendOk("파티원의 입장 횟수를 확인해주시길 바랍니다.");
                             	        cm.dispose();
                             	        return;
                             	    }
                             	}
//        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
//            cm.sendOkS(cm.isBossString(setting[st][0]), 0x04, 9010061);
//            cm.dispose();
//            return;
//
//        } else

        if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "파티원 중 "
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                if (i != 0) {
                    talk += ", "
                }
                talk += "#b#e" + cm.LevelNotAvailableChrList(setting[st][3])[i] + ""
            }
            talk += "#k#n님의 레벨이 부족합니다. 더스크 " + name[st] + "모드는 " + setting[st][3] + " 레벨 이상만 입장 가능합니다.";
            cm.sendOkS(talk, 0x28);
            cm.dispose();
            return;
        } else {
             if (st == 0 || st == 1) {



                     	    var it = cm.getPlayer().getParty().getMembers().iterator();
                     	    while (it.hasNext()) {
                     	    var chr = it.next();
                     	    var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                     	    // pchr.getClient().setKeyValue_Boss(setting[st][0] + "_" + date, 1);
                     	      pchr.getPlayer().addKV_boss(""+setting[st][0]+"_" +date, 1);
                     	      cm.getPlayer().dropMessage(-1, " 보스 입장 : == " + (pchr.getV_boss(""+setting[st][0]+"_" +date)) + " || " + setting[st][0] );
                            	}
                                 em = cm.getEventManager(setting[st][0]);
                                 if (em != null) {
                                     cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
                                 }
                                 cm.addBoss(setting[st][0]);
                                 cm.dispose();
            } else if (st == 2) {
                cm.sendYesNoS("익스트림 모드에 입장을 선택하셨습니다. 익스트림 모드에서는 데미지가 50% 감소하지만, #b#e보다 강력한 장비 보상이 추가옵션과 함께 드롭됩니다.#n#k 해당 장비의 추가옵션은 #b#e이노센트 주문서 이용 시 함께 사라지며, 이는 복구가 불가능합니다.#k#n 입장하시겠습니까?", 4, 2007);
            } else if (st == 3) {
	    cm.sendYesNoS("헬 모드에 입장을 선택하셨습니다. 헬 모드에서는 데미지가 90% 감소하지만, #b#e보다 강력한 장비 보상이 추가옵션과 함께 드롭됩니다.#n#k 해당 장비의 추가옵션은 #b#e이노센트 주문서 이용 시 함께 사라지며, 이는 복구가 불가능합니다.#k#n 입장하시겠습니까?", 4, 2007);
	}
        }

    } else if (status == 2) {
        if (st == 2 || st == 3) {
         	    var it = cm.getPlayer().getParty().getMembers().iterator();
                         	    while (it.hasNext()) {
                         	    var chr = it.next();
                         	    var pchr = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(chr.getId());
                         	    // pchr.getClient().setKeyValue_Boss(setting[st][0] + "_" + date, 1);
                         	      pchr.getPlayer().addKV_boss(""+setting[st][0]+"_" +date, 1);
                         	      cm.getPlayer().dropMessage(-1, " 보스 입장 : == " + (pchr.getV_boss(""+setting[st][0]+"_" +date)) + " || " + setting[st][0] );
                                	}
                                     em = cm.getEventManager(setting[st][0]);
                                     if (em != null) {
                                         cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
                                     }
                                     cm.addBoss(setting[st][0]);
                                     cm.dispose();
	            }
        }
    }


function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0"+month;
    }
    date2 = time.getDate() < 10 ? "0"+time.getDate() : time.getDate();
    date = year+""+month+""+date2;
    day = time.getDay();
    hour = time.getHours();
    minute = time.getMinutes();
}