var status = 0;

function start() {
    status = -1;
    action(1, 1, 0);
}

function action(mode, type, selection) {
    if (mode <= 0) {
        cm.dispose();
        return;
    } else {
        if (mode == 1) {
            status++;
        }
    }
    if (status == 0) {
        cm.getPlayer().gainDonationPoint(100000);
        cm.sendOk("#fs11##b후원 포인트를 지급받으셨습니다.#k");
        cm.gainItem(2630692, -1);
        cm.dispose();
    }
}