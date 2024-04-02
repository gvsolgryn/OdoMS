function start() {
    status = -1;
    action(1, 0, 0);
}
num = 0;
grade = -1;

function action(mode, type, selection) {
    name = ["#fc0xFF5CD1E5#SS", "#bS", "#fc0xFFC4B73B#A", "#fc0xFF747474#B", "#fc0xFF993800#C"];
    prob = [10, 5, 10, 20]; // 확률
    req = [
        [-1, 3000], // SS
        [400, 3000], // S
        [300, 3000], // A
        [200, 3000], // B
        [100, 3000] // C
    ]
    itemlist = [ // [아이템코드,개수,확률]
        [
            [4319999, 5000, 5], //카오코인
            [5062005, 20, 6], //어미큐
            [5062503, 20, 6], //화에큐
            [2450134, 5, 6], // 경쿠 3배
            [2049360, 1, 10], //놀장강
            [2631527, 30, 6], //경코잼
            [4319994, 1000, 5], // 파란구슬
            [4319996, 3, 0.5], //부적
            [4319995, 3, 0.5], //부적
            [4001715, 5, 2.5] //정착금
        ], // SS
        [
            [4319999, 3000, 2], //카오코인
            [5062005, 10, 6], //어미큐
            [5062503, 10, 6], //화에큐
            [2450134, 4, 6], // 경쿠 3배
            [2049360, 1, 10], //놀장강
            [2435719, 25, 6], //코젬
            [4319994, 1000, 5], // 파란구슬
            [4001715, 5, 2.5] //정착금
        ], // S
        [
            [4319999, 2500, 2], //카오코인
            [5062005, 8, 6], //어미큐
            [5062503, 8, 6], //화에큐
            [5062500, 150, 6], //에디셔널큐브
            [2450134, 3, 6], // 경쿠 3배
            [2049360, 1, 10], //놀장강
            [2435719, 20, 6], //코젬
            [4319994, 1000, 5], // 파란구슬
            [4001715, 3, 0.7] //정착금
        ], // A
        [
            [4319999, 1000, 2], //카오코인
            [5062005, 5, 6], //어미큐
            [5062503, 5, 6], //화에큐
            [5062500, 100, 6], //에디셔널큐브
            [2450134, 2, 5], // 경쿠 3배
            [2049360, 1, 10], //놀장강
            [2435719, 15, 5], //코젬
            [4001715, 1, 0.7] //정착금
        ], // B
        [
            [4319999, 500, 2], //카오코인
            [5062009, 50, 0.5], //레드큐브
            [5062010, 50, 0.5], //블랙큐브
            [5062500, 50, 0.5], //에디셔널큐브
            [2450134, 1, 5], // 경쿠 3배
            [2435719, 10, 5] //코잼
        ], // C
    ]
    debug = [];
    if (mode == 1) {
        if (status != 0 || selection >= 3) {
            status++;
        } else {
            if (selection == 0) {
                num--;
            } else if (selection == 1) {
                num++;
            } else {
                if (cm.itemQuantity(4310229) >= req[grade][0]) {
                    rd = Math.floor(Math.random() * 100);
                    if (rd < prob[grade - 1]) {
                        cm.getPlayer().setKeyValue(20190616, "upgradechip", cm.getPlayer().getKeyValue(20190616, "upgradechip") + 1);
                        cm.getPlayer().dropMessage(6, "기어의 유니온 업그레이드가 성공적으로 진행되었습니다.");
                    } else {
                        cm.getPlayer().dropMessage(5, "기어의 유니온 업그레이드가 실패하였습니다.");
                    }
                    cm.gainItem(4310229, -req[grade][0]);
                } else {
                    cm.sendOk("기어를 유니온 업그레이드 하기 위한 #i4310229# #b#z4310229##k#n의 개수가 부족합니다.");
                    cm.dispose();
                    return;
                }
            }
        }
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        grade = 3 - cm.getPlayer().getKeyValue(20190616, "upgradechip");
        talk = "#fs11##e<유니온 업그레이드 기어>#n\r\n"
        talk += "현재 기어는 #e" + name[grade] + "등급#k#n으로 유니온 업그레이드 되었습니다.\r\n\r\n"
        talk += "#e[현재 등급에서 얻을 수 있는 아이템]\r\n\r\n"
        if (num != 0) {
            talk += "#L0# #e<#n#l "
        } else {
            talk += "　　"
        }
        for (i = 0; i < 5; i++) {
            talk += "#i" + itemlist[grade][i + num] + "# "
        }
        if (num < itemlist[grade].length - 5) {
            talk += "#L1# #e>#n#l"
        }
        talk += "\r\n\r\n";
        if (req[grade][0] >= 0) {
            talk += "#L2##b 유니온 업그레이드 시도 (#i4310229# x " + req[grade][0] + " 소모)#k#l\r\n"
        }
        talk += "#L3##b 현재 등급에서 교환 (#i4310229# x " + req[grade][1] + " 소모)#k#l\r\n"
        talk += "#L4##b 유니온 업그레이드 기어에 대한 설명 듣기#k#l\r\n"
        talk += "#L5##b 유니온 업그레이드 기어에서 등장하는 아이템 종류 확인하기"
        cm.sendSimple(talk);
    } else if (status == 1) {
        if (selection == 3) {
            rd = 0;
            if (cm.itemQuantity(4310229) >= req[grade][1]) {
                allprob = 0;
                probcheck = 0;
                for (i = 0; i < itemlist[grade].length; i++) {
                    allprob += itemlist[grade][i][2];
                }
                allprobrd = Math.floor(Math.random() * allprob)
                for (i = 0; i < itemlist[grade].length; i++) {
                    if (probcheck >= allprobrd) {
                        rd = i;
                        break;
                    }
                    probcheck += itemlist[grade][i][2];
                }
                //cm.getPlayer().dropMessage(6, "[GM DEBUG] allprob : "+allprob+", probcheck : "+probcheck+", allprobrd : "+allprobrd+", rd : "+rd+"");
                cm.sendOk("#fs11#축하합니다! " + name[grade] + "등급#k 기어에서 아래와 같은 아이템을 획득하셨습니다. " +
                    "#r(기어의 유니온 업그레이드 수치가 초기화 됩니다.)#k\r\n\r\n" +
                    "#fUI/UIWindow2.img/QuestIcon/4/0#\r\n\r\n" +
                    "#i" + itemlist[grade][rd][0] + "# #z" + itemlist[grade][rd][0] + "# " + itemlist[grade][rd][1] + "개");
                rds = Packages.server.Randomizer.rand(2, 3);
                graderd = cm.getPlayer().getKeyValue(20190616, "upgradechip") - rds
                if (graderd < -1) {
                    graderd = -1;
                }
                cm.getPlayer().setKeyValue(20190616, "upgradechip", graderd);
                cm.gainItem(itemlist[grade][rd][0], itemlist[grade][rd][1]);
                cm.gainItem(4310229, -req[grade][1]);
            } else {
                cm.sendOk("#fs11#아이템을 교환 하기 위한 #i4310229# #b#z4310229##k#n의 개수가 부족합니다.");
            }
            cm.dispose();
        } else if (selection == 4) {
            talk = "#fs11#유니온 업그레이드 기어에 대한 설명을 해 드리겠습니다.\r\n\r\n"
            talk += "#i3994096# #z4310229#으로 #b유니온 업그레이드 기어를 유니온 업그레이드 하거나, 현재 등급에서 아이템을 교환#k할 수 있습니다.\r\n"
            talk += "#i3994097# #b유니온 업그레이드 기어#k 등급이 높아질수록 #b기어 유니온 업그레이드및 현재 등급에서의 교환에 사용되는 #z4310229#의 개수#k가 증가합니다.\r\n"
            talk += "#i3994098# #b유니온 업그레이드 기어#k 등급이 높아질수록 #b기어 유니온 업그레이드의 성공 확률이 낮아#k집니다.\r\n"
            talk += "#i3994099# 현재 등급에서 아이템 교환을 진행하면 현재 등급에서 획득할 수 있는 아이템 중 #b1종#k을 획득하며, 유니온 업그레이드 기어가 #bC등급으로 초기화#k됩니다.\r\n"
            //talk += "#i3994100# S급과SS급에서 등장하는 레드 무기는 캐시 판정 아이템 입니다. 반응이 좋으면 다른 직업군 무기도 제작하겠습니다."
            cm.sendOk(talk);
            cm.dispose();
        } else {
            for (i = 0; i < name.length; i++) {
                a = 0;
                for (j = 0; j < itemlist[i].length; j++) {
                    a += itemlist[i][j][2];
                }
                debug.push(a);
            }
            talk = "#fs11#유니온 업그레이드 기어에서 등장하는 아이템은 아래와 같습니다.\r\n"
            if (cm.getPlayer().getGMLevel() >= 6) {
                talk += "#fs11##r#e확률 정보는 GM에게만 표시됩니다.#k#n\r\n"
            }
            talk += "#fs11#\r\n"
            for (i = 0; i < name.length; i++) {
                talk += "#e<" + name[i] + "등급#k 기어>#n\r\n"
                for (j = 0; j < itemlist[i].length; j++) {
                    talk += "#i" + itemlist[i][j][0] + "# "
                    if (cm.getPlayer().getGMLevel() >= 6) {
                        talk += "[확률 : " + ((itemlist[i][j][2] / debug[i]) * 100).toFixed(2) + "%]　　"
                        if (j % 2 == 1) {
                            talk += "\r\n"
                        }
                    }
                }
                talk += "\r\n\r\n"
            }
            cm.sendOk(talk);
            cm.dispose();
        }
    }
}