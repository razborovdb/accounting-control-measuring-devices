
package meter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 * @author Andrey
 */
public class ChangeServiceTable extends JDialog{
    // Хранение прслушивателей события от этого окна
    private ArrayList<ActionListener> actionListeners;
    // Номер текущего окна для идентификации при отправке событий другим объектам
    int serviceTableID = 103;
    // Идентификатор событий от окна Добавить/изменить пользователя
    final int changeUserID = 103;
    // Идентификатор событий от окна Добавить/изменить группу прибора
    final int changeMeterGroupID = 104;
    // Идентификатор событий от окна Добавить/изменить установку
    final int changeProcessID = 105;
    // Идентификатор событий от окна Добавить/изменить тип поверки
    final int changeCalibrTypeID = 106;
    // Номер текущего окна для идентификации при отправке событий другим объектам
    int changeServiceTableID = 201;
    //
    String EVENT_OK_ADD = "EVENT_OK_ADD";
    String EVENT_OK_CHANGE = "EVENT_OK_CHANGE";
    String EVENT_CANCEL = "EVENT_CANCEL";
    //
    final int ADD_TABLE = 1;
    final int CHANGE_TABLE = 2;
    // Переменная хранит количество заголовков (столбцов)
    // в таблице для текущего прибора
    int numMaxParam = 20;
    // Строковый массив для хранения данных
    String[] dataForCurrentTable; 
    // Текущий индекс записи в базе данных
    int indexTable;
    // Переменная определяет тип изменения в базе данных
    // 1 - изменение, 2 - добавление нового
    int editTypeTable;
    // Строковый массив для хранения данных для текущего прибора
    String[] sTableID; 
    // Количество уровней доступа
    int numSecLevel;
    // Определение строкового массива 
    // с уровнями доступа для объекта comboProcess
    String[] secLevel;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве secLevel
    int[] indexSecLevel;
    // Панель для отображения объектов в диалоговом окне
    JPanel panel;
    // Расположение и размер панели
    private int panelX=0,panelY=0,panelWidth,panelHeight;
    // Цвет фона панели
    private Color panelBackground = Color.white;
    // Объявление объекта solidBorder для отображения рамки вокруг текстовых полей
    Border solidBorder;
    //
    JLabel label1;
    JTextArea text1;
    //
    JLabel label2;
    JTextArea text2;
    //
    JLabel label3;
    JTextArea text3;
    //
    JLabel label4;
    JTextArea text4;
    // Объявление объекта labelSecLevel на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Уровень доступа")
    JLabel labelSecLevel;
    // Объявление объекта comboSecLevel на основе класса JComboBox
    // (будет использоваться для выбора уровня доступа)
    JComboBox comboSecLevel;
    //
    ArrayList<JLabel> paramLabel;
    ArrayList<JTextArea> paramText;
    //
    JButton okButton;
    JButton cancelButton;
    //
    String stringForUpdate;
    String stringForAdd;
    /*********************************************************
     *  ChangeServiceTable
     *********************************************************/
    ChangeServiceTable(JFrame owner, int windowWidth, int windowHeight, int sTableID)
    {
        super(owner, "", true);
        //
        serviceTableID = sTableID;
        // Вычисление размеров диалогового окна
        switch(serviceTableID) {
            case changeUserID:
                panelWidth = 560;
                panelHeight = 5*(20+5)+30+20;
                break;
            case changeMeterGroupID:
                panelWidth = 1120;
                panelHeight = 20 *(20+5)+30+20;
                break;
            case changeProcessID:
                panelWidth = 560;
                panelHeight = 1 *(20+5)+30+20;
                break;
            case changeCalibrTypeID:
                panelWidth = 560;
                panelHeight = 1 *(20+5)+30+20;
                break;
        }
        // Установка размера диалогового окна
        setBounds(0,0,panelWidth+20,panelHeight+20);
        // Создание панели для отображения объектов
        panel = new JPanel();
        panel.setBounds(panelX,panelY,panelWidth,panelHeight);
        panel.setBackground(panelBackground);
        panel.setOpaque(true);
        panel.setLayout(null);
        // добавление панели в окне
        add(panel);
        // Создаем объект solidBorder для отображения рамки вокруг текстовых полей
        solidBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
        // Создание объекта label1 
 	label1 = new JLabel("");
        // Установка размеров и положения label1
        label1.setBounds(10,0, 250, 20);
        // label1 добавляется к панели
	panel.add(label1);
        // Создание объекта text1 
 	text1 = new JTextArea("");
        // Установка размеров и положения text1
        text1.setBounds(270,0, 250, 20);
        // Рисование рамки вокруг объекта text1
        text1.setBorder(solidBorder);
        // text1 добавляется к панели
	panel.add(text1);
        // *************************************************************
        // Создание объекта label2 
 	label2 = new JLabel("");
        // Установка размеров и положения label2
        label2.setBounds(10,25, 250, 20);
        // label2 добавляется к панели
	panel.add(label2);
        // Создание объекта text2 
 	text2 = new JTextArea("");
        // Установка размеров и положения text2
        text2.setBounds(270,25, 250, 20);
        // Рисование рамки вокруг объекта text2
        text2.setBorder(solidBorder);
        // text2 добавляется к панели
	panel.add(text2);
        // *************************************************************
        // Создание объекта label3 
 	label3 = new JLabel("");
        // Установка размеров и положения label3
        label3.setBounds(10,50, 250, 20);
        // label3 добавляется к панели
	panel.add(label3);
        // Создание объекта text3 
 	text3 = new JTextArea("");
        // Установка размеров и положения text3
        text3.setBounds(270,50, 250, 20);
        // Рисование рамки вокруг объекта text3
        text3.setBorder(solidBorder);
        // text3 добавляется к панели
	panel.add(text3);
        // *************************************************************
        // Создание объекта label4 
 	label4 = new JLabel("");
        // Установка размеров и положения label4
        label4.setBounds(10,75, 250, 20);
        // label4 добавляется к панели
	panel.add(label4);
        // Создание объекта text4 
 	text4 = new JTextArea("");
        // Установка размеров и положения text4
        text4.setBounds(270,75, 250, 20);
        // Рисование рамки вокруг объекта text4
        text4.setBorder(solidBorder);
        // text4 добавляется к панели
	panel.add(text4);
        // *************************************************************
        // Создание объекта labelSecLevel
 	labelSecLevel = new JLabel("Уровень доступа");
        // Установка размеров и положения labelSecLevel
        labelSecLevel.setBounds(10,100, 250, 20);
        // labelSecLevel добавляется к панели
	panel.add(labelSecLevel);
        // Инициализация строкового массива secLevel
        secLevel = new String[1];
        // Создание объекта comboSecLevel на основе класса JComboBox
        // с вариантами для выбора из строкового массива secLevel
        comboSecLevel = new JComboBox(secLevel);
        // Вызов метода setBounds объекта comboProcess для установки
        // положения и размеров comboProcess
        comboSecLevel.setBounds(270, 100, 250, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboSecLevel на панель
	panel.add(comboSecLevel);       
        //
        if (paramLabel == null) {
            paramLabel = new ArrayList();
        }
        if (paramText == null) {
            paramText = new ArrayList();
        }
        for(int i = 0; i < numMaxParam; i++) {
            // Создание объекта paramLabel 
            paramLabel.add(new JLabel("Заголовок " + Integer.toString(i+1)));
            // Установка размеров и положения paramLabel
            paramLabel.get(i).setBounds(600,25*i, 250, 20);
            // paramLabel добавляется к панели
            panel.add(paramLabel.get(i));
            // Создание объекта paramText 
            paramText.add(new JTextArea("Заголовок " + Integer.toString(i+1)));
            // Установка размеров и положения paramText
            paramText.get(i).setBounds(860,25*i, 250, 20);
            // Рисование рамки вокруг объекта paramText
            paramText.get(i).setBorder(solidBorder);
            // paramText добавляется к панели
            panel.add(paramText.get(i));
        }       
        // ************************************************
 	// Создание кнопки okButton 
 	okButton = new JButton("Изменить");
        // Установка размеров и положения okButton
        okButton.setBounds(10,panel.getHeight() -30 - 10, 100, 30);
        // Добавление прослушивателя события нажатия на okButton
	okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                switch(editTypeTable) {
                    case ADD_TABLE:
                        if(checkParam() || (serviceTableID != changeMeterGroupID)) {
                            getObjectData();
                            outEventToListener(EVENT_OK_ADD);
                        }
                        break;
                    case CHANGE_TABLE:
                        if(checkParam() || (serviceTableID != changeMeterGroupID)) {
                            getObjectData();
                            outEventToListener(EVENT_OK_CHANGE);
                        }
                        break;
                    default:
                        outEventToListener(EVENT_CANCEL);
                        break;
                }
            }
	});
	// Кнопка okButton добавляется к панели
	panel.add(okButton);
        // Создание кнопки cancelButton 
 	cancelButton = new JButton("Отмена");
        // Установка размеров и положения cancelButton
        cancelButton.setBounds(120,panel.getHeight() -30 -10, 100, 30);
        // Добавление прослушивателя события нажатия на cancelButton
	cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
		outEventToListener(EVENT_CANCEL);
            }
	});
	// Кнопка CANCEL добавляется к панели
	panel.add(cancelButton);
        //
        setObjectParam();
    }
    /*********************************************************
     *  setObjectParam
     *********************************************************/
    public void setObjectParam() {
        // Вычисление размеров диалогового окна
        switch(serviceTableID) {
            case changeUserID:
                label1.setText("Фамилия");
                label2.setText("Имя");
                label3.setText("Логин");
                label4.setText("Пароль");
                //
                for(int i=0; i< numMaxParam; i++) {
                    paramLabel.get(i).setVisible(false);
                    paramText.get(i).setVisible(false);
                }
                break;
            case changeMeterGroupID:
                label1.setText("Наименование группы приборов");
                label2.setText("Количество параметров в группе");
                //
                label3.setVisible(false);
                text3.setVisible(false);
                //
                label4.setVisible(false);
                text4.setVisible(false);
                //
                labelSecLevel.setVisible(false);
                comboSecLevel.setVisible(false);
                break;
            case changeProcessID:
                label1.setText("Наименование установки");
                //
                label2.setVisible(false);
                text2.setVisible(false);
                //
                label3.setVisible(false);
                text3.setVisible(false);
                //
                label4.setVisible(false);
                text4.setVisible(false);
                //
                labelSecLevel.setVisible(false);
                comboSecLevel.setVisible(false);
                //
                for(int i=0; i< numMaxParam; i++) {
                    paramLabel.get(i).setVisible(false);
                    paramText.get(i).setVisible(false);
                }
                break;
            case changeCalibrTypeID:
                label1.setText("Наименование типа поверки");
                //
                label2.setVisible(false);
                text2.setVisible(false);
                //
                label3.setVisible(false);
                text3.setVisible(false);
                //
                label4.setVisible(false);
                text4.setVisible(false);
                //
                labelSecLevel.setVisible(false);
                comboSecLevel.setVisible(false);
                //
                for(int i=0; i< numMaxParam; i++) {
                    paramLabel.get(i).setVisible(false);
                    paramText.get(i).setVisible(false);
                }
                break;
        }
    }
    /*********************************************************
     *  checkParam
     *********************************************************/
    public boolean checkParam() {
        boolean result = false;
        int i=0;
        try {
            i = Integer.parseInt(text2.getText());
            result = true;
        }
        catch (NumberFormatException  ex) {
            
        }
        if((serviceTableID == changeMeterGroupID) && (!result))
                JOptionPane.showMessageDialog(panel, "Некорректный ввод в поле Количество параметров в группе");
        return result; 
    }
    /*********************************************************
     *  getObjectData
     *********************************************************/
    public void getObjectData()
    {
        switch(serviceTableID) {
            case changeUserID:
                stringForUpdate = "UPDATE meterusers SET ";
                stringForAdd = "INSERT INTO meterusers VALUES (null, ";
                //
                stringForUpdate = stringForUpdate + "surname = '" + text1.getText() +"' ";
                stringForAdd = stringForAdd + "'" + text1.getText() +"'";
                //
                stringForUpdate = stringForUpdate + ", firstname  = '" + text2.getText() +"' ";
                stringForAdd = stringForAdd + ", '" + text2.getText() +"'";
                //
                stringForUpdate = stringForUpdate + ", login = '" + text3.getText() +"' ";
                stringForAdd = stringForAdd + ", '" + text3.getText() +"'";
                //
                stringForUpdate = stringForUpdate + ", password = '" + text4.getText() +"' ";
                stringForAdd = stringForAdd + ", '" + text4.getText() +"'";
                //
                stringForUpdate = stringForUpdate + ", seclevelID  = '" + 
                        Integer.toString(indexSecLevel[comboSecLevel.getSelectedIndex()]) + "' ";
                stringForAdd = stringForAdd + ", '" + Integer.toString(indexSecLevel[comboSecLevel.getSelectedIndex()]) +"'";
                //
                stringForUpdate = stringForUpdate + " WHERE meterusersID = " + Integer.toString(indexTable+1)+";";
                stringForAdd = stringForAdd + ");";
                break;
            case changeMeterGroupID:
                stringForUpdate = "UPDATE metergroup SET ";
                stringForAdd = "INSERT INTO metergroup VALUES (null, ";
                //
                stringForUpdate = stringForUpdate + "nameMeterGroup = '" + text1.getText() +"' ";
                stringForAdd = stringForAdd + "'" + text1.getText() +"'";
                //
                stringForUpdate = stringForUpdate + ", numGroupParam  = '" + text2.getText() +"' ";
                stringForAdd = stringForAdd + ", '" + text2.getText() +"'";
                //
                for(int i = 0; i < numMaxParam;i++) {
                    stringForUpdate = stringForUpdate + ", head" + Integer.toString(i+1)+ " = '" + paramText.get(i).getText() +"' ";
                    stringForAdd = stringForAdd + ", '" + paramText.get(i).getText() +"'";
                }
                //
                stringForUpdate = stringForUpdate + " WHERE metergroupID = " + Integer.toString(indexTable+1)+";";
                stringForAdd = stringForAdd + ");";
                break;
            case changeProcessID:
                stringForUpdate = "UPDATE process SET ";
                stringForAdd = "INSERT INTO process VALUES (null, ";
                //
                stringForUpdate = stringForUpdate + "nameProcess = '" + text1.getText() +"' ";
                stringForAdd = stringForAdd + "'" + text1.getText() +"'";
                //
                stringForUpdate = stringForUpdate + " WHERE processID = " + Integer.toString(indexTable+1)+";";
                stringForAdd = stringForAdd + ");";
                break;
            case changeCalibrTypeID:
                stringForUpdate = "UPDATE calibrtype SET ";
                stringForAdd = "INSERT INTO calibrtype VALUES (null, ";
                //
                stringForUpdate = stringForUpdate + "nameCalibrType = '" + text1.getText() +"' ";
                stringForAdd = stringForAdd + "'" + text1.getText() +"'";
                //
                stringForUpdate = stringForUpdate + " WHERE calibrtypeID = " + Integer.toString(indexTable+1)+";";
                stringForAdd = stringForAdd + ");";
                break;
        }     
    }
    /*********************************************************
     *  setObjectData
     *********************************************************/
    public void setObjectData()
    {
        String headerString = "";
        String okButtonString = "";
        //
        switch(serviceTableID) {
            case changeUserID:
                switch(editTypeTable) {
                    case ADD_TABLE:
                        headerString = "Добавление пользователя";
                        okButtonString = "Добавить";
                        break;
                    case CHANGE_TABLE:
                        headerString = "Изменение пользователя";
                        okButtonString = "Изменить";
                        break;
                }
                //
                text1.setText(dataForCurrentTable[0]);
                text2.setText(dataForCurrentTable[1]);
                text3.setText(dataForCurrentTable[2]);
                text4.setText(dataForCurrentTable[3]);
                //
                comboSecLevel.setModel(new DefaultComboBoxModel(secLevel));
                comboSecLevel.setBounds(270, 100, 250, 20);
                comboSecLevel.setSelectedItem(dataForCurrentTable[4]);
                break;
            case changeMeterGroupID:
                switch(editTypeTable) {
                    case ADD_TABLE:
                        headerString = "Добавление группы приборов";
                        okButtonString = "Добавить";
                        break;
                    case CHANGE_TABLE:
                        headerString = "Изменение группы приборов";
                        okButtonString = "Изменить";
                        break;
                }
                //
                text1.setText(dataForCurrentTable[0]);
                text2.setText(dataForCurrentTable[1]);
                //
                for(int i = 0; i < numMaxParam;i++) {
                    paramText.get(i).setText(dataForCurrentTable[2+i]);
                }
                break;
            case changeProcessID:
                switch(editTypeTable) {
                    case ADD_TABLE:
                        headerString = "Добавление установки";
                        okButtonString = "Добавить";
                        break;
                    case CHANGE_TABLE:
                        headerString = "Изменение установки";
                        okButtonString = "Изменить";
                        break;
                }
                //
                text1.setText(dataForCurrentTable[0]);
                break;
            case changeCalibrTypeID:
                switch(editTypeTable) {
                    case ADD_TABLE:
                        headerString = "Добавление типа поверки";
                        okButtonString = "Добавить";
                        break;
                    case CHANGE_TABLE:
                        headerString = "Изменение типа поверки";
                        okButtonString = "Изменить";
                        break;
                }
                //
                text1.setText(dataForCurrentTable[0]);
                break;
        }
        //
        setTitle(headerString);
        //
        okButton.setText(okButtonString);
    }
    /*********************************************************
     *  setDataForCurrentTable
     *********************************************************/
    public void setDataForCurrentTable(int sTableID, int editType, int nSecLevel, int numData)
    {
        // тип редактирования
        editTypeTable = editType;
        //
        indexTable = sTableID;
        //
        numSecLevel = nSecLevel;
        if(numSecLevel > 0) {
            // Определение строкового массива 
            // с установками для объекта comboProcess
            secLevel = new String[numSecLevel];
            // Определение массива с индексами в базе данных, соответствующие
            // данным в строковом массиве process
            indexSecLevel = new int[numSecLevel];
        }
        // количество параметров
        if(numData > 0) {
            // Строковый массив для хранения данных для текущего прибора
            dataForCurrentTable = new String[numData]; 
        }
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
            listener.actionPerformed(new ActionEvent(this, changeServiceTableID, actionCommand));
        }
    }
    /*********************************************************
     *  
     *********************************************************/
}
