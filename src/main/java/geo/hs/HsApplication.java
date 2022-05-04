package geo.hs;

import geo.hs.service.HazardService;
import geo.hs.service.HillShadeService;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.sql.SQLException;

@SpringBootApplication
public class HsApplication {

	private static HillShadeService hsService = new HillShadeService();
	private static HazardService hazardService = new HazardService();

	public static void main(String[] args) throws IOException, ParseException, SQLException {

		//hazard test
		//hazardService.run();

		//hillshade test
		//System.out.println("--start--");
		//hsService.run();
		SpringApplication.run(HsApplication.class, args);
	}

}
