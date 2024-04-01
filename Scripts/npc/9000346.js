﻿importPackage(Packages.client.inventory);
var status = -1;

R1N = null;
R2N = null;
R3N = null;

function start() {
    status = -1;
    action (1, 0, 0);
}

function action(mode, type, selection) {
    itemcode = [5062503]
    list = [[40057,"크리데미지 + 8%"],[40041,"힘 + 12%"],[40042,"덱 + 12%"],[40043,"인트 + 12%"],[40044,"럭 + 12%"],[40650,"메소 획득량 + 20%"],[30601,"보스 공격력 + 20%"],[30602,"보스 공격력 + 30%"],[30044,"럭 + 9%"],[30043,"인트 + 9%"],[30042,"덱스 + 9%"],[30041,"힘 + 9%"],[40086,"올스텟 + 9%"],[40045,"HP증가 + 12%"],[40055,"크리티컬 발동 + 12%"],
           [40656,"드랍률 + 20%"]]
    attlist = [[40051,"공격력 + 12%"],[40052,"마력 + 12%"],[40070,"데미지 + 12%"],[40603,"보스 공격력 + 40%"],[40602,"보스 공격력 + 35%"],[30051, "공격력 + 9%"],[40292,"방어력 무시 + 40%"],[40291,"방어력 무시 + 35%"],[30052,"마력 + 9%"],[40055,"크리티컬 발동 + 12%"],[10291,"방어력 무시 + 15%"],[20291,"방어력 무시 + 20%"],[30291,"방어력 무시 + 30%"],[30601,"보스 공격력 + 20%"],[30602,"보스 공격력 + 30%"],[40601,"보스 공격력 + 25%"]]
          if (mode == 1) {
            status++;
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
       말 = "#fs11#악세와 방어구에 최상의 옵으로 잠재능력을 설정 해 드립니다. \r\n물론 #v5062503# 를 가져온다면 말이죠.\r\n위의 아이템은 후원,홍보상점을 통해 얻을 수 있어요.\r\n#e화이트 에디셔널 큐브#k#n의 장점은  #b크리데미지옵션#k & #b최상위옵션#k이 붙습니다.\r\n"
       말+= "#L0# #b최상옵 잠재 설정권 사용하기 (최상위옵션 전용)\r\n";
       cm.sendSimple(말);
    } else if (status == 1) {
       sT = selection;
       if (cm.itemQuantity(itemcode[sT]) >= 1) {
           말 = "#fs11##d최상옵 잠재 설정권을 사용할 아이템을 선택해 주세요.\r\n\r\n"
           for (i=0; i<cm.getInventory(1).getSlotLimit(); i++) {
               if ((cm.getInventory(1).getItem(i) != null) && (cm.getInventory(1).getItem(i).getPotential4() != 0)) {
                  말 += "#L"+i+"# #i"+cm.getInventory(1).getItem(i).getItemId()+"# #z"+cm.getInventory(1).getItem(i).getItemId()+"#\r\n"
               }
           }
           cm.sendSimple(말);
       } else {
           cm.sendOk("#fs11#화이트 에디셔널 큐브가 부족합니다. 획득처는 후원,홍보상점입니다.");
           cm.dispose();
       }
    } else if (status == 2) {
       if (selection < 9999) {
           sItem = cm.getInventory(1).getItem(selection)
           G1 = sItem.getPotential4()
           G2 = sItem.getPotential5()
           G3 = sItem.getPotential6()
       }
       if (selection == 9999) {
           sItem.setPotential4(G1);
           sItem.setPotential5(G2);
           sItem.setPotential6(G3);
           cm.getPlayer().forceReAddItem(sItem, MapleInventoryType.EQUIP);
           cm.dispose();
       } else {
       if (cm.itemQuantity(itemcode[sT]) >= 1) {
           if (sItem.getItemId() < 1210000 || sItem.getItemId() > 1589999) {
               R1 = Math.floor(Math.random() * list.length);
               R2 = Math.floor(Math.random() * list.length);
               R3 = Math.floor(Math.random() * list.length);
           if (sT == 1) {
               for (a=0; a<list.length; a++) {
                   if (list[a][0] == G1) {
                        R1N = list[a][1]
                   }
               }
               for (b=0; b<list.length; b++) {
                   if (list[b][0] == G2) {
                        R2N = list[b][1]
                   }
               }
               for (c=0; c<list.length; c++) {
                   if (list[c][0] == G3) {
                        R3N = list[c][1]
                   }
               }
           }
           sItem.setPotential4(list[R1][0]);
           sItem.setPotential5(list[R2][0]);
           sItem.setPotential6(list[R3][0]);
           cm.getPlayer().forceReAddItem(sItem, MapleInventoryType.EQUIP);
           cm.gainItem(itemcode[sT], -1);
           말 = "#e#r선택된 아이템 : #i"+sItem.getItemId()+"##n#k\r\n\r\n"
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
                말+= "잠재능력 1 : " + R1N + "\r\n"
                말+= "잠재능력 2 : " + R2N + "\r\n"
                말+= "잠재능력 3 : " + R3N + "\r\n\r\n"
                말 += "[After]\r\n"
           }
           말+= "잠재능력 1 : " + list[R1][1] + "\r\n"
           말+= "잠재능력 2 : " + list[R2][1] + "\r\n"
           말+= "잠재능력 3 : " + list[R3][1] + "\r\n\r\n"
          } else {
                          R1 = Math.floor(Math.random() * attlist.length);
               R2 = Math.floor(Math.random() * attlist.length);
               R3 = Math.floor(Math.random() * attlist.length);
           if (sT == 1) {
               for (a=0; a<attlist.length; a++) {
                   if (attlist[a][0] == G1) {
                        R1N = attlist[a][1]
                   }
               }
               for (b=0; b<attlist.length; b++) {
                   if (attlist[b][0] == G2) {
                        R2N = attlist[b][1]
                   }
               }
               for (c=0; c<attlist.length; c++) {
                   if (attlist[c][0] == G3) {
                        R3N = attlist[c][1]
                   }
               }
           }
           sItem.setPotential4(attlist[R1][0]);
           sItem.setPotential5(attlist[R2][0]);
           sItem.setPotential6(attlist[R3][0]);
           cm.getPlayer().forceReAddItem(sItem, MapleInventoryType.EQUIP);
           cm.gainItem(itemcode[sT], -1);
           말 = "#e#r선택된 아이템 : #i"+sItem.getItemId()+"##n#k\r\n\r\n"
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
                말+= "잠재능력 1 : " + R1N + "\r\n"
                말+= "잠재능력 2 : " + R2N + "\r\n"
                말+= "잠재능력 3 : " + R3N + "\r\n\r\n"
                말 += "[After]\r\n"
           }
           말+= "잠재능력 1 : " + attlist[R1][1] + "\r\n"
           말+= "잠재능력 2 : " + attlist[R2][1] + "\r\n"
           말+= "잠재능력 3 : " + attlist[R3][1] + "\r\n\r\n"
           }
           말+= "#L10000# #b한번 더 사용 하겠습니다.\r\n"
           if (sT == 1) {
               말+= "#L9999# #b기존의 잠재능력으로 되돌리겠습니다.#l\r\n\r\n";
           }
           말+= "#r그만 사용하시려면, ESC버튼을 눌러주세요.#k"
           cm.sendSimple(말);
           status--;
        } else {
            cm.sendOk("화이트 에디셔널 큐브가 부족합니다.");
            cm.dispose();
        }
}
}
}