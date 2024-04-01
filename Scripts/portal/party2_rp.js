importPackage(Packages.packet.creators);

function enter(pi) {
    if (pi.getPortal().getName().startsWith("pt0" + pi.getPlayer().getKeyValue("box_00"))) {
	pi.currentPortal(2);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an0" + pi.getPlayer().getKeyValue("box_00"),2));
    } else if (pi.getPortal().getName().startsWith("pt1" + pi.getPlayer().getKeyValue("box_01"))) {
	pi.currentPortal(3);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an1" + pi.getPlayer().getKeyValue("box_01"),2));
    } else if (pi.getPortal().getName().startsWith("pt2" + pi.getPlayer().getKeyValue("box_02"))) {
	pi.currentPortal(4);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an2" + pi.getPlayer().getKeyValue("box_02"),2));
    } else if (pi.getPortal().getName().startsWith("pt3" + pi.getPlayer().getKeyValue("box_03"))) {
	pi.currentPortal(5);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an3" + pi.getPlayer().getKeyValue("box_03"),2));
    } else if (pi.getPortal().getName().startsWith("pt4" + pi.getPlayer().getKeyValue("box_04"))) {
	pi.currentPortal(6);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an4" + pi.getPlayer().getKeyValue("box_04"),2));
    } else if (pi.getPortal().getName().startsWith("pt5" + pi.getPlayer().getKeyValue("box_05"))) {
	pi.currentPortal(7);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an5" + pi.getPlayer().getKeyValue("box_05"),2));
    } else if (pi.getPortal().getName().startsWith("pt6" + pi.getPlayer().getKeyValue("box_06"))) {
	pi.currentPortal(8);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an6" + pi.getPlayer().getKeyValue("box_06"),2));
    } else if (pi.getPortal().getName().startsWith("pt7" + pi.getPlayer().getKeyValue("box_07"))) {
	pi.currentPortal(9);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an7" + pi.getPlayer().getKeyValue("box_07"),2));
    } else if (pi.getPortal().getName().startsWith("pt8" + pi.getPlayer().getKeyValue("box_08"))) {
	pi.currentPortal(10);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an8" + pi.getPlayer().getKeyValue("box_08"),2));
    } else if (pi.getPortal().getName().startsWith("pt9" + pi.getPlayer().getKeyValue("box_09"))) {
	pi.currentPortal(11);
	pi.getMap().broadcastMessage(MainPacketCreator.environmentChange("an9" + pi.getPlayer().getKeyValue("box_09"),2));
    } else {
	pi.currentPortal(13);
    }
}
