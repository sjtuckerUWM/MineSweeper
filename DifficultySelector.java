package game;

import javax.swing.JOptionPane;

public class DifficultySelector {

	public int pane() {
		String[] choices = { "Easy", "Normal", "Hard"};
	    String input = (String) JOptionPane.showInputDialog(null, "Choose your prefered difficulty:", "Difficulty Selector", JOptionPane.QUESTION_MESSAGE, null, choices, choices[1]);
		if(input == "Easy") {
			return 8;
		} if(input =="Normal") {
			return 16;
		} else {
			return 32;
		}
	}
}
