var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        cm.sendYesNo("꿈의 퀘스트전당 으로 이동하시겠습니까?");
    } else if (status == 1) {
        cm.warp(100030301, 0);
	cm.dispose();
    }
}