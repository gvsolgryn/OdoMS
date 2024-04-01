var status = 0;

importPackage(Packages.constants);


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
            if (cm.getPlayer().getLevel() >= 10) {
                if (cm.getPlayer().getMapId() == 121212123 || cm.getPlayer().getMapId() == 121212123) {
                    var jessica = "#fs11##d꿀밤의 또 하나의 재미 #r꿀잼 낚시터#k\r\n#d이 곳이 그렇게 월척이 잘 잡히기로 소문이 자자하지!!\r\n";
                    jessica += "#k#d#fs11##L0##fUI/GuildMark.img/Mark/Pattern/00004001/10#  #d낚시에 대한 설명#k\r\n";
                    jessica += "#k#d#fs11##L1##fUI/GuildMark.img/Mark/Pattern/00004001/11#  #d낚시 용품 구입\r\n";
                    jessica += "#k#d#fs11##L2##fUI/GuildMark.img/Mark/Pattern/00004001/12#  #r물고기로 템 교환#k";
                    jessica += "#L3# #r광장 이동#k";
                    cm.sendSimple(jessica);
                } else {
                    cm.sendOk("#fs11#세월을 낚는 재미란.. 즐겨본자만이 아는법이지..");
                    cm.sendOk("#fs11#현재 상점 점검중입니다.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("#fs11##r낚시 이용은 레벨 10 이상만 이용 가능합니다.", 9062004);
                cm.dispose();
            }
        } else if (status == 1) {
            if (selection == 0) {
                cm.sendOk("#fs11#나에게 낚시 용품을 구입한 후 좋은 자리를 선점해!\r\n의자에 앉아 있으면 낚시가 진행된다구~ \r\n물고기를 많이 모아오면 좋은 것으로 교환해 줄게!\r\n금화1만메소~100만메소,유니온코인을 얻을수있어! ");
                cm.dispose();

            } else if (selection == 1) {
                var jessica2 = "#fs11 ##b원하는 품목을 선택 해봐~#k\r\n#r인벤토리가 꽉차면 못 받을 수 있으니 주의하길바래!#k\r\n";
                jessica2 += "#L0##i3010432# #r낚시 의자#k #d(5.000.000)#k\r\n";
                //jessica2 += "#L1##i4035005# #r미끼 구입 50 개#k #d(10.000.000)#k\r\n";
                //jessica2 += "#L2##i4035005# #r미끼 구입 500 개#k #d(100.000.000)#k";
                cm.sendSimple(jessica2);
            } else if (selection == 3) {
                cm.dispose();
                cm.warp(121212123, 0);
                cm.sendOk("#fs11##d다음에 또 여유를 즐기러 오시게.. 젊은이..#k");
            } else if (selection == 2) {
                var jessica3 = "#fs11##b골라! 골라! 잽싸게 골라버려! (수정예정) #k\r\n#r인벤토리가 꽉차면 못 받을 수 있으니 주의하길바래!#k\r\n";
                jessica3 += "\r\n--------------------------------------------------\r\n";
                jessica3 += "#L3##r#k #i1142949# #b#t1142949##k#l\r\n            #d올스탯 200 공, 마 150#k #r[ #i4001187# #i4001188# #i4001189# 5000 개 ]#k\r\n";
                /*   jessica3 += "#L110##r#k #i1032024# #b#t1032024##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L106##r#k #i1102039# #b#t1102039##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L107##r#k #i1072153# #b#t1072153##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L108##r#k #i1012289# #b#t1012289##k#l\r\n            #d올스탯 200 공, 마 100#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L200##r#k #i1002186# #b#t1002186##k#l\r\n            #d올스탯 200 공, 마 100#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L2001##r#k #i1022079# #b#t1022079##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L2002##r#k #i1092067# #b#t1092067##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L20022##r#k #i1342069# #b#t1342069##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L201##r#k #i1082102# #b#t1082102##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                 jessica3 += "#L2003##r#k #i1012379# #b#t1012379##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";*/
                 jessica3 += "#L2004##r#k #i1702119# #b#t1702119##k#l\r\n            #d올스탯 300 공, 마 150#k #r[ #i4001187# #i4001188# #i4001189# 3000 개 ]#k\r\n";
                 jessica3 += "#L2005##r#k #i1702118# #b#t1702118##k#l\r\n            #d올스탯 300 공, 마 150#k #r[ #i4001187# #i4001188# #i4001189# 3000 개 ]#k\r\n";
                 jessica3 += "#L2006##r#k #i1702174# #b#t1702174##k#l\r\n            #d올스탯 300 공, 마 150#k #r[ #i4001187# #i4001188# #i4001189# 3000 개 ]#k\r\n";
                jessica3 += "#L2007##r#k #i1092056# #b#t1092056##k#l\r\n            #d올스탯 200 공, 마 100#k #r[ #i4001187# #i4001188# #i4001189# 3000 개 ]#k\r\n";
                 jessica3 += "#L202##r#k #i1113085# #b#t1113085##k#l\r\n            #d올스탯 300 공, 마 100#k #r[ #i4001187# #i4001188# #i4001189# 3000 개 ]#k\r\n";
                jessica3 += "#L203##r#k #i1114000# #b#t1114000##k#l\r\n            #d올스탯 300 공, 마 100#k #r[ #i4001187# #i4001188# #i4001189# 5000 개 ]#k\r\n";
                jessica3 += "#L204##r#k #i1113231# #b#t1113231##k#l\r\n            #d올스탯 500 공, 마 200#k #r[ #i4001187# #i4001188# #i4001189# 5000 개 ]#k\r\n";
                jessica3 += "#L205##r#k #i1182200# #b#t1182200##k#l\r\n            #d올스탯 400 공, 마 200#k #r[ #i4001187# #i4001188# #i4001189# 5000 개 ]#k\r\n";
                jessica3 += "#L206##r#k #i1182069# #b#t1182069##k#l\r\n            #d올스탯 400 공, 마 200#k #r[ #i4001187# #i4001188# #i4001189# 5000 개 ]#k\r\n";
                jessica3 += "#L207##r#k #i5680222# #b#t5680222##k#l\r\n            #d캐시 아이템#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k\r\n";
                jessica3 += "#L109##r#k #i2430218# #b#t2430218##k#l\r\n            #d소비 아이템#k #r[ #i4001187# #i4001188# #i4001189# 500 개 ]#k\r\n";
                jessica3 += "#L5##r#k #i4001861# #d1 천 5 백만 메소#k #r[ #i4001187# #i4001188# #i4001189# 50개 ]#k#l\r\n";
                jessica3 += "#L6##r#k #i4001861# #d1 억 5 천만 메소#k #r[ #i4001187# #i4001188# #i4001189# 500개 ]#k#l\r\n";
                jessica3 += "#L7##r#k #i4001861# #d15 억원 메소#k #r[ #i4001187# #i4001188# #i4001189# 5000개 ]#k#l\r\n";
                jessica3 += "\r\n---------------------------------------------------\r\n";
                jessica3 += "#L8##i1012329# #b#t1012329##k#l\r\n            #d올스탯 100 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 2000 개 ]#k#l\r\n";
                jessica3 += "\r\n---------------------------------------------------\r\n";
                jessica3 += "#L9##i1672004# #b올스탯 50 공, 마 50#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
                jessica3 += "#L10##i2435795# #b안드로이드 뽑기 상자#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
                jessica3 += "#L99##i2435795# #b안드로이드 뽑기 상자#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
                jessica3 += "#L11##i1662003# #b올스탯 300 공, 마 300#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
                jessica3 += "\r\n---------------------------------------------------\r\n"
                jessica3 += "#L12##i1102369# #b올스탯 30 공, 마 30#k #r[ #i4001187# #i4001188# #i4001189# 500 개 ]#k#l\r\n";
                jessica3 += "#L13##i1152090# #b올스탯 30 공, 마 30#k #r[ #i4001187# #i4001188# #i4001189# 500 개 ]#k#l\r\n";
                jessica3 += "\r\n---------------------------------------------------\r\n"
                jessica3 += "#L8000##i2049360# #b놀장강 3개#k #r[ #i4001187# #i4001188# #i4001189# 1500 개 ]#k#l\r\n";
                jessica3 += "#L8001##i2049153# #b놀긍혼 10개#k #r[ #i4001187# #i4001188# #i4001189# 800 개 ]#k#l\r\n";
                jessica3 += "#L8002##i2433019# #b메소 럭키백 1개#k #r[ #i4001187# #i4001188# #i4001189# 300 개 ]#k#l\r\n";
                jessica3 += "#L8003##i4021037# #b하급 강화석 1개#k #r[ #i4001187# #i4001188# #i4001189# 700 개 ]#k#l\r\n";
                jessica3 += "#L8004##i4031868# #b초월 강화석 1개#k #r[ #i4001187# #i4001188# #i4001189# 1000 개 ]#k#l\r\n";
                jessica3 += "#L8005##i2439653# #b영환불 10개 패키지 1개#k #r[ #i4001187# #i4001188# #i4001189# 700 개 ]#k#l\r\n";
                jessica3 += "\r\n---------------------------------------------------\r\n";
                jessica3 += "#L100##i4001187# #b음치#k #r500 개#k → #i4001188# #b몸치#k #r250 개#k#l\r\n";
                jessica3 += "#L101##i4001187# #b음치#k #r500 개#k → #i4001189# #b박치#k #r250 개#k#l\r\n";
                jessica3 += "#L102##i4001188# #b몸치#k #r500 개#k → #i4001187# #b음치#k #r250 개#k#l\r\n";
                jessica3 += "#L103##i4001188# #b몸치#k #r500 개#k → #i4001189# #b박치#k #r250 개#k#l\r\n";
                jessica3 += "#L104##i4001189# #b박치#k #r500 개#k → #i4001187# #b음치#k #r250 개#k#l\r\n";
                jessica3 += "#L105##i4001189# #b박치#k #r500 개#k → #i4001188# #b몸치#k #r250 개#k#l\r\n";
                jessica3 += "\r\n---------------------------------------------------\r\n";
                //jessica3 += "#L7777##i4310229# #b#t4310229##k#l\r\n             #r[ #i4001187# #i4001188# #i4001189# 100 개 ]#k#l\r\n";
                //jessica3 += "#L1112##i4021022# #b태초의 정수#k #r200 개#k → #i2436225# #r1개(1000만 ~ 5억 추뎀)#k#l\r\n";
                //jessica3 += "#L1113##i4021022# #b태초의 정수#k #r300 개#k → #i2433834# #r1개#k#l\r\n";
                cm.sendSimple(jessica3);
            }

        } else if (status == 2) {

            if (selection == 0) {
                if (cm.getMeso() >= 5000000) {
                    if (cm.canHold(3010432)) {
                        cm.gainItem(3010432, 1);
                        cm.gainMeso(-5000000);
                        cm.sendOk(" #fs11##d다음에 또 와~!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r설치칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r돈이 없으면 살 수 없어#k");
                    cm.dispose();
                }

            } else if (selection == 1) {
                if (cm.getMeso() >= 10000000) {
                    if (cm.canHold(4035005)) {
                        cm.gainItem(4035005, 50);
                        cm.gainMeso(-10000000);
                        cm.sendOk("#fs11##d항상 싱싱한 미끼! 다음에 또 와~#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r돈이 없으면 살 수 없어#k");
                    cm.dispose();
                }

            } else if (selection == 2) {
                if (cm.getMeso() >= 100000000) {
                    if (cm.canHold(4035005)) {
                        cm.gainItem(4035005, 500);
                        cm.gainMeso(-100000000);
                        cm.sendOk("#fs11##d항상 싱싱한 미끼! 다음에 또 와#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r돈이 없으면 살 수 없어.#k");
                    cm.dispose();
                }

            } else if (selection == 3) {
                if (cm.haveItem(4001187, 5000) && cm.haveItem(4001188, 5000) && cm.haveItem(4001189, 5000)) {
                    if (cm.canHold(1142949)) {
                        cm.gainItem(4001187, -5000);
                        cm.gainItem(4001188, -5000);
                        cm.gainItem(4001189, -5000);
                        cm.gainItemAllStat(1142949, 1, 200, 150);
                        cm.sendOk("#fs11##b#t1142949##k 를 교환 하였습니다.\r\n너야말로 진정한 #d낚시 천재#k 인걸~?!");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 4) {
                if (cm.haveItem(4001187, 500) && cm.haveItem(4001188, 500) && cm.haveItem(4001189, 500)) {
                    if (cm.canHold(2430218)) {
                        cm.gainItem(4001187, -500);
                        cm.gainItem(4001188, -500);
                        cm.gainItem(4001189, -500);
                        cm.gainItem(2430218, 1);
                        cm.sendOk("#fs11##b폭풍 성장의 비약#k 을 교환 하였습니다.\r\n누구보다 #d빠른 성장#k 을 위해서 노력 하는군..! ");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 5) {
                if (cm.haveItem(4021022, 100)) {
                    cm.gainItem(4021022, -100);
                    cm.gainMeso(50000000);
                    cm.sendOk("#fs11##r5 천만 메소#k 를 교환 하였습니다.\r\n#b" + ServerConstants.serverName + "#k 의 #d만수르#k 가 되어보게나..!");
                    cm.dispose();
                } else {
                    cm.sendOk("#fs11##r이런 태초의 정수가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 6) {
                if (cm.haveItem(4001187, 500) && cm.haveItem(4001188, 500) && cm.haveItem(4001189, 500)) {
                    cm.gainItem(4001187, -500);
                    cm.gainItem(4001188, -500);
                    cm.gainItem(4001189, -500);
                    cm.gainMeso(150000000);
                    cm.sendOk("#fs11##r1 억 5 천만 메소#k 를 교환 하였습니다.\r\n#b" + ServerConstants.serverName + "#k 의 #d만수르#k 가 되어보게나..!");
                    cm.dispose();
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 7) {
                if (cm.haveItem(4001187, 5000) && cm.haveItem(4001188, 5000) && cm.haveItem(4001189, 5000)) {
                    cm.gainItem(4001187, -5000);
                    cm.gainItem(4001188, -5000);
                    cm.gainItem(4001189, -5000);
                    cm.gainMeso(1500000000);
                    cm.sendOk("#fs11##r15 억원 메소#k 를 교환 하였습니다.\r\n#b" + ServerConstants.serverName + "#k 의 #d만수르#k 가 되어보게나..!");
                    cm.dispose();
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 1111) {
                if (cm.haveItem(4021022, 100)) {
                    cm.gainItem(4021022, -100);
                    cm.gainMeso(50000000);
                    cm.sendOk("#fs11##r5 천만 메소#k 를 교환 하였습니다.\r\n#b" + ServerConstants.serverName + "#k 의 #d만수르#k 가 되어보게나..!");
                    cm.dispose();
                } else {
                    cm.sendOk("#fs11##r이런 태초의 정수가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 8) {
                if (cm.haveItem(4001187, 1000) && cm.haveItem(4001188, 1000) && cm.haveItem(4001189, 1000)) {
                    if (cm.canHold(1012329)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1012329, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 9) {
                if (cm.haveItem(4001187, 800) && cm.haveItem(4001188, 800) && cm.haveItem(4001189, 800)) {
                    if (cm.canHold(1672004)) {
                        cm.gainItem(4001187, -800);
                        cm.gainItem(4001188, -800);
                        cm.gainItem(4001189, -800);
                        cm.gainItemAllStat(1672004, 1, 50, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 10) {
                if (cm.haveItem(4001187, 800) && cm.haveItem(4001188, 800) && cm.haveItem(4001189, 800)) {
                    if (cm.canHold(2435795)) {
                        cm.gainItem(4001187, -800);
                        cm.gainItem(4001188, -800);
                        cm.gainItem(4001189, -800);
                        cm.gainItemAllStat(2435795, 1, 0, 0);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 106) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1102039)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1102039, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 107) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1072153)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1072153, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 108) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1012289)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1012289, 1, 200, 100);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 109) {
                if (cm.haveItem(4001187, 500) && cm.haveItem(4001188, 500) && cm.haveItem(4001189, 500)) {
                    if (cm.canHold(2430218)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItem(2430218, 1);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 207) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(5680222)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItem(5680222, 1);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 200) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1002186)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1002186, 1, 200, 100);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 201) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1082102)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1082102, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }


                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 202) {
                if (cm.haveItem(4001187, 3000) && cm.haveItem(4001188, 3000) && cm.haveItem(4001189, 3000)) {
                    if (cm.canHold(1113085)) {
                        cm.gainItem(4001187, -3000);
                        cm.gainItem(4001188, -3000);
                        cm.gainItem(4001189, -3000);
                        cm.gainItemAllStat(1113085, 1, 300, 100);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 203) {
                if (cm.haveItem(4001187, 3000) && cm.haveItem(4001188, 3000) && cm.haveItem(4001189, 3000)) {
                    if (cm.canHold(1114000)) {
                        cm.gainItem(4001187, -5000);
                        cm.gainItem(4001188, -5000);
                        cm.gainItem(4001189, -5000);
                        cm.gainItemAllStat(1114000, 1, 300, 100);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 204) {
                if (cm.haveItem(4001187, 5000) && cm.haveItem(4001188, 5000) && cm.haveItem(4001189, 5000)) {
                    if (cm.canHold(1113231)) {
                        cm.gainItem(4001187, -5000);
                        cm.gainItem(4001188, -5000);
                        cm.gainItem(4001189, -5000);
                        cm.gainItemAllStat(1113231, 1, 500, 200);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 205) {
                if (cm.haveItem(4001187, 5000) && cm.haveItem(4001188, 5000) && cm.haveItem(4001189, 5000)) {
                    if (cm.canHold(1182200)) {
                        cm.gainItem(4001187, -5000);
                        cm.gainItem(4001188, -5000);
                        cm.gainItem(4001189, -5000);
                        cm.gainItemAllStat(1182200, 1, 400, 200);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2000) {
                if (cm.haveItem(4001187, 3000) && cm.haveItem(4001188, 3000) && cm.haveItem(4001189, 3000)) {
                    if (cm.canHold(1003392)) {
                        cm.gainItem(4001187, -3000);
                        cm.gainItem(4001188, -3000);
                        cm.gainItem(4001189, -3000);
                        cm.gainItemAllStat(1003392, 1, 300, 300);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2000) {
                if (cm.haveItem(4001187, 3000) && cm.haveItem(4001188, 3000) && cm.haveItem(4001189, 3000)) {
                    if (cm.canHold(1003392)) {
                        cm.gainItem(4001187, -3000);
                        cm.gainItem(4001188, -3000);
                        cm.gainItem(4001189, -3000);
                        cm.gainItemAllStat(1003392, 1, 300, 300);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2001) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1022079)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1022079, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2002) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1092056)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1092056, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 20022) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1342069)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1342069, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2003) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1012379)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1012379, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2004) {
                if (cm.haveItem(4001187, 3000) && cm.haveItem(4001188, 3000) && cm.haveItem(4001189, 3000)) {
                    if (cm.canHold(1702119)) {
                        cm.gainItem(4001187, -3000);
                        cm.gainItem(4001188, -3000);
                        cm.gainItem(4001189, -3000);
                        cm.gainItemAllStat(1702119, 1, 300, 150);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2005) {
                if (cm.haveItem(4001187, 3000) && cm.haveItem(4001188, 3000) && cm.haveItem(4001189, 3000)) {
                    if (cm.canHold(1702118)) {
                        cm.gainItem(4001187, -3000);
                        cm.gainItem(4001188, -3000);
                        cm.gainItem(4001189, -3000);
                        cm.gainItemAllStat(1702118, 1, 300, 150);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 2006) {
                if (cm.haveItem(4001187, 3000) && cm.haveItem(4001188, 3000) && cm.haveItem(4001189, 3000)) {
                    if (cm.canHold(1702174)) {
                        cm.gainItem(4001187, -3000);
                        cm.gainItem(4001188, -3000);
                        cm.gainItem(4001189, -3000);
                        cm.gainItemAllStat(1702174, 1, 300, 150);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 109) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(2430218)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(2430218, 1, 200, 100);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }

                }
            } else if (selection == 110) {
                if (cm.haveItem(4001187, 2000) && cm.haveItem(4001188, 2000) && cm.haveItem(4001189, 2000)) {
                    if (cm.canHold(1032024)) {
                        cm.gainItem(4001187, -2000);
                        cm.gainItem(4001188, -2000);
                        cm.gainItem(4001189, -2000);
                        cm.gainItemAllStat(1032024, 1, 100, 50);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 11) {
                if (cm.haveItem(4001187, 800) && cm.haveItem(4001188, 800) && cm.haveItem(4001189, 800)) {
                    if (cm.canHold(1662003)) {
                        cm.gainItem(4001187, -800);
                        cm.gainItem(4001188, -800);
                        cm.gainItem(4001189, -800);
                        cm.gainItemAllStat(1662003, 1, 300, 300);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 12) {
                if (cm.haveItem(4001187, 500) && cm.haveItem(4001188, 500) && cm.haveItem(4001189, 500)) {
                    if (cm.canHold(1102369)) {
                        cm.gainItem(4001187, -500);
                        cm.gainItem(4001188, -500);
                        cm.gainItem(4001189, -500);
                        cm.gainItemAllStat(1102369, 1, 30, 30);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 13) {
                if (cm.haveItem(4001187, 500) && cm.haveItem(4001188, 500) && cm.haveItem(4001189, 500)) {
                    if (cm.canHold(1152090)) {
                        cm.gainItem(4001187, -500);
                        cm.gainItem(4001188, -500);
                        cm.gainItem(4001189, -500);
                        cm.gainItemAllStat(1152090, 1, 30, 30);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r장비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }

            } else if (selection == 100) {
                if (cm.haveItem(4001187, 500)) {
                    if (cm.canHold(4001188)) {
                        cm.gainItem(4001187, -500);
                        cm.gainItem(4001188, 250);
                        cm.sendOk("#fs11##d싱싱한 물고기는 항상 대기중이야 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 101) {
                if (cm.haveItem(4001187, 500)) {
                    if (cm.canHold(4001189)) {
                        cm.gainItem(4001187, -500);
                        cm.gainItem(4001189, 250);
                        cm.sendOk("#fs11##d싱싱한 물고기는 항상 대기중이야 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 102) {
                if (cm.haveItem(4001188, 500)) {
                    if (cm.canHold(4001187)) {
                        cm.gainItem(4001188, -500);
                        cm.gainItem(4001187, 250);
                        cm.sendOk("#fs11##d싱싱한 물고기는 항상 대기중이야 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 103) {
                if (cm.haveItem(4001188, 500)) {
                    if (cm.canHold(4001189)) {
                        cm.gainItem(4001188, -500);
                        cm.gainItem(4001189, 250);
                        cm.sendOk("#fs11##d싱싱한 물고기는 항상 대기중이야 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 104) {
                if (cm.haveItem(4001189, 500)) {
                    if (cm.canHold(4001187)) {
                        cm.gainItem(4001189, -500);
                        cm.gainItem(4001187, 250);
                        cm.sendOk("#fs11##d싱싱한 물고기는 항상 대기중이야 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 1112) {
                if (cm.haveItem(4021022, 200)) {
                    if (cm.canHold(2436225)) {
                        cm.gainItem(4021022, -200);
                        cm.gainItem(2436225, 1);
                        cm.sendOk("#fs11##d소비칸을 확인해봐 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 태초의 정수가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 1113) {
                if (cm.haveItem(4021022, 300)) {
                    if (cm.canHold(2433834)) {
                        cm.gainItem(4021022, -300);
                        cm.gainItem(2433834, 1);
                        cm.sendOk("#fs11##d소비칸을 확인해봐 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 태초의 정수가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 105) {
                if (cm.haveItem(4001189, 500)) {
                    if (cm.canHold(4001188)) {
                        cm.gainItem(4001189, -500);
                        cm.gainItem(4001188, 250);
                        cm.sendOk("#fs11##d싱싱한 물고기는 항상 대기중이야 ~ !#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 8000) {
                if (cm.haveItem(4001187, 1500) && cm.haveItem(4001188, 1500) && cm.haveItem(4001189, 1500)) {
                    if (cm.canHold(2049360)) {
                        cm.gainItem(4001187, -1500);
                        cm.gainItem(4001188, -1500);
                        cm.gainItem(4001189, -1500);
                        cm.gainItem(2049360, 3);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 8001) {
                if (cm.haveItem(4001187, 800) && cm.haveItem(4001188, 800) && cm.haveItem(4001189, 800)) {
                    if (cm.canHold(2049153)) {
                        cm.gainItem(4001187, -800);
                        cm.gainItem(4001188, -800);
                        cm.gainItem(4001189, -800);
                        cm.gainItem(2049153, 10);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 8002) {
                if (cm.haveItem(4001187, 300) && cm.haveItem(4001188, 300) && cm.haveItem(4001189, 300)) {
                    if (cm.canHold(2433019)) {
                        cm.gainItem(4001187, -300);
                        cm.gainItem(4001188, -300);
                        cm.gainItem(4001189, -300);
                        cm.gainItemAllStat(2433019, 1, 0, 0);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 8003) {
                if (cm.haveItem(4001187, 700) && cm.haveItem(4001188, 700) && cm.haveItem(4001189, 700)) {
                    if (cm.canHold(4021037)) {
                        cm.gainItem(4001187, -700);
                        cm.gainItem(4001188, -700);
                        cm.gainItem(4001189, -700);
                        cm.gainItemAllStat(4021037, 1, 0, 0);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 8004) {
                if (cm.haveItem(4001187, 1000) && cm.haveItem(4001188, 1000) && cm.haveItem(4001189, 1000)) {
                    if (cm.canHold(4031868)) {
                        cm.gainItem(4001187, -1000);
                        cm.gainItem(4001188, -1000);
                        cm.gainItem(4001189, -1000);
                        cm.gainItemAllStat(4031868, 1, 0, 0);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r기타칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            } else if (selection == 8005) {
                if (cm.haveItem(4001187, 700) && cm.haveItem(4001188, 700) && cm.haveItem(4001189, 700)) {
                    if (cm.canHold(2439653)) {
                        cm.gainItem(4001187, -700);
                        cm.gainItem(4001188, -700);
                        cm.gainItem(4001189, -700);
                        cm.gainItemAllStat(2439653, 1, 0, 0);
                        cm.sendOk("#fs11##d마음에 들길 바라네..!#k");
                        cm.dispose();
                    } else {
                        cm.sendOk("#fs11##r소비칸에 빈 공간이 없습니다.#k");
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("#fs11##r이런 물고기가 모자른거 같은데~?#k");
                    cm.dispose();
                }
            }
        }
    }
}