var status = -1;
var enter = "\r\n";
var talkType = 0x86;

var NormalPassKeyValue = "Serenity_Premium_Pass_KeyValue";

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
        PremiumPassRewardFunction();
        if (cm.getPlayer().getV("HAS_PREMIUM_PASS") == null) {
            cm.sendOkS("KKILOOK PREMIUM PASS 이용권이 없습니다.", talkType);
            cm.dispose();
            return;
        } 
        if (cm.getPlayer().getSerenityPremiumPassCountComplete() == 11) {
            cm.sendOkS("이미 모든 #bKKILOOK PREMIUM PASS#k 를 클리어 하셨습니다.\r\n패스 초기화 후 다시 이용하시고 싶다면 마일리지샵에서 초기화권을 구매해주세요.", talkType);
            cm.dispose();
            return;
        }
        var chat = "KKILOOK 프리미엄 패스를 이용하시고 있습니다#k" + enter + enter;

        chat += "현재 #b" + cm.getPlayer().getName() + "#k님의 #b경험치#k : #fc0xFF191919#" + cm.getPlayer().getSerenityPremiumPassExpToString() +"#k"+ enter + enter;

                // Daily Check
                if (cm.getPlayer().getV("Clear_Pass_Premium_Kill_Monster_Amount") == "1" && !cm.getPlayer().isGM()) {
                    var chat = "KKILOOK 프리미엄 패스를 이용하시고 있습니다#k" + enter + enter;

                    chat += "현재 #b" + cm.getPlayer().getName() + "#k님의 #b경험치#k : " + cm.getPlayer().getSerenityPremiumPassExpToString() + enter + enter;
                    
                    chat += "#r금일 PREMIUM PASS 이용을 이미 하셨습니다 다음날 다시 이용해주세요";
                    cm.sendOkS(chat, talkType);
                    cm.dispose();
                    return;
                }

        chat += "#r금일 레벨 범위 몬스터 사냥 수 #k: (" + cm.getPlayer().getPassKillMonsterAmount()+ " / " + "#r5000" + "#k)" + enter + enter;
        chat += "#b[획득 가능한 보상] : " + "#i" + reward + "# " + rewardAmount +" 개"+ enter
        
        if (cm.getPlayer().getPassKillMonsterAmount() >= 5000) {
            chat += "#L0#" + "#r[클릭] #fc0xFF191919#Serenity 프리미엄 패스 보상 획득하기 ";
        } else {
            chat += "#k레벨범위 몬스터를 5000마리 이상 잡으시고 다시 말을 걸어주세요!";
	    cm.dispose();
        }

        cm.sendSimpleS(chat, talkType);
        // Clear Pass & Daily Mission
    } else if (status == 1) {
        if (selection == 0 ) {
            //cm.getPlayer().ClearPassKillMonsterAmount(); // Daily Clear
            cm.getPlayer().setSerenityPremiumPassCountComplete(1); // Increase Pass Exp +1
            cm.getPlayer().ClearPassKillPremiumMonsterAmount(); // Clear Key
            cm.getPlayer().setSerenityPass(); // Clear Monster Kill Count 
            cm.getPlayer().addPassKillMonsterAmount(5000);
            cm.getPlayer().gainItem(reward, rewardAmount);
            cm.sendOkS("#rKKILOOK PREMIUM PASS REWARD! 를 지급받았습니다 보상내역은 아래와 같습니다.\r\n\r\n#k획득한 보상 : #i" + reward+ "# #b" + rewardAmount + "개" , talkType);
            cm.dispose();
            return;
        }
    }
}

function PremiumPassRewardFunction() {
    var passRewardCount = cm.getPlayer().getSerenityPremiumPassCountComplete();
    var PremiumPassReward = 0;
    var PremiumPassRewardAmount = 0;
    switch(passRewardCount) {
        case 1:
            PremiumPassReward = 4310333; 
            PremiumPassRewardAmount = 10;
            break;
        case 2:
            PremiumPassReward =  4310330; // 프사냥코인
            PremiumPassRewardAmount = 5;
            break;
        case 3 :
            PremiumPassReward =  5060048; // 골드 애플 10개
            PremiumPassRewardAmount = 5;
            break;
        case 4:
            PremiumPassReward =  4033825; // 갈매기결정
            PremiumPassRewardAmount = 20;
            break;
        case 5 :
            PremiumPassReward =  4310301; //달사탕
            PremiumPassRewardAmount = 100;
            break;
        case 6:
            PremiumPassReward =  4310302; //별사탕
            PremiumPassRewardAmount = 100;
            break;
        case 7:
            PremiumPassReward =  4001833; //고급돌림판
            PremiumPassRewardAmount = 10;
            break;
        case 8 :
            PremiumPassReward =  2643133; //끼룩 링 강화
            PremiumPassRewardAmount = 1;
            break;
        case 9:
            PremiumPassReward =  4310312; //분홍콩
            PremiumPassRewardAmount = 200;
            break;
        case 10 :
            PremiumPassReward =  4310333; 
            PremiumPassRewardAmount = 30;
            break;
        }
        reward = PremiumPassReward;
        rewardAmount = PremiumPassRewardAmount;
}
    