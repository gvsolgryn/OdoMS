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
    var startmap = Integer.parseInt(eim.getProperty("Global_StartMap"));
    var curstage = Integer.parseInt(eim.getProperty("CurrentStage"));
    var curmap = (startmap + ((curstage - 1) * 100));
    if (eim.getProperty(curmap+"Prepared") == null) {
        pi.prepareAswanMob(curmap, eim);
        eim.setProperty(curmap+"Prepared", "1");
    }
    if(curmap == pi.getPlayer().getMapId()) {
        pi.getPlayer().send(UIPacket.showInfo("���� ���� ���͸� ��� ��ƾ� ���� ���������� �̵��� �� �ֽ��ϴ�."));
        pi.getPlayer().message(5, "���͸� ��� ������ �� ���� ��Ż�� �̵��� �ּ���.");
        return false;
    } else {
        pi.warp(curmap);
        return true;
    }
}