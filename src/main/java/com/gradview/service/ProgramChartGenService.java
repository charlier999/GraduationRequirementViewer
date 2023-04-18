package com.gradview.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gradview.model.AccClass;
import com.gradview.model.AccProgram;



@Service
public class ProgramChartGenService 
{
    private static final Logger logger = LoggerFactory.getLogger(ProgramChartGenService.class);

    @Autowired
    private ProgramService programService;
    @Autowired
    private ClassService classService;

    public String genOrgChartDataByProgramModel(AccProgram program)
    {
        // Create error output chart
        String output = "[['Error',''],['Occured','Error']]";
        output = output.replace("\'", "\"");

        // Start org chart with program name
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode orgChart = this.startOrgChart(program.getName());

        // Get Required course information
        List<AccClass> requiredClasses = this.getClassListFromIDs(program.getRequiredMajorClasses());

        for(AccClass classI : requiredClasses)
        {
            orgChart = this.addBranchToOrgChart(orgChart, program.getName(), classI.getNumber()+":"+classI.getName());
        }

        // orgChart = this.addBranchToOrgChart(orgChart, program.getName(), "Branch");

        // Convert orgChart to a JSON string
        try
        {
            output = mapper.writeValueAsString(orgChart);
        }
        catch (JsonProcessingException ex)
        {
            ex.printStackTrace();
        }
        return output;
    }


    // Helper Functions -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=-

    /**
     * Starts the OrgChart ArrayNode
     * @param programName
     * @return
     */
    private ArrayNode startOrgChart(String programName)
    {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        ArrayNode innerArray = mapper.createArrayNode();
        innerArray.add(programName);
        innerArray.add("");
        array.add(innerArray);
        return array;
    }

    /**
     * Adds a branch to the orgChart.
     * @param orgChart The orgChart to add the branch to.
     * @param parentBranchValue The root where the child branch will be added on to.
     * @param childBranchValue The child branch name.
     * @return The orgChart containing the added child branch.
     */
    private ArrayNode addBranchToOrgChart(
        ArrayNode orgChart, String parentBranchValue, 
        String childBranchValue)
    {
        ObjectMapper mapper = new ObjectMapper();
        // If char contains values
        if(orgChart.size() != 0)
        {
            // Iterate through 
            for(int i = 0; i < orgChart.size(); i++)
            {
                // If the current array's, inside of the orgChart, first value contains the parentBranchValue.
                if(orgChart.get(i).get(0).asText().equals(parentBranchValue))
                {
                    // Create new array
                    ArrayNode array = mapper.createArrayNode();
                    array.add(childBranchValue); // assign first value as child branch name
                    array.add(parentBranchValue); // assign second value as parent branch name
                    orgChart.add(array); // add array to orgChart
                    break;
                }
            }
        }
        return orgChart;
    }

    /**
     * Finds the courses from the database from the array of ids
     * @param ids The array of course id numbers to find course pull data from
     * @return List of AccClass's containing course information from the requested id numbers.
     */
    private List<AccClass> getClassListFromIDs(int[] ids)
    {
        // Get basic course information and return it
        return this.classService.getBasicClassesByClassIDs(Arrays.stream(ids).boxed().collect(Collectors.toList()));

    }
}
