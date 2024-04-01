/*
제작 : 판매요원 (267→295)
용도 : 판매요원 팩
*/

importPackage(Packages.constants);
importPackage(Packages.tools.packet);

메뉴 = "#fUI/Basic.img/actMark/1#\r\n#fs11#";
아이콘 = "#fUI/GuildMark.img/BackGround/00001026/1#"
별1 = "#fUI/GuildMark.img/Mark/Etc/00009003/15#"
별2 = "#fUI/GuildMark.img/Mark/Etc/00009002/15#"
별3 = "#fUI/UIWindow.img/ToolTip/WorldMap/StarForce#"
별4 = "#fUI/UIWindow.img/ToolTip/WorldMap/ArcaneForce#"
별5 = "#fUI/GuildMark.img/Mark/Etc/00009018/15#"
별6 = "#fUI/UIWindow.img/ToolTip/WorldMap/AuthenticForce#"
검정 = "#fc0xFF191919#"
마빌 = "#fUI/Basic.img/theblackcoin/6#"
몬스터파크 = "#fUI/Basic.img/theblackcoin/9#"
마을 = "#fUI/Basic.img/theblackcoin/10#"
스타포스 = "#fUI/Basic.img/theblackcoin/11#"
아케인 = "#fUI/Basic.img/theblackcoin/12#"
사냥터 = "#fUI/Basic.img/theblackcoin/17#"
어센틱 = "#fUI/Basic.img/theblackcoin/15#"
보스 = "#fUI/Basic.img/theblackcoin/16#"
재규어 = "#fUI/Basic.img/theblackcoin/18#"
이동 = "#fUI/Basic.img/theblack/5#";

회색 = "#fc0xFFBDBDBD#"
파랑 = "#fc0xFF4374D9#"
status = 0;
selectedMap = -1;

맵1 = [
        101083100,
        100010000,
        101071200,
        105010000,
        310050200,
        261020400,
        222110100,
        251010402,
        270020100,
        103041115,
        221030510,
        271030200,
        241000221,
        410000111,
        273040100,
        310070200,
        105300301,
    ]
    //일반 사냥터
맵이름1 = [
        "" + 회색 + "[10~30]#k " + 파랑 + "엘로딘 - 깊어지는 숲 2#k",
        "" + 회색 + "[10~30]#k " + 파랑 + "헤네시스 - 헤네시스 북쪽언덕#k",
        "" + 회색 + "[30~60]#k " + 파랑 + "엘리넬 호수 - 호수 위 3#k",
        "" + 회색 + "[50~80]#k " + 파랑 + "습지 - 조용한 습지#k",
        "" + 회색 + "[70~90]#k " + 파랑 + "레벤 광산 - 갱도 입구 1#k",
        "" + 회색 + "[80~100]#k " + 파랑 + "알카드노 연구소 - 연구소 C-2 구역#k",
        "" + 회색 + "[100~120]#k " + 파랑 + "판타스틱 테마파크 - 퍼니 스테이션 <2>#k",
        "" + 회색 + "[130~150]#k " + 파랑 + "무릉도원 - 빨간코 해적단 소굴 2#k",
        "" + 회색 + "[140~160]#k " + 파랑 + "타임로드 - 후회의 길 1#k",
        "" + 회색 + "[140~165]#k " + 파랑 + "커닝타워 - 2층 카페 <2>#k",
        "" + 회색 + "[160~180]#k " + 파랑 + "UFO 내부 - 복도 101#k",
        "" + 회색 + "[160~180]#k " + 파랑 + "기사단 요새 - 기사단 제 2구역#k",
        "" + 회색 + "[180~200]#k " + 파랑 + "킹덤로드 - 끝나지 않는 비극의 숲5#k",
        "" + 회색 + "[180~200]#k " + 파랑 + "뾰족귀 여우골짜기 - 여우숲 윗길#k",
        "" + 회색 + "[180~200]#k " + 파랑 + "황혼의 페리온 - 버려진 발굴지역 2#k",
        "" + 회색 + "[200~220]#k " + 파랑 + "스카이라인 - 스카이라인 올라가는 길#k",
        "" + 회색 + "[200~220]#k " + 파랑 + "타락한 세계수 - 상단 줄기 갈림길#k",

    ]
    //스타포스

맵3 = [
    220070400,
    211041700,
    211080400,
    240040512,
    103041139
]
맵이름3 = [
    "#fc0xFFE5D85C#28 " + 회색 + "[110~125]#k " + 파랑 + "시계탑 최하층 - 잊혀진 회랑#k",
    "#fc0xFFE5D85C#50 " + 회색 + "[125~145]#k " + 파랑 + "폐광 - 폐광3#k",
    "#fc0xFFE5D85C#45 " + 회색 + "[125~145]#k " + 파랑 + "장미정원 - 숨겨진 정원1#k",
    "#fc0xFFE5D85C#75 " + 회색 + "[140~160]#k " + 파랑 + "미나르숲 - 남겨진 용의 둥지2#k",
    "#fc0xFFE5D85C#80 " + 회색 + "[145~165]#k " + 파랑 + "커닝타워 - 4층 음반 매장 <4>#k",
]

맵4 = [
        450001216, //여로
        450001260,

        450002010, //츄츄 아일랜드
        450002014,

        450015030, //얌얌 아일랜드
        450015110,
        450015200,

        450003200, //레헬른
        450003320,
        450003420,
        450003440,
        450003510,

        450005121, //아르카나
        450005130,
        450005430,
        450005500,

        450006120, //모라스
        450006150,
        450006210,
        450006420,

        450007110, //에스페라
        450007210,

        450016060, //셀라스
        450016130,
        450016230,
        450016260,

        450009120, //문브릿지
        450009330,

        450011420, //미궁
        450011560,
        450011600,

        450012040, //리멘
        450012330,
        450012340,
        450012440,
        450012360,
        450012450,

    ]
    //아케인지역
맵이름4 = [
    "#fc0xFF4641D9#60 " + 회색 + "[200~210]#k " + 파랑 + "소멸의 여로 : 동굴 아래쪽",
    "#fc0xFF4641D9#80 " + 회색 + "[200~210]#k " + 파랑 + "소멸의 여로 : 숨겨진 호숫가#k\r\n", //2

    "#fc0xFF4641D9#100 " + 회색 + "[210~220]#k " + 파랑 + "츄츄 아일랜드 : 츄릅포레스트 깊은 곳",
    "#fc0xFF4641D9#130 " + 회색 + "[210~220]#k " + 파랑 + "츄츄 아일랜드 : 격류지대\r\n", //4

    "#fc0xFF4641D9#130 " + 회색 + "[215~220]#k " + 파랑 + "얌얌 아일랜드 : 머쉬버드 숲1",
    "#fc0xFF4641D9#160 " + 회색 + "[215~220]#k " + 파랑 + "얌얌 아일랜드 : 일리야드 들판1",
    "#fc0xFF4641D9#160 " + 회색 + "[215~220]#k " + 파랑 + "얌얌 아일랜드 : 펑고스 숲1\r\n", //7

    "#fc0xFF4641D9#190 " + 회색 + "[220~225]#k " + 파랑 + "레헬른 : 무법자들의 거리1",
    "#fc0xFF4641D9#210 " + 회색 + "[220~225]#k " + 파랑 + "레헬른 : 닭이 뛰노는 곳3",
    "#fc0xFF4641D9#210 " + 회색 + "[220~225]#k " + 파랑 + "레헬른 : 본색을 드러내는 곳3",
    "#fc0xFF4641D9#210 " + 회색 + "[220~225]#k " + 파랑 + "레헬른 : 춤추는 구두 점령지1",
    "#fc0xFF4641D9#240 " + 회색 + "[220~225]#k " + 파랑 + "레헬른 : 악몽의 시계탑 2층\r\n", //12

    "#fc0xFF4641D9#280 " + 회색 + "[225~230]#k " + 파랑 + " 아르카나 : 물과 햇살의 숲",
    "#fc0xFF4641D9#280 " + 회색 + "[225~230]#k " + 파랑 + " 아르카나 : 흙의 숲",
    "#fc0xFF4641D9#360 " + 회색 + "[225~230]#k " + 파랑 + " 아르카나 : 동굴 아랫길",
    "#fc0xFF4641D9#360 " + 회색 + "[225~230]#k " + 파랑 + " 아르카나 : 다섯 갈래 동굴\r\n", //16

    "#fc0xFF4641D9#440 " + 회색 + "[230~235]#k " + 파랑 + " 모라스 : 도둑고양이 출몰지",
    "#fc0xFF4641D9#440 " + 회색 + "[230~235]#k " + 파랑 + " 모라스 : 형님들 구역2",
    "#fc0xFF4641D9#480 " + 회색 + "[230~235]#k " + 파랑 + " 모라스 : 그림자가 춤추는 곳2",
    "#fc0xFF4641D9#520 " + 회색 + "[230~235]#k " + 파랑 + " 모라스 : 그날의 트뤼에페3\r\n", //20

    "#fc0xFF4641D9#600 " + 회색 + "[235~240]#k " + 파랑 + " 에스페라 : 거울빛에 물든 바다2",
    "#fc0xFF4641D9#640 " + 회색 + "[235~240]#k " + 파랑 + " 에스페라 : 거울에 비친 빛의 신전2\r\n", //22

    "#fc0xFF4641D9#600 " + 회색 + "[240~245]#k " + 파랑 + " 셀라스 : 빛이 마지막으로 닿는 곳6",
    "#fc0xFF4641D9#640 " + 회색 + "[240~245]#k " + 파랑 + " 셀라스 : 끝없이 추락하는 심해3",
    "#fc0xFF4641D9#670 " + 회색 + "[240~245]#k " + 파랑 + " 셀라스 : 별이 삼켜진 심해3",
    "#fc0xFF4641D9#670 " + 회색 + "[240~245]#k " + 파랑 + " 셀라스 : 별이 삼켜진 심해6\r\n", //26

    "#fc0xFF4641D9#670 " + 회색 + "[245~250]#k " + 파랑 + " 문브릿지 : 사상의 경계2",
    "#fc0xFF4641D9#730 " + 회색 + "[245~250]#k " + 파랑 + " 문브릿지 : 공허의 파도3\r\n", //28

    "#fc0xFF4641D9#760 " + 회색 + "[250~255]#k " + 파랑 + " 고통의 미궁 : 미궁 내부1",
    "#fc0xFF4641D9#790 " + 회색 + "[250~255]#k " + 파랑 + " 고통의 미궁 : 미궁 중심부6",
    "#fc0xFF4641D9#820 " + 회색 + "[250~255]#k " + 파랑 + " 고통의 미궁 : 미궁 최심부1\r\n", //31

    "#fc0xFF4641D9#850 " + 회색 + "[255~260]#k " + 파랑 + " 리멘 : 세계의 눈물 하단3",
    "#fc0xFF4641D9#880 " + 회색 + "[255~260]#k " + 파랑 + " 리멘 : 세계가 끝나는 곳 1-4",
    "#fc0xFF4641D9#880 " + 회색 + "[255~260]#k " + 파랑 + " 리멘 : 세계가 끝나는 곳 1-5",
    "#fc0xFF4641D9#880 " + 회색 + "[255~260]#k " + 파랑 + " 리멘 : 세계가 끝나는 곳 2-5",
    "#fc0xFF4641D9#1000 " + 회색 + "[255~260]#k " + 파랑 + " 리멘 : 세계가 끝나는 곳 1-7",
    "#fc0xFF4641D9#1000 " + 회색 + "[255~260]#k " + 파랑 + " 리멘 : 세계가 끝나는 곳 2-6", //37
]

맵5 = [
    410000530, //세르니움
    410000650,
    410000710,

    410000950, //불타는 세르니움
    410001000,
    410000890,

    410003070, //아르크스
    410003140,
    410003160,
]

//그란디스
맵이름5 = [
    "#fc0xFF4641D9#50 " + 회색 + "[260~265]#k " + 파랑 + " 세르니움 : 해변 암석 지대2",
    "#fc0xFF4641D9#50 " + 회색 + "[260~265]#k " + 파랑 + " 세르니움 : 세르니움 동쪽 성벽2",
    "#fc0xFF4641D9#50 " + 회색 + "[260~265]#k " + 파랑 + " 세르니움 : 왕립 도서관 제2구역\r\n",

    "#fc0xFF4641D9#70 " + 회색 + "[265~270]#k " + 파랑 + " 불타는 세르니움 : 격전의 서쪽 성벽4",
    "#fc0xFF4641D9#100 " + 회색 + "[265~270]#k " + 파랑 + " 불타는 세르니움 : 격전의 동쪽 성벽3",
    "#fc0xFF4641D9#100 " + 회색 + "[265~270]#k " + 파랑 + " 불타는 세르니움 : 불타는 왕립 도서관 제6구역\r\n",

    "#fc0xFF4641D9#130 " + 회색 + "[270~300]#k " + 파랑 + " 아르크스 - 무법자들이 지배하는 황야4",
    "#fc0xFF4641D9#160 " + 회색 + "[270~300]#k " + 파랑 + " 아르크스 - 낭만이 저무는 자동차 극장6",
    "#fc0xFF4641D9#200 " + 회색 + "[270~300]#k " + 파랑 + " 아르크스 - 종착지 없는 횡단열차2",

]


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

    if (mode == -1) {
        cm.dispose();
    } else {
        if (status == 0 && mode == 0) {
            cm.dispose();
            return;
        } else if (status == 1 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
    }
    if (status == 0) {
        말 = "#L4##fn나눔고딕##fs11##fc0xFFEDA900#" + 스타포스 + " 적정 레벨 사냥터#k로 이동하겠습니다.#l\r\n"
        말 += "#L0##fc0xFF6B66FF#" + 사냥터 + " 필드 사냥터#k로 이동하겠습니다.#l\r\n"
             //말 += "#L4##fc0xFFE5D85C#" + 스타포스 + " 스타포스 사냥터#k로 이동하겠습니다.#l\r\n"
        말 += "#L5##fc0xFF6799FF#" + 아케인 + " 아케인 사냥터#k로 이동하겠습니다.#l\r\n"
        말 += "#L10##fc0xFF4641D9#" + 어센틱 + " 그란디스 사냥터#k로 이동하겠습니다.#l\r\n"
        //말 += "#L11##fc0xFFF15F5F#" + 별2 + " 티켓 사냥터#k로 이동하겠습니다.#l\r\n"
        //말 += "#L12##fc0xFF990085#" + 별1 + " 랭크 사냥터#k로 이동하겠습니다.#l\r\n\r\n"
        말 += "#fc0xFFD5D5D5#───────────────────────────#k\r\n";
        말 += "#L6##fc0xFFF15F5F#" + 보스 + " 보스#k를 격파하러 가겠습니다.#l\r\n"
        말 += "#L2##fc0xFF35B62C#" + 마을 + " 다른 마을#k로 가겠습니다.#l\r\n"
        //말 += "#L8##fc0xFF990085#" + 몬스터파크 + " 몬스터파크#k로 이동하겠습니다.#l\r\n"
        //말 += "#L13##b" + 마빌 + " 낚시터#k로 이동하겠습니다.#l\r\n"
        if (cm.getPlayer().getJob() == 3300 || cm.getPlayer().getJob() == 3310 || cm.getPlayer().getJob() == 3311 || cm.getPlayer().getJob() == 3312) {
            말 += "#L7##fc0xFF8041D9#" + 재규어 + " 재규어#k를 잡으러 가겠습니다.\r\n"
        }
        cm.sendOkS(말, 0x4);
    } else if (status == 1) {
        fselection = selection;
        if (selection == 0) { //사냥터
            말 = "\r\n";
            for (i = 0; i < 맵1.length; i++) {
                말 += "#fs11##L" + i + "# " + 맵이름1[i] + "#l\r\n";
            }
            cm.sendOkS(말, 0x4);

        } else if (selection == 1) {
            cm.dispose()
            cm.openNpc(2150007);

        } else if (selection == 2) {
            cm.dispose();
            cm.openNpc(3000012);

        } else if (selection == 4) {
            말 = "\r\n";
            if (cm.getPlayer().getLevel() > 300) {
                말 += "#fs11#" + 검정 + "정상적인 레벨 범위가 아닙니다.\r\n"
                cm.sendOkS(말, 0x04, 9401243);
                cm.dispose();
            }
            if (cm.getPlayer().getLevel() < 301) {
                말 += "#fs11#" + 검정 + "현재 #b#h ##k" + 검정 + "님의 레벨은 " + cm.getPlayer().getLevel() + " 입니다.\r\n아래는 현재 레벨에 맞는 #r추천 사냥터 목록#k " + 검정 + "입니다.\r\n"
            }
            if (cm.getPlayer().getLevel() < 30) {
                말 += "#L101083000# #r[추천]#k #b엘로딘 - 깊어지는 숲 1#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 120) {
                말 += "#L261020400# #r[추천]#k #b알카드노 연구소 - 연구소 C-2 구역#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 170) {
                말 += "#L103041115# #r[추천]#k #b커닝타워 - 2층 카페 <2>#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 200) {
                말 += "#L273040100# #r[추천]#k #b황혼의 페리온 - 버려진 발굴지역 2#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 210) {
                말 += "#L450001216# #r[추천]#k #b소멸의 여로 : 동굴 아래쪽#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 220) {
                말 += "#L450002014# #r[추천]#k #b츄츄 아일랜드 : 격류지대3#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 225) {
                말 += "#L450003320# #r[추천]#k #b레헬른 : 닭이 뛰노는 곳3#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 230) {
                말 += "#L450003320# #r[추천]#k #b레헬른 : 닭이 뛰노는 곳3#k#l\r\n"
                말 += "#L450005500# #r[추천]#k #b아르카나 : 다섯 갈래 동굴#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 240) {
                말 += "#L450003320# #r[추천]#k #b레헬른 : 닭이 뛰노는 곳3#k#l\r\n"
                말 += "#L450005500# #r[추천]#k #b아르카나 : 다섯 갈래 동굴#k#l\r\n"
                말 += "#L450006210# #r[추천]#k #b모라스 : 그림자가 춤추는 곳2#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 245) {
                말 += "#L450005500# #r[추천]#k #b아르카나 : 다섯 갈래 동굴#k#l\r\n"
                말 += "#L450006210# #r[추천]#k #b모라스 : 그림자가 춤추는 곳2#k#l\r\n"
                말 += "#L450016060# #r[추천]#k #b셀라스 : 빛이 마지막으로 닿는 곳6#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 250) {
                말 += "#L450005500# #r[추천]#k #b알카 : 다섯 갈래 동굴#k#l\r\n"
                말 += "#L450006210# #r[추천]#k #b모라스 : 그림자가 춤추는 곳2#k#l\r\n"
                말 += "#L450016060# #r[추천]#k #b셀라스 : 빛이 마지막으로 닿는 곳6#k#l\r\n"
                말 += "#L450009120# #r[추천]#k #b문브릿지 : 사상의 경계2#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 255) {
                말 += "#L450011560# #r[추천]#k #b고통의 미궁 : 미궁 중심부6#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 260) {
                말 += "#L450011560# #r[추천]#k #b고통의 미궁 : 미궁 중심부6#k#l\r\n"
                말 += "#L450012440# #r[추천]#k #b리멘 : 세계가 끝나는 곳2-5#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 265) {
                말 += "#L410000650# #r[추천]#k #b세르니움 : 동쪽 성벽2#k#l\r\n"
                말 += "#L410000710# #r[추천]#k #b세르니움 : 왕립 도서관2#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 270) {
                말 += "#L410000650# #r[추천]#k #b세르니움 : 동쪽 성벽2#k#l\r\n"
                말 += "#L410000710# #r[추천]#k #b세르니움 : 왕립 도서관2#k#l\r\n"
                말 += "#L410000890# #r[추천]#k #b불타는 세르니움 : 불타는 왕립 도서관6#k#l\r\n\r\n"
            } else if (cm.getPlayer().getLevel() < 300) {
                말 += "#L410003140# #r[추천]#k #b아르크스 : 낭만이 저무는 자동차 극장6#k#l\r\n"
                말 += "#L410003160# #r[추천]#k #b아르크스 : 종착지 없는 횡단열차2#k#l\r\n\r\n"
            }
            cm.sendOkS(말, 0x4, 9401243);
        } else if (selection == 5) {
            말 = "\r\n";
            for (i = 0; i < 맵4.length; i++) {
                말 += "#fs11##L" + i + "#" + 별4 + " " + 맵이름4[i] + "#l\r\n";
            }
            cm.sendOkS(말, 0x4);

        } else if (selection == 10) {
            말 = "\r\n";
            for (i = 0; i < 맵5.length; i++) {
                말 += "#fs11##L" + i + "#" + 별6 + " " + 맵이름5[i] + "#l\r\n";
            }
            cm.sendOkS(말, 0x4);

        } else if (selection == 6) {
            cm.dispose();
            cm.openNpc(9062608);

        } else if (selection == 7) {
            cm.warp(931000500, 0);
            cm.dispose()

        } else if (selection == 11) {
            cm.openNpc(9000008);
            cm.dispose()

        } else if (selection == 13) {
            cm.warp(993000750, 0);
            cm.dispose()

        } else if (selection == 8) {
            cm.warp(951000000, 0);
            cm.dispose()
        } else if (selection == 9) {
            cm.warp(910001000, 0);
            cm.dispose()
        }
    } else if (status == 2) {
        selectedMap = selection;
        if (selection == 1000) {
            말 = "#fs11##dA. 아케인심볼은 다음과 같은곳에서 획득 가능합니다.\r\n\r\n";
            말 += "#fc0xFF4374D9#1. 아케인 포스 사냥터에서 낮은 확률로 필드 드롭됩니다.\r\n";
            말 += "2. 상점 시스템 > 포인트 상점에서 구매가 가능합니다.\r\n";
            말 += "3. 상점 시스템 > 십자 상점에서 구매가 가능합니다.";
            말 += "\r\n#y#└ 구입에는 다음과 같은 아이템이 필요합니다.\r\n\r\n";
            말 += "#b#i4310000##z4310000##k은 사냥시 획득이 가능합니다.";
            말 += "\r\n#y##b#i4310029##z4310029##k은 보스 클리어시 획득이 가능합니다.";
            cm.sendOkS(말, 0x4);
            cm.dispose()
        } else if (fselection == 0) {
            cm.warp(맵1[selectedMap], 0);
            cm.dispose();
        } else if (fselection == 1) {
            cm.warp(맵2[selectedMap], 0);
            cm.dispose();
        } else if (fselection == 4) {
            cm.warp(selection, 0);
            cm.dispose();
        } else if (fselection == 5) {
            if (selectedMap <= 1) {
                if (cm.getPlayer().getLevel() < 200) {
                    cm.sendOkS("#fs11##r소멸의 여로는 레벨 200 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 3) { //츄츄
                if (cm.getPlayer().getLevel() < 210) {
                    cm.sendOkS("#fs11##r츄츄 아일랜드는 레벨 210 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 6) { //얌얌
                if (cm.getPlayer().getLevel() < 215) {
                    cm.sendOkS("#fs11##r얌얌 아일랜드는 레벨 215 달성 시 입장이 가능합니다..", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 11) { //레헬른
                if (cm.getPlayer().getLevel() < 220) {
                    cm.sendOkS("#fs11##r레헬른 레벨 220 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 15) { //아르카나
                if (cm.getPlayer().getLevel() < 225) {
                    cm.sendOkS("#fs11##r아르카나 레벨 225 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 19) { //모라스
                if (cm.getPlayer().getLevel() < 230) {
                    cm.sendOkS("#fs11##r모라스는 레벨 230 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 21) { //에스페라
                if (cm.getPlayer().getLevel() < 235) {
                    cm.sendOkS("#fs11##r에스페라는 레벨 235 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 25) { //셀라스
                if (cm.getPlayer().getLevel() < 240) {
                    cm.sendOkS("#fs11##r셀라스는 레벨 240 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 27) { //문브릿지
                if (cm.getPlayer().getLevel() < 245) {
                    cm.sendOkS("#fs11##r문브릿지는 레벨 245 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 30) { //테네브리스
                if (cm.getPlayer().getLevel() < 250) {
                    cm.sendOkS("#fs11##r고통의 미궁은 레벨 250 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 36) { //리멘
                if (cm.getPlayer().getLevel() < 255) {
                    cm.sendOkS("#fs11##r리멘은 레벨 255 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            }
            cm.warp(맵4[selectedMap], 0);
            cm.dispose();

            /*} else if (fselection == 7) {
				cm.warp(931000500, 0);
				cm.dispose()
	*/
        } else if (fselection == 10) {
            if (selectedMap <= 2) { // 세르니움
                if (cm.getPlayer().getLevel() < 260) {
                    cm.sendOkS("#fs11##r세르니움은 레벨 260 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 5) { // 불타는 세르니움
                if (cm.getPlayer().getLevel() < 265) {
                    cm.sendOkS("#fs11##r불타는 세르니움은 레벨 265 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            } else if (selectedMap <= 8) { // 호텔 아르크스
                if (cm.getPlayer().getLevel() < 270) {
                    cm.sendOkS("#fs11##r아르크스는 레벨 270 달성 시 입장이 가능합니다.", 0x04, 9401243);
                    cm.dispose();
                    return;
                }
            }
            cm.warp(맵5[selectedMap], 0);
            cm.dispose();
        }
    }
}