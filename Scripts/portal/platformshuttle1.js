function enter(pi) {
    var lastshuttle = pi.getPlayer().getKeyValue(20190208, "lastshuttle");

    if (lastshuttle == 0 || lastshuttle == -1) {
        pi.getPlayer().setKeyValue(20190208, "lastshuttle", "1");
        pi.getPlayer().setKeyValue(20190208, "shuttlecount", (pi.getPlayer().getKeyValue(20190208, "shuttlecount") + 1) + "");
        var count = pi.getPlayer().getKeyValue(20190208, "shuttlecount");
        switch (count) {
            case 1:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "���� �����̴�! �ϳ�!", ""));
                break;
            case 2:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "��!", ""));
                break;
            case 3:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "��!", ""));
                break;
            case 4:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "��!", ""));
                break;
            case 5:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "�ټ�! ������ ������!", ""));
                break;
            case 6:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����!", ""));
                break;
            case 7:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "�ϰ�!", ""));
                break;
            case 8:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����!", ""));
                break;
            case 9:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "��ȩ!", ""));
                break;
            case 10:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "��! �� �̸��� ����Ʈ�� ��Ʈ!", ""));
                break;
            case 11:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "���ϳ�!", ""));
                break;
            case 12:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����!", ""));
                break;
            case 13:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����!", ""));
                break;
            case 14:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����!", ""));
                break;
            case 15:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "���ټ�! ���� �ټ��̴�!", ""));
                break;
            case 16:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "������!", ""));
                break;
            case 17:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "���ϰ�!", ""));
                break;
            case 18:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "������!", ""));
                break;
            case 19:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����ȩ!", ""));
                break;
            case 20:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����! ���� ���� ����?!", ""));
                break;
            case 21:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "�����ϳ�!", ""));
                break;
            case 22:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "������!", ""));
                break;
            case 23:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "������!", ""));
                break;
            case 24:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "������!", ""));
                break;
            case 25:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "�����ټ�! ���� �ټ� �� ���Ҵ�!", ""));
                break;
            case 26:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "��������!", ""));
                break;
            case 27:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "�����ϰ�!", ""));
                break;
            case 28:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "��������!", ""));
                break;
            case 29:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "������ȩ! ������ �� ��!", ""));
                break;
            case 29:
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.OnYellowDlg(9070203, 1000, "����! ����! �� �߾�! �����̾�!", ""));
                break;
        }
        if (count == 30) {
            pi.getPlayer().setKeyValue(20190208, "lastshuttle", "-1");
            pi.getPlayer().setKeyValue(20190208, "shuttlecount", "0");
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SendPacket(714, "01 01 01 00"));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.CharReLocationPacket(-510, 92));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.playSE("Sound/MiniGame.img/prize"));
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.environmentChange("monsterPark/clearF", 0x13));
            var schedule = Packages.server.Timer.MapTimer.getInstance().schedule(function () {
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.playSE("Sound/MiniGame.img/Catch"));
            }, 1000)
            pi.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.enforceMSG("�������� Ŭ�����. �κ�� �̵�����.", 212, 2000));
            pi.getPlayer().RegisterPlatformerRecord(18);
            pi.getPlayer().warpdelay(993001000, 2);
            var schedule = Packages.server.Timer.MapTimer.getInstance().schedule(function () {
                pi.getClient().getSession().writeAndFlush(Packages.tools.packet.SLFCGPacket.SendPacket(714, "00 01"));
            }, 2000)
        }
    }
}