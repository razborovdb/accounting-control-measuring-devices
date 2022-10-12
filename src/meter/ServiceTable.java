
package meter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/**
 * @author Andrey
 */
public class ServiceTable extends JFrame implements ActionListener {
    // Текущее окно
    JFrame thisFrame = this;
    // Текущий уровень доступа
    String currentSecLevel;
    // Объявление объекта connectToMySQL на основе класса JavaToMySQL
    // будет использоваться для получения данных из базы данных
    JavaToMySQL connectToMySQL;
    // Панель для отображения объектов на форме
    JPanel panel;
    //
    int ADD_TABLE = 1;
    int CHANGE_TABLE = 2;
    // Расположение и размер панели
    private final int panelX=0,panelY=0,panelWidth,panelHeight;
    // Цвет фона панели
    private Color panelBackground = Color.white;
    // Определение строкового массива 
    // с уровнями доступа 
    String[] secLevel;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве securityLevel
    int[] indexSecLevel;
    // Определение строкового массива 
    // с группами приборов 
    String[] meterGroup;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве meterGroup
    int[] indexMeterGroup;
    // Определение строкового массива 
    // с установками для объекта comboProcess
    String[] process;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве process
    int[] indexProcess;
    // Определение строкового массива 
    // с типами поверок для объекта comboCalibrType
    String[] calibrType;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве calibrType
    int[] indexCalibrType;
    // Переменная хранит количество заголовков (столбцов)
    // в таблице для выбранной группы приборов
    int numHeadForCurrentTable;
    // Строковый массив для хранения заголовков для текущей группы приборов
    String[] headForCurrentTable; 
    // Переменная хранит количество строк
    // в таблице 
    int numRowForCurrentTable;
    // Строковый массив для хранения данных для текущей таблицы
    String[][] dataForCurrentTable; 
    // Массив для хранения индекса параметра в таблице
    int indexDataTable[];
    // панель прокрутки для размещения таблицы
    JScrollPane tableScrollPane;
    // Таблица для отображения приборов
    JTable serviceTable;
    // Кнопка для добавления в базу данных
    JButton buttonAdd;
    // Кнопка для удаления из базы данных
    JButton buttonRemove;
    // Кнопка для записи данных таблицы в файл с расширением csv
    // для возможности дальнейшей обработки в Excell
    JButton saveCSV;
    // Кнопка для закрытия окна
    JButton closeButton;
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
    // Номер диалогового окна ChangeServiceTable для идентификации 
    final int changeServiceTableID = 201;
    // Сообщения при обмене между окнами
    final String EVENT_OK_ADD = "EVENT_OK_ADD";
    final String EVENT_OK_CHANGE = "EVENT_OK_CHANGE";
    final String EVENT_CANCEL = "EVENT_CANCEL";
    //
    final String EVENT_CLOSE = "EVENT_CLOSE";
    //
    int ADD_ROW = 1;
    int CHANGE_ROW = 2;
    //
    String STRING_ADD = "Добавление ";
    String STRING_CHANGE = "Изменение ";
    // Объект для изменения/добавления 
    ChangeServiceTable changeServiceTable;
    /*********************************************************
     *  MeterInformation конструктор
     *********************************************************/
    ServiceTable(int sTableID, String formTitle, int windowWidth, int windowHeight) {
        // Вывод заголовка окна
        super(formTitle);
        //
        serviceTableID = sTableID;
        // Определение параметров панели
        panelWidth = windowWidth;
        panelHeight = windowHeight;
        // установка операции по умолчанию при закрытии окна
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // Создание панели для отображения объектов
        panel = new JPanel();
        panel.setBounds(panelX,panelY,panelWidth-20,panelHeight-50);
        panel.setBackground(panelBackground);
        panel.setOpaque(true);
        panel.setLayout(null);
        // добавление панели в окне
        add(panel);
        // Создание объекта connectToMySQL для получения
        // данных из базы данных MySQL
        connectToMySQL = new JavaToMySQL();
        // ******************************************************
        // Получение данных из базы данных об уровнях доступа
        connectToMySQL.readFromSecLevel();
        if(connectToMySQL.rsSecLevel !=null) {
            if(connectToMySQL.numSecLevel > 0) {
                secLevel = new String[connectToMySQL.numSecLevel];
                indexSecLevel = new int[connectToMySQL.numSecLevel];
                System.arraycopy(connectToMySQL.rsSecLevel, 0, secLevel, 0, connectToMySQL.numSecLevel);
                System.arraycopy(connectToMySQL.indexSecLevel, 0, indexSecLevel, 0, connectToMySQL.numSecLevel);
            }
            else {
                secLevel = new String[1];
                indexSecLevel = new int[1];
                secLevel[0] = "Нет";
                indexSecLevel[0] = 0;
            }
        }
        else {
            secLevel = new String[1];
            indexSecLevel = new int[1];
            secLevel[0] = "Нет";
            indexSecLevel[0] = 0;
        }
        // ***********************************************************************
        drawTable();
        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
            // Создание таблицы meterTable на базе класса JTable
            serviceTable = new JTable();
            // Создание модели таблицы с данными в строковом массиве dataForCurrentMeterGroup
            // и заголовками в строковом массиве headForCurrentMeterGroup
            serviceTable.setModel(new DefaultTableModel(dataForCurrentTable,headForCurrentTable) {
                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                @Override   
                public boolean isCellEditable(int row, int column){   
                    return false;   
                }; 
            });
            // Добавление прослушивателя нажатий кнопок мыши
            serviceTable.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {
                        if(isEnableEdit(currentSecLevel)) {
                            changeServiceTable.setResizable(false);
                            setParametersForChange();
                            changeServiceTable.setObjectData();
                            changeServiceTable.setLocationRelativeTo(null);
                            changeServiceTable.setVisible(true);
                        }
                        else {
                            JOptionPane.showMessageDialog(panel, "Данный уровень доступа не позволяет редактировать базу данных");
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    
                }
                
            });
            // Создание объекта tableScrollPane на базе класса JScrollPane
            // и размещение в нем таблицы serviceTable
            tableScrollPane = new JScrollPane(serviceTable);
            // Вызов метода setBounds объекта tableScrollPane для установки
            // положения и размеров tableScrollPane
            tableScrollPane.setBounds(10, 10, panel.getWidth()-20, panel.getHeight() - 50);  
            // определение использвания полос прокрутки в tableScrollPane
            // по мере необходимости (AS_NEEDED)
            tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            //
            panel.add(tableScrollPane);
        //}
        // Создание объекта buttonAdd на основе класса JButton 
        buttonAdd = new JButton("Добавить ");
        // Вызов метода setBounds объекта buttonAdd для установки
        // положения и размеров buttonAdd
        buttonAdd.setBounds(10, tableScrollPane.getY() + tableScrollPane.getHeight() + 10, 200, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на closeButton
        ActionListener actionListenerAdd = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isEnableEdit(currentSecLevel)) {
                    changeServiceTable.setResizable(false);
                    setParametersForAdd();
                    changeServiceTable.setObjectData();
                    changeServiceTable.setLocationRelativeTo(null);
                    changeServiceTable.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(panel, "Данный уровень доступа не позволяет редактировать базу данных");
                }
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        buttonAdd.addActionListener(actionListenerAdd);
        // Вызов метода add объекта panel, который добавляет
        // buttonAdd на панель
        panel.add(buttonAdd);
        
        // Создание объекта buttonRemove на основе класса JButton 
        buttonRemove = new JButton("Удалить ");
        // Вызов метода setBounds объекта buttonRemove для установки
        // положения и размеров buttonRemove
        buttonRemove.setBounds(220, tableScrollPane.getY() + tableScrollPane.getHeight() + 10, 200, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на closeButton
        ActionListener actionListenerRemove = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isEnableEdit(currentSecLevel)) {
                    // Изменение параметров подключения к базе данных
                    connectToMySQL.changeConnectParameters();
                    // Удаление прибора
                    if( serviceTable.getSelectedRow() >=0 ) {
                        String removeString = "";
                        switch(serviceTableID) {
                            case changeUserID:
                                removeString = "DELETE FROM meterusers WHERE meterusersid = " + 
                                        Integer.toString(indexDataTable[serviceTable.getSelectedRow()])+ "; ";
                                break;
                            case changeMeterGroupID:
                                removeString = "DELETE FROM metergroup WHERE metergroupid = " + 
                                        Integer.toString(indexDataTable[serviceTable.getSelectedRow()])+ "; ";
                                break;
                            case changeProcessID:
                                removeString = "DELETE FROM process WHERE processid = " + 
                                        Integer.toString(indexDataTable[serviceTable.getSelectedRow()])+ "; ";
                                break;
                            case changeCalibrTypeID:
                                removeString = "DELETE FROM calibrType WHERE calibrtypeid = " + 
                                        Integer.toString(indexDataTable[serviceTable.getSelectedRow()])+ "; ";
                                break;
                        }
                        if(!removeString.equals("")) {
                            if(connectToMySQL.removeFromTable(removeString))
                                JOptionPane.showMessageDialog(panel, "Данные удалены успешно");
                            else JOptionPane.showMessageDialog(panel, "Ошибка при удалении данных");
                        }
                        //
                        drawTable();
                        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                            // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                            // и заголовками в строковом массиве headForCurrentMeterGroup
                            serviceTable.setModel(new DefaultTableModel(dataForCurrentTable,headForCurrentTable) {
                                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                                @Override   
                                public boolean isCellEditable(int row, int column){   
                                    return false;   
                                }; 
                            });
                        //}
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "Не выбрана запись для удаления");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(panel, "Данный уровень доступа не позволяет редактировать базу данных");
                }
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        buttonRemove.addActionListener(actionListenerRemove);
        // Вызов метода add объекта panel, который добавляет
        // buttonRemove на панель
        panel.add(buttonRemove);
        // Создание объекта saveCSV на основе класса JButton 
        saveCSV = new JButton("Сохранить в *.csv");
        // Вызов метода setBounds объекта saveCSV для установки
        // положения и размеров saveCSV
        saveCSV.setBounds(430, tableScrollPane.getY() + tableScrollPane.getHeight() + 10, 200, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на saveCSV
        ActionListener actionListenerSaveCSV = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Вызов диалога сохранения таблицы в файл *.csv
                JFileChooser fc = new JFileChooser();
                //
                if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
                    try ( FileWriter fw = new FileWriter(fc.getSelectedFile()) ) {
                        if(connectToMySQL.numRowsInTable > 0) { 
                            String tableRow = "";
                            // Запись в файл заголовков
                            for(int i=0;i<headForCurrentTable.length;i++) {
                                tableRow = tableRow+headForCurrentTable[i];
                                if(i != (headForCurrentTable.length-1))
                                    tableRow = tableRow+";";
                                else tableRow = tableRow+"\n";
                            }
                            fw.write(tableRow);
                            // Запись в файл данных
                            for(int j=0;j<connectToMySQL.numRowsInTable;j++) {
                                tableRow = "";
                                for(int i=0;i<headForCurrentTable.length;i++) {
                                    tableRow = tableRow+dataForCurrentTable[j][i];
                                    if(i != (headForCurrentTable.length-1))
                                        tableRow = tableRow+";";
                                    else tableRow = tableRow+"\n";
                                }
                                //
                                fw.write(tableRow);
                            }
                        }
                        // Выдача сообщения об успешном сохранении файла
                        JOptionPane.showMessageDialog(panel, "Файл сохранен успешно");
                    }
                    catch ( IOException ioe ) {
                        //System.out.println("Error");
                    }
                }
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        saveCSV.addActionListener(actionListenerSaveCSV);
        // Вызов метода add объекта panel, который добавляет
        // saveCSV на панель
        panel.add(saveCSV);
        // Создание объекта closeButton на основе класса JButton 
        closeButton = new JButton("Закрыть");
        // Вызов метода setBounds объекта closeButton для установки
        // положения и размеров closeButton
        closeButton.setBounds(640, tableScrollPane.getY() + tableScrollPane.getHeight() + 10, 100, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на closeButton
        ActionListener actionListenerClose = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                outEventToListener(EVENT_CLOSE);
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        closeButton.addActionListener(actionListenerClose);
        // Вызов метода add объекта panel, который добавляет
        // closeButton на панель
        panel.add(closeButton);
        // Инициализация диалогового окна для изменения параметров Прибора
        changeServiceTable = new ChangeServiceTable(thisFrame, panelWidth, panelHeight, serviceTableID);
        changeServiceTable.addListener(this);
        changeServiceTable.dispose();
    }
    /*********************************************************
     *  setParametersForAdd
     *********************************************************/
    public void setParametersForAdd() {
        //
        switch(serviceTableID) {
            case changeUserID:
                changeServiceTable.setDataForCurrentTable(0, ADD_TABLE, secLevel.length,  5);
                //
                for(int j=0;j<5;j++) {
                    changeServiceTable.dataForCurrentTable[j] = "";
                }
                //
                changeServiceTable.dataForCurrentTable[4] = "Контролер";
                //
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
            case changeMeterGroupID:
                changeServiceTable.setDataForCurrentTable(0, ADD_TABLE, secLevel.length,  22);
                //
                for(int j=0;j<22;j++) {
                    changeServiceTable.dataForCurrentTable[j] = "";
                }
                //
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
            case changeProcessID:
                changeServiceTable.setDataForCurrentTable(0, ADD_TABLE, secLevel.length,  1);
                //
                for(int j=0;j<1;j++) {
                    changeServiceTable.dataForCurrentTable[j] = "";
                }
                //
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
            case changeCalibrTypeID:
                changeServiceTable.setDataForCurrentTable(0, ADD_TABLE, secLevel.length,  1);
                //
                for(int j=0;j<1;j++) {
                    changeServiceTable.dataForCurrentTable[j] = "";
                }
                //
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
        }
    }
    /*********************************************************
     *  setParametersForChange
     *********************************************************/
    public void setParametersForChange() {
        int i = serviceTable.getSelectedRow();
        if(i<0) i = 0;
        switch(serviceTableID) {
            case changeUserID:
                changeServiceTable.setDataForCurrentTable(i, CHANGE_TABLE, secLevel.length,  5);
                for(int j=0;j<5;j++) {
                    changeServiceTable.dataForCurrentTable[j] = dataForCurrentTable[i][j];
                }
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
            case changeMeterGroupID:
                changeServiceTable.setDataForCurrentTable(i, CHANGE_TABLE, secLevel.length,  22);
                for(int j=0;j<22;j++) {
                    changeServiceTable.dataForCurrentTable[j] = dataForCurrentTable[i][j];
                }
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
            case changeProcessID:
                changeServiceTable.setDataForCurrentTable(i, CHANGE_TABLE, secLevel.length,  1);
                for(int j=0;j<1;j++) {
                    changeServiceTable.dataForCurrentTable[j] = dataForCurrentTable[i][j];
                }
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
            case changeCalibrTypeID:
                changeServiceTable.setDataForCurrentTable(i, CHANGE_TABLE, secLevel.length,  1);
                for(int j=0;j<1;j++) {
                    changeServiceTable.dataForCurrentTable[j] = dataForCurrentTable[i][j];
                }
                if( (secLevel.length) > 0 ) {
                    for(int j=0;j<(secLevel.length);j++) {
                        changeServiceTable.secLevel[j] = secLevel[j];
                        changeServiceTable.indexSecLevel[j] = indexSecLevel[j];
                    }
                }
                break;
        }
    }
    /*********************************************************
     *  drawTable
     *********************************************************/
    public void drawTable() {
        // Строка с запросом к базе данных о получении данных
        String requestString = "";
        //
        int readType = 0;
        // Изменение параметров подключения к базе данных
        connectToMySQL.changeConnectParameters();
        // Определение заголовков
        numHeadForCurrentTable = 0;
        switch(serviceTableID) {
            case changeUserID:
                numHeadForCurrentTable = 5;
                headForCurrentTable = new String[numHeadForCurrentTable];
                headForCurrentTable[0] = "Фамилия";
                headForCurrentTable[1] = "Имя";
                headForCurrentTable[2] = "Логин";
                headForCurrentTable[3] = "Пароль";
                headForCurrentTable[4] = "Уровень доступа";
                //
                requestString = "SELECT * FROM meterusers";
                //
                readType = 1;
                break;
            case changeMeterGroupID:
                numHeadForCurrentTable = 22;
                headForCurrentTable = new String[numHeadForCurrentTable];
                headForCurrentTable[0] = "Наименование группы приборов";
                headForCurrentTable[1] = "Количество параметров в группе";
                for(int i=2;i<22;i++)
                    headForCurrentTable[i] = "Заголовок " + Integer.toString(i-1);
                //
                requestString = "SELECT * FROM metergroup";
                //
                readType = 2;
                break;
            case changeProcessID:
                numHeadForCurrentTable = 1;
                headForCurrentTable = new String[numHeadForCurrentTable];
                headForCurrentTable[0] = "Наименование установки";
                //
                requestString = "SELECT * FROM process";
                break;
            case changeCalibrTypeID:
                numHeadForCurrentTable = 1;
                headForCurrentTable = new String[numHeadForCurrentTable];
                headForCurrentTable[0] = "Наименование типа поверки";
                //
                requestString = "SELECT * FROM calibrtype";
                break;
        }
        // 
        if(numHeadForCurrentTable > 0) {
            connectToMySQL.readParametersFromTable(requestString, numHeadForCurrentTable, readType);
            if(connectToMySQL.numRowsInTable > 0) {     
                dataForCurrentTable = new String[connectToMySQL.numRowsInTable][numHeadForCurrentTable];
                indexDataTable = new int[connectToMySQL.numRowsInTable];
                for(int i=0;i<connectToMySQL.numRowsInTable;i++)
                    for(int j=0;j<numHeadForCurrentTable;j++) {
                        dataForCurrentTable[i][j] = 
                                connectToMySQL.dataForCurrentTable[i][j];
                        if( (j==4) && (readType == 1) ) {
                            // Замена индекса на название Уровня доступа
                            dataForCurrentTable[i][j] = returnSecLevelString(Integer.parseInt(dataForCurrentTable[i][j]));
                        }
                    }
                System.arraycopy(connectToMySQL.indexDataTable, 0, indexDataTable, 0, connectToMySQL.numRowsInTable);
            }
            else {
                dataForCurrentTable = new String[1][1];
                indexDataTable = new int[1];
                dataForCurrentTable[0][0] = "Нет данных";
                indexDataTable[0] = 0;
            }
            
        }
    }
    /*********************************************************
     *  returnSecLevelString
     *********************************************************/
    private String returnSecLevelString(int ind) {
        String result = "";
        for(int i=0;i<secLevel.length;i++) {
            if(indexSecLevel[i]==ind)
                result = secLevel[i];
        }
        return result;
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
            listener.actionPerformed(new ActionEvent(this, serviceTableID, actionCommand));
        }
    }
    /*********************************************************
     *  Прослушиватель событий в форме meterInformation
     *********************************************************/
    @Override
    public void actionPerformed(ActionEvent e) {
        String labelCommand;
        //
        labelCommand = e.getActionCommand();
        switch(e.getID())  {
            case changeServiceTableID:
                switch (labelCommand) {
                    case EVENT_OK_ADD:
                        // Изменение параметров подключения к базе данных
                        connectToMySQL.changeConnectParameters();
                        // Добавление прибора
                        if(connectToMySQL.addTable(changeServiceTable.stringForAdd))
                            JOptionPane.showMessageDialog(panel, "Данные добавлены успешно");
                        else JOptionPane.showMessageDialog(panel, "Ошибка при добавлении данных");
                        //
                        drawTable();
                        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                            // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                            // и заголовками в строковом массиве headForCurrentMeterGroup
                            serviceTable.setModel(new DefaultTableModel(dataForCurrentTable,headForCurrentTable) {
                                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                                @Override   
                                public boolean isCellEditable(int row, int column){   
                                    return false;   
                                }; 
                            });
                        //}
                        //
                        changeServiceTable.setVisible(false);
                        break;
                    case EVENT_OK_CHANGE:
                        // Изменение параметров подключения к базе данных
                        connectToMySQL.changeConnectParameters();
                        // Изменение параметров прибора
                        if(connectToMySQL.changeTable(changeServiceTable.stringForUpdate))
                            JOptionPane.showMessageDialog(panel, "Данные изменены успешно");
                        else JOptionPane.showMessageDialog(panel, "Ошибка при изменении данных");
                        //
                        drawTable();
                        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                            // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                            // и заголовками в строковом массиве headForCurrentMeterGroup
                            serviceTable.setModel(new DefaultTableModel(dataForCurrentTable,headForCurrentTable) {
                                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                                @Override   
                                public boolean isCellEditable(int row, int column){   
                                    return false;   
                                }; 
                            });
                        //}
                        //
                        changeServiceTable.setVisible(false);
                        break;
                    case EVENT_CANCEL:
                        // Не изменять параметры прибора
                        changeServiceTable.setVisible(false);
                        break;
                }
                break;
            default:
                break;
        }
    }
    /*********************************************************
     * Проверка разрешения на редактирование
     *********************************************************/
    public boolean isEnableEdit(String curSecLevel) {
        boolean result = false;
        //
        switch(curSecLevel) {
            case "Нет":
                
                break;
            case "Контролер":
                
                break;
            case "Специалист":
                result = true;
                break;
            case "Администратор":
                result = true;
                break;
        }
        //
        return result;
    }
    /*********************************************************
     *  
     *********************************************************/
}
