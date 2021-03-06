/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coveragetool;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;

/**
 *
 * @author aam5617
 */
public class CoverageTool extends javax.swing.JFrame {

    /**
     * Creates new form CoverageTool
     */
    private Class c;
    // NOTE THIS WILL NEED CHANGED TO RUN A CORRECTLY
    File file = new File("\\\\Psbdfilesrvr.psu-erie.bd.psu.edu\\student\\AAM5617\\Private\\Documents\\NetBeansProjects\\Sample\\build\\classes\\SamplePack");
    ArrayList<File> files;
    DefaultListModel classes = new DefaultListModel();
    ClassOutline co = new ClassOutline();
    JTextArea numbers = new JTextArea();
    MyThread mt = null;
    public CoverageTool() throws ClassNotFoundException, MalformedURLException {
        initComponents();
        numbers.setBackground(Color.red);
        numbers.setSize(WIDTH, WIDTH);
        jScrollPane1.setRowHeaderView(numbers);
        List temp = Arrays.asList(file.listFiles());
        files = new ArrayList<File>(temp);
        for(int i = 0; i < files.size(); i++)
        {
            if(files.get(i).getName().contains(".class"))
            {
            // Getting the folder that the class file is in
            File parentFile = files.get(i).getParentFile();
            // Creating an array of URL objects that has the folder
            URL[] ua = {parentFile.toURI().toURL()};
            URLClassLoader ucl = new URLClassLoader(ua);
            String name = files.get(i).getName().replace(".class", "");
            // Trying to find the class file in the default package
            try{
                c = ucl.loadClass(name);
            } catch (NoClassDefFoundError e)    // Now I will look in one folder up
            {
                // The folder that the package is in
                File parentFile2 = parentFile.getParentFile();
                // New array with both original folder and the folder that that folder is in
                URL[] ua2 = {parentFile.toURI().toURL(), parentFile2.toURI().toURL()};
                ucl = new URLClassLoader(ua2);
                // Creating possible full name
                name = parentFile.getName() + "." + files.get(i).getName().replace(".class", "");
                c = ucl.loadClass(name);
            }
            classes.addElement(c);
            }
        }
        classList.setModel(classes);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonArea = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        outputArea = new javax.swing.JTextArea();
        displayArea = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        methodArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        classList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 500));

        runButton.setText("Run");
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        outputArea.setColumns(20);
        outputArea.setRows(5);
        jScrollPane3.setViewportView(outputArea);

        javax.swing.GroupLayout buttonAreaLayout = new javax.swing.GroupLayout(buttonArea);
        buttonArea.setLayout(buttonAreaLayout);
        buttonAreaLayout.setHorizontalGroup(
            buttonAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(runButton)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        buttonAreaLayout.setVerticalGroup(
            buttonAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonAreaLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(runButton)
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(buttonAreaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        displayArea.setLayout(new java.awt.BorderLayout());

        methodArea.setColumns(20);
        methodArea.setRows(5);
        jScrollPane1.setViewportView(methodArea);

        jSplitPane1.setRightComponent(jScrollPane1);

        classList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                classListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(classList);

        jSplitPane1.setLeftComponent(jScrollPane2);

        displayArea.add(jSplitPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(displayArea, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .addComponent(buttonArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(buttonArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(displayArea, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
        runClass((files.get(classList.getSelectedIndex())));
    }//GEN-LAST:event_runButtonActionPerformed

    private void classListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_classListValueChanged
        //Checks if a class has been run yet or not if not it runs a differnt
        // method because the hashtable has not been initialized. 
        if(mt != null)
            co.update((Class)classList.getSelectedValue(), this, mt.hashTable);
        else
            try {
                co.classSetup((Class)classList.getSelectedValue(), this);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CoverageTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CoverageTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_classListValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CoverageTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CoverageTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CoverageTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CoverageTool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new CoverageTool().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(CoverageTool.class.getName()).log(Level.SEVERE, null, ex);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(CoverageTool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonArea;
    private javax.swing.JList classList;
    private javax.swing.JPanel displayArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea methodArea;
    private javax.swing.JTextArea outputArea;
    private javax.swing.JButton runButton;
    // End of variables declaration//GEN-END:variables
    //Returns the text area that is used to hold the numbers
    public JTextArea getNumbers()
    {
        return numbers;
    }
    
    //Returns the text area that is used to hold the methods
    public JTextArea getMethodArea()
    {
        return methodArea;
    }
    
    /**
     * Updates the numbers that tell how many times a method has been run
     */
    public void updateNumbers()
    {
        //Checks if a class has been run yet or not if not it runs a differnt
        // method because the hashtable has not been initialized.
        if(mt != null)
        {
            co.update((Class)classList.getSelectedValue(), this, mt.hashTable);
        }
        else
            try {
                co.classSetup((Class)classList.getSelectedValue(), this);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CoverageTool.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CoverageTool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void runClass(File file)
    {
        LaunchingConnector lc = Bootstrap.virtualMachineManager().defaultConnector();
        Map map = lc.defaultArguments();
        Connector.Argument ca = (Connector.Argument) map.get("main");
        try {
            String packageName = file.getParent().substring(file.getParent().lastIndexOf("\\") + 1);
            String cName = packageName + "." +  file.getName().replaceAll(".class", "");
            ca.setValue("-cp \"" + file.getParentFile().getParent() + "\" " + cName);
            VirtualMachine vm = lc.launch(map);
            final Process process = vm.process();
            vm.setDebugTraceMode(VirtualMachine.TRACE_NONE);
            mt = new MyThread(vm, false, file.getParentFile().getName(), classes.size(), this);
            
            //Outputting output from virtual machine to outputArea
            Thread thr = new Thread("output reader") {
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output;
                try {
                    while ((output = in.readLine()) != null) {
                        outputArea.append(output + "\n"); // Put output into outputArea
                    }
                } catch (IOException ex) {
                    System.out.println("Failed reading output");
                }
            }
        };
        thr.setPriority(Thread.MAX_PRIORITY - 1);
        thr.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
