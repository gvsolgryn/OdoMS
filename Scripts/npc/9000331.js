var status = -1;
var enter = "\r\n";
var 부적 = 4031059;
var 태정 = 4021022;
var 영꺼불 = 4023027;
var 태정개수 = 30;
var 영꺼불개수 = 10;
var seld = -1;

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
        var 말 = "#fs11#"
        말 += "안녕하세요. #h0#님" + enter;
        말 += "#i" + 태정 + "# #z" + 태정 + "# " + 태정개수 + "개 또는," + enter;
        말 += "#i" + 영꺼불 + "# #z" + 영꺼불 + "# " + 영꺼불개수 + "개" + enter;
        말 += "위 재료를 #i" + 부적 + "# #z" + 부적 + "# 1개로 바꿔드립니다."
        cm.sendOk(말);
    } else if (status == 1) {
        var 말 = "#fs11#"
        말 += "#L1##i" + 태정 + "# #z" + 태정 + "#로 교환하기" + enter;
        말 += "#L2##i" + 영꺼불 + "# #z" + 영꺼불 + "#으로 교환하기" + enter;
        cm.sendOk(말);
    } else if (status == 2) {
        seld = selection;
        cm.sendGetNumber("몇개를 교환하시겠습니까?", 1, 1, 100);
    } else if (status == 3) {
        count = selection;
        var 말 = "#fs11#"
        if (seld == 1) {
            if (cm.haveItem(태정, 태정개수 * count)) {
                말 += "교환되었습니다." + enter;
                cm.gainItem(부적, count);
                cm.gainItem(태정, -태정개수 * count);
            } else {
                말 += "재료가 부족합니다." + enter;
            }
        } else if (seld == 2) {
            if (cm.haveItem(영꺼불, 영꺼불개수 * count)) {
                말 += "교환되었습니다." + enter;
                cm.gainItem(부적, count);
                cm.gainItem(영꺼불, -영꺼불개수 * count);
            } else {
                말 += "재료가 부족합니다." + enter;
            }
        }
        cm.sendOk(말);
        cm.dispose();
    }
}
