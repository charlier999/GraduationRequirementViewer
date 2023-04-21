/**
 * 
 */
package com.gradview.model;

import java.text.ParseException;
import java.util.Arrays;

import com.gradview.data.dam.AccProgramDAM;
import com.gradview.data.dam.AccProgramElectivesCreditsDAM;
import com.gradview.data.dam.AccProgramGeneralEducationCreditsDAM;
import com.gradview.data.dam.AccProgramMajorCreditsDAM;
import com.gradview.data.dam.AccProgramTotalCreditsDAM;

/**
 * The representation of the academic program.
 * 
 * @author Charles Davis
 */
public class AccProgram
{
	public static final String LEVEL_GRAD_CERT = "Graduate Certificate of Completion";
	public static final String LEVEL_BACHELOR = "Bachelor";
	public static final String LEVEL_MINOR = "Minor";
	public static final String LEVEL_BRIDGE2MASTER = "Bridge to Master";
	public static final String LEVEL_MASTER = "Master";
	public static final String LEVEL_DOCTOR = "Doctor";
	public static final String BA_ART = "Bachelor of Arts";
	public static final String BA_SCI = "Bachelor of Science";

	/**
	 * The ID number of the program.
	 */
	private int id;
	/**
	 * The name of the program.
	 */
	private String name;
	/**
	 * The description of the program.
	 */
	private String description;
	/**
	 * The level of the program.
	 */
	private String level;
	/**
	 * The program is a bachelors of arts.
	 */
	private boolean baOfArts;
	/**
	 * The program is a bachelors of science.
	 */
	private boolean baOfScience;
	/**
	 * The minimum amount of general education credits required.
	 */
	private int genEdMinCredits;
	/**
	 * The maximum amount of general education credits required.
	 */
	private int genEdMaxCredits;
	/**
	 * The minimum amount of elective credits required.
	 */
	private int electiveMinCredits;
	/**
	 * The minimum amount of elective credits required.
	 */
	private int electiveMaxCredits;
	/**
	 * The minimum amount of major credits required.
	 */
	private int majorMinCredits;
	/**
	 * The minimum amount of major credits required.
	 */
	private int majorMaxCredits;
	/**
	 * The minimum amount of total credits required.
	 */
	private int totalMinCredits;
	/**
	 * The array of class ID's required for the program.
	 */
	private int[] requiredMajorClasses;

	/**
	 * @param id                   - The ID number of the program.
	 * @param name                 - The name of the program.
	 * @param description          - The description of the program.
	 * @param level                - The level of the program.
	 * @param baOfArts             - The program is a bachelors of arts.
	 * @param baOfScience          - The program is a bachelors of science.
	 * @param genEdMinCredits      - The minimum amount of general education credits
	 *                             required.
	 * @param genEdMaxCredits      - The maximum amount of general education credits
	 *                             required.
	 * @param electiveMinCredits   - The minimum amount of elective credits
	 *                             required.
	 * @param electiveMaxCredits   - The minimum amount of elective credits
	 *                             required.
	 * @param majorMinCredits      - The minimum amount of major credits required.
	 * @param majorMaxCredits      - he minimum amount of major credits required.
	 * @param totalMinCredits      - The minimum amount of total credits required.
	 * @param requiredMajorClasses - The array of class ID's required for the
	 *                             program.
	 */
	public AccProgram( int id, String name, String description, String level, boolean baOfArts, boolean baOfScience,
			int genEdMinCredits, int genEdMaxCredits, int electiveMinCredits, int electiveMaxCredits,
			int majorMinCredits, int majorMaxCredits, int totalMinCredits, int[] requiredMajorClasses )
	{
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.level = level;
		this.baOfArts = baOfArts;
		this.baOfScience = baOfScience;
		this.genEdMinCredits = genEdMinCredits;
		this.genEdMaxCredits = genEdMaxCredits;
		this.electiveMinCredits = electiveMinCredits;
		this.electiveMaxCredits = electiveMaxCredits;
		this.majorMinCredits = majorMinCredits;
		this.majorMaxCredits = majorMaxCredits;
		this.totalMinCredits = totalMinCredits;
		this.requiredMajorClasses = requiredMajorClasses;
	}

	/**
	 * @param name                 - The name of the program.
	 * @param description          - The description of the program.
	 * @param level                - The level of the program.
	 * @param baOfArts             - The program is a bachelors of arts.
	 * @param baOfScience          - The program is a bachelors of science.
	 * @param genEdMinCredits      - The minimum amount of general education credits
	 *                             required.
	 * @param genEdMaxCredits      - The maximum amount of general education credits
	 *                             required.
	 * @param electiveMinCredits   - The minimum amount of elective credits
	 *                             required.
	 * @param electiveMaxCredits   - The minimum amount of elective credits
	 *                             required.
	 * @param majorMinCredits      - The minimum amount of major credits required.
	 * @param majorMaxCredits      - he minimum amount of major credits required.
	 * @param totalMinCredits      - The minimum amount of total credits required.
	 * @param requiredMajorClasses - The array of class ID's required for the
	 *                             program.
	 */
	public AccProgram( String name, String description, String level, boolean baOfArts, boolean baOfScience,
			int genEdMinCredits, int genEdMaxCredits, int electiveMinCredits, int electiveMaxCredits,
			int majorMinCredits, int majorMaxCredits, int totalMinCredits, int[] requiredMajorClasses )
	{
		super();
		this.name = name;
		this.description = description;
		this.level = level;
		this.baOfArts = baOfArts;
		this.baOfScience = baOfScience;
		this.genEdMinCredits = genEdMinCredits;
		this.genEdMaxCredits = genEdMaxCredits;
		this.electiveMinCredits = electiveMinCredits;
		this.electiveMaxCredits = electiveMaxCredits;
		this.majorMinCredits = majorMinCredits;
		this.majorMaxCredits = majorMaxCredits;
		this.totalMinCredits = totalMinCredits;
		this.requiredMajorClasses = requiredMajorClasses;
	}

	public AccProgram(){}

	

	@Override
	public String toString()
	{
		return "AccProgram [id=" + id + ", name=" + name + ", description=" + description + ", level=" + level
				+ ", baOfArts=" + baOfArts + ", baOfScience=" + baOfScience + ", genEdMinCredits=" + genEdMinCredits
				+ ", genEdMaxCredits=" + genEdMaxCredits + ", electiveMinCredits=" + electiveMinCredits
				+ ", electiveMaxCredits=" + electiveMaxCredits + ", majorMinCredits=" + majorMinCredits
				+ ", majorMaxCredits=" + majorMaxCredits + ", totalMinCredits=" + totalMinCredits
				+ ", requiredMajorClasses=" + Arrays.toString( requiredMajorClasses ) + "]";
	}

	public static AccProgram parse(String input) throws ParseException {
		AccProgram output = new AccProgram();
	
		String[] fields = input.split(", ");
	
		if (fields.length != 14) 
		{
			throw new ParseException("Invalid input format", 0);
		}
	
		try 
		{
			output.id = Integer.parseInt(fields[0].substring(fields[0].indexOf('=') + 1));
			output.name = fields[1].substring(fields[1].indexOf('=') + 1);
			output.description = fields[2].substring(fields[2].indexOf('=') + 1);
			output.level = fields[3].substring(fields[3].indexOf('=') + 1);
			output.baOfArts = Boolean.parseBoolean(fields[4].substring(fields[4].indexOf('=') + 1));
			output.baOfScience = Boolean.parseBoolean(fields[5].substring(fields[5].indexOf('=') + 1));
			output.genEdMinCredits = Integer.parseInt(fields[6].substring(fields[6].indexOf('=') + 1));
			output.genEdMaxCredits = Integer.parseInt(fields[7].substring(fields[7].indexOf('=') + 1));
			output.electiveMinCredits = Integer.parseInt(fields[8].substring(fields[8].indexOf('=') + 1));
			output.electiveMaxCredits = Integer.parseInt(fields[9].substring(fields[9].indexOf('=') + 1));
			output.majorMinCredits = Integer.parseInt(fields[10].substring(fields[10].indexOf('=') + 1));
			output.majorMaxCredits = Integer.parseInt(fields[11].substring(fields[11].indexOf('=') + 1));
			output.totalMinCredits = Integer.parseInt(fields[12].substring(fields[12].indexOf('=') + 1));
	
			String[] classIds = fields[13].substring(fields[13].indexOf('=') + 1, fields[13].indexOf(']')).split(", ");
			output.requiredMajorClasses = new int[classIds.length];
			for (int i = 0; i < classIds.length; i++) 
			{
				output.requiredMajorClasses[i] = Integer.parseInt(classIds[i]);
			}
		} catch (NumberFormatException e) 
		{
			throw new ParseException("Invalid input format", 0);
		}
	
		return output;
	}
	


	/**
	 * Retruns the program as a {@link AccProgramDAM}
	 * @return {@link AccProgramDAM}
	 */
	public AccProgramDAM toProgramDAM()
	{
		return new AccProgramDAM(this.id,this. name, this.description, 
				this.level, this.baOfArts, this.baOfScience);
	}

	/**
	 * Returns the programs Gen Ed credit details as a {@link AccProgramGeneralEducationCreditsDAM}
	 * @return {@link AccProgramGeneralEducationCreditsDAM}
	 */
	public AccProgramGeneralEducationCreditsDAM toGenEdCredDAM()
	{
		return new AccProgramGeneralEducationCreditsDAM(this.id, this.genEdMinCredits, this.genEdMaxCredits);
	}

	/**
	 * Retuns the program's Elective credit details as a {@link AccProgramElectivesCreditsDAM}
	 * @return {@link AccProgramElectivesCreditsDAM}
	 */
	public AccProgramElectivesCreditsDAM toElectivCredeDAM()
	{
		return new AccProgramElectivesCreditsDAM(this.id,this.electiveMinCredits, this.electiveMaxCredits);
	}

	/**
	 * Returns the program's Major credit details as a {@link AccProgramMajorCreditsDAM}
	 * @return {@link AccProgramMajorCreditsDAM}
	 */
	public AccProgramMajorCreditsDAM toMajorCredDAM()
	{
		return new AccProgramMajorCreditsDAM(this.id, this.majorMinCredits);
	}

	/**
	 * Returns the program's Total credit details as a {@link AccProgramTotalCreditsDAM}
	 * @return {@link AccProgramTotalCreditsDAM}
	 */
	public AccProgramTotalCreditsDAM toTotalCredDAM()
	{
		return new AccProgramTotalCreditsDAM(this.id, this.totalMinCredits);
	}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	/**
	 * @return the level
	 */
	public String getLevel()
	{
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel( String level )
	{
		this.level = level;
	}

	/**
	 * @return the baOfArts
	 */
	public boolean isBaOfArts()
	{
		return baOfArts;
	}

	/**
	 * @param baOfArts the baOfArts to set
	 */
	public void setBaOfArts( boolean baOfArts )
	{
		this.baOfArts = baOfArts;
	}

	/**
	 * @return the baOfScience
	 */
	public boolean isBaOfScience()
	{
		return baOfScience;
	}

	/**
	 * @param baOfScience the baOfScience to set
	 */
	public void setBaOfScience( boolean baOfScience )
	{
		this.baOfScience = baOfScience;
	}

	/**
	 * @return the genEdMinCredits
	 */
	public int getGenEdMinCredits()
	{
		return genEdMinCredits;
	}

	/**
	 * @param genEdMinCredits the genEdMinCredits to set
	 */
	public void setGenEdMinCredits( int genEdMinCredits )
	{
		this.genEdMinCredits = genEdMinCredits;
	}

	/**
	 * @return the genEdMaxCredits
	 */
	public int getGenEdMaxCredits()
	{
		return genEdMaxCredits;
	}

	/**
	 * @param genEdMaxCredits the genEdMaxCredits to set
	 */
	public void setGenEdMaxCredits( int genEdMaxCredits )
	{
		this.genEdMaxCredits = genEdMaxCredits;
	}

	/**
	 * @return the electiveMinCredits
	 */
	public int getElectiveMinCredits()
	{
		return electiveMinCredits;
	}

	/**
	 * @param electiveMinCredits the electiveMinCredits to set
	 */
	public void setElectiveMinCredits( int electiveMinCredits )
	{
		this.electiveMinCredits = electiveMinCredits;
	}

	/**
	 * @return the electiveMaxCredits
	 */
	public int getElectiveMaxCredits()
	{
		return electiveMaxCredits;
	}

	/**
	 * @param electiveMaxCredits the electiveMaxCredits to set
	 */
	public void setElectiveMaxCredits( int electiveMaxCredits )
	{
		this.electiveMaxCredits = electiveMaxCredits;
	}

	/**
	 * @return the majorMinCredits
	 */
	public int getMajorMinCredits()
	{
		return majorMinCredits;
	}

	/**
	 * @param majorMinCredits the majorMinCredits to set
	 */
	public void setMajorMinCredits( int majorMinCredits )
	{
		this.majorMinCredits = majorMinCredits;
	}

	/**
	 * @return the majorMaxCredits
	 */
	public int getMajorMaxCredits()
	{
		return majorMaxCredits;
	}

	/**
	 * @param majorMaxCredits the majorMaxCredits to set
	 */
	public void setMajorMaxCredits( int majorMaxCredits )
	{
		this.majorMaxCredits = majorMaxCredits;
	}

	/**
	 * @return the totalMinCredits
	 */
	public int getTotalMinCredits()
	{
		return totalMinCredits;
	}

	/**
	 * @param totalMinCredits the totalMinCredits to set
	 */
	public void setTotalMinCredits( int totalMinCredits )
	{
		this.totalMinCredits = totalMinCredits;
	}

	/**
	 * @return the requiredMajorClasses
	 */
	public int[] getRequiredMajorClasses()
	{
		return requiredMajorClasses;
	}

	/**
	 * @param requiredMajorClasses the requiredMajorClasses to set
	 */
	public void setRequiredMajorClasses( int[] requiredMajorClasses )
	{
		this.requiredMajorClasses = requiredMajorClasses;
	}

}
