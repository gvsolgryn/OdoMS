/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//파이날HP
/*package constants;

import static constants.ServerConstants.toUni;
import java.util.*;
import java.io.*;

public class FinalMaxHpConstants {

    public static List<Integer> finalMonsterMaxHpMobCode = new ArrayList<Integer>();
    public static List<Long> finalMonsterMaxHp = new ArrayList<Long>();

    static {
        try {
            FileInputStream setting = new FileInputStream("property/finalMaxHp.ini");
            Properties setting_ = new Properties();
            setting_.load(setting);
            setting.close();

            String FInalMonsterHpCode = setting_.getProperty(toUni("파이널몬스터코드"));
            if (!FInalMonsterHpCode.isEmpty()) {
                String FInalMonsterHpCodes[] = FInalMonsterHpCode.split(",");
                for (int i = 0; i < FInalMonsterHpCodes.length; i++) {
                    finalMonsterMaxHpMobCode.add(Integer.parseInt(FInalMonsterHpCodes[i]));
                }
            }

            String FinalMonsterHp = setting_.getProperty(toUni("파이널몬스터체력"));
            if (!FinalMonsterHp.isEmpty()) {
                String FinalMonsterHps[] = FinalMonsterHp.split(",");
                for (int i = 0; i < FinalMonsterHps.length; i++) {
                    finalMonsterMaxHp.add(Long.parseLong(FinalMonsterHps[i]));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
