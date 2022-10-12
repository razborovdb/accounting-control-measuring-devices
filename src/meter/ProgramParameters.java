
package meter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Andrey
 */
public class ProgramParameters {
    // IP адрес сервера базы данных
    String serverIP;
    // Порт сервера базы данных
    String serverPort;
    // Название базы данных
    String databaseName;
    // Имя пользователя для подключения к базе данных
    String databaseUser;
    // Пароль пользователя для подключения к базе данных
    String databasePassword;
    // Имя файла, в котором хранится информация по параметрам
    // подключения к базе данных
    String fileName = "meter.ini";
    // Количество параметров, хранящихся в файле
    int numParametersForRead =5;
    /*********************************************************
     *  ProgramParameters
     *********************************************************/
    ProgramParameters() {
        readProgramParameters();
    }
    /*********************************************************
     *  readProgramParameters
     *********************************************************/
    public void readProgramParameters() {
        boolean result = false;
        BufferedReader in;
        String readString;
        int i;
        //
        try ( FileReader fr = new FileReader(fileName) ) {
            in = new BufferedReader(fr);
            i = 0;
            if( (readString = in.readLine()) != null) {
                serverIP = readString;
                i++;
            }
            if( (readString = in.readLine()) != null) {
                serverPort = readString;
                i++;
            }
            if( (readString = in.readLine()) != null) {
                databaseName = readString;
                i++;
            }
            if( (readString = in.readLine()) != null) {
                databaseUser = readString;
                i++;
            }
            if( (readString = in.readLine()) != null) {
                databasePassword = readString;
                i++;
            }
            if( (readString = in.readLine()) != null) {
                i++;
            }
            in.close();
            if(i == numParametersForRead)
                result = true;
        }
        catch ( IOException ioe ) {
            //System.out.println("Error");
        }
        if(!result) {
            setDefaultProgramParameters();
            writeProgramParameters();
        }
    }
    /*********************************************************
     *  writeProgramParameters
     *********************************************************/
    public void writeProgramParameters() {
        try ( FileWriter fw = new FileWriter(fileName) ) {
            fw.write(serverIP);
            fw.write("\n");
            fw.write(serverPort);
            fw.write("\n");
            fw.write(databaseName);
            fw.write("\n");
            fw.write(databaseUser);
            fw.write("\n");
            fw.write(databasePassword);
            fw.write("\n");
        }
        catch ( IOException ioe ) {
            //System.out.println("Error");
        }
    }
    /*********************************************************
     *  setDefaultProgramParameters
     *********************************************************/
    public void setDefaultProgramParameters() {
        serverIP = "//localhost";
        serverPort = "3306";
        databaseName = "meter";
        databaseUser = "root";
        databasePassword = "irina";
    }
    /*********************************************************
     * 
     *********************************************************/
}
