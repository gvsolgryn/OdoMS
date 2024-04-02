importPackage(java.io);
importPackage(Packages.client.items);
importPackage(Packages.client.inventory);
importPackage(Packages.constants);
importPackage(Packages.server);
importPackage(Packages.server.items);

var status = -1;

bok9arr1 = [];
bok9arr2 = [];
bok9arr3 = [];
bok9arr4 = [];
bok9arr5 = [];
bok9arr6 = [];
bok9arr7 = [];
bok9arr8 = [];

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        a = new Date();
        cm.sendSimple("#e<장비 백업>#n\r\n\r\n"
                + "아이템 장비 탭에 있는 장비 아이템들을 서버에 저장합니다.\r\n\r\n"
                + "#r#e[유의사항]#k#n\r\n"
                + "장비백업을 여러번 할 경우, #b기존에 백업된 파일은 자동으로 삭제#k되니 유의하시길 바랍니다."
                + "\r\n\r\n#L0# #d장비를 백업하겠습니다.\r\n"
                + "#L1# 백업된 장비를 불러오겠습니다.\r\n     (장비 및 착용된 아이템이 초기화됩니다.)")

    } else if (status == 1) {
        if (selection == 0) {
            말 = "백업되는 장비의 종류는 아래와 같습니다.\r\n\r\n"
            말 += "#e[장착]#n\r\n"
            for (i = 0; i > -199; i--) {
                Chr = cm.getInventory(-1).getItem(i)
                if (Chr != null) {
                    말 += "#i" + Chr.getItemId() + "#";
                }
            }
            말 += "\r\n\r\n#e[장비]#n\r\n"
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                Eqp = cm.getInventory(1).getItem(i)
                if (Eqp != null) {
                    말 += "#i" + Eqp.getItemId() + "#";
                }
            }
            말 += "\r\n\r\n#e[소비]#n\r\n"
            for (a = 0; a < cm.getInventory(2).getSlotLimit(); a++) {
                Cos = cm.getInventory(2).getItem(a)
                if (Cos != null) {
                    말 += "#i" + Cos.getItemId() + "#";
                }
            }
            말 += "\r\n\r\n#e[기타]#n\r\n"
            for (b = 0; b < cm.getInventory(4).getSlotLimit(); b++) {
                Etc = cm.getInventory(4).getItem(b)
                if (Etc != null) {
                    말 += "#i" + Etc.getItemId() + "#";
                }
            }
            말 += "\r\n\r\n#e[설치]#n\r\n"
            for (c = 0; c < cm.getInventory(3).getSlotLimit(); c++) {
                Ist = cm.getInventory(3).getItem(c)
                if (Ist != null) {
                    말 += "#i" + Ist.getItemId() + "#";
                }
            }
            말 += "\r\n\r\n#e[캐시]#n\r\n"
            for (d = 0; d < cm.getInventory(5).getSlotLimit(); d++) {
                Cas = cm.getInventory(5).getItem(d)
                if ((Cas != null) && !GameConstants.isPet(Cas.getItemId())) {
                    말 += "#i" + Cas.getItemId() + "#";
                }
            }
            말 += "\r\n\r\n#e[펫]#n\r\n"
            for (e = 0; e < cm.getInventory(5).getSlotLimit(); e++) {
                Pet = cm.getInventory(5).getItem(e)
                if ((Pet != null) && GameConstants.isPet(Pet.getItemId())) {
                    말 += "#i" + Pet.getItemId() + "#";
                }
            }
            말 += "\r\n\r\n#e[치장]#n\r\n"
            for (a = 0; a < cm.getInventory(6).getSlotLimit(); a++) {
                Cody = cm.getInventory(6).getItem(a)
                if (Cody != null) {
                    말 += "#i" + Cody.getItemId() + "#";
                }
            }
            말 += "\r\n#d#L0# 장비 백업을 시작하겠습니다."
            cm.sendSimple(말);
        } else {
            fFile1 = new File("Log/백업로그/Eqp/Backup_" + cm.getPlayer().getId() + ".log");
            fFile2 = new File("Log/백업로그/Consume/Backup_" + cm.getPlayer().getId() + ".log");
            fFile3 = new File("Log/백업로그/Etc/Backup_" + cm.getPlayer().getId() + ".log");
            fFile4 = new File("Log/백업로그/Install/Backup_" + cm.getPlayer().getId() + ".log");
            fFile5 = new File("Log/백업로그/Cash/Backup_" + cm.getPlayer().getId() + ".log");
            fFile6 = new File("Log/백업로그/Pet/Backup_" + cm.getPlayer().getId() + ".log");
            fFile7 = new File("Log/백업로그/Chr/Backup_" + cm.getPlayer().getId() + ".log");
            fFile8 = new File("Log/백업로그/Cody/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile1.exists() && !fFile2.exists() && !fFile3.exists() && !fFile4.exists() && !fFile5.exists() && !fFile6.exists() && !fFile7.exists() && !fFile8.exists()) {
                cm.sendOk("해당 캐릭터의 장비백업 파일이 존재하지 않습니다.\r\n\r\n장비를 제대로 백업하셨는지 확인해 주세요.")
                cm.dispose();
            } else {
                infile1 = new BufferedReader(new FileReader(fFile1));
                put = "";
                msg = "";
                while ((put = infile1.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile1.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr1.push(msg);
                bok9arr1 = JSON.parse(bok9arr1);

                infile2 = new BufferedReader(new FileReader(fFile2));
                put = "";
                msg = "";
                while ((put = infile2.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile2.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr2.push(msg);
                bok9arr2 = JSON.parse(bok9arr2);

                infile3 = new BufferedReader(new FileReader(fFile3));
                put = "";
                msg = "";
                while ((put = infile3.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile3.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr3.push(msg);
                bok9arr3 = JSON.parse(bok9arr3);

                infile4 = new BufferedReader(new FileReader(fFile4));
                put = "";
                msg = "";
                while ((put = infile4.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile4.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr4.push(msg);
                bok9arr4 = JSON.parse(bok9arr4);

                infile5 = new BufferedReader(new FileReader(fFile5));
                put = "";
                msg = "";
                while ((put = infile5.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile5.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr5.push(msg);
                bok9arr5 = JSON.parse(bok9arr5);

                infile6 = new BufferedReader(new FileReader(fFile6));
                put = "";
                msg = "";
                while ((put = infile6.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile6.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr6.push(msg);
                bok9arr6 = JSON.parse(bok9arr6);

                infile7 = new BufferedReader(new FileReader(fFile7));
                put = "";
                msg = "";
                while ((put = infile7.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile7.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr7.push(msg);
                bok9arr7 = JSON.parse(bok9arr7);

                infile8 = new BufferedReader(new FileReader(fFile8));
                put = "";
                msg = "";
                while ((put = infile8.readLine()) != null) {
                    if (!msg.contains("\r\n" + put)) {
                        msg += "\r\n" + put;
                    }
                }
                infile8.close();
                msg = msg.replace(/'/g, '\"');
                bok9arr8.push(msg);
                bok9arr8 = JSON.parse(bok9arr8);

                말 = "#b#e캐릭터 코드#k#n : " + bok9arr1[0] + "\r\n";
                말 += "#b#e백업 기준일#k#n : " + bok9arr1[1] + "\r\n"
                말 += "#b#e검색된 백업 파일 종류#k#n : "
                if (fFile1.exists()) {
                    말 += "장비 "
                }
                if (fFile2.exists()) {
                    말 += "소비 "
                }
                if (fFile3.exists()) {
                    말 += "기타 "
                }
                if (fFile4.exists()) {
                    말 += "설치 "
                }
                if (fFile5.exists()) {
                    말 += "캐시 "
                }
                if (fFile6.exists()) {
                    말 += "펫 "
                }
                if (fFile7.exists()) {
                    말 += "장착 "
                }
                if (fFile8.exists()) {
                    말 += "치장 "
                }
                말 += "\r\n\r\n복구를 시작하시겠습니까?\r\n\r\n";
                말 += "#d#L1# 복구를 시작하겠습니다."
                cm.sendSimple(말);
            }
        }
    } else if (status == 2) {
        if (selection == 0) {
            a = new Date();

            // 장비백업 시작

            fFile1 = new File("Log/백업로그/Eqp/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile1.exists()) {
                fFile1.createNewFile();
            }
            out1 = new FileOutputStream("Log/백업로그/Eqp/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (i = 0; i < cm.getInventory(1).getSlotLimit(); i++) {
                Eqp = cm.getInventory(1).getItem(i)
                if (Eqp != null) {
                    msg += ", [" + Eqp.getItemId() + ", "
                    msg += Eqp.getStr() + ", "
                    msg += Eqp.getDex() + ", "
                    msg += Eqp.getInt() + ", "
                    msg += Eqp.getLuk() + ", "
                    msg += Eqp.getArc() + ", "
                    msg += Eqp.getArcEXP() + ", "
                    msg += Eqp.getArcLevel() + ", "
                    msg += Eqp.getHp() + ", "
                    msg += Eqp.getMp() + ", "
                    msg += Eqp.getWatk() + ", "
                    msg += Eqp.getMatk() + ", "
                    msg += Eqp.getWdef() + ", "
                    msg += Eqp.getMdef() + ", "
                    msg += Eqp.getAcc() + ", "
                    msg += Eqp.getAvoid() + ", "
                    msg += Eqp.getHands() + ", "
                    msg += Eqp.getSpeed() + ", "
                    msg += Eqp.getJump() + ", "
                    msg += Eqp.getViciousHammer() + ", "
                    msg += Eqp.getItemEXP() + ", "
                    msg += Eqp.getDurability() + ", "
                    msg += Eqp.getEnhance() + ", "
                    msg += Eqp.getState() + ", "
                    msg += Eqp.getLines() + ", "
                    msg += Eqp.getPotential1() + ", "
                    msg += Eqp.getPotential2() + ", "
                    msg += Eqp.getPotential3() + ", "
                    msg += Eqp.getPotential4() + ", "
                    msg += Eqp.getPotential5() + ", "
                    msg += Eqp.getPotential6() + ", "
                    msg += Eqp.getIncSkill() + ", "
                    msg += Eqp.getCharmEXP() + ", "
                    msg += Eqp.getPVPDamage() + ", "
                    msg += Eqp.getEnchantBuff() + ", "
                    msg += Eqp.getReqLevel() + ", "
                    msg += Eqp.getYggdrasilWisdom() + ", "
                    msg += Eqp.getFinalStrike() + ", "
                    msg += Eqp.getBossDamage() + ", "
                    msg += Eqp.getIgnorePDR() + ", "
                    msg += Eqp.getTotalDamage() + ", "
                    msg += Eqp.getAllStat() + ", "
                    msg += Eqp.getKarmaCount() + ", "
                    msg += Eqp.getSoulName() + ", "
                    msg += Eqp.getSoulEnchanter() + ", "
                    msg += Eqp.getSoulPotential() + ", "
                    msg += Eqp.getSoulSkill() + ", "
                    msg += Eqp.getFire() + ", "
                    msg += Eqp.getEquipmentType() + ", "
                    msg += Eqp.getMoru() + ", "
                    msg += Eqp.getAttackSpeed() + ", "
                    msg += Eqp.getOptionExpiration() + ", "
                    msg += Eqp.getCoption1() + ", "
                    msg += Eqp.getCoption2() + ", "
                    msg += Eqp.getCoption3() + ", "
                    msg += Eqp.getEnchantStr() + ", "
                    msg += Eqp.getEnchantDex() + ", "
                    msg += Eqp.getEnchantInt() + ", "
                    msg += Eqp.getEnchantLuk() + ", "
                    msg += Eqp.getEnchantHp() + ", "
                    msg += Eqp.getEnchantMp() + ", "
                    msg += Eqp.getEnchantWatk() + ", "
                    msg += Eqp.getEnchantMatk() + ", "
                    msg += Eqp.getEnchantWdef() + ", "
                    msg += Eqp.getEnchantMdef() + ", "
                    msg += Eqp.getEnchantAcc() + ", "
                    msg += Eqp.getEnchantAvoid() + ", "
                    msg += Eqp.getUpgradeSlots() + ", "
                    msg += Eqp.getLevel() + "] "
                }
            }
            msg += "]"
            out1.write(msg.getBytes());
            out1.close();

            // 장비백업 끝
            // 소비백업 시작

            fFile2 = new File("Log/백업로그/Consume/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile2.exists()) {
                fFile2.createNewFile();
            }
            out2 = new FileOutputStream("Log/백업로그/Consume/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (b = 0; b < cm.getInventory(2).getSlotLimit(); b++) {
                Cos = cm.getInventory(2).getItem(b)
                if (Cos != null) {
                    msg += ", [" + Cos.getItemId() + ", "
                    msg += Cos.getQuantity() + "] "
                }
            }
            msg += "]"
            out2.write(msg.getBytes());
            out2.close();

            // 소비백업 끝
            // 기타백업 시작

            fFile3 = new File("Log/백업로그/Etc/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile3.exists()) {
                fFile3.createNewFile();
            }
            out3 = new FileOutputStream("Log/백업로그/Etc/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (c = 0; c < cm.getInventory(4).getSlotLimit(); c++) {
                Etc = cm.getInventory(4).getItem(c)
                if (Etc != null) {
                    msg += ", [" + Etc.getItemId() + ", "
                    msg += Etc.getQuantity() + "] "
                }
            }
            msg += "]"
            out3.write(msg.getBytes());
            out3.close();

            // 기타백업 끝
            // 설치백업 시작

            fFile4 = new File("Log/백업로그/Install/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile4.exists()) {
                fFile4.createNewFile();
            }
            out4 = new FileOutputStream("Log/백업로그/Install/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (d = 0; d < cm.getInventory(3).getSlotLimit(); d++) {
                Ins = cm.getInventory(3).getItem(d)
                if (Ins != null) {
                    msg += ", [" + Ins.getItemId() + ", "
                    msg += Ins.getQuantity() + "] "
                }
            }
            msg += "]"
            out4.write(msg.getBytes());
            out4.close();

            // 설치백업 끝
            // 캐시백업 시작

            fFile5 = new File("Log/백업로그/Cash/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile5.exists()) {
                fFile5.createNewFile();
            }
            out5 = new FileOutputStream("Log/백업로그/Cash/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (e = 0; e < cm.getInventory(5).getSlotLimit(); e++) {
                Cas = cm.getInventory(5).getItem(e)
                if ((Cas != null) && !GameConstants.isPet(Cas.getItemId())) {
                    msg += ", [" + Cas.getItemId() + ", "
                    msg += Cas.getQuantity() + "] "
                }
            }
            msg += "]"
            out5.write(msg.getBytes());
            out5.close();

            // 캐시백업 끝
            // 펫백업 시작

            fFile6 = new File("Log/백업로그/Pet/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile6.exists()) {
                fFile6.createNewFile();
            }
            out6 = new FileOutputStream("Log/백업로그/Pet/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (e = 0; e < cm.getInventory(5).getSlotLimit(); e++) {
                Pet = cm.getInventory(5).getItem(e)
                if ((Pet != null) && GameConstants.isPet(Pet.getItemId())) {
                    msg += ", [" + Pet.getItemId() + ", "
                    msg += Pet.getQuantity() + "] "
                }
            }
            msg += "]"
            out6.write(msg.getBytes());
            out6.close();

            // 펫백업 끝
            // 장착아이템 백업 시작
            fFile7 = new File("Log/백업로그/Chr/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile7.exists()) {
                fFile7.createNewFile();
            }
            out7 = new FileOutputStream("Log/백업로그/Chr/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (f = 0; f > -199; f--) {
                Chr = cm.getInventory(-1).getItem(f)
                if (Chr != null) {
                    msg += ", [" + Chr.getItemId() + ", "
                    msg += Chr.getStr() + ", "
                    msg += Chr.getDex() + ", "
                    msg += Chr.getInt() + ", "
                    msg += Chr.getLuk() + ", "
                    msg += Chr.getArc() + ", "
                    msg += Chr.getArcEXP() + ", "
                    msg += Chr.getArcLevel() + ", "
                    msg += Chr.getHp() + ", "
                    msg += Chr.getMp() + ", "
                    msg += Chr.getWatk() + ", "
                    msg += Chr.getMatk() + ", "
                    msg += Chr.getWdef() + ", "
                    msg += Chr.getMdef() + ", "
                    msg += Chr.getAcc() + ", "
                    msg += Chr.getAvoid() + ", "
                    msg += Chr.getHands() + ", "
                    msg += Chr.getSpeed() + ", "
                    msg += Chr.getJump() + ", "
                    msg += Chr.getViciousHammer() + ", "
                    msg += Chr.getItemEXP() + ", "
                    msg += Chr.getDurability() + ", "
                    msg += Chr.getEnhance() + ", "
                    msg += Chr.getState() + ", "
                    msg += Chr.getLines() + ", "
                    msg += Chr.getPotential1() + ", "
                    msg += Chr.getPotential2() + ", "
                    msg += Chr.getPotential3() + ", "
                    msg += Chr.getPotential4() + ", "
                    msg += Chr.getPotential5() + ", "
                    msg += Chr.getPotential6() + ", "
                    msg += Chr.getIncSkill() + ", "
                    msg += Chr.getCharmEXP() + ", "
                    msg += Chr.getPVPDamage() + ", "
                    msg += Chr.getEnchantBuff() + ", "
                    msg += Chr.getReqLevel() + ", "
                    msg += Chr.getYggdrasilWisdom() + ", "
                    msg += Chr.getFinalStrike() + ", "
                    msg += Chr.getBossDamage() + ", "
                    msg += Chr.getIgnorePDR() + ", "
                    msg += Chr.getTotalDamage() + ", "
                    msg += Chr.getAllStat() + ", "
                    msg += Chr.getKarmaCount() + ", "
                    msg += Chr.getSoulName() + ", "
                    msg += Chr.getSoulEnchanter() + ", "
                    msg += Chr.getSoulPotential() + ", "
                    msg += Chr.getSoulSkill() + ", "
                    msg += Chr.getFire() + ", "
                    msg += Chr.getEquipmentType() + ", "
                    msg += Chr.getMoru() + ", "
                    msg += Chr.getAttackSpeed() + ", "
                    msg += Chr.getOptionExpiration() + ", "
                    msg += Chr.getCoption1() + ", "
                    msg += Chr.getCoption2() + ", "
                    msg += Chr.getCoption3() + ", "
                    msg += Chr.getCoption3() + ", "
                    msg += Chr.getEnchantStr() + ", "
                    msg += Chr.getEnchantDex() + ", "
                    msg += Chr.getEnchantInt() + ", "
                    msg += Chr.getEnchantLuk() + ", "
                    msg += Chr.getEnchantHp() + ", "
                    msg += Chr.getEnchantMp() + ", "
                    msg += Chr.getEnchantWatk() + ", "
                    msg += Chr.getEnchantMatk() + ", "
                    msg += Chr.getEnchantWdef() + ", "
                    msg += Chr.getEnchantMdef() + ", "
                    msg += Chr.getEnchantAcc() + ", "
                    msg += Chr.getEnchantAvoid() + ", "
                    msg += Chr.getUpgradeSlots() + ", "
                    msg += Chr.getLevel() + "] "
                }
            }
            msg += "]"
            out7.write(msg.getBytes());
            out7.close();

            // 장착아이템 백업 끝


            // 코디아이템 백업 시작
            fFile8 = new File("Log/백업로그/Cody/Backup_" + cm.getPlayer().getId() + ".log");
            if (!fFile8.exists()) {
                fFile8.createNewFile();
            }
            out8 = new FileOutputStream("Log/백업로그/Cody/Backup_" + cm.getPlayer().getId() + ".log", false);
            msg = "['"
            msg += cm.getPlayer().getId()
            msg += "', "
            msg += "'" + a.getFullYear() + "년 " + Number(a.getMonth() + 1) + "월 " + a.getDate() + "일'"
            for (i = 0; i < cm.getInventory(6).getSlotLimit(); i++) {
                Eqp = cm.getInventory(6).getItem(i)
                if (Eqp != null) {
                    msg += ", [" + Eqp.getItemId() + ", "
                    msg += Eqp.getStr() + ", "
                    msg += Eqp.getDex() + ", "
                    msg += Eqp.getInt() + ", "
                    msg += Eqp.getLuk() + ", "
                    msg += Eqp.getArc() + ", "
                    msg += Eqp.getArcEXP() + ", "
                    msg += Eqp.getArcLevel() + ", "
                    msg += Eqp.getHp() + ", "
                    msg += Eqp.getMp() + ", "
                    msg += Eqp.getWatk() + ", "
                    msg += Eqp.getMatk() + ", "
                    msg += Eqp.getWdef() + ", "
                    msg += Eqp.getMdef() + ", "
                    msg += Eqp.getAcc() + ", "
                    msg += Eqp.getAvoid() + ", "
                    msg += Eqp.getHands() + ", "
                    msg += Eqp.getSpeed() + ", "
                    msg += Eqp.getJump() + ", "
                    msg += Eqp.getViciousHammer() + ", "
                    msg += Eqp.getItemEXP() + ", "
                    msg += Eqp.getDurability() + ", "
                    msg += Eqp.getEnhance() + ", "
                    msg += Eqp.getState() + ", "
                    msg += Eqp.getLines() + ", "
                    msg += Eqp.getPotential1() + ", "
                    msg += Eqp.getPotential2() + ", "
                    msg += Eqp.getPotential3() + ", "
                    msg += Eqp.getPotential4() + ", "
                    msg += Eqp.getPotential5() + ", "
                    msg += Eqp.getPotential6() + ", "
                    msg += Eqp.getIncSkill() + ", "
                    msg += Eqp.getCharmEXP() + ", "
                    msg += Eqp.getPVPDamage() + ", "
                    msg += Eqp.getEnchantBuff() + ", "
                    msg += Eqp.getReqLevel() + ", "
                    msg += Eqp.getYggdrasilWisdom() + ", "
                    msg += Eqp.getFinalStrike() + ", "
                    msg += Eqp.getBossDamage() + ", "
                    msg += Eqp.getIgnorePDR() + ", "
                    msg += Eqp.getTotalDamage() + ", "
                    msg += Eqp.getAllStat() + ", "
                    msg += Eqp.getKarmaCount() + ", "
                    msg += Eqp.getSoulName() + ", "
                    msg += Eqp.getSoulEnchanter() + ", "
                    msg += Eqp.getSoulPotential() + ", "
                    msg += Eqp.getSoulSkill() + ", "
                    msg += Eqp.getFire() + ", "
                    msg += Eqp.getEquipmentType() + ", "
                    msg += Eqp.getMoru() + ", "
                    msg += Eqp.getAttackSpeed() + ", "
                    msg += Eqp.getOptionExpiration() + ", "
                    msg += Eqp.getCoption1() + ", "
                    msg += Eqp.getCoption2() + ", "
                    msg += Eqp.getCoption3() + ", "
                    msg += Eqp.getCoption3() + ", "
                    msg += Eqp.getEnchantStr() + ", "
                    msg += Eqp.getEnchantDex() + ", "
                    msg += Eqp.getEnchantInt() + ", "
                    msg += Eqp.getEnchantLuk() + ", "
                    msg += Eqp.getEnchantHp() + ", "
                    msg += Eqp.getEnchantMp() + ", "
                    msg += Eqp.getEnchantWatk() + ", "
                    msg += Eqp.getEnchantMatk() + ", "
                    msg += Eqp.getEnchantWdef() + ", "
                    msg += Eqp.getEnchantMdef() + ", "
                    msg += Eqp.getEnchantAcc() + ", "
                    msg += Eqp.getEnchantAvoid() + ", "
                    msg += Eqp.getUpgradeSlots() + ", "
                    msg += Eqp.getLevel() + "] "
                }
            }
            msg += "]"
            out8.write(msg.getBytes());
            out8.close();

            cm.sendOk("백업이 완료되었습니다")
            cm.dispose();
        } else {
            for (a = 0; a < cm.getInventory(1).getSlotLimit(); a++) {
                if (cm.getInventory(1).getItem(a) != null) {
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.EQUIP, a, cm.getInventory(1).getItem(a).getQuantity(), false);
                }
            }

            for (b = 0; b < cm.getInventory(2).getSlotLimit(); b++) {
                if (cm.getInventory(2).getItem(b) != null) {
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.USE, b, cm.getInventory(2).getItem(b).getQuantity(), false);
                }
            }

            for (c = 0; c < cm.getInventory(4).getSlotLimit(); c++) {
                if (cm.getInventory(4).getItem(c) != null) {
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.ETC, c, cm.getInventory(4).getItem(c).getQuantity(), false);
                }
            }

            for (e = 0; e < cm.getInventory(3).getSlotLimit(); e++) {
                if (cm.getInventory(3).getItem(e) != null) {
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.SETUP, e, cm.getInventory(3).getItem(e).getQuantity(), false);
                }
            }

            for (f = 0; f < cm.getInventory(5).getSlotLimit(); f++) {
                if (cm.getInventory(5).getItem(f) != null) {
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.CASH, f, cm.getInventory(5).getItem(f).getQuantity(), false);
                }
            }

            for (f = 0; f < cm.getInventory(6).getSlotLimit(); f++) {
                if (cm.getInventory(6).getItem(f) != null) {
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.CODY, f, cm.getInventory(6).getItem(f).getQuantity(), false);
                }
            }

            for (i = 0; i > -199; i--) {
                if (cm.getInventory(-1).getItem(i) != null) {
                    MapleInventoryManipulator.removeFromSlot(cm.getClient(), MapleInventoryType.EQUIPPED, i, cm.getInventory(-1).getItem(i).getQuantity(), false);
                }
            }

            for (h = 0; h < bok9arr1.length - 2; h++) {
                realitem = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(bok9arr1[Number(h + 2)][0]);
                realitem.setStr(bok9arr1[Number(h + 2)][1])
                realitem.setDex(bok9arr1[Number(h + 2)][2])
                realitem.setInt(bok9arr1[Number(h + 2)][3])
                realitem.setLuk(bok9arr1[Number(h + 2)][4])
                realitem.setArc(bok9arr1[Number(h + 2)][5])
                realitem.setArcEXP(bok9arr1[Number(h + 2)][6])
                realitem.setArcLevel(bok9arr1[Number(h + 2)][7])
                realitem.setHp(bok9arr1[Number(h + 2)][8])
                realitem.setMp(bok9arr1[Number(h + 2)][9])
                realitem.setWatk(bok9arr1[Number(h + 2)][10])
                realitem.setMatk(bok9arr1[Number(h + 2)][11])
                realitem.setWdef(bok9arr1[Number(h + 2)][12])
                realitem.setMdef(bok9arr1[Number(h + 2)][13])
                realitem.setAcc(bok9arr1[Number(h + 2)][14])
                realitem.setAvoid(bok9arr1[Number(h + 2)][15])
                realitem.setHands(bok9arr1[Number(h + 2)][16])
                realitem.setSpeed(bok9arr1[Number(h + 2)][17])
                realitem.setJump(bok9arr1[Number(h + 2)][18])
                realitem.setViciousHammer(bok9arr1[Number(h + 2)][19])
                realitem.setItemEXP(bok9arr1[Number(h + 2)][20])
                realitem.setDurability(bok9arr1[Number(h + 2)][21])
                realitem.setEnhance(bok9arr1[Number(h + 2)][22])
                realitem.setState(bok9arr1[Number(h + 2)][23])
                realitem.setLines(bok9arr1[Number(h + 2)][24])
                realitem.setPotential1(bok9arr1[Number(h + 2)][25])
                realitem.setPotential2(bok9arr1[Number(h + 2)][26])
                realitem.setPotential3(bok9arr1[Number(h + 2)][27])
                realitem.setPotential4(bok9arr1[Number(h + 2)][28])
                realitem.setPotential5(bok9arr1[Number(h + 2)][29])
                realitem.setPotential6(bok9arr1[Number(h + 2)][30])
                realitem.setIncSkill(bok9arr1[Number(h + 2)][31])
                realitem.setCharmEXP(bok9arr1[Number(h + 2)][32])
                realitem.setPVPDamage(bok9arr1[Number(h + 2)][33])
                realitem.setEnchantBuff(bok9arr1[Number(h + 2)][34])
                realitem.setReqLevel(bok9arr1[Number(h + 2)][35])
                realitem.setYggdrasilWisdom(bok9arr1[Number(h + 2)][36])
                realitem.setFinalStrike(bok9arr1[Number(h + 2)][37])
                realitem.setBossDamage(bok9arr1[Number(h + 2)][38])
                realitem.setIgnorePDR(bok9arr1[Number(h + 2)][39])
                realitem.setTotalDamage(bok9arr1[Number(h + 2)][40])
                realitem.setAllStat(bok9arr1[Number(h + 2)][41])
                realitem.setKarmaCount(bok9arr1[Number(h + 2)][42])
                realitem.setSoulName(bok9arr1[Number(h + 2)][43])
                realitem.setSoulEnchanter(bok9arr1[Number(h + 2)][44])
                realitem.setSoulPotential(bok9arr1[Number(h + 2)][45])
                realitem.setSoulSkill(bok9arr1[Number(h + 2)][46])
                realitem.setFire(bok9arr1[Number(h + 2)][47])
                realitem.setEquipmentType(bok9arr1[Number(h + 2)][48])
                realitem.setMoru(bok9arr1[Number(h + 2)][49])
                realitem.setAttackSpeed(bok9arr1[Number(h + 2)][50])
                realitem.setOptionExpiration(bok9arr1[Number(h + 2)][51])
                realitem.setCoption1(bok9arr1[Number(h + 2)][52])
                realitem.setCoption2(bok9arr1[Number(h + 2)][53])
                realitem.setCoption3(bok9arr1[Number(h + 2)][54])
                realitem.setEnchantStr(bok9arr1[Number(h + 2)][55])
                realitem.setEnchantDex(bok9arr1[Number(h + 2)][56])
                realitem.setEnchantInt(bok9arr1[Number(h + 2)][57])
                realitem.setEnchantLuk(bok9arr1[Number(h + 2)][58])
                realitem.setEnchantHp(bok9arr1[Number(h + 2)][59])
                realitem.setEnchantMp(bok9arr1[Number(h + 2)][60])
                realitem.setEnchantWatk(bok9arr1[Number(h + 2)][61])
                realitem.setEnchantMatk(bok9arr1[Number(h + 2)][62])
                realitem.setEnchantWdef(bok9arr1[Number(h + 2)][63])
                realitem.setEnchantMdef(bok9arr1[Number(h + 2)][64])
                realitem.setEnchantAcc(bok9arr1[Number(h + 2)][65])
                realitem.setEnchantAvoid(bok9arr1[Number(h + 2)][66])
                realitem.setUpgradeSlots(bok9arr1[Number(h + 2)][67])
                realitem.setLevel(bok9arr1[Number(h + 2)][68])
                MapleInventoryManipulator.addFromDrop(cm.getClient(), realitem, true);
            }

            for (d = 0; d < bok9arr7.length - 2; d++) {
                realitem2 = Packages.server.MapleItemInformationProvider.getInstance().getEquipById(bok9arr7[Number(d + 2)][0]);
                realitem2.setStr(bok9arr7[Number(h + 2)][1])
                realitem2.setDex(bok9arr7[Number(h + 2)][2])
                realitem2.setInt(bok9arr7[Number(h + 2)][3])
                realitem2.setLuk(bok9arr7[Number(h + 2)][4])
                realitem2.setArc(bok9arr7[Number(h + 2)][5])
                realitem2.setArcEXP(bok9arr7[Number(h + 2)][6])
                realitem2.setArcLevel(bok9arr7[Number(h + 2)][7])
                realitem2.setHp(bok9arr7[Number(h + 2)][8])
                realitem2.setMp(bok9arr7[Number(h + 2)][9])
                realitem2.setWatk(bok9arr7[Number(h + 2)][10])
                realitem2.setMatk(bok9arr7[Number(h + 2)][11])
                realitem2.setWdef(bok9arr7[Number(h + 2)][12])
                realitem2.setMdef(bok9arr7[Number(h + 2)][13])
                realitem2.setAcc(bok9arr7[Number(h + 2)][14])
                realitem2.setAvoid(bok9arr7[Number(h + 2)][15])
                realitem2.setHands(bok9arr7[Number(h + 2)][16])
                realitem2.setSpeed(bok9arr7[Number(h + 2)][17])
                realitem2.setJump(bok9arr7[Number(h + 2)][18])
                realitem2.setViciousHammer(bok9arr7[Number(h + 2)][19])
                realitem2.setItemEXP(bok9arr7[Number(h + 2)][20])
                realitem2.setDurability(bok9arr7[Number(h + 2)][21])
                realitem2.setEnhance(bok9arr7[Number(h + 2)][22])
                realitem2.setState(bok9arr7[Number(h + 2)][23])
                realitem2.setLines(bok9arr7[Number(h + 2)][24])
                realitem2.setPotential1(bok9arr7[Number(h + 2)][25])
                realitem2.setPotential2(bok9arr7[Number(h + 2)][26])
                realitem2.setPotential3(bok9arr7[Number(h + 2)][27])
                realitem2.setPotential4(bok9arr7[Number(h + 2)][28])
                realitem2.setPotential5(bok9arr7[Number(h + 2)][29])
                realitem2.setPotential6(bok9arr7[Number(h + 2)][30])
                realitem2.setIncSkill(bok9arr7[Number(h + 2)][31])
                realitem2.setCharmEXP(bok9arr7[Number(h + 2)][32])
                realitem2.setPVPDamage(bok9arr7[Number(h + 2)][33])
                realitem2.setEnchantBuff(bok9arr7[Number(h + 2)][34])
                realitem2.setReqLevel(bok9arr7[Number(h + 2)][35])
                realitem2.setYggdrasilWisdom(bok9arr7[Number(h + 2)][36])
                realitem2.setFinalStrike(bok9arr7[Number(h + 2)][37])
                realitem2.setBossDamage(bok9arr7[Number(h + 2)][38])
                realitem2.setIgnorePDR(bok9arr7[Number(h + 2)][39])
                realitem2.setTotalDamage(bok9arr7[Number(h + 2)][40])
                realitem2.setAllStat(bok9arr7[Number(h + 2)][41])
                realitem2.setKarmaCount(bok9arr7[Number(h + 2)][42])
                realitem2.setSoulName(bok9arr7[Number(h + 2)][43])
                realitem2.setSoulEnchanter(bok9arr7[Number(h + 2)][44])
                realitem2.setSoulPotential(bok9arr7[Number(h + 2)][45])
                realitem2.setSoulSkill(bok9arr7[Number(h + 2)][46])
                realitem2.setFire(bok9arr7[Number(h + 2)][47])
                realitem2.setEquipmentType(bok9arr7[Number(h + 2)][48])
                realitem2.setMoru(bok9arr7[Number(h + 2)][49])
                realitem2.setAttackSpeed(bok9arr7[Number(h + 2)][50])
                realitem2.setOptionExpiration(bok9arr7[Number(h + 2)][51])
                realitem2.setCoption1(bok9arr7[Number(h + 2)][52])
                realitem2.setCoption2(bok9arr7[Number(h + 2)][53])
                realitem2.setCoption3(bok9arr7[Number(h + 2)][54])
                realitem2.setEnchantStr(bok9arr7[Number(h + 2)][55])
                realitem2.setEnchantDex(bok9arr7[Number(h + 2)][56])
                realitem2.setEnchantInt(bok9arr7[Number(h + 2)][57])
                realitem2.setEnchantLuk(bok9arr7[Number(h + 2)][58])
                realitem2.setEnchantHp(bok9arr7[Number(h + 2)][59])
                realitem2.setEnchantMp(bok9arr7[Number(h + 2)][60])
                realitem2.setEnchantWatk(bok9arr7[Number(h + 2)][61])
                realitem2.setEnchantMatk(bok9arr7[Number(h + 2)][62])
                realitem2.setEnchantWdef(bok9arr7[Number(h + 2)][63])
                realitem2.setEnchantMdef(bok9arr7[Number(h + 2)][64])
                realitem2.setEnchantAcc(bok9arr7[Number(h + 2)][65])
                realitem2.setEnchantAvoid(bok9arr7[Number(h + 2)][66])
                realitem2.setUpgradeSlots(bok9arr7[Number(h + 2)][67])
                realitem2.setLevel(bok9arr7[Number(h + 2)][68])
                MapleInventoryManipulator.addFromDrop(cm.getClient(), realitem2, true);
            }

            for (j = 0; j < bok9arr2.length - 2; j++) {
                cm.gainItem(bok9arr2[Number(j + 2)][0], bok9arr2[Number(j + 2)][1]);
            }

            for (k = 0; k < bok9arr3.length - 2; k++) {
                cm.gainItem(bok9arr3[Number(k + 2)][0], bok9arr3[Number(k + 2)][1]);
            }

            for (l = 0; l < bok9arr4.length - 2; l++) {
                cm.gainItem(bok9arr4[Number(l + 2)][0], bok9arr4[Number(l + 2)][1]);
            }

            for (m = 0; m < bok9arr5.length - 2; m++) {
                cm.gainItem(bok9arr5[Number(m + 2)][0], bok9arr5[Number(m + 2)][1]);
            }

            for (m = 0; m < bok9arr8.length - 2; m++) {
                cm.gainItem(bok9arr8[Number(m + 2)][0], bok9arr8[Number(m + 2)][1]);
            }

            for (n = 0; n < bok9arr6.length - 2; n++) {
                cm.BuyPET(bok9arr6[Number(n + 2)][0])
            }

            cm.updateChar();
            cm.fakeRelog();
            cm.sendOk("복구가 완료되었습니다." + bok9arr1[Number(h + 2)][69]);
            cm.dispose();
        }
    }
}