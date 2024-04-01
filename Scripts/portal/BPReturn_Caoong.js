function enter(pi) {
    if (pi.getPlayer().getKeyValue(210406, "Return_BossMap") > 0) {
        pi.getPlayer().dropMessage(5, "���� �޴��� ���� �̵��Ͽ� ���� �ִ� ������ ���ư��ϴ�.");
        pi.warp(pi.getPlayer().getKeyValue(210406, "Return_BossMap"), 0);
        pi.getPlayer().removeKeyValue(210406);
    } else {
        pi.warp(120043000, 10);
    }
    return true;
}