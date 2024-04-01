﻿var bannedhair = [30070, 30071, 30072, 30073, 30074, 30075, 30076, 30077, 30080, 30081, 30082, 30083, 30084, 30085, 30086, 30087, 39000, 39010, 39020, 39030, 39040, 39050, 39060, 39070, 39080, 39100, 39110, 39120, 39130, 39140, 39150, 39160, 39170, 39180, 39190, 39200, 39210, 39220, 39230, 39240, 39250, 39260, 39270, 39280, 39290, 39300, 39310, 39320, 39330, 39340, 39350, 39360, 39370, 39380, 39390, 39400, 39410, 39420, 39430, 39440, 39450, 39460];
var bannedFace = [22100, 22200, 22300, 22400, 22500, 22600, 22700, 22800];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
        return;
    }
    if (mode != 1) {
        cm.dispose();
        return;
    } else {
        status++;
    }
    if (status == 0) {
        var msg = "";
        msg += "#L4##e검색캐시#n\r\n";
        cm.sendSimple(msg);

    } else if (status == 1) {
        selStr = "";
        codyList = [];
        seld = selection;
        gColor = !cm.getPlayer().getGender() ? "#fs11##d여성" : "#fs11##b남성";

        switch (seld) {
            case 1:
                selStr += "#e#r[캐릭터 헤어 변경하기]#b#n\r\n";
                selStr += "#L1##fs11##fMap/MapHelper.img/minimap/match# 헤어 리스트 A#l#L2##fMap/MapHelper.img/minimap/match# 헤어 리스트 B#l\r\n";
                selStr += "#L3##fMap/MapHelper.img/minimap/match# 헤어 리스트 C#l#L4##fMap/MapHelper.img/minimap/match# 헤어 리스트 D #e#r(최신&자작헤어)#l#k\r\n\r\n\r\n";
                selStr += "#fs12#";
                selStr += "#e#r[검색 헤어 이용하기]#n#k\r\n";
                selStr += "#fs11#";
                selStr += "#L100##fMap/MapHelper.img/minimap/match# #b검색헤어 이용하기#l#k\r\n\r\n\r\n";
                if (cm.getPlayer().getAndroid() != null) {
                    selStr += "#e#r#fs12#[안드로이드 헤어 변경하기]#k#b#n\r\n";
                    selStr += "#L12##fs11##fMap/MapHelper.img/minimap/match# 헤어 리스트 A#l#L13##fMap/MapHelper.img/minimap/match# 헤어 리스트 B#l\r\n";
                    selStr += "#L14##fMap/MapHelper.img/minimap/match# 헤어 리스트 C#l#L15##fMap/MapHelper.img/minimap/match# 헤어 리스트 D #e#r(최신 헤어)#l\r\n";
                }
                break;
            case 2:
                selStr += "#e#r[캐릭터 성형]#b#n\r\n";
                selStr += "#e #n#L5##fs11##fMap/MapHelper.img/minimap/anothertrader# 성형 리스트 A#l#L6##fMap/MapHelper.img/minimap/anothertrader# 성형 리스트 B #e#r(최신 성형)#k#l\r\n\r\n\r\n";
                selStr += "#fs12#";
                selStr += "#e#r[검색 성형 이용하기]#n#k\r\n";
                selStr += "#fs11#";
                selStr += "#L200##fMap/MapHelper.img/minimap/anothertrader# #b검색성형 이용하기#l#k\r\n\r\n\r\n";
                if (cm.getPlayer().getAndroid() != null) {
                    selStr += "#e#r#fs12#[안드로이드 성형]#k#b#n\r\n#fs11#";
                    selStr += "#e #n#L16##fMap/MapHelper.img/minimap/anothertrader# 성형 리스트 A#l#L17##fMap/MapHelper.img/minimap/anothertrader# 성형 리스트 B #e#r(최신 성형)#l\r\n";
                }
                break;
            case 3:
                selStr += "#e#r[캐릭터 COLOR]#b#n\r\n";
                selStr += "#fs11##L8##fMap/MapHelper.img/minimap/rune# #e일반 염색#n하기\r\n#l";
                selStr += "#L21##fMap/MapHelper.img/minimap/rune# #e믹스 염색#n하기\r\n#l";
                selStr += "#L9##fMap/MapHelper.img/minimap/rune# #e피부색#n 변경하기#l#k\r\n ";
                if (cm.getPlayer().getAndroid() != null) {
                    selStr += "\r\n\r\n#e#r[안드로이드 COLOR]#b#n\r\n";
                    selStr += "#L19##fMap/MapHelper.img/minimap/rune# #b#e안드로이드#n#b 일반 염색#n하기\r\n#l";
                    selStr += "#L18##fMap/MapHelper.img/minimap/rune# #b#e안드로이드#n#b 렌즈색#n 변경하기\r\n#l";
                    selStr += "#L20##fMap/MapHelper.img/minimap/rune# #b#e안드로이드#n#b 피부색#n 변경하기#l#b\r\n";
                }
                break;
            case 4:
                selStr += "#L24##fMap/MapHelper.img/minimap/party# #e검색 캐시#n 이용하기#l\r\n";
                break;
        }

        cm.sendSimpleS(selStr, 4);

    } else if (status == 2) {
        if (selection == 100) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 3003273, "검색헤어");
	return;
        }

        if (selection == 200) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 3003273, "검색성형");
	return;
        }

        if (selection == 21) {
            for (i = 0; i < bannedhair.length; i++) {
                if (cm.getPlayer().getHair() == bannedhair[i]) {
                    cm.sendOk("해당 헤어는 염색이 불가능 합니다.");
                    cm.dispose();
                    return;
                }
            }
        }

        if (selection == 7) {
            for (i = 0; i < bannedFace.length; i++) {
                if (cm.getPlayer().getHair() == bannedFace[i]) {
                    cm.sendOk("해당 얼굴은 렌즈 선택이 불가능 합니다.");
                    cm.dispose();
                    return;
                }
            }
        }

        SEL_00 = selection;
        Gender = cm.getPlayer().getGender();

        if (SEL_00 >= 12 && SEL_00 <= 17) {
            Gender = cm.getPlayer().getAndroid().getGender();
        }

        if (SEL_00 == 11) {
            cm.dispose();
            cm.openNpc(1012117);
            return;
        }

        if (SEL_00 == 21) {
            cm.dispose();
            cm.openNpc(9000161);
            return;
        }

        if (SEL_00 == 22) {
            cm.dispose();
            cm.openShop(11);
            return;
        }

        if (SEL_00 == 24) {
            cm.dispose();
            cm.openNpcCustom(cm.getClient(), 1012124, "Itemsearch");
            return;
        }

        if (SEL_00 == 10) {
            if (cm.getPlayer().getJob() == 10112) {
                cm.sendOkS("제로 직업군은 성전환을 할 수 없습니다.", 4);
                cm.dispose();
                return;
            }
            cm.sendYesNoS("#e" + gColor + "#n#k으로 성전환을 하시겠습니까? 더 이상 해당 성별에 맞는 장비를 착용할 수 없습니다.", 4);
        }

        Beauty = SEL_00 < 2 ? "#e헤어#n를" : SEL_00 == 2 ? "#e얼굴#n을" : "#e색깔#n을";
        selStr = "#fn돋움##fc0xFFFFFFFF#지금의 " + Beauty + " 전혀 새로운 스타일로 바꿔 줄 수 있지. 지금 모습이 지겨워 졌다면 바꾸고 싶은 헤어를 천천히 고민해 봐";

        switch (SEL_00) {
            case 1:
            case 12:
                if (!Gender) {
                    codyList = [30000, 30020, 30030, 30040, 30050, 30060, 30100, 30110, 30120, 30130, 30140, 30150, 30160, 30170, 30180, 30190, 30200, 30210, 30220, 30230, 30240, 30250, 30260, 30270, 30280, 30290, 30300, 30310, 30320, 30330, 30340, 30350, 30360, 30370, 30400, 30410, 30420, 30440, 30450, 30460, 30470, 30480, 30490, 30510, 30520, 30530, 30540, 30560, 30570, 30590, 30610, 30620, 30630, 30640, 30650, 30660, 30670, 30680, 30700, 30710, 30730, 30760, 30770, 30790, 30800, 30810, 30820, 30830, 30840, 30850, 30860, 30870, 30880, 30910, 30930, 30940, 30950, 33030, 33060, 33070, 33080, 33090, 33110, 33120, 33130, 33150, 33170, 33180, 33190, 33210, 33220, 33250, 33260, 33270, 33280, 33310, 33330, 33350, 33360, 33370];
                } else {
                    codyList = [31000, 31010, 31020, 31030, 31040, 31050, 31060, 31070, 31080, 31090, 31100, 31110, 31120, 31130, 31140, 31150, 31160, 31170, 31180, 31190, 31200, 31210, 31220, 31230, 31240, 31250, 31260, 31270, 31280, 31290, 31300, 31310, 31320, 31330, 31340, 31410, 31420, 31440, 31450, 31460, 31470, 31480, 31490, 31510, 31520, 31530, 31540, 31550, 31560, 31590, 31610, 31620, 31630, 31640, 31650, 31670, 31680, 31690, 31700, 31710, 31720, 31740, 31750, 31780, 31790, 31800, 31810, 31820, 31840, 31850, 31860, 31880, 31890, 31910];
                }
                break;
            case 2:
            case 13:
                if (!Gender) {
                    codyList = [33380, 33390, 33400, 33410, 33430, 33440, 33450, 33460, 33480, 33500, 33510, 33520, 33530, 33550, 33580, 33590, 33600, 33610, 33620, 33630, 33640, 33660, 33670, 33680, 33690, 33700, 33710, 33720, 33730, 33740, 33750, 33760, 33770, 33780, 33790, 33800, 33810, 33820, 33830, 33930, 33940, 33950, 33960, 33990, 35000, 35010, 35020, 35030, 35040, 35050, 35060, 35070, 35080, 35090, 35100, 35150, 35180, 35190, 35200, 35210, 35250, 35260, 35280, 35290, 35300, 35310, 35330, 35350, 35360, 35420, 35430, 35440, 35460, 35470, 35480, 35490, 35500, 35510, 35520, 35530, 35540, 35550, 35560, 35570, 35600, 35620, 35630, 35640, 35650, 35660, 35680, 35690, 35710, 35720, 35780, 35790, 35950, 35960];
                } else {
                    codyList = [31920, 31930, 31940, 31950, 31990, 34040, 34070, 34080, 34090, 34100, 34110, 34120, 34130, 34140, 34150, 34160, 34170, 34180, 34190, 34210, 34220, 34230, 34240, 34250, 34260, 34270, 34310, 34320, 34330, 34340, 34360, 34370, 34380, 34400, 34410, 34420, 34430, 34440, 34450, 34470, 34480, 34490, 34510, 34540, 34560, 34590, 34600, 34610, 34620, 34630, 34640, 34660, 34670, 34680, 34690, 34700, 34710, 34720, 34730, 34740, 34750, 34760, 34770, 34780, 34790, 34800, 34810, 34820, 34830, 34840, 34850, 34860, 34870, 34880, 34900, 34910, 34940, 34950, 34960, 34970, 38660, 38670, 38680, 38690, 38700, 38730, 38740, 38750, 38760, 38800, 38810, 38820, 38840, 38880, 38910, 38940, 39090, 41080, 37000, 37010, 37020, 37030, 37040, 37060, 37070, 37080, 37090, 37100, 37110, 37120, 37130, 37140, 37190, 37210, 37220, 37230, 37240];
                }
                break;
            case 3:
            case 14:
                if (!Gender) {
                    codyList = [36010, 36020, 36030, 36040, 36050, 36070, 36080, 36090, 36100, 36130, 36140, 36150, 36160, 36170, 36180, 36190, 36200, 36210, 36220, 36230, 36240, 36250, 36300, 36310, 36330, 36340, 36350, 36380, 36390, 36400, 36410, 36420, 36430, 36440, 36450, 36460, 36470, 36480, 36510, 36520, 36530, 36560, 36570, 36580, 36590, 36620, 36630, 36640, 36650, 36670, 36680, 36690, 36700, 36710, 36720, 36730, 36740, 36750, 36760, 36770, 36780, 36790, 36800, 36810, 36820, 36830, 36840, 36850, 36860, 36900, 36910, 36920, 36940, 36950, 36980, 36990, 40000, 40010, 40020, 40050, 40060];
                } else {
                    codyList = [37250, 37260, 37280, 37300, 37310, 37320, 37340, 37370, 37380, 37400, 37450, 37460, 37490, 37500, 37510, 37520, 37530, 37560, 37570, 37580, 37610, 37620, 37630, 37640, 37650, 37660, 37670, 37680, 37690, 37700, 37710, 37720, 37730, 37740, 37750, 37760, 37770, 37780, 37790, 37800, 37810, 37820, 37830, 37840, 37850, 37860, 37880, 37910, 37920, 37940, 37950, 37960, 37970, 37980, 37990, 38000, 38010, 38020, 38030, 38040, 38050, 38060, 38070, 38090, 38100, 38110, 38120, 38130, 38270, 38280, 38290, 38300, 38310, 38380, 38390, 38400, 38410, 38420, 38430, 38440, 38460, 38470, 38490, 38520, 38540, 38550, 38560, 38570, 38580, 38590, 38600, 38610, 38620, 38630, 38640, 38650, 41090, 41100, 41110, 41120, 41150, 41160, 41200, 41220, 41340, 41350, 41360, 41370, 41380, 41390, 41400, 41440, 41470, 41480, 41490, 41510, 41520];
                }
                break;
            case 4:
            case 15:
                if (!Gender) {
                    codyList = [46940, 46970, 46980, 47000, 47030, 47040, 47070, 47080, 47090, 47260, 47270, 47280, 47310, 47320, 47330, 47340, 47350, 47360, 47370, 47390, 47400, 47430, 47440, 47450, 47460, 47520, 47530, 47540, 47550, 47560, 47570, 47580, 47590, 47600, 47610, 47620, 47630, 47640, 47650, 47660, 47670, 47680, 47690, 47700, 47710, 47730, 47740, 47750, 47760, 47770, 47780, 47790, 47800, 48130, 48140, 48190, 48210, 48220, 48320, 48330, 48340, 48350, 48360, 48370, 48380, 48410, 48430, 48450, 48480, 48490, 48500, 48510, 48520, 48530, 48540, 48550, 48560, 48570, 48580, 48640, 48650, 48660, 48730, 48740, 48750, 48770, 48780, 48790, 48810, 48840, 48850, 48900, 48960, 48970, 48990, 60000, 60060, 60070, 60080, 60090, 60100, 60110, 60120, 60130, 60140, 60150, 60160, 60170, 60180, 60190, 60200, 60210, 60220, 60230, 60240, 60250, 60260, 60270, 60280, 60290, 60300, 60310, 60320, 60330, 61050, 61060, 61070, 61080, 61090, 61100, 61110, 61120, 61130, 61140, 61150, 61160, 61170, 61180, 61190, 61200, 61210, 61220, 61230, 61240, 61250];
                } else {
                    codyList = [41560, 41570, 41590, 41600, 41700, 41720, 41730, 41740, 41750, 41850, 41860, 41880, 41890, 41920, 41930, 41950, 44010, 44120, 44130, 44320, 44200, 44330, 44460, 41900, 44360, 44290, 44470, 44480, 44490, 44500, 44530, 41870, 44650, 44770, 44850, 44780, 44790, 44802, 44900, 41940, 44830, 44840, 44940, 44950, 47000, 47040, 47020, 47010, 47030, 47070, 47090, 47310, 47270, 47280, 47320, 47330, 47350, 47370, 47360, 47340, 48020, 48050, 48060, 48070, 48080, 47530, 47540, 47460, 47430, 47400, 47400, 47430, 47460, 41140, 46220, 48210, 48320, 48340, 48330, 48350, 48360, 48370, 48380, 48430, 48490, 48500, 48510, 48520, 48530, 48540, 48550, 48560, 48570, 48580, 48640, 48650, 48730, 48740, 47260, 47520, 48130, 48140, 48410, 48480, 48660, 48750, 48780, 48810, 48840, 48850, 47550, 47580, 47670, 47690, 48520, 47570, 47640, 47590, 47560, 47700, 47620, 47610, 47650, 47680, 47630, 47710, 61260, 61270, 61280, 61290, 61300, 61310, 61320, 61330, 61340, 61350, 61360, 61370, 61380, 61390, 61400, 61410, 61420, 61430, 61440, 61450, 61460, 61470, 61480, 61490, 61500, 61510, 62000, 62010, 62020];
                }
                break;
            case 5:
            case 16:
                if (!Gender) {
                    codyList = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20009, 20011, 20012, 20013, 20014, 20015, 20016, 20017, 20018, 20020, 20021, 20022, 20025, 20027, 20028, 20029, 20030, 20031, 20032, 20036, 20037, 20040, 20043, 20044, 20045, 20046, 20047, 20048, 20049, 20050, 20051, 20052, 20053, 20055, 20056, 20057, 20058, 20059, 20060, 20061, 20062, 20063, 20064, 20065, 20066, 20067, 20068, 20069, 20070, 20074, 20075, 20076, 20077, 20080, 20081, 20082, 20083, 20084, 20085, 20086, 20087, 20088, 20089, 20090, 20093, 20094, 20095, 20097, 20098, 20099, 20110, 23000, 23001, 23002, 23003, 23005, 23006, 23008, 23010, 23011, 23012];
                } else {
                    codyList = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21009, 21011, 21012, 21013, 21014, 21015, 21016, 21017, 21020, 21021, 21023, 21024, 21026, 21027, 21028, 21029, 21030, 21031, 21033, 21036, 21038, 21041, 21042, 21043, 21044, 21045, 21048, 21050, 21052, 21053, 21056, 21058, 21059, 21061, 21062, 21063, 21065, 21073, 21074, 21075, 21077, 21078, 21079, 21080, 21081, 21082, 21083, 21084, 21085, 21089, 21090, 21091, 21092, 21093, 21094, 21095, 21096, 21097, 21098, 24002, 24003, 24004];
                }
                break;
            case 6:
            case 17:
                if (!Gender) {
                    codyList = [23015, 23016, 23017, 23018, 23019, 23020, 23023, 23024, 23025, 23026, 23027, 23028, 23029, 23031, 23032, 23033, 23035, 23038, 23039, 23040, 23041, 23042, 23043, 23044, 23053, 23054, 23056, 23057, 23060, 23061, 23062, 23063, 23067, 23068, 23069, 23072, 23073, 23074, 23075, 23079, 23080, 23081, 23082, 23083, 23084, 23085, 23086, 23087, 23088, 23089, 23090, 23091, 23092, 23095, 23096, 23097, 23099, 24061, 24098, 25006, 25007, 25011, 25014, 25016, 25017, 25021, 25022, 25023, 25024, 25025, 25027, 25033, 25058, 25057, 25049, 25053, 25029, 25020, 25043, 25044, 25063, 25062, 25050, 25080, 25079, 25083, 25055, 25085, 25088, 25089, 25091, 25073, 25075, 25084, 25099, 27010, 27011, 27038, 27039, 25093, 27008, 27006, 27022, 27006, 27008, 27037, 27052, 27064, 27065, 27066, 27070, 28079, 23006, 27025, 27073, 27076, 27077, 27078, 27079, 27080, 27081, 27082, 27083, 27084, 27085, 27086, 27098];
                } else {
                    codyList = [24007, 24008, 24011, 24012, 24014, 24015, 24018, 24020, 24021, 24022, 24023, 24027, 24031, 24035, 24037, 24038, 24039, 24041, 24050, 24055, 24058, 24060, 24067, 24068, 24071, 24072, 24073, 24077, 24080, 24081, 24084, 24087, 24088, 24091, 24097, 24099, 25000, 25008, 25015, 26003, 26004, 26005, 26009, 26014, 26017, 26022, 26023, 26027, 26028, 26029, 26030, 26031, 26032, 26062, 26061, 26053, 26056, 26034, 26026, 26046, 26067, 26066, 26054, 26086, 26085, 25155, 26089, 26091, 26094, 26095, 26097, 28011, 26076, 26079, 28013, 26096, 28016, 28017, 28044, 26099, 28014, 28008, 28027, 28008, 28014, 28043, 28057, 28070, 28071, 28072, 28078, 28079, 28095, 28030, 28082, 28085, 28086, 28087, 28088, 28089, 28090, 28091, 28092, 28093, 28094, 28096, 2709827106, 27107, 27108, 27109, 27110, 27111, 27117, 27118, 27119, 27120, 27122, 27125, 27135, 27137, 27138, 27139, 27144, 27145, 27152, 27164, 27165, 27166, 27167, 27169, 27170, 27173, 27174, 27176, 27177, 27178, 27179, 27180, 27181, 27182, 27183, 27184, 27185, 27186, 27187, 27189, 27192, 27196, 27198, 27206, 27207, 27208, 27209, 27210, 27211, 27217, 27218, 27219, 27220, 27222, 27225, 27235, 27237, 27238, 27239, 27244, 27245, 27252, 27264, 27265, 27266, 27267, 27269, 27270, 27273, 27274, 27276, 27277, 27278, 27279, 27280, 27281, 27282, 27283, 27284, 27285, 27286, 27287, 27289];
                }
                break;
            case 7:
            case 18:
                for (i = 0; i < 8; i++) {
                    codyList.push(Math.floor((SEL_00 >= 12 ? cm.getPlayer().getAndroid().getFace() : cm.isAngelicBuster() ? cm.getPlayer().getDressup() ? cm.getPlayer().getSecondFace() : cm.getPlayer().getFace() : cm.getPlayer().getFace()) / 1000) * 1000 + ((SEL_00 >= 12 ? cm.getPlayer().getAndroid().getFace() : cm.isAngelicBuster() ? cm.getPlayer().getDressup() ? cm.getPlayer().getSecondFace() : cm.getPlayer().getFace() : cm.getPlayer().getFace()) % 100) + i * 100);
                }
                break;
            case 8:
            case 19:
                for (i = 0; i < 8; i++) {
                    codyList.push(Math.floor((SEL_00 >= 12 ? cm.getPlayer().getAndroid().getHair() : cm.isAngelicBuster() ? cm.getPlayer().getDressup() ? cm.getPlayer().getSecondHair() : cm.getPlayer().getHair() : cm.getPlayer().getHair()) / 10) * 10 + i);
                }
                break;
            case 9:
            case 20:
                codyList = [0, 1, 2, 3, 4, 9, 10, 11, 12, 13, 15, 16, 18, 19];
                break;
            default:
                break;
        }

        if (SEL_00 < 10) {
            cm.sendStyle(selStr, codyList);
        } else if (SEL_00 >= 12) {
            cm.askAvatarAndroid(selStr, codyList);
        }

    } else if (status == 3) {
        if (SEL_00 == 10) {
            if (!Gender) {
                cm.getPlayer().setHair(31002);
                cm.getPlayer().setFace(21700);
                cm.getPlayer().setGender(1);
            } else {
                cm.getPlayer().setHair(30000);
                cm.getPlayer().setFace(20100);
                cm.getPlayer().setGender(0);
            }
            cm.dispose();
            cm.fakeRelog();
            cm.updateChar();
            return;
        }

        newItem = selection & 0xFF;
        if (SEL_00 >= 12) {
            switch (SEL_00) {
                case 12:
                case 13:
                case 14:
                case 15:
                case 19:
                    cm.setHairAndroid(codyList[newItem]);
                    break;
                case 16:
                case 17:
                case 18:
                    cm.setFaceAndroid(codyList[newItem]);
                    break;
                case 20:
                    cm.setSkinAndroid(codyList[newItem]);
                    break;
            }
            cm.dispose();
            return;
        }

        if (cm.isAngelicBuster()) {
            cm.sendNext("#fs11#어떤 모습을 스타일링 하시겠어요?\r\n\r\n#b#L1#변신전의 모습을 바꾼다#l\r\n#L2#변신후의 모습을 바꾼다#l#k");
        } else {
            if (cm.isZero() && Gender) {
                switch (SEL_00) {
                    case 8:
                        cm.getPlayer().setSecondBaseColor(-1);
                        cm.getPlayer().setSecondAddColor(0);
                        cm.getPlayer().setSecondBaseProb(0);
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        cm.setZeroSecondHair(codyList[newItem]);
                        break;
                    case 5:
                    case 6:
                    case 7:
                        cm.setZeroSecondFace(codyList[newItem]);
                        break;
                    case 9:
                        cm.setZeroSecondSkin(codyList[newItem]);
                        break;
                }
            } else {
                if (SEL_00 == 8) {
                    if (codyList[newItem] == 39465) {
                        cm.sendOk("해당 헤어는 현재 오류가 있어 적용하실 수 없습니다.");
                        cm.dispose();
                        return;
                    }
                    cm.getPlayer().setBaseColor(-1);
                    cm.getPlayer().setAddColor(0);
                    cm.getPlayer().setBaseProb(0);
                }
                cm.setAvatar(4000000, codyList[newItem]);
            }
            cm.dispose();
            cm.updateChar();
        }

    } else if (status == 4) {
        if (selection == 1) {
            cm.setAvatar(4000000, codyList[newItem]);
        } else if (selection == 2) {
            switch (SEL_00) {
                case 8:
                    cm.getPlayer().setSecondBaseColor(-1);
                    cm.getPlayer().setSecondAddColor(0);
                    cm.getPlayer().setSecondBaseProb(0);
                case 1:
                case 2:
                case 3:
                case 4:
                    cm.setAngelicSecondHair(codyList[newItem]);
                    break;
                case 5:
                case 6:
                case 7:
                    cm.setAngelicSecondFace(codyList[newItem]);
                    break;

                case 9:
                    cm.setAngelicSecondSkin(codyList[newItem]);
                    break;
            }
        }
        cm.dispose();
        cm.updateChar();
    }
}