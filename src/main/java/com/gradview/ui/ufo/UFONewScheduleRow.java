package com.gradview.ui.ufo;

import com.gradview.model.ScheduleRow;

public class UFONewScheduleRow 
{
    public String schedule;
    public ScheduleRow newRow;
    public UFONewScheduleRow( String schedule, ScheduleRow newRow )
    {
        super();
        this.schedule = schedule;
        this.newRow = newRow;
    }
    public UFONewScheduleRow( String schedule )
    {
        super();
        this.schedule = schedule;
    }
    public UFONewScheduleRow()
    {
        super();
    }
    public String getSchedule()
    {
        return schedule;
    }
    public void setSchedule( String schedule )
    {
        this.schedule = schedule;
    }
    public ScheduleRow getNewRow()
    {
        return newRow;
    }
    public void setNewRow( ScheduleRow newRow )
    {
        this.newRow = newRow;
    }


}
