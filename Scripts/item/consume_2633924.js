var status;
var select = -1;
var enter = "\r\n";
var itemid = [
    1004808, // 아케인셰이드 나이트햇
    1004809, // 아케인셰이드 메이지햇
    1004810, // 아케인셰이드 아처햇
    1004811, // 아케인셰이드 시프햇
    1004812, // 아케인셰이드 파이렛햇
    1102940, // 아케인셰이드 나이트케이프
    1102941, // 아케인셰이드 메이지케이프
    1102942, // 아케인셰이드 아처케이프
    1102943, // 아케인셰이드 시프케이프
    1102944, // 아케인셰이드 파이렛케이프
    1082695, // 아케인셰이드 나이트글러브
    1082696, // 아케인셰이드 메이지글러브
    1082697, // 아케인셰이드 아처글러브
    1082698, // 아케인셰이드 시프글러브
    1082699, // 아케인셰이드 파이렛글러브
    1053063, // 아케인셰이드 나이트슈트
    1053064, // 아케인셰이드 메이지슈트
    1053065, // 아케인셰이드 아처슈트
    1053066, // 아케인셰이드 시프슈트
    1053067, // 아케인셰이드 파이렛슈트
    1073158, // 아케인셰이드 나이트슈즈
    1073159, // 아케인셰이드 메이지슈즈
    1073160, // 아케인셰이드 아처슈즈
    1073161, // 아케인셰이드 시프슈즈
    1073162, // 아케인셰이드 파이렛슈즈
    1152196, // 아케인셰이드 나이트숄더
    1152197, // 아케인셰이드 메이지숄더
    1152198, // 아케인셰이드 아처숄더
    1152199, // 아케인셰이드 시프숄더
    1152200 // 아케인셰이드 파이렛숄더
];
var allstat = 30;
var atk = 30;

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
        var amed = "#fs11##d#h0#님, 원하시는 아케인셰이드 방어구를 선택해 주세요.#k" + enter

        for (var i = 0; i < itemid.length; i++) {
            amed += "#L" + itemid[i] + "##i" + itemid[i] + "# #z" + itemid[i] + "#\r\n";
        }

        cm.sendOk(amed);

        var msg = "#fs11#";

        if (status == 0) {
            msg += "아래와 같은 아이템이 지급됩니다!" + enter + enter;
            for (i = 0; i < ringList.length; i++) {
                msg += "#i" + ringList[i] + "# #z" + ringList[i] + "#" + enter;
                citem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(ringList[i]);
                citem.setStr(allstat);
                citem.setDex(allstat);
                citem.setInt(allstat);
                citem.setLuk(allstat);
                citem.setWatk(atk);
                citem.setMatk(atk);
                Packages.server.MapleInventoryManipulator.addFromDrop(cm.getClient(), citem, false);
            }
        }
    } else if (status == 1) {
        cm.gainItem(selection, 1);
        cm.gainItem(2633915, -1);
        cm.dispose();
    }
}
