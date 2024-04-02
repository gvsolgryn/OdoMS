var enter = "\r\n";
var seld = -1;

var need = [
    {'itemid': 4034180, 'qty': 10},
    {'itemid': 4034181, 'qty': 10},
    {'itemid': 4034182, 'qty': 10},
    {'itemid': 4034183, 'qty': 10},
]
var tocoin = 4033825, toqty = 1;

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
        var msg = "#fs11##b" + cm.getPlayer().getName() + "#k님 환영합니다. [사냥] 이벤트의 담당엔피시를 맡고있는 무루피아 입니다.\r\n" + enter;

        for (i = 0; i < need.length; i++) {
            if (i != need.length - 1)
                msg += "#i" + need[i]['itemid'] + "##b#z" + need[i]['itemid'] + "##k " + need[i]['qty'] + "개#k" + enter;
            else
                msg += "#i" + need[i]['itemid'] + "##b#z" + need[i]['itemid'] + "##k " + need[i]['qty'] + "개#k\r\n\r\n위 재료들을 모아서 저에게 다시 찾아와주시면\r\n#b#z" + tocoin + "##k으로 교환 해드리겠습니다." + enter;
        }


        if (haveNeed(1))
            cm.sendNext(msg);
        else {
            msg += enter + enter + "현재는 재료가 많이 부족 하신거 같습니다..";
            cm.sendOk(msg);
            cm.dispose();
        }
    } else if (status == 1) {
        temp = [];
        for (i = 0; i < need.length; i++) {
            temp.push(Math.floor(cm.itemQuantity(need[i]['itemid']) / need[i]['qty']));
        }
        temp.sort();
        max = 300;
        cm.sendGetNumber("당신은 최대 #b" + max + "개를#k 교환할 수 있군요..\r\n몇 개를 교환하시겠습니까...?", 1, 1, max);
    } else if (status == 2) {
        if (!haveNeed(sel)) {
            cm.sendOk("당신이 소지한 아이템이 부족합니다.");
            cm.dispose();
            return;
        }
        for (i = 0; i < need.length; i++) {
            cm.gainItem(need[i]['itemid'], -(need[i]['qty'] * sel));
        }
        if (!cm.canHold(tocoin, (toqty * sel))) {
            cm.sendOk("인벤토리에 공간이 부족합니다.");
            cm.dispose();
            return;
        }
        cm.gainItem(tocoin, (toqty * sel));
        cm.sendOk("별빛의 결정을 지급 받으셨습니다..");
        //Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CField.getGameMessage(8, cm.getPlayer().getName() + "님이 영원한 환생의 불꽃 제작에 성공하였습니다."));
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
