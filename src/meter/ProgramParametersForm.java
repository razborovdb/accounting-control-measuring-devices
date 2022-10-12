
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
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * @author Andrey
 */
public class ProgramParametersForm extends JDialog{
    // Хранение прслушивателей события от этого окна
    private ArrayList<ActionListener> actionListeners;
    // Номер текущего окна для идентификации при отправке событий другим объектам
    int programParametersFormID = 151;
    // Событие завершение с сохранением параметров
    String EVENT_OK = "EVENT_OK";
    // Событие завершение без сохранения параметров
    String EVENT_CANCEL = "EVENT_CANCEL";
    // Объект для сохранения/чтения параметров программы
    public ProgramParameters programParameters;
    // Панель для отображения объектов в диалоговом окне
    JPanel panel;
    // Расположение и размер панели
    private final int panelX=0,panelY=0,panelWidth=440,panelHeight=190;
    // Цвет фона панели
    private Color panelBackground = Color.white;
    // Объявление объекта labelIP на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "IP адрес")
    JLabel labelIP;
    // Объявление объекта textIP на основе класса JTextArea
    // (будет использоваться для ввода IP) 
    JTextArea textIP;
    // Объявление объекта labelPort на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Порт")
    JLabel labelPort;
    // Объявление объекта textPort на основе класса JTextArea
    // (будет использоваться для ввода Порта) 
    JTextArea textPort;
    // Объявление объекта labelDB на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Название базы данных")
    JLabel labelDB;
    // Объявление объекта textDB на основе класса JTextArea
    // (будет использоваться для ввода Названия базы данных) 
    JTextArea textDB;
    // Объявление объекта labelUser на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Пользователь для подключения")
    JLabel labelUser;
    // Объявление объекта textUser на основе класса JTextArea
    // (будет использоваться для ввода Пользователя для подключения) 
    JTextArea textUser;
    // Объявление объекта labelPassword на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Пароль")
    JLabel labelPassword;
    // Объявление объекта textUser на основе класса JTextArea
    // (будет использоваться для ввода Пароля) 
    JTextArea textPassword;
    // Объявление объекта solidBorder для отображения рамки вокруг текстовых полей
    Border solidBorder;
    // Кнопка Login
    JButton buttonOk;
    // Кнопка Cancel
    JButton buttonCancel;
    
    /*********************************************************
     *  ProgramParametersForm
     *********************************************************/
    ProgramParametersForm(JFrame owner)
    {
        super(owner, "Настройка параметров системы учета", true);
        // Установка размера диалогового окна
        setBounds(0,0,panelWidth,panelHeight);
        //
        programParameters = new ProgramParameters();
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
        // Создание объекта labelIP на основе класса JLabel 
        labelIP = new JLabel("IP адрес");
        // Вызов метода setBounds объекта labelIP для установки
        // положения и размеров labelIP
        labelIP.setBounds(10, 0, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelIP на панель
        panel.add(labelIP);
        // Создание объекта textIP на основе класса JTextArea 
        textIP = new JTextArea(programParameters.serverIP);
        // Вызов метода setBounds объекта textIP для установки
        // положения и размеров textIP
        textIP.setBounds(220, 0, 200, 20);
        // Рисование рамки вокруг объекта textIP
        textIP.setBorder(solidBorder);
        // Вызов метода add объекта panel, который добавляет
        // textIP на панель
        panel.add(textIP);
        // **********************************************************
        // Создание объекта labelPort на основе класса JLabel 
        labelPort = new JLabel("Порт");
        // Вызов метода setBounds объекта labelPort для установки
        // положения и размеров labelPort
        labelPort.setBounds(10, 25, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelPort на панель
        panel.add(labelPort);
        // Создание объекта textPort на основе класса JTextArea 
        textPort = new JTextArea(programParameters.serverPort);
        // Вызов метода setBounds объекта textPort для установки
        // положения и размеров textPort
        textPort.setBounds(220, 25, 200, 20);
        // Рисование рамки вокруг объекта textPort
        textPort.setBorder(solidBorder);
        // Вызов метода add объекта panel, который добавляет
        // textPort на панель
        panel.add(textPort);
        // **********************************************************
        // Создание объекта labelDB на основе класса JLabel 
        labelDB = new JLabel("Название базы данных");
        // Вызов метода setBounds объекта labelDB для установки
        // положения и размеров labelDB
        labelDB.setBounds(10, 50, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelDB на панель
        panel.add(labelDB);
        // Создание объекта textDB на основе класса JTextArea 
        textDB = new JTextArea(programParameters.databaseName);
        // Вызов метода setBounds объекта textDB для установки
        // положения и размеров textDB
        textDB.setBounds(220, 50, 200, 20);
        // Рисование рамки вокруг объекта textDB
        textDB.setBorder(solidBorder);
        // Вызов метода add объекта panel, который добавляет
        // textDB на панель
        panel.add(textDB);
        // **********************************************************
        // Создание объекта labelUser на основе класса JLabel 
        labelUser = new JLabel("Пользователь для подключения");
        // Вызов метода setBounds объекта labelUser для установки
        // положения и размеров labelUser
        labelUser.setBounds(10, 75, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelUser на панель
        panel.add(labelUser);
        // Создание объекта textUser на основе класса JTextArea 
        textUser = new JTextArea(programParameters.databaseUser);
        // Вызов метода setBounds объекта textUser для установки
        // положения и размеров textUser
        textUser.setBounds(220, 75, 200, 20);
        // Рисование рамки вокруг объекта textUser
        textUser.setBorder(solidBorder);
        // Вызов метода add объекта panel, который добавляет
        // textUser на панель
        panel.add(textUser);
        // **********************************************************
        // Создание объекта labelPassword на основе класса JLabel 
        labelPassword = new JLabel("Пароль");
        // Вызов метода setBounds объекта labelPassword для установки
        // положения и размеров labelPassword
        labelPassword.setBounds(10, 100, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelPassword на панель
        panel.add(labelPassword);
        // Создание объекта textPassword на основе класса JTextArea 
        textPassword = new JTextArea(programParameters.databasePassword);
        // Вызов метода setBounds объекта textPassword для установки
        // положения и размеров textPassword
        textPassword.setBounds(220, 100, 200, 20);
        // Рисование рамки вокруг объекта textPassword
        textPassword.setBorder(solidBorder);
        // Вызов метода add объекта panel, который добавляет
        // textPassword на панель
        panel.add(textPassword);
        // **********************************************************
        // Создание объекта buttonOk на основе класса JButton 
        buttonOk = new JButton("Сохранить");
        // Вызов метода setBounds объекта buttonOk для установки
        // положения и размеров buttonOk
        buttonOk.setBounds(10, 125, 180, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на buttonOk
        ActionListener actionListenerOk = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                programParameters.serverIP = textIP.getText();
                programParameters.serverPort = textPort.getText();
                programParameters.databaseName = textDB.getText();
                programParameters.databaseUser = textUser.getText();
                programParameters.databasePassword = textPassword.getText();
                programParameters.writeProgramParameters();
                outEventToListener(EVENT_OK);
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        buttonOk.addActionListener(actionListenerOk);
        // Вызов метода add объекта panel, который добавляет
        // buttonLogin на панель
        panel.add(buttonOk);
        // Создание объекта buttonCancel на основе класса JButton 
        buttonCancel = new JButton("Отмена");
        // Вызов метода setBounds объекта buttonCancel для установки
        // положения и размеров М
        buttonCancel.setBounds(200, 125, 100, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на buttonCancel
        ActionListener actionListenerCancel = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
            listener.actionPerformed(new ActionEvent(this, programParametersFormID, actionCommand));
        }
    }
    /*********************************************************
     * 
     *********************************************************/
}
