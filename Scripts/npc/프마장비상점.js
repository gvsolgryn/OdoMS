var status = -1;

var item = [//아이템코드, 목록, 단위갯수, 가격
    [2439614, "제네시스 무기(25성,스텟+4000)", 1, 500000],
    [2439528, "에테르넬 4종 방어구선택상자", 1, 25000],
    [2439527, "칠흑 선택상자", 1, 20000],
    [2633915, "아케인 방어구 선택상자 ", 1, 10000],
    [2630782, "아케인 무기 선택 상자", 1, 10000],
    [2630495, "#z2630495#", 1 , 30000], //보조무기상자
    [1114318, "#z1114318#", 1, 100000], // 링
    [1662115, "#z1662115#", 1, 50000], // 로이드
    [1672028, "#z1672028#", 1, 50000], //하트
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