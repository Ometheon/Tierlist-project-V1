package swing;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.function.*;

public class TrashPanel extends JPanel{
    
    private static final DataFlavor ITEM_FLAVOR =
            new DataFlavor(Item.class, "Item");
    private Consumer<Item> onItemDrop;

    // -------------------------
    // CONSTRUCTOR
    // -------------------------
    public TrashPanel() {

        
        JLabel trashLabel = new JLabel("🗑️");
        trashLabel.setFont(new Font("Dialog", Font.PLAIN, 40));
        add(trashLabel);

        setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(ITEM_FLAVOR);
            }

            @Override
            public boolean importData(TransferSupport support) {

                if (!canImport(support)) {
                    return false;
                }

                try {
                    Transferable transferable = support.getTransferable();

                    Item item = (Item) transferable.getTransferData(ITEM_FLAVOR);

                    if (onItemDrop != null) {
                        onItemDrop.accept(item);
                    }

                    return true;

                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public void setItemDropAction(Consumer<Item> onItemDrop) {
        this.onItemDrop = onItemDrop;
    }
}
