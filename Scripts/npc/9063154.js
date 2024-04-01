importPackage(java.lang);
var enter = "\r\n";
var seld = -1;

보상 = [[4310248, 3000], [4001716, 1]];

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
        var msg = "#fs11#업데이트 지연 및 패치 오류로 인한 보상입니다. " + enter;
        msg += msg = "#r계정당 1회 지급됩니다.#k#l " + enter;
        msg += "#L1#아이템을 받는다" + enter;

        cm.sendSimple(msg);
    } else if (status == 1) {

        if (cm.getClient().getKeyValue("akdma123123") == null) {
            cm.getClient().setKeyValue("akdma12", "0");
        }

        if (cm.getClient().getKeyValue("akdma123123") == "1") {
            cm.sendOk("#fs11#이미 보상을 받으셨습니다.");
            cm.dispose();
            return;
        }

        if (sel == 1) {
            var msg = "#fs11#패치 지연 보상 및 이벤트 오류 보상이 지급 되었습니다." + enter;
            cm.getClient().setKeyValue("akdma123123", "1");
            for (i = 0; i <= 보상.length; i++) {
                cm.gainItem(보상[i][0], 보상[i][1]);
            }
            cm.sendNext(msg);
            cm.dispose();
        }
    }
}