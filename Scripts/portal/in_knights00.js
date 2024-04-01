/*
 * 
 */
importPackage(java.lang);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);

function enter(pi) {
if (pi.getQuestStatus(31124) < 2) {

if (pi.getQuestStatus(31124) == 1) {
    pi.getPlayer().send(UIPacket.showInfo("���� �����ִ�. �˷������� ���� ��������."));
    pi.forceCompleteQuest(31124);
} else {
    pi.getPlayer().send(UIPacket.showInfo("�ñ׳ʽ� ��� ���� ����Ʈ�� Ŭ���� ���ֽʽÿ�."));
}
} else if (pi.getQuestStatus(31124) == 2) {
    pi.playPortalSE();
    pi.warp(271030010);
}
    return true;
}