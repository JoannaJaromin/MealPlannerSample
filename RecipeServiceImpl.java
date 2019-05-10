package pl.twojjadlospis.mealplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojjadlospis.mealplanner.dao.RecipeDAO;
import pl.twojjadlospis.mealplanner.entity.Product;
import pl.twojjadlospis.mealplanner.entity.ProductsInRecipes;
import pl.twojjadlospis.mealplanner.entity.Recipe;
import pl.twojjadlospis.mealplanner.entity.User;
import pl.twojjadlospis.mealplanner.helperClasses.RecipeNutritionalValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeDAO recipeDAO;

    @Autowired
    public RecipeServiceImpl(RecipeDAO recipeDAO) {
        this.recipeDAO = recipeDAO;
    }

    @Override
    @Transactional
    public List<Recipe> getRecipes(Integer userId) {
        return recipeDAO.getRecipes(userId);
    }

    @Override
    @Transactional
    public List<RecipeNutritionalValues> searchRecipeNutritionalValues(String searchName, Integer userId) {
        List<Recipe> recipes = recipeDAO.searchRecipes(searchName, userId);
        return getRecipesNutritionalValuesFromRecipesList(recipes);
    }

    @Override
    public List<RecipeNutritionalValues> getRecipesNutritionalValues(Integer userId) {
        List<Recipe> recipes = getRecipes(userId);
        return getRecipesNutritionalValuesFromRecipesList(recipes);
    }

    private List<RecipeNutritionalValues> getRecipesNutritionalValuesFromRecipesList(List<Recipe> recipes) {
        if (recipes == null){
            return Collections.emptyList();
        }
        List<RecipeNutritionalValues> recipesNutritionalValues = new ArrayList<>();
        for (Recipe recipe : recipes) {
            RecipeNutritionalValues values = createRecipeNutritionalValues(recipe);
            recipesNutritionalValues.add(values);
        }
        return recipesNutritionalValues;
    }

    private RecipeNutritionalValues createRecipeNutritionalValues(Recipe recipe) {
        int recipeId = recipe.getId();
        double kcal=0;
        double protein=0;
        double fat=0;
        double carbohydrate=0;
        double fiber=0;
        for (ProductsInRecipes productsInRecipes : getProducts(recipeId)) {
            int amount = productsInRecipes.getAmount();
            Product product = productsInRecipes.getProduct();
            kcal += amount * product.getKcal() / 100.0;
            protein += amount * product.getProtein() / 100.0;
            fat += amount * product.getFat() / 100.0;
            carbohydrate += amount * product.getCarbohydrate() / 100.0;
            fiber += amount * product.getFiber() / 100.0;
        }
        String recipeName = recipe.getName();
        return new RecipeNutritionalValues((int)Math.round(kcal),(int)Math.round(protein),
                (int)Math.round(fat),(int)Math.round(carbohydrate),(int)Math.round(fiber),
                (int)Math.round(recipeId),recipeName);
    }

    @Override
    public void addRecipeToUser(Recipe recipe, User user) {
        user.addRecipe(recipe);
    }

    @Override
    @Transactional
    public void saveRecipe(Recipe recipe, Integer userId) {
        recipeDAO.saveRecipe(recipe, userId);
    }

    @Override
    @Transactional
    public List<ProductsInRecipes> getProducts(Integer id) {
        return recipeDAO.getProducts(id);
    }

    @Override
    @Transactional
    public List<Product> getSearchedProducts(String searchName) {
        return recipeDAO.getSearchedProducts(searchName);
    }

    @Override
    @Transactional
    public Recipe getRecipe(Integer recipeId) {
        return recipeDAO.getRecipe(recipeId);
    }

    @Override
    @Transactional
    public Product getProduct(Integer productId) {
        return recipeDAO.getProduct(productId);
    }

    @Override
    public void addProductToRecipe(Recipe recipe, ProductsInRecipes productsInRecipes) {
        recipe.addProductsInRecipe(productsInRecipes);
    }

    @Override
    public void saveProductInRecipe(Recipe recipe, Product product, ProductsInRecipes productInRecipe) {
        recipeDAO.saveProductInRecipe(recipe,product,productInRecipe);
    }

    @Override
    @Transactional
    public void deleteRecipe(Integer recipeId) {
        recipeDAO.deleteRecipe(recipeId);
    }

    @Override
    @Transactional
    public void updateAmountOfProductInRecipe(Integer productInRecipeId, Integer amount) {
        recipeDAO.updateAmountOfProductInRecipe(productInRecipeId, amount);
    }

    @Override
    @Transactional
    public void deleteProductFromRecipe(Integer productsInRecipeId) {
        recipeDAO.deleteProductFromRecipe(productsInRecipeId);
    }
}
