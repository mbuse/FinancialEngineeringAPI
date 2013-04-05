package de.mbuse.finance.optimization;

import de.mbuse.finance.util.ArrayUtil;
import de.mbuse.finance.util.Out;
import java.text.DecimalFormat;
import java.text.Format;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;

/**
 *
 * NOT THREAD SAFE!
 * 
 * @author mbuse
 */
public class Optimizer {
  
  public boolean maximize() {
    return optimize(GoalType.MAXIMIZE);
  }
  
  public boolean minimize() {
    return optimize(GoalType.MINIMIZE);
  }
  
  protected boolean optimize(GoalType goalType) {
    optimizedParameters = getInitialParameters().clone();
    if (isOutputEnabled()) {
      System.out.println("======================================================");
      System.out.println("Starting Optimization");
      System.out.println("------------------------------------------------------");
      System.out.println("INITIAL  : parameters: [" + toString(getParameterFormat(), optimizedParameters)
              + "]\t objective: " + getResultFormat().format(getObjectiveFunction().value(optimizedParameters)));     
      System.out.println("------------------------------------------------------");
    }
    double relT = powellOptimizerConfig.getRelativeThreshold();
    double absT = powellOptimizerConfig.getAbsoluteThreshold();
    PowellOptimizer solver = new PowellOptimizer(relT, absT);
    
    PointValuePair result = null;
    double maxDelta = -1;
    for (int runs=1; runs<getMaxNumberOfRuns(); runs++) {
      result = solver.optimize(powellOptimizerConfig.getMaxOptimizationCnt(), getObjectiveFunction(), goalType, optimizedParameters);   
      maxDelta = ArrayUtil.maximumAbsoluteDifference(result.getPointRef(), optimizedParameters);
      optimizedParameters = result.getPoint();
      if (isOutputEnabled()) {
        System.out.println(RUN_INDEX_FORMAT.format(runs) + ". run : parameters: [" + toString(getParameterFormat(), result.getPointRef())
                + "]\t objective: " + getResultFormat().format(result.getValue())
                + "\t delta: " + getResultFormat().format(maxDelta));
      }
      if (getMaxAbsoluteDifference() > maxDelta) {
        if (isOutputEnabled()) {
          System.out.println("------------------------------------------------------");
          System.out.println("Optimization finished successfully");
          System.out.println("    objective : " + getResultFormat().format(result.getValue()));
          System.out.println("    delta     : "  + getResultFormat().format(maxDelta));
          System.out.println("======================================================");
        }
        return true;
      }
      
      solver = new PowellOptimizer(relT, result.getValue() / 10);
    }
    if (isOutputEnabled()) {
      System.out.println("------------------------------------------------------");
      System.out.println("Optimization finished uncomplete");
      System.out.println("    objective : " + getResultFormat().format(result.getValue()));
      System.out.println("    delta     : "  + getResultFormat().format(maxDelta));
      System.out.println("======================================================");
    }
    return false;
  }
  
  private String toString(Format f, double... values) {
    StringBuilder b = new StringBuilder();
    for (int i=0; i<values.length;i++) {
      b.append(f.format(values[i]));
      if (i!=values.length-1) {
        b.append(", ");
      }
    }
    return b.toString();
  }
  
  // Getters and Setters
  
  public MultivariateFunction getObjectiveFunction() {
    return objectiveFunction;
  }

  public void setObjectiveFunction(MultivariateFunction objectiveFunction) {
    this.objectiveFunction = objectiveFunction;
  }

  public double[] getInitialParameters() {
    return initialParameters;
  }

  public void setInitialParameters(double[] initialParameters) {
    this.initialParameters = initialParameters;
  }

  public double[] getOptimizedParameters() {
    return optimizedParameters;
  }

  public double getMaxAbsoluteDifference() {
    return maxAbsoluteDifference;
  }

  public void setMaxAbsoluteDifference(double maximalAbsoluteDifference) {
    this.maxAbsoluteDifference = maximalAbsoluteDifference;
  }

  public int getMaxNumberOfRuns() {
    return maxNumberOfRuns;
  }

  public void setMaxNumberOfRuns(int maximalNumberOfRuns) {
    this.maxNumberOfRuns = maximalNumberOfRuns;
  }

  public boolean isOutputEnabled() {
    return outputEnabled;
  }

  public void setOutputEnabled(boolean outputEnabled) {
    this.outputEnabled = outputEnabled;
  }

  public Format getParameterFormat() {
    return parameterFormat;
  }

  public void setParameterFormat(Format parameterFormat) {
    this.parameterFormat = parameterFormat;
  }

  public Format getResultFormat() {
    return resultFormat;
  }

  public void setResultFormat(Format resultFormat) {
    this.resultFormat = resultFormat;
  }
  
  // Powell algorithm parameters
  private PowellOptimizerConfig powellOptimizerConfig = new PowellOptimizerConfig();
  
  // the maximal allowed difference between two successive runs
  private double maxAbsoluteDifference  = 0.00001;
  private int    maxNumberOfRuns        = 10;
  
  // output
  private boolean outputEnabled = true;
  private Format parameterFormat = DEFAULT_PARAMETER_FORMAT;
  private Format resultFormat    = DEFAULT_RESULT_FORMAT;
  
  // data
  private MultivariateFunction objectiveFunction;
  private double[] initialParameters;
  private double[] optimizedParameters;
  
  private static final Format DEFAULT_PARAMETER_FORMAT = new DecimalFormat("0.00000");
  private static final Format DEFAULT_RESULT_FORMAT = new DecimalFormat("0.00000E00");
  private static final Format RUN_INDEX_FORMAT = new DecimalFormat("000");
  
  public static class PowellOptimizerConfig {
    private int maxOptimizationCnt = 5000;
    private double relativeThreshold = 0.001;
    private double absoluteThreshold = 0.001;

    public void setAbsoluteThreshold(double absoluteThreshold) {
      this.absoluteThreshold = absoluteThreshold;
    }

    public void setMaxOptimizationCnt(int maxOptimizationCnt) {
      this.maxOptimizationCnt = maxOptimizationCnt;
    }

    public void setRelativeThreshold(double relativeThreshold) {
      this.relativeThreshold = relativeThreshold;
    }

    public int getMaxOptimizationCnt() {
      return this.maxOptimizationCnt;
    }

    public double getAbsoluteThreshold() {
      return this.absoluteThreshold;
    }

    public double getRelativeThreshold() {
      return this.relativeThreshold;
    }
    
  }
  
}
