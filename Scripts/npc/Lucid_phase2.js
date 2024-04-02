importPackage(Packages.tools.packet);
var status = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        cm.getPlayer().getClient().send(SLFCGPacket.MakeBlind(1, 0x00ff, 0x00f0, 0x00f0, 0x00f0, 0, 0));
        cm.getPlayer().getClient().send(SLFCGPacket.InGameDirectionEvent("Map002/Effect3.img/BossLucid/Lucid4", 0x02, 0, 0x59, 0x24, 0x1, 0x1, 0, 0x1, 0, 0, 0, 0));
        cm.sendNextS("#face6#어머, 이를 어째? 꿈이 무너지나봐요~!", 37, 0, 3003250);
        statusplus(4000);
    } else if (status == 1) {
        cm.getPlayer().getClient().send(SLFCGPacket.MakeBlind(0, 0, 0, 0, 0, 1500, 0));
        cm.getPlayer().getClient().send(SLFCGPacket.playSE("Sound/SoundEff.img/ArcaneRiver/phase2"));
        cm.getPlayer().getClient().send(SLFCGPacket.InGameDirectionEvent("Map/Effect3.img/BossLucid/Lucid2", 0x02, 0, 0x59, 0x24, 0xA, 0x1, 0, 0x1, 0, 0, 0, 0, 0));
        cm.getPlayer().getClient().send(SLFCGPacket.InGameDirectionEvent("Map/Effect3.img/BossLucid/Lucid3", 0x02, 0, -140, 0x24, 0xB, 0x1, 0, 0x1, 0, 0, 0, 0, 0));
        cm.getPlayer().getClient().send(SLFCGPacket.InGameDirectionEvent("Map/Effect3.img/BossLucid/Lucid4", 0x02, 0, 0x59, 0x24, 0x1, 0x1, 0, 0x1, 0, 0, 0, 0, 0));
        Packages.server.Timer.MapTimer.getInstance().schedule(function () {
            cm.getPlayer().getClient().send(SLFCGPacket.MakeBlind(1, 0x00ff, 0x00f0, 0x00f0, 0x00f0, 1300, 0));
            cm.dispose();
        }, 3000);
    }
}

function statusplus(time) {
    cm.getPlayer().getClient().getSession().writeAndFlush(SLFCGPacket.InGameDirectionEvent("", 1, time));
}