/* UndoListener helper

 Copyright (c) 2008-2009 The Regents of the University of California.
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
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * An Undo/Redo listener for use with a JTextComponent.
 *
 * <p> A convenience constructor has been provided such that a caller
 * can immediately have default shortcut key mappings for undo/redo
 * actions on the text component
 *
 * @author Ben Leinfelder
 * @version $Id: UndoListener.java 57044 2010-01-27 22:41:05Z cxh $
 * @since Ptolemy II 8.0
 * @Pt.ProposedRating Yellow (cxh)
 * @Pt.AcceptedRating Red (cx)
 * @author ben leinfelder
 */
public class UndoListener implements UndoableEditListener {

    /**
     * Construct an undo listener.
     * <p>This constructor allows simple usage without setting up key mapping
     * automatically (it is then the responsibilty of the caller to
     * provide a mechanism for invoking the undo and redo actions.)
     */
    public UndoListener() {
    }

    /**
     * Construct an undo listener with default key mappings.
     * The default key mappings invoke the undo and redo actions on
     * <i>textArea</i> .
     * <p>A typical usage pattern would be:
     * <code>
     *         JTextArea textArea = new JTextArea("testing");
     *         textArea.getDocument().addUndoableEditListener(new UndoListener(textArea));
     * </code>
     * @param textArea the text component that is being listened to
     * and upon which undo/redo actions will be performed
     */
    public UndoListener(JTextComponent textArea) {

        // Set the mapping for shortcut keys;
        InputMap inputMap = textArea.getInputMap();
        ActionMap actionMap = textArea.getActionMap();

        // Ctrl-z or equivalent to undo.
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()), "undo");
        actionMap.put("undo", _undoAction);
        // Ctrl-y or equivalent to redo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()), "redo");
        actionMap.put("redo", _redoAction);
    }

    /** Remember the edit and update the action state.
     *  @param event The event that occurred.
     */
    @Override
    public void undoableEditHappened(UndoableEditEvent event) {
        _undo.addEdit(event.getEdit());
        _undoAction._updateUndoState();
        _redoAction._updateRedoState();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** The undo action. */
    protected UndoAction _undoAction = new UndoAction();

    /** The redo action. */
    protected RedoAction _redoAction = new RedoAction();

    /** The undo manager. */
    protected UndoManager _undo = new UndoManager();

    /**
     * Perform the undo action.
     */
    protected class UndoAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                _undo.undo();
            } catch (CannotUndoException ex) {
                throw new RuntimeException("Unable to undo.", ex);
            }
            _updateUndoState();
            _redoAction._updateRedoState();
        }

        /** Depending on the whether the undo manager can undo, enable
         *  and disable the undo state.
         */
        protected void _updateUndoState() {
            if (_undo.canUndo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }

    /**
     * Peform the redo action.
     */
    protected class RedoAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                _undo.redo();
            } catch (CannotRedoException ex) {
                throw new RuntimeException("Unable to redo.", ex);
            }
            _updateRedoState();
            _undoAction._updateUndoState();
        }

        /** Depending on the whether the undo manager can redo, enable
         *  and disable the undo state.
         */
        protected void _updateRedoState() {
            if (_undo.canRedo()) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        }
    }
}
