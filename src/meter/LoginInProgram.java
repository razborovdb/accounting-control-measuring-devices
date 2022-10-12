
package meter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * @author Andrey
 */
public class LoginInProgram  extends JDialog {
    // Хранение прслушивателей события от этого окна
    private ArrayList<ActionListener> actionListeners;
    // Номер текущего окна для идентификации при отправке событий другим объектам
    int loginInProgramID = 150;
    // Событие регистрация
    String EVENT_LOGIN = "EVENT_LOGIN";
    // Событие снятие регистрации
    String EVENT_LOGOUT = "EVENT_LOGOUT";
    //  Отмена без выполнения каких либо действий
    String EVENT_CANCEL = "EVENT_CANCEL";
    // Переменая для хранения текущего Login
    String currentLogin;
    // Переменая для хранения текущего Password
    String currentPassword;
    // Панель для отображения объектов в диалоговом окне
    JPanel panel;
    // Расположение и размер панели
    private final int panelX=0,panelY=0,panelWidth=500,panelHeight=120;
    // Цвет фона панели
    private Color panelBackground = Color.white;
    // Объявление объекта labelLogin на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Логин")
    JLabel labelLogin;
    // Объявление объекта textLogin на основе класса JTextArea
    // (будет использоваться для ввода Логин) 
    JTextArea textLogin;
    // Объявление объекта labelPassword на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Пароль")
    JLabel labelPassword;
    // Объявление объекта textPassword на основе класса JTextArea
    // (будет использоваться для ввода Пароля) 
    //JTextArea textPassword;
    JPasswordField textPassword;
    // Объявление объекта solidBorder для отображения рамки вокруг текстовых полей
    Border solidBorder;
    // Кнопка Login
    JButton buttonLogin;
    // Кнопка Logout
    JButton buttonLogout;
    // Кнопка Cancel
    JButton buttonCancel;
    
    /*********************************************************
     *  LoginInProgram
     *********************************************************/
    LoginInProgram(JFrame owner, String curLogin)
    {
        super(owner, "Авторизация в системе учета", true);
        // Установка размера диалогового окна
        setBounds(0,0,panelWidth,panelHeight);
        //
        currentLogin = curLogin;
        currentPassword = "";
        // Создание панели для отображения объектов
        panel = new JPanel();
        panel.setBounds(panelX,panelY,panelWidth-20,panelHeight-20);
        panel.setBackground(panelBackground);
        panel.setOpaque(true);
        panel.setLayout(null);
        // добавление панели в окне
        add(panel);
        // Создаем объект solidBorder для отображения рамки вокруг текстовых полей
        solidBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        // Создание объекта labelLogin на основе класса JLabel 
        labelLogin = new JLabel("Логин");
        // Вызов метода setBounds объекта labelLogin для установки
        // положения и размеров labelLogin
        labelLogin.setBounds(10, 0, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelLogin на панель
        panel.add(labelLogin);
        // Создание объекта textLogin на основе класса JTextArea 
        textLogin = new JTextArea(currentLogin);
        // Вызов метода setBounds объекта textLogin для установки
        // положения и размеров textLogin
        textLogin.setBounds(290, 0, 200, 20);
        // Рисование рамки вокруг объекта textLogin
        textLogin.setBorder(solidBorder);
        // Вызов метода add объекта panel, который добавляет
        // textLogin на панель
        panel.add(textLogin);
        // Создание объекта labelPassword на основе класса JLabel 
        labelPassword = new JLabel("Пароль");
        // Вызов метода setBounds объекта labelPassword для установки
        // положения и размеров labelPassword
        labelPassword.setBounds(10, 25, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelPassword на панель
        panel.add(labelPassword);
        // Создание объекта textPassword на основе класса JTextArea 
        textPassword = new JPasswordField(currentPassword);
        // Вызов метода setBounds объекта textPassword для установки
        // положения и размеров textPassword
        textPassword.setBounds(290, 25, 200, 20);
        // Рисование рамки вокруг объекта textPassword
        textPassword.setBorder(solidBorder);
        // Вызов метода add объекта panel, который добавляет
        // textPassword на панель
        panel.add(textPassword);
        // Создание объекта buttonLogin на основе класса JButton 
        buttonLogin = new JButton("Авторизация");
        // Вызов метода setBounds объекта buttonLogin для установки
        // положения и размеров buttonLogin
        buttonLogin.setBounds(10, labelPassword.getHeight()+labelPassword.getY() + 10, 180, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на buttonLogin
        ActionListener actionListenerLogin = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentLogin = textLogin.getText();
                currentPassword = textPassword.getText();
                textLogin.setText("");
                textPassword.setText("");
                outEventToListener(EVENT_LOGIN);
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        buttonLogin.addActionListener(actionListenerLogin);
        // Вызов метода add объекта panel, который добавляет
        // buttonLogin на панель
        panel.add(buttonLogin);
        // Создание объекта buttonLogout на основе класса JButton 
        buttonLogout = new JButton("Снять авторизацию");
        // Вызов метода setBounds объекта buttonLogout для установки
        // положения и размеров buttonLogout
        buttonLogout.setBounds(200, labelPassword.getHeight()+labelPassword.getY() + 10, 180, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на buttonLogout
        ActionListener actionListenerLogout = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textLogin.setText("");
                textPassword.setText("");
                outEventToListener(EVENT_LOGOUT);
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        buttonLogout.addActionListener(actionListenerLogout);
        // Вызов метода add объекта panel, который добавляет
        // buttonLogout на панель
        panel.add(buttonLogout);
        // Создание объекта buttonCancel на основе класса JButton 
        buttonCancel = new JButton("Отмена");
        // Вызов метода setBounds объекта buttonCancel для установки
        // положения и размеров М
        buttonCancel.setBounds(390, labelPassword.getHeight()+labelPassword.getY() + 10, 100, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на buttonCancel
        ActionListener actionListenerCancel = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textLogin.setText("");
                textPassword.setText("");
                outEventToListener(EVENT_CANCEL);
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        buttonCancel.addActionListener(actionListenerCancel);
        // Вызов метода add объекта panel, который добавляет
        // buttonCancel на панель
        panel.add(buttonCancel);
    }
    /*********************************************************
     * Добавление прослушивателя событий от этого объекта
     *********************************************************/
    public void addListener(ActionListener actionListener) {
        if (actionListeners == null) {
            actionListeners = new ArrayList();
        }
        actionListeners.add(actionListener);
    }
    /*********************************************************
     *  Отправка событий из этого окна всем слушателям
     *********************************************************/
    private void outEventToListener(String actionCommand) {
        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(new ActionEvent(this, loginInProgramID, actionCommand));
        }
    }
    /*********************************************************
     * 
     *********************************************************/
}
