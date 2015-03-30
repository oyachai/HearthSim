/* Base class for displaying exceptions, warnings, and messages.

 Copyright (c) 2003-2010 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS.

 PT_COPYRIGHT_VERSION_2
 COPYRIGHTENDKEY
 */
package com.ptplot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

///////////////////////////////////////////////////////////////////
//// MessageHandler

/**
 This is a class that is used to report errors.  It provides a
 set of static methods that are called to report errors.  However, the
 actual reporting of the errors is deferred to an instance of this class
 that is set using the setMessageHandler() method.  Normally there
 is only one instance, set up by the application, so the class is
 a singleton.  But this is not enforced.
 <p>
 This base class simply writes the errors to log.error.
 When an applet or application starts up, it may wish to set a subclass
 of this class as the message handler, to allow a nicer way of
 reporting errors.  For example, a Swing application will probably
 want to report errors in a dialog box, using for example
 the derived class GraphicalMessageHandler.
 @see ptolemy.gui.GraphicalMessageHandler

 @author  Edward A. Lee, Steve Neuendorffer, Elaine Cheong
 @version $Id: MessageHandler.java 57040 2010-01-27 20:52:32Z cxh $
 @since Ptolemy II 4.0
 @Pt.ProposedRating Green (cxh)
 @Pt.AcceptedRating Green (cxh)
 */
public class MessageHandler {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Defer to the set message handler to show the specified
     *  error message.
     *  @param info The message.
     */
    public static void error(String info) {
        _handler._error(info);
    }

    /** Defer to the set message handler to
     *  show the specified message and throwable information.
     *  If the throwable is an instance of CancelException, then it
     *  is not shown.
     *
     *  @param info The message.
     *  @param throwable The throwable.
     *  @see CancelException
     */
    public static void error(String info, Throwable throwable) {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MessageHandler.class);
        // Sometimes you find that errors are reported multiple times.
        // To find out who is calling this method, uncomment the following.
        // log.info("------ reporting error:" + throwable);
        // throwable.printStackTrace();
        // log.info("------ called from:");
        // (new Exception()).printStackTrace();
        try {
            _handler._error(info, throwable);
        } catch (Throwable throwable2) {
            // An applet was throwing an exception while handling
            // the error - so we print the original message if _error() fails.
            log.error("Internal Error, exception thrown while "
                    + "handling error: \"" + info + "\"\n");
            log.error("{}", throwable);
            log.error("Internal Error: {}",throwable2);
        }
    }

    /** Return the message handler instance that is used by the static
     *  methods in this class.
     *  @return The message handler.
     *  @see #setMessageHandler(MessageHandler)
     */
    public static MessageHandler getMessageHandler() {
        return _handler;
    }

    /** Defer to the set message handler to show the specified
     *  message.
     *  @param info The message.
     */
    public static void message(String info) {
        _handler._message(info);
    }

    /** Set the message handler instance that is used by the static
     *  methods in this class.  If the given handler is null, then
     *  do nothing.
     *  @param handler The message handler.
     *  @see #getMessageHandler()
     */
    public static void setMessageHandler(MessageHandler handler) {
        if (handler != null) {
            _handler = handler;
        }
    }

    /** Return a short description of the throwable.
     *  @param throwable The throwable
     *  @return If the throwable is an Exception, return "Exception",
     *  if it is an Error, return "Error", if it is a Throwable, return
     *  "Throwable".
     */
    public static String shortDescription(Throwable throwable) {
        String throwableType;

        if (throwable instanceof Exception) {
            throwableType = "Exception";
        } else if (throwable instanceof Error) {
            throwableType = "Error";
        } else {
            throwableType = "Throwable";
        }

        return throwableType;
    }

    /** Display the warning message.  In this base class, the
     *  the default handler merely prints the warning to stderr.
     *  Derived classes such as ptolemy.gui.GraphicalMessageHandler
     *  might display the message graphically.
     *
     *  @param info The message.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    public static void warning(String info) throws CancelException {
        _handler._warning(info);
    }

    /** Display the warning message and throwable information.  In
     *  this base class, the the default handler merely prints the
     *  warning to stderr.  Derived classes such as
     *  ptolemy.gui.GraphicalMessageHandler might display the message
     *  graphically.
     *
     *  @param info The message.
     *  @param throwable The throwable associated with this warning.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    public static void warning(String info, Throwable throwable)
            throws CancelException {
        _handler._warning(info + ": " + throwable.getMessage(), throwable);
    }

    /** Ask the user a yes/no question, and return true if the answer
     *  is yes.
     *
     *  @param question The yes/no question.
     *  @return True if the answer is yes.
     */
    public static boolean yesNoQuestion(String question) {
        return _handler._yesNoQuestion(question);
    }

    /** Ask the user a yes/no/cancel question, and return true if the
     *  answer is yes.  If the user clicks on the "Cancel" button,
     *  then throw an exception.
     *
     *  @param question The yes/no/cancel question.
     *  @return True if the answer is yes.
     *  @exception ptolemy.util.CancelException If the user clicks on
     *  the "Cancel" button.
     */
    public static boolean yesNoCancelQuestion(String question)
            throws CancelException {
        return _handler._yesNoCancelQuestion(question);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /** Show the specified error message.
     *  @param info The message.
     */
    protected void _error(String info) {
        log.error(info);
    }

    /** Show the specified message and throwable information.
     *  If the throwable is an instance of CancelException, then nothing
     *  is shown.
     *
     *  @param info The message.
     *  @param throwable The throwable.
     *  @see CancelException
     */
    protected void _error(String info, Throwable throwable) {
        if (throwable instanceof CancelException) {
            return;
        }

        log.error(info);
        throwable.printStackTrace();
    }

    /** Display the warning message.  In this base class, the
     *  the default handler merely prints the warning to stderr.
     *  @param info The message.
     */
    protected void _message(String info) {
        log.error(info);
    }

    /** Show the specified message.  In this base class, the message
     *  is printed to standard error.
     *  <p>Derived classes might show the specified message in a modal
     *  dialog.  If the user clicks on the "Cancel" button, then throw
     *  an exception.  This gives the user the option of not
     *  continuing the execution, something that is particularly
     *  useful if continuing execution will result in repeated
     *  warnings.
     *  @param info The message.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    protected void _warning(String info) throws CancelException {
        _error(info);
    }

    /** Display the warning message and throwable information.  In
     *  this base class, the the default handler merely prints the
     *  warning to stderr.
     *  @param info The message.
     *  @param throwable The Throwable.
     *  @exception CancelException If the user clicks on the "Cancel" button.
     */
    protected void _warning(String info, Throwable throwable)
            throws CancelException {
        _error(info, throwable);
    }

    /** Ask the user a yes/no question, and return true if the answer
     *  is yes.  In this base class, this prints the question on standard
     *  output and looks for the reply on standard input.
     *  @param question The yes/no question to be asked.
     *  @return True if the answer is yes.
     */
    protected boolean _yesNoQuestion(String question) {
        log.info(question);
        log.info(" (yes or no) ");

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(
                System.in));

        try {
            String reply = stdIn.readLine();

            if (reply == null) {
                return false;
            } else if (reply.trim().toLowerCase().equals("yes")) {
                return true;
            }
        } catch (IOException ex) {
            log.warn("ignoring..", ex);
        }

        return false;
    }

    /** Ask the user a yes/no/cancel question, and return true if the
     *  answer is yes.  If the user chooses "cancel", then throw an
     *  exception.  In this base class, this prints the question on
     *  the standard output and looks for the reply on the standard
     *  input.
     *  @param question The yes/no/cancel question to be asked.
     *  @return True if the answer is yes.
     *  @exception ptolemy.util.CancelException If the user chooses
     *  "cancel".
     */
    protected boolean _yesNoCancelQuestion(String question)
            throws CancelException {
        log.info(question);
        log.info(" (yes or no or cancel) ");

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(
                System.in));

        try {
            String reply = stdIn.readLine();

            if (reply == null) {
                return false;
            } else {
                if (reply.trim().toLowerCase().equals("yes")) {
                    return true;
                } else if (reply.trim().toLowerCase().equals("cancel")) {
                    throw new CancelException("Cancelled: "
                            + question);
                }
            }
        } catch (IOException ex) {
            log.warn("ignoring..", ex);
        }

        return false;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    // The message handler.
    private static MessageHandler _handler = new MessageHandler();
}
