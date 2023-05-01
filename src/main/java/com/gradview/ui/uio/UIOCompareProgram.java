package com.gradview.ui.uio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gradview.model.AccClass;
import com.gradview.model.AccProgram;

public class UIOCompareProgram 
{
    private AccProgram program;
    private List<AccClass> passedClasses;
    private List<AccClass> requriredClassesList;
    private List<AccClass> passedRequriredClasses;
    private List<AccClass> missingRequriredClasses;
    private List<AccClass> passedNonRequiredClasses;
    private int totalPassedCredits;
    private int totalPassedProgramCredits;
    private int totalPassedElectiveCredtis;
    

    public UIOCompareProgram( AccProgram program, List< AccClass > passedClasses,
            List< AccClass > requriredClassesList )
    {
        this.program = program;
        this.passedClasses = passedClasses;
        this.requriredClassesList = requriredClassesList;
        this.passedRequriredClasses = new ArrayList<>();
        this.missingRequriredClasses = new ArrayList<>();
        this.passedNonRequiredClasses = new ArrayList<>();
        this.totalPassedCredits = 0;
        this.totalPassedProgramCredits = 0;
        this.totalPassedElectiveCredtis = 0;
        this.fillPassedRequiredClassList();
        this.fillMissingRequriredClassesList();
        this.fillPassedNonRequiredClassesList();
        this.calculateCredits();
        this.sortAllClassLists();
    }

    private void sortAllClassLists()
    {
        this.passedClasses = this.sortClassList(this.passedClasses);
        this.requriredClassesList = this.sortClassList(this.requriredClassesList);
        this.passedRequriredClasses = this.sortClassList(this.passedRequriredClasses);
        this.missingRequriredClasses = this.sortClassList(this.missingRequriredClasses);
        this.passedNonRequiredClasses = this.sortClassList(this.passedNonRequiredClasses);
    }

    private void calculateCredits()
    {
        // Calculate total credtis
        for(AccClass tempClass : this.passedClasses) 
            this.totalPassedCredits = this.totalPassedCredits + tempClass.getCredits();
        // Calculate Program Credits
        for(AccClass tempClass : this.passedRequriredClasses) 
            this.totalPassedProgramCredits = this.totalPassedProgramCredits + tempClass.getCredits();
        // Calculate Elective Credits
        for(AccClass tempClass : this.passedNonRequiredClasses) 
            this.totalPassedElectiveCredtis = this.totalPassedElectiveCredtis + tempClass.getCredits();
    }

    private void fillPassedRequiredClassList()
    {
        for(AccClass passedClass : this.passedClasses) 
            for(AccClass requiredClass : this.requriredClassesList)
                if(passedClass.getId() == requiredClass.getId())
                    this.passedRequriredClasses.add(passedClass);
    }

    private void fillMissingRequriredClassesList()
    {
        for(AccClass requiredClass : this.requriredClassesList)
        {
            boolean notFound = true;
            for(AccClass passedClass : this.passedClasses)
                if(passedClass.getId() == requiredClass.getId())
                    notFound = false;
            if(notFound) this.missingRequriredClasses.add(requiredClass);
        }
    }

    private void fillPassedNonRequiredClassesList()
    {
        // Fill PassedRequiredClassList if passedRequriredClasses list is empty
        if(this.passedRequriredClasses.isEmpty()) this.fillPassedRequiredClassList();
        // If passedRequriredClasses list is not empty 
        if(!this.passedRequriredClasses.isEmpty())
        {
            // Loop through passed classes
            for(AccClass passedClass : this.passedClasses)
            {
                boolean notFound = true;
                // Loop through required passed classes
                for(AccClass passedRequiredClass : this.passedRequriredClasses)
                    if(passedClass.getId() == passedRequiredClass.getId())
                        notFound = false;
                // Check to see if class is not required
                if(notFound) this.passedNonRequiredClasses.add(passedClass);
            }
        }
    }

    
    /** 
     * @return AccProgram
     */
    public AccProgram getProgram()
    {
        return program;
    }

    public void setProgram( AccProgram program )
    {
        this.program = program;
    }

    public List< AccClass > getPassedClasses()
    {
        return passedClasses;
    }

    public void setPassedClasses( List< AccClass > passedClasses )
    {
        this.passedClasses = passedClasses;
    }

    public List< AccClass > getRequriredClassesList()
    {
        return requriredClassesList;
    }

    public void setRequriredClassesList( List< AccClass > requriredClassesList )
    {
        this.requriredClassesList = requriredClassesList;
    }

    public List< AccClass > getPassedRequriredClassesList()
    {
        return passedRequriredClasses;
    }

    public void setPassedRequriredClassesList( List< AccClass > passedRequriredClasses )
    {
        this.passedRequriredClasses = passedRequriredClasses;
    }

    public int getTotalPassedCredits()
    {
        return totalPassedCredits;
    }

    public void setTotalPassedCredits( int totalPassedCredits )
    {
        this.totalPassedCredits = totalPassedCredits;
    }

    public int getTotalPassedProgramCredits()
    {
        return totalPassedProgramCredits;
    }

    public void setTotalPassedProgramCredits( int totalPassedProgramCredits )
    {
        this.totalPassedProgramCredits = totalPassedProgramCredits;
    }

    public int getTotalPassedElectiveCredtis()
    {
        return totalPassedElectiveCredtis;
    }

    public void setTotalPassedElectiveCredtis( int totalPassedElectiveCredtis )
    {
        this.totalPassedElectiveCredtis = totalPassedElectiveCredtis;
    }

    public List< AccClass > getPassedRequriredClasses()
    {
        return passedRequriredClasses;
    }

    public void setPassedRequriredClasses( List< AccClass > passedRequriredClasses )
    {
        this.passedRequriredClasses = passedRequriredClasses;
    }

    public List< AccClass > getMissingRequriredClasses()
    {
        return missingRequriredClasses;
    }

    public void setMissingRequriredClasses( List< AccClass > missingRequriredClasses )
    {
        this.missingRequriredClasses = missingRequriredClasses;
    }

    public List< AccClass > getPassedNonRequiredClasses()
    {
        return passedNonRequiredClasses;
    }

    public void setPassedNonRequiredClasses( List< AccClass > passedNonRequiredClasses )
    {
        this.passedNonRequiredClasses = passedNonRequiredClasses;
    }

    @Override
    public String toString()
    {
        return "UIOCompareProgram [program=" + program + ", passedClasses=" + passedClasses + ", requriredClassesList="
                + requriredClassesList + ", passedRequriredClasses=" + passedRequriredClasses
                + ", missingRequriredClasses=" + missingRequriredClasses + ", passedNonRequiredClasses="
                + passedNonRequiredClasses + ", totalPassedCredits=" + totalPassedCredits
                + ", totalPassedProgramCredits=" + totalPassedProgramCredits + ", totalPassedElectiveCredtis="
                + totalPassedElectiveCredtis + "]";
    }

     /**
     * Sorts the {@link AccClass Class} List alabeticlay
     * @param classes
     * @return
     */
    private List<AccClass> sortClassList(List<AccClass> classes)
    {
        Collections.sort(classes, new Comparator<AccClass>()
        {
            @Override
            public int compare(AccClass class1, AccClass class2)
            {
                return class1.getNumber().compareTo(class2.getNumber());
            }
        });
        return classes;
    }

}
