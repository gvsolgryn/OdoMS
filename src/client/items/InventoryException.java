package client.items;

public class InventoryException extends RuntimeException {

    public InventoryException() {
        super();
    }

    public InventoryException(String msg) {
        super(msg);
    }
}
