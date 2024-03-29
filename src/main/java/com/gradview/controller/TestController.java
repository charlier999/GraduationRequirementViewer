package com.gradview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
public class TestController 
{
    private static final Logger logger = LoggerFactory.getLogger( TestController.class );

    
    /** 
     * @param model
     * @return String
     */
    @GetMapping("/test/chart")
    public String displayChartTest(Model model)
    {
        logger.info("displayChartTest: Starting");

        logger.info("displayChartTest: Returning Chart");
        return "test/chartTest";
    }
}
