function enter(pi) {
    pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SetIngameDirectionMode(true, true, true, false));
    pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.playSE("Sound/MiniGame.img/prize"));
    pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("monsterPark/clearF", 0x13));
    pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.enforceMSG("�������� Ŭ�����. �κ�� �̵�����.", 212, 2000));
    Packages.server.Timer.MapTimer.getInstance().schedule(function () {
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.playSE("Sound/MiniGame.img/Catch"));
    }, 1000)
    var stage = (pi.getPlayer().getMapId() - 993001000) / 10;
    pi.getPlayer().RegisterPlatformerRecord(stage);
    pi.getPlayer().warpdelay(993001000, 2);
    Packages.server.Timer.MapTimer.getInstance().schedule(function () {
        pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SetIngameDirectionMode(false, true, false, false));
    }, 2000)
}