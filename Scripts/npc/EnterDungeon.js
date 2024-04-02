function start() {
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(false));
	cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(152));
	cm.dispose();
}

function mirrorD_321_0_() {
	//cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(1052224);
}

function mirrorD_321_1_() {
	//cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(2159442);
}

function mirrorD_321_2_() {
	//cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(3001930);
}

function mirrorD_321_3_() {
	//cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(1012000);
	//cm.openNpc(9075000);
}

function mirrorD_322_0_() {
	//cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.OnSetMirrorDungeonInfo(true));
	cm.dispose();
	cm.openNpc(2400003);
}

function action(a, b, c) {
	cm.dispose();
}