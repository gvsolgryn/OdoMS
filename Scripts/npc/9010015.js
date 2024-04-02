var status = -1;
var enter = "\r\n";
var list = [4319994, 4319999, 4319997];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, sel) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    var text = "#fs11#";

    if (status == 0) {
        text += "어떤 아이템으로 강화하겠냥?" + enter + enter;
        text += "#L1##i4319999# #z4319999# (방어구, 악세)" + enter
        text += "#L2##i4319997# #z4319997# (엠블렘, 훈장, 안드로이드)" + enter
        text += "#L3# #i5830001# [HEINZ] 후원 포인트 (무기, 보조무기)" + enter;
        text += "#L4##i4319994# #z4319994#" + enter
        text += "#L5##i2049372# [25성] 스타포스강화" + enter
        cm.sendOk(text);
    } else if (status == 1) {
        switch (sel) {
            case 1:
                cm.dispose();
                cm.openNpc(2020008);
                break;
            case 2:
                cm.dispose();
                cm.openNpc(2020009);
                break;
                case 3:
                    cm.dispose();
                    cm.openNpc(2020013);
                    break;
                    case 4:
                        cm.dispose();
                        cm.openNpc(9000213);
                        break;
                        case 5:
                            cm.dispose();
                            cm.openNpc(3003112);
                            break;
        }
    }
}
