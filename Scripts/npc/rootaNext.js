importPackage(Packages.tools.packet);

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        cm.dispose();
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getParty().getLeader().getId() == cm.getPlayer().getId()) {
            if (cm.getMap().getNumMonsters() > 0) {
                cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.getTopMsg("필드 내에 남아있는 적이 있습니다."));
                cm.dispose();
            } else {
                cm.sendYesNoS("그럼 이동해볼까?", 2);
            }
        } else {
            cm.getPlayer().dropMessage(5, "파티장만 입장을 신청할 수 있습니다.");
            cm.dispose();
            return;
        }
    } else if (status == 1) {
        var eim = cm.getPlayer().getEventInstance();
        if (eim != null) {
            var map = eim.getMapInstance(1).getId();
            eim.setProperty("stage", "1");
            cm.warpParty(map);
            if ((cm.getPlayer().getMapId() >= 105200310 && cm.getPlayer().getMapId() <= 105200319) || (cm.getPlayer().getMapId() >= 105200710 && cm.getPlayer().getMapId() <= 105200719))
                cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("잠든 블러디 퀸에게 말을 걸어보자.", 5120025, true));
            if ((cm.getPlayer().getMapId() >= 105200110 && cm.getPlayer().getMapId() <= 105200119) || (cm.getPlayer().getMapId() >= 105200510 && cm.getPlayer().getMapId() <= 105200519))
                cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("차원의 틈에서 반반을 소환하자.", 5120025, true));
            if ((cm.getPlayer().getMapId() >= 105200210 && cm.getPlayer().getMapId() <= 105200219) || (cm.getPlayer().getMapId() >= 105200610 && cm.getPlayer().getMapId() <= 105200616)) {
                cm.getPlayer().getClient().send(CField.environmentChange("rootabyss/firework", 19));
                cm.getPlayer().getMap().broadcastMessage(CField.startMapEffect("피에르의 티파티에 온 것을 진심으로 환영한다네!", 5120098, true));
            }
            cm.dispose();
        }
        cm.dispose();
    }
}