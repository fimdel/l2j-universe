package l2p.gameserver.model;

import l2p.gameserver.templates.item.RecipeTemplate.RecipeComponent;

/**
 * This class describes a Recipe used by Dwarf to craft Item.
 * All L2RecipeList are made of L2RecipeInstance (1 line of the recipe : Item-Quantity needed).<BR><BR>
 */
public class Recipe {
    /**
     * The table containing all L2RecipeInstance (1 line of the recipe : Item-Quantity needed) of the L2RecipeList
     */
    private RecipeComponent[] _recipes;

    /**
     * The Identifier of the Instance
     */
    private int _id;

    /**
     * The crafting level needed to use this L2RecipeList
     */
    private int _level;

    /**
     * The Identifier of the L2RecipeList
     */
    private int _recipeId;

    /**
     * The name of the L2RecipeList
     */
    private String _recipeName;

    /**
     * The crafting success rate when using the L2RecipeList
     */
    private int _successRate;

    /**
     * The crafting MP cost of this L2RecipeList
     */
    private int _mpCost;

    /**
     * The Identifier of the Item crafted with this L2RecipeList
     */
    private int _itemId;

    private int _foundation;

    /**
     * The quantity of Item crafted when using this L2RecipeList
     */
    private int _count;

    /**
     * Is dvarven-only or common
     */
    private boolean _isdwarvencraft;

    private long _exp, _sp;

    /**
     * Constructor<?> of L2RecipeList (create a new Recipe).<BR><BR>
     */
    public Recipe(int id, int level, int recipeId, String recipeName, int successRate, int mpCost, int itemId, int foundation, int count, long exp, long sp, boolean isdwarvencraft) {
        _id = id;
        _recipes = new RecipeComponent[0];
        _level = level;
        _recipeId = recipeId;
        _recipeName = recipeName;
        _successRate = successRate;
        _mpCost = mpCost;
        _itemId = itemId;
        _foundation = foundation;
        _count = count;
        _exp = exp;
        _sp = sp;
        _isdwarvencraft = isdwarvencraft;
    }

    /**
     * Add a L2RecipeInstance to the L2RecipeList (add a line Item-Quantity needed to the Recipe).<BR><BR>
     */
    public void addRecipe(RecipeComponent recipe) {
        int len = _recipes.length;
        RecipeComponent[] tmp = new RecipeComponent[len + 1];
        System.arraycopy(_recipes, 0, tmp, 0, len);
        tmp[len] = recipe;
        _recipes = tmp;
    }

    /**
     * Return the Identifier of the Instance.<BR><BR>
     */
    public int getId() {
        return _id;
    }

    /**
     * Return the crafting level needed to use this L2RecipeList.<BR><BR>
     */
    public int getLevel() {
        return _level;
    }

    /**
     * Return the Identifier of the L2RecipeList.<BR><BR>
     */
    public int getRecipeId() {
        return _recipeId;
    }

    /**
     * Return the name of the L2RecipeList.<BR><BR>
     */
    public String getRecipeName() {
        return _recipeName;
    }

    /**
     * Return the crafting success rate when using the L2RecipeList.<BR><BR>
     */
    public int getSuccessRate() {
        return _successRate;
    }

    /**
     * Return the crafting MP cost of this L2RecipeList.<BR><BR>
     */
    public int getMpCost() {
        return _mpCost;
    }

    /**
     * Return the Identifier of the Item crafted with this L2RecipeList.<BR><BR>
     */
    public int getItemId() {
        return _itemId;
    }

    /**
     * Return the quantity of Item crafted when using this L2RecipeList.<BR><BR>
     */
    public int getCount() {
        return _count;
    }

    /**
     * Return the table containing all L2RecipeInstance (1 line of the recipe : Item-Quantity needed) of the L2RecipeList.<BR><BR>
     */
    public RecipeComponent[] getRecipes() {
        return _recipes;
    }

    public boolean isDwarvenRecipe() {
        return _isdwarvencraft;
    }

    public long getExp() {
        return _exp;
    }

    public long getSp() {
        return _sp;
    }

    public int getFoundation() {
        return _foundation;
    }
}