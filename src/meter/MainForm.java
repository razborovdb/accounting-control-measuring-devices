
package meter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/*********************************************************
 *  MainForm
 *********************************************************/
public class MainForm  extends JFrame implements ActionListener {
    // Текущее окно
    JFrame thisFrame = this;
    // Меню основного окна
    JMenuBar menubar;
    // Пункт меню 
    JMenu menuMeter;
    // Подпункт пункта меню menuMeter
    JMenuItem menuMeterInformation;
    // Пункт меню 
    JMenu menuReport;
    // Подпункт пункта меню menuReport
    JMenuItem menuMonthReport;
    // Подпункт пункта меню menuReport
    JMenuItem menuExpiredReport;
    // Пункт меню 
    JMenu menuService;
    // Подпункт пункта меню menuService
    JMenuItem menuServiceConnect;
    // Подпункт пункта меню menuService
    JMenuItem menuServiceCreate;
    // Подпункт пункта меню menuService
    JMenuItem menuServiceAddUser;
    // Подпункт пункта меню menuService
    JMenuItem menuServiceAddMeterGroup;
    // Подпункт пункта меню menuService
    JMenuItem menuServiceAddProcess;
    // Подпункт пункта меню menuService
    JMenuItem menuServiceAddCalibrType;
    // Пункт меню 
    JMenu menuLogin;
    // Подпункт пункта меню menuLogin
    JMenuItem menuLoginItem;
    // Пункт меню
    JMenu menuExit;
    // Подпункт пункта меню menuExit
    JMenuItem menuExitItem;
    // Панель для отображения объектов
    JPanel panel;
    // Place and size of panel
    private final int panelX=0,panelY=0,panelWidth,panelHeight;
    // Background color of panel
    private Color panelBackground = Color.white;
    //
    MeterInformation meterinformation;
    // Идентификатор событий от окна meterinformation
    final int meterInformationID = 100;
    // Идентификатор событий от окна с месячным отчетом
    final int monthReportID = 101;
    // Идентификатор событий от окна с просроченными поверками
    final int expiredReportID = 102;
    // Идентификатор событий от окна Добавить/изменить пользователя
    final int changeUserID = 103;
    // Идентификатор событий от окна Добавить/изменить группу прибора
    final int changeMeterGroupID = 104;
    // Идентификатор событий от окна Добавить/изменить установку
    final int changeProcessID = 105;
    // Идентификатор событий от окна Добавить/изменить тип поверки
    final int changeCalibrTypeID = 106;
    // Идентификатор событий от диалогового окна loginInProgram
    final int loginInProgramID = 150;
    // Идентификатор событий от диалогового окна programParametersForm
    final int programParametersFormID = 151;
    // Событие закрытия окна
    final String EVENT_CLOSE = "EVENT_CLOSE";
    // Событие регистрация
    final String EVENT_LOGIN = "EVENT_LOGIN";
    // Событие снятие регистрации
    final String EVENT_LOGOUT = "EVENT_LOGOUT";
    // Событие завершение с сохранением параметров
    final String EVENT_OK = "EVENT_OK";
    //  Отмена без выполнения каких либо действий
    final String EVENT_CANCEL = "EVENT_CANCEL";
    // Текущий пользователь
    String currentLogin;
    // Текущий уровень доступа
    String currentSecLevel;
    // Статус подключения к базе данных
    String currentStatusConnectToDatabase;
    // Объявление объекта connectToMySQL на основе класса JavaToMySQL
    // будет использоваться для получения данных из базы данных
    JavaToMySQL connectToMySQL;    
    // Диалог авторизации в системе учета
    LoginInProgram loginInProgram;
    // Диалог настройки параметров системы
    ProgramParametersForm programParametersForm;
    // Панель статуса работы программы
    JPanel statusPanel;
    // Поле для отображения ттекущего пользователя и уровня доступа
    JLabel statusUser;
    // Поле для отображения статуса соединения с базой данных
    JLabel statusConnection;
    // График месячных поверок
    ProgramReport monthreport;
    // Просроченные поверки
    ProgramReport expiredreport;
    // Пользователи
    ChangeServiceTable changeUsers;
    // Пользователи
    ServiceTable usersTable;
    // Группа приборов
    ServiceTable meterGroupTable;
    // Установка
    ServiceTable processTable;
    // Тип поверки
    ServiceTable calibrTypeTable;
    /*********************************************************
     *  MainForm конструктор
     *********************************************************/
    MainForm(String title, int windowWidth, int windowHeight) {
        // Вывод заголовка окна
        super(title);
        //
        panelWidth = windowWidth;
        panelHeight = windowHeight;
        //
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //
        menubar = new JMenuBar();
        setJMenuBar(menubar);
        //*******************************
        menuMeter = new JMenu("Прибор");
        //
        menuMeterInformation = new JMenuItem("Информация о приборах");
        menuMeterInformation.addActionListener(this);
        //
        menuMeter.add(menuMeterInformation);
        //
        menubar.add(menuMeter);
        //*****************************************
        menuReport = new JMenu("Отчеты");
        //
        menuMonthReport = new JMenuItem("График поверки на месяц");
        menuMonthReport.addActionListener(this);
        //
        menuReport.add(menuMonthReport);
        //
        menuExpiredReport = new JMenuItem("Просроченные поверки");
        menuExpiredReport.addActionListener(this);
        //
        menuReport.add(menuExpiredReport);
        //
        menubar.add(menuReport);
        //*****************************************
        menuService = new JMenu("Сервис");
        //
        JMenuItem menuServiceConnect = new JMenuItem("Настройка параметров системы учета");
        menuServiceConnect.addActionListener(this);
        menuService.add(menuServiceConnect);
        //
        menuServiceCreate = new JMenuItem("Создать базу данных");
        menuServiceCreate.addActionListener(this);
        menuService.add(menuServiceCreate);
        //
        JMenuItem menuServiceAddUser = new JMenuItem("Добавить/изменить пользователя");
        menuServiceAddUser.addActionListener(this);
        menuService.add(menuServiceAddUser);
        //
        menuServiceAddMeterGroup = new JMenuItem("Добавить/изменить группу приборов");
        menuServiceAddMeterGroup.addActionListener(this);
        menuService.add(menuServiceAddMeterGroup);
        //
        menuServiceAddProcess = new JMenuItem("Добавить/изменить установку");
        menuServiceAddProcess.addActionListener(this);
        menuService.add(menuServiceAddProcess);
        //
        menuServiceAddCalibrType = new JMenuItem("Добавить/изменить тип поверки");
        menuServiceAddCalibrType.addActionListener(this);
        menuService.add(menuServiceAddCalibrType);
        //
        menubar.add(menuService);
        //*****************************************
        menuLogin = new JMenu("Авторизация");
        //
        menuLoginItem = new JMenuItem("Авторизация в системе учета");
        menuLoginItem.addActionListener(this);
        menuLogin.add(menuLoginItem);
        //
        menubar.add(menuLogin);
        //***********************************************
        menuExit = new JMenu("Выход");
        //
        menuExitItem = new JMenuItem("Завершение работы программы");
        menuExitItem.addActionListener(this);
        menuExit.add(menuExitItem);
        //
        menubar.add(menuExit);
        //**********************************
        panel = new JPanel();
        panel.setBounds(panelX,panelY,panelWidth,panelHeight);
        panel.setBackground(panelBackground);
        panel.setOpaque(true);
        panel.setLayout(null);
        //
        add(panel);
        // Создание объекта connectToMySQL для получения
        // данных из базы данных MySQL
        connectToMySQL = new JavaToMySQL();
        // Проверка подключения к базе данных
        if(connectToMySQL.checkConnectToDatabase())
            currentStatusConnectToDatabase = "Подключен";
        else currentStatusConnectToDatabase = "Не подключен";
        // Чтение пользователей из таблицы meterusers
        connectToMySQL.readFromMeterUsers();
        // Текущий пользователь
        currentLogin = "";
        // Текущий уровень доступа
        currentSecLevel = "Нет";
        // Разрешение/запрет пунктов меню для текущего пользователя
        setAllowedMenuItem();
        //**********************************
        // Создание панели статуса в нижней части основного окна
        statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        //
        statusUser = new JLabel("");
        statusUser.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusUser);
        //
        statusConnection = new JLabel("");
        statusConnection.setHorizontalAlignment(SwingConstants.RIGHT);
        statusPanel.add(statusConnection);
        // Отображение текущего статуса соединения с базой данных
        // и текущего уровня достпа в панели статуса
        showStatus();
    }
    /*********************************************************
     * Отображение текущего статуса соединения с базой данных
     * и текущего уровня достпа в панели статуса
     *********************************************************/
    public void showStatus() {
        if(currentLogin.equals(""))
            statusUser.setText("Уровень доступа: " + currentSecLevel);
        else 
            statusUser.setText("Текущий пользователь: " + currentLogin + 
                "                  Уровень доступа: " + currentSecLevel);
        statusConnection.setText("                  Подключение к базе данных: " + currentStatusConnectToDatabase);
    }
    /*********************************************************
     *  Установка разрешенных пунктов меню для текущего уровня доступа
     *********************************************************/
    public void setAllowedMenuItem() {
        switch(currentSecLevel) {
            case "Нет":
                menuMeter.setEnabled(false);
                menuReport.setEnabled(false);
                menuService.setEnabled(false);
                break;
            case "Контролер":
                menuMeter.setEnabled(true);
                menuReport.setEnabled(true);
                menuService.setEnabled(false);
                break;
            case "Специалист":
                menuMeter.setEnabled(true);
                menuReport.setEnabled(true);
                menuService.setEnabled(false);
                break;
            case "Администратор":
                menuMeter.setEnabled(true);
                menuReport.setEnabled(true);
                menuService.setEnabled(true);
                break;
        }
    }
    /*********************************************************
     *  Прослушиватель событий в главной форме
     *********************************************************/
    @Override
    public void actionPerformed(ActionEvent e) {
        String labelCommand;
        boolean result;
        //
        int eGetID = e.getID();
        labelCommand = e.getActionCommand();
        switch(e.getID())  {
            case loginInProgramID:
                switch (labelCommand) {
                    case EVENT_LOGIN:
                        result = false;
                        // Проверка пароля по умолчанию
                        if(loginInProgram.currentLogin.equals("Admin")) {
                            if(loginInProgram.currentPassword.equals("Admin")) { 
                                result = true;
                                // Текущий пользователь
                                currentLogin = loginInProgram.currentLogin;
                                // Текущий уровень доступа
                                currentSecLevel = "Администратор";
                                //
                                loginInProgram.currentPassword = "";
                            }
                        }
                        // Проверка логинов и паролей из базы данных
                        if(!result) {
                            if(connectToMySQL.rsMeterUsersLogin.length>0) {
                                int i=0;
                                while((i<connectToMySQL.rsMeterUsersLogin.length) && (!result)) {
                                    if(loginInProgram.currentLogin.equals(connectToMySQL.rsMeterUsersLogin[i])) {
                                        if(loginInProgram.currentPassword.equals(connectToMySQL.rsMeterUsersPassword[i])) { 
                                            result = true;
                                            // Текущий пользователь
                                            currentLogin = connectToMySQL.rsMeterUsersLogin[i];
                                            // Текущий уровень доступа
                                            currentSecLevel = connectToMySQL.rsMeterUsersSecLevel[i];
                                            //
                                            loginInProgram.currentPassword = "";
                                        }
                                    }
                                    i++;
                                }
                            }
                        }
                        //
                        if(result) {
                            JOptionPane.showMessageDialog(panel, "Авторизация прошла успешно");
                            setAllowedMenuItem(); // Разрешение/запрет пунктов меню для текущего пользователя
                        }
                        else {
                            JOptionPane.showMessageDialog(panel, "Неверный логин или пароль");
                        }
                        // Отображение текущего статуса соединения с базой данных
                        // и текущего уровня достпа в панели статуса
                        showStatus();
                        // Закрыть диалоговое окно
                        loginInProgram.dispose();
                        break;
                    case EVENT_LOGOUT:
                        loginInProgram.currentLogin = "";
                        loginInProgram.currentPassword = "";
                        // Текущий пользователь
                        currentLogin = "";
                        // Текущий уровень доступа
                        currentSecLevel = "Нет";
                        setAllowedMenuItem(); // Разрешение/запрет пунктов меню для текущего пользователя
                        // Отображение текущего статуса соединения с базой данных
                        // и текущего уровня достпа в панели статуса
                        showStatus();
                        // Закрыть диалоговое окно
                        loginInProgram.dispose();
                        //
                        JOptionPane.showMessageDialog(panel, "Авторизация снята");
                        break;
                    case EVENT_CANCEL:
                        // Отображение текущего статуса соединения с базой данных
                        // и текущего уровня достпа в панели статуса
                        showStatus();
                        // Закрыть диалоговое окно
                        loginInProgram.dispose();
                        break;
                }
                break;
            case programParametersFormID:
                switch (labelCommand) {
                    case EVENT_OK:
                        if(programParametersForm != null) 
                            programParametersForm.dispose();
                        // Изменение параметров подключения к базе данных
                        connectToMySQL.changeConnectParameters();
                        // Проверка подключения к базе данных
                        if(connectToMySQL.checkConnectToDatabase())
                            currentStatusConnectToDatabase = "Подключен";
                        else currentStatusConnectToDatabase = "Не подключен";
                        // Отображение текущего статуса соединения с базой данных
                        // и текущего уровня достпа в панели статуса
                        showStatus();
                        //
                        this.setVisible(true);
                        JOptionPane.showMessageDialog(panel, "Параметры сохранены успешно");
                        break;
                    case EVENT_CANCEL:
                        if(programParametersForm != null) 
                            programParametersForm.dispose();
                        //
                        this.setVisible(true);
                        break;
                }
                break;
            case meterInformationID:
                switch (labelCommand) {
                    case EVENT_CLOSE:
                        if(meterinformation != null) 
                            meterinformation.dispose();
                        this.setVisible(true);
                        break;
                }
                break;
            case changeUserID:
                switch (labelCommand) {
                    case EVENT_CLOSE:
                        if(usersTable != null) 
                            usersTable.dispose();
                        this.setVisible(true);
                        break;
                }
                break;
            case changeMeterGroupID:
                switch (labelCommand) {
                    case EVENT_CLOSE:
                        if(meterGroupTable != null) 
                            meterGroupTable.dispose();
                        this.setVisible(true);
                        break;
                }
                break;
            case changeProcessID:
                switch (labelCommand) {
                    case EVENT_CLOSE:
                        if(processTable != null) 
                            processTable.dispose();
                        this.setVisible(true);
                        break;
                }
                break;
            case changeCalibrTypeID:
                switch (labelCommand) {
                    case EVENT_CLOSE:
                        if(calibrTypeTable != null) 
                            calibrTypeTable.dispose();
                        this.setVisible(true);
                        break;
                }
                break;
            case monthReportID:
                switch (labelCommand) {
                    case EVENT_CLOSE:
                        if(monthreport != null) 
                            monthreport.dispose();
                        this.setVisible(true);
                        break;
                }
                break;
            case expiredReportID:
                switch (labelCommand) {
                    case EVENT_CLOSE:
                        if(expiredreport != null) 
                            expiredreport.dispose();
                        this.setVisible(true);
                        break;
                }
                break;
            default:
                switch (labelCommand) {
                    // Нажат пункт меню "Информация о приборе"
                    case "Информация о приборах":
                        if(meterinformation != null) {
                            meterinformation.currentSecLevel = currentSecLevel;
                            meterinformation.setVisible(true);
                            //meterinformation.dispose();
                        }
                        else {
                            meterinformation = new MeterInformation(meterInformationID, 
                                    "Информация о приборах", panelWidth, panelHeight);
                            meterinformation.addListener(this);
                            meterinformation.pack();
                            meterinformation.setSize(panelWidth, panelHeight);
                            meterinformation.setMinimumSize(new Dimension(panelWidth, panelHeight));
                            meterinformation.setResizable(false);
                            meterinformation.setLocationRelativeTo(null);
                            meterinformation.currentSecLevel = currentSecLevel;
                            meterinformation.setVisible(true);
                        }
                        this.setVisible(false);
                        break;
                    // Нажат пункт меню "График поверки на месяц"
                    case "График поверки на месяц":
                        if(monthreport != null) {
                            monthreport.currentSecLevel = currentSecLevel;
                            monthreport.setVisible(true);
                            //meterinformation.dispose();
                        }
                        else {
                            monthreport = new ProgramReport(monthReportID, "График поверки на месяц", panelWidth, panelHeight);
                            monthreport.addListener(this);
                            monthreport.pack();
                            monthreport.setSize(panelWidth, panelHeight);
                            monthreport.setMinimumSize(new Dimension(panelWidth, panelHeight));
                            monthreport.setResizable(false);
                            monthreport.setLocationRelativeTo(null);
                            monthreport.currentSecLevel = currentSecLevel;
                            monthreport.setVisible(true);
                        }
                        break;
                    // Нажат пункт меню "Просроченные поверки"
                    case "Просроченные поверки":
                        if(expiredreport != null) {
                            expiredreport.currentSecLevel = currentSecLevel;
                            expiredreport.setVisible(true);
                            //meterinformation.dispose();
                        }
                        else {
                            expiredreport = new ProgramReport(expiredReportID, "Просроченные поверки", panelWidth, panelHeight);
                            expiredreport.addListener(this);
                            expiredreport.pack();
                            expiredreport.setSize(panelWidth, panelHeight);
                            expiredreport.setMinimumSize(new Dimension(panelWidth, panelHeight));
                            expiredreport.setResizable(false);
                            expiredreport.setLocationRelativeTo(null);
                            expiredreport.currentSecLevel = currentSecLevel;
                            expiredreport.setVisible(true);
                        }
                        break;
                    // Нажат пункт меню "Настройка параметров системы учета"
                    case "Настройка параметров системы учета":
                        if(programParametersForm != null) {
                            programParametersForm.setVisible(true);
                        }
                        else {
                            programParametersForm = new ProgramParametersForm(thisFrame);
                            //programParametersForm.setBounds(0,0,panelWidth,panelHeight);
                            programParametersForm.addListener(this);
                            programParametersForm.setResizable(false);
                            programParametersForm.setLocationRelativeTo(null);
                            programParametersForm.setVisible(true);
                        }
                        break;
                    // Нажат пункт меню "Добавить/изменить пользователя"
                    case "Добавить/изменить пользователя":
                        if(usersTable != null) {
                            usersTable.currentSecLevel = currentSecLevel;
                            usersTable.setVisible(true);
                            //meterinformation.dispose();
                        }
                        else {
                            usersTable = new ServiceTable(changeUserID, 
                                    "Добавить/изменить пользователя", panelWidth, panelHeight-100);
                            usersTable.addListener(this);
                            usersTable.pack();
                            usersTable.setSize(panelWidth, panelHeight-100);
                            usersTable.setMinimumSize(new Dimension(panelWidth, panelHeight-100));
                            usersTable.setResizable(false);
                            usersTable.setLocationRelativeTo(null);
                            usersTable.currentSecLevel = currentSecLevel;
                            usersTable.setVisible(true);
                        }
                        this.setVisible(false);
                        break;
                    // Нажат пункт меню "Добавить/изменить группу приборов"
                    case "Добавить/изменить группу приборов":
                        if(meterGroupTable != null) {
                            meterGroupTable.currentSecLevel = currentSecLevel;
                            meterGroupTable.setVisible(true);
                            //meterinformation.dispose();
                        }
                        else {
                            meterGroupTable = new ServiceTable(changeMeterGroupID, 
                                    "Добавить/изменить группу приборов", panelWidth, panelHeight-100);
                            meterGroupTable.addListener(this);
                            meterGroupTable.pack();
                            meterGroupTable.setSize(panelWidth, panelHeight-100);
                            meterGroupTable.setMinimumSize(new Dimension(panelWidth, panelHeight-100));
                            meterGroupTable.setResizable(false);
                            meterGroupTable.setLocationRelativeTo(null);
                            meterGroupTable.currentSecLevel = currentSecLevel;
                            meterGroupTable.setVisible(true);
                        }
                        this.setVisible(false);
                        break;
                    // Нажат пункт меню "Добавить/изменить установку"
                    case "Добавить/изменить установку":
                        if(processTable != null) {
                            processTable.currentSecLevel = currentSecLevel;
                            processTable.setVisible(true);
                            //meterinformation.dispose();
                        }
                        else {
                            processTable = new ServiceTable(changeProcessID, 
                                    "Добавить/изменить установку", panelWidth, panelHeight-100);
                            processTable.addListener(this);
                            processTable.pack();
                            processTable.setSize(panelWidth, panelHeight-100);
                            processTable.setMinimumSize(new Dimension(panelWidth, panelHeight-100));
                            processTable.setResizable(false);
                            processTable.setLocationRelativeTo(null);
                            processTable.currentSecLevel = currentSecLevel;
                            processTable.setVisible(true);
                        }
                        this.setVisible(false);
                        break;
                    // Нажат пункт меню "Добавить/изменить тип поверки"
                    case "Добавить/изменить тип поверки":
                        if(calibrTypeTable != null) {
                            calibrTypeTable.currentSecLevel = currentSecLevel;
                            calibrTypeTable.setVisible(true);
                            //meterinformation.dispose();
                        }
                        else {
                            calibrTypeTable = new ServiceTable(changeCalibrTypeID, 
                                    "Добавить/изменить тип поверки", panelWidth, panelHeight-100);
                            calibrTypeTable.addListener(this);
                            calibrTypeTable.pack();
                            calibrTypeTable.setSize(panelWidth, panelHeight-100);
                            calibrTypeTable.setMinimumSize(new Dimension(panelWidth, panelHeight-100));
                            calibrTypeTable.setResizable(false);
                            calibrTypeTable.setLocationRelativeTo(null);
                            calibrTypeTable.currentSecLevel = currentSecLevel;
                            calibrTypeTable.setVisible(true);
                        }
                        this.setVisible(false);
                        break;
                    // Нажат пункт меню "Создать базу данных"
                    case "Создать базу данных":
                        JavaToMySQL mysql =  new JavaToMySQL();
                        if(mysql.createDatabase())
                            if(mysql.createTables())
                                if(mysql.insertInitialData()) 
                                    JOptionPane.showMessageDialog(panel, "База данных создана успешно");
                                else JOptionPane.showMessageDialog(panel, "Ошибка при добавлении начальных данных");
                            else JOptionPane.showMessageDialog(panel, "Ошибка при создании таблиц базы данных");
                        else JOptionPane.showMessageDialog(panel, "Ошибка при создании базы данных");
                        mysql.readFromAllTable();
                        break;
                    // Нажат пункт меню "Авторизация в системе учета"
                    case "Авторизация в системе учета":
                        if(loginInProgram != null) {
                            loginInProgram.setVisible(true);
                        }
                        else {
                            loginInProgram = new LoginInProgram(thisFrame, currentLogin);
                            //loginInProgram.setBounds(0,0,panelWidth,panelHeight);
                            loginInProgram.addListener(this);
                            loginInProgram.setResizable(false);
                            loginInProgram.setLocationRelativeTo(null);
                            loginInProgram.setVisible(true);
                        }
                        break;
                    // Нажат пункт меню "Завершение работы программы"
                    case "Завершение работы программы":
                        System.exit(0);
                        break;
                }
            break;
        }
    }
    /*********************************************************
     *  
     *********************************************************/
}
