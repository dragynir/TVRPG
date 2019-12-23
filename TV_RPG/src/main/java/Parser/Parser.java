package Parser;

import Instructions.Instruction;
import Instructions.InstructionList;
import UnitedClasses.R_PATH;
import UnitedClasses.TemporaryCodeStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private AcceptableInstructions acceptableInstructions;
    private InstructionList instructionList;
    private TemporaryCodeStorage temporaryCodeStorage;
    private Map<String, Integer> instructionIds;


    public Parser(TemporaryCodeStorage temporaryCodeStorage, InstructionList instructionList) throws IOException {
        this.temporaryCodeStorage = temporaryCodeStorage;
        this.acceptableInstructions = new AcceptableInstructions();
        this.instructionList = instructionList;
        instructionIds = new TreeMap<>();
        Properties properties = new Properties();


        File f = new File(R_PATH.res_path + "/acceptableInstructions.properties");
        InputStream inputStream = new FileInputStream(f);

        //properties.load(AcceptableInstructions.class.getResourceAsStream("../resources/acceptableInstructions.properties"));
        properties.load(inputStream);


        //properties.load(AcceptableInstructions.class.getResourceAsStream("../resources/acceptableInstructions.properties"));
        int instructionsCount = Integer.parseInt(properties.getProperty("instructionsCount"));
        for(int i = 0; i < instructionsCount; ++i) {
            this.instructionIds.put(properties.getProperty(String.valueOf(i)), i);
        }
    }

    public boolean parse() {
        String inputCode = removeAllWC(temporaryCodeStorage.getCode());
        List<Instruction> parsed = parseBody(inputCode);
        if(null == parsed) {
            return false;
        }
        this.instructionList.instructions = parsed;
        return true;
    }

    public void printInstructions() {
        printInstructionList(this.instructionList.instructions, 0);
    }

    private void printInstructionList(List<Instruction> instructions, int shifts) {
        for(Instruction instruction : instructions) {
            for(int i = 0; i < shifts; ++i) {
                System.out.print("\t");
            }
            System.out.print(instruction.instructionId + "(");
            for(int i = 0; i < instruction.arguments.size(); ++i) {
                System.out.print(instruction.arguments.get(i) + ",");
            }
            System.out.print("\b)");
            if(null == instruction.body) {
                System.out.println(";");
            } else {
                System.out.println(" {");
                printInstructionList(instruction.body, shifts + 1);
                for(int i = 0; i < shifts; ++i) {
                    System.out.print("\t");
                }
                System.out.println("}");
            }
        }
    }

    private List<Instruction> parseBody(String body) {
        List<Instruction> out = new ArrayList<>();
        List<Integer> arguments = new ArrayList<>();

        Pattern instructionPattern = Pattern.compile("[\\w&&[^\\d]]\\w*\\(");
        Pattern argumentPattern = Pattern.compile("\\d*\\d");
        Pattern bracketClosedPattern = Pattern.compile("\\)");
        Pattern bodyOpenedPattern = Pattern.compile("\\{");
        Pattern bodyClosedPattern = Pattern.compile("}");
        Pattern bodyPattern = Pattern.compile("\\{.*}");
        Pattern semicolonPattern = Pattern.compile(";");
        Pattern argSeparatorPattern = Pattern.compile(",");
        Matcher bodyOpenedMatcher;
        Matcher bodyMatcher = bodyPattern.matcher(body);
        if(!bodyMatcher.matches()) {
            return null;
        }
        String processedCode = body.substring(1, body.length() - 1);

        while (true) {
            // if code is over - return result
            if(0 == processedCode.length()) {
                return out;
            } else {
                // if body did not close - find next instruction
                Matcher instructionMatcher = instructionPattern.matcher(processedCode);
                if(!instructionMatcher.find() || 0 != instructionMatcher.start()) {
                    Matcher semicolonMatcher = semicolonPattern.matcher(processedCode);
                    if(!(semicolonMatcher.find() && 0 == semicolonMatcher.start())) {
                        return null;
                    }
                    continue;
                }

                Instruction newInstruction = new Instruction();
                StringBuilder currentInstruction = new StringBuilder(processedCode.substring(instructionMatcher.start(), instructionMatcher.end()));
                processedCode = processedCode.substring(instructionMatcher.end());
                Matcher argumentMatcher = argumentPattern.matcher(processedCode);
                // fill arguments of the instruction
                while(argumentMatcher.find() && 0 == argumentMatcher.start()) {
                    currentInstruction.append("int");
                    arguments.add(Integer.parseInt(processedCode.substring(argumentMatcher.start(), argumentMatcher.end())));
                    processedCode = processedCode.substring(argumentMatcher.end());
                    Matcher argSeparatorMatcher = argSeparatorPattern.matcher(processedCode);
                    if(argSeparatorMatcher.find() && 0 == argSeparatorMatcher.start()) {
                        processedCode = processedCode.substring(argSeparatorMatcher.end());
                        currentInstruction.append(',');
                        argumentMatcher = argumentPattern.matcher(processedCode);
                    } else {
                        Matcher bracketClosedMatcher = bracketClosedPattern.matcher(processedCode);
                        if(bracketClosedMatcher.find() && 0 == bracketClosedMatcher.start()) {
                            processedCode = processedCode.substring(bracketClosedMatcher.end());
                            currentInstruction.append(')');
                            break;
                        } else {
                            return null;
                        }
                    }
                }
                // fill body of the instruction
                List<Instruction> instructionBody = null;
                bodyOpenedMatcher = bodyPattern.matcher(processedCode);
                if(bodyOpenedMatcher.find() && 0 == bodyOpenedMatcher.start()) {
                    Matcher bodyClosedMatcher = bodyClosedPattern.matcher(processedCode);
                    int closedPosition = findBodyEnd(bodyClosedMatcher, bodyOpenedPattern, processedCode);
                    if(-1 == closedPosition) {
                        return null;
                    }

                    String subbody = processedCode.substring(bodyOpenedMatcher.start(), closedPosition + 1);
                    instructionBody = parseBody(subbody);
                    if(null == instructionBody) {
                        return null;
                    }
                    processedCode = processedCode.substring(closedPosition + 1);
                    currentInstruction.append("{}");
                } else {
                    // check instruction ends by ';'
                    Matcher semicolonMatcher = semicolonPattern.matcher(processedCode);
                    if(!(semicolonMatcher.find() && 0 == semicolonMatcher.start())) {
                        return null;
                    }
                    processedCode = processedCode.substring(semicolonMatcher.end());
                }
                // check instruction is correct
                if(!acceptableInstructions.isCorrect(currentInstruction.toString())) {
                    return null;
                }
                // build new instruction and add to out
                newInstruction.instructionId = instructionIds.get(currentInstruction.toString());
                newInstruction.arguments = arguments;
                arguments = new ArrayList<>();
                newInstruction.body = instructionBody;
                out.add(newInstruction);
            }
        }
    }
    private int findBodyEnd(Matcher bodyClosedMatcher, Pattern bodyOpenedPattern, String processedCode) {
        Matcher nextBodyOpenedMatcher = bodyOpenedPattern.matcher(processedCode);
        if(!nextBodyOpenedMatcher.find()) {
            return -1;
        }
        while(bodyClosedMatcher.find()) {
            if (nextBodyOpenedMatcher.find()) {
                if (bodyClosedMatcher.start() < nextBodyOpenedMatcher.start()) {
                    return bodyClosedMatcher.start();
                }
            } else {
                return bodyClosedMatcher.start();
            }
        }
        return -1;
    }

    private String removeAllWC(String input) {
        return input.replaceAll("[[\\s\\n\\t]+[\\r&&\\n]]+","");
    }

}