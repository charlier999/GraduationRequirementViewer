package com.gradview.model;

import java.util.ArrayList;
import java.util.List;

public class Schedule 
{
    public List<ScheduleRow> rows;

    
    public Schedule(List<ScheduleRow> rows)
    {
        super();
        this.rows = rows;
    }
    public Schedule()
    {
        super();
        this.rows = new ArrayList<ScheduleRow>();
    }

    
    /** 
     * @return String
     */
    @Override
    public String toString()
    {
        String output = "{";
        // Get the last ID number
        int lastID = this.rows.size() - 1;
        // Iterate through rows
        for(ScheduleRow row : this.rows)
        {
            // Add row to output
            output += row.toString();
            // if NOT last row id, add comma to output
            if(row.id != lastID) output += ",";        
        }
        output += "}";
        return output;
    }

    static public Schedule parse(String input)
    {
        Schedule output = new Schedule();
        if(input.startsWith("{") && input.endsWith("}") && input.length() > 2)
        {
            // Remove {} from string
            input = input.substring(1, input.length() - 1);
            // Iterate through each Row
            for(String row : input.split("],"))
            {
                // Add row tail back
                if(!row.endsWith("]")) row += "]";
                // Parse schedule row
                ScheduleRow scheduleRow = ScheduleRow.parse(row);
                // if scheduleRow is not null, add scheduleRow to output.rows
                if(scheduleRow != null) output.rows.add(scheduleRow);
            }
        }
        return output;
    }
}
