function enter(pi) {
	pi.getPlayer().getEventInstance().setProperty("LudiPQ_Gate","0");
    pi.warpParty(pi.getMapId() + 100);
}