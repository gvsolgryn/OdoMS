


/*

제작 : 피스(qudtlstorl79@nate.com)

*/
importPackage(Packages.server);

var status = -1;
var sel = 0;
var HuntCoin1 = 4319999;
var HuntCoin10000 = 4319999;
var needcoin = 0;
var needcoincount = 0;
var uptear = 0;
var tearname = "";
var suc = 0;
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
        txt = "#fs11##b각종 스탯의 랭크#fc0xFF000000#를 올릴 수 있는 시스템이라네 \r\n원하는 항목을 골라보게나\r\n"
        txt += "#b※데미지,크뎀,보공은 100%확률로 성공하나 \r\n경험치,메소,드롭은 실패할수 있다네#n#k\r\n\r\n#b"
        txt += "#L1# 경험치 랭크\r\n"        
        txt += "#L5# 메소 레벨업\r\n"
        txt += "#L2# 드롭 레벨업\r\n"
        txt += "#L0# 데미지 레벨업\r\n"
        txt += "#L3# 크리 데미지 레벨업\r\n"
        txt += "#L4# 보스 공격력 레벨업\r\n"
        cm.sendSimple(txt);
    } else if (status == 1) {
        sel = selection;
        if (sel == 0) {
            //데미지 티어
            tearname = "DamageTear";
            effect = "데미지";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            damageup = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 5000;
                    damageup = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    damageup = 35;
                    suc = 100;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    damageup = 55;
                    suc = 100;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    damageup = 75;
                    suc= 100;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 25000;
                    damageup = 105;
                    suc = 100;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    damageup = 145;
                    suc = 100;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    damageup = 190;
                    suc = 100;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    damageup = 240;
                    suc = 100;
                    break;
            }

            txt = "#fs11##b현재 나의 데미지 랭크#fc0xFF000000# #e[" + mytear + "랭크]#n\r\n"
            txt += "#b다음 승급 랭크는#fc0xFF000000# #e[" + uptear + "랭크]#n\r\n"
            txt += "#b승급 가능한 최대 랭크는#fc0xFF000000# #e[8랭크]#n\r\n"
            txt += "승급한 랭크의 효과와\r\n승급에 필요한 #r아이템#fc0xFF000000#은 다음과 같다네\r\n\r\n";
            txt += "#r#e승급 성공 시 총 " + effect + " + #r#e" + damageup + "%#n\r\n#fc0xFF000000# #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "개#k\r\n\r\n"
            txt += "#fs12##fc0xFF000000#정말 #e승급#n을 진행 하겠나?";
            cm.sendYesNo(txt);
        } else if (sel == 1) {
            //경험치 티어
            tearname = "ExpTear";
            effect = "경험치";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 3;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 6000;
                    up = 10;
                    suc = 80;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 7000;
                    up = 23;
                    suc = 60;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 31;
                    suc= 50;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    up = 54;
                    suc = 45;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 30000;//2개필요
                    up = 84;//데미지
                    suc = 30;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;//2개필요
                    up = 124;//데미지
                    suc = 20;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;//2개필요
                    up = 184;//데미지
                    suc = 10;
                    break;
            }

            txt = "#fs11##b#e<" + effect + " 티어>#n#k\r\n\r\n"
            txt += "현재 나의 티어 : #e#r" + mytear + "#n#k티어\r\n"
            txt += "다음 승급 티어는 #e[" + uptear + "티어]#n이며\r\n"
            txt += "승급한 티어의 효과와\r\n승급에 필요한 #r아이템#k은 다음과 같습니다\r\n\r\n";
            txt += "" + effect + " + #r#e" + up + "%#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "개#k\r\n\r\n"
            txt += "#fs14#정말 #e승급#n을 진행 하시겠습니까?";
            cm.sendYesNo(txt);
        } else if (sel == 2) {
            //드롭 티어
            tearname = "DropTear";
            effect = "드롭";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 6000;
                    up = 40;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 8000;
                    up = 80;
                    suc = 80;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 9000;
                    up = 120;
                    suc = 70;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 150;
                    suc= 60;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 11000;
                    up = 180;
                    suc = 50;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 12000;
                    up = 220;
                    suc = 40;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 13000;
                    up = 260;
                    suc = 30;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    up = 300;
                    suc = 20;
                    break;
            }

            txt = "현재 나의 티어 : #e#r" + mytear + "#n#k티어\r\n"
            txt += "다음 승급 티어는 #e[" + uptear + "티어]#n이며\r\n"
            txt += "승급한 티어의 효과와\r\n승급에 필요한 #r아이템#k은 다음과 같습니다\r\n\r\n";
            txt += "" + effect + " + #r#e" + up + "%\r\n#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "개#k\r\n\r\n"
            txt += "#fs12#정말 #e승급#n을 진행 하시겠습니까?";
            cm.sendYesNo(txt);
        } else if (sel == 3) {
            //크뎀 티어
            tearname = "CridamTear";
            effect = "크리데미지";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 15;
                    suc = 100;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    up = 30;
                    suc = 100;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    up = 50;
                    suc= 100;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 25000;
                    up = 80;
                    suc = 100;
                    break;
                case 6:
                    needcoin = 4319999;//만개짜리코인
                    needcoincount = 30000;
                    up = 130;//데미지
                    suc = 100;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 200;//데미지
                    suc = 100;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 300;//데미지
                    suc = 100;
                    break;
            }

            txt = "#fs11##b현재 나의 크리데미지 랭크#k #e[" + mytear + "랭크]#n\r\n"
            txt += "#b다음 승급 랭크 #k#e[" + uptear + "랭크]#n\r\n"
            txt += "#b승급 가능한 최대 랭크는#k #e[8랭크]#n\r\n"
            txt += "승급한 랭크의 효과와\r\n승급에 필요한 #r아이템#k은 다음과 같습니다\r\n\r\n";
            txt += "#r#e승급 성공시 총 " + effect + " + #r#e" + up + "%#k\r\n#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "개#k\r\n\r\n"
            txt += "#fs12#정말 #e승급#n을 진행 하시겠습니까?";
            cm.sendYesNo(txt);
        } else if (sel == 4) {
            //보공 티어
            tearname = "BossdamTear";
            effect = "보스 공격력";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 35;
                    suc = 100;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 15000;
                    up = 55;
                    suc = 100;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 20000;
                    up = 75;
                    suc= 100;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 105;
                    suc = 100;
                    break;
                case 6:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 145;//데미지
                    suc = 100;
                    break;
                case 7:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 190;//데미지
                    suc = 100;
                    break;
                case 8:
                    needcoin = 4319999;
                    needcoincount = 30000;
                    up = 240;//데미지
                    suc = 100;
                    break;
            }

            txt = "#fs11##b현재 나의 보스 공격력 랭크#k #e[" + mytear + "랭크]#n\r\n"
            txt += "#b다음 승급 랭크 #k#e[" + uptear + "랭크]#n\r\n"
            txt += "#b승급 가능한 최대 랭크는#k #e[8랭크]#n\r\n"
            txt += "승급한 랭크의 효과와\r\n승급에 필요한 #r아이템#k은 다음과 같습니다\r\n\r\n";
            txt += "#r#e승급 성공시 총 " + effect + " + #r#e" + up + "%#k\r\n#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "개#k\r\n\r\n"
            txt += "#fs12#정말 #e승급#n을 진행 하시겠습니까?";
            cm.sendYesNo(txt);
        } else if (sel == 5) {
            //메소 티어
            tearname = "MesoTear";
            effect = "메소";
            if (cm.getPlayer().getKeyValue(9919, tearname) < 0) {
                cm.getPlayer().setKeyValue(9919, tearname, "0");
            }
            mytear = cm.getPlayer().getKeyValue(9919, tearname);
            uptear = mytear + 1;
            up = 0;
            switch (uptear) {
                case 1://1티어
                    needcoin = 4319999;
                    needcoincount = 3000;
                    up = 10;
                    suc = 100;
                    break;
                case 2:
                    needcoin = 4319999;
                    needcoincount = 4000;
                    up = 30;
                    suc = 90;
                    break;
                case 3:
                    needcoin = 4319999;
                    needcoincount = 5000;
                    up = 40;
                    suc = 80;
                    break;
                case 4:
                    needcoin = 4319999;
                    needcoincount = 8000;
                    up = 60;
                    suc= 70;
                    break;
                case 5:
                    needcoin = 4319999;
                    needcoincount = 10000;
                    up = 80;
                    suc = 60;
                    break;
                case 6:
                    needcoin = 4319999;//만개짜리코인
                    needcoincount = 12000;//2개필요
                    up = 120;//데미지
                    suc = 55;
                    break;
                case 7:
                    needcoin = 4319999;//만개짜리코인
                    needcoincount = 15000;//2개필요
                    up = 180;//데미지
                    suc = 50;
                    break;
                case 8:
                    needcoin = 4319999;//만개짜리코인
                    needcoincount = 20000;//2개필요
                    up = 300;//데미지
                    suc = 40;
                    break;
            }

            txt = "#fs11##b#e<" + effect + " 티어>#n#k\r\n\r\n"
            txt += "현재 나의 티어 : #e#r" + mytear + "#n#k티어\r\n"
            txt += "다음 승급 티어는 #e[" + uptear + "티어]#n이며\r\n"
            txt += "승급한 티어의 효과와\r\n승급에 필요한 #r아이템#k은 다음과 같습니다\r\n\r\n";
            txt += "" + effect + " + #r#e" + up + "%#n#k #i" + needcoin + "# #b#z" + needcoin + "# " + needcoincount + "개#k\r\n\r\n"
            txt += "#fs14#정말 #e승급#n을 진행 하시겠습니까?";
            cm.sendYesNo(txt);
        }
    } else if (status == 2) {
        if (!cm.haveItem(needcoin, needcoincount)) {
            cm.sendOk("승급에 필요한 아이템이 부족합니다.");
            cm.dispose();
            return;
        }
        cm.gainItem(needcoin, -needcoincount);
        if (Randomizer.isSuccess(suc)) {
            cm.getPlayer().setKeyValue(9919, tearname, "" + uptear + "");
            cm.sendOk("축하하네! 승급에 성공하였네");
            cm.dispose();
        } else {
            cm.sendOk("승급에 실패 하였습니다.");
            cm.dispose();
        }

    }
}
