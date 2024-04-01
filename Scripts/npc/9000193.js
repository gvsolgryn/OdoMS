importPackage(Packages.java.io);

importPackage(Packages.server);
importPackage(Packages.provider);
importPackage(Packages.constants);
importPackage(Packages.client.inventory);

var EQUIP_TYPE = {
    "ACCESSORY": "Accessory",
    "CAP": "Cap",
    "CAPE": "Cape",
    "COAT": "Coat",
    "GLOVE": "Glove",
    "LONGCOAT": "Longcoat",
    "PANTS": "Pants",
    "SHIELD": "Shield",
    "SHOES": "Shoes",
    "SHOULDER": "Shoulder",
    "WEAPON": "Weapon",
    "UNKNOWN": "Unknown"
}

var OPTION_TYPE = {
    "ADDITIONAL": "Additional",
    "POTENTIAL": "Potential"
}

var USE_ITEM = 4031156; // 코드 이거 맞는지 확인해보실래요? 없는 아이템이라는데

var status, manager;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1 || mode == 0) {
        cm.dispose();
        return;
    } else {
        status++;
    }

    var message = "#fs11#";
    switch (status) {
        case 0:
            message += "#h #님 #fc0xFF008EFF##z{0}##k을(를) 사용하여 잠재능력을 #fc0xFFFF4B46#최상위 옵션#k으로 변경하실 수 있습니다.\r\n\r\n".format(USE_ITEM);
            message += "#L0##fc0xFF009F4A#잠재능력#k을 변경하겠습니다.\r\n" + 
                       "#L1##fc0xFFAB00B8#에디셔널 잠재능력#k을 변경하겠습니다.";
            cm.sendSimple(message);
            break;

        case 1:
            var type = selection ? OPTION_TYPE.ADDITIONAL : OPTION_TYPE.POTENTIAL;
            var items = getEquipInventory(type);
            if (items.length == 0) {
                disposeWithError(new Error("적용 가능한 아이템이 없습니다."));
                break;
            }

            manager = new ItemManager(cm.getPlayer(), type, items);
            message += "{0} 옵션을 변경하실 아이템을 선택해주세요.\r\n\r\n"
                .format(type == OPTION_TYPE.POTENTIAL ? "#fc0xFF009F4A#잠재능력#k" : "#fc0xFFAB00B8#에디셔널 잠재능력#k");
            for (var i = 0; i < items.length; i++){
                message += "#L{0}##e[{1} SLOT]#n #z{2}#\r\n".format(i, items[i].getPosition(), items[i].getItemId());
            }
            cm.sendSimple(message);
            break;

        case 2:
            var item = manager.getCached()[selection];
            try {
                manager.setTarget(item);
            } catch (err) {
                disposeWithError(err);
                break;
            }
            message += "#fc0xFF008EFF##z{0}##k의 변경할 {1} 슬롯을 선택해주세요.\r\n\r\n"
                .format(item.getItemId(), manager.getOptionType() == OPTION_TYPE.POTENTIAL ? "#fc0xFF009F4A#잠재능력#k" : "#fc0xFFAB00B8#에디셔널 잠재능력#k")
            message += "    #L1#첫번째 슬롯#l   #L2#두번째 슬롯#l   #L3#세번째 슬롯#l\r\n";
            cm.sendSimple(message);
            break;

        case 3:
            message += "#fc0xFF008EFF#{0} 번째 슬롯#k의 변경할 옵션을 선택해주세요.\r\n\r\n".format(selection);
            manager.setOptionSlot(selection);
            var options = manager.getCached();
            for (var i = 0; i < options.length; i++) {
                message += "#L{0}##e[{1} EA]#n {2}\r\n".format(i, options[i].getPrice().comma(), options[i].getDescription());
            }
            cm.sendSimple(message);
            break;

        case 4:
            try {
                manager.change(selection);
            } catch (err) {
                disposeWithError(err);
                break;
            }
            message += "#fc0xFF008EFF##z{0}##k의 {1} #fc0xFFFF4B46#{2} 번째#k 옵션이 변경되었습니다."
                .format(manager.getTarget().getItemId(), manager.getOptionType() == OPTION_TYPE.POTENTIAL ? "#fc0xFF009F4A#잠재능력#k" : "#fc0xFFAB00B8#에디셔널 잠재능력#k", manager.getOptionSlot());
            cm.sendOk(message);
            cm.dispose();
    }
}

function ItemManager(character, type, cached) {
    this.character = character;
    this.optionType = type;
    this.optionSlot = 0;
    this.target = null;
    this.cached = cached;
}

ItemManager.prototype.getOptionType = function() { return this.optionType; };

ItemManager.prototype.getTarget = function() { return this.target; };
ItemManager.prototype.setTarget = function(item) {
    this.target = item;
    try {
        var reqLevel = MapleItemInformationProvider.getInstance().getEquipStats(item.getItemId()).get("reqLevel");
        var equipType = getEquipType(this.target.getItemId());
        this.cached = getItemOptionList(reqLevel, equipType, this.optionType);
    } catch (err) {
        throw err
    }
};

ItemManager.prototype.getCached = function() { return this.cached; };
ItemManager.prototype.setCached = function(cached) { this.cached = cached; };

ItemManager.prototype.getOptionSlot = function() { return this.optionSlot; };
ItemManager.prototype.setOptionSlot = function(slot) { 
    this.optionSlot = slot; 
    this.optionSlot += this.optionType == OPTION_TYPE.ADDITIONAL ? 3 : 0;
};

ItemManager.prototype.change = function(index) {
    var option = this.cached[index];
    var quantity = this.character.getInventory(GameConstants.getInventoryType(USE_ITEM)).countById(USE_ITEM); // 저장이 안되었네여?
    if (quantity < option.getPrice()) {
        throw new Error("#fc0xFF008EFF##z{0}##k이(가) #fc0xFFFF4B46#{1}#k개 부족합니다.".format(USE_ITEM, (option.getPrice() - quantity)));
    }
    switch (this.optionSlot) {
        case 1:
            this.target.setPotential1(option.getId());
            break;
        case 2:
            this.target.setPotential2(option.getId());
            break;
        case 3:
            this.target.setPotential3(option.getId());
            break;
        case 4:
            this.target.setPotential4(option.getId());
            break;
        case 5:
            this.target.setPotential5(option.getId());
            break;
        case 6:
            this.target.setPotential6(option.getId());
            break;
        default:
            throw new Error("unknown slot");
    }
    if (this.target.getState() != 20) {
        this.target.setState(20);
    }
    this.character.forceReAddItem(this.target, MapleInventoryType.EQUIP);
    cm.gainItem(USE_ITEM, -option.getPrice());
};

function ItemOption(id, price, description) {
    this.id = id;
    this.price = price;
    this.description = description;
}

ItemOption.prototype.getId = function() { return this.id; };
ItemOption.prototype.getPrice = function() { return this.price; };
ItemOption.prototype.getDescription = function() { return this.description; };

function disposeWithError(err) {
    cm.sendOk("#fs11#{0}".format(err.message));
    cm.dispose();
}

function getItemOptionList(reqLevel, equipType, optionType) {
    var result = [];
    var level = Math.floor(reqLevel / 10);
    level = level >= 20 ? 20 : level;
    try {
        var provider = new MapleDataProvider(new File("WZ/Script.wz"));
        var children = provider.getData("Potential.img").getChildByPath(equipType).getChildByPath(optionType).getChildren();
        var size = children.size();
        for (var i = 0; i < size; i++) {
            var data = children.get(i);
            var id = parseInt(data.getName());
            var price = parseInt(data.getChildByPath("info/price").getData());
            var description = data.getChildByPath("info/string").getData();
            var values = data.getChildByPath("level/{0}".format(level)).getChildren();
            var size_ = values.size(); 
            for (var j = 0; j < size_; j++) {
                description = description.replaceAll("#{0}".format(values[j].getName()), values[j].getData());
            }
            result.push(new ItemOption(id, price, description));
        }
    } catch (err) {
        throw err;
    }
    if (result.length == 0) {
        throw new Error("설정 가능한 옵션이 없습니다.");
    }
    return result;
}

function getEquipInventory(optionType) {
    var result = [];
    var provider = MapleItemInformationProvider.getInstance();
    var inventory = cm.getPlayer().getInventory(MapleInventoryType.EQUIP);
    var limit = inventory.getSlotLimit();
    for (var i = 0; i < limit; i++) {
        var item = inventory.getItem(i);
        if (item == null) continue;
        if (provider.isCash(item.getItemId()))
        if (optionType == OPTION_TYPE.POTENTIAL && item.getPotential1() == 0) continue;
        if (optionType == OPTION_TYPE.ADDITIONAL && item.getPotential4() == 0) continue;
        result.push(item);
    }
    return result;
}

function getEquipType(itemId) {
    var wType = Math.floor(itemId / 1000);
    if (GameConstants.isWeapon(itemId) || wType == 1190 || wType == 1191 || wType == 1672) {
        return EQUIP_TYPE.WEAPON;
    }

    var eType = Math.floor(itemId / 10000);
    switch (eType) {
        case 101: case 102: case 103: case 111: case 112: case 113:
            return EQUIP_TYPE.ACCESSORY;
        case 100:
            return EQUIP_TYPE.CAP;
        case 104: 
            return EQUIP_TYPE.COAT;
        case 105: 
            return EQUIP_TYPE.LONGCOAT;
        case 106: 
            return EQUIP_TYPE.PANTS;
        case 107: 
            return EQUIP_TYPE.SHOES
        case 108: 
            return EQUIP_TYPE.GLOVE
        case 109: 
            return EQUIP_TYPE.SHIELD;
        case 110: 
            return EQUIP_TYPE.CAPE;
        case 115:
            return EQUIP_TYPE.SHOULDER
        default:
            return EQUIP_TYPE.UNKNOWN
    }
}

Number.prototype.comma = function() {
    return this.toString().replace(/(\d)(?=(\d\d\d)+(?!\d))/g, "$1,");
};

String.prototype.format = function() {
    var formatted = this;
    for( var arg in arguments ) {
        formatted = formatted.replace("{" + arg + "}", arguments[arg]);
    }
    return formatted;
};
