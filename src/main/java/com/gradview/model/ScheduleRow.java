package com.gradview.model;

public class ScheduleRow 
{
    /**
     * The row id number
     */
    public int id;
    /**
     * The course number
     */
    public String courseNumber;
    /**
     * Has the course been passed
     */
    public boolean isPassed;
    
    /**
     * 
     * @param id            - The row id number
     * @param courseNumber  - The course number
     * @param isPassed      - Has the course been passed
     */
    public ScheduleRow(int id, String courseNumber, boolean isPassed)
    {
        super();
        this.id = id;
        this.courseNumber = courseNumber;
        this.isPassed = isPassed;
    }

    @Override
    public String toString()
    {
        String output = "["+this.id+","+this.courseNumber+","+(this.isPassed ? 1:0)+"]";
        return output;
    }

    /**
     * Parses string to {@link ScheduleRow}. null if not valid string
     * @param input String to convert to {@link ScheduleRow}.
     * @return {@link ScheduleRow} if string is valid. null if invalid.
     */
    static ScheduleRow parse(String input)
    {
        // Check for endings []
        if(input.startsWith("[") && input.endsWith("]") && input.length() > 2)
        {
            // Remove [] from string
            input = input.substring(1, input.length() - 1);
            // Split values by ,
            String[] rows = input.split(",");
            // check for Not three values
            if(rows.length != 3) return null;
            // Assign row values to new ScheduleRow and return it
            return new ScheduleRow(Integer.parseInt(rows[0]), rows[1], Boolean.parseBoolean(rows[2]));
        }
        else return null;
    }
}
