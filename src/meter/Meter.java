package meter;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * @author Andrey
 */
public class Meter {
    // Main Window Title
    static public String programTitle = "Автоматизированная система учета измерительных приборов";
    // Window size
    static final int windowWidth = 1200, windowHeight = 600;
    /*********************************************************
     *  Начало выполнения программы
     *********************************************************/
    public static void main(String[] args) {
        MainForm mainform = new MainForm(programTitle, windowWidth, windowHeight);
        mainform.pack();
        mainform.setSize(windowWidth, windowHeight);
        mainform.setMinimumSize(new Dimension(windowWidth, windowHeight));
        // Определение размеров и расположение окна в центре монитора
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int X = ((int) screenSize.getWidth()-windowWidth) /2;
        int Y = ((int) screenSize.getHeight()-windowHeight) /2;
        mainform.setBounds(X, Y, windowWidth, windowHeight);
        // Создать окно неизменяемого размера
        mainform.setResizable(false);
        //
        mainform.setVisible(true);
    }
    /*********************************************************
     *  
     *********************************************************/
}
