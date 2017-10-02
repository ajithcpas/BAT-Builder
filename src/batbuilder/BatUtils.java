package batbuilder;

import java.awt.Component;
import java.awt.GridBagConstraints;
import javax.swing.JPanel;

/**
 *
 * @author Ajith
 */
public class BatUtils {
    
    public static void align(int x,int y,Component c,JPanel panel)
    {
        GridBagConstraints gb_constraints = new GridBagConstraints();
        gb_constraints.fill = GridBagConstraints.HORIZONTAL;
        gb_constraints.gridx = x;
        gb_constraints.gridy = y;
        gb_constraints.weighty = 0.5;
        panel.add(c,gb_constraints);
    }
    
    public static String quote(String text)
    {
        return "\"" + text + "\"";
    }
    public static String unquote(String text)
    {
        return text.substring(1, text.length()-1);
    }
}
