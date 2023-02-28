package net.wf0b.code;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ConnectionCredential Unit Test")
class ConnectionCredentialTest {

    public static Gson gson = new GsonBuilder().create();

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Tag("detail")
    @Tag("ConnectionCredential")
    @Test
    public @interface DetailTest {
    }

    @DetailTest
    @DisplayName("ConnectionCredential -V")
    void mainVersion() {
        ConnectionCredential.main(new String[]{"-V"});
    }

    @DetailTest
    @DisplayName("ConnectionCredential -h")
    void mainHelp() {
        ConnectionCredential.main(new String[]{"-h"});
    }

    @DetailTest
    @DisplayName("ConnectionCredential -i instanceName -d minnieDriver -s connectmeup -p password")
    void mainAddNew() throws IOException {
        Path path = Paths.get(OperatingSystem.getApplicationHome("ConnectionCredential").toString(), ".credentials");
        File file = path.toFile();
        if (file.exists()) file.delete();

        ConnectionCredential.main(new String[]{"-i", "instanceName", "-d", "minnieDriver", "-s", "connectmeup", "-p", "password"});
        String credentials = gson.toJson(OperatingSystem.getCredentials());
        //System.out.println("Add: " + credentials);
        assertEquals("{\"1036255499\":{\"instanceName\":\"instanceName\",\"connectionString\":\"connectmeup\",\"driverName\":\"minnieDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true}}", credentials);

        ConnectionCredential.main(new String[]{"-i", "instanceName", "-d", "minnieDriver", "-s", "connectmeup", "-p", "password"});
        credentials = gson.toJson(OperatingSystem.getCredentials());
        //System.out.println("Add Duplicate: " + credentials);
        assertEquals("{\"1036255499\":{\"instanceName\":\"instanceName\",\"connectionString\":\"connectmeup\",\"driverName\":\"minnieDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true}}", credentials);

        ConnectionCredential.main(new String[]{"-i", "instanceName2", "-d", "babyDriver", "-s", "connectmeup", "-p", "password"});
        credentials = gson.toJson(OperatingSystem.getCredentials());
        //System.out.println("Add2: " + credentials);
        assertEquals("{\"1036255499\":{\"instanceName\":\"instanceName\",\"connectionString\":\"connectmeup\",\"driverName\":\"minnieDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true},\"1817471549\":{\"instanceName\":\"instanceName2\",\"connectionString\":\"connectmeup\",\"driverName\":\"babyDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true}}", credentials);


        ConnectionCredential.main(new String[]{"-i", "instanceName", "-x"});
        credentials = gson.toJson(OperatingSystem.getCredentials());
        //System.out.println("Delete: " + credentials);
        assertEquals("{\"1036255499\":{\"instanceName\":\"instanceName\",\"connectionString\":\"connectmeup\",\"driverName\":\"minnieDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true},\"1817471549\":{\"instanceName\":\"instanceName2\",\"connectionString\":\"connectmeup\",\"driverName\":\"babyDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true}}", credentials);


        ConnectionCredential.main(new String[]{"-c", "-i", "instanceName2", "-d", "miniDriver", "-s", "connectmeup", "-p", "password"});
        credentials = gson.toJson(OperatingSystem.getCredentials());
        //System.out.println("Change: " + credentials);
        assertEquals("{\"1036255499\":{\"instanceName\":\"instanceName\",\"connectionString\":\"connectmeup\",\"driverName\":\"minnieDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true},\"1817471549\":{\"instanceName\":\"instanceName2\",\"connectionString\":\"connectmeup\",\"driverName\":\"miniDriver\",\"user\":\"wgfra\",\"password\":\"2wF0ESWAXqQzyuAVC7SmHA\\u003d\\u003d\",\"active\":true}}", credentials);

    }

}
