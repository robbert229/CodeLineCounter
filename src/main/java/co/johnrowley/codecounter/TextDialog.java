package co.johnrowley.codecounter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class TextDialog extends JDialog implements ActionListener {
    public TextDialog(JFrame parent, String title, String message){
        super(parent, title, true);
        if(parent != null) {
            Dimension parentSize = parent.getSize();
            Point p = parent.getLocation();
            setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
        }

        JPanel textPane = new JPanel();
        textPane.add(new JLabel(message));
        getContentPane().add(textPane);

        JPanel buttonPane = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        buttonPane.add(closeButton);

        getContentPane().add(buttonPane, BorderLayout.SOUTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        setVisible(false);
        dispose();
    }
}
