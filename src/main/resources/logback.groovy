import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.filter.ThresholdFilter
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.status.OnConsoleStatusListener
import org.slf4j.MDC

import static ch.qos.logback.classic.Level.*

MDC.put("board_format","simple")

displayStatusOnConsole()
scan('10 seconds')
setupAppenders()
setupLoggers()

def displayStatusOnConsole() {
    statusListener OnConsoleStatusListener
}

def setupAppenders() {

    def logfileDate = timestamp('yyyy-MM-dd')
    def plainFormat = "%d{HH:mm:ss.SSS} %-5level %logger{0} [%thread]: %msg%n"
    def colorFormat = "%d{HH:mm:ss.SSS} %highlight(%-5level) %logger{0} [%thread]: %msg%n"

    // use plainFormat if you have trouble with ANSI colors in logs
    def format = colorFormat

    appender('logfile', FileAppender) {
        file = "log/debug.${logfileDate}.log"
        append = false
        filter(ThresholdFilter) {
            level = INFO
        }
        encoder(PatternLayoutEncoder) {
            pattern = plainFormat
        }
    }

    appender('dangerLog', FileAppender) {
        file = "log/danger_zone.${logfileDate}.log"
        append = true
        filter(ThresholdFilter) {
            level = WARN
        }
        encoder(PatternLayoutEncoder) {
            pattern = plainFormat
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
    root ALL, ['logfile', 'systemOut', 'dangerLog']
}