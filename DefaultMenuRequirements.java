package pl.twojjadlospis.mealplanner.helperClasses;

import pl.twojjadlospis.mealplanner.entity.MenuRequirements;
import pl.twojjadlospis.mealplanner.entity.Visit;

import java.time.LocalDate;
import java.time.Period;

import static pl.twojjadlospis.mealplanner.service.Sex.SEX_FEMALE;
import static pl.twojjadlospis.mealplanner.service.Sex.SEX_MALE;

public class DefaultMenuRequirements {

    private Integer kcal;
    private Integer protein;
    private Integer fat;
    private Integer carbohydrates;
    private Integer fiber;
    private Integer proteinPercentage;
    private Integer fatPercentage;
    private Integer carbohydratesPercentage;
    private Integer fiberPercentage;
    private Integer bmr;

    public static DefaultMenuRequirements getDefaultMenuRequirements(Visit visit){
        return new DefaultMenuRequirements(visit);
    }
    public static MenuRequirements getMenuRequirementsWithDefaultValues(DefaultMenuRequirements defaultMenuRequirements){
        String name = "Jadłospis";
        int kcal = defaultMenuRequirements.getKcal();
        int protein = defaultMenuRequirements.getProtein();
        int fat = defaultMenuRequirements.getFat();
        int carbohydrates = defaultMenuRequirements.getCarbohydrates();
        int fiber = defaultMenuRequirements.getFiber();
        return new MenuRequirements(name, kcal, protein, fat, carbohydrates, fiber);
    }

    private DefaultMenuRequirements(Visit visit) {
        this.bmr = calculateBmr(visit);
        this.kcal = calculateKcal(visit);
        this.protein = calculateProtein(visit);
        this.fat = calculateFat(kcal);
        this.fiber = calculateFiber(visit);
        this.carbohydrates = calculateCarbohydrates(kcal,protein,fiber,fat);
        this.proteinPercentage = Math.round((protein*400)/kcal);
        this.fatPercentage = Math.round((fat*900)/kcal);
        this.carbohydratesPercentage = Math.round((carbohydrates*400)/kcal);
        this.fiberPercentage = Math.round((fiber*200)/kcal);
    }

    public static Integer calculateBmr(Visit visit) {
        String sex = visit.getPatient().getSex();
        double weight = visit.getWeight();
        double height = visit.getHeight();
        LocalDate now = LocalDate.now();
        int age = Period.between(visit.getPatient().getDateOfBirth(), now).getYears();
        double bmr = 0.0;
        // using Mifflin equation to count Basic Metabolic Rate
        if (SEX_FEMALE.getSex().equals(sex)) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }
        else if (SEX_MALE.getSex().equals(sex)) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        }
        return (int) Math.round(bmr);
    }

    public static Integer calculateKcal(Visit visit) {
        double pal = visit.getPal();
        int bmr = calculateBmr(visit);
        return (int) Math.round(bmr * pal);
    }

    private Integer calculateProtein(Visit visit) {
        double protein = visit.getRequirements().getProtein();
        int weight = visit.getWeight();
        return (int) Math.round(protein * weight);
    }

    private Integer calculateFat(int kcal){
        return (int)Math.round(kcal * 0.3 / 9);
    }

    private static Integer calculateCarbohydrates(Integer kcal, Integer protein, Integer fiber, Integer fat) {
        int remainingKcal = kcal - protein * 4 - fiber * 2 - fat * 9;
        return (int) Math.round(remainingKcal/4.0);
    }

    private Integer calculateFiber(Visit visit){
        return (int) Math.round(visit.getRequirements().getFiber());
    }

    public Integer getKcal() {
        return kcal;
    }

    public Integer getProtein() {
        return protein;
    }

    public Integer getFat() {
        return fat;
    }

    public Integer getCarbohydrates() {
        return carbohydrates;
    }

    public Integer getFiber() {
        return fiber;
    }

    public Integer getProteinPercentage() {
        return proteinPercentage;
    }

    public Integer getFatPercentage() {
        return fatPercentage;
    }

    public Integer getCarbohydratesPercentage() {
        return carbohydratesPercentage;
    }

    public Integer getFiberPercentage() {
        return fiberPercentage;
    }

    public Integer getBmr() {
        return bmr;
    }
}
