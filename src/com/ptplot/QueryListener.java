/* One line description of file.

 Copyright (c) 1999-2010 The Regents of the University of California.
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

import java.util.EventListener;

///////////////////////////////////////////////////////////////////
//// QueryListener

/**
 Listener interface for changes in the query box.

 @author Edward A. Lee
 @version $Id: QueryListener.java 57040 2010-01-27 20:52:32Z cxh $
 @since Ptolemy II 0.3
 @Pt.ProposedRating Red (eal)
 @Pt.AcceptedRating Red (eal)
 */
public interface QueryListener extends EventListener {
    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Called to notify that one of the entries has changed.
     *  The name of the entry is passed as an argument.
     *  @param name The name of the entry.
     */
    public abstract void changed(String name);
}
