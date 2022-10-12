
package meter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ProgramReport extends JFrame implements ActionListener {
    // Текущее окно
    JFrame thisFrame = this;
    // заголовок окна
    public String programReportTitle = "График поверки на месяц";
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
    // Кнопка для записи данных таблицы в файл с расширением csv
    // для возможности дальнейшей обработки в Excell
    JButton saveCSV;
    // Кнопка для закрытия окна
    JButton closeButton;
    // Хранение прслушивателей события от этого окна
    private ArrayList<ActionListener> actionListeners;
    // Идентификатор событий от окна с месячным отчетом
    final int monthReportID = 101;
    // Идентификатор событий от окна с просроченными поверками
    final int expiredReportID = 102;
    // Номер текущего окна для идентификации при отправке событий другим объектам
    int reportID = 101;
    // Сообщения при обмене между окнами
    final String EVENT_CLOSE = "EVENT_CLOSE";
    // Объявление объекта labelMonth на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Месяц")
    JLabel labelMonth;
    // Объявление объекта comboMonth на основе класса JComboBox
    // (будет использоваться для выбора месяца)
    JComboBox comboMonth;
    // Определение строкового массива 
    // с месяцами для объекта comboMonth
    String[] month = {"Все", "Январь", "Февраль", "Март", "Апрель", "Май","Июнь", 
                        "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь","Декабрь"} ;
    // Объявление объекта labelYear на основе класса JLabel
    // (будет использоваться для отбражения заголовка 
    // "Год")
    JLabel labelYear;
    // Объявление объекта comboYear на основе класса JComboBox
    // (будет использоваться для выбора месяца)
    JComboBox comboYear;
    // Определение строкового массива 
    // с годами для объекта comboYear
    String[] year;
    //
    int numYearBefore = 5;
    int numYearAfter = 10;
    // Текущий год
    int intCurrentYear;
    String stringCurrentYear;
    // Текущий месяц
    int intCurrentMonth;
    String stringCurrentMonth;
    // Текущий день
    int intCurrentDay;
    String stringCurrentDay;
    /*********************************************************
     *  ProgramReport конструктор
     *********************************************************/
    ProgramReport(int repID, String programTitle, int windowWidth, int windowHeight) {
        // Вывод заголовка окна
        super(programTitle);
        //
        reportID = repID;
        // 
        programReportTitle = programTitle;
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
                meterGroup = new String[connectToMySQL.numMeterGroup + 1];
                indexMeterGroup = new int[connectToMySQL.numMeterGroup + 1];
                meterGroup[0] = "Все";
                indexMeterGroup[0] = 0;
                System.arraycopy(connectToMySQL.rsMeterGroup, 0, meterGroup, 1, connectToMySQL.numMeterGroup);
                System.arraycopy(connectToMySQL.indexMeterGroup, 0, indexMeterGroup, 1, connectToMySQL.numMeterGroup);
            }
            else {
                meterGroup = new String[1];
                indexMeterGroup = new int[1];
                meterGroup[0] = "Все";
                indexMeterGroup[0] = 0;
            }
        }
        else {
            meterGroup = new String[1];
            indexMeterGroup = new int[1];
            meterGroup[0] = "Все";
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
                //if(connectToMySQL.numRowsInProgramReport > 0) {  
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
                //if(connectToMySQL.numRowsInProgramReport > 0) {  
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
                //if(connectToMySQL.numRowsInProgramReport > 0) {  
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
        // *********************************************************************
        getCurrentDate();
        // ***********************************************************************
        // Создание объекта labelYear на основе класса JLabel 
        labelYear = new JLabel("Год");
        // Вызов метода setBounds объекта labelYear для установки
        // положения и размеров labelYear
        labelYear.setBounds(500, 0, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelYear на панель
        panel.add(labelYear);
        // Формирование массива с годами
        year = new String[numYearBefore+numYearAfter+1];
        for(int i=0;i<(numYearBefore+numYearAfter+1);i++) {
            year[i] = Integer.toString((intCurrentYear-numYearBefore) + i);
        }
        // Создание объекта comboYear на основе класса JComboBox
        // с вариантами для выбора из строкового массива year
        comboYear = new JComboBox(year);
        // Вызов метода setBounds объекта comboYear для установки
        // положения и размеров comboYear
        comboYear.setBounds(710, 0, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboMonth на панель
	panel.add(comboYear);
        //
        comboYear.setSelectedItem(stringCurrentYear);
        // Cоздаем слушателя событий ActionListener при нажатии на варианты из списка comboYear
        ActionListener actionListenerYear = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                // Изменение текущего года
                stringCurrentYear = box.getSelectedItem().toString();
                intCurrentMonth = Integer.parseInt(stringCurrentYear);
                // Получение новых данных из базы данных
                drawTable();
                //if(connectToMySQL.numRowsInMonthReport > 0) {  
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
        comboYear.addActionListener(actionListenerYear);
        // ***********************************************************************
        // Создание объекта labelMonth на основе класса JLabel 
        labelMonth = new JLabel("Месяц");
        // Вызов метода setBounds объекта labelMonth для установки
        // положения и размеров labelMonth
        labelMonth.setBounds(500, 25, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // labelMonth на панель
        panel.add(labelMonth);
        // Создание объекта comboMonth на основе класса JComboBox
        // с вариантами для выбора из строкового массива month
        comboMonth = new JComboBox(month);
        // Вызов метода setBounds объекта comboMonth для установки
        // положения и размеров comboMonth
        comboMonth.setBounds(710, 25, 200, 20);
        // Вызов метода add объекта panel, который добавляет
        // comboMonth на панель
	panel.add(comboMonth);
        //
        comboMonth.setSelectedIndex(intCurrentMonth);
        // Cоздаем слушателя событий ActionListener при нажатии на варианты из списка comboMonth
        ActionListener actionListenerMonth = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox)e.getSource();
                // Изменение текущего месяца
                if(box.getSelectedIndex()!=0) {
                    intCurrentMonth = box.getSelectedIndex();
                    stringCurrentMonth = Integer.toString(intCurrentMonth);
                    if(stringCurrentMonth.length()==1)
                        stringCurrentMonth = "0"+ stringCurrentMonth;
                }
                else {
                    intCurrentMonth = 0;
                    stringCurrentMonth = "00";
                }
                // Получение новых данных из базы данных
                drawTable();
                //if(connectToMySQL.numRowsInProgramReport > 0) {  
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
        comboMonth.addActionListener(actionListenerMonth);
        // ***********************************************************************
        setObjectVisibile();
        drawTable();
        //if(connectToMySQL.numRowsInProgramReport > 0) {  
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
        // Создание объекта saveCSV на основе класса JButton 
        saveCSV = new JButton("Сохранить в *.csv");
        // Вызов метода setBounds объекта saveCSV для установки
        // положения и размеров saveCSV
        saveCSV.setBounds(10, tableScrollPane.getY() + tableScrollPane.getHeight() + 10, 200, 30);
        // Cоздаем слушателя событий ActionListener при нажатии на saveCSV
        ActionListener actionListenerSaveCSV = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Вызов диалога сохранения таблицы в файл *.csv
                JFileChooser fc = new JFileChooser();
                //
                if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
                    try ( FileWriter fw = new FileWriter(fc.getSelectedFile()) ) {
                        if(connectToMySQL.numRowsInProgramReport > 0) { 
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
                            for(int j=0;j<connectToMySQL.numRowsInProgramReport;j++) {
                                tableRow = "";
                                for(int i=0;i<headForCurrentMeterGroup.length;i++) {
                                    tableRow = tableRow+dataForCurrentMeterGroup[j][i];
                                    if(i != (headForCurrentMeterGroup.length-1))
                                        tableRow = tableRow+";";
                                    else tableRow = tableRow+"\n";
                                }
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
        closeButton.setBounds(220, tableScrollPane.getY() + tableScrollPane.getHeight() + 10, 100, 30);
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
        //
    }
    /*********************************************************
     *  setObjectVisibile
     *********************************************************/
    public void setObjectVisibile() {
        switch(reportID) {
            case monthReportID:
                labelYear.setVisible(true);
                comboYear.setVisible(true);
                labelMonth.setVisible(true);
                comboMonth.setVisible(true);
                break;
            case expiredReportID:
                labelYear.setVisible(false);
                comboYear.setVisible(false);
                labelMonth.setVisible(false);
                comboMonth.setVisible(false);
                break;
        }
    }
    /*********************************************************
     *  drawTable
     *********************************************************/
    public void drawTable() {
        // Изменение параметров подключения к базе данных
        connectToMySQL.changeConnectParameters();
        // Для отображения используются только постоянные заголовки
        numHeadForCurrentMeterGroup = 10;
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
        //
        String meterGroupID = Integer.toString(indexMeterGroup[comboMeterGroup.getSelectedIndex()]);
        if(meterGroupID.equals("0")) meterGroupID = "0";
        String processID = Integer.toString(indexProcess[comboProcess.getSelectedIndex()]);
        if(processID.equals("0")) processID = "0";
        String calibrTypeID = Integer.toString(indexCalibrType[comboCalibrType.getSelectedIndex()]);
        if(processID.equals("0")) processID = "0";
        String startDate = stringCurrentYear + "-" + stringCurrentMonth + "-01";
        String endDate = addDate(startDate, 1, Calendar.MONTH);
        if(stringCurrentMonth.equals("00")) {
            startDate = stringCurrentYear + "-01-01";
            endDate = addDate(startDate, 1, Calendar.YEAR);
        }
        //
        connectToMySQL.numRowsInProgramReport = 0;
        //
        switch(reportID) {
            case expiredReportID:
                getCurrentDate();
                String expiredDate = stringCurrentYear + "-" + stringCurrentMonth + "-" + stringCurrentDay;
                connectToMySQL.readParametersFromMeterForExpiredReport(headForCurrentMeterGroup.length, meterGroupID, processID, 
                    calibrTypeID, expiredDate);
                break;
            case monthReportID:
                connectToMySQL.readParametersFromMeterForMonthReport(headForCurrentMeterGroup.length, meterGroupID, processID, 
                    calibrTypeID, startDate, endDate);
                break;
        }
        
        if(connectToMySQL.numRowsInProgramReport > 0) {     
            dataForCurrentMeterGroup = new String[connectToMySQL.numRowsInProgramReport][headForCurrentMeterGroup.length];
            indexDataMeterGroup = new int[connectToMySQL.numRowsInProgramReport];
            for(int i=0;i<connectToMySQL.numRowsInProgramReport;i++)
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
            System.arraycopy(connectToMySQL.indexDataMeterGroup, 0, indexDataMeterGroup, 0, connectToMySQL.numRowsInProgramReport);
        }
        else {
            dataForCurrentMeterGroup = new String[1][1];
            indexDataMeterGroup = new int[1];
            dataForCurrentMeterGroup[0][0] = "Нет Данных";
            indexDataMeterGroup[0] = 0;
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
            listener.actionPerformed(new ActionEvent(this, reportID, actionCommand));
        }
    }
    /*********************************************************
     *  Прослушиватель событий в форме
     *********************************************************/
    @Override
    public void actionPerformed(ActionEvent e) {
        String labelCommand;
        //
        labelCommand = e.getActionCommand();
        
    }
    /*********************************************************
     *  getCurrentDate
     *********************************************************/
    public void getCurrentDate() { 
        Date date1 = new Date();
        //
        SimpleDateFormat dateformatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateformatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateformatDay = new SimpleDateFormat("dd");
        // 
        stringCurrentYear = dateformatYear.format(date1);
        intCurrentYear = Integer.parseInt(stringCurrentYear);
        stringCurrentMonth = dateformatMonth.format(date1);
        intCurrentMonth = Integer.parseInt(stringCurrentMonth);
        stringCurrentDay = dateformatDay.format(date1);
        intCurrentDay = Integer.parseInt(stringCurrentDay);
    }
    /*********************************************************
     *  addDate
     *********************************************************/
    public String addDate(String inDate, int addValue, int addType) {
        String result;
        Calendar date;   
        //
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateformatYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateformatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateformatDay = new SimpleDateFormat("dd");
        //
        int Year;
        int Month;
        int Day;
        Year = Integer.parseInt(inDate.substring(0, 4));
        Month = Integer.parseInt(inDate.substring(5, 7)) - 1;
        Day = Integer.parseInt(inDate.substring(8, 10));
        date = new GregorianCalendar(Year, Month, Day);
        //
        date.add(addType, addValue);
        //
        String strDay = Integer.toString(date.get(Calendar.DAY_OF_MONTH));
        if(strDay.length()==1) strDay = "0" + strDay;
        String strMonth = Integer.toString(date.get(Calendar.MONTH)+1);
        if(strMonth.length()==1) strMonth = "0" + strMonth;
        String strYear = Integer.toString(date.get(Calendar.YEAR));

        result = strYear+"-" + strMonth + "-" + strDay;
        
        return result;
    }
    /*********************************************************
     *  
     *********************************************************/
}
