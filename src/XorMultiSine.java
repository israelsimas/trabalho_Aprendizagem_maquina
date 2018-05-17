import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

public class XorMultiSine {

	public double 			ERROR_MAX 					= 0.000001;
	public static int 	LEN_ARRAY_TRAINING = 13;
	public static int 	LEN_ARRAY_TEST 		= 72;
	public static int 	NUM_TRAINING 			= 1;

	public XorMultiSine() {

	}

	public void generateNeural() {

		// create training set
		DataSet trainingSet = new DataSet(1, 1);
		double[] anglesn = getVectorAnglesN();
		double[] sines = getSines(getVectorAngles());

		for (int x = 0; x < (LEN_ARRAY_TRAINING - 1); x++) {
			trainingSet.addRow(new DataSetRow(new double[] { anglesn[x] }, new double[] { sines[x] }));
		}

		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 1, 6, 10, 1);

		// learn the training set
		BackPropagation learningrules = new BackPropagation();
		learningrules.setMaxError(ERROR_MAX);

		System.out.println("Training neural network ....");
		myMlPerceptron.learn(trainingSet, learningrules);

		// save trained neural network
		System.out.println("Saving neural network ....");
		myMlPerceptron.save("myMlPerceptron.nnet");
		
		System.out.println("Neural network generated");
	}
	
	public void testNeural() {
		
		// create training set
		DataSet trainingSet = new DataSet(1, 1);
		double[] anglesn 	= getVectorAnglesTestN();
		double[] sines 		= getSines(getVectorAnglesTest());

		for (int x = 0; x < (LEN_ARRAY_TEST - 1); x++) {
			trainingSet.addRow(new DataSetRow(new double[] { anglesn[x] }, new double[] { sines[x] }));
		}		
		
		// load saved neural network
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.load("myMlPerceptron.nnet");
		
		// test loaded neural network
		System.out.println("Testing loaded neural network");
		
		try {
			
			testNeuralNetwork(loadedMlPerceptron, trainingSet);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) throws Exception {
		
	    PrintWriter pw = new PrintWriter(new File("anglesTest.csv"));
	    StringBuilder sb = new StringBuilder();   
			
			double[] output = new double[LEN_ARRAY_TEST];
			int x = 0;

			for (DataSetRow dataRow : testSet.getRows()) {

				nnet.setInput(dataRow.getInput());
				nnet.calculate();

				double[] networkOutput = nnet.getOutput();

				output[x] = networkOutput[0];
				x++;
			}

			double[] sines = getSines(getVectorAnglesTest());
			double[] angles = getVectorAnglesTest();
			double errorCalc = 0;
			
			for (int z = 0; z < LEN_ARRAY_TEST; z++) {
				
				errorCalc += Math.abs((output[z] - sines[z]));
				
				sb.append(String.valueOf(angles[z]));
				sb.append(",");				
				sb.append(String.valueOf(sines[z]));
				sb.append(",");
				sb.append(String.valueOf(angles[z]));
				sb.append(",");		
				sb.append(String.valueOf(output[z]));
				sb.append("\n");
			}

			System.out.println("\n\n");
			System.out.println("Erro Total: " + errorCalc);		
			
	    pw.write(sb.toString());
	    pw.close();
	}

	private double[] getVectorAnglesTest() {

		int start = 0;
		int end = 360;
		int i = 5;
		
		double qtd = end/i;
		double[] angles = new double[(int)(qtd)+1];
		
		for (int x = start; x <= qtd; x++) {
			angles[x] = x * i;
		}

		return angles;
	}
	
	private double[] getVectorAnglesTestN() {
		double[] angles = getVectorAnglesTest();

		// Normaliza os valores dos Angulos
		double[] anglesn = new double[angles.length];
		for (int x = 0; x < angles.length; x++) {
			anglesn[x] = angles[x] / 360;
		}

		return anglesn;
	}
	
	private double[] getVectorAngles() {

		double qtd = LEN_ARRAY_TRAINING;
		double[] angles = new double[(int) (qtd) + 1];

		// Angulos inferiores (0 - 45)
		angles[0]  = 0;
		angles[1]  = 30;
		angles[2]  = 60;
		angles[3]  = 90;
		angles[4]  = 120;
		angles[5]  = 150;
		angles[6]  = 180;
		angles[7]  = 210;
		angles[8]  = 240;
		angles[9]  = 270;
		angles[10] = 300;
		angles[11] = 330;
		angles[12] = 360;

		return angles;
	}

	private double[] getVectorAnglesN() {
		double[] angles = getVectorAngles();

		// Normaliza os valores dos Angulos
		double[] anglesn = new double[angles.length];
		for (int x = 0; x < angles.length; x++) {
			anglesn[x] = angles[x] / 360;
		}

		return anglesn;
	}

	private static double[] getSines(double[] angles) {
		double[] sines = new double[angles.length];

		for (int x = 0; x < angles.length; x++) {
			sines[x] = Math.sin(Math.toRadians(angles[x]));
		}

		return sines;
	}

}
