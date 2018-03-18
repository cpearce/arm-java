package nz.org.pearce.arm;

import java.io.File;

public class Arguments
{

  public String inputPath;
  public String outputPath;
  public double minimumSupport = 0.0;
  public double minimumConfidence = 0.0;
  public double minimumLift;

  public static Arguments parseOrDie(String[] args)
  {
    String inputPath = "";
    boolean haveInputPath = false;
    String outputPath = "";
    boolean haveOutputPath = false;
    double minimumSupport = 0.0;
    boolean haveMinimumSupport = false;
    double minimumConfidence = 0.0;
    boolean haveMinimumConfidence = false;
    double minimumLift = 0.0;
    boolean haveMinimumLift = false;
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--input")) {
        if (i + 1 == args.length || !(new File(args[i + 1])).exists()) {
          System.err.println("You must supply a valid path with '--input'");
          System.exit(-1);
        }
        inputPath = args[i + 1];
        i++;
        haveInputPath = true;
      } else if (args[i].equals("--output")) {
        if (i + 1 == args.length) {
          System.err.println("You must supply a valid path with '--output'");
          System.exit(-1);
        }
        outputPath = args[i + 1];
        i++;
        haveOutputPath = true;
      } else if (args[i].equals("--min-support")) {
        String errorMessage =
          "You must supply a float value in range [0,1] with '--min-support'";
        minimumSupport = parseDoubleOrDie(i + 1, args, errorMessage, 0.0, 1.0);
        i++;
        haveMinimumSupport = true;
      } else if (args[i].equals("--min-confidence")) {
        String errorMessage =
          "You must supply a float value in range [0,1] with '--min-confidence'";
        minimumConfidence =
          parseDoubleOrDie(i + 1, args, errorMessage, 0.0, 1.0);
        i++;
        haveMinimumConfidence = true;
      } else if (args[i].equals("--min-lift")) {
        String errorMessage =
          "You must supply a float value in range [1,+Inf] with '--min-lift'";
        minimumLift =
          parseDoubleOrDie(i + 1, args, errorMessage, 1.0, Double.MAX_VALUE);
        haveMinimumLift = true;
        i++;
      } else {
        System.err.println("Unknown argument: " + args[i]);
        System.exit(-1);
      }
    }
    int errors = 0;
    if (!haveInputPath) {
      System.err.println(
        "You must supply an input path with --input-path $path");
      errors++;
    }
    if (!haveOutputPath) {
      System.err.println(
        "You must supply an output path with --output-path $path");
      errors++;
    }
    if (!haveMinimumSupport) {
      System.err.println(
        "You must supply a minimum support in range [0,1] with --min-support $value");
      errors++;
    }
    if (!haveMinimumConfidence) {
      System.err.println(
        "You must supply a minimum confidence in range [0,1] with --min-confidence $value");
      errors++;
    }
    if (!haveMinimumLift) {
      System.err.println(
        "You must supply a minimum lift in range [1,+Inf] with --min-lift $value");
      errors++;
    }
    if (errors > 0) {
      System.exit(-1);
    }
    return new Arguments(
      inputPath, outputPath, minimumSupport, minimumConfidence, minimumLift);
  }

  private Arguments(String inputPath,
                    String outputPath,
                    double minimumSupport,
                    double minimumConfidence,
                    double minimumLift)
  {
    this.inputPath = inputPath;
    this.outputPath = outputPath;
    this.minimumSupport = minimumSupport;
    this.minimumConfidence = minimumConfidence;
    this.minimumLift = minimumLift;
  }

  private static double parseDoubleOrDie(int index,
                                         String[] args,
                                         String errorMessage,
                                         double minimum,
                                         double maximum)
  {
    if (index == args.length) {
      System.err.println(errorMessage);
      System.exit(-1);
    }
    double value = 0.0;
    try {
      value = Double.parseDouble(args[index]);
    } catch (NumberFormatException e) {
      System.err.println(errorMessage);
      System.exit(-1);
    }
    if (value < minimum || value > maximum) {
      System.err.println(errorMessage);
      System.exit(-1);
    }
    return value;
  }
}
