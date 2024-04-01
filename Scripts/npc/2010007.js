/* guild creation npc */
var status = -1;
var sel;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
        if (cm.getPlayerStat("GID") > 0) {
            말 = "자 무엇을 도와줄까?\r\n";
            말 += "#L2##b길드 인원수를 늘리고 싶어요#l\r\n";
            말 += "#L1##b길드를 해체하고 싶어요#l\r\n";
            말 += "#L3##b길드 마스터를 변경하고 싶어요.#l\r\n";
            cm.sendOk(말);
        } else {
            cm.sendNext("당신.. 혹시 길드에 관심이 있어서 나를 찾아온 것인가?");
        }
    } else if (status == 1) {
        sel = selection;
        if (cm.getPlayerStat("GID") > 0) {
            if (selection == 1) {
                if (cm.getPlayerStat("GID") < 0 || cm.getPlayerStat("GRANK") != 1) {
                    cm.sendOk("길드장만이 길드를 해체할 수 있다네.");
                    cm.dispose();
                } else {
                    cm.sendYesNo("길드를 해체하고 싶은가....? 지금 해체하게 된다면 절대 되돌릴 수 없다네.. 또, 모아뒀던 GP는 모두 사라지게 된다네. 길드 해체는 신중하게 선택하도록 하게나. 다시 한번 생각해보기 바라네. 정말 길드를 해체하고 싶은가?");
                }
            } else if (selection == 2) {
                if (cm.getPlayerStat("GID") < 0 || cm.getPlayerStat("GRANK") != 1) {
                    cm.sendOk("길드장만이 길드 인원을 늘릴 수 있다네.");
                    cm.dispose();
                } else
                    cm.sendNext("길드 인원을 늘리고 싶어서 왔는가? 음 길드가 많이 성장했나보군~ 잘 알겠지만 길드 인원을 늘리려면 우리 길드 본부에 다시 등록을 해야되지. 물론 수수료로 GP를 사용해야 하지만 말일세. 참고로 길드원은 최대 200명까지 늘릴 수 있다네.");
            } else if (selection == 3) {
                cm.sendOk("길드 마스터의 자리가 부담스러운건가? 길드원 리스트에서 위임할 대상을 선택하고 마우스 우클릭을 해보게. 길드 마스터 위임 버튼을 클릭하면 길드 마스터를 위임할 수 있다네. 단 상대방이 온라인인 경우에만 위임할 수 있지.");
            }
        } else {
            말 = "원하는 것이 무엇인가? 말해보게..\r\n";
            말 += "#L100##b길드가 무엇인지 알려주세요#l\r\n";
            말 += "#L10##b길드를 만들려면 어떻게 해야 돼요?#l\r\n";
            말 += "#L0##b길드를 만들고 싶어요#l\r\n";
            //말 += "#L4##b길드 시스템에 대해 더 자세히 설명받고 싶어요#l\r\n";
            //말 += "#L10##b<샤레니안의 지하 수로>에 대해 알려주세요#l\r\n";
            cm.sendOk(말);
        }
    } else if (status == 2) {
        if (cm.getPlayerStat("GID") > 0) {
            if (sel == 2) {
                cm.sendYesNo("현재 길드 최대 인원 수는 #b#k 이고, #b10명#k 늘리는데 필요한 수수료는 #bGP 10000#k 일세. 참고로, 자네 길드는 현재 #bGP #k만 큼의 GP를 소지하고 있네. 어때 길드 인원을 늘릴텐가?");
            } else if (sel == 0 && cm.getPlayer().getGuildId() <= 0) {
                cm.genericGuildMessage(41);
                cm.dispose();
            } else if (sel == 1 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
                cm.dispose();
                cm.disbandGuild();
            } else if (sel == 3 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
                cm.increaseGuildCapacity(true);
                cm.dispose();
            }
        } else {
            sel = selection;
            if (selection == 0) {
                if (cm.getPlayerStat("GID") > 0) {
                    cm.sendOk("흐음.. 이미 길드에 가입되어 있는 것 같은데?");
                    cm.dispose();
                } else
                    cm.sendYesNo("오! 길드를 등록하러 왔군.. 길드를 등록하려면 500만 메소가 필요하다네. 준비는 되어있을 것이라 믿겠네. 자~ 길드를 만들겠는가?");
            } else if (selection == 1) {
                if (cm.getPlayerStat("GID") < 0 || cm.getPlayerStat("GRANK") != 1) {
                    cm.sendOk("길드장만이 길드를 해체할 수 있다네.");
                    cm.dispose();
                } else
                    cm.sendYesNo("길드를 해체하고 싶은가....? 지금 해체하게 된다면 절대 되돌릴 수 없다네.. 또, 모아뒀던 GP는 모두 사라지게 된다네. 길드 해체는 신중하게 선택하도록 하게나. 다시 한번 생각해보기 바라네. 정말 길드를 해체하고 싶은가?");
            } else if (selection == 2) {
                if (cm.getPlayerStat("GID") < 0 || cm.getPlayerStat("GRANK") != 1) {
                    cm.sendOk("길드장만이 길드 인원을 늘릴 수 있다네.");
                    cm.dispose();
                } else
                    cm.sendYesNo("길드 최대 인원 추가 비용은 #b50,000#k 길드포인트와 #r30,000,000#k만 메소라네. 지금 추가하면 최대 인원이 10명 만큼 더 늘어날걸세. 정말 최대 인원을 늘려보고 싶은가?");
            } else if (selection == 3) {
                if (cm.getPlayerStat("GID") < 0 || cm.getPlayerStat("GRANK") != 1) {
                    cm.sendOk("길드장만이 길드 인원을 늘릴 수 있다네.");
                    cm.dispose();
                } else
                    cm.sendYesNo("길드 최대 인원 추가 비용은 #b50,000#k 길드포인트와 #r30,000,000#k만 메소라네. 지금 추가하면 최대 인원이 10명 만큼 더 늘어날걸세. 정말 최대 인원을 늘려보고 싶은가?");
            } else if (selection == 10) {
                cm.sendNext("길드를 만들려면 레벨이 적어도 101은 되어야 하지.");
            } else if (selection == 100) {
                cm.sendNext("길드란.. 우선 소모임 같은 것으로 이해하면 되네. 서로 마음이 맞는 사람들끼리 같은 목적을 가지고 모임을 만든 것이네. 하지만 길드는 그런 목적으로 만든 소모임을 길드 본부에 정식 등록을 해서 공식적으로 인정된 모임이지.");
            }
        }
    } else if (status == 3) {
        if (sel == 10) {
            cm.sendNext("그리고 500만 메소가 필요해.. 이건 길드를 등록하는데 필요한 수수료라네.");
        }
        if (sel == 100) {
            cm.sendNext("길드 활동을 통해 여러가지 혜택을 얻을 수도 있지. 예를 들어, 길드 스킬이나 길드 전용 아이템 같은 것을 얻을 수 있다네.");
        }

        if (sel == 0 && cm.getPlayer().getGuildId() <= 0) {
            cm.genericGuildMessage(41);
            cm.dispose();
        } else if (sel == 1 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
            cm.disbandGuild();
            cm.dispose();
        } else if (sel == 2 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
            cm.increaseGuildCapacity(true);
            cm.dispose();
        } else if (sel == 3 && cm.getPlayerStat("GID") > 0 && cm.getPlayerStat("GRANK") == 1) {
            cm.increaseGuildCapacity(true);
            cm.dispose();
        }
    } else if (status == 4) {
        if (sel == 10) {
            cm.sendNext("자.. 길드를 등록하고 싶다면 내게 오라고~\r\n아! 물론 이미 다른 길드에 가입되어 있으면 안돼!!");
        }
    }
}