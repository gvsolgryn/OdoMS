var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
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



    if (status == 0) {
        cm.sendYesNo("마을로 이동하시겠어요? #b예#k를 누르시면 마을로 이동됩니다.");
    } else if (status == 1) {
        cm.dispose();
        cm.warp(993192100);
    }
}