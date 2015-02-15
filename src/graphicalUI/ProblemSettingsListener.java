package graphicalUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class ProblemSettingsListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        selectionBox();
    }

    // Method to allow bounds/objective box call anywhere
    public static void selectionBox() {
        // Display input box for objective and bounds
        BoundsObjectiveDialog bod = new BoundsObjectiveDialog(GraphicalUI.frame);
        bod.pack();
        bod.setLocationRelativeTo(GraphicalUI.frame);
        bod.setVisible(true);
        
        if (!BoundsObjectiveDialog.cancelled) {
        	JOptionPane.showMessageDialog(GraphicalUI.frame, "Click on the board to select your bounds.\n"
        			+ "Press 'Show Bounds' once finished.");
        }
        
        BoardJPanel.boundsSelectionMode = true;
        GraphicalUI.updateBoundsButton(true);
		GraphicalUI.setBounds(true);
    }
}