/*
MelonK

importPackage(Packages.tools.packet);
importPackage(java.util);
var St = 0;

qnum = 5; // 퀘스트 고유번호 (겹치지만 않으면 됨);
questlist = [
[8644000, 200, "mob"],
[8644001, 200, "mob"],
[8644002, 200, "mob"],
[8644003, 200, "mob"],
[8644004, 200, "mob"],
[8644005, 200, "mob"],
[8644006, 200, "mob"],
[8644007, 200, "mob"],
[8644008, 200, "mob"],
[8644009, 200, "mob"],
[8644010, 200, "mob"],
[4036573, 50, "item"],
[4036574, 50, "item"]
];


//Don't Touch :D
count1 = 0;
count2 = 0;
isnewed = true;
qarr = [];
questarray = [];
color = ["b", "b", "b", "b", "b"];
var Stplus = true;

function start() {
    St = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    a = cm.getPlayer().getV("arcane_quest_" + qnum);
    if (mode == -1 || mode == 0) {
        if (St == 0 && Stplus == true) {
            isnewed = false;
            St = 3;
        } else {
            cm.dispose();
            return;
        }
    }
    if (mode == 1) {
        if (St == -1) {
            b = a;
        }
        if (b < 0 && St == 2 && selection != 100) {
            if (color[selection] == "b") {
                color[selection] = "k";
            } else {
                color[selection] = "b";
            }
        } else {
            St++;
        }
    }
    if (b == null || b < 1) { // 일일퀘스트를 받지 않았을때
        if (St == 0) {
            for (i = 0; i < questlist.length; i++) {
                if (b == null || b < 0) {
                    clearquest(questlist[i][0]);
                }
                qarr.push(questlist[i]);
            }
            if (b == null || b < 0) {
                for (i = 0; i < 3; i++) {
                    rd = Math.floor(Math.random() * questlist.length)
                    cm.getPlayer().addKV("arcana_" + questlist[rd][0] + "_count", 0);
                    cm.getPlayer().addKV("arcana_" + questlist[rd][0] + "_" + questlist[rd][2] + "q", questlist[rd][1]);
                    cm.getPlayer().addKV("arcana_" + questlist[rd][0] + "_isclear", 0);
                    questlist.splice(rd, 1); // 중복 방지
                }
            cm.getPlayer().addKV("arcane_quest_" + qnum, 0);
            } else {
                listed = 0;
                while (listed < 3) {
                    for (i = 0; i < questlist.length; i++) {
                        if (cm.getPlayer().getV("arcana_" + qarr[i][0] + "_" + qarr[i][2]+"q") > 0) {
                            questlist.splice(i, 1);
                            listed++;
                        }
                    }
		    if (listed < 3) {
			break;
		    }
                }
            }
            dialogue = "숲을 위해 할 수 있는 일들은 다음과 같아.\r\n\r\n"
            for (i = 0; i < qarr.length; i++) {
                if (cm.getPlayer().getV("arcana_" + qarr[i][0] + "_mobq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #o" + qarr[i][0] + "# " + qarr[i][1] + "마리 퇴치 \r\n"
                    questarray.push(qarr[i]);
                } else if (cm.getPlayer().getV("arcana_" + qarr[i][0] + "_itemq") > 0) {
                    dialogue += "#b#e[일일 퀘스트] #z" + qarr[i][0] + "# " + qarr[i][1] + "개 수집\r\n"
                    questarray.push(qarr[i]);
                }

            }
            cm.sendConductExchange(dialogue);
        } else if (St == 1) {
            cm.sendYesNo("어때? 무리 없이 해줄 수 있을 것 같아? 다른 할 일들도 있는데 그것도 봐볼래?\r\n\r\n#b(일부 의뢰 혹은 전체 의뢰를 제외시키고 목록을 재구성 합니다.)#k");
        } else if (St == 2) {
            newcheck = true;
            dialogue = "바꾸고 할 일을 골라봐.\r\n\r\n"
            for (i = 0; i < questarray.length; i++) {
                if (cm.getPlayer().getV("arcana_" + questarray[i][0] + "_mobq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #o" + questarray[i][0] + "# " + questarray[i][1] + "마리 퇴치#k#n#l\r\n"
                } else if (cm.getPlayer().getV("arcana_" + questarray[i][0] + "_itemq") > 0) {
                    dialogue += "#L" + i + "##" + color[i] + "#e[일일 퀘스트] #z" + questarray[i][0] + "# " + questarray[i][1] + "개 수집#k#n#l\r\n"
                }
            }
            dialogue += "\r\n#L100##r#e더 이상 바꿀만한 일은 없을 것 같아."
            cm.sendSimple(dialogue);
        } else if (St == 3) {
            for (i = 0; i < qarr.length; i++) {
                clearquest(qarr[i][0]);
            }
            talk = "";
            if (isnewed) {
	    talk += "선택한 할 일 대신 새로운 할 일을 보여줄게."
	}
	talk+= "모두 3가지야.\r\n\r\n";
            for (i = 0; i < 3; i++) {
                if (color[i] == "k") { // 제외되었으면 (색으로 판단)
                    isnewed = true;
                    rd = Math.floor(Math.random() * questlist.length)
                    questarray[i] = questlist[rd];
                    questlist.splice(rd, 1); // 중복 방지 (questlist 배열의 rd번째를 제거)
                }
                isnew = color[i] == "k" ? "#r#e[NEW]#k#n" : "#k#n"
                if (questarray[i][2] == "mob") {
                    talk += "#b#e[일일 퀘스트] #o" + questarray[i][0] + "# " + questarray[i][1] + "마리 퇴치#k#n " + isnew + "\r\n"
                } else {
                    talk += "#b#e[일일 퀘스트] #z" + questarray[i][0] + "# " + questarray[i][1] + "개 수집#k#n " + isnew + "\r\n"
                }
                cm.getPlayer().addKV("arcana_" + questarray[i][0] + "_count", 0);
                cm.getPlayer().addKV("arcana_" + questarray[i][0] + "_" + questarray[i][2] + "q", questarray[i][1]);
                cm.getPlayer().addKV("arcana_" + questarray[i][0] + "_isclear", 0);
            }
            cm.sendNext(talk);
	    cm.getPlayer().addKV("arcane_quest_" + qnum, 1);
            cm.dispose();
        }
    } else {
        if (St == 0) {
            dialogue = "....\r\n\r\n"
            dialogue2 = "";
            dialogue3 = "";
	 if (a >= 4) {
	    cm.sendOk("오늘은 숲을 위해 할 일이 더 이상 없어");
                cm.dispose();
                return;
            }
            for (i = 0; i < questlist.length; i++) {
                if (cm.getPlayer().getV("arcana_" + questlist[i][0] + "_mobq") > 0 && cm.getPlayer().getV("arcana_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #o" + questlist[i][0] + "# " + questlist[i][1] + "마리 퇴치#k"
                    if (cm.getPlayer().getV("arcana_" + questlist[i][0] + "_count") >= cm.getPlayer().getV("arcana_" + questlist[i][0] + "_mobq")) {
                        count1++;
                        dialogue2 += 선택지 + " (완료 가능)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 선택지 + " (진행 중)\r\n"
                    }
                } else if (cm.getPlayer().getV("arcana_" + questlist[i][0] + "_itemq") > 0 && cm.getPlayer().getV("arcana_" + questlist[i][0] + "_isclear") < 2) {
                    선택지 = "#L" + i + "##d[일일 퀘스트] #z" + questlist[i][0] + "# " + questlist[i][1] + "개 수집#k"
                    if (cm.itemQuantity(questlist[i][0]) >= cm.getPlayer().getV("arcana_" + questlist[i][0] + "_itemq")) {
                        count1++;
                        dialogue2 += 선택지 + " (완료 가능)\r\n"
                    } else {
                        count2++;
                        dialogue3 += 선택지 + " (진행 중)\r\n"
                    }
                }
            }
            if (count1 >= 1) {
                dialogue += "\r\n#fUI/UIWindow2.img/UtilDlgEx/list3#\r\n" // 완료 가능한 퀘스트 UI
            }
            dialogue += dialogue2;
            dialogue += "\r\n"
            if (count2 >= 1) {
                dialogue += "#fUI/UIWindow2.img/UtilDlgEx/list0#\r\n" // 진행중 퀘스트 UI
            }
            dialogue += dialogue3;
            cm.sendSimple(dialogue);
        } else if (St == 1) {
			
			if (!cm.canHold(1712004, 3)) {
				cm.sendOk("인벤토리에 공간이 부족합니다.");
				cm.dispose();
				return;
			}
            if ((cm.getPlayer().getV("arcana_" + questlist[selection][0] + "_mobq") > 0 && cm.getPlayer().getV("arcana_" + questlist[selection][0] + "_count") >= cm.getPlayer().getV("arcana_" + questlist[selection][0] + "_mobq")) ||
                (cm.getPlayer().getV("arcana_" + questlist[selection][0] + "_itemq") > 0 && cm.itemQuantity(questlist[selection][0]) >= cm.getPlayer().getV("arcana_" + questlist[selection][0] + "_itemq"))) {
	    cm.gainItem(1712004,3); // 심볼
        if(cm.getPlayer().getKeyValue(100592, "point") < 100){
            cm.getPlayer().setKeyValue(100592, "point", cm.getPlayer().getKeyValue(100592, "point")+1);
        }
	    var text2 = "벌써 끝난거야?! 예상보다 상당하네. 답례로 #i1712004# #z1712004#를 3개를 주고 싶어. 자 여기.\r\n\r\n";
	    text2 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n";
	    text2 += "#i1712004# #b#z1712004# #r#e3 개#k#n\r\n";
                cm.sendOk(text2);
                if (cm.getPlayer().getV("arcana_" + questlist[selection][0] + "_itemq") > 0) {
                    cm.gainItem(questlist[selection][0], -cm.getPlayer().getV("arcana_" + questlist[selection][0] + "_itemq"));
                }
                cm.getPlayer().addKV("arcana_" + questlist[selection][0] + "_isclear", 2);
                cm.getPlayer().addKV("arcane_quest_" + qnum, parseInt(cm.getPlayer().getV("arcane_quest_" + qnum)) + 1);
                cm.dispose();
            } else {
	cm.sendOk("아직 임무를 다 하지 못한거야?");
            }
            cm.dispose();
            return;

        }
    }
}

function clearquest(paramint) {
    cm.getPlayer().addKV("arcana_" + paramint + "_count", -1);
    cm.getPlayer().addKV("arcana_" + paramint + "_mobq", -1);
    cm.getPlayer().addKV("arcana_" + paramint + "_itemq", -1);
    cm.getPlayer().addKV("arcana_" + paramint + "_isclear", -1);
} */

var enter = "\r\n";
var seld = -1;

var need = [
	{'itemid' : 4036574, 'qty' : 1000}
]
var tocoin = 4001332, toqty = 1;

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
		var msg = "학교 과제 준비물 크레파스가 필요한대 .. 혹시 가지고있나?"+enter;

		for (i = 0; i < need.length; i++) {
			if (i != need.length - 1) msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개와"+enter;
			else msg += "#i"+need[i]['itemid']+"##z"+need[i]['itemid']+"# "+need[i]['qty']+"개를 주신다면 저에게 큰 도움이 될겁니다. 대신 제가 가진 #b#z"+tocoin+"##k1개를 드리겠습니다."+enter;
		}

		
		if (haveNeed(1))
			cm.sendNext(msg);
		else {
			msg += enter+enter+"허허.. 크레파스를 가지고 있지 않군요.....";
			cm.sendOk(msg);
			cm.dispose();
		}
	} else if (status == 1) {
		temp = [];
		for (i = 0; i < need.length; i++) {
			temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
		}
		temp.sort();
		max = temp[0];
		cm.sendGetNumber("당신은 최대 #b"+max+"번을#k 교환할 수 있군요..\r\n몇 번 교환하시겠습니까...?", 1, 1, max);
	} else if (status == 2) {
		if (!haveNeed(sel)) {
			cm.sendOk("당신이 소지한 아이템이 부족합니다.");
			cm.dispose();
			return;
		}
		for (i = 0; i < need.length; i++) {
			cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
		}
		cm.gainItem(tocoin, (toqty * sel));
		cm.sendOk("메소럭키백 을 지급해드렸습니다.");
		cm.dispose();
	}
}

function haveNeed(a) {
	var ret = true;
	for (i = 0; i < need.length; i++) {
		if (!cm.haveItem(need[i]['itemid'], (need[i]['qty'] * a)))
			ret = false;
	}
	return ret;
}