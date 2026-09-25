package swing;
import model.*;

import javax.swing.JFrame;

public class SwingApp {

    public static void main(String[] args) {

        JFrame frame = new JFrame("My Tier List");

        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        TierList tierList = new TierList();

        // Test data
        Tier sTier = new Tier("S");
        sTier.addItem(new Item("Lorco"));
        sTier.addItem(new Item("Corlo"));

        Tier aTier = new Tier("A");
        aTier.addItem(new Item("Samuel"));

        Tier bTier = new Tier("B");
        bTier.addItem(new Item("Laurin"));

        Tier cTier = new Tier("C");

        Tier dTier = new Tier("D");
        dTier.addItem(new Item("Herr Schwerer"));

        tierList.addTier(sTier);
        tierList.addTier(aTier);
        tierList.addTier(bTier);
        tierList.addTier(cTier);
        tierList.addTier(dTier);

        TierListPanel tierListPanel = new TierListPanel(tierList);

        frame.add(tierListPanel);

        frame.setVisible(true);
    }
    
}