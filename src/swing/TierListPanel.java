package swing;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;

// Represents the entire tier list visually
// Handels adding/removing tiers and items
public class TierListPanel extends JPanel {
    private TierList tierList;
    private JPanel tiersPanel;
    private JPanel unrankedItemsPanel;
    private JPanel bottomPanel;
    private TrashPanel trashPanel;
    private JPanel controlPanel;
    private JTextField itemInput;
    private JButton addButton;
    private JTextField tierInput;
    private JButton addTierButton;
    private static final DataFlavor ITEM_FLAVOR =
        new DataFlavor(Item.class, "Item");


    // -------------------------
    // CONSTRUCTOR
    // -------------------------
    public TierListPanel(TierList tierList) {
        this.tierList = tierList;
        
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());

        // -------------------------
        // Tiers panel and unranked Panel with included dropzone mechanic
        // -------------------------
        tiersPanel = new JPanel();
        tiersPanel.setLayout(new GridLayout(0, 1));

        unrankedItemsPanel = new JPanel();
        unrankedItemsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        unrankedItemsPanel.setBorder(BorderFactory.createTitledBorder("Unranked Items")); // Add a titled border for unranked items panel with size
        unrankedItemsPanel.setPreferredSize(new Dimension(800, 100)); // Set preferred size for unranked items panel
        unrankedItemsPanel.setBackground(Color.LIGHT_GRAY); // Set background color for unranked items panel
        unrankedItemsPanel.setTransferHandler(new TransferHandler() {

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

                    moveItemToUnranked(item);

                    return true;

                } catch (Exception e) {
                    return false;
                }
            }
        });

        //Display the tiers and the unranked
        displayTiers();

        // -------------------------
        // Bottom panel for adding new items and tiers and trashcan
        // -------------------------
        bottomPanel = new JPanel();

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add text field for new item name
        itemInput = new JTextField(15);
        // Add button for adding new items
        addButton = new JButton("Add Item");
        // Add text field for new tier name
        tierInput = new JTextField(8);
        // Add button for adding new tiers
        addTierButton = new JButton("Add Tier");

        controlPanel.add(itemInput);
        controlPanel.add(addButton);
        controlPanel.add(tierInput);
        controlPanel.add(addTierButton);

        // Add action listener to the add (item) button
        addButton.addActionListener(e -> {
            
            String name = itemInput.getText();

            Item newItem = new Item(name);

            tierList.addUnrankedItem(newItem);

            // Create a new panel for the item and add it to the unranked items panel
            ItemPanel itemPanel = new ItemPanel(newItem);
            unrankedItemsPanel.add(itemPanel);

            unrankedItemsPanel.revalidate();
            unrankedItemsPanel.repaint();

            itemInput.setText("");
        });

        // Add action listener to the create tier button
        addTierButton.addActionListener(e -> {
            String tierName = tierInput.getText();
            if (!tierName.isEmpty()) {
                Tier newTier = new Tier(tierName);
                tierList.addTier(newTier);
                TierPanel tierPanel = new TierPanel(newTier);

                // Set what the itemdrop and delete-tier actions do
                tierPanel.setDeleteAction(() -> {
                    tierList.removeTier(newTier);
                    tiersPanel.remove(tierPanel);

                    tiersPanel.revalidate();
                    tiersPanel.repaint();
                });
                tierPanel.setItemDropAction(item -> {
                    moveItem(item, newTier);
                });

                tiersPanel.add(tierPanel);
                tiersPanel.revalidate();
                tiersPanel.repaint();
                tierInput.setText("");
            }
        });

        // Trash can
        trashPanel = new TrashPanel();
        trashPanel.setItemDropAction(item -> {
            deleteItem(item);
        });
        bottomPanel.add(controlPanel,BorderLayout.WEST);
        bottomPanel.add(trashPanel,BorderLayout.EAST);

        add(tiersPanel, BorderLayout.NORTH);
        add(unrankedItemsPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // -------------------------
    // FUNCTIONS
    // -------------------------

    // Move an item to a different tier
    private void moveItem(Item item, Tier destinationTier) {

        // Remove item from its current tier or from the unranked pool
        for (Tier tier : tierList.getTiers()) {
            if (tier.getItems().contains(item)) {
                tier.removeItem(item);
                break;
            }
        }
        tierList.removeUnrankedItem(item);

        // Add item to the new tier
        destinationTier.addItem(item);

        // Refresh the GUI
        displayTiers();
    }


    // Move an item to unranked
    private void moveItemToUnranked(Item item) {

        // Remove the item from whichever tier contains it
        for (Tier tier : tierList.getTiers()) {
            if (tier.getItems().contains(item)) {
                tier.removeItem(item);
                break;
            }
        }

        // Add it to the unranked items
        tierList.addUnrankedItem(item);

        // Rebuild the GUI
        displayTiers();
    }


    // Move an Item to the trash bin and delete
    private void deleteItem(Item item) {

        // Remove from unranked items
        tierList.removeUnrankedItem(item);

        // Remove from whichever tier contains it
        for (Tier tier : tierList.getTiers()) {
            if (tier.getItems().contains(item)) {
                tier.removeItem(item);
                break;
            }
        }

        // Rebuild the GUI
        displayTiers();
    }

    // Displays all tiers in the tier list (including the unranked part)
    private void displayTiers() {

        // Clear existing GUI
        tiersPanel.removeAll();
        unrankedItemsPanel.removeAll();

        //rebuild tiers
        for (Tier tier : tierList.getTiers()) {
            TierPanel tierPanel = new TierPanel(tier);

            // set what the actions do
            tierPanel.setDeleteAction(() -> {
                tierList.removeTier(tier);
                tiersPanel.remove(tierPanel);

                tiersPanel.revalidate();
                tiersPanel.repaint();
            });

            tierPanel.setItemDropAction(item -> {
                moveItem(item, tier);
            });

            tierPanel.setMoveUpAction(() -> {
                tierList.moveTierUp(tier);
                displayTiers();
            });

            tierPanel.setMoveDownAction(() -> {
                tierList.moveTierDown(tier);
                displayTiers();
            });

            tiersPanel.add(tierPanel);

        }

        //rebuild unranked
        for (Item item: tierList.getUnrankedItems()){
            ItemPanel itemPanel = new ItemPanel(item);
            unrankedItemsPanel.add(itemPanel);
        }

        tiersPanel.revalidate();
        tiersPanel.repaint();

        unrankedItemsPanel.revalidate();
        unrankedItemsPanel.repaint();
    }
}
