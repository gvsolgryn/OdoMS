


/*

    ���� KMS �� �ҽ��� ��ũ��Ʈ �Դϴ�.

    ��Ż�� �ִ� �� : ��׽ý�

    ��Ż ���� : ����������� ����


*/


function enter(pi) {
    if (pi.getPlayer().getLevel() < 30) {
        pi.getPlayer().dropMessage(5, "���� 30�̻� ���� �����մϴ�.");
        return false;
    }
    pi.getPlayer().setKeyValue(7860, "returnMap", pi.getPlayer().getMapId()+"");
    pi.warp(910001000, 0);
    return true;
}
