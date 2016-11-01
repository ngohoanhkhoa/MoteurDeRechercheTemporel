package demo.bob.stanford.sutime;

import java.util.List;

import edu.stanford.nlp.simple.Sentence;

public class Demo2 {

	public static void main(String[] str) {
		Sentence sentence = new Sentence("Lucy is in the sky with diamonds.");
		List<String> nerTags = sentence.nerTags(); // [PERSON, O, O, O, O, O, O, O]
		String firstPOSTag = sentence.posTag(0); // NNP

		System.out.println(firstPOSTag);
	}
}
