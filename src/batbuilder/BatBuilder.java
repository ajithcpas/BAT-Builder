/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package batbuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Ajith
 */
class Modal implements ActionListener
{
    private boolean stat = false;
    private JDialog dialog = null;
    private JButton ok_btn = null;
    private JButton cancel_btn = null;
    
    public boolean confirm(JFrame frame, String msg)
    {
        dialog = new JDialog(frame,"Alert");
        ok_btn = new JButton("Ok");
        cancel_btn = new JButton("Cancel");
        dialog.setLayout(new BorderLayout());
        
        dialog.add(new JLabel(msg),BorderLayout.PAGE_START);
        dialog.add(ok_btn,BorderLayout.EAST);
        dialog.add(cancel_btn,BorderLayout.WEST);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        
        ok_btn.addActionListener(this);
        cancel_btn.addActionListener(this);
        
        dialog.setSize(200,200);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        
        return stat;
    }
    
    public void alert(JFrame frame, String msg)
    {
        dialog = new JDialog(frame,"Alert");
        dialog.setLayout(new BorderLayout());
        dialog.add(new JLabel(msg),BorderLayout.PAGE_START);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(200,200);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == ok_btn){ stat = true; }
        else{ stat = false; }
        dialog.setVisible(false);
    }
}
public class BatBuilder implements ActionListener
{
    private static Logger logger = Logger.getLogger(BatBuilder.class.getName());
    JFrame frame = null;
    JPanel panel = null;
    String title = "BAT Builder";
    JButton java_btn = null;
    JButton jar_btn = null;
    JButton out_btn = null;
    JButton build_btn = null;
    JTextField java_text = null;
    JTextField jar_text = null;
    JTextField out_text = null;
    
    public BatBuilder()
    {
        frame = new JFrame(title);
        java_btn = new JButton("Open");
        jar_btn = new JButton("Open");
        out_btn = new JButton("Select");
        build_btn = new JButton("Build");
        java_btn.addActionListener(this);
        jar_btn.addActionListener(this);
        out_btn.addActionListener(this);
        build_btn.addActionListener(this);
        
        java_text = new JTextField();
        jar_text = new JTextField();
        out_text = new JTextField();
       
        panel = new JPanel(new GridBagLayout());
        align(10,10,new JLabel("Java source file path:"));
        align(10,20,java_text);
        align(100,20,java_btn);
        align(10,40,new JLabel("Jar file path:"));
        align(10,50,jar_text);
        align(100,50,jar_btn);
        align(10,60,new JLabel("Output file path:"));
        align(10,70,out_text);
        align(100,70,out_btn);
        align(10,80,build_btn);
        panel.setBounds(0, 20, 250, 200);
        
        align(10,100,new JLabel("Copyright \u00a9 2017"));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(250,300);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == build_btn)
        {
            if(java_text.getText().isEmpty() || jar_text.getText().isEmpty() || out_text.getText().isEmpty())
            {
                new Modal().alert(frame,"Fields cannot be empty");
                return;
            }
            File file = new File(out_text.getText());
            File tmp_dir = new File("bat_temp");
            if(!tmp_dir.exists())
            {
                    tmp_dir.mkdir();
            }
            if(file.exists())
            {
                boolean stat = new Modal().confirm(frame,"Replace existing file?");
                if(stat){ file.delete(); }
                else{ return; }
            }
                FileWriter fw = null;
                try
                {
                    file.createNewFile();
                    fw = new FileWriter(file);
                    fw.append("javac -d " + tmp_dir.getAbsolutePath() + " " + java_text.getText() + "\r\n");
                    fw.append("cd " + tmp_dir.getAbsolutePath() + "\r\n");
                    File jar_file = new File(jar_text.getText());
                    if(!jar_file.exists())
                    {
                        fw.append("jar -cf " + jar_text.getText() + " " + "*" + "\r\n");
                    }
                    else
                    {
                        fw.append("jar -uf " + jar_text.getText() + " " + "*" + "\r\n");
                    }
                    fw.append("pause");
                }
                catch(IOException ex)
                {
                    logger.log(Level.SEVERE," " + ex);
                }
                finally{
                    try{
                        if(fw != null){ fw.close(); }
                    }catch(Exception ex){
                        logger.log(Level.SEVERE," " + ex);
                    }
                }
            
        }
        else
        {
            Object event_src = e.getSource();
            JFileChooser fc = new JFileChooser();
            int i;
            if(event_src == out_btn)
            {
                i = fc.showSaveDialog(this.frame);
            }
            else
            {
                i = fc.showOpenDialog(this.frame);
            }
            if(i == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();
                String path = file.getAbsolutePath();
                if(event_src == java_btn)
                {
                    java_text.setText(path);
                }
                else if(event_src == jar_btn)
                {
                    jar_text.setText(path);
                }
                else if(event_src == out_btn)
                {
                    out_text.setText(path);
                }
            }
        }
    }
    
    private void align(int x,int y,Component c){
        GridBagConstraints gb_constraints = new GridBagConstraints();
        gb_constraints.fill = GridBagConstraints.HORIZONTAL;
        gb_constraints.gridx = x;
        gb_constraints.gridy = y;
        gb_constraints.weighty = 0.5;
        panel.add(c,gb_constraints);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        BatBuilder batBuilder = new BatBuilder();
    }
    
}
