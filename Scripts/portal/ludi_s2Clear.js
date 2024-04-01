function enter(pi) {
	var eim = pi.getPlayer().getEventInstance();
	if (eim.getProperty("LudiPQ_Gate") == 1) {
    var upbox = ["0","1","2"];
    for (x = 0; x < 10; x++) {
	pi.setKeyValueParty("box_0" + x + "",upbox[Math.floor(Math.random() * 3)] + "");
    }
    pi.warpParty(pi.getMapId() + 200);
	} else {
}
}