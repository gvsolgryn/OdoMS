/*
 * ǻ��¶��� �ҽ� ��ũ��Ʈ �Դϴ�.
 * 
 * ��Ż��ġ : 
 * ��Ż���� : 
 * 
 * ���� : ��ũ��
 * 
 */
importPackage(java.lang);
importPackage(Packages.packet.creators);

function enter(pi) {
    var eim = pi.getPlayer().getEventInstance();
    if (eim == null) {
        pi.warp(262000000);
        return true;
    }
    if (eim.getProperty("CurrentStage").equals("4")) {
        pi.getPlayer().gainExp(30000, true, true, true);
        eim.unregisterPlayer(pi.getPlayer());
        pi.getPlayer().addInnerExp(100 * pi.getPlayer().getInnerLevel());
        pi.getPlayer().message(5, 100 * pi.getPlayer().getInnerLevel()+"�� ��ġ�� ������ϴ�.");
        pi.getPlayer().makeNewAswanShop();
        pi.warp(262000000);
        return true;
    } else {
        pi.getPlayer().send(UIPacket.showInfo("���� ���� ���͸� ��� ��ƾ� ���� ���������� �̵��� �� �ֽ��ϴ�."));
        pi.getPlayer().message(5, "���͸� ��� ������ �� ���� ��Ż�� �̵��� �ּ���.");
        return false;
    }
}