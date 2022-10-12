
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
public class MeterInformation extends JFrame implements ActionListener {
    // Текущее окно
    JFrame thisFrame = this;
    // заголовок окна MeterInformation
    public String meterInformationTitle = "Информация о приборах";
    // Текущий уровень доступа
    String currentSecLevel;
    // Объявление объекта connectToMySQL на основе класса JavaToMySQL
    // будет использоваться для получения данных из базы данных
    JavaToMySQL connectToMySQL;
    // Панель для отображения объектов на форме
    JPanel panel;
    // Расположение и размер панели
    private final int panelX=0,panelY=0,panelWidth,panelHeight;
    // Цвет фона панели
    private Color panelBackground = Color.white;
    // Объявление объекта labelMeterGroup на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Группа прибора")
    JLabel labelMeterGroup;
    // Объявление объекта comboMeterGroup на основе класса JComboBox
    // (будет использоваться для выбора группы прибора)
    JComboBox comboMeterGroup;
    // Определение строкового массива 
    // с группами приборов для объекта comboMeterGroup
    String[] meterGroup;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве meterGroup
    int[] indexMeterGroup;
    // Переменная для хранения выбранного типа прибора
    String currentMeterGroup;
    // Объявление объекта labelProcess на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Установка")
    JLabel labelProcess;
    // Объявление объекта comboProcess на основе класса JComboBox
    // (будет использоваться для выбора установки)
    JComboBox comboProcess;
    // Определение строкового массива 
    // с установками для объекта comboProcess
    String[] process;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве process
    int[] indexProcess;
    // Переменная для хранения выбранной установки
    String currentProcess;
    // Объявление объекта labelCalibrType на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Тип поверки")
    JLabel labelCalibrType;
    // Объявление объекта comboCalibrType на основе класса JComboBox
    // (будет использоваться для выбора типа поверки)
    JComboBox comboCalibrType;
    // Определение строкового массива 
    // с типами поверок для объекта comboCalibrType
    String[] calibrType;
    // Определение массива с индексами в базе данных, соответствующие
    // данным в строковом массиве calibrType
    int[] indexCalibrType;
    // Переменная для хранения выбранного типа поверки
    String currentCalibrType;
    // Переменная хранит количество заголовков (столбцов)
    // в таблице для выбранной группы приборов
    int numHeadForCurrentMeterGroup;
    // Строковый массив для хранения заголовков для текущей группы приборов
    String[] headForCurrentMeterGroup; 
    // Переменная хранит количество строк
    // в таблице для выбранной группы приборов
    int numRowForCurrentMeterGroup;
    // Строковый массив для хранения данных для текущей группы приборов
    String[][] dataForCurrentMeterGroup; 
    // Массив для хранения индекса прибора в таблице meter
    int indexDataMeterGroup[];
    // Количество постоянных параметров в таблице MeterGroup
    int numConstantParamInnMeterGroup = 10;
    // панель прокрутки для размещения таблицы
    JScrollPane tableScrollPane;
    // Таблица для отображения приборов
    JTable meterTable;
    // Кнопка для добавления прибора в базу данных
    JButton buttonAdd;
    // Кнопка для удаления прибора из базы данных
    JButton buttonRemove;
    // Кнопка для записи данных таблицы в файл с расширением csv
    // для возможности дальнейшей обработки в Excell
    JButton saveCSV;
    // Кнопка для закрытия окна
    JButton closeButton;
    // Хранение прслушивателей события от этого окна
    private ArrayList<ActionListener> actionListeners;
    // Номер текущего окна для идентификации при отправке событий другим объектам
    int meterInformationID = 100;
    // Номер диалогового окна ChangeMeter для идентификации 
    final int changeMeterID = 200;
    // Сообщения при обмене между окнами
    final String EVENT_OK_ADD = "EVENT_OK_ADD";
    final String EVENT_OK_CHANGE = "EVENT_OK_CHANGE";
    final String EVENT_CANCEL = "EVENT_CANCEL";
    //
    final String EVENT_CLOSE = "EVENT_CLOSE";
    //
    int ADD_METER = 1;
    int CHANGE_METER = 2;
    //
    String STRING_ADD = "Добавление прибора";
    String STRING_CHANGE = "Изменение данных прибора";
    // Объект для изменения/добавления прибора
    ChangeMeter changeMeter;
    /*********************************************************
     *  MeterInformation конструктор
     *********************************************************/
    MeterInformation(int mInformationID, String formTitle, int windowWidth, int windowHeight) {
        // Вывод заголовка окна
        super(formTitle);
        //
        meterInformationID = mInformationID;
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
        // Создание объекта labelMeterGroup на основе класса JLabel 
        labelMeterGroup = new JLabel("Группа прибора");
        // Вызов метода setBounds объекта labelMeterGroup для установки
        // положения и размеров labelMeterGroup
        labelMeterGroup.setBounds(10, 0, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelMeterGroup на панель
        panel.add(labelMeterGroup);
        // Получение данных из базы данных о группах приборов
        connectToMySQL.readFromMeterGroup();
        if(connectToMySQL.rsMeterGroup !=null) {
            if(connectToMySQL.numMeterGroup > 0) {
                meterGroup = new String[connectToMySQL.numMeterGroup];
                indexMeterGroup = new int[connectToMySQL.numMeterGroup];
                System.arraycopy(connectToMySQL.rsMeterGroup, 0, meterGroup, 0, connectToMySQL.numMeterGroup);
                System.arraycopy(connectToMySQL.indexMeterGroup, 0, indexMeterGroup, 0, connectToMySQL.numMeterGroup);
            }
            else {
                meterGroup = new String[1];
                indexMeterGroup = new int[1];
                meterGroup[0] = "Нет";
                indexMeterGroup[0] = 0;
            }
        }
        else {
            meterGroup = new String[1];
            indexMeterGroup = new int[1];
            meterGroup[0] = "Нет";
            indexMeterGroup[0] = 0;
        }
        // Создание объекта comboMeterGroup на основе класса JComboBox
        // с вариантами для выбора из строкового массива meterGroup
        comboMeterGroup = new JComboBox(meterGroup);
        // Вызов метода setBounds объекта comboMeterGroup для установки
        // положения и размеров comboMeterGroup
        comboMeterGroup.setBounds(210, 0, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboMeterGroup на панель
	panel.add(comboMeterGroup);
        //
        currentMeterGroup = (String)comboMeterGroup.getSelectedItem();
        // Cоздаем слушателя событий ActionListener при нажатии на варианты из списка comboMeterGroup
        ActionListener actionListenerMeterGroup = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                // Изменение текущей группы приборов
                currentMeterGroup = (String)box.getSelectedItem();
                // Получение новых данных из базы данных
                drawTable();
                //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                    // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                    // и заголовками в строковом массиве headForCurrentMeterGroup
                    meterTable.setModel(new DefaultTableModel(dataForCurrentMeterGroup,headForCurrentMeterGroup) {
                        // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                        @Override   
                        public boolean isCellEditable(int row, int column){   
                            return false;   
                        }; 
                    });
                //}
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        comboMeterGroup.addActionListener(actionListenerMeterGroup);
        // ***********************************************************************
        // Создание объекта labelProcess на основе класса JLabel 
        labelProcess = new JLabel("Установка");
        // Вызов метода setBounds объекта labelProcess для установки
        // положения и размеров labelProcess
        labelProcess.setBounds(10, 25, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelProcess на панель
        panel.add(labelProcess);
        // Получение данных из базы данных об установках
        connectToMySQL.readFromProcess();
        if(connectToMySQL.rsProcess !=null) {
            if(connectToMySQL.numProcess > 0) {
                process = new String[connectToMySQL.numProcess+1];
                indexProcess = new int[connectToMySQL.numProcess+1];
                process[0] = "Все";
                indexProcess[0] = 0;
                System.arraycopy(connectToMySQL.rsProcess, 0, process, 1, connectToMySQL.numProcess);
                System.arraycopy(connectToMySQL.indexProcess, 0, indexProcess, 1, connectToMySQL.numProcess);
            }
            else {
                process = new String[1];
                indexProcess = new int[1];
                process[0] = "Все";
                indexProcess[0] = 0;
            }
        }
        else {
            process = new String[1];
            indexProcess = new int[1];
            process[0] = "Все";
            indexProcess[0] = 0;
        }
        // Создание объекта comboProcess на основе класса JComboBox
        // с вариантами для выбора из строкового массива process
        comboProcess = new JComboBox(process);
        // Вызов метода setBounds объекта comboProcess для установки
        // положения и размеров comboProcess
        comboProcess.setBounds(210, 25, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboProcess на панель
	panel.add(comboProcess);
        //
        currentProcess = (String)comboProcess.getSelectedItem();
        // Cоздаем слушателя событий ActionListener при нажатии на варианты из списка comboProcess
        ActionListener actionListenerProcess = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                // Изменение текущей установки
                currentProcess = (String)box.getSelectedItem();
                // Получение новых данных из базы данных
                drawTable();
                //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                    // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                    // и заголовками в строковом массиве headForCurrentMeterGroup
                    meterTable.setModel(new DefaultTableModel(dataForCurrentMeterGroup,headForCurrentMeterGroup) {
                        // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                        @Override   
                        public boolean isCellEditable(int row, int column){   
                            return false;   
                        }; 
                    });
                //}
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        comboProcess.addActionListener(actionListenerProcess);
        // ***********************************************************************
        // Создание объекта labelCalibrType на основе класса JLabel 
        labelCalibrType = new JLabel("Тип поверки");
        // Вызов метода setBounds объекта labelCalibrType для установки
        // положения и размеров labelCalibrType
        labelCalibrType.setBounds(10, 50, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelCalibrType на панель
        panel.add(labelCalibrType);
        // Получение данных из базы данных о типах поверок
        connectToMySQL.readFromCalibrType();
        if(connectToMySQL.rsCalibrType !=null) {
            if(connectToMySQL.numCalibrType > 0) {
                calibrType = new String[connectToMySQL.numCalibrType+1];
                indexCalibrType = new int[connectToMySQL.numCalibrType+1];
                calibrType[0] = "Все";
                indexCalibrType[0] = 0;
                System.arraycopy(connectToMySQL.rsCalibrType, 0, calibrType, 1, connectToMySQL.numCalibrType);
                System.arraycopy(connectToMySQL.indexCalibrType, 0, indexCalibrType, 1, connectToMySQL.numCalibrType);
            }
            else {
                calibrType = new String[1];
                indexCalibrType = new int[1];
                calibrType[0] = "Все";
                indexCalibrType[0] = 0;
            }
        }
        else {
            calibrType = new String[1];
            indexCalibrType = new int[1];
            calibrType[0] = "Все";
            indexCalibrType[0] = 0;
        }
        // Создание объекта comboCalibrType на основе класса JComboBox
        // с вариантами для выбора из строкового массива calibrType
        comboCalibrType = new JComboBox(calibrType);
        // Вызов метода setBounds объекта comboCalibrType для установки
        // положения и размеров comboCalibrType
        comboCalibrType.setBounds(210, 50, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboCalibrType на панель
	panel.add(comboCalibrType);
        //
        currentCalibrType = (String)comboCalibrType.getSelectedItem();
        // Cоздаем слушателя событий ActionListener при нажатии на варианты из списка comboCalibrType
        ActionListener actionListenerCalibrType = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                // Изменение текущего Типа калибровки
                currentCalibrType = (String)box.getSelectedItem();
                // Получение новых данных из базы данных
                drawTable();
                //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                    // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                    // и заголовками в строковом массиве headForCurrentMeterGroup
                    meterTable.setModel(new DefaultTableModel(dataForCurrentMeterGroup,headForCurrentMeterGroup) {
                        // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                        @Override   
                        public boolean isCellEditable(int row, int column){   
                            return false;   
                        }; 
                    });
                //}
            }
        };
        // Затем добавляем слушателя при помощи метода addActionListener.
        comboCalibrType.addActionListener(actionListenerCalibrType);
        
        // ***********************************************************************
        drawTable();
        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
            // Создание таблицы meterTable на базе класса JTable
            meterTable = new JTable();
            // Создание модели таблицы с данными в строковом массиве dataForCurrentMeterGroup
            // и заголовками в строковом массиве headForCurrentMeterGroup
            meterTable.setModel(new DefaultTableModel(dataForCurrentMeterGroup,headForCurrentMeterGroup) {
                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                @Override   
                public boolean isCellEditable(int row, int column){   
                    return false;   
                }; 
            });
            // Добавление прослушивателя нажатий кнопок мыши
            meterTable.addMouseListener(new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(e.getClickCount() == 2) {
                        if(isEnableEdit(currentSecLevel)) {
                            changeMeter.setResizable(false);
                            setParametersForChangeMeter();
                            changeMeter.setObjectData();
                            changeMeter.setLocationRelativeTo(null);
                            changeMeter.setVisible(true);
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
            // и размещение в нем таблицы meterTable
            tableScrollPane = new JScrollPane(meterTable);
            // Вызов метода setBounds объекта tableScrollPane для установки
            // положения и размеров tableScrollPane
            tableScrollPane.setBounds(10, 80, panel.getWidth()-20, panel.getHeight() - 190);  
            // определение использвания полос прокрутки в tableScrollPane
            // по мере необходимости (AS_NEEDED)
            tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            //
            panel.add(tableScrollPane);
        //}
        // Создание объекта buttonAdd на основе класса JButton 
        buttonAdd = new JButton("Добавить прибор");
        // Вызов метода setBounds объекта buttonAdd для установки
        // положения и размеров buttonAdd
        buttonAdd.setBounds(10, tableScrollPane.getY() + tableScrollPane.getHeight() + 10, 200, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на closeButton
        ActionListener actionListenerAdd = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(isEnableEdit(currentSecLevel)) {
                    changeMeter.setResizable(false);
                    setParametersForAddMeter();
                    changeMeter.setObjectData();
                    changeMeter.setLocationRelativeTo(null);
                    changeMeter.setVisible(true);
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
        buttonRemove = new JButton("Удалить прибор");
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
                    if( meterTable.getSelectedRow() >=0 ) {
                        if(connectToMySQL.removeMeter(indexDataMeterGroup[meterTable.getSelectedRow()]))
                            JOptionPane.showMessageDialog(panel, "Данные удалены успешно");
                        else JOptionPane.showMessageDialog(panel, "Ошибка при удалении данных");
                        //
                        drawTable();
                        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                            // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                            // и заголовками в строковом массиве headForCurrentMeterGroup
                            meterTable.setModel(new DefaultTableModel(dataForCurrentMeterGroup,headForCurrentMeterGroup) {
                                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                                @Override   
                                public boolean isCellEditable(int row, int column){   
                                    return false;   
                                }; 
                            });
                        //}
                    }
                    else {
                        JOptionPane.showMessageDialog(panel, "Не выбран прибор для удаления");
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
                        if(connectToMySQL.numRowsInMeterGroup > 0) { 
                            String tableRow = "";
                            // Запись в файл заголовков
                            for(int i=0;i<headForCurrentMeterGroup.length;i++) {
                                tableRow = tableRow+headForCurrentMeterGroup[i];
                                if(i != (headForCurrentMeterGroup.length-1))
                                    tableRow = tableRow+";";
                                else tableRow = tableRow+"\n";
                            }
                            fw.write(tableRow);
                            // Запись в файл данных
                            for(int j=0;j<connectToMySQL.numRowsInMeterGroup;j++) {
                                tableRow = "";
                                for(int i=0;i<headForCurrentMeterGroup.length;i++) {
                                    tableRow = tableRow+dataForCurrentMeterGroup[j][i];
                                    if(i != (headForCurrentMeterGroup.length-1))
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
        changeMeter = new ChangeMeter(thisFrame, panelWidth, panelHeight);
        changeMeter.addListener(this);
        changeMeter.dispose();
    }
    /*********************************************************
     *  setParametersForAddMeter
     *********************************************************/
    public void setParametersForAddMeter() {
        Calendar date;   
        Date date1 = new Date();
        SimpleDateFormat dateformatJava = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateformatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateformatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateformatDay = new SimpleDateFormat("dd");
        //
        int Year;
        int Month;
        int Day;
        //
        changeMeter.setDataForCurrentMeter(STRING_ADD, 
        0, indexMeterGroup[comboMeterGroup.getSelectedIndex()], numHeadForCurrentMeterGroup,  ADD_METER,
        process.length-1, calibrType.length-1);
        //
        for(int j=0;j<numHeadForCurrentMeterGroup;j++) {
            changeMeter.headForCurrentMeter[j] = headForCurrentMeterGroup[j];
            changeMeter.dataForCurrentMeter[j] = "";
        }
        //
        changeMeter.dataForCurrentMeter[0] = "meter name";
        changeMeter.dataForCurrentMeter[1] = comboMeterGroup.getSelectedItem().toString();
        if(comboProcess.getSelectedIndex() > 0)
            changeMeter.dataForCurrentMeter[2] = comboProcess.getSelectedItem().toString();
        else 
            if(process.length>1) 
                changeMeter.dataForCurrentMeter[2] = comboProcess.getItemAt(1).toString();
            else changeMeter.dataForCurrentMeter[2] = "";
        //
        Year = Integer.parseInt(dateformatYear.format(date1));
        Month = Integer.parseInt(dateformatMonth.format(date1));
        Day = Integer.parseInt(dateformatDay.format(date1));
        date = new GregorianCalendar(Year, Month, Day);
        //
        String strDay = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        if(strDay.length()==1) strDay = "0" + strDay;
        String strMonth = Integer.toString(date.get(Calendar.MONTH));
        if(strMonth.length()==1) strMonth = "0" + strMonth;
        String strYear = Integer.toString(date.get(Calendar.YEAR));
        changeMeter.dataForCurrentMeter[3] = strDay+"."+strMonth+"."+strYear;
        //
        changeMeter.dataForCurrentMeter[4] = "plantNum";
        changeMeter.dataForCurrentMeter[5] = "plantMaker";
        if(comboCalibrType.getSelectedIndex() > 0)
            changeMeter.dataForCurrentMeter[6] = comboCalibrType.getSelectedItem().toString();
        else 
            if(calibrType.length>1) 
                changeMeter.dataForCurrentMeter[6] = comboCalibrType.getItemAt(1).toString();
            else changeMeter.dataForCurrentMeter[6] = "";
        //
        changeMeter.dataForCurrentMeter[7] = changeMeter.dataForCurrentMeter[3];
        changeMeter.dataForCurrentMeter[8] = "1";
        //
        date.add(Calendar.YEAR, 1);
        strDay = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        if(strDay.length()==1) strDay = "0" + strDay;
        strMonth = Integer.toString(date.get(Calendar.MONTH));
        if(strMonth.length()==1) strMonth = "0" + strMonth;
        strYear = Integer.toString(date.get(Calendar.YEAR));
        changeMeter.dataForCurrentMeter[9] = strDay+"."+strMonth+"."+strYear;
        //
        if( (process.length-1) > 0 ) {
            for(int j=0;j<(process.length-1);j++) {
                changeMeter.process[j] = process[j+1];
                changeMeter.indexProcess[j] = indexProcess[j+1];
            }
        }
        //
        if( (calibrType.length-1) > 0 ) {
            for(int j=0;j<(calibrType.length-1);j++) {
                changeMeter.calibrType[j] = calibrType[j+1];
                changeMeter.indexCalibrType[j] = indexCalibrType[j+1];
            }
        }
    }
    /*********************************************************
     *  setParametersForChangeMeter
     *********************************************************/
    public void setParametersForChangeMeter() {
        int i = meterTable.getSelectedRow();
        if(i<0) i = 0;
        changeMeter.setDataForCurrentMeter(STRING_CHANGE, 
        indexDataMeterGroup[i], indexMeterGroup[comboMeterGroup.getSelectedIndex()], numHeadForCurrentMeterGroup,  CHANGE_METER,
        process.length-1, calibrType.length-1);
        //
        for(int j=0;j<numHeadForCurrentMeterGroup;j++) {
            changeMeter.headForCurrentMeter[j] = headForCurrentMeterGroup[j];
            changeMeter.dataForCurrentMeter[j] = dataForCurrentMeterGroup[i][j];
        }
        //
        if( (process.length-1) > 0 ) {
            for(int j=0;j<(process.length-1);j++) {
                changeMeter.process[j] = process[j+1];
                changeMeter.indexProcess[j] = indexProcess[j+1];
            }
        }
        //
        if( (calibrType.length-1) > 0 ) {
            for(int j=0;j<(calibrType.length-1);j++) {
                changeMeter.calibrType[j] = calibrType[j+1];
                changeMeter.indexCalibrType[j] = indexCalibrType[j+1];
            }
        }
    }
    /*********************************************************
     *  drawTable
     *********************************************************/
    public void drawTable() {
        // Изменение параметров подключения к базе данных
        connectToMySQL.changeConnectParameters();
        // Получение заголовков для выбранной группы приборов из базы данных
        connectToMySQL.readHeadFromMeterGroup(currentMeterGroup);
        if(connectToMySQL.numHeadInMeterGroup > 0) {
            numHeadForCurrentMeterGroup = numConstantParamInnMeterGroup + connectToMySQL.numHeadInMeterGroup;
            headForCurrentMeterGroup = new String[numHeadForCurrentMeterGroup];
            headForCurrentMeterGroup[0] = "Наименование прибора";
            headForCurrentMeterGroup[1] = "Группа прибора";
            headForCurrentMeterGroup[2] = "Установка";
            headForCurrentMeterGroup[3] = "Дата ввода прибора";
            headForCurrentMeterGroup[4] = "Заводской номер";
            headForCurrentMeterGroup[5] = "Завод изготовитель";
            headForCurrentMeterGroup[6] = "Тип поверки";
            headForCurrentMeterGroup[7] = "Дата последней поверки";
            headForCurrentMeterGroup[8] = "Периодичность поверки, год";
            headForCurrentMeterGroup[9] = "Дата следующей поверки";
            System.arraycopy(connectToMySQL.rsHeadInMeterGroup, 0, headForCurrentMeterGroup, numConstantParamInnMeterGroup, connectToMySQL.numHeadInMeterGroup);
            //
            String meterGroupID = Integer.toString(indexMeterGroup[comboMeterGroup.getSelectedIndex()]);
            String processID = Integer.toString(indexProcess[comboProcess.getSelectedIndex()]);
            if(processID.equals("0")) processID = "0";
            String calibrTypeID = Integer.toString(indexCalibrType[comboCalibrType.getSelectedIndex()]);
            if(processID.equals("0")) processID = "0";
            connectToMySQL.readParametersFromMeter(headForCurrentMeterGroup.length, meterGroupID,
                    processID, calibrTypeID);
            if(connectToMySQL.numRowsInMeterGroup > 0) {     
                dataForCurrentMeterGroup = new String[connectToMySQL.numRowsInMeterGroup][headForCurrentMeterGroup.length];
                indexDataMeterGroup = new int[connectToMySQL.numRowsInMeterGroup];
                for(int i=0;i<connectToMySQL.numRowsInMeterGroup;i++)
                    for(int j=0;j<headForCurrentMeterGroup.length;j++) {
                        dataForCurrentMeterGroup[i][j] = 
                                connectToMySQL.dataForCurrentMeterGroup[i][j];
                        if(j==1) {
                            // Замена индекса на название Группы приборов
                            dataForCurrentMeterGroup[i][j] = returnMeterGroupString(Integer.parseInt(dataForCurrentMeterGroup[i][j]));
                        }
                        if(j==2) {
                            // Замена индекса на название Установки
                            dataForCurrentMeterGroup[i][j] = returnProcess(Integer.parseInt(dataForCurrentMeterGroup[i][j]));
                        }
                        if(j==6) {
                            // Замена индекса на название Типа поверки
                            dataForCurrentMeterGroup[i][j] = returnCalibrType(Integer.parseInt(dataForCurrentMeterGroup[i][j]));
                        }
                    }
                System.arraycopy(connectToMySQL.indexDataMeterGroup, 0, indexDataMeterGroup, 0, connectToMySQL.numRowsInMeterGroup);
                int j=1;
                //
            }
            else {
                dataForCurrentMeterGroup = new String[1][1];
                indexDataMeterGroup = new int[1];
                dataForCurrentMeterGroup[0][0] = "Нет данных";
                indexDataMeterGroup[0] = 0;
            }
            
        }
    }
    /*********************************************************
     *  returnMeterGroupString
     *********************************************************/
    private String returnMeterGroupString(int ind) {
        String result = "";
        for(int i=0;i<meterGroup.length;i++) {
            if(indexMeterGroup[i]==ind)
                result = meterGroup[i];
        }
        return result;
    }
    /*********************************************************
     *  returnProcess
     *********************************************************/
    private String returnProcess(int ind) {
        String result = "";
        for(int i=0;i<process.length;i++) {
            if(indexProcess[i]==ind)
                result = process[i];
        }
        return result;
    }
    /*********************************************************
     *  returnCalibrType
     *********************************************************/
    private String returnCalibrType(int ind) {
        String result = "";
        for(int i=0;i<calibrType.length;i++) {
            if(indexCalibrType[i]==ind)
                result = calibrType[i];
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
            listener.actionPerformed(new ActionEvent(this, meterInformationID, actionCommand));
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
            case changeMeterID:
                switch (labelCommand) {
                    case EVENT_OK_ADD:
                        // Изменение параметров подключения к базе данных
                        connectToMySQL.changeConnectParameters();
                        // Добавление прибора
                        if(connectToMySQL.addMeter(changeMeter.stringForAdd))
                            JOptionPane.showMessageDialog(panel, "Данные добавлены успешно");
                        else JOptionPane.showMessageDialog(panel, "Ошибка при добавлении данных");
                        //
                        drawTable();
                        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                            // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                            // и заголовками в строковом массиве headForCurrentMeterGroup
                            meterTable.setModel(new DefaultTableModel(dataForCurrentMeterGroup,headForCurrentMeterGroup) {
                                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                                @Override   
                                public boolean isCellEditable(int row, int column){   
                                    return false;   
                                }; 
                            });
                        //}
                        //
                        changeMeter.setVisible(false);
                        break;
                    case EVENT_OK_CHANGE:
                        // Изменение параметров подключения к базе данных
                        connectToMySQL.changeConnectParameters();
                        // Изменение параметров прибора
                        if(connectToMySQL.changeMeter(changeMeter.stringForUpdate))
                            JOptionPane.showMessageDialog(panel, "Данные изменены успешно");
                        else JOptionPane.showMessageDialog(panel, "Ошибка при изменении данных");
                        //
                        drawTable();
                        //if(connectToMySQL.numRowsInMeterGroup > 0) {  
                            // Пересоздание модели таблицы с новыми данными в строковом массиве dataForCurrentMeterGroup
                            // и заголовками в строковом массиве headForCurrentMeterGroup
                            meterTable.setModel(new DefaultTableModel(dataForCurrentMeterGroup,headForCurrentMeterGroup) {
                                // Переопределение метода isCellEditable для запрета редактирования ячеек таблицы
                                @Override   
                                public boolean isCellEditable(int row, int column){   
                                    return false;   
                                }; 
                            });
                        //}
                        //
                        changeMeter.setVisible(false);
                        break;
                    case EVENT_CANCEL:
                        // Не изменять параметры прибора
                        changeMeter.setVisible(false);
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
