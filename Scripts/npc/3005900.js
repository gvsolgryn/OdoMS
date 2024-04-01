var enter = "\r\n";
var qS = -1;
var Start = false;
var End = false;

/*
qid = 퀘스트번호입력 // 겹치면 절대안되여 번호
mobid = 몹코드
mobcount = 몹몇마리 잡는지
qcount = 하루 몇회
info = 0 고정

reward = [ [아이템코드, 갯수], [아이템코드, 갯수] ]
*/

var quest = [
    //{'qid': 2301, 'mobid': 8630015, 'mobcount': 5000, 'qcount': 1, 'info': 0,
    //'reward': [[4310218, 1], [4310218, 1], [4310218, 3]], 'meso': 0,},

    {'qid': 2302, 'mobid': 8645012, 'mobcount': 10000, 'qcount': 10, 'info': 0,
    'reward': [[4310156, 1], [4034181, 1]], 'meso': 0,},

    {'qid': 2303, 'mobid': 8645126, 'mobcount': 10000, 'qcount': 10, 'info': 0,
    'reward': [[4310218, 1], [4034182, 1]], 'meso': 0,},

    {'qid': 2304, 'mobid': 8645290, 'mobcount': 10000, 'qcount': 10, 'info': 0,
    'reward': [[4310218, 2], [4034183, 1]], 'meso': 0,},
]

function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
        //cm.getPlayer().setKeyValue(2301, "info", 2)
        // cm.getPlayer().setKeyValue(2302, "mobkill", 10)
        // cm.getPlayer().setKeyValue(2302, "qcount", 0)
        //msg2(cm.getPlayer().getKeyValue(2301, "info"))
        // if(!cm.getPlayer().isGM()){
		// 	cm.sendOk("안녕하세요.");
		// 	cm.dispose();
		// 	return;
		// }
        var msg = "#b일일퀘스트 담당 미하일이야. 수행할 퀘스트를 골라!"+enter
        //시작가능
        for (i = 0; i < quest.length; i++){
            if(cm.getPlayer().getKeyValue(quest[i]['qid'], "info") <= 0){
                msg += enter+"#fUI/UIWindow2.img/UtilDlgEx/list1#"+enter
                break;
            }
        }
        for (i = 0; i < quest.length; i++){
            if(cm.getPlayer().getKeyValue(quest[i]['qid'], "info") <= 0){
                msg += "#L"+i+"# #o"+quest[i]['mobid']+"# "+quest[i]['mobcount']+"마리 사냥"+enter
            }
        }
        //진행중
        for (i = 0; i < quest.length; i++){
            if(cm.getPlayer().getKeyValue(quest[i]['qid'], "info") == 1){
                msg += enter+"#fUI/UIWindow2.img/UtilDlgEx/list0#"+enter
                break;
            }
        }
        for (i = 0; i < quest.length; i++){
            if(cm.getPlayer().getKeyValue(quest[i]['qid'], "info") == 1){
                msg += "#L"+i+"# #o"+quest[i]['mobid']+"# "+quest[i]['mobcount']+"마리 사냥"+enter
            }
        }
        //완료가능
        for (i = 0; i < quest.length; i++){
            if(cm.getPlayer().getKeyValue(quest[i]['qid'], "info") == 2){
                msg += enter+"#fUI/UIWindow2.img/UtilDlgEx/list3#"+enter
                break;
            }
        }
        for (i = 0; i < quest.length; i++){
            if(cm.getPlayer().getKeyValue(quest[i]['qid'], "info") == 2){
                msg += "#L"+i+"# #o"+quest[i]['mobid']+"# "+quest[i]['mobcount']+"마리 사냥"+enter
            }
        }
		cm.sendSimple(msg);
    } else if(St == 1) {
        qS = S;
        var msg = ""
        if(cm.getPlayer().getKeyValue(quest[qS]['qid'], "info") <= 0){
            Start = true;
            msg += "#o"+quest[qS]['mobid']+"# "+quest[qS]['mobcount']+"마리 사냥 (남은횟수 : "+(quest[qS]['qcount'] - cm.getPlayer().getKeyValue(quest[qS]['qid'], "qcount"))+"회)"+enter
            msg += enter+"#fs11##b#e[획득 가능 보상]#n#k#l"+enter
            for (i = 0; i < quest[qS]['reward'].length; i++){
                msg += "#i"+quest[qS]['reward'][i][0]+"# #z"+quest[qS]['reward'][i][0]+"# "+quest[qS]['reward'][i][1]+"개"+enter+enter
            }
            if(quest[qS]['meso'] > 0){
                msg += enter+"#fUI/UIWindow2.img/QuestIcon/7/0#"+enter
                msg += numberToKorean(quest[qS]['meso'])+" 메소"+enter
            }
            msg += "#r위 몬스터를 잡으시고 저에게 오시면됩니다."
            cm.sendYesNo(msg);
        } else if(cm.getPlayer().getKeyValue(quest[qS]['qid'], "info") == 1){
            mobC = cm.getPlayer().getKeyValue(quest[qS]['qid'], "mobkill");
            mobCT = cm.getPlayer().getKeyValue(quest[qS]['qid'], "mobcount");
            msg += "아직 몬스터를 다 잡지 못하셧군요"+enter+enter
            msg += "[현재 진행율]"+enter
            msg += "#b#o"+quest[qS]['mobid']+"# ("+mobC+"/"+mobCT+"마리)#k"+enter
            cm.sendOk(msg);
            cm.dispose();
            return;
        } else if(cm.getPlayer().getKeyValue(quest[qS]['qid'], "info") == 2){
            End = true;
            msg += "몬스터를 모두 잡아오셧군요"+enter
            msg += enter+"#fUI/UIWindow2.img/QuestIcon/4/0#"+enter
            for (i = 0; i < quest[qS]['reward'].length; i++){
                msg += "#i"+quest[qS]['reward'][i][0]+"# #z"+quest[qS]['reward'][i][0]+"# "+quest[qS]['reward'][i][1]+"개"+enter
            }
            if(quest[qS]['meso'] > 0){
                msg += enter+"#fUI/UIWindow2.img/QuestIcon/7/0#"+enter
                msg += numberToKorean(quest[qS]['meso'])+" 메소"+enter
            }
            msg += enter+"지금 보상을 받으시겠습니까?"
            cm.sendYesNo(msg);
        }
    } else if(St == 2) {
        var msg = ""
        if(Start == true){
            cm.StartDailyQuest(quest[qS]['qid'], quest[qS]['mobid'], quest[qS]['mobcount']);
            msg += "#o"+quest[qS]['mobid']+"# "+quest[qS]['mobcount']+"마리 사냥 (남은횟수 : "+quest[qS]['qcount']+"회)"+enter
            msg += enter+"#fs11##b#e[획득 가능 보상]#n#k#l"+enter
            for (i = 0; i < quest[qS]['reward'].length; i++){
                msg += "#i"+quest[qS]['reward'][i][0]+"# #z"+quest[qS]['reward'][i][0]+"#"+enter+enter
            }
            if(quest[qS]['meso'] > 0){
                msg += enter+"#fUI/UIWindow2.img/QuestIcon/7/0#"+enter
                msg += numberToKorean(quest[qS]['meso'])+" 메소"+enter
            }
            msg += "#r위 몬스터를 잡으시고 저에게 오시면됩니다."
        } else if(End == true){
            EndDailyQuest(quest[qS]['qid'], quest[qS]['qcount'])
            msg += "고생하셨습니다 아래와 같은 보상을 드리지요."+enter
            msg += enter+"#fUI/UIWindow2.img/QuestIcon/4/0#"+enter
            for (i = 0; i < quest[qS]['reward'].length; i++){
                msg += "#i"+quest[qS]['reward'][i][0]+"# #z"+quest[qS]['reward'][i][0]+"# "+quest[qS]['reward'][i][1]+"개"+enter
                cm.gainItem(quest[qS]['reward'][i][0], quest[qS]['reward'][i][1]);
            }
            if(quest[qS]['meso'] > 0){
                msg += enter+"#fUI/UIWindow2.img/QuestIcon/7/0#"+enter
                msg += numberToKorean(quest[qS]['meso'])+" 메소"+enter
                cm.gainMeso(quest[qS]['meso']);
            }
            msg += enter+"남은횟수 : "+(quest[qS]['qcount'] - cm.getPlayer().getKeyValue(quest[qS]['qid'], "qcount"))+"회"
        }
        cm.sendOk(msg);
        cm.dispose();
	}
}

function EndDailyQuest(qnum, qcount){
    cm.getPlayer().setKeyValue(qnum, "info", 0);
    cm.getPlayer().setKeyValue(qnum, "mobkill", 0);
    if(cm.getPlayer().getKeyValue(qnum, "qcount") != qcount-1){
        cm.getPlayer().setKeyValue(qnum, "qcount", cm.getPlayer().getKeyValue(qnum, "qcount")+1);
    } else {
        cm.getPlayer().setKeyValue(qnum, "qcount", cm.getPlayer().getKeyValue(qnum, "qcount")+1);
        cm.getPlayer().setKeyValue(qnum, "info", 3);
    }
}


function numberToKorean(number) {
    var inputNumber = number < 0 ? false : number;
    var unitWords = ["", "만", "억", "조", "경"];
    var splitUnit = 10000;
    var splitCount = unitWords.length;
    var resultArray = [];
    var resultString = "";

    for (var i = 0; i < splitCount; i++) {
        var unitResult = (inputNumber % Math.pow(splitUnit, i + 1)) / Math.pow(splitUnit, i);
        unitResult = Math.floor(unitResult);
        if (unitResult > 0) {
            resultArray[i] = unitResult;
        }
    }

    for (var i = 0; i < resultArray.length; i++) {
        if (!resultArray[i]) continue;
        resultString = String(resultArray[i]) + unitWords[i] + resultString;
    }

    return resultString;
}

function msg2(text){
	cm.getPlayer().dropMessage(5, ""+text)
}