function action() {
    if (cm.getPlayer().getLevel() < 255) {
        cm.gainItem(2430218, -1);
        //cm.getPlayer().levelUp();
        cm.gainExp(530760963990);
        cm.dispose();
    } else {
        cm.sendOk("200레벨 이상은 효과를 볼 수 없습니다.");
        cm.dispose();
    }
}
