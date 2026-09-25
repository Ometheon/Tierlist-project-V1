package model;
import java.util.ArrayList;
import java.util.List;

//import model.Tier;

public class TierList {
    private List<Tier> tiers;
    private List<Item> unrankedItems; //added this line to store unranked items

    public TierList() {
        this.tiers = new ArrayList<>();
        this.unrankedItems = new ArrayList<>();
    }

    public void addTier(Tier tier) {
        tiers.add(tier);
    }

    public void removeTier(Tier tier) {
        tiers.remove(tier);
    }

    public List<Tier> getTiers() {
        return tiers;
    }

    // Added methods for unranked items
    public void addUnrankedItem(Item item) {
        unrankedItems.add(item);
    }

    public void removeUnrankedItem(Item item) {
        unrankedItems.remove(item);
    }

    public List<Item> getUnrankedItems() {
        return unrankedItems;
    }
    
    public void moveTierUp(Tier tier) {
        int index = tiers.indexOf(tier);

        if (index > 0) {
            Tier previousTier = tiers.get(index - 1);

            tiers.set(index - 1, tier);
            tiers.set(index, previousTier);
        }
    }

    public void moveTierDown(Tier tier) {
        int index = tiers.indexOf(tier);

        if (index < tiers.size() - 1) {
            Tier nextTier = tiers.get(index + 1);

            tiers.set(index + 1, tier);
            tiers.set(index, nextTier);
        }
    }
}
