importPackage(Packages.server);
importPackage(Packages.client.inventory);

var status;
var enter = "\r\n";
var item;

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
    var msg = "#fs11#";
    if (status == 0) {
        r1 = Randomizer.rand(0, 2);
        if (r1 == 0) {
            r2 = Randomizer.rand(0, 3);
            switch (r2) {
                case 0:
                    item = 2046967;
                    break;
                case 1:
                    item = 2046971;
                    break;
                case 2:
                    item = 2047803;
                    break;
            }
        } else {
            item = 2049153;
        }
        cm.gainItem(item, 1);
        cm.gainItem(2028181, -1);
        cm.dispose();
    }
}
