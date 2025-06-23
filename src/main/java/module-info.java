module dev.kkarrasmil80.gestoritv {

    //Dependencias

    //Java
    requires javafx.controls;
    requires javafx.fxml;

    //Kotlin
    requires kotlin.reflect;

    //Logger
    requires logging.jvm;
    requires org.slf4j;

    //Jdbi
    requires org.jdbi.v3.core;
    requires org.jdbi.v3.sqlobject;
    requires org.jdbi.v3.kotlin;
    requires org.jdbi.v3.sqlobject.kotlin;

    //Koin
    requires koin.core.jvm;

    //Cache
    requires com.github.benmanes.caffeine;
    requires kotlin.result.jvm;
    requires java.sql;
    requires jbcrypt;

    requires transitive io.leangen.geantyref;
    requires kotlinx.serialization.json;
    requires java.desktop;
    requires openhtmltopdf.pdfbox;
    requires open;


    //App
    opens dev.kkarrasmil80.gestoritv to javafx.fxml;
    exports dev.kkarrasmil80.gestoritv;

    //DAO
    opens dev.kkarrasmil80.gestoritv.vehiculo.dao to org.jdbi.v3.core;
    exports dev.kkarrasmil80.gestoritv.vehiculo.dao to kotlin.reflect;

    opens dev.kkarrasmil80.gestoritv.cita.dao to org.jdbi.v3.core;
    exports dev.kkarrasmil80.gestoritv.cita.dao to kotlin.reflect;

    opens dev.kkarrasmil80.gestoritv.cliente.dao to org.jdbi.v3.core;
    exports dev.kkarrasmil80.gestoritv.cliente.dao to kotlin.reflect;

    //SPLASH SCREEN
    opens dev.kkarrasmil80.gestoritv.controllers to javafx.fxml;
    exports dev.kkarrasmil80.gestoritv.controllers to javafx.graphics;

    //MODELS
    opens dev.kkarrasmil80.gestoritv.vehiculo.models to javafx.base;




}