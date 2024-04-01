function enter(pi) {
	var eim = pi.getPlayer().getEventInstance();
	if (eim.getProperty("LudiPQ_Gate") == 1) {
	pi.getPlayer().getEventInstance().setProperty("LudiPQ_Eyes","0");
	pi.getPlayer().getEventInstance().setProperty("LudiPQ_Gate","0");
    pi.warpParty(922010400);
	} else {
}
}