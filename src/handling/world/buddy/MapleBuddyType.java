/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.world.buddy;

public enum MapleBuddyType {
    addBuddy(27), // 친구신청 25
    Attendance(183);

    private final int type;

    private MapleBuddyType(int i) {
        this.type = i;
    }

    public final int getType() {
        return type;
    }

}
