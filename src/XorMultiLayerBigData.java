import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;
import java.util.Scanner;

public class XorMultiLayerBigData {
	
	public static void main(String[] args) {

		XorMultiSine sineTest = new XorMultiSine();
//		sineTest.generateNeural();
		sineTest.testNeural();
	}
}
