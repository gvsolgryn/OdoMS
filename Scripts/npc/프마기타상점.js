var status = -1;

var item = [
    [4310063, "#z4310063#", 1, 100000],  //코인
    [4310086, "자유전직 코인", 1, 100000], 
    [4310333, "프리미엄 마일리지", 1, 1050], 
    [4033235, "프리미엄 자동사냥터 ", 1, 30000],
    [2432097, "#z2432097# ", 1, 30000], // 패스이용
    [2432096, "#z2432096# ", 1, 50000], //패스초기화
    [2631834, "오토루팅 상자", 1, 150000],
    [2430030, "보스 초기화권 0렙 ", 1, 10000],
    [4034803, "닉네임 변경권", 1, 20000],
    [5060048, "#z5060048# 10개", 10, 30000],
    [2430218, "#z2430218#", 1, 10000],
    [4001833, "고급 돌림판 이용권 10개", 10, 15000],
    [2435899, "스페셜 위대한 소울상자 ", 1, 10000],
    [4036804, "무토의 성장 스킬 ", 1, 200000],
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