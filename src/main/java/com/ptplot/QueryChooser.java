/*

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
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Abstract superclass for customized entry boxes. Used to make protected
 * methods of the <code>ptolemy.gui.Query</code> class available to derived
 * classes outside the <code>ptolemy.gui</code> package.
 *
 * @author  fracpete (fracpete at waikato dot ac dot nz)
@version $Id: QueryChooser.java 57044 2010-01-27 22:41:05Z cxh $
@since Ptolemy II 8.0
 * @version $Id: QueryChooser.java 57044 2010-01-27 22:41:05Z cxh $
 */
public abstract class QueryChooser extends Box implements SettableQueryChooser,
        ActionListener {

    private static final long serialVersionUID = 1L;

    /**
     * Initializes the chooser.
     *
     * @param owner       the owning query
     * @param name        the name of the component
     * @param background  the background color
     * @param foreground  the foreground color
     */
    public QueryChooser(Query owner, String name, Color background,
            Color foreground) {

        super(BoxLayout.X_AXIS);

        _owner = owner;
        _name = name;
        _background = background;
        _foreground = foreground;
    }

    /**
     * Returns the owning query object.
     *
     * @return            the query object
     */
    public Query getOwner() {
        return _owner;
    }

    /**
     * Returns the name of the component.
     *
     * @return            the name of the component
     */
    @Override
    public String getName() {
        return _name;
    }

    /**
     * Returns the color for the background.
     *
     * @return            the background color
     */
    public Color getBackgroundColor() {
        return _background;
    }

    /**
     * Returns the color for the foreground.
     *
     * @return            the foreground color
     */
    public Color getForegroundColor() {
        return _foreground;
    }

    /**
     * Add a label and a widget to the panel.
     *
     * @param name        The name of the entry.
     * @param label       The label.
     * @param widget      The interactive entry to the right of the label.
     * @param entry       The object that contains user data.
     */
    protected void _addPair(String name, JLabel label, Component widget,
            Object entry) {
        getOwner()._addPair(name, label, widget, entry);
    }

    /**
     * Notify all registered listeners that something changed for the
     * specified entry, if it indeed has changed.  The getStringValue()
     * method is used to check the current value against the previously
     * notified value, or the original value if there have been no
     * notifications.
     *
     * @param name The entry that may have changed.
     */
    protected void _notifyListeners(String name) {
        getOwner()._notifyListeners(name);
    }

    /** The owning query. */
    private Query _owner;

    /** The name of the chooser. */
    private String _name;

    /** The foreground color. */
    private Color _foreground;

    /** The background color. */
    private Color _background;
}
