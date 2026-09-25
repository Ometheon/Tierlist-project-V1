package swing;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.datatransfer.*;


//Represents one visual item
public class ItemPanel extends JPanel {
    private Item item;

    private static final DataFlavor ITEM_FLAVOR =
        new DataFlavor(Item.class, "Item");
    
    // Constructor to create an ItemPanel for a given Item that displays the item's name and has a border around it
    public ItemPanel(Item item) {
        this.item = item;

        setPreferredSize(new Dimension(100, 50)); // Set preferred size for the item panel
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border to the item panel

        JLabel itemLabel = new JLabel(this.item.getName());

        TransferHandler transferHandler = new TransferHandler() {

            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }

            @Override
            protected java.awt.datatransfer.Transferable createTransferable(JComponent c) {

                return new java.awt.datatransfer.Transferable() {

                    @Override
                    public DataFlavor[] getTransferDataFlavors() {
                        return new DataFlavor[] { ITEM_FLAVOR };
                    }

                    @Override
                    public boolean isDataFlavorSupported(DataFlavor flavor) {
                        return flavor.equals(ITEM_FLAVOR);
                    }

                    @Override
                    public Object getTransferData(DataFlavor flavor) {

                        if (flavor.equals(ITEM_FLAVOR)) {
                            return item;
                        }

                        return null;
                    }
                };
            }
        };

        setTransferHandler(transferHandler);
        itemLabel.setTransferHandler(transferHandler);

        MouseAdapter mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                JComponent component = (JComponent) e.getSource();

                TransferHandler handler = component.getTransferHandler();

                handler.exportAsDrag(
                    ItemPanel.this,
                    e,
                    TransferHandler.MOVE
                );
            }
        };

        addMouseMotionListener(mouseAdapter);
        itemLabel.addMouseMotionListener(mouseAdapter);

        add(itemLabel);
    }

    public Item getItem() {
    return item;
    }
}
