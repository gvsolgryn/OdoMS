function enter(pi) {
	if (pi.getClient().getChannelServer().getMapFactory().getMap(pi.getPlayer().getMapId()).getNumMonsters() > 0) {
		pi.getPlayer().dropMessage(5, "먼저 필드 내의 모든 몬스터를 제거하세요.");
		pi.getClient().send(Packages.tools.packet.CField.UIPacket.detailShowInfo("먼저 필드 내의 모든 몬스터를 제거하세요.", 3, 20, 4))
	} else {
		pi.warp(pi.getPlayer().getMapId() + 100, 0);
		pi.resetMap(pi.getPlayer().getMapId());
	}
}