importPackage(Packages.tools.packet);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    setting = [
        ["Normal_JinHillah", 99, 450010500, 255],
        ["Normal_JinHillah", 99, 450010500, 255],
        ["Extreme_JinHillah", 99, 450010500, 255] 
    ]
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        if (cm.getPlayer().getMapId() == 450010400) {
            cm.addBoss(setting[0][0]);
            em = cm.getEventManager(setting[0][0]);
            if (em != null) {
                cm.getEventManager(setting[0][0]).startInstance_Party(setting[0][2] + "", cm.getPlayer());
            }
        } else {
            cm.addBossPractice(setting[1][0]);
            em = cm.getEventManager(setting[1][0]);
            if (em != null) {
                cm.getEventManager(setting[1][0]).startInstance_Party(setting[1][2] + "", cm.getPlayer());
            }
        }
        cm.dispose();
    }

}

function statusplus(time) {
    cm.getPlayer().getMap().broadcastMessage(SLFCGPacket.InGameDirectionEvent("", 1, time));
}