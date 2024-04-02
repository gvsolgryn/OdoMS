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
		// for(var i = 1; i<11; i++){
		// 	if(Packages.handling.channel.ChannelServer.getInstance(i).getMapFactory().getMap(350111399).getNumMonsters() > 0){
		// 		msg2(i+" 채널 몬스터 소환 유무 : "+ i);
		// 	}
		// }
		var em = cm.getEventManager("WorldBoss");
		if (em == null) {
			cm.sendOk("오류가 발생하였습니다.");
			//cm.getPlayer().changeChannelMap(1, outmap);
			cm.dispose();
			return;
		} else {
			cm.getPlayer().WorldbossDamage = 0;
			em.startInstance(cm.getPlayer());
			cm.dispose();
		}
        cm.dispose();
	}
}

function msg2(text){
	cm.getPlayer().dropMessage(5, text);
}
