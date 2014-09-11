/* Utilities for managing the background color.

 Copyright (c) 2009-2010 The Regents of the University of California.
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
import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;

///////////////////////////////////////////////////////////////////
//// JFileChooserBugFix

/**
 A workaround for a JFileChooser bug.
 <p>This class is necessary to work around a bug under Windows where
 the "common places" portion of the JFileChooser dialog is affected
 by the background color of a component.  Sun has acknowledged the
 bug as <a href="http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6817933
">#6817933</a>.  See also "<a href="http://bugzilla.ecoinformatics.org/show_bug.cgi?id=3801">open dialog, common places pane has white box instead of text</a>."

 <p> Every time JFileChooser is instantiated, saveBackground() should
 be called so that the background is properly set.  Then, in a finally
 clause, restoreBackground() should be called.  For example:
 <pre>

  // Swap backgrounds and avoid white boxes in "common places" dialog
  JFileChooserBugFix jFileChooserBugFix = new JFileChooserBugFix();
  Color background = null;
  try {
      background = jFileChooserBugFix.saveBackground();
      JFileChooser fileDialog = new JFileChooser();
      // Do the usual JFileChooser song and dance . . .
  } finally {
      jFileChooserBugFix.restoreBackground(background);
  }
 </pre>

 @author Christopher Brooks
 @version $Id: JFileChooserBugFix.java 59167 2010-09-21 17:08:02Z cxh $
 @since Ptolemy II 8.0
 @Pt.ProposedRating Red (cxh)
 @Pt.AcceptedRating Red (cxh)
 */
public class JFileChooserBugFix {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    /** Instantiate a JFileChooserBugFix object. */
    public JFileChooserBugFix() {
        _HTMLEditorKit = new HTMLEditorKit();
    }

    /** Restore the background.
     *  @param background The background to be restored.
     *  @see #saveBackground()
     */
    public void restoreBackground(Color background) {
        try {
            if (background != null) {
                // Restore the background color.
                String rgb = Integer.toHexString(background.getRGB());
                String rule = "body {background: #"
                        + rgb.substring(2, rgb.length()) + ";}";
                StyleSheet styleSheet = _HTMLEditorKit.getStyleSheet();
                styleSheet.addRule(rule);
                _HTMLEditorKit.setStyleSheet(styleSheet);
            }
        } catch (Exception ex) {
            log.error("Problem restoring background color. {}", ex);
        }
    }

    /** Set the background to the value of the ToolBar.shadow property
     *  and return the previous background.
     *  <p>Avoid a problem under Windows where the common places pane
     *  on the left of the file browser dialog has white boxes
     *  because the background is set to white.
     *  http://bugzilla.ecoinformatics.org/show_bug.cgi?id=3801
     *  <p>Call this method before instantiating a JFileChooser.
     *  @return the value of the previous background.
     */
    public Color saveBackground() {
        if (_HTMLEditorKit == null) {
            _HTMLEditorKit = new HTMLEditorKit();
        }
        StyleSheet styleSheet = _HTMLEditorKit.getStyleSheet();
        Color background = null;

        try {
            // Get the background color of the HTML widget.
            AttributeSet bodyAttribute = (AttributeSet) styleSheet.getStyle(
                    "body").getAttribute(
                    javax.swing.text.StyleConstants.ResolveAttribute);
            background = styleSheet.getBackground(bodyAttribute);
        } catch (Exception ex) {
            log.warn("ignoring..", ex);
            // Ignore, we just won't set the background.
        }

        try {
            // Get the color of the ToolBar shadow and use it.
            Color shadow = UIManager.getColor("ToolBar.shadow");
            String rgb = Integer.toHexString(shadow.getRGB());
            String rule = "body {background: #"
                    + rgb.substring(2, rgb.length()) + ";}";
            styleSheet.addRule(rule);
            _HTMLEditorKit.setStyleSheet(styleSheet);
        } catch (Exception ex) {
            log.error("Problem setting background color: {}", ex);
        }
        return background;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    /** The HTMLEditorKit*/
    private HTMLEditorKit _HTMLEditorKit;
}
