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
		double[] angulosn = getVectorAngulosN();
		double[] senos = getSenos(getVectorAngulos());

		for (int x = 0; x < (LEN_ARRAY_TRAINING - 1); x++) {
			trainingSet.addRow(new DataSetRow(new double[] { angulosn[x] }, new double[] { senos[x] }));
		}

		// create multi layer perceptron
		MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 1, 6, 10, 1);

		// learn the training set
		BackPropagation learningrules = new BackPropagation();
		learningrules.setMaxError(ERROR_MAX);

		System.out.println("Training neural network ....");
		myMlPerceptron.learn(trainingSet, learningrules);

		// test perceptron
		System.out.println("Testing trained neural network ....");
		testNeuralNetwork(myMlPerceptron, trainingSet);

	}

	public void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

		double[] saidas = new double[LEN_ARRAY_TEST];
		int x = 0;

		for (DataSetRow dataRow : testSet.getRows()) {

			nnet.setInput(dataRow.getInput());
			nnet.calculate();

			double[] networkOutput = nnet.getOutput();

			saidas[x] = networkOutput[0];
			x++;
		}

		double[] senos = getSenos(getVectorAngulos());
		double erro1 = 0;
		for (int z = 0; z < LEN_ARRAY_TRAINING; z++) {
			erro1 += Math.abs((saidas[z] - senos[z]));
			System.out.println("saidas[z]: " + saidas[z]);
			System.out.println("senos[z]: " + senos[z]);
		}

		System.out.println("\n\n");
		System.out.println("Erro Total: " + erro1);
	}

	private double[] getVectorAngulos() {

		double qtde = LEN_ARRAY_TRAINING;
		double[] angulos = new double[(int) (qtde) + 1];

		// Angulos inferiores (0 - 45)
		angulos[0]  = 0;
		angulos[1]  = 30;
		angulos[2]  = 60;
		angulos[3]  = 90;
		angulos[4]  = 120;
		angulos[5]  = 150;
		angulos[6]  = 180;
		angulos[7]  = 210;
		angulos[8]  = 240;
		angulos[9]  = 270;
		angulos[10] = 300;
		angulos[11] = 330;
		angulos[12] = 360;

		return angulos;
	}

	private double[] getVectorAngulosN() {
		double[] angulos = getVectorAngulos();

		// Normaliza os valores dos Angulos
		double[] angulosn = new double[angulos.length];
		for (int x = 0; x < angulos.length; x++) {
			angulosn[x] = angulos[x] / 360;
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
