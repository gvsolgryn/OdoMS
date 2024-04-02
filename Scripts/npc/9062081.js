importPackage(Packages.server);
importPackage(java.lang);

var enter = "\r\n";
var year, month, date2, date, day
var hour, minute;

var questt = "jump_18713"; // jump_고유번호

var reward = [
    {'itemid': 2432423, 'qty': 1}

]

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
        if (cm.getClient().getKeyValue(questt) == null)
            cm.getClient().setKeyValue(questt, "0");

        if (Integer.parseInt(cm.getClient().getKeyValue(questt)) > 0) {
            cm.sendOk("#fs11#이미 오늘의 보상을 지급하였습니다..");
            cm.dispose();
            return;
        }
        var msg = " 동접 30명 이벤트 보상  새해복 많이 받으시길바랍니다 .~~~." + enter;
        msg += "클리어 보상을 받으시겠습니까?";
        cm.sendYesNo(msg);
    } else if (status == 1) {
        if (Integer.parseInt(cm.getClient().getKeyValue(questt)) > 0) {
            cm.sendOk("#fs11#오늘의 보상을 지급하였습니다..");
            cm.dispose();
            return;
        }
        rkdghk = Randomizer.rand(0, 3);
        cm.getClient().setKeyValue(questt, "1");    
        cm.gainItem(2437115, 0);
        cm.gainItem(2431156, 1);
        cm.gainItem(4310320, 5000);
        cm.gainItem(2049371, 2);
        cm.gainItem(2435797, 2);
        cm.gainItem(2435796, 1);
        cm.gainItem(2430218, 0);
        cm.getPlayer().AddStarDustCoin(200);
        Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 축하합니다. 더욱더 안전성 재미난 컨텐츠 보장 하겠습니다."));
        cm.sendOk("오늘의 보상이 지급되었습니다.");
        cm.dispose();
    }
}

function getData() {
    time = new Date();
    year = time.getFullYear();
    month = time.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    date2 = time.getDate() < 10 ? "0" + time.getDate() : time.getDate();
    date = year + "" + month + "" + date2;
    day = time.getDay();
    hour = time.getHours();
    minute = time.getMinutes();
}