var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    name = ["Easy_VonLeon", "Normal_VonLeon", "Hard_VonLeon"];
    mobid = [8840013, 8840010, 8840018]
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

        if (!cm.isLeader()) {
            cm.sendOk("파티장만 말 걸 수 있어요");
            cm.dispose();
            return;
        }
        cm.sendAcceptDecline("나를 물리치러 온 용사들인가... 검은 마법사를 적대하는 자들인가... 어느 쪽이건 상관 없겠지. 서로의 목적이 명확하다면 더 이야기 할 필요는 없을 테니...\r\n덤벼라. 어리석은 자들아...");
        cm.playSound(false, "Sound/Voice.img/vonleon/0");
    } else if (status == 1) {
        var check = cm.getPlayer().getMapId() == 211070104 ? 2 : cm.getPlayer().getMapId() == 211070100 ? 0 : 1;
        var em = cm.getEventManager(name[check]);
        var eim = cm.getPlayer().getEventInstance();
        if (em == null || eim == null) {
            cm.getPlayer().dropMessage(5, "비정상적인 값이 발견되어 보스 인스턴스가 종료됩니다.");
            cm.warp(100000000);
            cm.dispose();
        }
        mob = em.getMonster(mobid[check]);
        eim.getMapInstance(0).spawnMonsterOnGroundBelow(mob, new java.awt.Point(0, -181));
        eim.registerMonster(mob);
        eim.getMapInstance(0).killMonster(mob, cm.getPlayer(), false, false, 1);
        cm.getPlayer().getMap().resetNPCs();
        cm.dispose();
        return;
    }
}