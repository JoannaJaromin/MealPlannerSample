package pl.twojjadlospis.mealplanner.helperClasses;

import org.junit.Test;
import pl.twojjadlospis.mealplanner.entity.MenuRequirements;
import pl.twojjadlospis.mealplanner.entity.Patient;
import pl.twojjadlospis.mealplanner.entity.Requirements;
import pl.twojjadlospis.mealplanner.entity.Visit;
import pl.twojjadlospis.mealplanner.helperClasses.DefaultMenuRequirements;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class DefaultMenuRequirementsTests {

    private LocalDate now = LocalDate.now();
    private LocalDate elevenYears = now.minusYears(11);
    private LocalDate seventyYears = now.minusYears(70);
    private Requirements requirementsMale11Years = new Requirements(1.1,19.0);
    private final Requirements requirementsFemale70Years = new Requirements(0.9, 20.0);
    private final Patient patientMale11Years = new Patient("M",elevenYears);
    private final Patient patientFemale70Years = new Patient("F",seventyYears);

    private final Visit VISIT_MALE = new Visit(patientMale11Years,requirementsMale11Years,43,153,1.6);
    private final Visit VISIT_FEMALE = new Visit(patientFemale70Years,requirementsFemale70Years,80,170, 1.2);
    private final DefaultMenuRequirements DEFAULTMR_MALE = DefaultMenuRequirements.getDefaultMenuRequirements(VISIT_MALE);
    private final DefaultMenuRequirements DEFAULTMR_FEMALE = DefaultMenuRequirements.getDefaultMenuRequirements(VISIT_FEMALE);
    private final MenuRequirements MENUREQ_MALE = DefaultMenuRequirements.getMenuRequirementsWithDefaultValues(DEFAULTMR_MALE);
    private final MenuRequirements MENUREQ_FEMALE = DefaultMenuRequirements.getMenuRequirementsWithDefaultValues(DEFAULTMR_FEMALE);

    private final Integer BMR_MALE = 1336;
    private final Integer BMR_FEMALE = 1352;

    private final Integer TEE_MALE = 2138;
    private final Integer TEE_FEMALE = 1622;

    private final Integer PROTEIN_MALE = 47;
    private final Integer PROTEIN_FEMALE = 72;

    private final Integer FAT_MALE = 71;
    private final Integer FAT_FEMALE = 54;

    private final Integer FIBER_MALE = 19;
    private final Integer FIBER_FEMALE = 20;

    private final Integer CARBOHYDRATES_MALE = 318;
    private final Integer CARBOHYDRATES_FEMALE = 202;

    @Test
    public void canCalculateBmr(){
        assertEquals(BMR_MALE,DEFAULTMR_MALE.getBmr());
        assertEquals(BMR_FEMALE,DEFAULTMR_FEMALE.getBmr());
        assertNotEquals(BMR_MALE,DEFAULTMR_FEMALE.getBmr());
    }

    @Test
    public void canCalculateTee(){
        assertEquals(TEE_MALE,DEFAULTMR_MALE.getKcal());
        assertEquals(TEE_MALE,MENUREQ_MALE.getKcal());
        assertEquals(TEE_FEMALE,DEFAULTMR_FEMALE.getKcal());
        assertEquals(TEE_FEMALE,MENUREQ_FEMALE.getKcal());
        assertNotEquals(TEE_MALE,DEFAULTMR_FEMALE.getKcal());
    }

    @Test
    public void canCalculateProtein(){
        assertEquals(PROTEIN_MALE,DEFAULTMR_MALE.getProtein());
        assertEquals(PROTEIN_MALE,MENUREQ_MALE.getProtein());
        assertEquals(PROTEIN_FEMALE,DEFAULTMR_FEMALE.getProtein());
        assertEquals(PROTEIN_FEMALE,MENUREQ_FEMALE.getProtein());
        assertNotEquals(PROTEIN_MALE,DEFAULTMR_FEMALE.getProtein());
    }

    @Test
    public void canCalculateFat(){
        assertEquals(FAT_MALE,DEFAULTMR_MALE.getFat());
        assertEquals(FAT_MALE,MENUREQ_MALE.getFat());
        assertEquals(FAT_FEMALE,DEFAULTMR_FEMALE.getFat());
        assertEquals(FAT_FEMALE,MENUREQ_FEMALE.getFat());
        assertNotEquals(FAT_MALE,MENUREQ_FEMALE.getFat());
    }

    @Test
    public void canCalculateFiber(){
        assertEquals(FIBER_MALE,DEFAULTMR_MALE.getFiber());
        assertEquals(FIBER_MALE,MENUREQ_MALE.getFiber());
        assertEquals(FIBER_FEMALE,DEFAULTMR_FEMALE.getFiber());
        assertEquals(FIBER_FEMALE,MENUREQ_FEMALE.getFiber());
        assertNotEquals(FIBER_FEMALE,MENUREQ_MALE.getFiber());
    }

    @Test
    public void canCalculateCarbohydrates(){
        assertEquals(CARBOHYDRATES_MALE,DEFAULTMR_MALE.getCarbohydrates());
        assertEquals(CARBOHYDRATES_MALE,MENUREQ_MALE.getCarbohydrates());
        assertEquals(CARBOHYDRATES_FEMALE,DEFAULTMR_FEMALE.getCarbohydrates());
        assertEquals(CARBOHYDRATES_FEMALE,MENUREQ_FEMALE.getCarbohydrates());
        assertNotEquals(CARBOHYDRATES_MALE,MENUREQ_FEMALE.getCarbohydrates());
    }

    @Test
    public void canCalculateProteinPercentage(){
        Integer proteinPercentageMale = Math.round(DEFAULTMR_MALE.getProtein()*400/DEFAULTMR_MALE.getKcal());
        Integer proteinPercentageFemale = Math.round(DEFAULTMR_FEMALE.getProtein()*400/DEFAULTMR_FEMALE.getKcal());
        assertEquals(proteinPercentageMale,DEFAULTMR_MALE.getProteinPercentage());
        assertEquals(proteinPercentageFemale,DEFAULTMR_FEMALE.getProteinPercentage());
    }

    @Test
    public void canCalculateFatPercentage(){
        Integer fatPercentageMale = Math.round(DEFAULTMR_MALE.getFat()*900/DEFAULTMR_MALE.getKcal());
        Integer fatPercentageFemale = Math.round(DEFAULTMR_FEMALE.getFat()*900/DEFAULTMR_FEMALE.getKcal());
        assertEquals(fatPercentageMale,DEFAULTMR_MALE.getFatPercentage());
        assertEquals(fatPercentageFemale,DEFAULTMR_FEMALE.getFatPercentage());
    }

    @Test
    public void canCalculateCarbohydratesPercentage(){
        Integer carbohydratesPercentageMale = Math.round(DEFAULTMR_MALE.getCarbohydrates()*400/DEFAULTMR_MALE.getKcal());
        Integer carbohydratesPercentageFemale = Math.round(DEFAULTMR_FEMALE.getCarbohydrates()*400/DEFAULTMR_FEMALE.getKcal());
        assertEquals(carbohydratesPercentageMale,DEFAULTMR_MALE.getCarbohydratesPercentage());
        assertEquals(carbohydratesPercentageFemale,DEFAULTMR_FEMALE.getCarbohydratesPercentage());
    }

    @Test
    public void canCalculateFiberPercentage(){
        Integer fiberPercentageMale = Math.round(DEFAULTMR_MALE.getFiber()*200/DEFAULTMR_MALE.getKcal());
        Integer fiberPercentageFemale = Math.round(DEFAULTMR_FEMALE.getFiber()*200/DEFAULTMR_FEMALE.getKcal());
        assertEquals(fiberPercentageMale,DEFAULTMR_MALE.getFiberPercentage());
        assertEquals(fiberPercentageFemale,DEFAULTMR_FEMALE.getFiberPercentage());
    }
}
