package pl.twojjadlospis.mealplanner.service;

import org.junit.Test;
import pl.twojjadlospis.mealplanner.entity.Patient;
import pl.twojjadlospis.mealplanner.entity.Visit;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class VisitServiceTests {

    private VisitServiceImpl visitService = new VisitServiceImpl();

    private final LocalDate NOW = LocalDate.now();
    private final LocalDate THREE_MONTHS_OLD = NOW.minusMonths(3);
    private final LocalDate TWO_YEARS_OLD = NOW.minusYears(2);
    private final LocalDate EIGHTEEN_YEARS_OLD = NOW.minusYears(18);
    private final LocalDate FORTY_YEARS_OLD = NOW.minusYears(40);

    private final Patient THREE_MONTHS_OLD_FEMALE = new Patient("F", THREE_MONTHS_OLD);
    private final Patient TWO_YEARS_OLD_FEMALE = new Patient("F", TWO_YEARS_OLD);
    private final Patient EIGHTEEN_YEARS_OLD_FEMALE = new Patient("F", EIGHTEEN_YEARS_OLD);
    private final Patient FORTY_YEARS_OLD_FEMALE = new Patient("F", FORTY_YEARS_OLD);

    private final Patient THREE_MONTHS_OLD_MALE = new Patient("M", THREE_MONTHS_OLD);
    private final Patient TWO_YEARS_OLD_MALE = new Patient("M", TWO_YEARS_OLD);
    private final Patient EIGHTEEN_YEARS_OLD_MALE = new Patient("M", EIGHTEEN_YEARS_OLD);
    private final Patient FORTY_YEARS_OLD_MALE = new Patient("M", FORTY_YEARS_OLD);

    private final Visit VISIT = new Visit(false,false);
    private final Visit VISIT_PREGNANT = new Visit(true,false);
    private final Visit VISIT_BREASTFEEDING = new Visit(false,true);
    private final Visit VISIT_PREGNANT_BREASTFEEDING = new Visit(true,true);


    @Test
    public void canInterpretBMI(){
        String underweight = visitService.interpretBmi(17.9);
        String normalWeight = visitService.interpretBmi(23.7);
        String overweight = visitService.interpretBmi(29.9);
        String obesityClass1 = visitService.interpretBmi(30.0);
        String obesityClass2 = visitService.interpretBmi(39.9);
        String obesityClass3 = visitService.interpretBmi(43.0);
        assertEquals(BmiInterpretation.UNDERWEIGHT.getBmiInterpretation(),underweight);
        assertEquals(BmiInterpretation.NORMAL_WEIGHT.getBmiInterpretation(),normalWeight);
        assertEquals(BmiInterpretation.OVERWEIGHT.getBmiInterpretation(),overweight);
        assertEquals(BmiInterpretation.OBESITY_CLASS_1.getBmiInterpretation(),obesityClass1);
        assertEquals(BmiInterpretation.OBESITY_CLASS_2.getBmiInterpretation(),obesityClass2);
        assertEquals(BmiInterpretation.OBESITY_CLASS_3.getBmiInterpretation(),obesityClass3);
    }

    @Test
    public void canGetInformationAboutPregnancyOrBreastfeeding(){
        String notPregnantOrBreastfeeding = visitService.getPregnantOrBreastfeeding(VISIT);
        String pregnant = visitService.getPregnantOrBreastfeeding(VISIT_PREGNANT);
        String breastFeeding = visitService.getPregnantOrBreastfeeding(VISIT_BREASTFEEDING);
        String pregnantAndBreastfeeding = visitService.getPregnantOrBreastfeeding(VISIT_PREGNANT_BREASTFEEDING);
        assertEquals("",notPregnantOrBreastfeeding);
        assertEquals("ciąża",pregnant);
        assertEquals("karmienie piersią",breastFeeding);
        assertEquals("ciąża i karmienie piersią",pregnantAndBreastfeeding);
    }

    @Test
    public void canGetRequirementsForMale(){
        int requirementsForThreeMonths = visitService.getRequirementsId(THREE_MONTHS_OLD_MALE,VISIT);
        int requirementsForTwoYears = visitService.getRequirementsId(TWO_YEARS_OLD_MALE,VISIT);
        int requirementsForEighteenYears = visitService.getRequirementsId(EIGHTEEN_YEARS_OLD_MALE,VISIT);
        int requirementsForFortyYears = visitService.getRequirementsId(FORTY_YEARS_OLD_MALE,VISIT);
        assertEquals(1,requirementsForThreeMonths);
        assertEquals(3,requirementsForTwoYears);
        assertEquals(8,requirementsForEighteenYears);
        assertEquals(10,requirementsForFortyYears);
    }


    @Test
    public void canGetRequirementsForFemale(){
        int requirementsForThreeMonths = visitService.getRequirementsId(THREE_MONTHS_OLD_FEMALE,VISIT);
        int requirementsForTwoYears = visitService.getRequirementsId(TWO_YEARS_OLD_FEMALE,VISIT);
        int requirementsForEighteenYears = visitService.getRequirementsId(EIGHTEEN_YEARS_OLD_FEMALE,VISIT);
        int requirementsForFortyYears = visitService.getRequirementsId(FORTY_YEARS_OLD_FEMALE,VISIT);
        assertEquals(14,requirementsForThreeMonths);
        assertEquals(16,requirementsForTwoYears);
        assertEquals(21,requirementsForEighteenYears);
        assertEquals(23,requirementsForFortyYears);
    }

    @Test
    public void canGetRequirementsForPregnantFemale(){
        int requirementsForEighteenYears = visitService.getRequirementsId(EIGHTEEN_YEARS_OLD_FEMALE,VISIT_PREGNANT);
        int requirementsForFortyYears = visitService.getRequirementsId(FORTY_YEARS_OLD_FEMALE,VISIT_PREGNANT);
        assertEquals(27,requirementsForEighteenYears);
        assertEquals(28,requirementsForFortyYears);
    }

    @Test
    public void canGetRequirementsForBreastfeedingFemale(){
        int requirementsForEighteenYears = visitService.getRequirementsId(EIGHTEEN_YEARS_OLD_FEMALE,VISIT_BREASTFEEDING);
        int requirementsForFortyYears = visitService.getRequirementsId(FORTY_YEARS_OLD_FEMALE,VISIT_BREASTFEEDING);
        assertEquals(29,requirementsForEighteenYears);
        assertEquals(30,requirementsForFortyYears);
    }

    @Test
    public void canGetRequirementsForPregnantAndBreastfeedingFemale(){
        int requirementsForEighteenYears = visitService.getRequirementsId(EIGHTEEN_YEARS_OLD_FEMALE,VISIT_PREGNANT_BREASTFEEDING);
        int requirementsForFortyYears = visitService.getRequirementsId(FORTY_YEARS_OLD_FEMALE,VISIT_PREGNANT_BREASTFEEDING);
        assertEquals(31,requirementsForEighteenYears);
        assertEquals(32,requirementsForFortyYears);
    }
}
