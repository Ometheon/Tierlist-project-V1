import model.Item;
import model.Tier;
import model.TierList;

public class Main {
    public static void main(String[] args) {

        Item mario = new Item("Mario");
        Item luigi = new Item("Luigi");
        Item peach = new Item("Peach");

        Tier sTier = new Tier("S");
        Tier aTier = new Tier("A");
        Tier bTier = new Tier("B");
        Tier cTier = new Tier("C");

        sTier.addItem(mario);
        sTier.addItem(luigi);
        aTier.addItem(peach);

        TierList tierList = new TierList();

        tierList.addTier(sTier);
        tierList.addTier(aTier);
        tierList.addTier(bTier);
        tierList.addTier(cTier);

        tierList.moveTierDown(bTier);

        for (Tier tier : tierList.getTiers()) {
            System.out.println(tier.getName());

            for (Item item : tier.getItems()) {
                System.out.println("  " + item.getName());
            }
        }
    }
}
