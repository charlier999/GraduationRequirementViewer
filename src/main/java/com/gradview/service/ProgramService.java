package com.gradview.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gradview.data.dam.AccProgramClassesDAM;
import com.gradview.data.dam.AccProgramDAM;
import com.gradview.data.dam.AccProgramElectivesCreditsDAM;
import com.gradview.data.dam.AccProgramGeneralEducationCreditsDAM;
import com.gradview.data.dam.AccProgramMajorCreditsDAM;
import com.gradview.data.dam.AccProgramTotalCreditsDAM;
import com.gradview.data.dao.AccProgramClassesDAO;
import com.gradview.data.dao.AccProgramDAO;
import com.gradview.data.dao.AccProgramElectivesCreditsDAO;
import com.gradview.data.dao.AccProgramGeneralEducationCreditsDAO;
import com.gradview.data.dao.AccProgramMajorCreditsDAO;
import com.gradview.data.dao.AccProgramTotalCreditsDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccProgram;

@Service
public class ProgramService
{
    private static final Logger logger = LoggerFactory.getLogger(ProgramService.class);
    @Autowired
    private AccProgramDAO accProgramDAO;
    @Autowired
    private AccProgramClassesDAO accProgramClassesDAO;
    @Autowired
    private AccProgramElectivesCreditsDAO accProgramElectivesCreditsDAO;
    @Autowired
    private AccProgramGeneralEducationCreditsDAO accProgramGeneralEducationCreditsDAO;
    @Autowired
    private AccProgramMajorCreditsDAO accProgramMajorCreditsDAO;
    @Autowired
    private AccProgramTotalCreditsDAO accProgramTotalCreditsDAO;

    /**
     * Retrieves Programs by the name of the program
     * @param input The name of the program to be searched for
     * @return List <{@link AccProgram}> list of programs found.
     * @throws DataAccessException
     * @throws NoRowsFoundException
     * @throws Exception
     */
    public List<AccProgram> getProgramsByName(String input) throws DataAccessException, NoRowsFoundException, Exception
    {
        logger.debug("getProgramByName: Starting");
        List<AccProgram> output = new ArrayList<>();
        try
        {
            logger.debug("getProgramByName: Retrieving ProgramDAMs");
            List<AccProgramDAM> programDAMs = accProgramDAO.search(AccProgramDAO.COL_NAME, input);

            for(int i = 0; i < programDAMs.size(); i++)
            {
                logger.debug("getProgramByName: Iteration " + i);
                AccProgram program = new AccProgram();
                // pull information from programDAMs
                program.setId(programDAMs.get(i).getId());
                program.setBaOfArts(programDAMs.get(i).isBaOfArts());
                program.setBaOfScience(programDAMs.get(i).isBaOfScience());
                program.setName(programDAMs.get(i).getName());
                program.setDescription(programDAMs.get(i).getDescription());
                program.setLevel(programDAMs.get(i).getLevel());

                logger.debug("getProgramByName: Retrieving AccProgramElectivesCreditsDAM");
                List<AccProgramElectivesCreditsDAM> accProgramElectivesCreditsDAMs =
                    accProgramElectivesCreditsDAO.search(AccProgramElectivesCreditsDAO.COL_PROGRAMID, ""+(programDAMs.get(i).getId()));
                program.setElectiveMinCredits(accProgramElectivesCreditsDAMs.get(0).getMinimum());
                program.setElectiveMaxCredits(accProgramElectivesCreditsDAMs.get(0).getMaximum());

                logger.debug("getProgramByName: Retrieving AccProgramGeneralEducationCreditsDAM");
                List<AccProgramGeneralEducationCreditsDAM> accProgramGeneralEducationCreditsDAMs =
                    accProgramGeneralEducationCreditsDAO.search(AccProgramGeneralEducationCreditsDAO.COL_PROGRAMID, ""+(programDAMs.get(i).getId()));
                program.setGenEdMinCredits(accProgramGeneralEducationCreditsDAMs.get(0).getMinimum());
                program.setGenEdMaxCredits(accProgramGeneralEducationCreditsDAMs.get(0).getMaximum());
                
                logger.debug("getProgramByName: Retrieving AccProgramMajorCreditsDAM");
                List<AccProgramMajorCreditsDAM> accProgramMajorCreditsDAMs = 
                    accProgramMajorCreditsDAO.search(AccProgramMajorCreditsDAO.COL_PROGRAMID, ""+(programDAMs.get(i).getId()));
                program.setMajorMinCredits(accProgramMajorCreditsDAMs.get(0).getCredits());
                program.setMajorMaxCredits(accProgramMajorCreditsDAMs.get(0).getCredits());

                logger.debug("getProgramByName: Retrieving AccProgramTotalCreditsDAM");
                List<AccProgramTotalCreditsDAM> accProgramTotalCreditsDAMs =
                    accProgramTotalCreditsDAO.search(AccProgramTotalCreditsDAO.COL_PROGRAMID, ""+(programDAMs.get(i).getId()));
                program.setTotalMinCredits(accProgramTotalCreditsDAMs.get(0).getCredits()); 
                try
                {
                    logger.debug("getProgramByName: Retrieving AccProgramClassesDAM");
                    List<AccProgramClassesDAM> accProgramClassesDAMs =
                    accProgramClassesDAO.search(AccProgramClassesDAO.COL_PROGRAMID, Integer.toString(program.getId()));
                    int[] classIDs = new int[accProgramClassesDAMs.size()];
                    for(int j = 0; j <accProgramClassesDAMs.size(); j++)
                    {
                        classIDs[j] = accProgramClassesDAMs.get(j).getClassID();
                    }
                    program.setRequiredMajorClasses(classIDs);

                }
                catch ( NoRowsFoundException e )
                {
                    logger.warn("getProgramByName: No Rows Found Exception occured: " + e);
                }

                output.add(program);   
                logger.debug("getProgramByName: Iteration " + i + " Complete");
            }
            logger.debug("getProgramByName: Returning Programs");
            return output;
        }
        catch ( DataAccessException e )
        {
            logger.error("getProgramByName: Data Access Exception occured: " + e);
            throw e;
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("getProgramByName: No Rows Found Exception occured: " + e);
            throw e;
        }
        catch ( Exception e )
        {
            logger.error("getProgramByName: Exception occured: " + e);
            throw e;
        }
    }




}