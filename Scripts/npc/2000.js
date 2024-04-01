별 = "#fUI/FarmUI.img/objectStatus/star/whole#"
importPackage(Packages.tools.packet);
importPackage(java.lang);
importPackage(Packages.handling.channel);
var status;
var questcompleted = 0;
user = 0;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {

/* questlist 
아이템 모아오기
[아이템코드, "mob", 개수, 비밀퀘스트여부, 레벨]
레벨 찍기
[0, "level", 0, 비밀퀘스트여부, 레벨]
메소 모아오기
[0, "meso", 수치, 비밀퀘스트여부, 레벨]
캐시 모아오기
[0, "mpoint", 수치, 비밀퀘스트여부, 레벨]
파티생성하기
[0,"party",파티원수,비밀퀘스트여부,레벨]
보스 잡기
[보스이름,"boss",몇번잡아야되는지,비밀퀘스트여부,레벨]
동접 n명 이상
[0, "howmany", 수치, 비밀퀘스트여부, 레벨]
킬포인트
[0, "kpoint", 수치, 비밀퀘스트여부, 레벨]
아이템 보유하기 (안 가져감)
[아이템코드, "item", 개수, 비밀퀘스트여부, 레벨]
*/

    questlist = [
        [280, "level", 0, false, 280, -1],
		[4310156, "mob", 100, false, 250, 450004000],		
        [4001843, "mob", 50, false, 250, 350060300],
        [4001869, "mob", 50, false, 250, 105300303],
        [4001879, "mob", 50, false, 250, 450004000],
        [285, "level", 0, false, 285, -1],
        [2437760, "mob", 300, false, 280, 100000000],
        [4001893, "mob", 100, false, 280, -1],
        [290, "level", 0, false, 290, -1],
        [4310229, "mob", 9999, false, 280, 100000000],
        [295, "level", 0, false, 295, -1],
        [300, "level", 0, false, 300, -1],
		[4310308, "mob", 5000, false, 300, 100000000]
    ];
/* itemlist
일반 아이템
[아이템코드, "item", 개수, 0]
기간아이템
[아이템코드, "itemPeriod", 개수, 기간(단위 : 일)]
장비아이템 올스탯 공마 붙여서
[아이템코드, "EqpAllStatAtk", 올스탯, 공마]
*/
    itemlist = [
        [[4001716, "item", 20, 0]],
        [[4310248, "item", 5000, 0]],
        [[4310248, "item", 5000, 0]],
        [[4310248, "item", 5000, 0]],
        [[4310248, "item", 5000, 0]],
        [[4310266, "item", 300, 0]],
        [[4310308, "item", 50, 0]],
        [[4310308, "item", 100, 0]],
        [[4310308, "item", 200, 0]],
        [[4310308, "item", 300, 0]],
        [[4310308, "item", 400, 0]],
        [[4310308, "item", 500, 0]],
        [[2049372, "item", 2, 0]]
    ];

    rewardlist = [
        [[4001716, 20]], 
        [[4310248, 5000]],
        [[4310248, 5000]],
        [[4310248, 5000]],
        [[4310248, 5000]],
        [[4310266, 300]],
        [[4310308, 50]],
        [[4310308, 100]],
        [[4310308, 200]],
        [[4310308, 300]],
        [[4310308, 400]],
        [[4310308, 500]],
        [[2049372, 2]]
    ];

    if (mode <= 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        for (i = 0; i < ChannelServer.getAllInstances().length; i++) {
            user += ChannelServer.getAllInstances().get(i).getPlayerStorage().getAllCharacters().length;
        }
        말 = "#fc0xFF6600CC##e#fs11##fn나눔고딕#[HEINZ] 육성 퀘스트#n#k\r\n\r\n";
        말 += "#b" + questlist.length + " 개#k 의 #b#e[HEINZ] 육성 퀘스트#k#n를 완료하고 보상을 획득하세요!\r\n#r#e#fs12##k#fs11##n\r\n";
        말 += "#b * 모든 다이어리 완료보상 : #b#i4310266##z4310266# #r2000개#k\r\n";

        for (i = 0; i < questlist.length; i++) {

          if (Integer.parseInt(cm.getPlayer().getKeyValue(20190710+i,"diary")) != -1) {
	
                questcompleted++;
		            } else {
                말 += "#fs11##L" + i + "#"
                if (cm.getPlayer().getLevel() >= questlist[i][4]) {
                    말 += "#b"
                } else {
                    말 += "#Cgray#"
                }
                말 += getQuestType(questlist[i][0], questlist[i][1], questlist[i][2], questlist[i][3], questlist[i][4])
                말 += " #d(" + isQuestCompletable(questlist[i][0], questlist[i][1], questlist[i][2], questlist[i][4]) + ")#k#l\r\n\r\n";
	  for (j = 0; j < rewardlist[i].length; j++)
                    말 += "#fs11#     #fc0xFFF781D8#ㄴ 보상 : #i"+rewardlist[i][j][0]+"# #b#z"+rewardlist[i][j][0]+"# "+rewardlist[i][j][1]+"개#k\r\n"
            }
        }
        if (cm.getPlayer().getKeyValue(20190710,"questallcompleted") == -1) {
            말+= "\r\n#L1000# #b#e모든 퀘스트를 완료 하셨습니다! #d("
            if (questcompleted == questlist.length || cm.getPlayer().getGMLevel() >= 11) { // GM테스트
                말+= "완료가능)#k#l"
            } else {
                말+= "진행중)#r\r\n"
                말+= "["+questcompleted+"개의 퀘스트를 완료했으며, "+(questlist.length - questcompleted)+"개의 퀘스트가 진행중 입니다.]"
            }
        }
        cm.sendSimple(말);
    } else if (status == 1) {
        if (selection == 1000) {
            말2="";
            if (questcompleted == questlist.length || cm.getPlayer().getGMLevel() >= 11) {
                gift = [[4310266,2000]];
                for (i=0; i<gift.length; i++) {
                    말2 += "#i"+gift[i][0]+"# #b#z"+gift[i][0]+"# "+gift[i][1]+"개#k\r\n"
                    cm.gainItem(gift[i][0], gift[i][1]);
                }
                cm.getPlayer().setKeyValue(20190710,"questallcompleted", 1);
                말 = "모든 퀘스트를 완성 하셨습니다! 보상으로 아래와 같은 아이템을 지급해드렸습니다!\r\n\r\n";
                말+= 말2;
                cm.sendOk(말);
            } else {
                cm.sendOk("아직 모든 퀘스트를 완료하지 못한 것 같습니다.");
                cm.dispose();
            }
        } else {
        st = selection;
        qt = questlist[st]
        if (isQuestCompletable(qt[0], qt[1], qt[2], qt[4]) == "완료 가능") {
            for (i = 0; i < itemlist[st].length; i++) {
                gainItemByType(itemlist[st][i][0], itemlist[st][i][1], itemlist[st][i][2], itemlist[st][i][3])
            }
            gainReqitemByType(qt[0], qt[1], qt[2]);
            cm.getPlayer().setKeyValue(20190710 + st, "diary", 1);
            cm.sendOk("퀘스트를 완료했습니다! 보상을 지급해드렸습니다!");
        } else {
            mapid = qt[5];
            if (mapid < 0)
                cm.sendOk("현재 이 퀘스트를 수행하기 위한 레벨이 부족하거나, 퀘스트를 완료하지 못한 것 같습니다.");
            else
                cm.warp(qt[5]);
        }
        }
        cm.dispose();
    }
}

function getQuestType(qid, qtype, qnum, isSecret, qlevel) {
    if (!isSecret) {
        switch (qtype) {
            case "mob":
                return "#e[Lv." + qlevel + "]#n #fc0xFFF361A6##z" + qid + "##k #r" + nf(qnum) + "개#k 모으기#k";
                break;
            case "level":
                return "#e[Lv." + qlevel + "]#n #r" + qlevel + "#k레벨 달성하기#k";
                break;
            case "item":
                return "#e[Lv." + qlevel + "]#n #fc0xFFF361A6##z" + qid + "##k #r" + nf(qnum) + "개#k 이상 소지#k";
                break;
            case "party":
                return "#e[Lv." + qlevel + "]#n #r" + qnum + "#k명 이상의 파티에 속해있기#k";
                break;
            case "boss":
                return "#e[Lv." + qlevel + "]#n #r" + qid + "#k를 " + qnum + "번 이상 클리어 하기#k";
                break;
            case "meso":
                return "#e[Lv." + qlevel + "]#n #r" + nf(qnum) + "#k 메소 이상 보유#k";
                break;
            case "mpoint":
                return "#e[Lv." + qlevel + "]#n #r" + nf(qnum) + "#k P 이상 캐시 보유하기#k";
                break;
            case "kpoint":
                return "#e[Lv." + qlevel + "]#n #r" + nf(qnum) + "#k 이상 칸 커뮤니티 포인트 보유하기#k";
                break;
            case "howmany":
                return "#e[Lv." + qlevel + "]#n #k#fc0xFFF361A6#동접 #r" + nf(qnum) + "명#k 달성하기";
                break;
            default:
                return "오류입니다.";
                break;
        }
    } else {
        return "[Lv. " + qlevel + "] 달성 조건이 비밀입니다.";
    }
}

function isQuestCompletable(qid, qtype, qnum, qlevel) {
    if (cm.getPlayer().getLevel() >= qlevel) {
        switch (qtype) {
            case "mob":
                if (cm.itemQuantity(qid) >= qnum) {
                    return "완료 가능"
                } else {
                    return "진행 중"
                }
                break;
            case "level":
                return "완료 가능"
                break;
            case "item":
                if (cm.itemQuantity(qid) >= qnum) {
                    return "완료 가능"
                } else {
                    return "진행 중"
                }
                break;
            case "party":
                if (cm.getPlayer().getParty() != null && cm.getPartyMembers().size() >= qnum) {
                    return "완료 가능"
                } else {
                    return "진행 중"
                }
                break;
            case "boss":
                if (cm.GetCount(qid,1) >= qnum) {
                    return "완료 가능"
                } else {
                    return "진행 중"
                }
                break;
            case "meso":
                if (cm.getPlayer().getMeso() >= qnum) {
                    return "완료 가능"
                } else {
                    return "진행 중"
                }
                break;
            case "mpoint":
                if (cm.getPlayer().getCSPoints(1) >= qnum) {
                    return "완료 가능"
                } else {
                    return "진행 중"
                }
                break;
            case "kpoint":
/*                if (cm.getPlayer().getbounscoin() >= qnum) { // 임시
                    return "완료 가능"
                } else {
                    return "진행 중"
                }*/
                break;
            case "howmany":
                if (user >= qnum) {
                    return "완료 가능"
                } else {
                    return "진행 중"
                }
                break;
            default:
                return "오류입니다.";
                break;
        }
    } else {
        return "레벨 부족"
    }
}

function gainReqitemByType(qid, qtype, qnum) {
    switch (qtype) {
        case "mob":
            cm.gainItem(qid, -qnum);
            break;
        case "meso":
            cm.gainMeso(-qnum);
            break;
        case "mpoint":
            cm.getPlayer().modifyCSPoints(2, -qnum, false);
            break;
        case "kpoint":
   cm.getPlayer().gainbounscoin(-qnum);
            break;
        default:
            break;
    }
}

function gainItemByType(iid, itype, i1, i2) {
    switch (itype) {
        case "item":
            cm.gainItem(iid, i1);
            break;
        case "meso":
            cm.gainMeso(i1);
            break;
        case "itemPeriod":
            cm.gainItemPeriod(iid, i1, i2);
            break;
        case "EqpAllStatAtk":
            break;
        case "Point":
            cm.getPlayer().AddStarDustCoin(i1);
            break;
        default:
            cm.sendOk("오류가 발생하였습니다.");
            cm.dispose();
            break;
    }
}

function nf(paramint) {
    return java.text.NumberFormat.getInstance().format(paramint);
}