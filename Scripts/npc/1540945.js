var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 0) {
        cm.sendSimple("#fs11#용사여, 무엇을 하고자 하는가?\r\n\r\n#L0##bV코어를 강화하거나 제작하고 싶습니다.#l\r\n#L1#아무것도 아닙니다. 날씨가 좋군요.");
        } else if (status == 1) {
            if (selection == 0) {
    	    //cm.getClient().getSession().write(Packages.tools.packet.CField.UIPacket.openUI(1131)); //2차비밀번호 입력창
            cm.getClient().getSession().writeAndFlush(Packages.tools.packet.CField.UIPacket.openUI(1131));
            cm.dispose();
            } else if (selection == 1) {
                cm.sendOk("이 부근은 에르다의 흐름에 따라 기후가 급격하게 변화하지. 몸 조심하게나.");
                cm.dispose();
            }
        }
    }
}