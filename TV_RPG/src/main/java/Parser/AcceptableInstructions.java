package Parser;

import UnitedClasses.R_PATH;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

class AcceptableInstructions {
    private Set<String> instructionsPrototypes;

    AcceptableInstructions() throws IOException {
        this.instructionsPrototypes = new TreeSet<>();
        Properties properties = new Properties();

        File f = new File(R_PATH.res_path + "/acceptableInstructions.properties");
        InputStream inputStream = new FileInputStream(f);

        //properties.load(AcceptableInstructions.class.getResourceAsStream("../resources/acceptableInstructions.properties"));
        properties.load(inputStream);

        int instructionsCount = Integer.parseInt(properties.getProperty("instructionsCount"));
        for(int i = 0; i < instructionsCount; ++i) {
            this.instructionsPrototypes.add(properties.getProperty(String.valueOf(i)));
        }
    }

    boolean isCorrect(String instruction) {
        return this.instructionsPrototypes.contains(instruction);
    }
}
