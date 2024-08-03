import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
//credit to https://www.jfree.org/jfreechart/ for visualisation library
public class ApplicationGUI extends DataProcessing {
    DataProcessing d = new DataProcessing();
    List<StudentData> sdatabase;
    List<ModuleData> mdatabase;

    public ApplicationGUI() {

        JFrame f = new JFrame("Education View Tool");
        JPanel appPanel = new JPanel();
        appPanel.setLayout(new BorderLayout());
        appPanel.setVisible(true);

        JPanel outarea = new JPanel();
        JTextArea out = new JTextArea() ;
        outarea.add(out) ;
        outarea.setSize(300,300);
        
        JPanel buttons = new JPanel() ;

        outarea.add(new JScrollPane()) ;
        //f.add(outarea) ;

        JButton b3 = new JButton("Student data");
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame stats = new JFrame() ;
                try{
                    sdatabase.get(1) ;
                    StudentStats ss = new StudentStats(sdatabase) ;
                    stats.setVisible(false);
                }
                catch (NullPointerException npe){
                    JOptionPane.showMessageDialog(stats, "Import dataset first");

                }
                catch (IndexOutOfBoundsException oob){
                    JOptionPane.showMessageDialog(stats, "Imported dataset in wrong format");
                }


            }
        });

        JButton b2 = new JButton("Create PDF");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


            }
        });


        JButton b1 = new JButton("Import data");
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser choosefile = new JFileChooser() ;
                int result = choosefile.showOpenDialog(buttons) ;

                if (result == JFileChooser.APPROVE_OPTION){
                    try {
                        sdatabase = d.readData(choosefile.getSelectedFile().getAbsolutePath());
                        JOptionPane.showMessageDialog(new JFrame(), "Data has been loaded");
                    }
                    catch (IndexOutOfBoundsException oob){
                        JOptionPane.showMessageDialog(new JFrame(), "Imported dataset in wrong format");
                    }
                    catch (NumberFormatException nf){
                        JOptionPane.showMessageDialog(new JFrame(), "Imported dataset in wrong format");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(new JFrame(), "No file has been selected");
                }


            }
        });
        JButton b4 = new JButton("Module Data");
        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame visual = new JFrame() ;
                try{
                    sdatabase.get(1) ;
                    List<ModuleData> modules = d.createModuleDatabase();
                    Visualisation v = new Visualisation(sdatabase, modules) ;
                    visual.setVisible(false);
                }
                catch (NullPointerException npe){
                    JOptionPane.showMessageDialog(visual, "Import dataset first");

                }
                catch (IndexOutOfBoundsException oob){
                    JOptionPane.showMessageDialog(visual, "Imported dataset in wrong format");
                }
            }
        });
        /*JButton b5 = new JButton("PlaceHolder5");
        b5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });

         */

        appPanel.add(BorderLayout.NORTH,buttons) ;
        //appPanel.add(BorderLayout.SOUTH,outarea) ;

        buttons.add(b1);
        //buttons.add(b2);
        buttons.add(b3);
        buttons.add(b4);
        //buttons.add(b5);
        buttons.setSize(100,100);

        //create new panel that shows output

        f.setSize(800, 100);
        f.add(appPanel, "Center");
        f.setDefaultCloseOperation(3);
        f.setVisible(true);
    }
}

