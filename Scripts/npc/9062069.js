var status = -1;

R1N = null;
R2N = null;
R3N = null;

itemcode = [5062500, 5062006];

list = [[30602, "보스 몬스터 공격력 30%"], [30291, "몬스터 방어율 무시 30%"], [40292, "몬스터 방어율 무시 40%"], [40603, "보스 몬스터 공격력 40%"], [40070, " 데미지12%"], [30051, " 공격력 9%"], [40056, "크리티컬 데미지 8%"], [40052, "마력 12%"], [40070, "데미지 12%"], [40086, "올스텟 9%"],
        [40650, "메소 획득량 증가 20%"], [40051, "공격력 12%"], [40656, "아이템 획득 확률 증가 20%"]];


attlist = [[30602, "보스 몬스터 공격력 30%"], [30291, "몬스터 방어율 무시 30%"], [40292, "몬스터 방어율 무시 40%"], [40603, "보스 몬스터 공격력 40%"], [40070, " 데미지12%"], [30051, " 공격력 9%"], [40056, "크리티컬 데미지 8%"], [40052, "마력 12%"], [40070, "데미지 12%"], [40086, "올스텟 9%"],
        [40650, "메소 획득량 증가 20%"], [40051, "공격력 12%"], [40656, "아이템 획득 확률 증가 20%"]];

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
        말 = "#fs11#";
        말 += "최상의 옵션으로 플래티넘 잠재능력을 재설정 해드립니다.\r\n";
        말 += "물론 #i5062006##d#z5062006##k를 가져온다면 말이죠.\r\n";
        말 += "위의 아이템은 마일리지샵을 통해 얻을 수 있습니다.\r\n";
        말 += "#e#z5062006##n의 장점은 최상의 잠재능력이 부여됩니다.\r\n";
        말 += "#L1##b#z5062006#를 사용하고 싶습니다.\r\n\r\n";
        cm.sendSimple(말);
    } else if (status == 1) {
        sT = selection;
        if (cm.itemQuantity(itemcode[sT]) >= 1) {
            말 = "#fs11##d#z5062006##k를 사용하실 아이템을 신중히 선택해주세요.\r\n#r주의 : 선택시 잠재능력이 바로 변경 됩니다.#k\r\n"
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                if ((cm.getInventory(1).getItem(i) != null) && (cm.getInventory(1).getItem(i).getPotential1() != 0)) {
                    말 += "#L" + i + "# #i" + cm.getInventory(1).getItem(i).getItemId() + "# #b#z" + cm.getInventory(1).getItem(i).getItemId() + "##k\r\n"
                }
            }
            cm.sendSimple(말);
        } else {
            cm.sendOk("플래티넘 큐브가 부족합니다.");
            cm.dispose();
        }
    } else if (status == 2) {
        if (selection < 9999) {
            sItem = cm.getInventory(1).getItem(selection)
            G1 = sItem.getPotential1()
            G2 = sItem.getPotential2()
            G3 = sItem.getPotential3()
        }
        if (selection == 9999) {
            sItem.setPotential1(G1);
            sItem.setPotential2(G2);
            sItem.setPotential3(G3);
            cm.getPlayer().forceReAddItem(sItem, Packages.client.inventory.MapleInventoryType.EQUIP);
            cm.dispose();
        } else {
            if (sItem.getPotential1() <= 0) {
                cm.sendOk("#fs11#잠재부여가 되지 않아 큐브를 사용할 수 없습니다.");
                cm.dispose();
                return;
            }
            if (cm.itemQuantity(itemcode[sT]) >= 1) {
                if (sItem.getItemId() < 1210000 || sItem.getItemId() > 1589999) {
                    R1 = Math.floor(Math.random() * list.length);
                    R2 = Math.floor(Math.random() * list.length);
                    R3 = Math.floor(Math.random() * list.length);
                    if (sT == 1) {
                        for (a = 0; a < list.length; a++) {
                            if (list[a][0] == G1) {
                                R1N = list[a][1]
                            }
                        }
                        for (b = 0; b < list.length; b++) {
                            if (list[b][0] == G2) {
                                R2N = list[b][1]
                            }
                        }
                        for (c = 0; c < list.length; c++) {
                            if (list[c][0] == G3) {
                                R3N = list[c][1]
                            }
                        }
                    }
                    sItem.setPotential1(list[R1][0]);
                    sItem.setPotential2(list[R2][0]);
                    sItem.setPotential3(list[R3][0]);
                    cm.getPlayer().forceReAddItem(sItem, Packages.client.inventory.MapleInventoryType.EQUIP);
                    cm.gainItem(itemcode[sT], -1);
                    말 = "#e#r선택된 아이템 : #i" + sItem.getItemId() + "##n#k\r\n\r\n"
                    if (sT == 1) {
                        if (R1N == null) {
                            R1N = "기타 잠재능력"
                        }
                        if (R2N == null) {
                            R2N = "기타 잠재능력"
                        }
                        if (R3N == null) {
                            R3N = "기타 잠재능력"
                        }
                        말 += "[Before]\r\n"
                        말 += "잠재능력 1 : " + R1N + "\r\n";
                        말 += "잠재능력 2 : " + R2N + "\r\n";
                        말 += "잠재능력 3 : " + R3N + "\r\n\r\n";
                        말 += "[After]\r\n"
                    }
                    말 += "잠재능력 1 : " + list[R1][1] + "\r\n";
                    말 += "잠재능력 2 : " + list[R2][1] + "\r\n";
                    말 += "잠재능력 3 : " + list[R3][1] + "\r\n\r\n";
                } else {
                    R1 = Math.floor(Math.random() * attlist.length);
                    R2 = Math.floor(Math.random() * attlist.length);
                    R3 = Math.floor(Math.random() * attlist.length);
                    if (sT == 1) {
                        for (a = 0; a < attlist.length; a++) {
                            if (attlist[a][0] == G1) {
                                R1N = attlist[a][1]
                            }
                        }
                        for (b = 0; b < attlist.length; b++) {
                            if (attlist[b][0] == G2) {
                                R2N = attlist[b][1]
                            }
                        }
                        for (c = 0; c < attlist.length; c++) {
                            if (attlist[c][0] == G3) {
                                R3N = attlist[c][1]
                            }
                        }
                    }
                    sItem.setPotential1(attlist[R1][0]);
                    sItem.setPotential2(attlist[R2][0]);
                    sItem.setPotential3(attlist[R3][0]);
                    cm.getPlayer().forceReAddItem(sItem, Packages.client.inventory.MapleInventoryType.EQUIP);
                    cm.gainItem(itemcode[sT], -1);
                    말 = "#fs11##e[강화완료]#n #i" + sItem.getItemId() + "# #b의 변경된 플래티넘 잠재옵션#k\r\n\r\n"
                    if (sT == 1) {
                        if (R1N == null) {
                            R1N = "기타 잠재능력";
                        }
                        if (R2N == null) {
                            R2N = "기타 잠재능력";
                        }
                        if (R3N == null) {
                            R3N = "기타 잠재능력";
                        }
                    }
                    말 += "#d잠재능력 1 :#k #r" + attlist[R1][1] + "#k\r\n";
                    말 += "#d잠재능력 2 :#k #r" + attlist[R2][1] + "#k\r\n";
                    말 += "#d잠재능력 3 :#k #r" + attlist[R3][1] + "#k\r\n";
                }
                말 += "#L10000# #b한번 더 사용하겠습니다.#l\r\n\r\n";
                // if (sT == 1) {
                //     말 += "#L9999# #b기존의 잠재능력으로 되돌리겠습니다.#l\r\n\r\n";
                // }
                말 += "#r그만 사용하시려면, ESC버튼을 눌러주세요.#k";
                cm.sendSimple(말);
                status--;
            } else {
                cm.sendOk("플래티넘 큐브가 부족합니다.");
                cm.dispose();
            }
        }
    }
}
