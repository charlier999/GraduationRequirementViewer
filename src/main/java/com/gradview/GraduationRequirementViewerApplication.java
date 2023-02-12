package com.gradview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.gradview"})
public class GraduationRequirementViewerApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run( GraduationRequirementViewerApplication.class, args );
		
	}

	


	// @Bean
	// public CourseImportService courseImportService()
	// {
	// 	return new CourseImportService();
	// }
	
	// @Bean
	// public AccClassDAO accClassDAO()
	// {
	// 	return new AccClassDAO();
	// }

	// @Bean
	// public JdbcTemplate jdbcTemplate()
	// {
	// 	return new JdbcTemplate();
	// }
}
