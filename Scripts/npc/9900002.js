


/*

    * 단문엔피시 자동제작 스크립트를 통해 만들어진 스크립트 입니다.

    * (Guardian Project Development Source Script)

    닉스 에 의해 만들어 졌습니다.

    엔피시아이디 : 9900002

    엔피시 이름 : 신용협동조합

    엔피시가 있는 맵 : 빅토리아로드 : 엘리니아 (180000000)

    엔피시 설명 : MISSINGNO


*/
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.handling.channel.handler);
importPackage(Packages.tools.packet);
importPackage(Packages.handling.world);
importPackage(java.lang);
importPackage(Packages.constants);
importPackage(Packages.server.items);
importPackage(Packages.client.items);
importPackage(java.lang);
importPackage(Packages.launch.world);
importPackage(Packages.tools.packet);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);
importPackage(Packages.server.enchant);
importPackage(java.sql);
importPackage(Packages.database);
importPackage(Packages.handling.world);
importPackage(Packages.constants);
importPackage(java.util);
importPackage(java.io);
importPackage(Packages.client.inventory);
importPackage(Packages.client);
importPackage(Packages.server);
importPackage(Packages.tools.packet);
var 클론 = [[1032800], [1022700], [1012950], [1032801], [1022701], [1012951], [1032802], [1022702], [1012952], [1032803], [1022703], [1012953]]
var status = -1;
var sel = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode == 0) {
        status--;
    }
    if (mode == 1) {
        status++;
    }

    if (status == 0) {
        말 = "#fs11#반갑네! 자네는 #e클론아바타#n에 대해서 알고 있나? 크크.\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        말 += "#L0##fc0xFF8041D9#클론아바타 - 에픽 등급#k 아이템을 제작하고 싶습니다.\r\n";
        말 += "#L1##fc0xFFEDA900#클론아바타 - 유니크 등급#k 아이템을 제작하고 싶습니다.\r\n";
        말 += "#L2##fc0xFF47C83E#클론아바타 - 레전드리 등급#k 아이템을 제작하고 싶습니다.\r\n\r\n";
        말 += "#L10##b클론아바타 종류를 확인해보고 싶습니다.\r\n";
        말 += "#L99##r대화를 종료하고 싶습니다.\r\n";
        cm.sendSimpleS(말, 0x04, 9401232);
    } else if (status == 1) {
        sel = selection;
        if (selection == 0) {
            말 = "#fs11#어떤 아이템을 제작할 건지 선택해보게나.\r\n";
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#L0##i1032801# #fc0xFF8041D9##z1032801# 제작하기\r\n";
            말 += "#L1##i1022701# #fc0xFF8041D9##z1022701# 제작하기\r\n";
            말 += "#L2##i1012951# #fc0xFF8041D9##z1012951# 제작하기\r\n";
            cm.sendSimpleS(말, 0x04, 9401232);
        } else if (selection == 1) {
            말 = "#fs11#어떤 아이템을 제작할 건지 선택해보게나.\r\n";
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#L10##i1032802# #fc0xFFEDA900##z1032802# 제작하기\r\n";
            말 += "#L11##i1022702# #fc0xFFEDA900##z1022702# 제작하기\r\n";
            말 += "#L12##i1012952# #fc0xFFEDA900##z1012952# 제작하기\r\n";
            cm.sendSimpleS(말, 0x04, 9401232);
        } else if (selection == 2) {
            말 = "#fs11#어떤 아이템을 제작할 건지 선택해보게나.\r\n";
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#L20##i1032803# #fc0xFF47C83E##z1032803# 제작하기\r\n";
            말 += "#L21##i1022703# #fc0xFF47C83E##z1022703# 제작하기\r\n";
            말 += "#L22##i1012953# #fc0xFF47C83E##z1012953# 제작하기\r\n";
            cm.sendSimpleS(말, 0x04, 9401232);
        } else if (selection == 10) {
            말 = "#fs11#클론 아바타 리스트입니다.\r\n\r\n"
            for (var a = 0; a < 클론.length; a++) {
                말 += "#i" + 클론[a][0] + "# #b#z" + 클론[a][0] + "##k\r\n"
                if (a == 11) break;
            }
            cm.sendOkS(말, 0x04, 9401232);
            cm.dispose();
        } else if (seletion == 99) {
            cm.dispose();
        }
    } else if (status == 2) {
        if (selection == 0) {
            말 = "#fs11##i1032801# #b#z1032801##k 아이템을 제작하겠나?"
            말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#i1032800# #b#z1032800##k #r1개#k\r\n"
            말 += "#i1022700# #b#z1022700##k #r1개#k\r\n"
            말 += "#i1012950# #b#z1012950##k #r1개#k\r\n"
            말 += "#i4310022# #b#z4310022##k #r25개#k\r\n\r\n"
            말 += "제작 확률은 #b75%#k 입니다. 정말로 제작하시겠습니까?"
            cm.sendYesNoS(말, 0x04, 9401232);
        } else if (selection == 1) {
            말 = "#fs11##i1022701# #b#z1022701##k 아이템을 제작하겠나?"
            말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#i1032800# #b#z1032800##k #r1개#k\r\n"
            말 += "#i1022700# #b#z1022700##k #r1개#k\r\n"
            말 += "#i1012950# #b#z1012950##k #r1개#k\r\n"
            말 += "#i4310022# #b#z4310022##k #r25개#k\r\n\r\n"
            말 += "제작 확률은 #b75%#k 입니다. 정말로 제작하시겠습니까?"
            cm.sendYesNoS(말, 0x04, 9401232);
        } else if (selection == 2) {
            말 = "#fs11##i1012951# #b#z1012951##k 아이템을 제작하겠나?"
            말 += "\r\n제작을 하려면 아래의 재료가 필요하지.\r\n"
            말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
            말 += "#i1032800# #b#z1032800##k #r1개#k\r\n"
            말 += "#i1022700# #b#z1022700##k #r1개#k\r\n"
            말 += "#i1012950# #b#z1012950##k #r1개#k\r\n"
            말 += "#i4310022# #b#z4310022##k #r25개#k\r\n\r\n"
            말 += "제작 확률은 #b75%#k 입니다. 정말로 제작하시겠습니까?"
            cm.sendYesNoS(말, 0x04, 9401232);
        }
    } else if (status == 3) {
        item = cm.getInventory(6)
        if (sel == 0) {
            if (!cm.haveItem(1032800, 1) || !cm.haveItem(1022700, 1) || !cm.haveItem(1012950, 1) || !cm.haveItem(4310022, 25)) {   //재료체크
                cm.sendOkS("#fs11##b#z1032801##k을 제작하기 위한 재료 아이템이 부족한거 같군.", 0x04, 9401232);
                cm.dispose();
                return;
            }
            if (Math.floor(Math.random() * 100) <= 75) {// 성공했을때
                var a = 0, b = 0, c = 0;
                for (w = 0; w < inz.getSlotLimit(); w++) {
                    if (inz.getItem(w) == null) {
                        continue;
                    }
                    if (1032800 == inz.getItem(w).getItemId() && a < qty[0]) {
                        MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                        a++;
                        if (a >= 1) {
                            break;
                        }
                    }
                }
                for (w = 0; w < inz.getSlotLimit(); w++) {
                    if (inz.getItem(w) == null) {
                        continue;
                    }
                    if (1022700 == inz.getItem(w).getItemId() && b < qty[1]) {
                        MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                        b++;
                        if (b >= 1) {
                            break;
                        }
                    }
                }
                for (w = 0; w < inz.getSlotLimit(); w++) {
                    if (inz.getItem(w) == null) {
                        continue;
                    }
                    if (1012950 == inz.getItem(w).getItemId() && c < qty[2]) {
                        MapleInventoryManipulator.removeFromSlot(cm.getPlayer().getClient(), GameConstants.getInventoryType(inz.getItem(w).getItemId()), inz.getItem(w).getPosition(), inz.getItem(w).getQuantity(), false);
                        c++;
                        if (c >= 1) {
                            break;
                        }
                    }
                }
                cm.gainItem(4310022, -25);
                cm.gainItem(1032801, 1);
                말 = "#fs11##b#z1032801##k 제작에 성공했다네!\r\n\r\n"
                말 += "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n"
                말 += "#i1032801##b#z1032801##k"
                cm.sendOkS(말, 0x04, 9401232);
                cm.dispose();
            } else { // 실패했을때
                cm.getInventory(6).removeItem(1022700, -1, true);
                cm.getInventory(6).removeItem(1012950, -1, true);
                cm.getInventory(6).removeItem(1032800, -1, true);
                cm.gainItem(4310022, -25);
                cm.sendOkS("#fs11##r제작이 실패했네.. 다음에는 꼭 성공해보게!", 0x04, 9401232);
                cm.dispose();
                return;
            }
        }
    }
}
