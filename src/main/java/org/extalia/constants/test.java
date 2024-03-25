package org.extalia.constants;

public class test {
    public static void main(String[] args) {
        int buffstat = 0x4000000;
        int pos = 3;

        for (int flag = 0; flag < 999; flag++)
        {
            if ((1 << (31 - (flag % 32))) == buffstat && pos == (byte) Math.floor(flag / 32))
                System.out.println(flag);
            if ((1 << (31 - (flag % 32))) == buffstat && pos == (byte) (4 - Math.floor(flag / 32)))
                System.out.println("mob " + flag);

        }

    }
}
