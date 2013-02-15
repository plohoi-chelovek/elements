package ua.elements.model;

import java.io.*;
import java.sql.*;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.datasource.*;

public class DataManagement {
    private File programDir;
    private String dbName = "data";

    public JdbcTemplate template;
    
    private ProductManagement productManagement;
    private ServiceManagement serviceManagement;

    public DataManagement() {
	programDir = new File(System.getProperty("user.home"), "elements");
	if (!programDir.exists())
	    programDir.mkdir();
	File h2File = new File(programDir, dbName + ".h2.db");
	if (!h2File.exists())
	    template = generate(new File(programDir, dbName));
	else
	    template = createTemplate(new File(programDir, dbName));
    }

    private JdbcTemplate generate(File db) {
	if (template != null)
	    throw new RuntimeException("template is not null");
	System.out.println("generate");
	SingleConnectionDataSource datasource = 
	    new SingleConnectionDataSource("jdbc:h2:" + db.toString(),"junk", "junk", false);
	JdbcTemplate t = new JdbcTemplate(datasource);
	t.execute("CREATE TABLE product (name VARCHAR(50), price DOUBLE, " +
		  "count INT, time TIMESTAMP)");
	t.execute("CREATE TABLE charge (name VARCHAR(50), " +
		  "count INT, time TIMESTAMP)");
	t.execute("CREATE TABLE service (name VARCHAR(50), price DOUBLE, " +
		  "time TIMESTAMP)");
	return t;
    }
    
    private JdbcTemplate createTemplate(File db) {
	if (template != null)
	    throw new RuntimeException("template is not null");
	System.out.println("create");
	SingleConnectionDataSource datasource = 
	    new SingleConnectionDataSource("jdbc:h2:" + db.toString(),"junk", "junk", false);
	return new JdbcTemplate(datasource);
    }

    public ProductManagement getProductManagement() {
	if (productManagement == null)
	    productManagement = new ProductManagement(this);
	return productManagement;
    }

    public ServiceManagement getServiceManagement() {
	if (serviceManagement == null) 
	    serviceManagement = new ServiceManagement(this);
	return serviceManagement;
    }
    
    public boolean sync(File syncFile) {
	try {
	    template.getDataSource().getConnection().close();
	    File h2File = new File(programDir, dbName + ".h2.db");
	    h2File.delete();
	    h2File.createNewFile();
	    newDbFile(syncFile);
	} catch (Exception e) {
	    throw new RuntimeException(e.getMessage());
	}
	return true;
    }

    private void newDbFile(File syncFile) {
	InputStream is = null;
	OutputStream os = null;
	try {
	    is = new FileInputStream(syncFile);
	    os = new FileOutputStream(new File(programDir, dbName + ".h2.db"));
	    byte[] buffer = new byte[1024];
	    int length = 0;
	    while ((length = is.read(buffer)) > 0)
		os.write(buffer, 0, length);
	    is.close();
	    os.close();
	} catch (IOException e) {
	    throw new RuntimeException(e.getMessage());
	}
    }

    /* TESTING */
    public static void main(String[] args) {
	new DataManagement();
    }
}
