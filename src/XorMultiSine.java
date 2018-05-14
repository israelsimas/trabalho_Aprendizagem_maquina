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

	public XorMultiSine() {
		
	} 
	
	public void generateNeural(int numNeuro, int numLayers) {
		
		// create training set (logical XOR function)
		DataSet trainingSet = new DataSet(1, 1);
		
		double[] angulosn = getVectorAngulosN();
		double[] senos = getSenos(getVectorAngulos());
		
		for (int x = 0; x < angulosn.length; x++) {
			trainingSet.addRow(new DataSetRow(new double[] {angulosn[x]}, new double[] {senos[x]}));
		}
		
		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, numNeuro, numLayers, 1);
		
		// learn the training set
		BackPropagation learningrules = new BackPropagation();
		learningrules.setMaxError(0.00001);
		myMlPerceptron.learn(trainingSet, learningrules);
		
		// test perceptron
		System.out.println("Testing trained neural network");
		testNeuralNetwork(myMlPerceptron, trainingSet);
		
		// save trained neural network
		myMlPerceptron.save("myMlPerceptron.nnet");
		
		// load saved neural network
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");
		
		// test loaded neural network
		System.out.println("Testing loaded neural network");
		testNeuralNetwork(loadedMlPerceptron, trainingSet);
	}
	
	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
		
		double[] saidas = new double[9];
		int x = 0;
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			System.out.println(" Output: " + Arrays.toString(networkOutput));
			
			saidas[x] = networkOutput[0];
			x++;
		}
		
		double[] senos = getSenos(getVectorAngulos());
		double erro1 = 0;
		for (int z = 0; z < senos.length; z++) {
			erro1 += Math.abs((saidas[z] - senos[z]));
		}
		
		System.out.println("Erro Total: " + erro1);
		
	}
	
	private static double[] getVectorAngulos() {
	//	angulos[0] = 0;
	//	angulos[1] = 45;
	//	angulos[2] = 90;
	//	angulos[3] = 135;
	//	angulos[4] = 180;
	//	angulos[5] = 225;
	//	angulos[6] = 270;
	//	angulos[7] = 315;
	//	angulos[8] = 360;
	//	
		int inicio = 0;
		int fim = 360;
		int i = 5;
		
		double qtde = fim/i;
		double[] angulos = new double[(int)(qtde)+1];
		
		for (int x = inicio; x <= qtde; x++) {
			angulos[x] = x * i;
		}
		
		return angulos;
	}
	private static double[] getVectorAngulosN() {
		double[] angulos = getVectorAngulos();
		
		double[] angulosn = new double[angulos.length];
		for (int x = 0; x < angulos.length; x++) {
			angulosn[x] = angulos[x]/360;
		}
		
		return angulosn;
	}
	
	
	private static double[] getSenos(double[] angulos) {
		double[] senos = new double[angulos.length];
		
		for (int x = 0; x < angulos.length; x++) {
			senos[x] = Math.sin(Math.toRadians(angulos[x]));
		}
		
		return senos;
	}

}	
