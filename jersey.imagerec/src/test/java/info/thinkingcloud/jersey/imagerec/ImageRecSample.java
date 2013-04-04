package info.thinkingcloud.jersey.imagerec;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.DataSet;
import org.neuroph.imgrec.ColorMode;
import org.neuroph.imgrec.FractionRgbData;
import org.neuroph.imgrec.ImageRecognitionHelper;
import org.neuroph.imgrec.ImageRecognitionPlugin;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

public class ImageRecSample implements LearningEventListener {

	public static Map<String, FractionRgbData> images = new HashMap<String, FractionRgbData>();

	public static DataSet createTrainingSet() throws IOException {
		for (int i = 0; i < 9; i++) {
			images.put(
			        String.valueOf(i),
			        new FractionRgbData(ImageIO.read(Thread.currentThread().getContextClassLoader()
			                .getResourceAsStream("images/" + i + ".png"))));
		}
		images.put(
		        "dot",
		        new FractionRgbData(ImageIO.read(Thread.currentThread().getContextClassLoader()
		                .getResourceAsStream("images/dot.png"))));
		return ImageRecognitionHelper.createTrainingSet(new ArrayList<String>(images.keySet()), images);
	}

	public static void main(String[] args) throws IOException {
		DataSet trainSet = createTrainingSet();
		List<Integer> list = new ArrayList<Integer>();
		list.add(1350);
		list.add(1350);

		NeuralNetwork network = ImageRecognitionHelper.createNewNeuralNetwork("Test", new Dimension(9, 15),
		        ColorMode.FULL_COLOR, new ArrayList<String>(images.keySet()), list, TransferFunctionType.SIGMOID);
		BackPropagation rule = (BackPropagation) network.getLearningRule();
		rule.setMaxIterations(100);
		network.getLearningRule().addListener(new ImageRecSample());
		network.learn(trainSet);

		ImageRecognitionPlugin rec = (ImageRecognitionPlugin) network.getPlugin(ImageRecognitionPlugin.class);
		BufferedImage test = ImageIO
		        .read(Thread.currentThread().getContextClassLoader().getResourceAsStream("aaa.png"));
		int count = test.getWidth() - 9;
		for (int i = 0; i <= count; i++) {
			System.out.println("------" + i);
			ImageIO.write(test.getSubimage(i, 0, 9, 15), "png",
			        ImageIO.createImageOutputStream(new File("/tmp/imgs/" + i + ".png")));
			Map<String, Double> result = rec.recognizeImage(test.getSubimage(i, 0, 9, 15));
			for (String key : result.keySet()) {
				System.out.println(MessageFormat.format("{0} = {1}", key, result.get(key)));
			}
		}
	}

	@Override
	public void handleLearningEvent(LearningEvent event) {
		BackPropagation bp = (BackPropagation) event.getSource();
		System.out.println(bp.getCurrentIteration() + ". iteration : " + bp.getTotalNetworkError());
	}
}
