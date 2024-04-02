var outmap = 100000000

function start() {
	St = -1;
	action(1, 0, 0);
}

function action(M, T, S) {
	if(M != 1) {
		cm.dispose();
		return;
	}

	if(M == 1)
	    St++;

	if(St == 0) {
		var em = cm.getEventManager("WorldBoss");
		if (em == null) {
			cm.sendOk("오류가 발생하였습니다.");
			cm.getPlayer().changeChannelMap(1, outmap);
			cm.dispose();
			return;
		} else {
			cm.getPlayer().WorldbossDamage = 0;
			em.startInstance(cm.getPlayer());
			cm.dispose();
		}
	}
}