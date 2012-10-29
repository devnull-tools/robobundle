package atatec.robocode.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** @author Marcelo Varella Barca Guimarães */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface When {

  String value();

}
