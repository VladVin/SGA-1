package command_line_parser;

import java.util.ArrayList;

/**
 * Created by VladVin on 16.09.2016.
 */
public class CommandLineParser {
    public enum LaunchType { TRAIN, TEST, NONE }

    private enum TrainArgType { POS_DIRS, NEG_DIRS, NONE }
    private enum PredictArgType { DOCS, NONE }

    private LaunchType launchType = LaunchType.NONE;

    private ArrayList<String> posDirs;
    private ArrayList<String> negDirs;

    private ArrayList<String> predictDocs;

    public void parse(String[] args) {
        if (args == null || args.length < 2) {
            return;
        }

        switch (args[0]) {
            case "train":
                parseTrainArgs(args);
                launchType = LaunchType.TRAIN;
                break;
            case "test":
                parsePredictArgs(args);
                launchType = LaunchType.TEST;
                break;
        }
    }

    public ArrayList<String> getPosDirs() {
        return posDirs;
    }

    public ArrayList<String> getNegDirs() {
        return negDirs;
    }

    public ArrayList<String> getPredictDocs() {
        return predictDocs;
    }

    public LaunchType getLaunchType() {
        return launchType;
    }

    private void parseTrainArgs(String[] args) {
        TrainArgType argType = TrainArgType.NONE;

        posDirs = new ArrayList<>();
        negDirs = new ArrayList<>();

        for (String arg : args) {
            switch (arg) {
                case "--pos":
                    argType = TrainArgType.POS_DIRS;
                    break;
                case "--neg":
                    argType = TrainArgType.NEG_DIRS;
                    break;
                default:
                    switch (argType) {
                        case POS_DIRS:
                            posDirs.add(arg);
                            break;
                        case NEG_DIRS:
                            negDirs.add(arg);
                    }
                    break;
            }
        }
    }

    private void parsePredictArgs(String[] args) {
        PredictArgType argType = PredictArgType.NONE;

        predictDocs = new ArrayList<>();

        for (String arg : args) {
            switch (arg) {
                case "--docs":
                    argType = PredictArgType.DOCS;
                    break;
                default:
                    switch (argType) {
                        case DOCS:
                            predictDocs.add(arg);
                            break;
                    }
                    break;
            }
        }
    }
}
