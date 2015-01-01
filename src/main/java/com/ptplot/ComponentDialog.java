/* A top-level dialog window containing an arbitrary component.

 Copyright (c) 1998-2010 The Regents of the University of California.
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

///////////////////////////////////////////////////////////////////
//// ComponentDialog

/**

 This class is a modal dialog box that contains an arbitrary component.
 It can be used, for example, to put an instance of Query in a
 top-level dialog box.  The general way to use this class is to create
 the component that you wish to have contained in the dialog.
 Then pass that component to the constructor of this class.  The dialog
 is modal, so the statement that creates the dialog will not return
 until the user dismisses the dialog.  The method buttonPressed()
 can then be called to find out whether the user clicked the OK button
 or the Cancel button (or any other button specified in the constructor).
 Then you can access the component to determine what values were set
 by the user.
 <p>
 If the component that is added implements the CloseListener interface,
 then that component is notified when this dialog closes.

 @see CloseListener
 @author Edward A. Lee
 @version $Id: ComponentDialog.java 57040 2010-01-27 20:52:32Z cxh $
 @since Ptolemy II 0.4
 @Pt.ProposedRating Yellow (eal)
 @Pt.AcceptedRating Yellow (janneck)
 */
@SuppressWarnings("PMD")
public class ComponentDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    /** Construct a dialog with the specified owner, title, and component.
     *  An "OK" and a "Cancel" button are added to the dialog.
     *  The dialog is placed relative to the owner.
     *  @param owner The object that, per the user, appears to be
     *   generating the dialog.
     *  @param title The title of the dialog.
     *  @param component The component to insert in the dialog.
     */
    public ComponentDialog(Frame owner, String title, Component component) {
        this(owner, title, component, null, null);
    }

    /** Construct a dialog with the specified owner, title, component,
     *  and buttons.  The first button is the "default" in that
     *  it is the one activated by "Enter" or "Return" keys.
     *  If the last argument is null, then an "OK"
     *  and a "Cancel" button will be created.
     *  The dialog is placed relative to the owner.
     *  @param owner The object that, per the user, appears to be
     *   generating the dialog.
     *  @param title The title of the dialog.
     *  @param component The component to insert in the dialog.
     *  @param buttons An array of labels for buttons at the bottom
     *   of the dialog.
     */
    public ComponentDialog(Frame owner, String title, Component component,
            String[] buttons) {
        this(owner, title, component, buttons, null);
    }

    /** Construct a dialog with the specified owner, title, component,
     *  buttons, and message.  The message is placed above the component.
     *  The first button is the "default" in that
     *  it is the one activated by "Enter" or "Return" keys.
     *  If the <i>buttons</i> argument is null, then an "OK"
     *  and a "Cancel" button will be created.
     *  The dialog is placed relative to the owner.
     *  @param owner The object that, per the user, appears to be
     *   generating the dialog.
     *  @param title The title of the dialog.
     *  @param component The component to insert in the dialog.
     *  @param buttons An array of labels for buttons at the bottom
     *   of the dialog.
     *  @param message A message to place above the component, or null
     *   if no message is needed.
     */
    public ComponentDialog(Frame owner, String title, Component component,
            String[] buttons, String message) {
        this(owner, title, component, buttons, message, false);
    }

    /** Construct a dialog with the specified owner, title, component,
     *  buttons, and message.  The message is placed above the component.
     *  The first button is the "default" in that
     *  it is the one activated by "Enter" or "Return" keys.
     *  If the <i>buttons</i> argument is null, then an "OK"
     *  and a "Cancel" button will be created.
     *  The dialog is placed relative to the owner.
     *  @param owner The object that, per the user, appears to be
     *   generating the dialog.
     *  @param title The title of the dialog.
     *  @param component The component to insert in the dialog.
     *  @param buttons An array of labels for buttons at the bottom
     *   of the dialog.
     *  @param message A message to place above the component, or null
     *   if no message is needed.
     *  @param resizable True to allow the dialog to be resized.
     */
    public ComponentDialog(Frame owner, String title, Component component,
            String[] buttons, String message, boolean resizable) {
        super(owner, title, true);

        // Create a panel that contains the optional message
        // and the specified component, separated by a spacing rigid area.
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        if (message != null) {
            _messageArea = new JTextArea(message);
            _messageArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
            _messageArea.setEditable(false);
            _messageArea.setLineWrap(true);
            _messageArea.setWrapStyleWord(true);
            _messageArea.setBackground(getContentPane().getBackground());

            // Left Justify.
            _messageArea.setAlignmentX(0.0f);
            panel.add(_messageArea);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        panel.add(component);
        contents = component;

        if (buttons != null) {
            _buttons = buttons;
        } else {
            _buttons = _defaultButtons;
        }

        _optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION, null, _buttons, _buttons[0]);

        // The following code is based on Sun's CustomDialog example...
        _optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();

                // PropertyChange is an extremely non-selective listener,
                // so we have to filter...
                if (isVisible()
                        && (e.getSource() == _optionPane)
                        && (prop.equals(JOptionPane.VALUE_PROPERTY) || prop
                                .equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
                    Object value = _optionPane.getValue();

                    // Ignore reset.
                    if (value == JOptionPane.UNINITIALIZED_VALUE) {
                        return;
                    }

                    // Reset the JOptionPane's value.
                    // If you don't do this, then if the user
                    // presses the same button next time, no
                    // property change event will be fired.
                    // Note that this seems to trigger the listener
                    // again, so the previous line is essential.
                    _optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);

                    if (value instanceof String) {
                        // A button was pressed...
                        _buttonPressed = (String) value;
                    }

                    // Close the window.
                    setVisible(false);

                    // Take any action that might be associated with
                    // window closing.
                    _handleClosing();

                    // Java's AWT yields random results if we do this.
                    // And anyway, it doesn't work.  Components still don't
                    // have their ComponentListener methods called to indicate
                    // that they have become invisible.
                    // dispose();
                }
            }
        });

        getContentPane().add(_optionPane);
        pack();
        setResizable(resizable);

        if (owner != null) {
            setLocationRelativeTo(owner);
        } else {
            // Center on screen.  According to the Java docs,
            // passing null to setLocationRelationTo() _may_ result
            // in centering on the screen, but it is not required to.
            Toolkit tk = Toolkit.getDefaultToolkit();
            setLocation((tk.getScreenSize().width - getSize().width) / 2, (tk
                    .getScreenSize().height - getSize().height) / 2);
        }

        // NOTE: Java's AWT may yield random results if we do the following.
        // And anyway, it doesn't work.  Components still don't
        // have their ComponentListener methods called to indicate
        // that they have become invisible.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Catch closing events so that components are notified if
        // the window manager is used to close the window.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                _handleClosing();
            }
        });

        // Make the window visible.
        setVisible(true);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Return the label of the button that triggered closing the
     *  dialog, or an empty string if none.
     *  @return The label of the button pressed.
     */
    public String buttonPressed() {
        return _buttonPressed;
    }

    /** Change the message that was specified in the constructor to
     *  read as specified.  If no message was specified in the constructor,
     *  then do nothing.
     *  @param message The new message.
     */
    public void setMessage(String message) {
        if (_messageArea != null) {
            _messageArea.setText(message);
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /** If the contents of this dialog implements the CloseListener
     *  interface, then notify it that the window has closed, unless
     *  notification has already been done (it is guaranteed to be done
     *  only once).
     */
    protected void _handleClosing() {
        if ((contents instanceof CloseListener) && !_doneHandleClosing) {
            _doneHandleClosing = true;
            ((CloseListener) contents).windowClosed(this, _buttonPressed);
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public variables                  ////

    /** The component contained by this dialog.
     */
    public Component contents;

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** The label of the button pushed to dismiss the dialog. */
    protected String _buttonPressed = "";

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    /** Button labels. */
    private static String[] _buttons;

    /** Default button labels. */
    private static String[] _defaultButtons = { "OK", "Cancel" };

    /** Indicator that we have notified of window closing. */
    private boolean _doneHandleClosing = false;

    /** The pane with the buttons. */
    private JOptionPane _optionPane;

    /** The container for messages. */
    private JTextArea _messageArea;
}
