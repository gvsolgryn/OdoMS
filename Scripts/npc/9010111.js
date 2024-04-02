importPackage(Packages.client);
importPackage(Packages.constants);
importPackage(Packages.server.maps);
importPackage(Packages.tools.packet);
importPackage(Packages.server);
importPackage(java.lang);
importPackage(java.util);

var enter = "\r\n";
var seld = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}
function action(mode, type, sel) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		var msg = "#L1##b유니온 등급 관리#n #k\r\n\r\n";
		msg += "#L20##b유니온 기어등급 관리#n #k\r\n\r\n";
		msg += "#L2##b유니온 상점#n #k\r\n";

		cm.sendSimpleS(msg, 4);
	} else if (status == 1) {
		seld = sel;
		switch (sel) {
			case 1:
				cm.dispose();
				cm.openNpc(9010106);
				break;
			case 2:
	           		 cm.dispose();
            			 cm.openShop(9010107);
					break;
			case 50:
				cm.dispose();
				cm.openNpc(3001860);
				break;
			case 55:
				cm.dispose();
				cm.openNpc(1012005);
				break;
			case 15:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3003500, "2005");
				break;
			case 14:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3003500, "일일퀘스트");
				break;
			case 10:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001604, "BuffShop");
				break;
			case 11:
				cm.dispose();
				cm.openNpc(3001931);
				break;
			case 12:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001604, "뉴비지원");
				break;

			case 20:
				cm.dispose();
				cm.openNpcCustom(cm.getClient(), 3001604, "1002000");
				break;

			case 3:
				var msg = "#e#b[ 게임 시스템]#k#n" + enter;
				msg += "#L1#원카드" + enter;
				msg += "#L2#배틀 리버스" + enter;
				cm.sendSimple(msg);

				break;
			case 4:
				/*
								var msg = "#e#b[ 랭킹 시스템]#k#n"+enter;
								msg +="#L1#레벨 랭킹"+enter;
								msg +="#L2#메소 랭킹"+enter;
								msg +="#L3#데미지 측정랭킹"+enter;
								msg +="#L4#길드 랭킹"+enter;
								msg +="#L5#무릉도장 랭킹"+enter;
								cm.sendSimple(msg);
				*/
				cm.dispose();
				cm.openNpc(9076004);
				break;
			case 5:
				cm.dispose();
				cm.warp(200000301);
				break;
			case 6:
				var msg = "#e#b[ 커플 시스템]#k#n" + enter;

				msg += "#L1#하우스 웨딩" + enter + enter;
				msg += "#L2#커플링 시스템" + enter;
				//msg += "#L3#선택지3"+enter;
				//msg += "#L4#선택지4"+enter;
				//msg += "#L5#선택지5"+enter;
				cm.sendSimple(msg);
				break;
			case 7:
				var msg = "#e#b [ 점프 맵]#k#n" + enter;

				msg += "#L1#인내의 숲" + enter;
				msg += "#L2#끈기의 숲" + enter;
				msg += "#L3#고지를 향해서" + enter;
				msg += "#L4#펫 산책로 (헤네시스)" + enter;
				msg += "#L5#펫 산책로 (루디브리엄)" + enter;
				cm.sendSimple(msg);
				break;
			case 13:
				var msg = "#e#b [UI지원목록]#k#n" + enter;

				msg += "#L1#아무것도 안했는데!" + enter;
				msg += "#L2#PC방 퀵 슬롯" + enter;
				msg += "#L3#페어리 브로의 황금마차" + enter;
				msg += "#L4#아이템 메이커" + enter;
				//msg +="#L4#펫 산책로 (헤네시스)"+enter;
				//msg +="#L5#펫 산책로 (루디브리엄)"+enter;
				cm.sendSimple(msg);
				break;
			case 8:
				var msg = " #e#b [ 컨텐츠]#k#n" + enter;
				msg += "#L1# 무릉도장" + enter;
				msg += "#L2# 레벨 달성 보상받기 (준비중)" + enter;
				msg += "#L3# 환생 시스템" + enter;
				//msg += "#L4# 해적왕의 비보"+enter;
				//msg += "#L5# 기어 업그레이드"+enter;
				//msg += "#L6# 낚시터"+enter;
				cm.sendSimple(msg);
				break;
			case 51:
				var msg = " #e#b [ 레벨업 보상 계정당 1회 수령]#k#n" + enter;
				msg += "#L200# 200 레벨 달성 보상받기 " + enter;
				msg += "#L210# 210 레벨 달성 보상받기 " + enter
				msg += "#L220# 220 레벨 달성 보상받기 " + enter
				msg += "#L230# 230 레벨 달성 보상받기 " + enter
				msg += "#L240# 240 레벨 달성 보상받기 " + enter
				msg += "#L250# 250 레벨 달성 보상받기 " + enter
				msg += "#L260# 260 레벨 달성 보상받기 " + enter
				msg += "#L270# 270 레벨 달성 보상받기 " + enter
				msg += "#L280# 280 레벨 달성 보상받기 " + enter
				msg += "#L290# 290 레벨 달성 보상받기 " + enter
				msg += "#L300# 300 레벨 달성 보상받기 " + enter
				cm.sendSimple(msg);
				break;
			case 24:
				cm.dispose();
				cm.warp(925020001);
				break;
			case 30:
				cm.dispose();
				cm.openNpc(9010111);
				break;	
		}
	} else if (status == 2) {
		switch (seld) {
			case 51:
				if (cm.getPlayer().getLevel() < sel) {

					cm.sendOk("보상 수령을 위한 레벨이 부족합니다.");
					cm.dispose();
					break;
				}
				switch (sel) { // 레벨업보상
					case 200: // 200레벨 달성시 아래아이템 지급(보관함으로 지급되며 계정당 1회지급됨)
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2048717, 10); // 영환불*/
							cm.getPlayer().gainCabinetItem(2631527, 10); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 3); // 3배
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 210: // 210레벨
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 20); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 6); // 3배
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 220:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 30); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 9); // 3배
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 230:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 40); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 12); // 3배
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 240:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 50); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 15); // 3배
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 250:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 60); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 18); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 6); // 10억
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 260:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 80); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 23); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 7); // 10억
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 270:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 100); // 경코젬
							cm.getPlayer().gainCabinetItem(2450163, 28); // 3배
							cm.getPlayer().gainCabinetItem(4001716, 8); // 10억
							cm.getPlayer().gainCabinetItem(5121060, 20); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 280:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 120); // 경코젬
							cm.getPlayer().gainCabinetItem(4001716, 9); // 10억
							cm.getPlayer().gainCabinetItem(2049376, 2); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 20); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 290:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 140); // 경코젬
							cm.getPlayer().gainCabinetItem(2049376, 3); // 17성
							cm.getPlayer().gainCabinetItem(5121060, 20); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
					case 300:
						if (cm.getPlayer().getClient().getKeyValue("level" + this.level) == null) {
							cm.getPlayer().gainCabinetItem(2631527, 160); // 경코젬
							cm.getPlayer().gainCabinetItem(5121060, 20); // 경뿌
							cm.getPlayer().getClient().setKeyValue("level" + this.level, "true");
							cm.sendOk("보상을 정상적으로 수령하셨습니다.");
							cm.dispose();

						} else {
							cm.sendOk("이미 보상을 수령하셨습니다.");
							cm.dispose();
						}
						break;
				}
				break;
			case 1:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(9010106);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(9010107);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(3003162);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(3003252);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(3003480);
						break;
					case 6:
						cm.dispose();
						cm.openNpc(3003756);
						break;
				}
				break;
			case 13:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(1207)); // 152
						break;
					case 2:
						cm.dispose();
						cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(1126)); // 152
						break;
					case 3:

						cm.openGold(cm.getClient());
						cm.dispose();
						break;
					case 4:
						cm.dispose();
						cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(26)); // 22
						break;
					case 5:
						cm.dispose();
						cm.openNpc(3003480);
						break;
					case 6:
						cm.dispose();
						cm.openNpc(3003756);
						break;
					case 9:
						cm.dispose();
						cm.openNpc(3003151);
						break;
					case 8:
						cm.dispose();
						cm.openNpc(3003381);
						break;
					case 10:
						cm.dispose();
						cm.warp(450004000, 0);
						break;
				}
				break;
			case 2:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2155000);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(3003104);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(3003162);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(3003252);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(3003480);
						break;
					case 6:
						cm.dispose();
						cm.openNpc(3003756);
						break;
					case 9:
						cm.dispose();
						cm.openNpc(3003151);
						break;
					case 8:
						cm.dispose();
						cm.openNpc(3003381);
						break;
					case 10:
						cm.dispose();
						cm.warp(450004000, 0);
						break;
				}
				break;
			case 3:
				switch (sel) {
					case 1:
						cm.dispose();
						if (cm.isLeader()) {
							if (cm.getPlayer().getParty().getMembers().size() >= 3 && cm.getPlayer().getParty().getMembers().size() <= 4) {
								for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
									mem = cm.getPlayer().getParty().getMembers().get(i);
									if (mem != null) {
										relmem = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem.getName());
										if (relmem != null && relmem.getKeyValue(100592, "today") >= 20000) {
											cm.sendOk(mem.getName() + "님이 하루 제한치를 넘어섰습니다.");
											return;
										}
									}
								}
								Packages.server.games.OneCardGame.addQueueParty(cm.getPlayer());
							} else {
								cm.sendOk("3, 4인 파티의 파티장만이 시도하실 수 있습니다.");
							}
						} else {
							cm.sendOk("3, 4인 파티의 파티장만이 시도하실 수 있습니다.");
						}
						break;
					case 2:
						cm.dispose();
						if (cm.isLeader()) {
							if (cm.getPlayer().getParty().getMembers().size() == 2) {
								for (i = 0; i < cm.getPlayer().getParty().getMembers().size(); ++i) {
									mem = cm.getPlayer().getParty().getMembers().get(i);
									if (mem != null) {
										relmem = cm.getClient().getChannelServer().getPlayerStorage().getCharacterByName(mem.getName());
										if (relmem != null && relmem.getKeyValue(100592, "today") >= 20000) {
											cm.sendOk(mem.getName() + "님이 하루 제한치를 넘어섰습니다.");
											return;
										}
									}
								}
								Packages.server.games.BattleReverse.addQueue(cm.getPlayer(), true);
							} else {
								cm.sendOk("2인 파티의 파티장만이 시도하실 수 있습니다.");
							}
						} else {
							cm.sendOk("2파티의 파티장만이 시도하실 수 있습니다.");
						}
						break;
				}
				break;
			case 4:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2008);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(2008);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(2008);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
						break;
				}
				break;
			case 5:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.openNpc(2010007);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(2010009);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(1540107);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(2012038);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
						break;
				}
				break;
			case 6:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(680000900, 1);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(1031001);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(2008);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(2008);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(2008);
						break;
				}
				break;
			case 7:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(910130100, 0);
						break;
					case 2:
						cm.dispose();
						cm.warp(910530100, 0);
						break;
					case 3:
						cm.dispose();
						cm.warp(109040001, 0);
						break;
					case 4:
						cm.dispose();
						cm.warp(100000202, 0);
						break;
					case 5:
						cm.dispose();
						cm.warp(220000006, 0);
						break;
				}
				break;
			case 8:
				switch (sel) {
					case 1:
						cm.dispose();
						cm.warp(993174800, 1);
						break;
					case 2:
						cm.dispose();
						cm.openNpc(1540101);
						break;
					case 3:
						cm.dispose();
						cm.openNpc(3001941);
						break;
					case 4:
						cm.dispose();
						cm.openNpc(9000224);
						break;
					case 5:
						cm.dispose();
						cm.openNpc(9001153);
						break;
					case 6:
						cm.dispose();
						cm.warp(993174800);
						break;
				}
				break;
		}
	}
}