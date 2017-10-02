package batbuilder;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Ajith
 */
public class Modal
{
    private boolean stat = false;
    private JDialog dialog = null;
    private Hashtable<String,String> options = null;
    public Hashtable<String,String> optionDialog(JFrame frame, Hashtable<String,String> opt_obj)
    {
        String target = opt_obj.containsKey("TARGET") ? opt_obj.get("TARGET") : "";
        String source = opt_obj.containsKey("SOURCE") ? opt_obj.get("SOURCE") : "";
        String bcp = opt_obj.containsKey("BOOTCLASSPATH") ? BatUtils.unquote(opt_obj.get("BOOTCLASSPATH")) : "";
        String cp = opt_obj.containsKey("CLASSPATH") ? BatUtils.unquote(opt_obj.get("CLASSPATH")) : "";
        
        Hashtable<String,String> options = new Hashtable<String,String>();
        dialog = new JDialog(frame,"javac options");
        JTextField target_text = new JTextField(target);
        JTextField source_text = new JTextField(source);
        JTextField bcp_text = new JTextField(bcp);
        JTextField cp_text = new JTextField(cp);
        JButton ok_btn = new JButton("Ok");
        JButton bcp_btn = new JButton("Add");
        JButton cp_btn = new JButton("Add");
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        BatUtils.align(10,10,new JLabel("Target :"),panel);
        BatUtils.align(50,10,target_text,panel);
        BatUtils.align(10,20,new JLabel("Source :"),panel);
        BatUtils.align(50,20,source_text,panel);
        BatUtils.align(10,30,new JLabel("Bootclasspath :"),panel);
        BatUtils.align(50,30,bcp_text,panel);
        BatUtils.align(70,30,bcp_btn,panel);
        
        BatUtils.align(10,40,new JLabel("Classpath :"),panel);
        BatUtils.align(50,40,cp_text,panel);
        BatUtils.align(70,40,cp_btn,panel);
        
        BatUtils.align(50,50,ok_btn,panel);
        bcp_text.setPreferredSize(new Dimension(300,20));
        
        bcp_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if(JFileChooser.APPROVE_OPTION == fc.showOpenDialog(dialog))
                {
                    String path = fc.getSelectedFile().getAbsolutePath() + ";";
                    bcp_text.setText(bcp_text.getText()+path);
                }
            }
        });
        cp_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if(JFileChooser.APPROVE_OPTION == fc.showOpenDialog(dialog))
                {
                    String path = fc.getSelectedFile().getAbsolutePath() + ";";
                    cp_text.setText(cp_text.getText()+path);
                }
            }
        });
        ok_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!target_text.getText().trim().isEmpty())
                {
                    options.put("TARGET", target_text.getText().trim());
                }
                if(!source_text.getText().trim().isEmpty())
                {
                    options.put("SOURCE", source_text.getText().trim());
                }
                if(!bcp_text.getText().trim().isEmpty())
                {
                    options.put("BOOTCLASSPATH", BatUtils.quote(bcp_text.getText().trim()));
                }
                if(!cp_text.getText().trim().isEmpty())
                {
                    options.put("CLASSPATH", BatUtils.quote(cp_text.getText().trim()));
                }
                dialog.setVisible(false);
            }
        });
        dialog.add(panel);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        
        return options;
    }
    public boolean confirm(JFrame frame, String msg)
    {
        dialog = new JDialog(frame,"Alert");
        JPanel panel = new JPanel();
        JButton ok_btn = new JButton("Ok");
        JButton cancel_btn = new JButton("Cancel");
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel(msg);
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        panel.add(label,BorderLayout.PAGE_START);
        panel.add(ok_btn,BorderLayout.WEST);
        panel.add(cancel_btn,BorderLayout.EAST);
        
        ok_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stat = true;
                dialog.setVisible(false);
            }
        });
        cancel_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stat = false;
                dialog.setVisible(false);
            }
        });
        
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        
        return stat;
    }
    
    public void alert(JFrame frame, String msg)
    {
        dialog = new JDialog(frame,"Alert");
        dialog.setLayout(new BorderLayout());
        JLabel label = new JLabel(msg);
        label.setBorder(new EmptyBorder(10, 10, 10, 10));
        dialog.add(label,BorderLayout.PAGE_START);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    //    dialog.setSize(200,200);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
}
