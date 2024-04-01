var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
        var count = 1;
    setting = [
        ["Teng", count, 813000000, 290]
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        talk = "#b이곳까지 찾아온이유가 무엇인가.. 텐구를 잡으려한다면 지금이라도 포기하게나,,#k\r\n\r\n"
        talk += "#L0#텐구 처치 (299레벨)#l\r\n"
        cm.sendSimpleS(talk, 2);
    } else if (status == 1) {
        st = selection;
        if (cm.getParty() == null) {
            cm.sendOk("1인 이상 파티를 맺어야만 입장할 수 있어.");
            cm.dispose();
            return;
        } else if (cm.getPlayerCount(setting[st][2]) >= 1) {
            cm.sendOk("이미 누군가가 텐구 처치에 도전하고 있어.\r\n다른채널을 이용 해 줘.");
            cm.dispose();
            return;
        } else if (!cm.isLeader()) {
            cm.sendOk("파티장만이 입장을 신청할 수 있어.");
            cm.dispose();
            return;
	} else if (!cm.allMembersHere()) {
	    cm.sendOk("모든 멤버가 같은 장소에 있어야 해.");
	    cm.dispose();
            return;
        }
        if (!cm.isBossAvailable(setting[st][0], setting[st][1])) {
            talk = "파티원 중 이미 입장한 사람이 있어.\r\n\r\n"
            for (i = 0; i < cm.BossNotAvailableChrList(setting[st][0], setting[st][1]).length; i++) {
                talk += "#b#e-" + cm.BossNotAvailableChrList(setting[st][0], setting[st][1])[i] + "\r\n"
            }
            talk += "#k#n님이 오늘 이용할 수 있는 횟수를 넘기셨습니다.";
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else if (!cm.isLevelAvailable(setting[st][3])) {
            talk = "파티원 중 텐구에 도전하기 위한 레벨이 부족한 사람이 있어."
            for (i = 0; i < cm.LevelNotAvailableChrList(setting[st][3]).length; i++) {
                talk += "#b#e-" + cm.LevelNotAvailableChrList(setting[st][3])[i] + "\r\n"
            }
            cm.sendOk(talk);
            cm.dispose();
            return;
        } else {
            cm.addBoss(setting[st][0]);
            em = cm.getEventManager(setting[st][0]);
            if (em != null) {
                cm.getEventManager(setting[st][0]).startInstance_Party(setting[st][2] + "", cm.getPlayer());
            }
            cm.dispose();
        }
    }
}