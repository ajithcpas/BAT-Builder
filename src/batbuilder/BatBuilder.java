package batbuilder;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
public class BatBuilder implements ActionListener
{
    private static Logger logger = Logger.getLogger(BatBuilder.class.getName());
    JFrame frame = null;
    JPanel panel = null;
    JButton java_btn = null;
    JButton jar_btn = null;
    JButton out_btn = null;
    JButton build_btn = null;
    JTextField java_text = null;
    JTextField jar_text = null;
    JTextField out_text = null;
    JCheckBox compile_options = null;
    private String options = null;
    private Hashtable<String,String> opt_obj = null;
    
    public BatBuilder()
    {
        frame = new JFrame("BAT Builder");
        java_btn = new JButton("Open");
        jar_btn = new JButton("Open");
        out_btn = new JButton("Select");
        build_btn = new JButton("Build");
        compile_options = new JCheckBox();
        options = "";
        opt_obj = new Hashtable<String,String>();
        compile_options.addActionListener(this);
        java_btn.addActionListener(this);
        jar_btn.addActionListener(this);
        out_btn.addActionListener(this);
        build_btn.addActionListener(this);
        
        java_text = new JTextField();
        jar_text = new JTextField();
        out_text = new JTextField();
        
        panel = new JPanel(new GridBagLayout());
        BatUtils.align(10,10,new JLabel("Java file path:"),panel);
        BatUtils.align(10,20,java_text,panel);
        BatUtils.align(100,20,java_btn,panel);
        
        JPanel tmppanel = new JPanel();
        tmppanel.add(compile_options);
        tmppanel.add(new JLabel("include javac options"));
        BatUtils.align(10,40,tmppanel,panel);
        
        BatUtils.align(10,60,new JLabel("Jar file path:"),panel);
        BatUtils.align(10,70,jar_text,panel);
        BatUtils.align(100,70,jar_btn,panel);
        BatUtils.align(10,80,new JLabel("Output file path:"),panel);
        BatUtils.align(10,90,out_text,panel);
        BatUtils.align(100,90,out_btn,panel);
        BatUtils.align(10,100,build_btn,panel);
        BatUtils.align(10,110,new JLabel("Copyright \u00a9 2017"),panel);
        java_text.setPreferredSize(new Dimension(300,20));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.pack();
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
            if(!compile_options.isSelected())
            {
                options = "";
            }
            File file = new File(out_text.getText() + ".bat");
            File tmp_dir = new File(System.getProperty("java.io.tmpdir") + "bat_temp");
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
                    StringBuilder sb = new StringBuilder();
                    sb.append("@echo off\r\n");
                    sb.append("if exist ").append(BatUtils.quote(tmp_dir.getAbsolutePath())).append(" ( rmdir /s /q ").append(BatUtils.quote(tmp_dir.getAbsolutePath())).append(" )\r\n");
                    sb.append("mkdir ").append(BatUtils.quote(tmp_dir.getAbsolutePath())).append("\r\n");
                    sb.append("echo Compiling java file...\r\n");
                    sb.append("javac -d ").append(BatUtils.quote(tmp_dir.getAbsolutePath())).append(options).append(" ").append(BatUtils.quote(java_text.getText())).append("\r\n");
                    sb.append("if not %errorlevel% equ 0 ( \r\n echo Compilation failed.\r\n goto :END\r\n) ");
                    sb.append("else ( \r\n echo Compilation success. )\r\n");
                    sb.append("cd ").append(BatUtils.quote(tmp_dir.getAbsolutePath())).append("\r\n");
                    sb.append("echo Updating jar file...\r\n");
                    File jar_file = new File(jar_text.getText());
                    sb.append("jar ");
                    if(!jar_file.exists())
                    {
                        sb.append("echo File not exist. Creating new JAR file...\r\n");
                        sb.append("-cf ");
                    }
                    else
                    {
                        sb.append("-uf ");
                    }
                    sb.append(BatUtils.quote(jar_text.getText())).append(" *\r\n");
                    sb.append("if not %errorlevel% equ 0 ( \r\n echo Updation failed.\r\n goto :END\r\n) ");
                    sb.append("else ( \r\n echo Updation success. )\r\n");
                    sb.append(":END\r\npause");
                    fw.append(sb.toString());
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
        else if(e.getSource() == compile_options)
        {
            if(!compile_options.isSelected())
            {
                return;
            }
            opt_obj = new Modal().optionDialog(frame,opt_obj);
            StringBuilder options = new StringBuilder();
            if(opt_obj.containsKey("TARGET") && !opt_obj.get("TARGET").isEmpty())
            {
                options.append(" -target ").append(opt_obj.get("TARGET"));
            }
            if(opt_obj.containsKey("SOURCE") && !opt_obj.get("SOURCE").isEmpty())
            {
                options.append(" -source ").append(opt_obj.get("SOURCE"));
            }
            if(opt_obj.containsKey("BOOTCLASSPATH") && !opt_obj.get("BOOTCLASSPATH").isEmpty())
            {
                options.append(" -bootclasspath ").append(opt_obj.get("BOOTCLASSPATH"));
            }
            if(opt_obj.containsKey("CLASSPATH") && !opt_obj.get("CLASSPATH").isEmpty())
            {
                options.append(" -cp ").append(opt_obj.get("CLASSPATH"));
            }
            options.append(" ");
            this.options = options.toString();
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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        BatBuilder batBuilder = new BatBuilder();
    }
    
}
