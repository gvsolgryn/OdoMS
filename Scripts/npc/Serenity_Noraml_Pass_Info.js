var status = -1;
var enter = "\r\n";
var talkType = 0x86;

var NormalPassKeyValue = "Serenity_Normal_Pass_KeyValue";

var PassEvenetKey = "Pass_kill_Monster_amount";

var reward;
var rewardAmount;
function start() {
    action (1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status --;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        NormalPassRewardFunction();

        if (cm.getPlayer().getSerenityNormalPassCountComplete() == 11) {
            cm.sendOkS("이미 모든 #bKKILOOK NORMAL PASS#k 를 클리어 하셨습니다.\r\n패스 초기화 후 다시 이용하시고 싶다면 마일리지샵에서 초기화권을 구매해주세요.", talkType);
            cm.dispose();
            return;
        }
        var chat = "끼룩 일반 패스를 이용하시고 있습니다#k" + enter + enter;

        chat += "현재 #b" + cm.getPlayer().getName() + "#k님의 #b경험치#k : #fc0xFF191919#" + cm.getPlayer().getSerenityNormalPassExpToString() +"#k"+ enter + enter;

                // Daily Check
                if (cm.getPlayer().getV("Clear_Pass_Kill_Monster_Amount") == "1" && !cm.getPlayer().isGM()) {
                    var chat = "끼룩 일반 패스를 이용하시고 있습니다#k" + enter + enter;

                    chat += "현재 #b" + cm.getPlayer().getName() + "#k님의 #b경험치#k : " + cm.getPlayer().getSerenityNormalPassExpToString() + enter + enter;
                    
                    chat += "#r금일 NORMAL PASS 이용을 이미 하셨습니다 다음날 다시 이용해주세요";
                    cm.sendOkS(chat, talkType);
                    cm.dispose();
                    return;
                }

        chat += "#r금일 레벨 범위 몬스터 사냥 수 #k: (" + cm.getPlayer().getPassKillMonsterAmount()+ " / " + "#r5000" + "#k)" + enter + enter;
        chat += "#b[획득 가능한 보상] : " + "#i" + reward + "# " + rewardAmount +" 개"+ enter
        
        if (cm.getPlayer().getPassKillMonsterAmount() >= 5000) {
            chat += "#L0#" + "#r[클릭] #fc0xFF191919#Serenity 일반 패스 보상 획득하기 ";
        } else {
            chat += "#k레벨범위 몬스터를 5000마리 이상 잡으시고 다시 말을 걸어주세요!";
	    cm.dispose();
        }

        cm.sendSimpleS(chat, talkType);
        // Clear Pass & Daily Mission
    } else if (status == 1) {
        if (selection == 0 ) {
            cm.getPlayer().ClearPassKillMonsterAmount(); // Daily Clear
            cm.getPlayer().setSerenityNormalPassCountComplete(1); // Increase Pass Exp +1
           // cm.getPlayer().ClearPassKillMonsterAmount(); // Clear Key
            cm.getPlayer().setSerenityPass(); // Clear Monster Kill Count 
            cm.getPlayer().addPassKillMonsterAmount(5000);
            cm.getPlayer().gainItem(reward, rewardAmount);
            cm.sendOkS("#rKKILOOK NORMAL PASS REWARD! 를 지급받았습니다 보상내역은 아래와 같습니다.\r\n\r\n#k획득한 보상 : #i" + reward+ "# #b" + rewardAmount + "개" , talkType);
            cm.dispose();
            return;
        }
    }
}

function NormalPassRewardFunction() {
    var passRewardCount = cm.getPlayer().getSerenityNormalPassCountComplete();
    var NormalPassReward = 0;
    var NormalPassRewardAmount = 0;
    switch(passRewardCount) {
        case 1:
            NormalPassReward = 2049704; // 레잠
            NormalPassRewardAmount = 5;
            break;
        case 2:
            NormalPassReward = 4310320; // 사냥코인
            NormalPassRewardAmount = 5000;
            break;
        case 3 :
            NormalPassReward = 5060048; // 골드 애플
            NormalPassRewardAmount = 2;
            break;
        case 4:
            NormalPassReward = 2437750; // 10억 메소 주머니
            NormalPassRewardAmount = 30;
            break;
        case 5 :
            NormalPassReward = 4310332; //마일리지
            NormalPassRewardAmount = 10;
            break;
        case 6:
            NormalPassReward = 4001833; //고급 돌림판
            NormalPassRewardAmount = 5;
            break;
        case 7:
            NormalPassReward = 2439527; // 칠흑 선택 상자 1개
            NormalPassRewardAmount = 1;
            break;
        case 8 :
            NormalPassReward = 4310330; // 프리미엄 사냥코인
            NormalPassRewardAmount = 3;
            break;
        case 9:
            NormalPassReward = 4033825; // 갈매기결정
            NormalPassRewardAmount = 10;
            break;
        case 10 :
            NormalPassReward = 4310332; // 마일리지
            NormalPassRewardAmount = 50;
            break;
        }
        reward = NormalPassReward;
        rewardAmount = NormalPassRewardAmount;
}
    