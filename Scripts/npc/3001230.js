importPackage(Packages.database);

var status = 0;
var enter = "\r\n";
var S1, chr, sql_chr_id, sql_chr_accid;
var clientDateKeyValues = [["dailyGiftComplete", "데일리 기프트 클리어 여부"], ["dailyGiftDay", "데일리 기프트 완료 날짜"], ["goldDay", "황금마차 완료 날짜"], ["goldComplete", "황금마차 클리어 여부"], ["ht", "핫타임 획득 여부"], ["jump_1", "끈기의 숲 클리어 여부"], ["jump_2", "인내의 숲 클리어 여부"], ["jump_3", "고지를 향하여 클리어 여부"], ["mPark", "몬스터파크 클리어 횟수"], ["day_reborn", "하루 환생 제한 횟수"], ["day_qitem", "하루 알파벳 교환 제한 횟수"]];
var todayKeyValues = [];

function findChrFromSQL(chr_name) {
    var con, ps, rs;
    try {
        con = DatabaseConnection.getConnection();
        var sql_query = "SELECT * FROM `characters` WHERE `name` = ?";
        ps = con.prepareStatement(sql_query);
        ps.setString(1, chr_name);
        rs = ps.executeQuery();

        if (rs.next()) {
            sql_chr_id = rs.getInt("id");
            sql_chr_accid = rs.getInt("accountid");
            return true;
        }
        rs.close();
        ps.close();
        con.close();
    } catch (e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (e) {
            e.printStackTrace();
        }

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (e) {
            e.printStackTrace();
        }

        try {
            if (con != null) {
                con.close();
            }
        } catch (e) {
            e.printStackTrace();
        }
    }
    return false;
}

function updateSQL(table_name, table_key, table_value, table_chr) {
    var con, ps;
    try {
        con = DatabaseConnection.getConnection();
        var sql_query = "UPDATE `" + table_name + "` SET `value` = ? WHERE `key` = ?";
        if (table_chr != 0) {
            sql_query += " AND `id` = ?";
        }
        ps = con.prepareStatement(sql_query);
        ps.setString(1, table_value);
        ps.setString(2, table_key);
        if (table_chr != null) {
            ps.setInt(3, table_chr);
        }
        ps.executeUpdate();
        return true;
    } catch (e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (e) {
            e.printStackTrace();
        }

        try {
            if (con != null) {
                con.close();
            }
        } catch (e) {
            e.printStackTrace();
        }
    }
    return false;
}

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else {
        cm.dispose();
        return;
    }
    if (status == 1 && selection >= 1 && mode == 1) { // 채널 관련
        S1 = selection;
        status++;
    }
    switch (status) {
        case 0:
            if (!cm.getPlayer().isGM()) {
                cm.dispose();
                return;
            }
            txt = "키벨류 관련 도우미 입니다." + enter;
            txt += "원하시는 행동을 선택해주세요.#b" + enter;
            txt += "#L0#특정 캐릭터/계정에 접근하기" + enter;
            txt += "#L1#현재 채널의 모든 캐릭터/계정 동시에 변환하기" + enter;
            txt += "#L2#모든 채널의 모든 캐릭터/계정 동시에 변환하기" + enter;
            cm.sendSimple(txt);
            break;
        case 1:
            S1 = selection;
            cm.sendGetText("접근할 캐릭터의 이름을 입력해주세요.");
            break;
        case 2:
            switch (S1) {
                case 0:
                    name = cm.getText();
                    if (sql_chr_accid == null && sql_chr_id == null) {
                        ch = Packages.handling.world.World.Find.findChannel(name);
                        if (ch < 0) {
                            status = 10;
                            cm.sendOk("미접속중인 캐릭터입니다.");
                            return;
                        }
                        chr = Packages.handling.channel.ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(name);
                        if (chr == null) {
                            status = 10;
                            cm.sendOk("미접속중인 캐릭터입니다.");
                            return;
                        }
                    }
                    break;
            }
            if (chr != null) {
                txt = chr.getName() + " 캐릭터에 접속하였습니다." + enter;
            } else if (S1 == 1) {
                txt = cm.getClient().getChannel() + "채널에 접속하였습니다." + enter;
            } else if (sql_chr_id != null) {
                S1 = 3;
                txt = name + " 캐릭터의 SQL에 접속하였습니다." + enter;
            } else {
                txt = "모든 채널에 연결하였습니다." + enter;
            }
            txt += " 원하시는 항목을 골라주세요. #b" + enter;
            txt += "#L0#계정 당 1회 항목 관련 수정" + enter;
            txt += "#L1#캐릭터 당 1회 항목 관련 수정" + enter;
            cm.sendSimple(txt);
            break;
        case 3:
            S3 = selection;
            switch (S3) {
                case 0:
                    txt = "원하시는 항목을 골라주세요. #d" + enter;
                    for (i = 0; i < clientDateKeyValues.length; ++i) {
                        txt += "#L" + i + "#" + clientDateKeyValues[i][1] + enter;
                    }
                    cm.sendSimple(txt);
                    break;
                case 1:
                    txt = "원하시는 항목을 골라주세요." + enter;
                    txt += "#L0#일반 항목 수정" + enter;
                    txt += "#L1#컨텐츠 관련 항목 수정" + enter;
                    cm.sendSimple(txt);
                    break;
            }
            break;
        case 4:
            S4 = selection;
            switch (S3) {
                case 0:
                    switch (S1) {
                        case 0:
                            txt = "현재 접속중인 캐릭터 : #d" + chr.getName() + "#k" + enter;
                            break;
                        case 1:
                            txt = "현재 접속중인 채널 : #d" + cm.getClient().getChannel() + "#k" + enter;
                            break;
                        case 2:
                            txt = "현재 모든 채널에 연결중입니다." + enter;
                            break;
                        case 3:
                            txt = "현재 접속중인 캐릭터 : #d" + name + "#k" + enter;
                            break;
                        default:
                            cm.sendOk("잘못된 접근입니다 : " + S1);
                            cm.dispose();
                            return;
                    }
                    txt += "현재 선택하신 항목 : #b" + clientDateKeyValues[S4][1] + "#k" + enter;
                    txt += "원하시는 값을 입력해주세요." + enter;
                    txt += "자정 초기화 기본 값 : 0" + enter;
                    if (S4 == 0 || S4 >= 3 && S4 <= 7)
                        txt += "#r클리어 여부, 0 : 미클리어 / 1 : 클리어#k";

                    cm.sendGetNumber(txt, 0, 0, 10000);
                    break;
                case 1:
                    switch (S4) {
                        case 0:
                            todayKeyValues.push(["muto", "0", "배고픈 무토 클리어 횟수"]);
                            todayKeyValues.push(["arcane_quest_2", "-1", "소멸의 여로 일일 퀘스트 클리어 횟수"]);
                            todayKeyValues.push(["arcane_quest_3", "-1", "츄츄 아일랜드 일일 퀘스트 클리어 횟수"]);
                            todayKeyValues.push(["arcane_quest_4", "-1", "레헬른 일일 퀘스트 클리어 횟수"]);
                            todayKeyValues.push(["arcane_quest_5", "-1", "아르카나 일일 퀘스트 클리어 횟수"]);
                            todayKeyValues.push(["arcane_quest_6", "-1", "모라스 일일 퀘스트 클리어 횟수"]);
                            todayKeyValues.push(["arcane_quest_7", "-1", "에스페라 일일 퀘스트 클리어 횟수"]);
                            todayKeyValues.push(["NettPyramid", "0", "네트의 피라미드 클리어 횟수"]);
                            todayKeyValues.push(["linkMobCount", "0", "프로즌 링크 충전 횟수"]);

                            txt = "원하시는 항목을 골라주세요." + enter;
                            for (i = 0; i < todayKeyValues.length; ++i) {
                                txt += "#b#L" + i + "#" + todayKeyValues[i][2] + enter;
                            }
                            break;
                        case 1:
                            arr = cm.getClient().getChannelServer().getEventSM().getEvents().keySet();
                            arr.forEach(function (event) {
                                todayKeyValues.push([event, "0"]);
                            });

                            txt = "원하시는 항목을 골라주세요. #d" + enter;
                            for (i = 0; i < todayKeyValues.length; ++i) {
                                txt += "#b#L" + i + "#" + todayKeyValues[i][0] + enter;
                            }
                            break;
                    }
                    cm.sendSimple(txt);
                    break;
            }
            break;
        case 5:
            S5 = selection;
            switch (S3) {
                case 0:
                    switch (S1) {
                        case 0:
                            txt = "현재 접속중인 캐릭터 : #d" + chr.getName() + "#k" + enter;
                            break;
                        case 1:
                            txt = "현재 접속중인 채널 : #d" + cm.getClient().getChannel() + "#k" + enter;
                            break;
                        case 2:
                            txt = "현재 모든 채널에 연결중입니다." + enter;
                            break;
                        case 3:
                            txt = "현재 접속중인 캐릭터 : #d" + name + "#k" + enter;
                            break;
                        default:
                            cm.sendOk("잘못된 접근입니다 : " + S1);
                            cm.dispose();
                            return;
                    }
                    txt += "현재 선택하신 항목 : " + clientDateKeyValues[S4][1] + enter;
                    txt += "현재 적용하실 값 : " + S5 + enter;
                    txt += "해당 값을 적용하시겠습니까?" + enter;
                    txt += "#r한번 더 신중히 체크 후 선택해주세요.#k";
                    cm.sendYesNo(txt);
                    break;
                case 1:
                    switch (S1) {
                        case 0:
                            txt = "현재 접속중인 캐릭터 : #d" + chr.getName() + "#k" + enter;
                            break;
                        case 1:
                            txt = "현재 접속중인 채널 : #d" + cm.getClient().getChannel() + "#k" + enter;
                            break;
                        case 2:
                            txt = "현재 모든 채널에 연결중입니다." + enter;
                            break;
                        case 3:
                            txt = "현재 접속중인 캐릭터 : #d" + name + "#k" + enter;
                            break;
                        default:
                            cm.sendOk("잘못된 접근입니다 : " + S1);
                            cm.dispose();
                            return;
                    }
                    switch (S4) {
                        case 0:
                            txt += "현재 선택하신 항목 : #b" + todayKeyValues[S5][2] + "#k" + enter;
                            break;
                        case 1:
                            txt += "현재 선택하신 항목 : #b" + todayKeyValues[S5][0] + "#k" + enter;
                            break;
                    }
                    txt += "원하시는 값을 입력해주세요." + enter;
                    txt += "자정 초기화 기본 값 : " + todayKeyValues[S5][1] + enter;

                    cm.sendGetNumber(txt, 0, 0, 100);
                    break;
            }
            break;
        case 6:
            S6 = selection;
            switch (S3) {
                case 0:
                    cm.dispose();
                    switch (S1) {
                        case 0:
                            if (chr != null) {
                                chr.getClient().setKeyValue(clientDateKeyValues[S4][0], S5);
                                cm.sendOk(chr.getName() + " 계정의 #r" + clientDateKeyValues[S4][1] + "#k값이 #b" + S5 + "#k로 변경되었습니다.");

                                if (S4 <= 1) {
                                    chr.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.updateDailyGift("count=0;day=" + chr.getClient().getKeyValue("dailyGiftDay") + ";date=" + Packages.constants.GameConstants.getCurrentDate_NoTime()));
                                }
                            }
                            break;
                        case 1:
                            arr = cm.getClient().getChannelServer().getPlayerStorage().getAllCharacters().values();
                            arr.forEach(function (chr) {
                                if (chr != null) {
                                    chr.getClient().setKeyValue(clientDateKeyValues[S4][0], S5);

                                    if (S4 <= 1) {
                                        chr.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.updateDailyGift("count=0;day=" + chr.getClient().getKeyValue("dailyGiftDay") + ";date=" + Packages.constants.GameConstants.getCurrentDate_NoTime()));
                                    }
                                }
                            });
                            cm.sendOk(cm.getClient().getChannel() + " 채널에 있는 모든 계정의 #r" + clientDateKeyValues[S4][1] + "#k값이 #b" + S5 + "#k로 변경되었습니다.");
                            break;
                        case 2:
                            arr = Packages.handling.channel.ChannelServer.getAllInstances();
                            arr.forEach(function (cs) {
                                arr2 = cs.getPlayerStorage().getAllCharacters().values();
                                arr2.forEach(function (chr) {
                                    if (chr != null) {
                                        chr.getClient().setKeyValue(clientDateKeyValues[S4][0], S5);

                                        if (S4 <= 1) {
                                            chr.getClient().getSession().writeAndFlush(Packages.tools.packet.CWvsContext.updateDailyGift("count=0;day=" + chr.getClient().getKeyValue("dailyGiftDay") + ";date=" + Packages.constants.GameConstants.getCurrentDate_NoTime()));
                                        }
                                    }
                                });
                            });
                            cm.sendOk("모든 채널에 있는 모든 계정의 #r" + clientDateKeyValues[S4][1] + "#k값이 #b" + S5 + "#k로 변경되었습니다.");
                            break;
                        case 3:

                            find_acc_ch = Packages.handling.world.World.Find.findAccChannel(sql_chr_accid);

                            if (find_acc_ch >= 0) {
                                find_acc_other_chr = Packages.handling.channel.ChannelServer.getInstance(find_acc_ch).getPlayerStorage().getClientById(sql_chr_accid);
                                if (find_acc_other_chr != null) {
                                    find_acc_other_chr.setKeyValue(clientDateKeyValues[S4][0], S5);
                                    cm.sendOk("현재 " + name + " 계정은 #b" + find_acc_other_chr.getPlayer().getName() + "#k 캐릭터로 접속중입니다. \r\n해당 캐릭터로 연결하여 #r" + clientDateKeyValues[S4][1] + "#k값을 #b" + S5 + "#k로 변경했습니다.");
                                } else {
                                    cm.sendOk("계정 탐색에 오류가 발생했습니다.");
                                }
                            } else {
                                if (updateSQL("acckeyvalue", clientDateKeyValues[S4][1], S5, sql_chr_accid)) {
                                    cm.sendOk(name + " 계정의 #r" + clientDateKeyValues[S4][1] + "#k값이 #b" + S5 + "#k로 변경되었습니다.");
                                } else {
                                    cm.sendOk("SQL 업데이트에 실패했습니다. 구동기의 오류를 참조해주세요.");
                                }
                            }
                        default:
                            cm.sendOk("잘못된 접근입니다 : " + S1);
                            return;
                    }


                    break;
                case 1:
                    switch (S1) {
                        case 0:
                            txt = "현재 접속중인 캐릭터 : #d" + chr.getName() + "#k" + enter;
                            break;
                        case 1:
                            txt = "현재 접속중인 채널 : #d" + cm.getClient().getChannel() + "#k" + enter;
                            break;
                        case 2:
                            txt = "현재 모든 채널에 연결중입니다." + enter;
                            break;
                        case 3:
                            txt = "현재 접속중인 캐릭터 : #d" + name + "#k" + enter;
                            break;
                        default:
                            cm.sendOk("잘못된 접근입니다 : " + S1);
                            cm.dispose();
                            return;
                    }
                    switch (S4) {
                        case 0:
                            txt += "현재 선택하신 항목 : #b" + todayKeyValues[S5][2] + "#k" + enter;
                            break;
                        case 1:
                            txt += "현재 선택하신 항목 : #b" + todayKeyValues[S5][0] + "#k" + enter;
                            break;
                    }
                    txt += "현재 적용하실 값 : " + S6 + enter;
                    txt += "해당 값을 적용하시겠습니까?" + enter;
                    txt += "#r한번 더 신중히 체크 후 선택해주세요.#k";
                    cm.sendYesNo(txt);
                    break;
            }
            break;
        case 7:
            cm.dispose();
            switch (S1) {
                case 0:
                    if (chr != null) {
                        chr.addKV(todayKeyValues[S5][0], S6);
                        switch (S4) {
                            case 0:
                                cm.sendOk(chr.getName() + " 캐릭터의 #r" + todayKeyValues[S5][2] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                                break;
                            case 1:
                                cm.sendOk(chr.getName() + " 캐릭터의 #r" + todayKeyValues[S5][0] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                                break;
                        }
                    }
                    break;
                case 1:
                    arr = cm.getClient().getChannelServer().getPlayerStorage().getAllCharacters().values();
                    arr.forEach(function (chr) {
                        if (chr != null) {
                            chr.addKV(todayKeyValues[S5][0], S6);
                        }
                    });
                    switch (S4) {
                        case 0:
                            cm.sendOk(cm.getClient().getChannel() + " 채널에 있는 모든 캐릭터의 #r" + todayKeyValues[S5][2] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                            break;
                        case 1:
                            cm.sendOk(cm.getClient().getChannel() + " 채널에 있는 모든 캐릭터의 #r" + todayKeyValues[S5][0] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                            break;
                    }
                    break;
                case 2:
                    arr = Packages.handling.channel.ChannelServer.getAllInstances();
                    arr.forEach(function (cs) {
                        arr2 = cs.getPlayerStorage().getAllCharacters().values();
                        arr2.forEach(function (chr) {
                            if (chr != null) {
                                chr.addKV(todayKeyValues[S5][0], S6);
                            }
                        });
                    });
                    switch (S4) {
                        case 0:
                            cm.sendOk("모든 채널에 있는 모든 캐릭터의 #r" + todayKeyValues[S5][2] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                            break;
                        case 1:
                            cm.sendOk("모든 채널에 있는 모든 캐릭터의 #r" + todayKeyValues[S5][0] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                            break;
                    }
                    break;
                case 3:
                    if (updateSQL("keyvalue", todayKeyValues[S5][0], S6, sql_chr_accid)) {
                        switch (S4) {
                            case 0:
                                cm.sendOk(name + " 캐릭터의 #r" + todayKeyValues[S5][2] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                                break;
                            case 1:
                                cm.sendOk(name + " 캐릭터의 #r" + todayKeyValues[S5][0] + "#k값이 #b" + S6 + "#k로 변경되었습니다.");
                                break;
                        }
                    } else {
                        cm.sendOk("SQL 업데이트에 실패했습니다. 구동기의 오류를 참조해주세요.");
                    }
                default:
                    cm.sendOk("잘못된 접근입니다 : " + S1);
                    cm.dispose();
                    return;
            }
        case 11:
            if (findChrFromSQL(name)) {
                txt = name + " 캐릭터를 SQL 데이터에서 찾았습니다." + enter;
                txt += name + " 캐릭터의 캐릭터 ID : " + sql_chr_id + enter;
                txt += name + " 캐릭터의 계정 ID : " + sql_chr_accid;
                status = 1;
                cm.sendOk(txt);
            } else {
                txt = name + " 캐릭터를 SQL 데이터에서 찾을 수 없습니다." + enter;
                txt += "아마도 삭제되거나, 닉네임이 변경된 캐릭터일 수 있습니다.";
                cm.sendOk(txt);
                cm.dispose();
            }
            break;
    }
}