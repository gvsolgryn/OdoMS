var enter = "\r\n";
var seld = -1;
var selditem;

var shops = [
    { 'itemid': 4310332, 'qty': 1, 'price': 2 }, // 코인 아이템 코드 , 코인 지급갯수 , 포인트소모갯수
];

var p = -1;
var seldqty = -1;

var isEquip = false;

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
    if (status == 0) {
        cm.sendGetNumber("#i4310332#을 #b일반 마일리지#k로 전환하시겠습니까?\r\n\r\n#r[교환비 1 : 1000]#k\r\n\r\n교환을 원하는 코인 갯수를 입력하세요.", 1, 1, 999999);

    } else if (status == 1) {
        seld = sel; // 사용자가 입력한 코인의 갯수를 저장
        var pointsToGain = seld * 1000; // 코인 갯수에 따른 포인트 변환 비율
        if (cm.haveItem(4310332, seld)) { // 사용자가 가지고 있는 코인보다 많은지 확인
            var msg = "코인 #r" + seld + "개#k를 전환하시겠습니까?" + enter;
            msg += "획득 마일리지 : #b"+pointsToGain+" 일반 마일리지#k" + enter;
            cm.sendYesNo(msg);
        } else {
            cm.sendOk("가지고 있는 코인보다 많은 양을 입력하셨습니다.");
            cm.dispose();
        }

    } else if (status == 2) {
        if (mode == 1) { // 예 누를 때
            var pointsToGain = seld * 1000; // 코인 갯수에 따른 포인트 변환 비율
            cm.gainItem(4310332, -seld); // 코인 소모
            cm.getPlayer().gainHPoint(pointsToGain);
            cm.sendOk("코인을 #b" + pointsToGain + " 일반 마일리지#k로 성공적으로 전환하였습니다.");
        } else { // 아니오 또는 취소 누를 때
            cm.sendOk("전환을 취소하셨습니다.");
        }
        cm.dispose();
    }
}
