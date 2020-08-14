package selindoalpha.com.selindovsatprod.Utils;

/**
 * Created by ekosulistyoa on 6/20/17.
 */

import android.os.Environment;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

/**
 * Created by Potato on 19 Mei 2017.
 */

public class ALogger {
    @SuppressWarnings("rawtypes")
    private static String logFolder="vsatapp_sa.log";
    public static org.apache.log4j.Logger getLogger(Class clazz)
    {
        final LogConfigurator logConfigurator = new LogConfigurator();
        if (!Environment.getExternalStorageState().equals("mounted")) {
            //Log.d("ALogger","SD CARD Not mounted");
            logConfigurator.setFileName(null);
        }
        else
        {
                logConfigurator.setFileName(Environment.getExternalStorageDirectory().toString() + File.separator + logFolder);
                logConfigurator.setRootLevel(Level.ERROR);//Change Before Up
                logConfigurator.setLevel("org.apache", Level.ERROR);//Change Before Up
                logConfigurator.setUseFileAppender(true);
                logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
                logConfigurator.setMaxFileSize(1024 * 1024 * 5);
                logConfigurator.setImmediateFlush(true);
                logConfigurator.configure();
        }
        Logger log = Logger.getLogger(clazz);
        return log;
    }

}

