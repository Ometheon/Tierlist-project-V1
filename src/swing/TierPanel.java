package swing;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;

public class TierPanel extends JPanel{
    private Tier tier;

    private JLabel tierNameLabel;
    private JPanel itemsPanel;

    private JButton moveUpButton;
    private JButton moveDownButton;
    private JButton deleteTierButton;
    private java.util.function.Consumer<Item> onItemDrop;

    private static final DataFlavor ITEM_FLAVOR =
        new DataFlavor(Item.class, "Item");

    /* 
    private static final Color[] TIER_COLORS = {
            new Color(255, 0, 0),    // Red
            new Color(255, 165, 0),  // Orange
            new Color(255, 255, 0),  // Yellow
            new Color(0, 128, 0),    // Green
            new Color(0, 0, 255),    // Blue
            new Color(75, 0, 130),   // Indigo
            new Color(238, 130, 238) // Violet
    };
    */
    
    // -------------------------
    // CONSTRUCTOR
    // -------------------------
    public TierPanel(Tier tier) {
        this.tier = tier;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border to the tier panel

        
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

        // Create and add the tier name label
        tierNameLabel = new JLabel(tier.getName());
        tierNameLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tierNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        tierNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tierNameLabel.setVerticalAlignment(SwingConstants.CENTER);
        tierNameLabel.setPreferredSize(new Dimension(50, 50));
        tierNameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Create and add the items panel
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (Item item : tier.getItems()) {
            ItemPanel itemPanel = new ItemPanel(item);
            itemsPanel.add(itemPanel);
        }

        // Create the functionality button Panel
        moveUpButton = new JButton("↑");
        moveDownButton = new JButton("↓");
        deleteTierButton = new JButton("Remove Tier");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1));

        buttonPanel.add(moveUpButton);
        buttonPanel.add(moveDownButton);
        buttonPanel.add(deleteTierButton);

        buttonPanel.add(moveUpButton, BorderLayout.NORTH);
        buttonPanel.add(moveDownButton, BorderLayout.SOUTH);
        buttonPanel.add(deleteTierButton, BorderLayout.EAST);

        add(tierNameLabel, BorderLayout.WEST);
        add(itemsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.EAST);
    }


    // -------------------------
    // FUNCTIONS
    // -------------------------

    // Create various possible action that happen in a tier
    public void setDeleteAction(Runnable onDelete) {
        deleteTierButton.addActionListener(e -> onDelete.run());
    }

    public void setItemDropAction(java.util.function.Consumer<Item> onItemDrop) {
    this.onItemDrop = onItemDrop;
    }

    public void setMoveUpAction(Runnable action) {
    moveUpButton.addActionListener(e -> action.run());
    }

    public void setMoveDownAction(Runnable action) {
        moveDownButton.addActionListener(e -> action.run());
    }
    /* 
    public void addItem(Item item) {
    tier.addItem(item);

    ItemPanel itemPanel = new ItemPanel(item);
    itemsPanel.add(itemPanel);

    itemsPanel.revalidate();
    itemsPanel.repaint();
    }*/
}
