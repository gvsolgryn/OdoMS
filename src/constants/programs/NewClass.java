package constants.programs;

import constants.GameConstants;

public class NewClass {

    public static void main(String[] args) {
        System.out.println("asdf");
        int job = 2412;
        if ((job >= 100 && job < 200) || job == 512 || job == 1512 || job == 2512 || job == 1112 || GameConstants.isAran(job) || GameConstants.isBlaster(job) || GameConstants.isDemonSlayer(job) || GameConstants.isMikhail(job) || GameConstants.isKaiser(job) || GameConstants.isZero(job)) {
            System.out.println(1);
        } else if ((job >= 200 && job < 300) || GameConstants.isFlameWizard(job) || GameConstants.isEvan(job) || GameConstants.isLuminous(job) || job == 3212 || GameConstants.isKinesis(job)) {
            System.out.println(2);
        } else if ((job >= 300 && job < 400) || job == 522 || job == 532 || GameConstants.isMechanic(job) || GameConstants.isAngelicBuster(job) || job >= 1312 || GameConstants.isMercedes(job) || job >= 3312) {
            System.out.println(3);
        } else if ((job >= 400 && job < 500) || job == 1412 || GameConstants.isPhantom(job)) {
            System.out.println(4);
        } else if (GameConstants.isDemonAvenger(job)) {
            System.out.println(5);
        } else if (GameConstants.isXenon(job)) {
            System.out.println(6);
        }
    }
}
