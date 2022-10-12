
package meter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * @author Andrey
 */
public class ChangeMeter extends JDialog{
    // Хранение прслушивателей события от этого окна
    private ArrayList<ActionListener> actionListeners;
    // Номер текущего окна для идентификации при отправке событий другим объектам
    int changeMeterID = 200;
    //
    String EVENT_OK_ADD = "EVENT_OK_ADD";
    String EVENT_OK_CHANGE = "EVENT_OK_CHANGE";
    String EVENT_CANCEL = "EVENT_CANCEL";
    //
    final int ADD_METER = 1;
    final int CHANGE_METER = 2;
    // Количество постоянных параметров в таблице MeterGroup
    int numConstantParamInnMeterGroup = 10;
    // Максимальное количество параметров в таблице MeterGroup
    int numMaximumParamInnMeterGroup = 30;
    // Переменная хранит количество заголовков (столбцов)
    // в таблице для текущего прибора
    int numHeadForCurrentMeter;
    // Текущий индекс прибора в базе данных
    int indexMeter;
    // Текущий индекс группы прибора в базе данных
    int indexMeterGroup;
    // Переменная определяет тип изменения в базе данных
    // 1 - изменение, 2 - добавление нового
    int editTypeMeter;
    // Строковый массив для хранения заголовков для текущего прибора
    String[] headForCurrentMeter; 
    // Строковый массив для хранения данных для текущего прибора
    String[] dataForCurrentMeter; 
    // Количество установок
    int numProcess;
    // Определение строкового массива 
    // с установками для объекта comboProcess
    String[] process;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве process
    int[] indexProcess;
    // Количество типов поверок
    int numCalibrType;
    // Определение строкового массива 
    // с типами поверок для объекта comboCalibrType
    String[] calibrType;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве calibrType
    int[] indexCalibrType;
    // Панель для отображения объектов в диалоговом окне
    JPanel panel;
    // Расположение и размер панели
    private int panelX=0,panelY=0,panelWidth,panelHeight;
    // Цвет фона панели
    private Color panelBackground = Color.white;
    // Объявление объекта solidBorder для отображения рамки вокруг текстовых полей
    Border solidBorder;
    //
    JLabel labelNameMeter;
    JTextArea textNameMeter;
    //
    JLabel labelMeterGroup;
    JTextArea textMeterGroup;
    // Объявление объекта labelProcess на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Установка")
    JLabel labelProcess;
    // Объявление объекта comboProcess на основе класса JComboBox
    // (будет использоваться для выбора установки)
    JComboBox comboProcess;
    //
    JLabel labelDateIn;
    JTextArea textDateIn;
    //
    JLabel labelPlantNum;
    JTextArea textPlantNum;
    //
    JLabel labelPlantMaker;
    JTextArea textPlantMaker;
    // Объявление объекта labelCalibrType на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Тип поверки")
    JLabel labelCalibrType;
    // Объявление объекта comboCalibrType на основе класса JComboBox
    // (будет использоваться для выбора типа поверки)
    JComboBox comboCalibrType;
    //
    JLabel labelDateLast;
    JTextArea textDateLast;
    //
    JLabel labelCalibrPeriod;
    JTextArea textCalibrPeriod;
    //
    JLabel labelDateNext;
    JTextArea textDateNext;
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
     *  ChangeMeter
     *********************************************************/
    ChangeMeter(JFrame owner, int windowWidth, int windowHeight)
    {
        super(owner, "", true);
        // Вычисление размеров диалогового окна
        panelWidth = 1120;
        int maxRow = (numHeadForCurrentMeter-numConstantParamInnMeterGroup);
        if(numConstantParamInnMeterGroup > maxRow)
            maxRow = numConstantParamInnMeterGroup;
        panelHeight = maxRow*(20+5)+30+20;
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
        // Создание объекта labelNameMeter 
 	labelNameMeter = new JLabel("Прибор");
        // Установка размеров и положения labelNameMeter
        labelNameMeter.setBounds(10,0, 250, 20);
        // labelNameMeter добавляется к панели
	panel.add(labelNameMeter);
        // Создание объекта textNameMeter 
 	textNameMeter = new JTextArea("Прибор");
        // Установка размеров и положения textNameMeter
        textNameMeter.setBounds(270,0, 250, 20);
        // Рисование рамки вокруг объекта textNameMeter
        textNameMeter.setBorder(solidBorder);
        // textNameMeter добавляется к панели
	panel.add(textNameMeter);
        // ************************************************
        // Создание объекта labelMeterGroup
 	labelMeterGroup = new JLabel("Группа прибора");
        // Установка размеров и положения labelMeterGroup
        labelMeterGroup.setBounds(10,25, 250, 20);
        // labelMeterGroup добавляется к панели
	panel.add(labelMeterGroup);
        // Создание объекта textNameMeter 
 	textMeterGroup = new JTextArea("Прибор");
        //
        textMeterGroup.setEditable(false);
        // Установка размеров и положения textMeterGroup
        textMeterGroup.setBounds(270,25, 250, 20);
        // Рисование рамки вокруг объекта textMeterGroup
        textMeterGroup.setBorder(solidBorder);
        // textMeterGroup добавляется к панели
	panel.add(textMeterGroup);
        // *************************************************************
        // Создание объекта labelProcess
 	labelProcess = new JLabel("Установка");
        // Установка размеров и положения labelMeterGroup
        labelProcess.setBounds(10,50, 250, 20);
        // labelProcess добавляется к панели
	panel.add(labelProcess);
        // Инициализация строкового массива process
        process = new String[1];
        // Создание объекта comboProcess на основе класса JComboBox
        // с вариантами для выбора из строкового массива process
        comboProcess = new JComboBox(process);
        // Вызов метода setBounds объекта comboProcess для установки
        // положения и размеров comboProcess
        comboProcess.setBounds(270, 50, 250, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboProcess на панель
	panel.add(comboProcess);       
        // *************************************************************
        // Создание объекта labelDateIn 
 	labelDateIn = new JLabel("Дата ввода");
        // Установка размеров и положения labelDateIn
        labelDateIn.setBounds(10,75, 250, 20);
        // labelDateIn добавляется к панели
	panel.add(labelDateIn);
        // Создание объекта textDateIn 
 	textDateIn = new JTextArea("01.01.2016");
        // Установка размеров и положения textDateIn
        textDateIn.setBounds(270,75, 250, 20);
        // Рисование рамки вокруг объекта textDateIn
        textDateIn.setBorder(solidBorder);
        // textNameMeter добавляется к панели
	panel.add(textDateIn);
        // *************************************************************
        // Создание объекта labelPlantNum 
 	labelPlantNum = new JLabel("Завод");
        // Установка размеров и положения labelPlantNum
        labelPlantNum.setBounds(10,100, 250, 20);
        // labelPlantNum добавляется к панели
	panel.add(labelPlantNum);
        // Создание объекта textPlantNum 
 	textPlantNum = new JTextArea("2016");
        // Установка размеров и положения textPlantNum
        textPlantNum.setBounds(270,100, 250, 20);
        // Рисование рамки вокруг объекта textPlantNum
        textPlantNum.setBorder(solidBorder);
        // textNameMeter добавляется к панели
	panel.add(textPlantNum);
        // *************************************************************
        // Создание объекта labelPlantMaker 
 	labelPlantMaker = new JLabel("Завод");
        // Установка размеров и положения labelPlantMaker
        labelPlantMaker.setBounds(10,125, 250, 20);
        // labelPlantMaker добавляется к панели
	panel.add(labelPlantMaker);
        // Создание объекта textPlantMaker 
 	textPlantMaker = new JTextArea("2016");
        // Установка размеров и положения textPlantMaker
        textPlantMaker.setBounds(270,125, 250, 20);
        // Рисование рамки вокруг объекта textPlantMaker
        textPlantMaker.setBorder(solidBorder);
        // textNameMeter добавляется к панели
	panel.add(textPlantMaker);
        // *************************************************************
        // Создание объекта labelProcess
 	labelCalibrType = new JLabel("Тип поверки");
        // Установка размеров и положения labelCalibrType
        labelCalibrType.setBounds(10,150, 250, 20);
        // labelCalibrType добавляется к панели
	panel.add(labelCalibrType);
        // Инициализация строкового массива calibrType
        calibrType = new String[1];
        // Создание объекта comboCalibrType на основе класса JComboBox
        // с вариантами для выбора из строкового массива process
        comboCalibrType = new JComboBox(calibrType);
        // Вызов метода setBounds объекта comboCalibrType для установки
        // положения и размеров comboCalibrType
        comboCalibrType.setBounds(270, 150, 250, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboProcess на панель
	panel.add(comboCalibrType);
        // *************************************************************
        // Создание объекта labelDateLast 
 	labelDateLast = new JLabel("Дата ");
        // Установка размеров и положения labelDateLast
        labelDateLast.setBounds(10,175, 250, 20);
        // labelDateLast добавляется к панели
	panel.add(labelDateLast);
        // Создание объекта textDateLast 
 	textDateLast = new JTextArea("01.01.2016");
        // Установка размеров и положения textDateLast
        textDateLast.setBounds(270,175, 250, 20);
        // Рисование рамки вокруг объекта textDateLast
        textDateLast.setBorder(solidBorder);
        //
        textDateLast.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                calcDate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calcDate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calcDate();
            }
            // **********************************************
            public void calcDate() {
                boolean result = true;
                int period;
                int Year;
                int Month;
                int Day;
                Date date1;
                Date date2;
                Calendar date;
                //
                SimpleDateFormat dateformatJava = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat dateformatYear = new SimpleDateFormat("yyyy");
                SimpleDateFormat dateformatMonth = new SimpleDateFormat("MM");
                SimpleDateFormat dateformatDay = new SimpleDateFormat("dd");
                //
                try {
                    date1 = dateformatJava.parse(textDateLast.getText());
                    result = true;
                }
                catch (ParseException ex) {
                    //JOptionPane.showMessageDialog(panel, "Некорректный ввод даты в поле Дата последней поверки");
                }
                //
                if(result) {
                    result = false;
                    try {
                        period = Integer.parseInt(textCalibrPeriod.getText());
                        result = true;
                    }
                    catch (NumberFormatException  ex) {
                        //JOptionPane.showMessageDialog(panel, "Некорректный ввод в поле Периодичность поверки, год");
                    }
                }
                //
                if(result) {
                    // Добавление к последней дате поверки Периода поверки
                    try {
                        if(!textDateLast.getText().equals(dataForCurrentMeter[7])) {
                            dataForCurrentMeter[7] = textDateLast.getText();
                            date1 = dateformatJava.parse(textDateLast.getText());
                            Year = Integer.parseInt(dateformatYear.format(date1));
                            Month = Integer.parseInt(dateformatMonth.format(date1))-1;
                            Day = Integer.parseInt(dateformatDay.format(date1));
                            date = new GregorianCalendar(Year, Month, Day);
                            date.add(Calendar.YEAR, 1);
                            String strDay = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
                            if(strDay.length()==1) strDay = "0" + strDay;
                            String strMonth = Integer.toString(date.get(Calendar.MONTH)+1);
                            if(strMonth.length()==1) strMonth = "0" + strMonth;
                            String strYear = Integer.toString(date.get(Calendar.YEAR));
                            textDateNext.setText(strDay+"."+strMonth+"."+strYear);
                        }
                    }
                    catch (ParseException ex) {
                        //JOptionPane.showMessageDialog(panel, "Некорректный ввод даты в поле Дата последней поверки");
                    }
                }
            }
        });
        // textNameMeter добавляется к панели
	panel.add(textDateLast);
        // *************************************************************
        // Создание объекта labelCalibrPeriod 
 	labelCalibrPeriod = new JLabel("Дата ");
        // Установка размеров и положения labelCalibrPeriod
        labelCalibrPeriod.setBounds(10,200, 250, 20);
        // labelCalibrPeriod добавляется к панели
	panel.add(labelCalibrPeriod);
        // Создание объекта textCalibrPeriod 
 	textCalibrPeriod = new JTextArea("01.01.2016");
        // Установка размеров и положения textCalibrPeriod
        textCalibrPeriod.setBounds(270,200, 250, 20);
        // Рисование рамки вокруг объекта textCalibrPeriod
        textCalibrPeriod.setBorder(solidBorder);
        // textNameMeter добавляется к панели
	panel.add(textCalibrPeriod);
        // *************************************************************
        // Создание объекта labelDateNext
 	labelDateNext = new JLabel("Дата ");
        // Установка размеров и положения labelDateNext
        labelDateNext.setBounds(10,225, 250, 20);
        // labelDateNext добавляется к панели
	panel.add(labelDateNext);
        // Создание объекта textDateNext 
 	textDateNext = new JTextArea("01.01.2016");
        // Установка размеров и положения textDateNext
        textDateNext.setBounds(270,225, 250, 20);
        // Рисование рамки вокруг объекта textDateNext
        textDateNext.setBorder(solidBorder);
        // textNameMeter добавляется к панели
	panel.add(textDateNext);
        //
        if (paramLabel == null) {
            paramLabel = new ArrayList();
        }
        if (paramText == null) {
            paramText = new ArrayList();
        }
        for(int i =0; i< (numMaximumParamInnMeterGroup-numConstantParamInnMeterGroup);i++ ) {
            // Создание объекта paramLabel 
            paramLabel.add(new JLabel("param"));
            // Установка размеров и положения paramLabel
            paramLabel.get(i).setBounds(600,25*i, 250, 20);
            // paramLabel добавляется к панели
            panel.add(paramLabel.get(i));
            // Создание объекта paramText 
            paramText.add(new JTextArea("param"));
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
                switch(editTypeMeter) {
                    case ADD_METER:
                        if(checkData()) {
                            getObjectData();
                            outEventToListener(EVENT_OK_ADD);
                        }
                        break;
                    case CHANGE_METER:
                        if(checkData()) {
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
    }
    /*********************************************************
     *  checkData
     *********************************************************/
    public boolean checkData() {
        boolean result = false;
        SimpleDateFormat dateformatJava = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateformatMySQL = new SimpleDateFormat("yyyy-MM-dd");
        //
        try {
            String strDate = dateformatMySQL.format(dateformatJava.parse(textDateIn.getText()));
            result = true;
        }
        catch (ParseException ex) {
            JOptionPane.showMessageDialog(panel, "Некорректный ввод даты в поле Дата ввода прибора");
        }
        //
        if(result) {
            result = false;
            try {
                String strDate = dateformatMySQL.format(dateformatJava.parse(textDateLast.getText()));
                result = true;
            }
            catch (ParseException ex) {
                JOptionPane.showMessageDialog(panel, "Некорректный ввод даты в поле Дата последней поверки");
            }
        }
        //
        if(result) {
            result = false;
            try {
                String strDate = dateformatMySQL.format(dateformatJava.parse(textDateNext.getText()));
                result = true;
            }
            catch (ParseException ex) {
                JOptionPane.showMessageDialog(panel, "Некорректный ввод даты в поле Дата следующей поверки");
            }
        }
        //
        if(result) {
            result = false;
            try {
                int intData = Integer.parseInt(textCalibrPeriod.getText());
                result = true;
            }
            catch (NumberFormatException  ex) {
                JOptionPane.showMessageDialog(panel, "Некорректный ввод в поле Периодичность поверки, год");
            }
        }
        //
        return result;
    }
    /*********************************************************
     *  getObjectData
     *********************************************************/
    public void getObjectData()
    {
        SimpleDateFormat dateformatJava = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateformatMySQL = new SimpleDateFormat("yyyy-MM-dd");
        //
        stringForUpdate = "UPDATE meter SET ";
        stringForAdd = "INSERT INTO meter VALUES (null, ";
        //
        stringForUpdate = stringForUpdate + "nameMeter = '" + textNameMeter.getText() +"' ";
        stringForAdd = stringForAdd + "'" + textNameMeter.getText() +"'";
        //
        stringForUpdate = stringForUpdate + ", meterGroupID = '" + Integer.toString(indexMeterGroup) + "' ";
        stringForAdd = stringForAdd + ", '" + Integer.toString(indexMeterGroup) +"'";
        //
        stringForUpdate = stringForUpdate + ", processID = '" + 
                Integer.toString(indexProcess[comboProcess.getSelectedIndex()]) + "' ";
        stringForAdd = stringForAdd + ", '" + Integer.toString(indexProcess[comboProcess.getSelectedIndex()]) +"'";
        try {
            String strDate = dateformatMySQL.format(dateformatJava.parse(textDateIn.getText()));
            stringForUpdate = stringForUpdate + ", dateIn = '" + strDate + "' ";
            stringForAdd = stringForAdd + ", '" + strDate +"'";
        }
        catch (ParseException ex) {
            
        }
        //
        stringForUpdate = stringForUpdate + ", plantNum = '" + textPlantNum.getText() +"' ";
        stringForAdd = stringForAdd + ", '" + textPlantNum.getText() +"'";
        //
        stringForUpdate = stringForUpdate + ", plantMaker = '" + textPlantMaker.getText() +"' ";
        stringForAdd = stringForAdd + ", '" + textPlantMaker.getText() +"'";
        //
        stringForUpdate = stringForUpdate + ", calibrTypeID = '" + 
                Integer.toString(indexCalibrType[comboCalibrType.getSelectedIndex()]) + "' ";
        stringForAdd = stringForAdd + ", '" + Integer.toString(indexCalibrType[comboCalibrType.getSelectedIndex()]) +"'";
        //
        try {
            String strDate = dateformatMySQL.format(dateformatJava.parse(textDateLast.getText()));
            stringForUpdate = stringForUpdate + ", dateLast = '" + strDate + "' ";
            stringForAdd = stringForAdd + ", '" + strDate +"'";
        }
        catch (ParseException ex) {
            
        }
        //
        stringForUpdate = stringForUpdate + ", calibrPeriod = '" + textCalibrPeriod.getText() +"' ";
        stringForAdd = stringForAdd + ", '" + textCalibrPeriod.getText() +"'";
        //
        try {
            String strDate = dateformatMySQL.format(dateformatJava.parse(textDateNext.getText()));
            stringForUpdate = stringForUpdate + ", dateNext = '" + strDate + "' ";
            stringForAdd = stringForAdd + ", '" + strDate +"'";
        }
        catch (ParseException ex) {
            
        }
        //
        for(int i = 0; i<(numHeadForCurrentMeter-numConstantParamInnMeterGroup);i++) {
            stringForUpdate = stringForUpdate + ", param" + Integer.toString(i+1)+ " = '" + paramText.get(i).getText() +"' ";
            stringForAdd = stringForAdd + ", '" + paramText.get(i).getText() +"'";
        }
        //
        for(int i = (numHeadForCurrentMeter-numConstantParamInnMeterGroup); 
                i<(numMaximumParamInnMeterGroup-numConstantParamInnMeterGroup);i++) {
            stringForAdd = stringForAdd + ", 'h" + Integer.toString(i+1) +"'";
        }
        //
        stringForUpdate = stringForUpdate + " WHERE meterID = " + Integer.toString(indexMeter)+";";
        stringForAdd = stringForAdd + ");";
    }
    /*********************************************************
     *  setObjectData
     *********************************************************/
    public void setObjectData()
    {
        String headerString = "";
        String okButtonString = "";
        // Вычисление размеров диалогового окна
        panelWidth = 1120;
        int maxRow = (numHeadForCurrentMeter-numConstantParamInnMeterGroup);
        if(numConstantParamInnMeterGroup > maxRow)
            maxRow = numConstantParamInnMeterGroup;
        panelHeight = maxRow*(20+5)+30+20;
        // Установка размера диалогового окна
        setBounds(0,0,panelWidth+20,panelHeight+20);
        // Установка размеров панели
        panel.setBounds(panelX,panelY,panelWidth,panelHeight);
        //
        switch(editTypeMeter) {
            case ADD_METER:
                headerString = "Добавление прибора";
                okButtonString = "Добавить";
                break;
            case CHANGE_METER:
                headerString = "Изменение параметров прибора";
                okButtonString = "Изменить";
                break;
        }
        setTitle(headerString);
        //
        labelNameMeter.setText(headForCurrentMeter[0]);
        textNameMeter.setText(dataForCurrentMeter[0]);
        //
        labelMeterGroup.setText(headForCurrentMeter[1]);
        textMeterGroup.setText(dataForCurrentMeter[1]);
        //
        labelProcess.setText(headForCurrentMeter[2]);
        comboProcess.setModel(new DefaultComboBoxModel(process));
        comboProcess.setBounds(270, 50, 250, 20);
        comboProcess.setSelectedItem(dataForCurrentMeter[2]);
        //
        labelDateIn.setText(headForCurrentMeter[3]);
        textDateIn.setText(dataForCurrentMeter[3]);
        //
        labelPlantNum.setText(headForCurrentMeter[4]);
        textPlantNum.setText(dataForCurrentMeter[4]);
        //
        labelPlantMaker.setText(headForCurrentMeter[5]);
        textPlantMaker.setText(dataForCurrentMeter[5]);
        //
        labelCalibrType.setText(headForCurrentMeter[6]);
        comboCalibrType.setModel(new DefaultComboBoxModel(calibrType));
        comboCalibrType.setBounds(270, 150, 250, 20);
        comboCalibrType.setSelectedItem(dataForCurrentMeter[6]);
        //
        labelDateLast.setText(headForCurrentMeter[7]);
        textDateLast.setText(dataForCurrentMeter[7]);
        //
        labelCalibrPeriod.setText(headForCurrentMeter[8]);
        textCalibrPeriod.setText(dataForCurrentMeter[8]);
        //
        labelDateNext.setText(headForCurrentMeter[9]);
        textDateNext.setText(dataForCurrentMeter[9]);
        //
        for(int i = 0; i<(numHeadForCurrentMeter-numConstantParamInnMeterGroup);i++) {
            paramLabel.get(i).setVisible(true);
            paramLabel.get(i).setText(headForCurrentMeter[10+i]);
            paramText.get(i).setVisible(true);
            paramText.get(i).setText(dataForCurrentMeter[10+i]);
        }
        for(int i = (numHeadForCurrentMeter-numConstantParamInnMeterGroup); i<(numMaximumParamInnMeterGroup-numConstantParamInnMeterGroup);i++) {
            paramLabel.get(i).setVisible(false);
            paramText.get(i).setVisible(false);
        }
        //
        okButton.setText(okButtonString);
        // Установка размеров и положения okButton
        okButton.setBounds(10,panel.getHeight() -30 - 10, 100, 30);
        // Установка размеров и положения cancelButton
        cancelButton.setBounds(120,panel.getHeight() -30 -10, 100, 30);
    }
    /*********************************************************
     *  setDataForCurrentMeter
     *********************************************************/
    public void setDataForCurrentMeter(String winHeader, int indMeter, int indMeterGroup, int nHeadForCurrentMeter, int editType,
            int nProcess, int nCalibrType)
    {
        this.setTitle(winHeader);
        // 
        indexMeter = indMeter;
        indexMeterGroup = indMeterGroup;
        //
        // количество параметров для прибора
        numHeadForCurrentMeter = nHeadForCurrentMeter;
        if(nHeadForCurrentMeter > 0) {
            // Строковый массив для хранения заголовков для текущего прибора
            headForCurrentMeter = new String[nHeadForCurrentMeter]; 
            // Строковый массив для хранения данных для текущего прибора
            dataForCurrentMeter = new String[nHeadForCurrentMeter]; 
        }
        // тип редактирования
        editTypeMeter = editType;
        //
        numProcess = nProcess;
        if(numProcess > 0) {
            // Определение строкового массива 
            // с установками для объекта comboProcess
            process = new String[numProcess];
            // Определение массива с индексами в базе данных, соответствующие
            // данным в строковом массиве process
            indexProcess = new int[numProcess];
        }
        // Количество типов поверок
        numCalibrType = nCalibrType;
        if(numCalibrType > 0) {
            // Определение строкового массива 
            // с типами поверок для объекта comboCalibrType
            calibrType = new String[numCalibrType];
            // Определение массива с индексами в базе данных, соответствующие
            // данным в строковом массиве calibrType
            indexCalibrType = new int[numCalibrType];
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
            listener.actionPerformed(new ActionEvent(this, changeMeterID, actionCommand));
        }
    }
    /*********************************************************
     *  
     *********************************************************/
}
