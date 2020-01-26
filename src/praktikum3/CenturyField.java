package praktikum3;

import javafx.scene.control.TextField;

/**
 *
 * @author Sarah Grugiel
 */
public class CenturyField extends TextField {
    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches("[0-9]") && (getText().length() < 4 || text == "")) {
            super.replaceText(start, end, text);
        }
    }
    
    @Override
    public void replaceSelection(String text) {
        if (text.matches("[0-9]") && (getText().length() < 4 || text == "")) {
            super.replaceSelection(text);
        }
    }
    
}
