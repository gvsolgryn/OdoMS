var status = -1;
2430394
var item = [//아이템코드, 목록, 단위갯수, 가격
    [5062006, "플래티넘 미라클 큐브", 1, 2000],
    [5062503, "화이트 에디셔널 큐브", 1, 3000],
    [5062005, "어메이징 미라클 큐브", 1, 2000],
    [2439629, "#z2439629#", 1, 20000], 
    [2439630, "#z2439630#", 1, 20000],
    [2470021, "업횟5+ 황금망치", 1, 30000],
    [2049376, "스타포스 20성", 1, 10000],
    [2643133, "끼룩 링 주문서(스텟+200)", 1, 10000],
    [2049704, "레전드리 잠재주문서 10개", 10, 8000], // 레전더리잠재주문서
    [2049360, "놀라운 장비강화 주문서 10개", 10, 10000],
    [2702003, "#z2702003#", 1, 2000], 
    [2046400, "P 한손무기(공) 주문서", 1, 8000], // 후원 무기 줌서 공
    [2046402, "P 두손무기(공) 주문서", 1, 8000], // 후원 무기 줌서 공
    [2046401, "P 한손무기(마) 주문서", 1, 8000], // 후원 무기 줌서 마
    [2046408, "P 펫장비(공) 주문서", 1, 8000], // 후원 펫공
    [2046409, "P 펫장비(마) 주문서", 1, 8000], // 후원 펫마
    [2046405, "P 악세(공) 주문서", 1, 8000], // 후원 악세 공
    [2046406, "P 악세(마) 주문서", 1, 8000], // 후원 악세 마
    [2046403, "P 방어구(공) 주문서", 1, 8000], // 후원 방어구 공
    [2046404, "P 방어구(마) 주문서", 1, 8000], // 후원 방어구 마
    
];

var choice = -1;
var count = -1;
var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    }
    if (status == 0) {
        chat = "#fs11#";
        chat += "#fs11##b#h0##k님#k 의 #bP 마일리지#k : #r" + cm.getPlayer().getDonationPoint() + " P#k#n\r\n";
        for (i = 0; i < item.length; i++) {
            chat += "#fs11##L" + i + "##n#k#i" + item[i][0] + "# #d" + item[i][1] + "#k - #bP 마일리지#k #r" + item[i][3] + "#l\r\n";
        }
        cm.sendSimple(chat);
    } else if (status == 1) {
        choice = selection;
        cm.sendGetNumber("#i" + item[choice][0] + "#" + item[choice][1] + " " + item[choice][2] + "개(묶음)을 선택하셨습니다.\r\n" +
                "구입하실 수량을 입력해주세요. (1묶음 당 " + item[choice][3] + " P 마일리지 필요)", 0, 1, 30000);
    } else if (status == 2) {
        count = selection;
        if (count < 1 || count > 30000) {
            cm.dispose();
            return;
        }
        cm.sendYesNo("#i" + item[choice][0] + "#" + item[choice][1] + " " + item[choice][2] + "개(묶음)을 " + count + "개(세트) 구매하시겠습니까?\r\n" +
                item[choice][3] * count + "P 마일리지가 필요합니다.");
    } else if (status == 3) {
        if (cm.getPlayer().getDonationPoint() >= item[choice][3] * count && cm.canHold(item[choice][0], item[choice][2] * count)) {
            cm.getPlayer().gainDonationPoint(-item[choice][3] * count);
            cm.gainItem(item[choice][0], item[choice][2] * count);
            cm.sendOk("#i" + item[choice][0] + "#" + item[choice][1] + " " + item[choice][2] + "개(묶음)을 " + count + "개(세트) 구매하였습니다. 인벤토리를 확인해 주세요.");
            cm.dispose();
            return;
        } else {
            cm.sendOk("프리미엄 마일리지가 부족하거나 인벤토리 슬롯이 꽉 찬것은 아닌지 확인해 주세요.");
            cm.dispose();
            return;
        }
    } else {
        cm.dispose();
        return;
    }
}