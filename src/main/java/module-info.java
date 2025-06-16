module dev.kkarrasmil80.gestoritv {

    //Dependencias

    //Java
    requires javafx.controls;
    requires javafx.fxml;

    //Kotlin
    requires kotlin.reflect;
    requires kotlin.stdlib;

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


    //App
    opens dev.kkarrasmil80.gestoritv to javafx.fxml;
    exports dev.kkarrasmil80.gestoritv;

    //DAO
    opens dev.kkarrasmil80.gestoritv.vehiculo.dao to org.jdbi.v3.core;
    exports dev.kkarrasmil80.gestoritv.vehiculo.dao to kotlin.reflect;

}