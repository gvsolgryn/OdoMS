var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1212120, // 아케인셰이드 샤이닝로드
    1213018, // 아케인셰이드 튜너
    1222113, // 아케인셰이드 소울슈터
    1232113, // 아케인셰이드 데스페라도
    1242121, // 아케인셰이드 에너지체인
    1262039, // 아케인셰이드 ESP리미터
    1272017, // 아케인셰이드 체인
    1282017, // 아케인셰이드 매직 건틀렛
    1292018, // 아케인셰이드 초선
    1302343, // 아케인셰이드 세이버
    1312203, // 아케인셰이드 엑스
    1322255, // 아케인셰이드 해머
    1332279, // 아케인셰이드 대거
    1342104, // 아케인셰이드 블레이드
    1362140, // 아케인셰이드 케인
    1372228, // 아케인셰이드 완드
    1382265, // 아케인셰이드 스태프
    1402259, // 아케인셰이드 투핸드소드
    1412181, // 아케인셰이드 투핸드엑스
    1422189, // 아케인셰이드 투핸드해머
    1432218, // 아케인셰이드 스피어
    1442274, // 아케인셰이드 폴암
    1452257, // 아케인셰이드 보우
    1462243, // 아케인셰이드 크로스보우
    1472265, // 아케인셰이드 가즈
    1482221, // 아케인셰이드 클로
    1492235, // 아케인셰이드 피스톨
    1522143, // 아케인셰이드 듀얼보우건
    1532150, // 아케인셰이드 시즈건
    1582023, // 아케인셰이드 엘라하
    1592020, // 아케인셰이드 에인션트 보우
	1214018, // 아케인셰이드 브레스 슈터
	1404018, // 아케인셰이드 차크람
];

function start() {
    status = -1;
    action(1, 1, 0);
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
        var amed = "#fs11##d#h0#님, 원하시는 아케인셰이드 무기를 선택해 주세요.#k" + enter
        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }
        cm.sendOk(amed);
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2633914, -1);
        cm.dispose();
    }
}
