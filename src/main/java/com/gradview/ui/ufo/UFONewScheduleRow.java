package com.gradview.ui.ufo;

import com.gradview.model.Schedule;
import com.gradview.model.ScheduleRow;

public class UFONewScheduleRow 
{
    public Schedule schedule;
    public ScheduleRow newRow;
    public UFONewScheduleRow( Schedule schedule, ScheduleRow newRow )
    {
        super();
        this.schedule = schedule;
        this.newRow = newRow;
    }
    public UFONewScheduleRow( Schedule schedule )
    {
        super();
        this.schedule = schedule;
    }
    public UFONewScheduleRow()
    {
        super();
    }
    public Schedule getSchedule()
    {
        return schedule;
    }
    public void setSchedule( Schedule schedule )
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
