importPackage(Packages.client);

var enter = "\r\n";
var seld = -1, seld2 = -1;

var n = 0;
var t = 0;

var jobs = [
    {'jobid': 112, 'jobname': "히어로", 'job': "모험가", 'stat': 1, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 122, 'jobname': "팔라딘", 'job': "모험가", 'stat': 1, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    //{'jobid': 132, 'jobname': "다크나이트", 'job': "모험가", 'stat': 1, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 212, 'jobname': "불독", 'job': "모험가", 'stat': 3, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 222, 'jobname': "썬콜", 'job': "모험가", 'stat': 3, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 232, 'jobname': "비숍", 'job': "모험가", 'stat': 3, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 312, 'jobname': "보우마스터", 'job': "모험가", 'stat': 2, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 322, 'jobname': "신궁", 'job': "모험가", 'stat': 2, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 332, 'jobname': "패스파인더", 'job': "모험가", 'stat': 2, 'sk': [80001152, 1281, 1297, 1298, 12, 73], 'uq': true, 'ujob': [301, 330, 331]},
    {'jobid': 412, 'jobname': "나이트로드", 'job': "모험가", 'stat': 4, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 422, 'jobname': "섀도어", 'job': "모험가", 'stat': 4, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 434, 'jobname': "듀얼블레이드", 'job': "모험가", 'stat': 4, 'sk': [80001152, 1281, 12, 73], 'uq': true, 'ujob': [400, 430, 431, 432, 433]},
    {'jobid': 512, 'jobname': "바이퍼", 'job': "모험가", 'stat': 1, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 522, 'jobname': "캡틴", 'job': "모험가", 'stat': 2, 'sk': [80001152, 1281, 12, 73], 'uq': false},
    {'jobid': 532, 'jobname': "캐논슈터", 'job': "모험가", 'stat': 1, 'sk': [80001152, 1281, 110, 109, 111, 1283, 12, 73], 'uq': true, 'ujob': [501, 530, 531]},
    {'jobid': 1112, 'jobname': "소울마스터", 'job': "시그너스", 'stat': 1, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000246, 10000012, 10000073], 'uq': false},
    //{'jobid': 1212, 'jobname': "플레임위자드", 'job': "시그너스", 'stat': 3, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000248, 10000012, 10000073], 'uq': false},
    //{'jobid': 1312, 'jobname': "윈드브레이커", 'job': "시그너스", 'stat': 2, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000247, 10000012, 10000073], 'uq': false},
    //{'jobid': 1412, 'jobname': "나이트워커", 'job': "시그너스", 'stat': 4, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000249, 10000012, 10000073], 'uq': false},
    //{'jobid': 1512, 'jobname': "스트라이커", 'job': "시그너스", 'stat': 1, 'sk': [10001244, 10000252, 80001152, 10001253, 10001254, 10001245, 10000250, 10000246, 10000012, 10000073], 'uq': false},
    //{'jobid': 5112, 'jobname': "미하일", 'job': "시그너스", 'stat': 1, 'sk': [50001214, 10000250, 10000074, 10000075, 50000012, 50000073], 'uq': false},
    {'jobid': 2112, 'jobname': "아란", 'job': "영웅", 'stat': 1, 'sk': [20000194, 20001295, 20001296, 20000012, 20000073], 'uq': false},
    {'jobid': 2217, 'jobname': "에반", 'job': "영웅", 'stat': 3, 'sk': [20010022, 20010194, 20011293, 20010012, 20010073], 'uq': true, 'ujob': [2200, 2211, 2214]},
    {'jobid': 2312, 'jobname': "메르세데스", 'job': "영웅", 'stat': 2, 'sk': [20020109, 20021110, 20020111, 20020112, 20020012, 20020073], 'uq': false},
    {'jobid': 2412, 'jobname': "팬텀", 'job': "영웅", 'stat': 4, 'sk': [20031208, 20040190, 20031203, 20031205, 20030206, 20031207, 20031209, 20031251, 20031260, 20030012, 20030073], 'uq': false},
    {'jobid': 2512, 'jobname': "은월", 'job': "영웅", 'stat': 1, 'sk': [20051284, 20050285, 20050286, 20050074, 20050012, 20050073], 'uq': false},
    {'jobid': 2712, 'jobname': "루미너스", 'job': "영웅", 'stat': 3, 'sk': [20040216, 20040217, 20040218, 20040219, 20040221, 20041222, 20040012, 20040073], 'uq': false},
    {'jobid': 14212, 'jobname': "키네시스", 'job': "영웅", 'stat': 3, 'sk': [140000291, 14200, 14210, 14211, 14212, 140001290, 140000012, 140000073], 'uq': false},
    {'jobid': 3212, 'jobname': "배틀메이지", 'job': "레지스탕스", 'stat': 3, 'sk': [30001281, 30000012, 30000073], 'uq': false},
    {'jobid': 3312, 'jobname': "와일드헌터", 'job': "레지스탕스", 'stat': 2, 'sk': [30001281, 30001062, 30001061, 30000012, 30000073], 'uq': false},
    {'jobid': 3512, 'jobname': "메카닉", 'job': "레지스탕스", 'stat': 2, 'sk': [30001281, 30001068, 30000227, 30000012, 30000073], 'uq': false},
    {'jobid': 3612, 'jobname': "제논", 'job': "레지스탕스", 'stat': 5, 'sk': [30001281, 30020232, 30020233, 30020234, 30020240, 30021235, 30021236, 30021237, 30020012, 30020073], 'uq': false},
    //{'jobid': 3112, 'jobname': "데몬슬레이어", 'job': "레지스탕스", 'stat': 1, 'sk': [30001281, 80001152, 30001061, 30010110, 30010185, 30010112, 30010111, 30010012, 30010073], 'uq': false},
    //{'jobid': 3122, 'jobname': "데몬어벤저", 'job': "레지스탕스", 'stat': 6, 'sk': [30001281, 80001152, 30001061, 30010110, 30010185, 30010242, 30010241, 30010230, 30010231, 30010232, 30010012, 30010073], 'uq': true, 'ujob': [3101, 3120, 3121]},
    {'jobid': 6112, 'jobname': "카이저", 'job': "노바", 'stat': 1, 'sk': [600000219, 60000222, 60001217, 60001216, 60001225, 60001005, 60001296, 60000012, 60000073, 60001218, 60000219], 'uq': false},
    //{'jobid': 6512, 'jobname': "엔젤릭버스터", 'job': "노바", 'stat': 2, 'sk': [60011216, 60010217, 60011218, 60011219, 60011220, 60011221, 60011222, 60011005, 60010012, 60010073], 'uq': false},
    //{'jobid': 6412, 'jobname': "카데나", 'job': "노바", 'stat': 4, 'sk': [60020216, 60021217, 60021005, 60020218, 60020012, 60020073], 'uq': false},
    //{'jobid': 15112, 'jobname': "아델", 'job': "레프", 'stat': 1, 'sk': [150020041, 150021000, 150020079, 150020006, 150020241, 151001004], 'uq': false},
    {'jobid': 15512, 'jobname': "아크", 'job': "레프", 'stat': 1, 'sk': [150010079, 150011005, 150011074, 150010241, 150010012, 150010073, 155101006], 'uq': false},
    {'jobid': 16412, 'jobname': "호영", 'job': "아니마", 'stat': 4, 'sk': [160001074, 160001075, 160001005, 160000000, 160000076, 160000012, 60000073, 164001004], 'uq': false},
    //{'jobid': 6312, 'jobname': "카인", 'job': "노바", 'stat': 1, 'sk': [80003018, 80003015], 'uq': false},
    {'jobid': 16212, 'jobname': "라라", 'job': "아니마", 'stat': 4, 'sk': [160011074, 160010000, 160010001, 160011075, 160011005], 'uq': false}];

var level = -1;
var coin = -1;
var hpmp = -1;

var final = [];
var finaljob;
var jrand = -1;
var ast = -1;

var price = 50000;

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

    if (!cm.haveItem(4310086, 1)) {
        cm.sendOk("#i4310086##b#z4310086##k이필요합니다.");
        cm.dispose();
        return;
    }

    if (status == 0) {
        hpmp = 1000 * n;
        var msg = "#e#b[별빛 직업변경]#n#k\r\n\r\n다른 직업으로 바꿔보고 싶진 않으신가요??#b" + enter;
        msg += "#L1#직업을 변경하고 싶습니다." + enter;
        msg += "#L2#직업 변경 주의사항"+enter;
        cm.sendSimple(msg);

    } else {
        seld = seld == -1 ? sel : seld;
        switch (seld) {
            case 1:
                if (status == 1) {
                    if ((Math.floor(cm.getPlayer().getJob() % 10) != 2 && Math.floor(cm.getPlayer().getJob() % 10) != 4 && Math.floor(cm.getPlayer().getJob() % 10) != 7) || cm.getPlayer().getLevel() < 200) {
                        cm.sendOk("직업변경을 하기 위해선 4차전직(5차)을 완료한 상태여야 하며, 레벨이 200 이상인 캐릭터만 가능합니다.");
                        cm.dispose();
                        return;
                    }
                    var msg = "원하시는 직업을 선택해주세요." + enter;
                    for (i = 0; i < jobs.length; i++) {
                        msg += "#b#L" + i + "#" + jobs[i]['jobname'] + "#로 직업변경" + enter;
                    }
                    cm.sendSimple(msg);
                } else if (status == 2) {
                    seld2 = sel;
                    var msg = "직업변경을 하기 전에 다음 유의사항을 필독해주세요!" + enter;
                    msg += "직접 선택한 직업(#b#fs11#" + jobs[seld2]['jobname'] + "#k#fs12#)을(를) 갖고 돌아가게 됩니다." + enter;
                    msg += "이 시스템에 관련된 복구를 비롯한 모든 문의는 받지 않습니다." + enter;
                    msg += "정말 이 직업을 하실 것이 아니라면 '아니오'를 누르고 다시 선택해주세요." + enter;
                    msg += enter + "#fs12##b동의#k하신다면 '예'를 눌러주세요.";
                    cm.sendYesNo(msg);
                } else if (status == 3) {
                    if (!cm.haveItem(4310086, 1)) {
                        cm.sendOk("#i4310086##b#z4310086##k이(가) 필요합니다.");
                        cm.dispose();
                        return;
                    }
                    tempjob = jobs[seld2];
                    cm.gainItem(4310086, -1);
                    changeJobscript(tempjob, 1);
                    Packages.handling.channel.handler.MatrixHandler.gainMatrix(cm.getPlayer());
                    Packages.handling.channel.handler.MatrixHandler.gainVCoreLevel(cm.getPlayer());
                    cm.sendOk("직업 변경이 완료되었습니다.");
                    cm.dispose();
                }
                break;
                case 2:
                    if (status == 1) {
                        var msg = "                     #fs11##※직업 변경 주의사항※"+enter+enter;
                        msg += "#b자유전직 코인으로 원하는 직업을 선택하실 수 있습니다."+enter+enter;
                        msg += "#b직업변경시 심볼의 스탯은 변경되지 않습니다."+enter+enter;
                        msg += "#b직업 변경후 @스킬마스터로 스킬을 모두 마스터해주세요."+enter+enter;
                        msg += "#b5차 슬롯강화 및 기존 5차코어들이 모두 초기화 됩니다."+enter+enter;
                        msg += "#r경험의 코어젬스톤x500 지급됩니다."+enter;
                        msg += "#r[후원]봉인된 제네시스무기상자x1 지급됩니다."+enter;
                        cm.sendOk(msg);
                        cm.dispose();
                    }
                    break;
        }
    }
}

function baseSkill() {
    switch (cm.getJob()) {
        case 2100:
            cm.teachSkill(20001295, 1, 1);
            break;
        case 6100:
            cm.teachSkill(60000219, 1, 1);
            cm.teachSkill(60001217, 1, 1);
            cm.teachSkill(60001216, 1, 1);
            cm.teachSkill(60001218, 1, 1);
            cm.teachSkill(60001219, 1, 1);
            cm.teachSkill(60001225, 1, 1);
            break;
        case 2700:
            cm.teachSkill(27000106, 5, 5);
            cm.teachSkill(27000207, 5, 5);
            cm.teachSkill(27001201, 20, 20);
            cm.teachSkill(27001100, 20, 20);
            break;
        case 2500:
            cm.teachSkill(20051284, 30, 30);
            cm.teachSkill(20050285, 30, 30);
            cm.teachSkill(20050286, 30, 30);
            cm.teachSkill(25001000, 30, 30);
            cm.teachSkill(25001002, 30, 30);
            cm.teachSkill(25000003, 30, 30);
            break;
        case 1100:
        case 1200:
        case 1300:
        case 1400:
        case 1500:
            cm.teachSkill(10001251, 1, 1);
            cm.teachSkill(10001252, 1, 1);
            cm.teachSkill(10001253, 1, 1);
            cm.teachSkill(10001254, 1, 1);
            cm.teachSkill(10001255, 1, 1);
            break;
        case 14200:
            cm.teachSkill(140000291, 6, 6);
            break;
        case 15200:
            cm.teachSkill(150000079, 1, 1);
            cm.teachSkill(150011005, 1, 1);
            break;
        case 16400:
            cm.teachSkill(160000001, 1, 1);
            break;
        case 3500:
            cm.teachSkill(30001068, 1, 1);
            break;
    }
}

function symbol(j) {
    var inv = cm.getInventory(-1);
    for (i = -1600; i > -1605; i--) {
        item = cm.getInventory(-1).getItem(i);
        if (item == null) {
            continue;
        }

        if (Math.floor(item.getItemId() / 1000) != 1712) {
            continue;
        }

        ial = item.getArcLevel();
        var normal = 100 * (ial + 2);
        var zen = 39 * (ial + 2);
        var dev = 175 * (ial + 2);

        item.setStr(0);
        item.setDex(0);
        item.setInt(0);
        item.setLuk(0);
        item.setHp(0);

        var stat = (j >= 1 && j <= 4) ? normal : j == 5 ? zen : dev;
        switch (j) {
            case 1:
                item.setStr(stat);
                break;
            case 2:
                item.setDex(stat);
                break;
            case 3:
                item.setInt(stat);
                break;
            case 4:
                item.setLuk(stat);
                break;
            case 5:
                item.setStr(stat);
                item.setDex(stat);
                item.setLuk(stat);
                break;
            case 6:
                item.setHp(stat);
                break;
        }
        cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIPPED);
    }

    var inv = cm.getInventory(1);
    for (i = 0; i < inv.getSlotLimit(); i++) {
        item = cm.getInventory(1).getItem(i);
        if (item == null) {
            continue;
        }

        if (Math.floor(item.getItemId() / 1000) != 1712) {
            continue;
        }

        ial = item.getArcLevel();
        var normal = 100 * (ial + 2);
        var zen = 39 * (ial + 2);
        var dev = 175 * (ial + 2);

        item.setStr(0);
        item.setDex(0);
        item.setInt(0);
        item.setLuk(0);
        item.setHp(0);

        var stat = (j >= 1 && j <= 4) ? normal : j == 5 ? zen : dev;
        switch (j) {
            case 1:
                item.setStr(stat);
                break;
            case 2:
                item.setDex(stat);
                break;
            case 3:
                item.setInt(stat);
                break;
            case 4:
                item.setLuk(stat);
                break;
            case 5:
                item.setStr(stat);
                item.setDex(stat);
                item.setLuk(stat);
                break;
            case 6:
                item.setHp(stat);
                break;
        }
        cm.getPlayer().forceReAddItem(item, Packages.client.inventory.MapleInventoryType.EQUIPPED);
    }
}

function changeJobscript(tjob, jt) {
    if (cm.getPlayer().getJob() == 3122) {
        resetStatsDV();
    } else {
        cm.getPlayer().resetStatDonation((cm.getPlayer().getKeyValue(1912211, "timerf") - 1), cm.getPlayer().getKeyValue(1912211, "timerft"));
    }

    jid = tjob['jobid'];
    cm.changeJob(jid);
    cm.clearSkills();
    cm.getPlayer().getCore().clear();
    cm.getPlayer().getStolenSkills().clear();
    cm.getPlayer().setKeyValue(1477, "count", "0");

    for (i = 0; i < cm.getPlayer().getMatrixs().size(); i++) {
        cm.getPlayer().getMatrixs().get(i).setLevel(0);
    }

    if (tjob['uq']) {
        for (i = 0; i < tjob['ujob'].length; i++)
            cm.getPlayer().SkillMasterJob(tjob['ujob'][i]);
    } else {
        cm.getPlayer().SkillMasterJob(Math.floor(cm.getPlayer().getJob() / 100) * 100);
        cm.getPlayer().SkillMasterJob(cm.getPlayer().getJob() - (cm.getPlayer().getJob() % 10));
        cm.getPlayer().SkillMasterJob(cm.getPlayer().getJob() - (cm.getPlayer().getJob() % 10) + 1);
    }

    cm.getPlayer().SkillMasterByJob();
    cm.gainItem(2631527, 500);
    cm.gainItem(2439614, 1);

    baseSkill();
    symbol(tjob['stat']);

    for (i = 0; i < tjob['sk'].length; i++) {
        cm.teachSkill(tjob['sk'][i], 30, 30);
    }

    if (jid == 6500) {
        cm.getPlayer().setGender(1);
    }

    cm.getPlayer().reloadChar();
}