package com.gradview.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;
import com.gradview.model.AccClassPrerequisite;
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
        // Add required courses to OrgChart
        for(AccClass classI : requiredClasses)
        {
            orgChart = this.addBranchToOrgChart(orgChart, program.getName(), classI.getNumber()+":"+classI.getName());
            // pull list of prerequs
            try
            {
                // Get full class info
                List<AccClass> fullClass = this.classService.getClassByNumber(classI.getNumber());
                // 
                if(fullClass.size() > 0) 
                {
                    classI = fullClass.get(0);
                    orgChart = this.requrAddPreReqsToOrgChart(orgChart, classI, program.getName());
                    classI = classI; // breakpoint check
                }
            }
            catch ( DataAccessException e )
            {
                logger.error("genOrgChartDataByProgramModel: DataAccessException occured on class: "+classI.getNumber());
                e.printStackTrace();
            }
            catch ( NoRowsFoundException e )
            {
                // DO nothing
            }
            catch ( Exception e )
            {
                logger.error("genOrgChartDataByProgramModel: Exception occured on class: "+classI.getNumber());
                e.printStackTrace();
            }
        }

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

    /**
     * (RECURSIVE) adds prereusites to OrgChart. 
     * If course's prereqs have prereqs, function will recursivly add the prereqs
     * @param orgChart The OrgChart ot add branches to
     * @param course The course to exctract prereqs from to add to Org Chart
     * @return The OrgChart with added courses.
     */
    private ArrayNode requrAddPreReqsToOrgChart(ArrayNode orgChart, AccClass course, String parrentCourseNumber)
    {
        // Iterate though prereqs in input course
        for(AccClassPrerequisite accPrereq : course.getPrerequisites())
        {
            // Get course information from list of prereq ids
            List<AccClass> prereqClasses = this.classService.getBasicClassesByClassIDs(accPrereq.classIDs);
            // Iterrate through prereqs courses
            for(AccClass preReqClass : prereqClasses)
            {
                String parentBranch = "";
                // Create branch strings
                if(parrentCourseNumber.contains(":")) parentBranch = course.getNumber()+":"+course.getName()+":PRof "+parrentCourseNumber;
                else parentBranch = course.getNumber()+":"+course.getName();
                String childBranch = preReqClass.getNumber()+":"+preReqClass.getName()+":PRof "+course.getNumber();

                // Add branch to orgChart
                orgChart = this.addBranchToOrgChart(orgChart, parentBranch, childBranch);

                try
                {
                    // Get full class info
                    List<AccClass> fullClass = this.classService.getBasicClassByNumber(preReqClass.getNumber());
                    // If only one coure is returned
                    if(fullClass.size() == 1) 
                    {
                        // update preReqClass with full class info
                        preReqClass = fullClass.get(0);
                        // Requre for prerequesits found.
                        orgChart = this.requrAddPreReqsToOrgChart(orgChart, preReqClass, course.getNumber());
                    }
                }
                catch ( DataAccessException e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch ( NoRowsFoundException e )
                {
                    // DO nothing
                }
                catch ( Exception e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return orgChart;
    }
}
