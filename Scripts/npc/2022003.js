var status = -1;

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
        var msg = "교환하실 아이템을 선택해주세요.\r\n";
        msg += "#fs11#레시피와 아이템의 정보는 선택하면 나옵니다.#fs12##b\r\n";
        msg += "#L1##b#i1142807# Allstat.300, PAD&MAD.250  1개\r\n";
        msg += "#L2##b#i1143115# Allstat.500, PAD&MAD.300  1개\r\n";
        msg += "#L3##b#i1142913# Allstat.700, PAD&MAD.500  1개\r\n";
        msg += "#L4##b#i1142005# Allstat.1000, PAD&MAD.750  1개\r\n";
        msg += "#L5##b#i1142914# Allstat.1500, PAD&MAD.1000  1개\r\n";
        msg += "#L6##b#i1142701# Allstat.2000, PAD&MAD.1500  1개\r\n";
        msg += "#L7##b#i1142681# Allstat.2500, PAD&MAD.2000  1개\r\n";
        msg += "#L8##b#i1142836# Allstat.3500, PAD&MAD.2700  1개\r\n";
	msg += "#L9##b#i1143199# Allstat.5000, PAD&MAD.4000  1개\r\n";
        cm.sendSimple(msg);

    } else if (status == 1) {
        if (sel == 1) {
            var chat = "#fs11#";
            chat += "브론즈 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 브론즈 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L1##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 2) {
            var chat = "#fs11#";
            chat += "실버 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 실버 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L2##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 3) {
            var chat = "#fs11#";
            chat += "골드 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 골드 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L3##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 4) {
            var chat = "#fs11#";
            chat += "플래티넘 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 플래티넘 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L4##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 5) {
            var chat = "#fs11#";
            chat += "다이아몬드 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 다이아몬드 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L5##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 6) {
            var chat = "#fs11#";
            chat += "마스터 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 마스터 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L6##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 7) {
            var chat = "#fs11#";
            chat += "그랜드 마스터 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 그랜드 마스터 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L7##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 8) {
            var chat = "#fs11#";
            chat += "챌린저 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 챌린저 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L8##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        } else if (sel == 9) {
            var chat = "#fs11#";
            chat += "프로페셔널 티어 칭호를 제작하려면 아래의 아이템이 필요합니다.\r\n\r\n";
            chat += "#d * 프로페셔널 티어 이상만 교환이 가능합니다.\r\n\r\n";
            
            chat += "#i4036313# #b#z4036313# 10 억 메소\r\n\r\n";
            chat += "#L9##r정말 아이템을 제작하시겠습니까?";
            cm.sendSimple(chat);

        }

    } else if (status == 2) {
        if (sel == 1) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 1) {
                cm.gainItem(4310261, -0);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142807);
                vitem.setStr(300);
                vitem.setDex(300);
                vitem.setInt(300);
                vitem.setLuk(300);
                vitem.setWatk(250);
                vitem.setMatk(250);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Bronze] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 2) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 2) {
                cm.gainItem(4310261, -0);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1143115);
                vitem.setStr(500);
                vitem.setDex(500);
                vitem.setInt(500);
                vitem.setLuk(500);
                vitem.setWatk(300);
                vitem.setMatk(300);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Silver] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 3) {
            if (cm.haveItem(4310261, 0) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 3) {
                cm.gainItem(4310261, -0);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142913);
                vitem.setStr(700);
                vitem.setDex(700);
                vitem.setInt(700);
                vitem.setLuk(700);
                vitem.setWatk(500);
                vitem.setMatk(500);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Gold] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 4) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 4) {
                cm.gainItem(4310261, -00);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142005);
                vitem.setStr(1000);
                vitem.setDex(1000);
                vitem.setInt(1000);
                vitem.setLuk(1000);
                vitem.setWatk(750);
                vitem.setMatk(750);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Platinum] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 5) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 5) {
                cm.gainItem(4310261, -00);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142914);
                vitem.setStr(1500);
                vitem.setDex(1500);
                vitem.setInt(1500);
                vitem.setLuk(1500);
                vitem.setWatk(1000);
                vitem.setMatk(1000);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Diamond] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 6) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 6) {
                cm.gainItem(4310261, -00);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142701);
                vitem.setStr(2000);
                vitem.setDex(2000);
                vitem.setInt(2000);
                vitem.setLuk(2000);
                vitem.setWatk(1500);
                vitem.setMatk(1500);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Master] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 7) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 7) {
                cm.gainItem(4310261, -00);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142681);
                vitem.setStr(2500);
                vitem.setDex(2500);
                vitem.setInt(2500);
                vitem.setLuk(2500);
                vitem.setWatk(2000);
                vitem.setMatk(2000);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Grand Master] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 8) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 8) {
                cm.gainItem(4310261, -00);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1142836);
                vitem.setStr(3500);
                vitem.setDex(3500);
                vitem.setInt(3500);
                vitem.setLuk(3500);
                vitem.setWatk(2700);
                vitem.setMatk(2700);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Challenger] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 9) {
            if (cm.haveItem(4310261, 00) && cm.getPlayer().getMeso() >= 1000000000 && cm.getPlayer().getKeyValue(0, "Tear_Upgrade") >= 9) {
                cm.gainItem(4310261, -00);
                cm.gainMeso(-1000000000);
                vitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(1143199);
                vitem.setStr(5000);
                vitem.setDex(5000);
                vitem.setInt(5000);
                vitem.setLuk(5000);
                vitem.setWatk(4000);
                vitem.setMatk(4000);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), vitem, false);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [Professional] [등급] 아이템을 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }

        } else if (sel == 10) {
            if (cm.haveItem(4310269, 1)) {
                cm.gainItem(4310269, -1);
                cm.gainItem(4310261, 9999);
                Packages.handling.world.World.Broadcast.broadcastMessage(Packages.tools.packet.CWvsContext.serverNotice(11, "", cm.getPlayer().getName() + "님께서 [사냥 코인] 아이템을 9999개 얻었습니다."));
                cm.sendOk("교환을 완료했습니다.");
                cm.dispose();
            } else {
                cm.sendOk("조건이 만족하지 않아서 교환을 실패하였습니다.");
                cm.dispose();
            }
        }
    }
}