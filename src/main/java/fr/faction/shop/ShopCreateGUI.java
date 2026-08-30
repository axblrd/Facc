package fr.faction.shop;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * GUI de création d'annonce pour le shop global.
 *
 * Layout (6 rangées = 54 slots) :
 *
 *  ┌─────────────────────────────────────────────────────────────────────────────┐
 *  │  VENDRE                                                         CONTRE      │
 *  │  [ITEM]  ← slot 20        Quantité: [−][+]        [PRIX]  ← slot 24        │
 *  │  ← dépose l'item ici                               ← dépose le prix ici     │
 *  │                                                                             │
 *  │  Qté item : [−−][−][qty][+][++]    Qté prix : [−−][−][qty][+][++]          │
 *  │                                                                             │
 *  │  MODE : [💰 Monnaie]  [🔄 Troc]         [✔ CONFIRMER]  [✗ Annuler]        │
 *  └─────────────────────────────────────────────────────────────────────────────┘
 *
 * Slots fixes :
 *   2  → label "VENDRE"
 *   6  → label "CONTRE"
 *   11 → slot de dépôt item en vente  (interactif)
 *   15 → slot de dépôt item prix      (interactif)
 *   28,29 → qty item −− −
 *   30    → affichage qté item
 *   31,32 → qty item + ++
 *   33,34 → qty prix −− −
 *   35    → affichage qté prix
 *   36,37 → qty prix + ++
 *   38 → mode MONNAIE
 *   40 → mode TROC
 *   42 → ✔ Confirmer
 *   44 → ✗ Annuler
 */
public class ShopCreateGUI implements Listener {

    private static final String TITLE = "§8§l[§6§lNouvelle Annonce§8§l] §7Shop Global";

    // ─── État par joueur ─────────────────────────────────────────────────────────
    private static class CreateState {
        ItemStack sellItem;   // item mis en vente (type seulement, quantité gérée séparément)
        int sellQty = 1;
        ItemStack priceItem;  // item prix (type seulement, quantité gérée séparément)
        int priceQty = 1;
        ShopListing.PriceMode mode = ShopListing.PriceMode.CURRENCY;
        ShopListing.Currency currency = ShopListing.Currency.EMERALD;
    }

    private final JavaPlugin plugin;
    private final ShopManager shopManager;
    private final Map<UUID, CreateState> states = new HashMap<>();

    // Slots spéciaux (annoncés dans le titre de la section)
    private static final int SLOT_SELL_ITEM  = 11;
    private static final int SLOT_PRICE_ITEM = 15;

    // Contrôles quantité item vendu
    private static final int S_SELL_MM = 28, S_SELL_M = 29, S_SELL_DIS = 30, S_SELL_P = 31, S_SELL_PP = 32;
    // Contrôles quantité prix
    private static final int S_PRICE_MM = 33, S_PRICE_M = 34, S_PRICE_DIS = 35, S_PRICE_P = 36, S_PRICE_PP = 37;
    // Modes & actions
    private static final int S_MODE_CURR = 38, S_MODE_BARTER = 40, S_CONFIRM = 42, S_CANCEL = 44;

    public ShopCreateGUI(JavaPlugin plugin, ShopManager shopManager) {
        this.plugin      = plugin;
        this.shopManager = shopManager;
    }

    // ── Ouverture ────────────────────────────────────────────────────────────────

    public void open(Player player) {
        states.put(player.getUniqueId(), new CreateState());
        player.openInventory(buildGUI(player));
    }

    // ── Construction du GUI ───────────────────────────────────────────────────────

    private Inventory buildGUI(Player player) {
        CreateState state = states.get(player.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 54, TITLE);

        // Remplissage fond
        ItemStack bg = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++) inv.setItem(i, bg);

        // ── Labels ───────────────────────────────────────────────────────────────
        inv.setItem(2,  label(Material.LIME_STAINED_GLASS_PANE,
                "§a§l◆ ITEM EN VENTE", "§7Dépose l'item ici §8(slot central gauche)",
                "§7puis ajuste la quantité."));
        inv.setItem(6,  label(Material.ORANGE_STAINED_GLASS_PANE,
                "§6§l◆ PRIX DEMANDÉ",  "§7Dépose l'item-prix ici §8(slot central droit)",
                "§7ou sélectionne une monnaie."));

        // ── Séparateur vertical ───────────────────────────────────────────────────
        for (int r : new int[]{3, 12, 21, 30, 39, 48}) inv.setItem(r, glass(Material.WHITE_STAINED_GLASS_PANE));

        // ── Zone item vendu ───────────────────────────────────────────────────────
        if (state.sellItem != null) {
            ItemStack display = state.sellItem.clone();
            display.setAmount(Math.min(state.sellQty, display.getType().getMaxStackSize()));
            inv.setItem(SLOT_SELL_ITEM, annotate(display, "§aItem en vente",
                    "§7Quantité : §e" + state.sellQty,
                    "§8Clic gauche : retirer l'item",
                    "§8Clic droit  : remettre en inventaire"));
        } else {
            inv.setItem(SLOT_SELL_ITEM, label(Material.GRAY_STAINED_GLASS_PANE,
                    "§7▶ Dépose l'item à vendre", "§8Clic gauche depuis ton inventaire"));
        }

        // Contrôles quantité item vendu
        inv.setItem(S_SELL_MM,  btn(Material.RED_TERRACOTTA,    "§c§l−10",  "§7Clic : −10"));
        inv.setItem(S_SELL_M,   btn(Material.ORANGE_TERRACOTTA, "§6§l−1",   "§7Clic gauche : −1 §8/ §7droit : −5"));
        inv.setItem(S_SELL_DIS, qtyDisplay(state.sellQty, state.sellItem,   "§aItem en vente", true));
        inv.setItem(S_SELL_P,   btn(Material.LIME_TERRACOTTA,   "§a§l+1",   "§7Clic gauche : +1 §8/ §7droit : +5"));
        inv.setItem(S_SELL_PP,  btn(Material.GREEN_TERRACOTTA,  "§2§l+10",  "§7Clic : +10"));

        // ── Zone item prix ────────────────────────────────────────────────────────
        buildPriceZone(inv, state);

        // Contrôles quantité prix (seulement en mode BARTER ou si monnaie sélectionnable)
        inv.setItem(S_PRICE_MM,  btn(Material.RED_TERRACOTTA,    "§c§l−10",  "§7Clic : −10"));
        inv.setItem(S_PRICE_M,   btn(Material.ORANGE_TERRACOTTA, "§6§l−1",   "§7Clic gauche : −1 §8/ §7droit : −5"));
        inv.setItem(S_PRICE_DIS, qtyDisplay(state.priceQty, state.priceItem, "§6Prix demandé", false));
        inv.setItem(S_PRICE_P,   btn(Material.LIME_TERRACOTTA,   "§a§l+1",   "§7Clic gauche : +1 §8/ §7droit : +5"));
        inv.setItem(S_PRICE_PP,  btn(Material.GREEN_TERRACOTTA,  "§2§l+10",  "§7Clic : +10"));

        // ── Modes ─────────────────────────────────────────────────────────────────
        boolean isCurr = state.mode == ShopListing.PriceMode.CURRENCY;
        inv.setItem(S_MODE_CURR,   modeBtn(Material.EMERALD, "§a💰 Mode Monnaie",
                "§7Prix en item de monnaie fixe :",
                "§8Fer · Or · Diamant · Émeraude",
                isCurr ? "§a✔ Actif" : "§7Clic pour sélectionner"));
        inv.setItem(S_MODE_BARTER, modeBtn(Material.GOLD_INGOT, "§6🔄 Mode Troc",
                "§7Prix en n'importe quel item.",
                "§8Ex : 16 cobble contre 2 steak",
                !isCurr ? "§a✔ Actif" : "§7Clic pour sélectionner"));

        // ── Actions ───────────────────────────────────────────────────────────────
        boolean valid = canConfirm(state);
        inv.setItem(S_CONFIRM, label(valid ? Material.LIME_STAINED_GLASS_PANE : Material.GRAY_STAINED_GLASS_PANE,
                valid ? "§a§l✔ Confirmer l'annonce" : "§8✔ Confirmer §c(annonce incomplète)",
                valid ? "§7Item : " + ShopListing.displayName(state.sellItem) + " ×" + state.sellQty : "§cDépose un item à vendre.",
                valid ? "§7Prix : " + getPriceDescription(state) : "§cVérifie le prix.",
                valid ? "" : "",
                valid ? "§eClic pour mettre en vente !" : ""));
        inv.setItem(S_CANCEL, label(Material.RED_STAINED_GLASS_PANE,
                "§c§l✗ Annuler", "§7Retour au shop (aucun item perdu)."));

        return inv;
    }

    private void buildPriceZone(Inventory inv, CreateState state) {
        if (state.mode == ShopListing.PriceMode.CURRENCY) {
            // Afficher les 4 monnaies comme sélecteurs
            ShopListing.Currency[] currencies = ShopListing.Currency.values();
            int[] currSlots = {13, 14, 15, 16};
            // Slot 15 = la monnaie sélectionnée en gros, les autres autour
            for (int ci = 0; ci < currencies.length; ci++) {
                ShopListing.Currency cur = currencies[ci];
                boolean selected = cur == state.currency;
                ItemStack icon = new ItemStack(cur.getMaterial(), selected ? state.priceQty : 1);
                ItemMeta meta = icon.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName((selected ? "§a§l✔ " : "§7") + cur.getDisplayName());
                    meta.setLore(Arrays.asList(
                            "§7Quantité : §e" + (selected ? state.priceQty : 1),
                            selected ? "§a✔ Sélectionné" : "§7Clic pour choisir"
                    ));
                    if (selected) meta.addEnchant(org.bukkit.enchantments.Enchantment.LUCK_OF_THE_SEA, 1, true);
                    meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS,
                            org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
                    icon.setItemMeta(meta);
                }
                inv.setItem(currSlots[ci], icon);
            }
        } else {
            // Mode troc : slot 15 = zone de dépôt
            if (state.priceItem != null) {
                inv.setItem(SLOT_PRICE_ITEM, annotate(state.priceItem.clone(), "§6Item-prix (troc)",
                        "§7Quantité : §e" + state.priceQty,
                        "§8Clic gauche : retirer"));
            } else {
                inv.setItem(SLOT_PRICE_ITEM, label(Material.YELLOW_STAINED_GLASS_PANE,
                        "§7▶ Dépose l'item prix ici", "§8Ex : 2 steak, 4 pain, 1 diamant…"));
            }
        }
    }

    // ── Events ────────────────────────────────────────────────────────────────────

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (!e.getView().getTitle().equals(TITLE)) return;

        CreateState state = states.get(player.getUniqueId());
        if (state == null) { e.setCancelled(true); return; }

        int raw = e.getRawSlot();
        int invSize = e.getInventory().getSize();

        // ── Clic depuis l'inventaire du joueur → déposer dans les zones actives ──
        if (raw >= invSize) {
            // Clic depuis l'inventaire du joueur
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) { e.setCancelled(true); return; }

            // Si shift-clic ou clic sur un item dans l'inventaire joueur, on laisse passer SAUF si c'est une zone protégée
            // Ici on détourne : si l'item vendu n'est pas défini, on le saisit
            if (e.isShiftClick()) {
                e.setCancelled(true);
                // Déposer dans la zone item vendu si vide, sinon zone prix si barter et vide
                if (state.sellItem == null) {
                    state.sellItem = new ItemStack(clicked.getType(), 1);
                    state.sellItem.setItemMeta(clicked.getItemMeta() != null ? clicked.getItemMeta().clone() : null);
                    state.sellQty = clicked.getAmount();
                } else if (state.mode == ShopListing.PriceMode.BARTER && state.priceItem == null) {
                    state.priceItem = new ItemStack(clicked.getType(), 1);
                    state.priceItem.setItemMeta(clicked.getItemMeta() != null ? clicked.getItemMeta().clone() : null);
                    state.priceQty = clicked.getAmount();
                }
                refresh(player, e.getInventory());
                return;
            }
            // Clic simple depuis inventaire joueur → on laisse Bukkit gérer (drag vers le GUI)
            // mais on intercepte si destination = slot vendu ou prix
            return;
        }

        e.setCancelled(true);

        // ── Slot item vendu ───────────────────────────────────────────────────────
        if (raw == SLOT_SELL_ITEM) {
            if (state.sellItem != null) {
                // Retirer l'item au joueur (on le rend si possible)
                if (e.isLeftClick()) {
                    giveItemBack(player, state.sellItem, state.sellQty);
                    state.sellItem = null; state.sellQty = 1;
                }
            } else {
                // Essayer de prendre l'item du curseur
                ItemStack cursor = e.getCursor();
                if (cursor != null && cursor.getType() != Material.AIR) {
                    state.sellItem = new ItemStack(cursor.getType(), 1);
                    if (cursor.getItemMeta() != null) state.sellItem.setItemMeta(cursor.getItemMeta().clone());
                    state.sellQty = cursor.getAmount();
                    player.setItemOnCursor(new ItemStack(Material.AIR));
                }
            }
            refresh(player, e.getInventory()); return;
        }

        // ── Slot item prix (mode troc) ────────────────────────────────────────────
        if (raw == SLOT_PRICE_ITEM && state.mode == ShopListing.PriceMode.BARTER) {
            if (state.priceItem != null) {
                if (e.isLeftClick()) {
                    giveItemBack(player, state.priceItem, state.priceQty);
                    state.priceItem = null; state.priceQty = 1;
                }
            } else {
                ItemStack cursor = e.getCursor();
                if (cursor != null && cursor.getType() != Material.AIR) {
                    state.priceItem = new ItemStack(cursor.getType(), 1);
                    if (cursor.getItemMeta() != null) state.priceItem.setItemMeta(cursor.getItemMeta().clone());
                    state.priceQty = cursor.getAmount();
                    player.setItemOnCursor(new ItemStack(Material.AIR));
                }
            }
            refresh(player, e.getInventory()); return;
        }

        // ── Sélecteurs de monnaie (mode CURRENCY) ────────────────────────────────
        if (state.mode == ShopListing.PriceMode.CURRENCY) {
            ShopListing.Currency[] curs = ShopListing.Currency.values();
            int[] currSlots = {13, 14, 15, 16};
            for (int ci = 0; ci < currSlots.length; ci++) {
                if (raw == currSlots[ci]) { state.currency = curs[ci]; refresh(player, e.getInventory()); return; }
            }
        }

        // ── Contrôles quantité item vendu ─────────────────────────────────────────
        if (raw == S_SELL_MM) { state.sellQty = Math.max(1, state.sellQty - 10); refresh(player, e.getInventory()); return; }
        if (raw == S_SELL_M)  {
            int delta = e.isRightClick() ? 5 : 1;
            state.sellQty = Math.max(1, state.sellQty - delta);
            refresh(player, e.getInventory()); return;
        }
        if (raw == S_SELL_P)  {
            int delta = e.isRightClick() ? 5 : 1;
            state.sellQty = Math.min(2304, state.sellQty + delta); // max 36 stacks
            refresh(player, e.getInventory()); return;
        }
        if (raw == S_SELL_PP) { state.sellQty = Math.min(2304, state.sellQty + 10); refresh(player, e.getInventory()); return; }

        // ── Contrôles quantité prix ───────────────────────────────────────────────
        if (raw == S_PRICE_MM) { state.priceQty = Math.max(1, state.priceQty - 10); refresh(player, e.getInventory()); return; }
        if (raw == S_PRICE_M)  {
            int delta = e.isRightClick() ? 5 : 1;
            state.priceQty = Math.max(1, state.priceQty - delta);
            refresh(player, e.getInventory()); return;
        }
        if (raw == S_PRICE_P)  {
            int delta = e.isRightClick() ? 5 : 1;
            state.priceQty = Math.min(2304, state.priceQty + delta);
            refresh(player, e.getInventory()); return;
        }
        if (raw == S_PRICE_PP) { state.priceQty = Math.min(2304, state.priceQty + 10); refresh(player, e.getInventory()); return; }

        // ── Modes ─────────────────────────────────────────────────────────────────
        if (raw == S_MODE_CURR) {
            state.mode = ShopListing.PriceMode.CURRENCY;
            if (state.priceItem != null) { giveItemBack(player, state.priceItem, state.priceQty); state.priceItem = null; }
            refresh(player, e.getInventory()); return;
        }
        if (raw == S_MODE_BARTER) {
            state.mode = ShopListing.PriceMode.BARTER;
            refresh(player, e.getInventory()); return;
        }

        // ── Confirmer ─────────────────────────────────────────────────────────────
        if (raw == S_CONFIRM) {
            if (!canConfirm(state)) {
                player.sendMessage("§8[§6Shop§8] §cAnnonce incomplète ! Dépose un item et configure le prix.");
                return;
            }
            confirmListing(player, state);
            return;
        }

        // ── Annuler ───────────────────────────────────────────────────────────────
        if (raw == S_CANCEL) {
            cancelAndClose(player, state);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if (!e.getView().getTitle().equals(TITLE)) return;
        CreateState state = states.remove(player.getUniqueId());
        if (state != null) {
            // Rendre les items si le GUI est fermé sans confirmation
            if (state.sellItem != null)  giveItemBack(player, state.sellItem, state.sellQty);
            if (state.priceItem != null) giveItemBack(player, state.priceItem, state.priceQty);
        }
    }

    // ── Logique de création ───────────────────────────────────────────────────────

    private void confirmListing(Player player, CreateState state) {
        // Vérifier que le joueur possède bien les items à vendre
        int inInv = countInInventory(player, state.sellItem);
        if (inInv < state.sellQty) {
            player.sendMessage("§8[§6Shop§8] §cTu n'as pas assez de §e"
                    + ShopListing.displayName(state.sellItem)
                    + " §c(besoin : " + state.sellQty + ", possédé : " + inInv + ").");
            return;
        }

        // Retirer les items de l'inventaire du vendeur
        removeFromInventory(player, state.sellItem, state.sellQty);

        // Créer la mise en standby
        ItemStack forSale = state.sellItem.clone();
        forSale.setAmount(state.sellQty);

        ShopListing listing;
        if (state.mode == ShopListing.PriceMode.CURRENCY) {
            listing = shopManager.createCurrencyListing(player, forSale, state.currency, state.priceQty);
            player.sendMessage("§8[§6Shop§8] §aAnnonce créée !");
            player.sendMessage("§7Vente : §e" + state.sellQty + "× " + ShopListing.displayName(forSale));
            player.sendMessage("§7Prix  : §e" + state.priceQty + "× " + state.currency.getDisplayName());
        } else {
            ItemStack price = state.priceItem.clone();
            price.setAmount(state.priceQty);
            listing = shopManager.createBarterListing(player, forSale, price);
            player.sendMessage("§8[§6Shop§8] §aAnnonce de troc créée !");
            player.sendMessage("§7Vente  : §e" + state.sellQty + "× " + ShopListing.displayName(forSale));
            player.sendMessage("§7Contre : §e" + state.priceQty + "× " + ShopListing.displayName(price));
        }
        player.sendMessage("§8ID : §7" + listing.getId() + " §8— §7/fac recuperer " + listing.getId() + " §7pour reprendre.");

        states.remove(player.getUniqueId());
        player.closeInventory();
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.8f, 1.2f);
    }

    private void cancelAndClose(Player player, CreateState state) {
        states.remove(player.getUniqueId());
        // Les items sont rendus par onInventoryClose
        player.closeInventory();
    }

    // ── Helpers GUI ───────────────────────────────────────────────────────────────

    private void refresh(Player player, Inventory inv) {
        CreateState state = states.get(player.getUniqueId());
        if (state == null) return;
        Inventory fresh = buildGUI(player);
        for (int i = 0; i < fresh.getSize(); i++) inv.setItem(i, fresh.getItem(i));
        // Garder les items du joueur dans la partie basse intacts
    }

    private ItemStack glass(Material mat) {
        ItemStack is = new ItemStack(mat);
        ItemMeta meta = is.getItemMeta();
        if (meta != null) { meta.setDisplayName(" "); is.setItemMeta(meta); }
        return is;
    }

    private ItemStack label(Material mat, String name, String... lore) {
        ItemStack is = new ItemStack(mat);
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return is;
        meta.setDisplayName(name);
        if (lore.length > 0) meta.setLore(Arrays.asList(lore));
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES,
                org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);
        return is;
    }

    private ItemStack btn(Material mat, String name, String loreStr) {
        return label(mat, name, loreStr);
    }

    private ItemStack annotate(ItemStack base, String header, String... extraLore) {
        ItemStack copy = base.clone();
        ItemMeta meta = copy.getItemMeta();
        if (meta == null) return copy;
        if (!meta.hasDisplayName()) meta.setDisplayName("§f" + ShopListing.displayName(copy));
        List<String> lore = new ArrayList<>();
        lore.add("§8§m──────────────");
        lore.add(header);
        lore.addAll(Arrays.asList(extraLore));
        meta.setLore(lore);
        copy.setItemMeta(meta);
        return copy;
    }

    private ItemStack qtyDisplay(int qty, ItemStack ref, String label, boolean isSell) {
        Material mat = (ref != null) ? ref.getType() : (isSell ? Material.LIME_DYE : Material.GOLD_INGOT);
        ItemStack is = new ItemStack(mat, Math.max(1, Math.min(qty, mat.getMaxStackSize())));
        ItemMeta meta = is.getItemMeta();
        if (meta == null) return is;
        meta.setDisplayName(label);
        List<String> lore = new ArrayList<>();
        lore.add("§7Quantité : §e§l" + qty);
        if (ref != null) {
            int maxStack = ref.getType().getMaxStackSize();
            int stacks = qty / maxStack;
            int rem = qty % maxStack;
            if (stacks > 0) lore.add("§8= §7" + stacks + " stack(s)" + (rem > 0 ? " + " + rem : ""));
        }
        lore.add("§8Utilise ± pour ajuster.");
        meta.setLore(lore);
        is.setItemMeta(meta);
        return is;
    }

    private ItemStack modeBtn(Material mat, String name, String... lore) {
        return label(mat, name, lore);
    }

    private boolean canConfirm(CreateState state) {
        if (state.sellItem == null || state.sellQty < 1) return false;
        if (state.mode == ShopListing.PriceMode.CURRENCY) {
            return state.currency != null && state.priceQty > 0;
        } else {
            return state.priceItem != null && state.priceQty > 0;
        }
    }

    private String getPriceDescription(CreateState state) {
        if (state.mode == ShopListing.PriceMode.CURRENCY)
            return state.priceQty + "× " + (state.currency != null ? state.currency.getDisplayName() : "?");
        if (state.priceItem != null) return state.priceQty + "× " + ShopListing.displayName(state.priceItem);
        return "§cNon défini";
    }

    // ── Inventaire helpers ────────────────────────────────────────────────────────

    private void giveItemBack(Player player, ItemStack type, int qty) {
        ItemStack give = type.clone();
        give.setAmount(qty);
        ShopManager.giveOrDrop(player, give);
    }

    private int countInInventory(Player player, ItemStack type) {
        if (type == null) return 0;
        int total = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null || is.getType() != type.getType()) continue;
            total += is.getAmount();
        }
        return total;
    }

    private void removeFromInventory(Player player, ItemStack type, int qty) {
        int rem = qty;
        ItemStack[] contents = player.getInventory().getContents();
        for (int i = 0; i < contents.length && rem > 0; i++) {
            ItemStack is = contents[i];
            if (is == null || is.getType() != type.getType()) continue;
            int take = Math.min(is.getAmount(), rem);
            is.setAmount(is.getAmount() - take);
            if (is.getAmount() <= 0) contents[i] = null;
            rem -= take;
        }
        player.getInventory().setContents(contents);
    }
}
