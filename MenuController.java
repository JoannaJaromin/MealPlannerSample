package pl.twojjadlospis.mealplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.twojjadlospis.mealplanner.helperClasses.DefaultMenuRequirements;
import pl.twojjadlospis.mealplanner.entity.MenuRequirements;
import pl.twojjadlospis.mealplanner.entity.Patient;
import pl.twojjadlospis.mealplanner.entity.Visit;
import pl.twojjadlospis.mealplanner.service.MenuService;
import pl.twojjadlospis.mealplanner.service.VisitService;

import javax.validation.Valid;

@Controller
@RequestMapping("/jadlospis")
public class MenuController {

    private MenuService menuService;

    private VisitService visitService;

    @Autowired
    public MenuController(MenuService menuService, VisitService visitService) {
        this.menuService = menuService;
        this.visitService = visitService;
    }

    @GetMapping("/okreslZalozenia")
    public String showFormForMenuRequirements(@RequestParam("visitId") int visitId, Model model) {
        Visit visit = visitService.getVisit(visitId);
        DefaultMenuRequirements defaultMenuRequirements = DefaultMenuRequirements.getDefaultMenuRequirements(visit);
        MenuRequirements menuRequirements = DefaultMenuRequirements.getMenuRequirementsWithDefaultValues(defaultMenuRequirements);
        model.addAttribute("defaultMenuRequirements", defaultMenuRequirements);
        model.addAttribute("menuRequirements", menuRequirements);
        model.addAttribute("visit", visit);
        return "requirements-form";
    }

    @GetMapping("/edytujZalozenia")
    public String editRequirements(@RequestParam("menuId") int id, Model model) {
        MenuRequirements menuRequirements = menuService.getMenu(id);
        Visit visit = menuRequirements.getVisit();
        DefaultMenuRequirements defaultMenuRequirements = DefaultMenuRequirements.getDefaultMenuRequirements(visit);
        model.addAttribute("defaultMenuRequirements", defaultMenuRequirements);
        model.addAttribute("menuRequirements", menuRequirements);
        model.addAttribute("visit",visit);
        return "requirements-form";
    }

    @PostMapping("/zapiszZalozenia")
    public String saveMenuRequirements(@Valid @ModelAttribute("menuRequirements") MenuRequirements menuRequirements,
                                       BindingResult bindingResult,
                                       @RequestParam("visitId") int visitId,
                                       Model model) {
        if(bindingResult.hasErrors()){
            Visit visit = visitService.getVisit(visitId);
            DefaultMenuRequirements defaultMenuRequirements = DefaultMenuRequirements.getDefaultMenuRequirements(visit);
            model.addAttribute("visit",visit);
            model.addAttribute("menuRequirements",menuRequirements);
            model.addAttribute("defaultMenuRequirements",defaultMenuRequirements);
            return "requirements-form";
        }
        else {
            Visit visit = visitService.getVisit(visitId);
            DefaultMenuRequirements defaultMenuRequirements = DefaultMenuRequirements.getDefaultMenuRequirements(visit);
            Integer carbohydrates = defaultMenuRequirements.getCarbohydrates();
            menuRequirements.setCarbohydrates(carbohydrates);
            menuService.saveMenuRequirements(menuRequirements, visitId);
            return "redirect:/wizyta/szczegoly?visitId=" + visitId;
        }
    }

    @GetMapping("/usunJadlospis")
    public String deleteMenu(@RequestParam("menuId") int menuId) {
        MenuRequirements menuRequirements = menuService.getMenu(menuId);
        Integer visitId = menuRequirements.getVisit().getId();
        menuService.deleteMenu(menuId);
        return "redirect:/wizyta/szczegoly?visitId=" + visitId;
    }

    @GetMapping("/planujPosilki")
    public String showFormForMealsPlanning(@RequestParam("menuId") int menuId, Model model) {
        MenuRequirements menuRequirements = menuService.getMenu(menuId);
        Visit visit = menuRequirements.getVisit();
        Patient patient = visit.getPatient();
        model.addAttribute("menuRequirements", menuRequirements);
        model.addAttribute("visit", visit);
        model.addAttribute("patient", patient);
        return "meal-planning";
    }
}