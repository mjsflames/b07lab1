import java.io.File;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.FileNotFoundException;

public class Polynomial 
{
        double[] coefficients;
        int[] exponents;

        public Polynomial() 
        {
                this.coefficients = null;
                this.exponents = null;
        }

        public Polynomial(double[] coefficients, int[] exponents) 
        {
                this.coefficients = coefficients;
                this.exponents = exponents;
        }
        
        public Polynomial add(Polynomial other) 
        {
                int length = other.coefficients.length;
                int explength = other.exponents.length;

                if(this.coefficients.length > length) 
                {
                        length = this.coefficients.length;
                }
                
                if(this.exponents.length > explength)
                {
                	explength = this.exponents.length;
                }
           
                double[] newPoly = new double[length + this.coefficients.length];
                int[] newExp = new int[explength + this.exponents.length];

                for(int i = 0; i < this.coefficients.length; i++) 
                {
                        newPoly[i] = this.coefficients[i];
                }

                for(int i = this.coefficients.length; i < (this.coefficients.length + other.coefficients.length); i++) 
                {
                        newPoly[i] = other.coefficients[i - this.coefficients.length];
                }
		
		for(int i = 0; i < this.exponents.length; i++)
		{
			newExp[i] = this.exponents[i];
		}
		for(int i = this.exponents.length; i < (this.exponents.length + other.coefficients.length); i++)
		{
			newExp[i] = other.exponents[i - this.coefficients.length];
		}

                return new Polynomial(newPoly, newExp);
        }

        public double evaluate(double x) 
        {
                double returnVAl = 0;
                double scalingX = 1;
                for(double item: this.coefficients) 
                {
                        returnVAl += scalingX * item;
                        scalingX = scalingX * x;
                }
                /* 
                double returnVAl2 = 0;
                double scalingX2 = 1;
                for(double item2: this.exponents)
                {
                	returnVAl2 += scalingX2 * item;
                	scalingX2 = scalingX2 * x;
                }
                */
                return returnVAl;
        }

        public boolean hasRoot(double x) 
        {
                if(evaluate(x) == 0)
                {
                        return true;
                }
                return false;
        }

	public Polynomial multiply(Polynomial other)
	{
		if(this.coefficients.length != other.coefficients.length || this.exponents.length != other.exponents.length || this == null || other == null)
		{
			return null;
		}

		int length1 = this.coefficients.length;
		int length2 = other.coefficients.length;

		double[] coefficientSet = new double[length1 + length2 * 2];
		int[] exponentSet = new int[length1 + length2 * 2];

		int biggestExp = 0;

		// 2x^2 + 3x + 4, 5x^2 + 6x + 7
		// Solution: 2x^2(5x^2 + 6x + 7) + 3x(5x^2 + 6x + 7) + 4(5x^2 + 6x + 7)
		// 10x^4 + 12x^3 + 14x^2 + 15x^3 + 18x^2 + 21x + 20x^2 + 24x + 28
		
		// Calculations
		for(int i = 0; i < this.coefficients.length; i++)
		{
			for(int j = 0; j < other.coefficients.length; j++)
			{
				for(int k = 0; k < coefficientSet.length; k++)
				{
					if(coefficientSet[k] == 0)
					{
						coefficientSet[k] = this.coefficients[i] * other.coefficients[j];
						exponentSet[k] = this.exponents[i] + other.exponents[j];
						if(exponentSet[k] > biggestExp)
						{
							biggestExp = exponentSet[k];
						}
						break;
					}			
				}
			}
		}

		// Simplify
		for(int i = 0; i < coefficientSet.length; i++)
		{
			for(int j = 0; j < coefficientSet.length; j++)
			{
				if(exponentSet[i] == exponentSet[j] && i != j && coefficientSet[i] != 0)
				{
					coefficientSet[i] = coefficientSet[i] + coefficientSet[j];
					coefficientSet[j] = 0;
					exponentSet[j] = 0;
				}
			}
		}

		// Create Polynomial
		double[] productCoefficients = new double[biggestExp + 1];
		int[] productExponents = new int[biggestExp + 1];

		for(int i = 0; i < coefficientSet.length; i++)
		{
			if(coefficientSet[i] != 0)
			{
				for(int j = 0; j < productCoefficients.length; j++)
				{
					if(productCoefficients[j] == 0)
					{
						productCoefficients[j] = coefficientSet[i];
						productExponents[j] = exponentSet[i];
						System.out.println(productCoefficients[j]);
						System.out.println(productExponents[j]);
						break;
					}
				}
			}
		}

		return new Polynomial(productCoefficients, productExponents);
	}

        public Polynomial(File file) throws Exception
        {
        	Scanner myScanner = new Scanner(file);
        	if(!myScanner.hasNextLine())
        	{
        		this.coefficients = null;
        		this.exponents = null;
        	}
        	else
        	{
        		String line = myScanner.nextLine();
        		line = line.replace("-", "+-");
        		
        		String[] poly_arr = line.split("+");
        		this.exponents = new int[poly_arr.length];
        		this.coefficients = new double[poly_arr.length];
        		
        		for(int i = 0; i < poly_arr.length; i++) 
        		{
        			String[] subArray = poly_arr[i].split("x");
        			coefficients[i] = Double.parseDouble(subArray[0]);
        			
        			if(subArray.length > 1)
        			{
        				exponents[i] = Integer.parseInt(subArray[1]);
        			}
        			else
        			{
        				exponents[i] = 0;
        			}
        		}
        	}
        	myScanner.close();
        }

        public void saveToFile(String myFile) throws Exception
        {
        	if(coefficients == null || exponents == null || this.coefficients.length != this.exponents.length)
        	{
        		return;
        	}
        	String writeString = "";
        	
        	for(int i = 0; i < this.coefficients.length; i++)
        	{
        		writeString += coefficients[i];
        		if(exponents[i] != 0)
        		{
        			writeString += "x" + exponents[i];
        		}
        		writeString += "+";
        	}
        	if(writeString.endsWith("+"))
        	{
        		writeString = writeString.substring(0, writeString.length() - 1);
        	}
        	
        	FileWriter myWriter = new FileWriter(new File(myFile));
        	myWriter.write(writeString);
        	myWriter.close();
        }
}
