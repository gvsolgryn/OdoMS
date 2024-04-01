var status = -1;

function start() {
    status = -1;
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
        if (cm.getMapId() >= 922010401 && cm.getMapId() <= 922010406) {
            if(cm.getClient().getChannelServer().getMapFactory().getMap(cm.getMapId()).getNumMonsters() > 0){
                cm.getPlayer().getMap().startMapEffect("몬스터의 기운이 느껴집니다. 몬스터를 찾아 퇴치하여 주세요.", 5120018, 5000);
            } else {
                cm.getPlayer().getMap().startMapEffect("몬스터의 기운이 느껴지지 않습니다. 다른 방으로 이동하여 주세요.", 5120018, 5000);
            }
        }
	cm.dispose();
    }
}

