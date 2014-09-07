import ch.qos.logback.classic.filter.ThresholdFilter

import static ch.qos.logback.classic.Level.*
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.status.OnConsoleStatusListener
import ch.qos.logback.core.FileAppender

displayStatusOnConsole()
scan('10 seconds')
setupAppenders()
setupLoggers()

def displayStatusOnConsole() {
    statusListener OnConsoleStatusListener
}

def setupAppenders() {

    def logfileDate = timestamp('yyyy-MM-dd')
    def format = "%d{HH:mm:ss.SSS} %-5level %logger{0}: %msg%n"

    appender('logfile', FileAppender) {
        file = "log/debug.${logfileDate}.log"
        append = false
        filter(ThresholdFilter) {
            level = DEBUG
        }
        encoder(PatternLayoutEncoder) {
            pattern = format
        }
    }

    appender('systemOut', ConsoleAppender) {
        filter(ThresholdFilter) {
            level = INFO
        }
        encoder(PatternLayoutEncoder) {
            pattern = format
        }
    }
}

def setupLoggers() {
    root ALL, ['logfile', 'systemOut']
}