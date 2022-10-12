package meter;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * @author Andrey
 */
public class JavaToMySQL {
    // Объект для сохранения/чтения параметров программы
    public ProgramParameters programParameters;
    //
    private String urlCreateDatabase;
    private String url;
    private String user;
    private String password;
    //
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    //
    public int numCalibrType=0;
    public String rsCalibrType[];
    public int indexCalibrType[];
    //
    public int numMeterGroup=0;
    public String rsMeterGroup[];
    public int indexMeterGroup[];
    //
    public int numHeadInMeterGroup;
    public String rsHeadInMeterGroup[];
    //
    public int numProcess=0;
    public String rsProcess[];
    public int indexProcess[];
    //
    public int numSecLevel=0;
    public int indexSecLevel[];
    public String rsSecLevel[];
    // Пользователи в системе
    public int numMeterUsers=0;
    public String rsMeterUsersLogin[];
    public String rsMeterUsersPassword[];
    public String rsMeterUsersSecLevel[];
    //
    public String rsMeter[];
    // Строковый массив для хранения данных для текущей группы приборов
    public String[][] dataForCurrentMeterGroup; 
    //
    public int indexDataMeterGroup[];
    // 
    public int numRowsInMeterGroup;
    //
    public int numRowsInProgramReport;
    // Строковый массив для хранения данных для текущей группы приборов
    public String[][] dataForCurrentTable; 
    //
    public int indexDataTable[];
    //
    public int numRowsInTable;
    //
    private int numConstantParamInnMeterGroup = 10;
    /*********************************************************
     *  Конструктор класса
     *********************************************************/
    JavaToMySQL() {
        programParameters = new ProgramParameters();
        //
        changeConnectParameters();
    }
    /*********************************************************
     *  changeConnectParameters
     *********************************************************/
    public void changeConnectParameters() {
        programParameters.readProgramParameters();
        //
        urlCreateDatabase = "jdbc:mysql:"+programParameters.serverIP+":"+programParameters.serverPort;
        url = "jdbc:mysql:"+programParameters.serverIP+":"+programParameters.serverPort+"/"+programParameters.databaseName;
        user = programParameters.databaseUser;
        password = programParameters.databasePassword;
    }
    /*********************************************************
     *  Создание базы данных
     *********************************************************/
    public boolean createDatabase() {
        boolean result = false;
        //
        String dropDatabase = "DROP DATABASE IF EXISTS meter;";
        String createDatabase = "CREATE DATABASE meter;";
        String gPost = "";
        try {
            con = DriverManager.getConnection(urlCreateDatabase,user,password);
            if(con != null) {
                
                stmt = con.createStatement();
                if(stmt != null) {
                    stmt.execute(dropDatabase);
                    stmt.execute(createDatabase);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  Проверка подключения к базе данных
     *********************************************************/
    public boolean checkConnectToDatabase() {
        boolean result = false;
        //
        try {
            con = DriverManager.getConnection(urlCreateDatabase,user,password);
            if(con != null) {
                
                stmt = con.createStatement();
                if(stmt != null) {
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  создание таблиц в базе данных
     *********************************************************/
    public boolean createTables() {
        boolean result = false;
        //
        String createTableSecLevel = "CREATE TABLE seclevel (\n" +
                "	seclevelID int(6) NOT NULL AUTO_INCREMENT,\n" +
                "	nameSecLevel varchar(100) NOT NULL default 'Уровень доступа',\n" +
                "	PRIMARY KEY  (seclevelID)\n" +
                "       );";
        //
        String createTableMeterUsers = "CREATE TABLE meterusers (\n" +
                "	meterUsersID int(6) NOT NULL AUTO_INCREMENT,\n" +
                "	surname varchar(100) NOT NULL default 'Фамилия',\n" +
                "	firstname varchar(100) NOT NULL default 'Имя',\n" +
                "	login varchar(100) NOT NULL default 'Логин',\n" +
                "	password varchar(100) NOT NULL default 'Пароль',\n" +
                "       seclevelID int(6) NOT NULL default '1',\n" +
                "	PRIMARY KEY  (meterUsersID),\n" +
                "	FOREIGN KEY (seclevelID) \n" +
                "                       REFERENCES seclevel (seclevelID) \n" +
                "                                  ON DELETE CASCADE ON UPDATE CASCADE\n" +
                "       );";
        //
        String createTableCalibrType = "CREATE TABLE calibrtype (\n" +
                "	calibrTypeID int(6) NOT NULL AUTO_INCREMENT,\n" +
                "	nameCalibrType varchar(100) NOT NULL default 'Тип поверки',\n" +
                "	PRIMARY KEY  (calibrTypeID)\n" +
                "       );";
        //
        String createTableMeterGroup = "CREATE TABLE metergroup (\n" +
                "	meterGroupID int(6) NOT NULL AUTO_INCREMENT,\n" +
                "	nameMeterGroup varchar(100) NOT NULL default 'Группа прибора',\n" +
                "       numGroupParam int(6) NOT NULL default '1',\n" +
                "       head1 varchar(100),\n" +
                "       head2 varchar(100),\n" +
                "       head3 varchar(100),\n" +
                "       head4 varchar(100),\n" +
                "       head5 varchar(100),\n" +
                "       head6 varchar(100),\n" +
                "       head7 varchar(100),\n" +
                "       head8 varchar(100),\n" +
                "       head9 varchar(100),\n" +
                "       head10 varchar(100),\n" +
                "       head11 varchar(100),\n" +
                "       head12 varchar(100),\n" +
                "       head13 varchar(100),\n" +
                "       head14 varchar(100),\n" +
                "       head15 varchar(100),\n" +
                "       head16 varchar(100),\n" +
                "       head17 varchar(100),\n" +
                "       head18 varchar(100),\n" +
                "       head19 varchar(100),\n" +
                "       head20 varchar(100),\n" +
                "	PRIMARY KEY  (meterGroupID)\n" +
                "       );";
        //
        String createTableProcess = "CREATE TABLE process (\n" +
                "	processID int(6) NOT NULL AUTO_INCREMENT,\n" +
                "	nameProcess varchar(100) NOT NULL default 'Установка',\n" +
                "	PRIMARY KEY  (processID)\n" +
                "       );";
        //
        String createTableMeter = "CREATE TABLE meter (\n" +
                "	meterID int(6) NOT NULL AUTO_INCREMENT,\n" +
                "	nameMeter varchar(100) NOT NULL default 'Прибор',\n" +
                "       meterGroupID int(6) NOT NULL default '1',\n" +
                "       processID int(6) NOT NULL default '1',\n" +
                "       dateIn date,\n" +
                "       plantNum varchar(100),\n" +
                "       plantMaker varchar(100),\n" +
                "       calibrTypeID int(6) NOT NULL default '1',\n" +
                "       dateLast date,\n" +
                "       calibrPeriod int(6),\n" +
                "       dateNext date,\n" +
                "       param1 varchar(100),\n" +
                "       param2 varchar(100),\n" +
                "       param3 varchar(100),\n" +
                "       param4 varchar(100),\n" +
                "       param5 varchar(100),\n" +
                "       param6 varchar(100),\n" +
                "       param7 varchar(100),\n" +
                "       param8 varchar(100),\n" +
                "       param9 varchar(100),\n" +
                "       param10 varchar(100),\n" +
                "       param11 varchar(100),\n" +
                "       param12 varchar(100),\n" +
                "       param13 varchar(100),\n" +
                "       param14 varchar(100),\n" +
                "       param15 varchar(100),\n" +
                "       param16 varchar(100),\n" +
                "       param17 varchar(100),\n" +
                "       param18 varchar(100),\n" +
                "       param19 varchar(100),\n" +
                "       param20 varchar(100),\n" +
                "	PRIMARY KEY  (meterID),\n" +
                "       FOREIGN KEY (meterGroupID) \n" +
                "                       REFERENCES metergroup (meterGroupID) \n" +
                "                                  ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "       FOREIGN KEY (processID) \n" +
                "                       REFERENCES process (processID) \n" +
                "                                  ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "       FOREIGN KEY (calibrTypeID) \n" +
                "                       REFERENCES calibrtype (calibrTypeID) \n" +
                "                                  ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    stmt.execute(createTableSecLevel);
                    stmt.execute(createTableMeterUsers);
                    stmt.execute(createTableCalibrType);
                    stmt.execute(createTableMeterGroup);
                    stmt.execute(createTableProcess);
                    stmt.execute(createTableMeter);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  Наполнение таблиц первоначальными данными
     *********************************************************/
    public boolean insertInitialData() {
        boolean result = false;
        //
        String setCharacterSet = "SET CHARACTER SET cp1251;";
        //
        String insertDataIntoSecLevel[] = {
            "INSERT INTO seclevel VALUES (1,'Контролер');",
            "INSERT INTO seclevel VALUES (2,'Специалист');",
            "INSERT INTO seclevel VALUES (3,'Администратор');"
        };
        //
        String insertDataIntoMeterUsers[] = {
            "INSERT INTO meterusers VALUES (1, 'Admin', 'Admin', 'Admin', 'Admin', '3');",
            "INSERT INTO meterusers VALUES (2, 'Spec', 'Spec', 'Spec', 'Spec', '2');",
            "INSERT INTO meterusers VALUES (3, 'Control', 'Control', 'Control', 'Control', '1');"
        };
        //
        String insertDataIntoMeterGroup[] = {
            "INSERT INTO metergroup VALUES (1,'Расходомеры', '16', 'Диаметр d, мм', 'Диаметр D, мм', 'Измеряемая среда', 'Температура', 'Давление кгс/см2', 'Плотность кг/м3', 'Вязкость кинетическая *0.000001', 'Вязкость динамическая *0.000001', 'Плотность (при 20) кг/м3', 'Коэффициент расхода', 'Показатель адиабаты', 'Kt', 'K', 'Давление (барометрическое)', 'Шкала', 'Перепад', 'h17', 'h18', 'h19', 'h20');",
            "INSERT INTO metergroup VALUES (2, 'Приборы температуры', '3', 'Предел измерения', 'Класс точности', 'Градуировка', 'h4', 'h5', 'h6', 'h7', 'h8', 'h9', 'h10', 'h11', 'h12', 'h13', 'h14', 'h15', 'h16', 'h17', 'h18', 'h19', 'h20');",
            "INSERT INTO metergroup VALUES (3, 'Приборы давления', '3', 'Предел измерения Р кг/см2', 'Класс точности', 'Измеряемая среда', 'h4', 'h5', 'h6', 'h7', 'h8', 'h9', 'h10', 'h11', 'h12', 'h13', 'h14', 'h15', 'h16', 'h17', 'h18', 'h19', 'h20');",
            "INSERT INTO metergroup VALUES (4, 'Уровнемеры', '2', 'Предел измерения',  'Класс точности', 'h3', 'h4', 'h5', 'h6', 'h7', 'h8', 'h9', 'h10', 'h11', 'h12', 'h13', 'h14', 'h15', 'h16', 'h17', 'h18', 'h19', 'h20');"
        };
        //
        String insertDataIntoCalibrType[] = {
            "INSERT INTO calibrtype VALUES (1,'Ведомственная поверка');",
            "INSERT INTO calibrtype VALUES (2, 'Государственная поверка');"
        };
        //
        String insertDataIntoProcess[] = {
            "INSERT INTO process VALUES (1,'Первичная переработка');",
            "INSERT INTO process VALUES (2,'Каталитический крекинг');",
            "INSERT INTO process VALUES (3,'Гидроочистка');"
        };
        //
        String insertDataIntoMeter[] = {
            "INSERT INTO meter VALUES (1,'FC-1', 1, 1, '2010-03-03', '18601', '-', 1, '2015-03-03', 1, '2016-03-03', '173', '40', 'Нефть', '20', '16', '840', '-', '484.4', '-',  '0.6132', '-', '1', '-', '-',  '630 м3/ч', '6300 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (2,'FC-2', 1, 2, '2012-11-01', '234855', '-', 2, '2015-11-01', 1, '2016-11-01', '22.76', '100', 'Вода', '70', '12', '978.3', '-', '41.5', '-',  '0.5987', '-', '-', '-', '-',  '6.3 м3/ч', '2500 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (3,'FI-18', 1, 3, '2015-10-12', '6725-F', '-', 1, '2016-10-12', 1, '2017-10-12', '109.70', '350', 'Пар', '190', '10', '4.878', '-', '1.610', '-',  '-', '-', '-', '-', '-',  '10 т/ч', '2500 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (4,'FI-121', 1, 3, '2009-05-24', '43762', '-', 1, '2015-05-24', 1, '2016-05-24', '48.62', '96.16', 'Мазут', '100', '12', '915.6', '-', '1639', '-',  '0.8010', '-', '1.0013', '-', '-',  '80 м3/ч', '16000 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (5,'FI-121', 1, 2, '2016-10-01', '762-F', '-', 1, '2016-10-01', 1, '2017-10-01', '6.34', '49', 'Технический воздух', '20', '5.026', '-', '-', '1.85', '1,2044',  '0.7353', '1.41', '1', '-', '-',  '23.35 м3/ч', '0.1 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (6,'FI-20', 1, 1, '2015-08-23', '675987', '-', 2, '2016-08-23', 1, '2017-08-23', '47.09', '80', 'Дизельное топливо', '40', '1', '825', '-', '4854,29', '-',  '0.6568', '-', '1', '-', '-',  '32 м3/ч', '2500 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (7,'FI-11', 1, 1, '2014-07-18', '987-F', '-', 2, '2016-07-18', 1, '2017-07-18', '98.49', '150', 'Бензин', '50', '10', '658', '44.08', '-', '-',  '0.6694', '-', '1.004', '-', '-',  '160 м3/ч', '2500 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (8,'FC-8', 1, 2, '2012-06-01', '45345', '-', 1, '2016-06-01', 1, '2017-06-01', '83.86', '141.85', 'Нефть', '40', '25', '819', '-', '585.8', '-',  '0.6512', '-', '1.00033', '-', '-',  '160 м3/ч', '6300 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (9,'FI-138', 1, 3, '2010-05-18', '45-F', '-', 1, '2016-05-18', 1, '2017-05-18', '298.6', '406', 'Вода оборотная', '30', '2.5', '995.76', '-', '82', '-',  '0.7101', '-', '1.00017', '-', '-',  '2000 м3/ч', '6300 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (10,'FI-55', 1, 2, '2011-04-10', '45-F', '-', 2, '2016-04-10', 1, '2017-04-10', '57.51', '100', 'Бензин', '45', '12', '719', '-', '0.00043', '-',  '-', '-', '1.0003', '-', '-',  '50 м3/ч', '2500 кгс/м2', '', '', '', ''); ",
            "INSERT INTO meter VALUES (11,'TI-1', 2, 2, '2011-02-18', 'T154', '-', 1, '2015-02-18', 2, '2017-02-18', '200', '0.1', 'Pt100',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (12,'TI-18', 2, 1, '2015-11-05', 'T354', '-', 1, '2015-11-05', 2, '2017-11-05', '100', '0.1', 'Pt100',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (13,'TC-115', 2, 1, '2014-10-29', '745321', '-', 1, '2014-10-29', 2, '2016-10-29', '150', '0.1', 'Pt100',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (14,'TC-115', 2, 2, '2009-09-15', '321-T', '-', 1, '2015-09-15', 2, '2017-09-15', '400', '0.1', 'Pt100',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (15,'TI-23', 2, 2, '2013-08-12', '532888', '-', 1, '2015-08-12', 2, '2017-08-12', '500', '0.1', 'Pt100',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (16,'TI-85', 2, 3, '2012-10-01', '28974', '-', 1, '2015-10-01', 2, '2017-10-01', '250', '0.1', 'XK',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (17,'TC-111', 2, 2, '2015-07-25', '32752', '-', 2, '2015-07-25', 1, '2016-07-25', '300', '0.1', 'XK',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (18,'TC-34', 2, 1, '2016-06-17', '752-T', '-', 1, '2016-06-17', 1, '2017-06-17', '200', '0.1', 'XK',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (19,'TI-15', 2, 2, '2007-05-04', '32752', '-', 2, '2015-05-04', 2, '2017-05-04', '600', '0.1', 'Pt100',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (20,'TI-151', 2, 1, '2008-04-05', '32752', '-', 2, '2014-04-05', 2, '2016-04-05', '400', '0.1', 'Pt100',  '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (21,'PI-1', 3, 3, '2013-10-01', '1328-P', '-', 1, '2013-02-18', 2, '2015-02-18', '10', '0.2', 'Нефть', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (22,'PC-25', 3, 2, '2011-04-01', '965743', '-', 2, '2014-04-01', 2, '2016-04-01', '5', '0.1', 'Техническая вода', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (23,'PI-125', 3, 1, '2010-05-18', '965-P', '-', 2, '2015-05-18', 2, '2017-05-18', '1', '0.01', 'Техническая воздух', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (24,'PI-12', 3, 1, '2011-06-22', '965-P', '-', 2, '2015-06-22', 1, '2016-06-22', '10', '0.1', 'Бензин', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (25,'PI-243', 3, 2, '2012-07-16', '123824', '-', 1, '2015-07-16', 2, '2017-07-16', '5', '0.1', 'Дизельное топливо', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (26,'PI-254', 3, 2, '2013-07-26', '1238-ДТ', '-', 1, '2015-07-26', 2, '2017-07-26', '10', '0.1', 'Дизельное топливо', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (27,'PI-324', 3, 3, '2014-12-12', '1238-ДТ', '-', 1, '2014-12-12', 2, '2016-12-12', '10', '0.1', 'Нефть', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (28,'PC-32', 3, 2, '2016-10-11', '1238-ДТ', '-', 1, '2016-10-11', 2, '2018-10-11', '3', '0.01', 'Технический воздух', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (29,'PI-114', 3, 1, '2006-11-16', '935216', '-', 1, '2015-11-16', 2, '2017-11-16', '10', '0.1', 'Нефть', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (30,'PC-95', 3, 2, '2007-09-09', '1238-ДТ', '-', 1, '2016-09-09', 1, '2017-09-09', '5', '0.1', 'Бензин', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (31,'LI-1', 4, 3, '2013-10-01', '1328-L', '-', 1, '2013-02-18', 2, '2015-02-18', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (32,'LC-187', 4, 2, '2012-11-01', '328-LC', '-', 2, '2014-11-01', 2, '2016-11-01', '1000', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (33,'LC-200', 4, 2, '2016-10-13', '674298', '-', 2, '2016-10-13', 2, '2018-10-13', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (34,'LI-212', 4, 1, '2011-10-03', '674-I', '-', 1, '2016-10-03', 2, '2018-10-03', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (35,'LI-311', 4, 1, '2010-10-09', '915-L', '-', 1, '2016-10-09', 2, '2018-10-09', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (36,'LC-19', 4, 2, '2009-08-07', '25567', '-', 1, '2015-08-07', 2, '2017-08-07', '1000', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (37,'LI-299', 4, 3, '2008-09-11', '567-LI', '-', 1, '2014-09-11', 2, '2016-09-11', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (38,'LC-311', 4, 3, '2007-08-05', '750932', '-', 2, '2015-08-05', 2, '2017-08-05', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (39,'LI-303', 4, 2, '2002-07-06', '567-LI', '-', 1, '2015-07-06', 2, '2017-07-06', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); ",
            "INSERT INTO meter VALUES (40,'LI-421', 4, 1, '2015-06-17', '567-LI', '-', 1, '2015-06-17', 2, '2017-06-17', '100', '0.1', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', '', ''); "
        };
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {

                
                stmt = con.createStatement();
                if(stmt != null) {
                    //stmt.execute(setCharacterSet);
                    for(int i=0;i< insertDataIntoSecLevel.length;i++)
                        stmt.execute(insertDataIntoSecLevel[i]);
                    for(int i=0;i<insertDataIntoMeterUsers.length;i++)
                        stmt.execute(insertDataIntoMeterUsers[i]);
                    for(int i=0;i<insertDataIntoMeterGroup.length;i++)
                        stmt.execute(insertDataIntoMeterGroup[i]);
                    for(int i=0;i<insertDataIntoCalibrType.length;i++)
                        stmt.execute(insertDataIntoCalibrType[i]);
                    for(int i=0;i<insertDataIntoProcess.length;i++)
                        stmt.execute(insertDataIntoProcess[i]);
                    for(int i=0;i<insertDataIntoMeter.length;i++)
                        stmt.execute(insertDataIntoMeter[i]);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  Чтение данных из всех таблиц
     *********************************************************/
    public void readFromAllTable() {
        String showSecLevel = "select * from seclevel";
        String showMeterUsers = "select * from meterusers";
        String showCalibrType = "select * from calibrtype";
        String showMeterGroup = "select * from metergroup";
        String showProcess = "select * from process";
        String showMeter = "select * from meter";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showSecLevel);
                    if(rs!=null) {
                        while(rs.next()) {
                            String gPost = rs.getString("nameSecLevel");
                            //JPasswordField.println("nameSecLevel : " + gPost);
                        }
                    }
                    //
                    rs = stmt.executeQuery(showMeterUsers);
                    if(rs!=null) {
                        while(rs.next()) {
                            String gPost = rs.getString("login");
                            //JPasswordField.println("login : " + gPost);
                        }
                    }
                    //
                    rs = stmt.executeQuery(showCalibrType);
                    if(rs!=null) {
                        while(rs.next()) {
                            String gPost = rs.getString("nameCalibrType");
                            //JPasswordField.println("nameCalibrType : " + gPost);
                        }
                    }
                    //
                    rs = stmt.executeQuery(showMeterGroup);
                    if(rs!=null) {
                        while(rs.next()) {
                            String gPost = rs.getString("nameMeterGroup");
                            //JPasswordField.println("nameMeterGroup : " + gPost);
                        }
                    }
                    //
                    rs = stmt.executeQuery(showProcess);
                    if(rs!=null) {
                        while(rs.next()) {
                            String gPost = rs.getString("nameProcess");
                            //JPasswordField.println("nameProcess : " + gPost);
                        }
                    }
                    //
                    rs = stmt.executeQuery(showMeter);
                    if(rs!=null) {
                        while(rs.next()) {
                            String gPost = rs.getString("nameMeter");
                            //JPasswordField.println("nameMeter : " + gPost);
                        }
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Чтение данных из таблицы metergroup
     *********************************************************/
    public void readFromMeterGroup() {
        String showTable = "select * from metergroup";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showTable);
                    if(rs!=null) {
                        rs.last();
                        numMeterGroup = rs.getRow();
                        rsMeterGroup = new String[numMeterGroup];
                        indexMeterGroup = new int[numMeterGroup];
                        int i=0;
                        rs.first();
                        rsMeterGroup[i] = rs.getString("nameMeterGroup");
                        indexMeterGroup[i] = rs.getInt("meterGroupID");
                        i++;
                        while(rs.next()) {
                            rsMeterGroup[i] = rs.getString("nameMeterGroup");
                            indexMeterGroup[i] = rs.getInt("meterGroupID");
                            i++;
                        }
                    }
                    else {
                        numMeterGroup = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Чтение данных из таблицы process
     *********************************************************/
    public void readFromProcess() {
        String showProcess = "select * from process";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showProcess);
                    if(rs!=null) {
                        rs.last();
                        numProcess = rs.getRow();
                        rsProcess = new String[numProcess];
                        indexProcess = new int[numProcess];
                        int i=0;
                        rs.first();
                        rsProcess[i] = rs.getString("nameProcess");
                        indexProcess[i] = rs.getInt("processID");
                        i++;
                        while(rs.next()) {
                            rsProcess[i] = rs.getString("nameProcess");
                            indexProcess[i] = rs.getInt("processID");
                            //System.out.println("nameProcess : " + rsProcess[i]);
                            i++;
                        }
                    }
                    else {
                        numProcess = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Чтение данных из таблицы calibrtype
     *********************************************************/
    public void readFromCalibrType() {
        String showCalibrType = "select * from calibrtype";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showCalibrType);
                    if(rs!=null) {
                        rs.last();
                        numCalibrType = rs.getRow();
                        rsCalibrType = new String[numCalibrType];
                        indexCalibrType = new int[numCalibrType];
                        int i=0;
                        rs.first();
                        rsCalibrType[i] = rs.getString("nameCalibrType");
                        indexCalibrType[i] = rs.getInt("calibrTypeID");
                        i++;
                        while(rs.next()) {
                            rsCalibrType[i] = rs.getString("nameCalibrType");
                            indexCalibrType[i] = rs.getInt("calibrTypeID");
                            //System.out.println("nameCalibrType : " + rsCalibrType[i]);
                            i++;
                        }
                    }
                    else {
                        numCalibrType = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     * Чтение заголовков для группы приборов 
     * из таблицы metergroup
     *********************************************************/
    public void readHeadFromMeterGroup(String nMeterGroup) {
        String showHeadFromMeterGroup = "select * from metergroup where nameMeterGroup = '" + nMeterGroup +"'";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showHeadFromMeterGroup);
                    numHeadInMeterGroup = 0;
                    if(rs!=null) {
                        while(rs.next()) {
                            numHeadInMeterGroup = rs.getInt("numGroupParam");
                            if(numHeadInMeterGroup>0) {
                                rsHeadInMeterGroup = new String[numHeadInMeterGroup];
                                for(int i=0;i<numHeadInMeterGroup;i++)
                                    rsHeadInMeterGroup[i] = rs.getString(4+i);
                            }
                        }
                    }
                    else {
                        numHeadInMeterGroup = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     * Чтение параметров для приборов 
     * из таблицы meter
     *********************************************************/
    public void readParametersFromMeter(int numColumns, String meterGroupID, String processID, String calibrTypeID) {
        String showParametersFromMeterGroup1 = "select * from meter where meterGroupID = " + meterGroupID +" AND "
                + "processID  = " + processID  +" AND " + "calibrTypeID  = " + calibrTypeID;
        String showParametersFromMeterGroup2 = "select * from meter where meterGroupID = " + meterGroupID +" AND "
                + "processID  = " + processID;
        String showParametersFromMeterGroup3 = "select * from meter where meterGroupID = " + meterGroupID +" AND "
                + "calibrTypeID  = " + calibrTypeID;
        String showParametersFromMeterGroup4 = "select * from meter where meterGroupID = " + meterGroupID;
        String showParametersFromMeterGroup = showParametersFromMeterGroup1;
        //
        SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
        
        //
        if(processID.equals("0") && calibrTypeID.equals("0"))
            showParametersFromMeterGroup = showParametersFromMeterGroup4;
        else if(processID.equals("0"))
            showParametersFromMeterGroup = showParametersFromMeterGroup3;
        else if(calibrTypeID.equals("0"))
            showParametersFromMeterGroup = showParametersFromMeterGroup2;
            
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showParametersFromMeterGroup);
                    numRowsInMeterGroup = 0;
                    if(rs!=null) {
                        rs.last();
                        numRowsInMeterGroup = rs.getRow();
                        rs.first();
                        dataForCurrentMeterGroup = new String[numRowsInMeterGroup][numColumns];
                        indexDataMeterGroup = new int[numRowsInMeterGroup];
                        int i=0;
                        indexDataMeterGroup[i] = rs.getInt("meterID");
                        int j = 0;
                        dataForCurrentMeterGroup[i][j] = rs.getString("nameMeter");
                        j++;
                        dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("meterGroupID"));
                        j++;
                        dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("processID"));
                        j++;
                        dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateIn"));
                        j++;
                        dataForCurrentMeterGroup[i][j] = rs.getString("plantNum");
                        j++;
                        dataForCurrentMeterGroup[i][j] = rs.getString("plantMaker");
                        j++;
                        dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrTypeID"));
                        j++;
                        dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateLast"));
                        j++;
                        dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrPeriod"));
                        j++;
                        dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateNext"));
                        j++;
                        for(;j<numColumns;j++) {
                           dataForCurrentMeterGroup[i][j] = rs.getString("param" + 
                                   Integer.toString(j-numConstantParamInnMeterGroup+1));
                        }
                        //
                        i++;
                        while(rs.next()) {
                            
                            indexDataMeterGroup[i] = rs.getInt("meterID");
                            j = 0;
                            dataForCurrentMeterGroup[i][j] = rs.getString("nameMeter");
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("meterGroupID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("processID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateIn"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = rs.getString("plantNum");
                            j++;
                            dataForCurrentMeterGroup[i][j] = rs.getString("plantMaker");
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrTypeID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateLast"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrPeriod"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateNext"));
                            j++;
                            for(;j<numColumns;j++) {
                               dataForCurrentMeterGroup[i][j] = rs.getString("param" + 
                                       Integer.toString(j-numConstantParamInnMeterGroup+1));
                            }
                            i++;
                        }
                    }
                    else {
                        numRowsInMeterGroup = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Чтение данных из таблицы seclevel
     *********************************************************/
    public void readFromSecLevel() {
        String showSecLevel = "select * from seclevel";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showSecLevel);
                    if(rs!=null) {
                        rs.last();
                        numSecLevel = rs.getRow();
                        rsSecLevel = new String[numSecLevel];
                        indexSecLevel = new int[numSecLevel];
                        int i=0;
                        rs.first();
                        rsSecLevel[i] = rs.getString("nameSecLevel");
                        indexSecLevel[i] = rs.getInt("secLevelID");
                        i++;
                        while(rs.next()) {
                            rsSecLevel[i] = rs.getString("nameSecLevel");
                            indexSecLevel[i] = rs.getInt("secLevelID");
                            //
                            i++;
                        }
                    }
                    else {
                        numSecLevel = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Чтение данных из таблицы meterusers
     *********************************************************/
    public void readFromMeterUsers() {
        String showMeterUsers = "select * from meterusers";
        // Чтение данных из таблицы seclevel
        readFromSecLevel();
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showMeterUsers);
                    if(rs!=null) {
                        rs.last();
                        numMeterUsers = rs.getRow();
                        rsMeterUsersLogin = new String[numMeterUsers];
                        rsMeterUsersPassword = new String[numMeterUsers];
                        rsMeterUsersSecLevel = new String[numMeterUsers];
                        int i=0;
                        rs.first();
                        rsMeterUsersLogin[i] = rs.getString("login");
                        rsMeterUsersPassword[i] = rs.getString("password");
                        rsMeterUsersSecLevel[i] = returnSecLevel(rs.getInt("secLevelID"));
                        i++;
                        while(rs.next()) {
                            rsMeterUsersLogin[i] = rs.getString("login");
                            rsMeterUsersPassword[i] = rs.getString("password");
                            rsMeterUsersSecLevel[i] = returnSecLevel(rs.getInt("secLevelID"));
                            //
                            i++;
                        }
                    }
                    else {
                        numMeterUsers = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Возвращает уровень доступа в соответствии
     *  с таблицей seclevel 
     *********************************************************/
    public String returnSecLevel(int indSecLevel) {
        String result = "Нет";
        for(int i=0;i<rsSecLevel.length;i++) {
            if(indexSecLevel[i] == indSecLevel)
                result = rsSecLevel[i];
        }
        return result;
    }
    /*********************************************************
     *  Изменение данных в таблице meter
     *********************************************************/
    public boolean changeMeter(String changeMeter) {
        boolean result = false;
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    stmt.execute(changeMeter);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  Изменение данных в таблице meter
     *********************************************************/
    public boolean addMeter(String changeMeter) {
        boolean result = false;
        //
        String setCharacterSet = "SET CHARACTER SET cp1251;";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    //stmt.execute(setCharacterSet);
                    stmt.execute(changeMeter);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  Удаление данных из таблицы meter
     *********************************************************/
    public boolean removeMeter(int deleteMeter) {
        boolean result = false;
        //
        String forDelete = "DELETE FROM meter WHERE meterid = " + Integer.toString(deleteMeter)+ "; ";
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    stmt.execute(forDelete);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     * Чтение параметров для приборов 
     * из таблицы meter
     *********************************************************/
    public void readParametersFromMeterForMonthReport(int numColumns, String meterGroupID, String processID, 
            String calibrTypeID, String startDate, String endDate) {
        String showParametersFromMeterGroup = "select * from meter";
        if(!meterGroupID.equals("0"))
            showParametersFromMeterGroup = showParametersFromMeterGroup + " where meterGroupID = " + meterGroupID;
        if(!processID.equals("0"))
            if(!meterGroupID.equals("0"))
                showParametersFromMeterGroup = showParametersFromMeterGroup + " AND processID = " + processID;
            else showParametersFromMeterGroup = showParametersFromMeterGroup + " where processID = " + processID;
        if(!calibrTypeID.equals("0"))
            if(meterGroupID.equals("0") && processID.equals("0"))
                showParametersFromMeterGroup = showParametersFromMeterGroup + " where calibrTypeID = " + calibrTypeID;
            else showParametersFromMeterGroup = showParametersFromMeterGroup + " AND calibrTypeID = " + calibrTypeID;
        if(meterGroupID.equals("0") && processID.equals("0") && calibrTypeID.equals("0"))
            showParametersFromMeterGroup = showParametersFromMeterGroup + " where dateNext >= '" + startDate
                    + "' AND dateNext < '" + endDate + "'";
        else
            showParametersFromMeterGroup = showParametersFromMeterGroup + " AND dateNext >= '" + startDate
                    + "' AND dateNext < '" + endDate + "'";
        showParametersFromMeterGroup = showParametersFromMeterGroup +" ORDER BY dateNext;";
        //
        SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showParametersFromMeterGroup);
                    numRowsInProgramReport = 0;
                    if(rs!=null) {
                        rs.last();
                        
                        numRowsInProgramReport = rs.getRow();
                        if(numRowsInProgramReport > 0) {
                            rs.first();
                            dataForCurrentMeterGroup = new String[numRowsInProgramReport][numColumns];
                            indexDataMeterGroup = new int[numRowsInProgramReport];
                            int i=0;
                            indexDataMeterGroup[i] = rs.getInt("meterID");
                            int j = 0;
                            dataForCurrentMeterGroup[i][j] = rs.getString("nameMeter");
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("meterGroupID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("processID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateIn"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = rs.getString("plantNum");
                            j++;
                            dataForCurrentMeterGroup[i][j] = rs.getString("plantMaker");
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrTypeID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateLast"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrPeriod"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateNext"));
                            j++;
                            //
                            i++;
                            while(rs.next()) {
                                indexDataMeterGroup[i] = rs.getInt("meterID");
                                j = 0;
                                dataForCurrentMeterGroup[i][j] = rs.getString("nameMeter");
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("meterGroupID"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("processID"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateIn"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = rs.getString("plantNum");
                                j++;
                                dataForCurrentMeterGroup[i][j] = rs.getString("plantMaker");
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrTypeID"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateLast"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrPeriod"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateNext"));
                                j++;
                                //
                                i++;
                            }
                        }
                    }
                    else {
                        numRowsInProgramReport = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     * Чтение параметров для приборов 
     * из таблицы meter
     *********************************************************/
    public void readParametersFromMeterForExpiredReport(int numColumns, String meterGroupID, String processID, 
            String calibrTypeID, String currentDate) {
        String showParametersFromMeterGroup = "select * from meter";
        if(!meterGroupID.equals("0"))
            showParametersFromMeterGroup = showParametersFromMeterGroup + " where meterGroupID = " + meterGroupID;
        if(!processID.equals("0"))
            if(!meterGroupID.equals("0"))
                showParametersFromMeterGroup = showParametersFromMeterGroup + " AND processID = " + processID;
            else showParametersFromMeterGroup = showParametersFromMeterGroup + " where processID = " + processID;
        if(!calibrTypeID.equals("0"))
            if(meterGroupID.equals("0") && processID.equals("0"))
                showParametersFromMeterGroup = showParametersFromMeterGroup + " where calibrTypeID = " + calibrTypeID;
            else showParametersFromMeterGroup = showParametersFromMeterGroup + " AND calibrTypeID = " + calibrTypeID;
        if(meterGroupID.equals("0") && processID.equals("0") && calibrTypeID.equals("0"))
            showParametersFromMeterGroup = showParametersFromMeterGroup + " where dateNext < '" + currentDate + "'";
        else
            showParametersFromMeterGroup = showParametersFromMeterGroup + " AND dateNext < '" + currentDate + "'";
        showParametersFromMeterGroup = showParametersFromMeterGroup +" ORDER BY dateNext;";
        //
        SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(showParametersFromMeterGroup);
                    numRowsInProgramReport = 0;
                    if(rs!=null) {
                        rs.last();
                        
                        numRowsInProgramReport = rs.getRow();
                        if(numRowsInProgramReport > 0) {
                            rs.first();
                            dataForCurrentMeterGroup = new String[numRowsInProgramReport][numColumns];
                            indexDataMeterGroup = new int[numRowsInProgramReport];
                            int i=0;
                            indexDataMeterGroup[i] = rs.getInt("meterID");
                            int j = 0;
                            dataForCurrentMeterGroup[i][j] = rs.getString("nameMeter");
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("meterGroupID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("processID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateIn"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = rs.getString("plantNum");
                            j++;
                            dataForCurrentMeterGroup[i][j] = rs.getString("plantMaker");
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrTypeID"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateLast"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrPeriod"));
                            j++;
                            dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateNext"));
                            j++;
                            //
                            i++;
                            while(rs.next()) {
                                indexDataMeterGroup[i] = rs.getInt("meterID");
                                j = 0;
                                dataForCurrentMeterGroup[i][j] = rs.getString("nameMeter");
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("meterGroupID"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("processID"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateIn"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = rs.getString("plantNum");
                                j++;
                                dataForCurrentMeterGroup[i][j] = rs.getString("plantMaker");
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrTypeID"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateLast"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = Integer.toString(rs.getInt("calibrPeriod"));
                                j++;
                                dataForCurrentMeterGroup[i][j] = dateformat.format(rs.getDate("dateNext"));
                                j++;
                                //
                                i++;
                            }
                        }
                    }
                    else {
                        numRowsInProgramReport = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Удаление данных из таблиц
     *********************************************************/
    public boolean removeFromTable(String removeString) {
        boolean result = false;
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    stmt.execute(removeString);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     * Чтение параметров из таблиц
     *********************************************************/
    public void readParametersFromTable(String requestString, int numColumns, int readType) {
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    rs = stmt.executeQuery(requestString);
                    numRowsInTable = 0;
                    if(rs!=null) {
                        rs.last();
                        numRowsInTable = rs.getRow();
                        rs.first();
                        dataForCurrentTable = new String[numRowsInTable][numColumns];
                        indexDataTable = new int[numRowsInTable];
                        int i=0;
                        indexDataTable[i] = rs.getInt(1);
                        int j = 0;
                        for(j=0;j<numColumns;j++) {
                            if( (readType == 1) && (j==4) )
                                dataForCurrentTable[i][j] = Integer.toString(rs.getInt(j+2));
                            else 
                                if( (readType == 2) && (j==1) ) 
                                    dataForCurrentTable[i][j] = Integer.toString(rs.getInt(j+2));
                                else dataForCurrentTable[i][j] = rs.getString(j+2);
                        }
                        //
                        i++;
                        while(rs.next()) {
                            indexDataTable[i] = rs.getInt(1);
                            for(j=0;j<numColumns;j++) {
                                if( (readType == 1) && (j==4) )
                                    dataForCurrentTable[i][j] = Integer.toString(rs.getInt(j+2));
                                else 
                                if( (readType == 2) && (j==1) ) 
                                    dataForCurrentTable[i][j] = Integer.toString(rs.getInt(j+2));
                                else dataForCurrentTable[i][j] = rs.getString(j+2);
                            }
                            i++;
                        }
                    }
                    else {
                        numRowsInTable = 0;
                    }
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
    }
    /*********************************************************
     *  Изменение данных в таблицах
     *********************************************************/
    public boolean changeTable(String changeTable) {
        boolean result = false;
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    stmt.execute(changeTable);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  Изменение данных в таблицах
     *********************************************************/
    public boolean addTable(String addTable) {
        boolean result = false;
        //
        try {
            con = DriverManager.getConnection(url,user,password);
            if(con != null) {
                stmt = con.createStatement();
                if(stmt != null) {
                    //stmt.execute(setCharacterSet);
                    stmt.execute(addTable);
                    result = true;
                }
            }
        }
        catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        finally {
            try {
                if(con != null) 
                    con.close();
            } catch(SQLException sqlEx) {}
            try {
                if(stmt != null) 
                    stmt.close();
            } catch(SQLException sqlEx) {}
            try {
                if(rs != null) 
                    rs.close();
            } catch(SQLException sqlEx) {}
        }
        return result;
    }
    /*********************************************************
     *  
     *********************************************************/
}
