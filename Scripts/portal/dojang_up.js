function enter(pi) {
	if ((pi.getPlayer().getSkillCustomValue0(92507) > 0 || pi.haveMonster()) && pi.getPlayer().getMap().getId() != 925070000) {
		pi.playerMessage("���� ���Ͱ� �����ֽ��ϴ�.");
	} else {
		if (pi.getPlayer().getMap().getId() == 925078000) {
			pi.warp(925040000, 1);
		} else {
			pi.warp(pi.getPlayer().getMap().getId() + 100, 1);
			pi.doJoWarpMap(pi.getPlayer().getMap().getId());
			pi.getPlayer().setSkillCustomInfo(92507, 1, 4000);
		}
		if (pi.getPlayer().getMapId() != 925070100) {
			if (pi.getPlayer().getInfoQuest(3887) != "") {
				pi.getPlayer().updateInfoQuest(3887, "point=" + (parseInt(pi.getPlayer().getInfoQuest(3887).split("=")[1]) + 10));
			} else {
				pi.getPlayer().updateInfoQuest(3887, "point=10");
			}
			pi.playerMessage("10����Ʈ�� ȹ���Ͽ����ϴ�.");
			if (pi.getPlayer().getKeyValue(100466, "Score") < pi.getPlayer().getKeyValue(3, "dojo")) {
				pi.playerMessage("���� �ְ� ����� �޼��Ͽ����ϴ�.");
			}
		}
		
		pi.getPlayer().getMap().resetFully();
	}
}