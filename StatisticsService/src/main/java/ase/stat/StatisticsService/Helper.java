package ase.stat.StatisticsService;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Tommi on 19.06.2017.
 */
public class Helper {

    public static String setParameter(String script, String key, String value){
        key = "<{" + key + "}>";
        return script.replace(key, value);
    }


    public static String readFile(String path, Charset encoding)
            throws IOException
    {
        Resource resource = new ClassPathResource(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(),encoding));
        String s = "";
        String tmp = null;
        while ((tmp = reader.readLine()) != null){
            s+=tmp;
        }
        return s.replace("\r"," ").replace("\n"," ");
    }

}
