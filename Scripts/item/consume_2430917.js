var status;

function start() {
    status = -1;
    action(1, 1, 0);
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
		cm.gainItem(1112748, 1);
		cm.gainItem(1032148, 1);
		cm.gainItem(1122200, 1);
		cm.gainItem(1132161, 1);
		cm.gainItem(1152099, 1);
		cm.gainItem(2430917, -1);
		cm.dispose();
	}
}
