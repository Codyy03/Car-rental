module com.mycompany.project_java_3try {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
  
    opens com.mycompany.project_java_3try to javafx.fxml, org.hibernate.orm.core;
    exports com.mycompany.project_java_3try;
}
