var status = -1;
var name = "";

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        NullKeyValue();
        var chat = "#fs11#";
        chat += "#e티어 승급 시스템#n입니다.\r\n";
        chat += "#b티어 승급#k을 통해 #b티어전용혜택#k과 #b자신의숙련도#k를 증명하세요.\r\n\r\n";
        if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 9) {
            chat += "#r#L3#현재 티어가 최대 등급이므로 승급이 불가합니다.#k\r\n";
        } else {
            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 3) {
                chat += "#L1##d" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "으로 승급을 한다.\r\n";
            } else {
                chat += "#L1##d" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 한다.\r\n";
            }
        }
        chat += "#L2##d설명을 듣는다.\r\n";
        cm.sendSimple(chat);

    } else if (status == 1) {
        if (selection == 1) {
            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 0) { // 브론즈로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n\r\n";
                chat += "#i4001716# #b#z4001716##k #r1개#k\r\n";
		chat += "#i4310320# #b#z4310320##k #r1000개#k \r\n\r\n";               
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L1##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 1) { // 실버로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n\r\n";
                chat += "#i1142807# #b#z1142807##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r3개#k\r\n";
                chat += "#i4310330# #b#z4310330##k #r1개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r500개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L2##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 2) { // 골드로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n\r\n";
                chat += "#i1143115# #b#z1143115##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r5개#k\r\n";
                chat += "#i4310330# #b#z4310330##k #r2개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r1000개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L3##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 3) { // 플레티넘으로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "으로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n\r\n";
                chat += "#i1142913# #b#z1142913##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r7개#k\r\n";
                chat += "#i4310330# #b#z4310330##k #r3개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r2000개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L4##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 4) { // 다이아몬드로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n";
                chat += "#i1142005# #b#z1142005##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r10개#k\r\n";
                chat += "#i4310330# #b#z4310330##k #r4개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r3000개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L5##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 5) { // 마스터로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n";
                chat += "#i1142914# #b#z1142914##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r15개#k\r\n";
                chat += "#i4310330# #b#z4310330##k #r5개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r3000개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L6##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 6) { // 그랜드 마스터로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n";
                chat += "#i1142701# #b#z1142701##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r20개#k\r\n";
                chat += "#i4310330# #b#z4310330##k #r7개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r4000개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L7##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 7) { // 챌린저로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n";
                chat += "#i1142681# #b#z1142681##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r20개#k\r\n";
                chat += "#i4310063# #b#z4310063##k #r3개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r5000개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L8##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }
            
            if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == 8) { // 스카이로 승급
                var chat = "" + getTearRank((cm.getPlayer().getKeyValue(0, "Tear_Upgrade") + 1)) + "로 승급을 하기 위해선 아래와 같은 재료가 필요합니다.\r\n\r\n";
                chat += "#i1142836# #b#z1142836##k #r1개#k\r\n";
		chat += "#i4001716# #b#z4001716##k #r20개#k\r\n";
                chat += "#i4310063# #b#z4310063##k #r5개#k\r\n";
                chat += "#i4310320# #b#z4310320##k #r9999개#k \r\n\r\n";
                chat += "승급에 성공 할 확률은 #r#e100%#k#n이며,승급의 성공 유무와 상관없이 #r재료#k만 소모됩니다.\r\n";
                chat += "#L9##r#e정말 승급을 하시겠습니까?#k#n";
                cm.sendSimple(chat);
            }

        } else if (selection == 2) {
            cm.sendOk("티어는 총 10단계로 나뉘어져있어.\r\n#b언랭크, 브론즈, 실버, 골드, 플래티넘, 다이아, 마스터, 그랜드마스터, 챌린저, 프로페셔널#k 순이야.\r\n티어를 올릴수록 좋은 옵션을 가진 티어 칭호를 만들 수 있고, 티어 사냥터에 입장할 수 있어.\r\n열심히 재료를 모아서 티어 승급을 해봐 ㅋ");
            cm.dispose();

        } else if (selection == 3) {
            cm.dispose();
            return;
        }

    } else if (status == 2) {
        if (selection == 1) {
            if (cm.haveItem(4310320, 1000) && cm.haveItem(4001716, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310320, -1000);           
		    cm.gainItem(4001716, -1);           
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "1");
                    cm.sendOk("브론즈 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 브론즈 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -5);
                    cm.sendOk("브론즈 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족합니다.");
                cm.dispose();
                return;
            }
        }

        if (selection == 2) {
            if (cm.haveItem(4310320, 500) && cm.haveItem(4001716, 3)&& cm.haveItem(4310330, 1) && cm.haveItem(1142807, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310330, -1);
                    cm.gainItem(4001716, -3);
		    cm.gainItem(4310320, -500);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "2");
                    cm.sendOk("실버 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 브론즈 실버로 승급을 성공하였습니다!"));
                    cm.dispose();
                    return;
                } else {
                    cm.gainItem(4001716, -10);
                    cm.sendOk("실버 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }

        if (selection == 3) {
            if (cm.haveItem(4310320, 1000) && cm.haveItem(4001716, 5)&& cm.haveItem(4310330, 2) && cm.haveItem(1143115, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310330, -1);
                    cm.gainItem(4001716, -5);
		    cm.gainItem(4310320, -1000);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "3");
                    cm.sendOk("골드 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 골드 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -15);
                    cm.sendOk("골드 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }

        if (selection == 4) {
            if (cm.haveItem(4310320, 2000) && cm.haveItem(4001716, 7)&& cm.haveItem(4310330, 3) && cm.haveItem(1142913, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310330, -3);
                    cm.gainItem(4001716, -7);
		    cm.gainItem(4310320, -2000);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "4");
                    cm.sendOk("플래티넘 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 플래티넘 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -20);
                    cm.sendOk("플래티넘 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }

        if (selection == 5) {
            if (cm.haveItem(4310320, 3000) && cm.haveItem(4001716, 10)&& cm.haveItem(4310330, 4) && cm.haveItem(1142005, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310330, -4);
                    cm.gainItem(4001716, -10);
		    cm.gainItem(4310320, -3000);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "5");
                    cm.sendOk("다이아몬드 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 다이아몬드 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -30);
                    cm.sendOk("다이아몬드 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }

        if (selection == 6) {
            if (cm.haveItem(4310320, 3000) && cm.haveItem(4001716, 15)&& cm.haveItem(4310330, 5) && cm.haveItem(1142914, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310330, -5);
                    cm.gainItem(4001716, -15);
		    cm.gainItem(4310320, -3000);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "6");
                    cm.sendOk("마스터 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 마스터 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -50);
                    cm.sendOk("마스터 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }

        if (selection == 7) {
            if (cm.haveItem(4310320, 4000) && cm.haveItem(4001716, 20)&& cm.haveItem(4310330, 7) && cm.haveItem(1142701, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310330, -7);
                    cm.gainItem(4001716, -20);
		    cm.gainItem(4310320, -4000);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "7");
                    cm.sendOk("그랜드마스터 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 그랜드마스터 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -100);
                    cm.sendOk("그랜드마스터 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }

        if (selection == 8) {
            if (cm.haveItem(4310320, 5000) && cm.haveItem(4001716, 20)&& cm.haveItem(4310063, 3) && cm.haveItem(1142681, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310063, -3);
                    cm.gainItem(4001716, -20);
		    cm.gainItem(4310320, -5000);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "8");
                    cm.sendOk("챌린저 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 챌린저 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -200);
                    cm.sendOk("챌린저 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }
        
        if (selection == 9) {
            if (cm.haveItem(4310320, 9999) && cm.haveItem(4001716, 20)&& cm.haveItem(4310063, 5) && cm.haveItem(1142836, 1)) {
                if (Packages.server.Randomizer.rand(1, 100) <= 100) {
                    cm.gainItem(4310063, -5);
                    cm.gainItem(4001716, -20);
		    cm.gainItem(4310320, -5000);
                    cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "9");
                    cm.sendOk("프로페셔널 티어로 승급을 정상적으로 완료했습니다.");
                    Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 프로페셔널 티어로 승급을 성공하였습니다!"));
                    cm.dispose();
                } else {
                    cm.gainItem(4001716, -2000);
                    cm.sendOk("프로페셔널 티어로 승급을 실패하였습니다.");
                    cm.dispose();
                    return;
                }
            } else {
                cm.sendOk("재료가 부족하여 승급이 취소되었습니다.");
                cm.dispose();
                return;
            }
        }
    }
}

function NullKeyValue() {
    if (cm.getPlayer().getKeyValue(0, "Tear_Upgrade") == -1) {
        cm.getPlayer().setKeyValue(0, "Tear_Upgrade", "0");
    }
}

function getTearRank(level) {
    switch (level) {
        case 9:
            name = "프로페셔널";
            break;
        case 8:
            name = "챌린저";
            break;
        case 7:
            name = "그랜드 마스터";
            break;
        case 6:
            name = "마스터";
            break;
        case 5:
            name = "다이아몬드";
            break;
        case 4:
            name = "플래티넘";
            break;
        case 3:
            name = "골드";
            break;
        case 2:
            name = "실버";
            break;
        case 1:
            name = "브론즈";
            break;
        default:
            name = "언랭크";
            break;
    }
    return name;
}
        