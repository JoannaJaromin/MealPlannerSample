package pl.twojjadlospis.mealplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.twojjadlospis.mealplanner.helperClasses.RecipeNutritionalValues;
import pl.twojjadlospis.mealplanner.entity.Product;
import pl.twojjadlospis.mealplanner.entity.ProductsInRecipes;
import pl.twojjadlospis.mealplanner.entity.Recipe;
import pl.twojjadlospis.mealplanner.entity.User;
import pl.twojjadlospis.mealplanner.service.RecipeService;
import pl.twojjadlospis.mealplanner.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/przepis")
public class RecipeController {

    private RecipeService recipeService;
    private UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping("/listaPrzepisow")
    public String listRecipes(Model model) {
        Integer userId = userService.getUserId();
        List<RecipeNutritionalValues> recipes = recipeService.getRecipesNutritionalValues(userId);
        model.addAttribute("recipes", recipes);
        return "list-recipes";
    }

    @GetMapping("/edytujPrzepis")
    public String showFormForRecipeUpdate(@RequestParam("recipeId") Integer recipeId, Model model) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        List<ProductsInRecipes> productsInRecipe = recipeService.getProducts(recipeId);
        model.addAttribute("productsInRecipe", productsInRecipe);
        model.addAttribute("recipe", recipe);
        return "recipe-instructions-form";
    }

    @GetMapping("/szukaj")
    public String searchRecipes(@RequestParam("fraza") String searchName, Model model) {
        Integer userId = userService.getUserId();
        List<RecipeNutritionalValues> recipes =
                recipeService.searchRecipeNutritionalValues(searchName, userId);
        model.addAttribute("recipes", recipes);
        return "list-recipes";
    }

    @GetMapping("/dodajPrzepis")
    public String showFormForAdd(Model model) {
        Recipe recipe = new Recipe();
        User user = userService.getUser();
        recipeService.addRecipeToUser(recipe, user);
        model.addAttribute("recipe", recipe);
        return "recipe-instructions-form";
    }

    @PostMapping("/dodajProdukty")
    public String saveRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "recipe-instructions-form";
        } else {
            Integer userId = userService.getUserId();
            recipeService.saveRecipe(recipe, userId);
            List<ProductsInRecipes> productsInRecipe;
            if (recipeService.getProducts(recipe.getId()) != null){
                productsInRecipe=recipeService.getProducts(recipe.getId());
            }
            else productsInRecipe = new ArrayList<>();
            model.addAttribute("recipe", recipe);
            model.addAttribute("productsInRecipe",productsInRecipe);
            return "recipe-products-form";
        }
    }

    @GetMapping("/edytujProdukty")
    public String showFormForRecipeProductsUpdate(@RequestParam("recipeId") Integer recipeId,
                                                  Model model) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        List<ProductsInRecipes> productsInRecipe = recipeService.getProducts(recipeId);
        model.addAttribute("productsInRecipe", productsInRecipe);
        model.addAttribute("recipe", recipe);
        return "recipe-products-form";
    }

    @GetMapping("/szukajProduktu")
    public String searchProducts(@RequestParam("fraza") String searchName, @RequestParam("recipeId") Integer recipeId, Model model) {
        List<Product> searchedProducts = recipeService.getSearchedProducts(searchName);
        Recipe recipe = recipeService.getRecipe(recipeId);
        List<ProductsInRecipes> productsInRecipe = recipeService.getProducts(recipe.getId());
        model.addAttribute("searchedProducts", searchedProducts);
        model.addAttribute("recipe", recipe);
        model.addAttribute("productsInRecipe", productsInRecipe);
        return "recipe-products-form";
    }

    @GetMapping("/dodajProdukt")
    public String addProductToRecipe(@RequestParam("productId") Integer productId,
                                     @RequestParam("recipeId") Integer recipeId,
                                     @RequestParam("amount") Integer amount,
                                     Model model) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        Product product = recipeService.getProduct(productId);
        ProductsInRecipes productInRecipe = new ProductsInRecipes(amount, product, recipe);
        recipeService.saveProductInRecipe(recipe, product, productInRecipe);
        List<ProductsInRecipes> productsInRecipe = recipeService.getProducts(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("productsInRecipe", productsInRecipe);
        return "redirect:/przepis/edytujProdukty?recipeId=" + recipeId;
    }

    @GetMapping("/aktualizujIloscProduktu")
    public String updateAmountOfProductInRecipe(@RequestParam("productInRecipeId") Integer productInRecipeId,
                                                @RequestParam("amount") Integer amount,
                                                @RequestParam("recipeId") Integer recipeId) {
        recipeService.updateAmountOfProductInRecipe(productInRecipeId, amount);
        return "redirect:/przepis/edytujProdukty?recipeId=" + recipeId;
    }

    @GetMapping("/usunProdukt")
    public String deleteProductFromRecipe(@RequestParam("productsInRecipeId") Integer productsInRecipeId,
                                          @RequestParam("recipeId") Integer recipeId, Model model) {
        recipeService.deleteProductFromRecipe(productsInRecipeId);
        Recipe recipe = recipeService.getRecipe(recipeId);
        List<ProductsInRecipes> productsInRecipe = recipeService.getProducts(recipeId);
        model.addAttribute("productsInRecipe", productsInRecipe);
        model.addAttribute("recipe", recipe);
        return "recipe-products-form";
    }

    @GetMapping("/usunPrzepis")
    public String deleteRecipe(@RequestParam("recipeId") Integer recipeId) {
        recipeService.deleteRecipe(recipeId);
        return "redirect:/przepis/listaPrzepisow";
    }

    @GetMapping("/szczegoly")
    public String showRecipeDetails(@RequestParam("recipeId") Integer recipeId, Model model) {
        Recipe recipe = recipeService.getRecipe(recipeId);
        List<ProductsInRecipes> productsInRecipe = recipeService.getProducts(recipe.getId());
        model.addAttribute("recipe", recipe);
        model.addAttribute("productsInRecipe", productsInRecipe);
        return "recipe-view";
    }


}
