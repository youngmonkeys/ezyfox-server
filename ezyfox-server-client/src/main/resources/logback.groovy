//
// Built on Sun Apr 24 09:59:09 UTC 2016 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.DEBUG

appender("stdout", ConsoleAppender) {
  target = "System.out"
  encoder(PatternLayoutEncoder) {
    //pattern = "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level  %class{36}.%M %L  - %msg%n"
  }
}
root(DEBUG, ["stdout"])