package pl.twojjadlospis.mealplanner.entity;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "menu_requirements")
public class MenuRequirements {

    // database table based on Mirosław Jarosz: "Normy żywienia dla populacji Polski", IŻŻ 2017

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    @Size(max = 45,message = "Długość nazwy nie może przekraczać 20 znaków")
    private String name;

    @NotNull(message = "Określ kaloryczność diety")
    @Max(value = 8000,message = "Kaloryczność diety nie może przekraczać 8000")
    @Column(name = "kcal")
    private Integer kcal;

    @NotNull(message = "Określ zawartość białka w diecie")
    @Max(value = 500,message = "Ilość białka w diecie nie może przekraczać 500 gramów")
    @Column(name = "protein")
    private Integer protein;

    @NotNull(message = "Określ zawartość tłuszczu w diecie")
    @Max(value = 500,message = "Ilość tłuszczu nie może przekraczać 500 gramów")
    @Column(name = "fat")
    private Integer fat;

    @Column(name = "carbohydrates")
    private Integer carbohydrates;

    @NotNull(message = "Określ zawartość błonnika w diecie.")
    @Max(value = 200, message = "Ilość błonnika w diecie nie może przekraczać 200 gramów")
    @Column(name = "fiber")
    private Integer fiber;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_visit")
    private Visit visit;

    @Formula("round(protein*400/kcal)")
    private Integer proteinPercentage;

    @Formula("round(fat*900/kcal)")
    private Integer fatPercentage;

    @Formula("round(carbohydrates*400/kcal)")
    private Integer carbohydratesPercentage;

    @Formula("round(fiber*200/kcal)")
    private Integer fiberPercentage;

    public MenuRequirements() {
    }

    public MenuRequirements(String name, Integer kcal, Integer protein, Integer fat, Integer carbohydrates, Integer fiber) {
        this.name = name;
        this.kcal = kcal;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getKcal() {
        return kcal;
    }

    public void setKcal(Integer kcal) {
        this.kcal = kcal;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(Integer carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public Integer getFiber() {
        return fiber;
    }

    public void setFiber(Integer fiber) {
        this.fiber = fiber;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
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

}
