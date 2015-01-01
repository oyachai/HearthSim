/* A signal plotter.

 @Copyright (c) 1997-2010 The Regents of the University of California.
 All rights reserved.

 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the
 above copyright notice and the following two paragraphs appear in all
 copies of this software.

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

// TO DO:
//   - steps between points rather than connected lines.
//   - cubic spline interpolation
//
// NOTE: The XOR drawing mode is needed in order to be able to erase
// plotted points and restore the grid line, tick marks, and boundary
// rectangle.  This introduces a number of artifacts, particularly
// where lines cross.  A better alternative in the long run would be
// use Java 2-D, which treats each notation on the screen as an object,
// and supports redrawing only damaged regions of the screen.
// NOTE: There are quite a few subjective spacing parameters, all
// given, unfortunately, in pixels.  This means that as resolutions
// get better, this program may need to be adjusted.

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


///////////////////////////////////////////////////////////////////
//// Plot

/**
 A flexible signal plotter.  The plot can be configured and data can
 be provided either through a file with commands or through direct
 invocation of the public methods of the class.
 <p>
 When calling the public methods, in most cases the changes will not
 be visible until paintComponent() is called.  To request that this
 be done, call repaint().  One exception is addPoint(), which
 makes the new point visible immediately if the plot is visible on
 the screen and addPoint() is called from the event dispatching thread.
 <p>
 This base class supports a simple file syntax that has largely been
 replaced by the XML-based PlotML syntax.  To read a file or a
 URL in this older syntax, use the read() method.
 This older syntax contains any number commands,
 one per line.  Unrecognized commands and commands with syntax
 errors are ignored.  Comments are denoted by a line starting with a
 pound sign "#".  The recognized commands include those supported by
 the base class, plus a few more.  The commands are case
 insensitive, but are usually capitalized.  The number of data sets
 to be plotted does not need to be specified.  Data sets are added as needed.
 Each dataset can be optionally identified with
 color (see the base class) or with unique marks.  The style of
 marks used to denote a data point is defined by one of the following
 commands:
 <pre>
 Marks: none
 Marks: points
 Marks: bigdots
 Marks: dots
 Marks: various
 Marks: pixels
 </pre>
 Here, "points" are small dots, while "dots" are larger.  If "various"
 is specified, then unique marks are used for the first ten data sets,
 and then recycled. If "pixels" are specified, then each point is
 drawn as one pixel.
 Using no marks is useful when lines connect the points in a plot,
 which is done by default.  However, if persistence is set, then you
 may want to choose "pixels" because the lines may overlap, resulting
 in annoying gaps in the drawn line.
 If the above directive appears before any DataSet directive, then it
 specifies the default for all data sets.  If it appears after a DataSet
 directive, then it applies only to that data set.
 <p>
 To disable connecting lines, use:
 <pre>
 Lines: off
 </pre>
 To reenable them, use
 <pre>
 Lines: on
 </pre>
 You can control the line style on a per dataset basis by adding
 <pre>
 LineStyle: solid
 </pre>
 Other supported line styles are "dotted", "dashed", "dotdashed" and
 "dotdotdashed".
 You can also specify "impulses", which are lines drawn from a plotted point
 down to the x axis.  Plots with impulses are often called "stem plots."
 These are off by default, but can be turned on with the
 command:
 <pre>
 Impulses: on
 </pre>
 or back off with the command
 <pre>
 Impulses: off
 </pre>
 If that command appears before any DataSet directive, then the command
 applies to all data sets.  Otherwise, it applies only to the current data
 set.
 To create a bar graph, turn off lines and use any of the following commands:
 <pre>
 Bars: on
 Bars: <i>width</i>
 Bars: <i>width, offset</i>
 </pre>
 The <i>width</i> is a real number specifying the width of the bars
 in the units of the x axis.  The <i>offset</i> is a real number
 specifying how much the bar of the <i>i</i><sup>th</sup> data set
 is offset from the previous one.  This allows bars to "peek out"
 from behind the ones in front.  Note that the frontmost data set
 will be the first one.  To turn off bars, use
 <pre>
 Bars: off
 </pre>
 To specify data to be plotted, start a data set with the following command:
 <pre>
 DataSet: <i>string</i>
 </pre>
 Here, <i>string</i> is a label that will appear in the legend.
 It is not necessary to enclose the string in quotation marks.
 To start a new dataset without giving it a name, use:
 <pre>
 DataSet:
 </pre>
 In this case, no item will appear in the legend.
 New datasets are plotted <i>behind</i> the previous ones.
 If the following directive occurs:
 <pre>
 ReuseDataSets: on
 </pre>
 Then datasets with the same name will be merged.  This makes it
 easier to combine multiple datafiles that contain the same datasets
 into one file.  By default, this capability is turned off, so
 datasets with the same name are not merged.
 The data itself is given by a sequence of commands with one of the
 following forms:
 <pre>
 <i>x</i>, <i>y</i>
 draw: <i>x</i>, <i>y</i>
 move: <i>x</i>, <i>y</i>
 <i>x</i>, <i>y</i>, <i>yLowErrorBar</i>, <i>yHighErrorBar</i>
 draw: <i>x</i>, <i>y</i>, <i>yLowErrorBar</i>, <i>yHighErrorBar</i>
 move: <i>x</i>, <i>y</i>, <i>yLowErrorBar</i>, <i>yHighErrorBar</i>
 </pre>
 The "draw" command is optional, so the first two forms are equivalent.
 The "move" command causes a break in connected points, if lines are
 being drawn between points. The numbers <i>x</i> and <i>y</i> are
 arbitrary numbers as supported by the Double parser in Java.
 If there are four numbers, then the last two numbers are assumed to
 be the lower and upper values for error bars.
 The numbers can be separated by commas, spaces or tabs.
 <p>Some of the methods, such as those that add points a plot, are
 executed in the event thread, possibly some time after they are called.
 If they are called from a thread different from the event thread,
 then the order in which changes to the plot take effect may be
 surprising.  We recommend that any code you write that changes
 the plot in visible ways be executed in the event thread. You
 can accomplish this using the following template:
 <pre>
 Runnable doAction = new Runnable() {
 public void run() {
 ... make changes here (e.g. setMarksStyle()) ...
 }
 };
 plot.deferIfNecessary(doAction);
 </pre>
 <p>
 This plotter has some <A NAME="ptplot limitations">limitations</a>:
 <ul>
 <li> If you zoom in far enough, the plot becomes unreliable.
 In particular, if the total extent of the plot is more than
 2<sup>32</sup> times extent of the visible area, quantization
 errors can result in displaying points or lines.
 Note that 2<sup>32</sup> is over 4 billion.
 <li> The limitations of the log axis facility are listed in
 the <code>_gridInit()</code> method in the PlotBox class.
 </ul>

 @author Edward A. Lee, Christopher Brooks, Contributor: Tom Peachey, Bert Rodiers
 @version $Id: Plot.java 59167 2010-09-21 17:08:02Z cxh $
 @since Ptolemy II 0.2
 @Pt.ProposedRating Yellow (cxh)
 @Pt.AcceptedRating Yellow (cxh)
 */
@SuppressWarnings("PMD")
public class Plot extends PlotBox {
    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Add a legend (displayed at the upper right) for the specified
     *  data set with the specified string.  Short strings generally
     *  fit better than long strings.
     *  @param dataset The dataset index.
     *  @param legend The label for the dataset.
     */
    @Override
    public synchronized void addLegend(int dataset, String legend) {
        _checkDatasetIndex(dataset);

        if (!_reuseDatasets) {
            super.addLegend(dataset, legend);
        } else {
            // If _reuseDataSets is true, then look to see if we
            // already have a dataset with the same legend.
            String possibleLegend = getLegend(dataset);

            if ((possibleLegend == null)
                    || ((possibleLegend != null) && !possibleLegend
                            .equals(legend))) {
                super.addLegend(dataset, legend);
            }
        }
    }

    /** In the specified data set, add the specified x, y point to the
     *  plot.  Data set indices begin with zero.  If the data set
     *  does not exist, create it.  The fourth argument indicates
     *  whether the point should be connected by a line to the previous
     *  point.  Regardless of the value of this argument, a line will not
     *  drawn if either there has been no previous point for this dataset
     *  or setConnected() has been called with a false argument.
     *  <p>
     *  In order to work well with swing and be thread safe, this method
     *  actually defers execution to the event dispatch thread, where
     *  all user interface actions are performed.  Thus, the point will
     *  not be added immediately (unless you call this method from within
     *  the event dispatch thread). All the methods that do this deferring
     *  coordinate so that they are executed in the order that you
     *  called them.
     *
     *  @param dataset The data set index.
     *  @param x The X position of the new point.
     *  @param y The Y position of the new point.
     *  @param connected If true, a line is drawn to connect to the previous
     *   point.
     */
    public synchronized void addPoint(final int dataset, final double x,
            final double y, final boolean connected) {
        Runnable doAddPoint = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                _addPoint(dataset, x, y, 0, 0, connected, false);
            }
        });

        deferIfNecessary(doAddPoint);
    }

    /** In the specified data set, add the specified x, y point to the
     *  plot with error bars.  Data set indices begin with zero.  If
     *  the dataset does not exist, create it.  yLowEB and
     *  yHighEB are the lower and upper error bars.  The sixth argument
     *  indicates whether the point should be connected by a line to
     *  the previous point.
     *  The new point will be made visible if the plot is visible
     *  on the screen.  Otherwise, it will be drawn the next time the plot
     *  is drawn on the screen.
     *  This method is based on a suggestion by
     *  Michael Altmann (michael@email.labmed.umn.edu).
     *  <p>
     *  In order to work well with swing and be thread safe, this method
     *  actually defers execution to the event dispatch thread, where
     *  all user interface actions are performed.  Thus, the point will
     *  not be added immediately (unless you call this method from within
     *  the event dispatch thread).  All the methods that do this deferring
     *  coordinate so that they are executed in the order that you
     *  called them.
     *
     *  @param dataset The data set index.
     *  @param x The X position of the new point.
     *  @param y The Y position of the new point.
     *  @param yLowEB The low point of the error bar.
     *  @param yHighEB The high point of the error bar.
     *  @param connected If true, a line is drawn to connect to the previous
     *   point.
     */
    public synchronized void addPointWithErrorBars(final int dataset,
            final double x, final double y, final double yLowEB,
            final double yHighEB, final boolean connected) {
        Runnable doAddPoint = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                _addPoint(dataset, x, y, yLowEB, yHighEB, connected, true);
            }
        });

        deferIfNecessary(doAddPoint);
    }

    /** Clear the plot of all data points.  If the argument is true, then
     *  reset all parameters to their initial conditions, including
     *  the persistence, plotting format, and axes formats.
     *  For the change to take effect, you must call repaint().
     *  @param format If true, clear the format controls as well.
     *  <p>
     *  In order to work well with swing and be thread safe, this method
     *  actually defers execution to the event dispatch thread, where
     *  all user interface actions are performed.  Thus, the clear will
     *  not be executed immediately (unless you call this method from within
     *  the event dispatch thread).  All the methods that do this deferring
     *  coordinate so that they are executed in the order that you
     *  called them.
     */
    @Override
    public synchronized void clear(final boolean format) {
        Runnable doClear = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                _clear(format);
            }
        });

        deferIfNecessary(doClear);
    }

    /** Clear the plot of data points in the specified dataset.
     *  This calls repaint() to request an update of the display.
     *  <p>
     *  In order to work well with swing and be thread safe, this method
     *  actually defers execution to the event dispatch thread, where
     *  all user interface actions are performed.  Thus, the point will
     *  not be added immediately (unless you call this method from within
     *  the event dispatch thread).  If you call this method, the addPoint()
     *  method, and the erasePoint() method in any order, they are assured
     *  of being processed in the order that you called them.
     *
     *  @param dataset The dataset to clear.
     */
    public synchronized void clear(final int dataset) {
        Runnable doClear = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                _clear(dataset);
            }
        });

        deferIfNecessary(doClear);
    }

    /** Erase the point at the given index in the given dataset.  If
     * lines are being drawn, these lines are erased and if necessary new
     * ones will be drawn. The point is not checked to
     *  see whether it is in range, so care must be taken by the caller
     *  to ensure that it is.
     *  <p>
     *  In order to work well with swing and be thread safe, this method
     *  actually defers execution to the event dispatch thread, where
     *  all user interface actions are performed.  Thus, the point will
     *  not be erased immediately (unless you call this method from within
     *  the event dispatch thread).  All the methods that do this deferring
     *  coordinate so that they are executed in the order that you
     *  called them.
     *
     *  @param dataset The data set index.
     *  @param index The index of the point to erase.
     */
    public synchronized void erasePoint(final int dataset, final int index) {
        Runnable doErasePoint = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                _erasePoint(dataset, index);
            }
        });

        deferIfNecessary(doErasePoint);
    }

    /** Rescale so that the data that is currently plotted just fits.
     *  This overrides the base class method to ensure that the protected
     *  variables _xBottom, _xTop, _yBottom, and _yTop are valid.
     *  This method calls repaint(), which eventually causes the display
     *  to be updated.
     *  <p>
     *  In order to work well with swing and be thread safe, this method
     *  actually defers execution to the event dispatch thread, where
     *  all user interface actions are performed.  Thus, the fill will
     *  not occur immediately (unless you call this method from within
     *  the event dispatch thread).  All the methods that do this deferring
     *  coordinate so that they are executed in the order that you
     *  called them.
     */
    @Override
    public synchronized void fillPlot() {
        Runnable doFill = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                _fillPlot();
            }
        });

        deferIfNecessary(doFill);
    }

    /** Return whether the default is to connect
     *  subsequent points with a line.  If the result is false, then
     *  points are not connected.  When points are by default
     *  connected, individual points can be not connected by giving the
     *  appropriate argument to addPoint().  Also, a different default
     *  can be set for each dataset, overriding this global default.
     *  @return True if points will be connected by default
     * @see #setConnected
     */
    public boolean getConnected() {
        return _connected;
    }

    /** Return whether a line will be drawn from any
     *  plotted point down to the x axis.
     *  A plot with such lines is also known as a stem plot.
     *  @return True if this is an impulse plot
     *  @see #setImpulses
     */
    public boolean getImpulses() {
        return _impulses;
    }

    /** Return false if setLineStyles() has not yet been called or if
     *  setLineStyles(false) has been called, which signifies that
     *  different line styles are not to be used.  Otherwise, return true.
     *  @return True if line styles are to be used.
     *  @see #setLineStyles(boolean)
     */
    public boolean getLineStyles() {
        return _lineStyles;
    }

    /** Get the marks style, which is one of
     *  "none", "points", "dots", or "various".
     *  @return A string specifying the style for points.
     *  @see #setMarksStyle
     */
    public synchronized String getMarksStyle() {
        // NOTE: If the number of marks increases, we will need to do
        // something better here...
        if (_marks == 0) {
            return "none";
        } else if (_marks == 1) {
            return "points";
        } else if (_marks == 2) {
            return "dots";
        } else if (_marks == 3) {
            return "various";
        } else if (_marks == 4) {
            return "bigdots";
        } else {
            return "pixels";
        }
    }

    /** Return the maximum number of data sets.
     *  This method is deprecated, since there is no longer an upper bound.
     *  @return The maximum number of data sets
     *  @deprecated
     */
    @Deprecated
    public int getMaxDataSets() {
        return Integer.MAX_VALUE;
    }

    /** Return the actual number of data sets.
     *  @return The number of data sets that have been created.
     */
    public synchronized int getNumDataSets() {
        return _points.size();
    }

    /** Return false if setReuseDatasets() has not yet been called
     *  or if setReuseDatasets(false) has been called.
     *  @return false if setReuseDatasets() has not yet been called
     *  or if setReuseDatasets(false) has been called.
     *  @since Ptplot 5.3
     *  @see #setReuseDatasets(boolean)
     */
    public boolean getReuseDatasets() {
        return _reuseDatasets;
    }

    /** Override the base class to indicate that a new data set is being read.
     *  This method is deprecated.  Use read() instead (to read the old
     *  file format) or one of the classes in the plotml package to read
     *  the new (XML) file format.
     *  @deprecated
     */
    @Override
    @Deprecated
    public void parseFile(String filespec, URL documentBase) {
        _firstInSet = true;
        _sawFirstDataSet = false;
        super.parseFile(filespec, documentBase);
    }

    /** Mark the disconnections with a Dot in case value equals true, otherwise these
     *  points are not marked.
     *  @param value True when disconnections should be marked.
     */
    public void markDisconnections(boolean value) {
        _markDisconnections = value;
    }

    /** Read a file with the old syntax (non-XML).
     *  Override the base class to register that we are reading a new
     *  data set.
     *  @param inputStream The input stream.
     *  @exception IOException If the stream cannot be read.
     */
    @Override
    public synchronized void read(InputStream inputStream) throws IOException {
        super.read(inputStream);
        _firstInSet = true;
        _sawFirstDataSet = false;
    }

    /** Create a sample plot.  This is not actually done immediately
     *  unless the calling thread is the event dispatch thread.
     *  Instead, it is deferred to the event dispatch thread.
     *  It is important that the calling thread not hold a synchronize
     *  lock on the Plot object, or deadlock will result (unless the
     *  calling thread is the event dispatch thread).
     */
    @Override
    public synchronized void samplePlot() {
        // This needs to be done in the event thread.
        Runnable sample = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                synchronized (Plot.this) {
                    // Create a sample plot.
                    clear(true);

                    setTitle("Sample plot");
                    setYRange(-4, 4);
                    setXRange(0, 100);
                    setXLabel("time");
                    setYLabel("value");
                    addYTick("-PI", -Math.PI);
                    addYTick("-PI/2", -Math.PI / 2);
                    addYTick("0", 0);
                    addYTick("PI/2", Math.PI / 2);
                    addYTick("PI", Math.PI);
                    setMarksStyle("none");
                    setImpulses(true);

                    boolean first = true;

                    for (int i = 0; i <= 100; i++) {
                        double xvalue = i;

                        // NOTE: jdk 1.3beta has a bug exhibited here.
                        // The value of the second argument in the calls
                        // to addPoint() below is corrupted the second
                        // time that this method is called.  The print
                        // statement below shows that the value is
                        // correct before the call.
                        // log.info("x value: " + xvalue);
                        // For some bizarre reason, this problem goes
                        // away when this code is executed in the event
                        // dispatch thread.
                        addPoint(0, xvalue, 5 * Math.cos((Math.PI * i) / 20),
                                !first);
                        addPoint(1, xvalue, 4.5 * Math.cos((Math.PI * i) / 25),
                                !first);
                        addPoint(2, xvalue, 4 * Math.cos((Math.PI * i) / 30),
                                !first);
                        addPoint(3, xvalue, 3.5 * Math.cos((Math.PI * i) / 35),
                                !first);
                        addPoint(4, xvalue, 3 * Math.cos((Math.PI * i) / 40),
                                !first);
                        addPoint(5, xvalue, 2.5 * Math.cos((Math.PI * i) / 45),
                                !first);
                        addPoint(6, xvalue, 2 * Math.cos((Math.PI * i) / 50),
                                !first);
                        addPoint(7, xvalue, 1.5 * Math.cos((Math.PI * i) / 55),
                                !first);
                        addPoint(8, xvalue, 1 * Math.cos((Math.PI * i) / 60),
                                !first);
                        addPoint(9, xvalue, 0.5 * Math.cos((Math.PI * i) / 65),
                                !first);
                        first = false;
                    } // for
                } // synchronized

                repaint();
            } // run method
        }); // Runnable class

        deferIfNecessary(sample);
    }

    /** Turn bars on or off (for bar charts).  Note that this is a global
     *  property, not per dataset.
     *  @param on If true, turn bars on.
     */
    public void setBars(boolean on) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _bars = on;
    }

    /** Turn bars on and set the width and offset.  Both are specified
     *  in units of the x axis.  The offset is the amount by which the
     *  i <sup>th</sup> data set is shifted to the right, so that it
     *  peeks out from behind the earlier data sets.
     *  @param width The width of the bars.
     *  @param offset The offset per data set.
     */
    public synchronized void setBars(double width, double offset) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _barWidth = width;
        _barOffset = offset;
        _bars = true;
    }

    /** If the argument is true, then the default is to connect
     *  subsequent points with a line.  If the argument is false, then
     *  points are not connected.  When points are by default
     *  connected, individual points can be not connected by giving the
     *  appropriate argument to addPoint().  Also, a different default
     *  can be set for each dataset, overriding this global default.
     *  setConnected will also change the behavior of points that were
     *  already drawn if the graph is redrawn. If it isn't the points
     *  are not touched. If you change back the setConnected state,
     *  the again see what was visible before.
     *  @param on If true, draw lines between points.
     *  @see #setConnected(boolean, int)
     *  @see #getConnected
     */
    public void setConnected(boolean on) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _connected = on;
    }

    /** If the first argument is true, then by default for the specified
     *  dataset, points will be connected by a line.  Otherwise, the
     *  points will not be connected. When points are by default
     *  connected, individual points can be not connected by giving the
     *  appropriate argument to addPoint().
     *  Note that this method should be called before adding any points.
     *  Note further that this method should probably be called from
     *  the event thread.
     *  @param on If true, draw lines between points.
     *  @param dataset The dataset to which this should apply.
     *  @see #setConnected(boolean)
     *  @see #getConnected
     */
    public synchronized void setConnected(boolean on, int dataset) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _checkDatasetIndex(dataset);

        Format fmt = _formats.get(dataset);
        fmt.connected = on;
        fmt.connectedUseDefault = false;
    }

    /** If the argument is true, then a line will be drawn from any
     *  plotted point down to the x axis.  Otherwise, this feature is
     *  disabled.  A plot with such lines is also known as a stem plot.
     *  @param on If true, draw a stem plot.
     *  @see #getImpulses
     */
    public synchronized void setImpulses(boolean on) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _impulses = on;
    }

    /** If the first argument is true, then a line will be drawn from any
     *  plotted point in the specified dataset down to the x axis.
     *  Otherwise, this feature is
     *  disabled.  A plot with such lines is also known as a stem plot.
     *  @param on If true, draw a stem plot.
     *  @param dataset The dataset to which this should apply.
     */
    public synchronized void setImpulses(boolean on, int dataset) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _checkDatasetIndex(dataset);

        Format fmt = _formats.get(dataset);
        fmt.impulses = on;
        fmt.impulsesUseDefault = false;
    }

    /** Set the style of the lines joining marks.
     *  @param styleString A string specifying the color for points.
     *  The following styles are permitted: "solid", "dotted",
     *  "dashed", "dotdashed", "dotdotdashed".
     *  @param dataset The data set index.
     */
    public synchronized void setLineStyle(String styleString, int dataset) {
        float[] dashvalues;
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _checkDatasetIndex(dataset);

        Format format = _formats.get(dataset);
        if (styleString.equalsIgnoreCase("solid")) {
            format.lineStroke = new BasicStroke(_width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0);
            ///_graphics.setStroke(stroke);
        } else if (styleString.equalsIgnoreCase("dotted")) {
            dashvalues = new float[2];
            dashvalues[0] = (float) 2.0;
            dashvalues[1] = (float) 2.0;
            format.lineStroke = new BasicStroke(_width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, dashvalues, 0);
        } else if (styleString.equalsIgnoreCase("dashed")) {
            dashvalues = new float[2];
            dashvalues[0] = (float) 8.0;
            dashvalues[1] = (float) 4.0;
            format.lineStroke = new BasicStroke(_width, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_BEVEL, 0, dashvalues, 0);
        } else if (styleString.equalsIgnoreCase("dotdashed")) {
            dashvalues = new float[4];
            dashvalues[0] = (float) 2.0;
            dashvalues[1] = (float) 2.0;
            dashvalues[2] = (float) 8.0;
            dashvalues[3] = (float) 2.0;
            format.lineStroke = new BasicStroke(_width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, dashvalues, 0);
        } else if (styleString.equalsIgnoreCase("dotdotdashed")) {
            dashvalues = new float[6];
            dashvalues[0] = (float) 2.0;
            dashvalues[1] = (float) 2.0;
            dashvalues[2] = (float) 2.0;
            dashvalues[3] = (float) 2.0;
            dashvalues[4] = (float) 8.0;
            dashvalues[5] = (float) 2.0;
            format.lineStroke = new BasicStroke(_width, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, dashvalues, 0);
        } else {
            StringBuffer results = new StringBuffer();
            for (String style : java.util.Arrays.asList(_LINE_STYLES_ARRAY)) {
                if (results.length() > 0) {
                    results.append(", ");
                }
                results.append("\"" + style + "\"");
            }
            throw new IllegalArgumentException("Line style \"" + styleString
                    + "\" is not found, style must be one of " + results);
        }
        format.lineStyle = styleString;
        format.lineStyleUseDefault = false;
    }

    /** If the argument is true, draw the data sets with different line
     *  styles.  Otherwise, use one line style.
     *  @param lineStyles True if the data sets are to be drawn in different
     *  line styles.
     *  @see #getLineStyles()
     */
    public synchronized void setLineStyles(boolean lineStyles) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _lineStyles = lineStyles;
        if (!_lineStyles) {
            for (Format fmt : _formats) {
                fmt.lineStyle = null;
                fmt.lineStroke = null;
                fmt.lineStyleUseDefault = true;
            }
        }
    }

    /** Set the marks style to "none", "points", "dots", or "various".
     *  In the last case, unique marks are used for the first ten data
     *  sets, then recycled.
     *  This method should be called only from the event dispatch thread.
     *  @param style A string specifying the style for points.
     *  @see #getMarksStyle
     */
    public synchronized void setMarksStyle(String style) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;

        if (style.equalsIgnoreCase("none")) {
            _marks = 0;
        } else if (style.equalsIgnoreCase("points")) {
            _marks = 1;
        } else if (style.equalsIgnoreCase("dots")) {
            _marks = 2;
        } else if (style.equalsIgnoreCase("various")) {
            _marks = 3;
        } else if (style.equalsIgnoreCase("bigdots")) {
            _marks = 4;
        } else if (style.equalsIgnoreCase("pixels")) {
            _marks = 5;
        }
    }

    /** Set the marks style to "none", "points", "dots", "various",
     *  or "pixels" for the specified dataset.
     *  In the last case, unique marks are used for the first ten data
     *  sets, then recycled.
     *  @param style A string specifying the style for points.
     *  @param dataset The dataset to which this should apply.
     *  @see #getMarksStyle
     */
    public synchronized void setMarksStyle(String style, int dataset) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _checkDatasetIndex(dataset);

        Format fmt = _formats.get(dataset);

        if (style.equalsIgnoreCase("none")) {
            fmt.marks = 0;
        } else if (style.equalsIgnoreCase("points")) {
            fmt.marks = 1;
        } else if (style.equalsIgnoreCase("dots")) {
            fmt.marks = 2;
        } else if (style.equalsIgnoreCase("various")) {
            fmt.marks = 3;
        } else if (style.equalsIgnoreCase("bigdots")) {
            fmt.marks = 4;
        } else if (style.equalsIgnoreCase("pixels")) {
            fmt.marks = 5;
        }

        fmt.marksUseDefault = false;
    }

    /** Specify the number of data sets to be plotted together.
     *  This method is deprecated, since it is no longer necessary to
     *  specify the number of data sets ahead of time.
     *  @param numSets The number of data sets.
     *  @deprecated
     */
    @Deprecated
    public void setNumSets(int numSets) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;

        if (numSets < 1) {
            throw new IllegalArgumentException("Number of data sets ("
                    + numSets + ") must be greater than 0.");
        }

        _currentdataset = -1;
        _points.clear();
        _bins.clear();
        _formats.clear();
        _prevxpos.clear();
        _prevypos.clear();
        _prevErasedxpos.clear();
        _prevErasedypos.clear();
        _lastPointWithExtraDot.clear();

        for (int i = 0; i < numSets; i++) {
            _points.add(new ArrayList<PlotPoint>());
            _formats.add(new Format());
            _prevxpos.add(_INITIAL_PREVIOUS_VALUE);
            _prevypos.add(_INITIAL_PREVIOUS_VALUE);
            _prevErasedxpos.add(_INITIAL_PREVIOUS_VALUE);
            _prevErasedypos.add(_INITIAL_PREVIOUS_VALUE);
            _lastPointWithExtraDot.put(i, null);
        }
    }

    /** Calling this method with a positive argument sets the
     *  persistence of the plot to the given number of points.  Calling
     *  with a zero argument turns off this feature, reverting to
     *  infinite memory (unless sweeps persistence is set).  If both
     *  sweeps and points persistence are set then sweeps take
     *  precedence.
     *  <p>
     *  Setting the persistence greater than zero forces the plot to
     *  be drawn in XOR mode, which allows points to be quickly and
     *  efficiently erased.  However, there is a bug in Java (as of
     *  version 1.3), where XOR mode does not work correctly with
     *  double buffering.  Thus, if you call this with an argument
     *  greater than zero, then we turn off double buffering for this
     *  panel <i>and all of its parents</i>.  This actually happens
     *  on the next call to addPoint().
     *  @param persistence Number of points to persist for.
     */
    public void setPointsPersistence(int persistence) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;

        // NOTE: No file format.  It's not clear it makes sense to have one.
        _pointsPersistence = persistence;
    }

    /** If the argument is true, then datasets with the same name
     *  are merged into a single dataset.
     *  @param on If true, then merge datasets.
     *  @see #getReuseDatasets()
     */
    public void setReuseDatasets(boolean on) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _reuseDatasets = on;
    }

    /** Calling this method with a positive argument sets the
     *  persistence of the plot to the given width in units of the
     *  horizontal axis. Calling
     *  with a zero argument turns off this feature, reverting to
     *  infinite memory (unless points persistence is set).  If both
     *  X and points persistence are set then both are applied,
     *  meaning that points that are old by either criterion will
     *  be erased.
     *  <p>
     *  Setting the X persistence greater than zero forces the plot to
     *  be drawn in XOR mode, which allows points to be quickly and
     *  efficiently erased.  However, there is a bug in Java (as of
     *  version 1.3), where XOR mode does not work correctly with
     *  double buffering.  Thus, if you call this with an argument
     *  greater than zero, then we turn off double buffering for this
     *  panel <i>and all of its parents</i>.  This actually happens
     *  on the next call to addPoint().
     *  @param persistence Persistence in units of the horizontal axis.
     */
    public void setXPersistence(double persistence) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;

        // NOTE: No file format.  It's not clear it makes sense to have one.
        _xPersistence = persistence;
    }

    /** Write plot data information to the specified output stream in PlotML.
     *  @param output A buffered print writer.
     */
    @Override
    public synchronized void writeData(PrintWriter output) {
        super.writeData(output);

        for (int dataset = 0; dataset < _points.size(); dataset++) {
            StringBuffer options = new StringBuffer();

            Format fmt = _formats.get(dataset);

            if (!fmt.connectedUseDefault) {
                if (_isConnected(dataset)) {
                    options.append(" connected=\"yes\"");
                } else {
                    options.append(" connected=\"no\"");
                }
            }

            if (!fmt.impulsesUseDefault) {
                if (fmt.impulses) {
                    options.append(" stems=\"yes\"");
                } else {
                    output.println(" stems=\"no\"");
                }
            }

            if (!fmt.lineStyleUseDefault && fmt.lineStyle.length() > 0) {
                options.append(" lineStyle=\"" + fmt.lineStyle + "\"");
            }

            String legend = getLegend(dataset);

            if (legend != null) {
                options.append(" name=\"" + getLegend(dataset) + "\"");
            }

            output.println("<dataset" + options.toString() + ">");

            // Write the data
            ArrayList<PlotPoint> pts = _points.get(dataset);

            for (int pointnum = 0; pointnum < pts.size(); pointnum++) {
                PlotPoint pt = pts.get(pointnum);

                if (!pt.connected) {
                    output.print("<m ");
                } else {
                    output.print("<p ");
                }

                output.print("x=\"" + pt.x + "\" y=\"" + pt.y + "\"");

                if (pt.errorBar) {
                    output.print(" lowErrorBar=\"" + pt.yLowEB
                            + "\" highErrorBar=\"" + pt.yHighEB + "\"");
                }

                output.println("/>");
            }

            output.println("</dataset>");
        }
    }

    /** Write plot format information to the specified output stream in
     *  PlotML, an XML scheme.
     *  @param output A buffered print writer.
     */
    @Override
    public synchronized void writeFormat(PrintWriter output) {
        super.writeFormat(output);

        if (_reuseDatasets) {
            output.println("<reuseDatasets/>");
        }

        StringBuffer defaults = new StringBuffer();

        if (!_connected) {
            defaults.append(" connected=\"no\"");
        }

        switch (_marks) {
        case 1:
            defaults.append(" marks=\"points\"");
            break;

        case 2:
            defaults.append(" marks=\"dots\"");
            break;

        case 3:
            defaults.append(" marks=\"various\"");
            break;

        case 4:
            defaults.append(" marks=\"bigdots\"");
            break;

        case 5:
            defaults.append(" marks=\"pixels\"");
            break;
        }

        // Write the defaults for formats that can be controlled by dataset
        if (_impulses) {
            defaults.append(" stems=\"yes\"");
        }

        if (defaults.length() > 0) {
            output.println("<default" + defaults.toString() + "/>");
        }

        if (_bars) {
            output.println("<barGraph width=\"" + _barWidth + "\" offset=\""
                    + _barOffset + "\"/>");
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /** Check the argument to ensure that it is a valid data set index.
     *  If it is less than zero, throw an IllegalArgumentException (which
     *  is a runtime exception).  If it does not refer to an existing
     *  data set, then fill out the _points Vector so that it does refer
     *  to an existing data set. All other dataset-related vectors are
     *  similarly filled out.
     *  @param dataset The data set index.
     */
    protected synchronized void _checkDatasetIndex(int dataset) {
        if (dataset < 0) {
            throw new IllegalArgumentException(
                    "Plot._checkDatasetIndex: Cannot"
                            + " give a negative number for the data set index.");
        }

        while (dataset >= _points.size()) {
            _points.add(new ArrayList<PlotPoint>());
            _bins.add(new ArrayList<Bin>());
            _pointInBinOffset.add(0);
            _formats.add(new Format());
            _prevxpos.add(_INITIAL_PREVIOUS_VALUE);
            _prevypos.add(_INITIAL_PREVIOUS_VALUE);
            _prevErasedxpos.add(_INITIAL_PREVIOUS_VALUE);
            _prevErasedypos.add(_INITIAL_PREVIOUS_VALUE);
        }
    }

    /** Draw bar from the specified point to the y axis.
     *  If the specified point is below the y axis or outside the
     *  x range, do nothing.  If the <i>clip</i> argument is true,
     *  then do not draw above the y range.
     *  Note that paintComponent() should be called before
     *  calling this method so that _xscale and _yscale are properly set.
     *  This method should be called only from the event dispatch thread.
     *  It is not synchronized, so its caller should be.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param xpos The x position.
     *  @param ypos The y position.
     *  @param clip If true, then do not draw outside the range.
     */
    protected void _drawBar(Graphics graphics, int dataset, long xpos,
            long ypos, boolean clip) {
        if (clip) {
            if (ypos < _uly) {
                ypos = _uly;
            }

            if (ypos > _lry) {
                ypos = _lry;
            }
        }

        if ((ypos <= _lry) && (xpos <= _lrx) && (xpos >= _ulx)) {
            // left x position of bar.
            int barlx = (int) (xpos - ((_barWidth * _xscale) / 2) + (dataset
                    * _barOffset * _xscale));

            // right x position of bar
            int barrx = (int) (barlx + (_barWidth * _xscale));

            if (barlx < _ulx) {
                barlx = _ulx;
            }

            if (barrx > _lrx) {
                barrx = _lrx;
            }

            // Make sure that a bar is always at least one pixel wide.
            if (barlx >= barrx) {
                barrx = barlx + 1;
            }

            // The y position of the zero line.
            long zeroypos = _lry - (long) ((0 - _yMin) * _yscale);

            if (_lry < zeroypos) {
                zeroypos = _lry;
            }

            if (_uly > zeroypos) {
                zeroypos = _uly;
            }

            if ((_yMin >= 0) || (ypos <= zeroypos)) {
                graphics.fillRect(barlx, (int) ypos, barrx - barlx,
                        (int) (zeroypos - ypos));
            } else {
                graphics.fillRect(barlx, (int) zeroypos, barrx - barlx,
                        (int) (ypos - zeroypos));
            }
        }
    }

    /** Draw an error bar for the specified yLowEB and yHighEB values.
     *  If the specified point is below the y axis or outside the
     *  x range, do nothing.  If the <i>clip</i> argument is true,
     *  then do not draw above the y range.
     *  This method should be called only from the event dispatch thread.
     *  It is not synchronized, so its caller should be.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param xpos The x position.
     *  @param yLowEBPos The lower y position of the error bar.
     *  @param yHighEBPos The upper y position of the error bar.
     *  @param clip If true, then do not draw above the range.
     */
    protected void _drawErrorBar(Graphics graphics, int dataset, long xpos,
            long yLowEBPos, long yHighEBPos, boolean clip) {
        _drawLine(graphics, dataset, xpos - _ERRORBAR_LEG_LENGTH, yHighEBPos,
                xpos + _ERRORBAR_LEG_LENGTH, yHighEBPos, clip);
        _drawLine(graphics, dataset, xpos, yLowEBPos, xpos, yHighEBPos, clip);
        _drawLine(graphics, dataset, xpos - _ERRORBAR_LEG_LENGTH, yLowEBPos,
                xpos + _ERRORBAR_LEG_LENGTH, yLowEBPos, clip);
    }

    /** Draw a line from the specified point to the y axis.
     *  If the specified point is below the y axis or outside the
     *  x range, do nothing.  If the <i>clip</i> argument is true,
     *  then do not draw above the y range.
     *  This method should be called only from the event dispatch thread.
     *  It is not synchronized, so its caller should be.
     *  @param graphics The graphics context.
     *  @param xpos The x position.
     *  @param ypos The y position.
     *  @param clip If true, then do not draw outside the range.
     */
    protected void _drawImpulse(Graphics graphics, long xpos, long ypos,
            boolean clip) {
        if (clip) {
            if (ypos < _uly) {
                ypos = _uly;
            }

            if (ypos > _lry) {
                ypos = _lry;
            }
        }

        if ((ypos <= _lry) && (xpos <= _lrx) && (xpos >= _ulx)) {
            // The y position of the zero line.
            double zeroypos = _lry - (long) ((0 - _yMin) * _yscale);

            if (_lry < zeroypos) {
                zeroypos = _lry;
            }

            if (_uly > zeroypos) {
                zeroypos = _uly;
            }

            _setWidth(graphics, 1f);
            graphics.drawLine((int) xpos, (int) ypos, (int) xpos,
                    (int) zeroypos);
        }
    }

    /** Draw a line from the specified starting point to the specified
     *  ending point.  The current color is used.  If the <i>clip</i> argument
     *  is true, then draw only that portion of the line that lies within the
     *  plotting rectangle. This method draws a line one pixel wide.
     *  This method should be called only from the event dispatch thread.
     *  It is not synchronized, so its caller should be.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param startx The starting x position.
     *  @param starty The starting y position.
     *  @param endx The ending x position.
     *  @param endy The ending y position.
     *  @param clip If true, then do not draw outside the range.
     */
    protected void _drawLine(Graphics graphics, int dataset, long startx,
            long starty, long endx, long endy, boolean clip) {
        _drawLine(graphics, dataset, startx, starty, endx, endy, clip, 1f);
    }

    /** Draw a line from the specified starting point to the specified
     *  ending point.  The current color is used.  If the <i>clip</i> argument
     *  is true, then draw only that portion of the line that lies within the
     *  plotting rectangle.  The width argument is ignored if the graphics
     *  argument is not an instance of Graphics2D.
     *  This method should be called only from the event dispatch thread.
     *  It is not synchronized, so its caller should be.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param startx The starting x position.
     *  @param starty The starting y position.
     *  @param endx The ending x position.
     *  @param endy The ending y position.
     *  @param clip If true, then do not draw outside the range.
     *  @param width The thickness of the line.
     */
    protected void _drawLine(Graphics graphics, int dataset, long startx,
            long starty, long endx, long endy, boolean clip, float width) {
        _setWidth(graphics, width);

        Format format = _formats.get(dataset);
        Stroke previousStroke = null;
        if (!format.lineStyleUseDefault && graphics instanceof Graphics2D) {
            previousStroke = ((Graphics2D) graphics).getStroke();
            // Draw a dashed or dotted line
            ((Graphics2D) graphics).setStroke(format.lineStroke);
        }

        if (clip) {
            // Rule out impossible cases.
            if (!(((endx <= _ulx) && (startx <= _ulx))
                    || ((endx >= _lrx) && (startx >= _lrx))
                    || ((endy <= _uly) && (starty <= _uly)) || ((endy >= _lry) && (starty >= _lry)))) {
                // If the end point is out of x range, adjust
                // end point to boundary.
                // The integer arithmetic has to be done with longs so as
                // to not loose precision on extremely close zooms.
                if (startx != endx) {
                    if (endx < _ulx) {
                        endy = (int) (endy + (((starty - endy) * (_ulx - endx)) / (startx - endx)));
                        endx = _ulx;
                    } else if (endx > _lrx) {
                        endy = (int) (endy + (((starty - endy) * (_lrx - endx)) / (startx - endx)));
                        endx = _lrx;
                    }
                }

                // If end point is out of y range, adjust to boundary.
                // Note that y increases downward
                if (starty != endy) {
                    if (endy < _uly) {
                        endx = (int) (endx + (((startx - endx) * (_uly - endy)) / (starty - endy)));
                        endy = _uly;
                    } else if (endy > _lry) {
                        endx = (int) (endx + (((startx - endx) * (_lry - endy)) / (starty - endy)));
                        endy = _lry;
                    }
                }

                // Adjust current point to lie on the boundary.
                if (startx != endx) {
                    if (startx < _ulx) {
                        starty = (int) (starty + (((endy - starty) * (_ulx - startx)) / (endx - startx)));
                        startx = _ulx;
                    } else if (startx > _lrx) {
                        starty = (int) (starty + (((endy - starty) * (_lrx - startx)) / (endx - startx)));
                        startx = _lrx;
                    }
                }

                if (starty != endy) {
                    if (starty < _uly) {
                        startx = (int) (startx + (((endx - startx) * (_uly - starty)) / (endy - starty)));
                        starty = _uly;
                    } else if (starty > _lry) {
                        startx = (int) (startx + (((endx - startx) * (_lry - starty)) / (endy - starty)));
                        starty = _lry;
                    }
                }
            }

            // Are the new points in range?
            if ((endx >= _ulx) && (endx <= _lrx) && (endy >= _uly)
                    && (endy <= _lry) && (startx >= _ulx) && (startx <= _lrx)
                    && (starty >= _uly) && (starty <= _lry)) {
                graphics.drawLine((int) startx, (int) starty, (int) endx,
                        (int) endy);
            }
        } else {
            // draw unconditionally.
            graphics.drawLine((int) startx, (int) starty, (int) endx,
                    (int) endy);
        }
        if (previousStroke != null) {
            ((Graphics2D) graphics).setStroke(previousStroke);
        }
    }

    /** Draw the axes and then plot all points. If the second
     *  argument is true, clear the display first.
     *  This method is called by paintComponent().
     *  To cause it to be called you would normally call repaint(),
     *  which eventually causes paintComponent() to be called.
     *  <p>
     *  Note that this is synchronized so that points are not added
     *  by other threads while the drawing is occurring.  This method
     *  should be called only from the event dispatch thread, consistent
     *  with swing policy.
     *  @param graphics The graphics context.
     *  @param clearfirst If true, clear the plot before proceeding.
     *  @param drawRectangle The Rectangle to draw in.
     */
    @Override
    protected synchronized void _drawPlot(Graphics graphics,
            boolean clearfirst, Rectangle drawRectangle) {
        // BRDebug log.error.println("_drawPlot(begin)");
        if (_graphics == null) {
            _graphics = graphics;
        } else if (graphics != _graphics) {
            // If the graphics has changed, then we don't care about
            // the previous values.  Exporting to EPS uses a different
            // graphics, see test/onePointStem.plt for an example that
            // requires this change.
            _graphics = graphics;
            _prevxpos.clear();
            _prevypos.clear();
            _prevErasedxpos.clear();
            _prevErasedypos.clear();
            _lastPointWithExtraDot.clear();
            for (int dataset = 0; dataset < _points.size(); dataset++) {
                _prevxpos.add(_INITIAL_PREVIOUS_VALUE);
                _prevypos.add(_INITIAL_PREVIOUS_VALUE);
                _prevErasedxpos.add(_INITIAL_PREVIOUS_VALUE);
                _prevErasedypos.add(_INITIAL_PREVIOUS_VALUE);
                _lastPointWithExtraDot.put(dataset, null);
            }
        }

        // We must call PlotBox._drawPlot() before calling _drawPlotPoint
        // so that _xscale and _yscale are set.
        super._drawPlot(graphics, clearfirst, drawRectangle);

        // Divide the points into different Bins. This should be done each time
        // _xscale and _yscale are set
        _dividePointsIntoBins();

        // Plot the points in reverse order so that the first colors
        // appear on top.
        for (int dataset = _bins.size() - 1; dataset >= 0; dataset--) {
            ArrayList<Bin> data = _bins.get(dataset);

            int numberOfBins = data.size();

            for (int binnum = 0; binnum < numberOfBins; binnum++) {
                _drawBin(graphics, dataset, binnum);
            }

            if (_markDisconnections && _marks == 0 && numberOfBins > 0) {
                Bin bin = data.get(numberOfBins - 1);

                // We are going to add an extra dot for the last point.
                // Every segment will be marked by two dots in case there
                // are no marks.
                // Typically at the end the plot is repaint and hence also the
                // dot need to be added to close the last segment. However
                // it might happen that points are added afterwards again and in this
                // case the mark has to be removed again.

                boolean connectedFlag = getConnected();
                ArrayList<PlotPoint> points = _points.get(dataset);

                int currentPointPosition = points.size() - 1;
                PlotPoint lastPoint = points.get(currentPointPosition);
                if (connectedFlag && lastPoint.connected) {
                    // In case the point is not connected there is already a dot.
                    _setColorForDrawing(graphics, dataset, false);
                    long xpos = bin.xpos;
                    long ypos = _lry - (long) ((lastPoint.y - _yMin) * _yscale);
                    // BRDebug log.info("_drawPlot");
                    _drawPoint(graphics, dataset, xpos, ypos, true, 2 /*dots*/);
                    _resetColorForDrawing(graphics, false);

                    // We keep track of the last dot that has been add to be able to
                    // remove the dot again in case an extra point was added afterwards.
                    _lastPointWithExtraDot.put(dataset, lastPoint);
                }
            }
        }

        _showing = true;
    }

    /** Put a mark corresponding to the specified dataset at the
     *  specified x and y position. The mark is drawn in the current
     *  color. What kind of mark is drawn depends on the _marks
     *  variable and the dataset argument. If the fourth argument is
     *  true, then check the range and plot only points that
     *  are in range.
     *  This method should be called only from the event dispatch thread.
     *  It is not synchronized, so its caller should be.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param xpos The x position.
     *  @param ypos The y position.
     *  @param clip If true, then do not draw outside the range.
     */
    @Override
    protected void _drawPoint(Graphics graphics, int dataset, long xpos,
            long ypos, boolean clip) {
        // Check to see whether the dataset has a marks directive
        Format fmt = _formats.get(dataset);
        int marks = _marks;

        if (!fmt.marksUseDefault) {
            marks = fmt.marks;
        }
        _drawPoint(graphics, dataset, xpos, ypos, clip, marks);
    }

    /** Parse a line that gives plotting information. Return true if
     *  the line is recognized.  Lines with syntax errors are ignored.
     *  It is not synchronized, so its caller should be.
     *  @param line A command line.
     *  @return True if the line is recognized.
     */
    @Override
    protected boolean _parseLine(String line) {
        boolean connected = false;

        if (_isConnected(_currentdataset)) {
            connected = true;
        }

        // parse only if the super class does not recognize the line.
        if (super._parseLine(line)) {
            return true;
        } else {
            // We convert the line to lower case so that the command
            // names are case insensitive
            String lcLine = line.toLowerCase();

            if (lcLine.startsWith("linestyle:")) {
                String style = (line.substring(10)).trim();
                setLineStyle(style, _currentdataset);
                return true;
            } else if (lcLine.startsWith("marks:")) {
                // If we have seen a dataset directive, then apply the
                // request to the current dataset only.
                String style = (line.substring(6)).trim();

                if (_sawFirstDataSet) {
                    setMarksStyle(style, _currentdataset);
                } else {
                    setMarksStyle(style);
                }

                return true;
            } else if (lcLine.startsWith("numsets:")) {
                // Ignore.  No longer relevant.
                return true;
            } else if (lcLine.startsWith("reusedatasets:")) {
                if (lcLine.indexOf("off", 16) >= 0) {
                    setReuseDatasets(false);
                } else {
                    setReuseDatasets(true);
                }

                return true;
            } else if (lcLine.startsWith("dataset:")) {
                if (_reuseDatasets && (lcLine.length() > 0)) {
                    String tlegend = (line.substring(8)).trim();
                    _currentdataset = -1;

                    int i;

                    for (i = 0; i <= _maxDataset; i++) {
                        if (getLegend(i).compareTo(tlegend) == 0) {
                            _currentdataset = i;
                        }
                    }

                    if (_currentdataset != -1) {
                        return true;
                    } else {
                        _currentdataset = _maxDataset;
                    }
                }

                // new data set
                _firstInSet = true;
                _sawFirstDataSet = true;
                _currentdataset++;

                if (lcLine.length() > 0) {
                    String legend = (line.substring(8)).trim();

                    if ((legend != null) && (legend.length() > 0)) {
                        addLegend(_currentdataset, legend);
                    }
                }

                _maxDataset = _currentdataset;
                return true;
            } else if (lcLine.startsWith("lines:")) {
                if (_sawFirstDataSet) {
                    // Backward compatbility with xgraph here.
                    // If we see some data sets, then they are drawn
                    // with lines, if we then see a Lines: off
                    // the current dataset and succeeding datasets
                    // will be drawn without lines.
                    // For each of the existing datasets, if
                    // it fmt.connectedUseDefault is true, then
                    // set fmt.connectedUseDefault to false and set
                    // the value of fmt.connected

                    for (Format format : _formats) {

                        if (format.connectedUseDefault) {
                            format.connectedUseDefault = false;
                            format.connected = _connected;
                        }
                    }
                }

                if (lcLine.indexOf("off", 6) >= 0) {
                    setConnected(false);
                } else {
                    setConnected(true);
                }

                return true;
            } else if (lcLine.startsWith("impulses:")) {
                // If we have not yet seen a dataset, then this is interpreted
                // as the global default.  Otherwise, it is assumed to apply
                // only to the current dataset.
                if (_sawFirstDataSet) {
                    if (lcLine.indexOf("off", 9) >= 0) {
                        setImpulses(false, _currentdataset);
                    } else {
                        setImpulses(true, _currentdataset);
                    }
                } else {
                    if (lcLine.indexOf("off", 9) >= 0) {
                        setImpulses(false);
                    } else {
                        setImpulses(true);
                    }
                }

                return true;
            } else if (lcLine.startsWith("bars:")) {
                if (lcLine.indexOf("off", 5) >= 0) {
                    setBars(false);
                } else {
                    setBars(true);

                    int comma = line.indexOf(",", 5);
                    String _thisBarWidth;
                    String baroffset = null;

                    if (comma > 0) {
                        _thisBarWidth = (line.substring(5, comma)).trim();
                        baroffset = (line.substring(comma + 1)).trim();
                    } else {
                        _thisBarWidth = (line.substring(5)).trim();
                    }

                    try {
                        // Use Double.parseDouble() and avoid creating a Double
                        double bwidth = Double.parseDouble(_thisBarWidth);
                        double boffset = _barOffset;

                        if (baroffset != null) {
                            boffset = Double.parseDouble(baroffset);
                        }

                        setBars(bwidth, boffset);
                    } catch (NumberFormatException e) {
                        log.warn("ignoring..", e);
                        // ignore if format is bogus.
                    }
                }

                return true;
            } else if (line.startsWith("move:")) {
                // a disconnected point
                connected = false;

                // deal with 'move: 1 2' and 'move:2 2'
                line = line.substring(5, line.length()).trim();
            } else if (line.startsWith("move")) {
                // a disconnected point
                connected = false;

                // deal with 'move 1 2' and 'move2 2'
                line = line.substring(4, line.length()).trim();
            } else if (line.startsWith("draw:")) {
                // a connected point, if connect is enabled.
                line = line.substring(5, line.length()).trim();
            } else if (line.startsWith("draw")) {
                // a connected point, if connect is enabled.
                line = line.substring(4, line.length()).trim();
            }

            line = line.trim();

            // We can't use StreamTokenizer here because it can't
            // process numbers like 1E-01.
            // This code is somewhat optimized for speed, since
            // most data consists of two data points, we want
            // to handle that case as efficiently as possible.
            int fieldsplit = line.indexOf(",");

            if (fieldsplit == -1) {
                fieldsplit = line.indexOf(" ");
            }

            if (fieldsplit == -1) {
                fieldsplit = line.indexOf("\t"); // a tab
            }

            if (fieldsplit > 0) {
                String x = (line.substring(0, fieldsplit)).trim();
                String y = (line.substring(fieldsplit + 1)).trim();

                // Any more separators?
                int fieldsplit2 = y.indexOf(",");

                if (fieldsplit2 == -1) {
                    fieldsplit2 = y.indexOf(" ");
                }

                if (fieldsplit2 == -1) {
                    fieldsplit2 = y.indexOf("\t"); // a tab
                }

                if (fieldsplit2 > 0) {
                    line = (y.substring(fieldsplit2 + 1)).trim();
                    y = (y.substring(0, fieldsplit2)).trim();
                }

                try {
                    // Use Double.parseDouble() and avoid creating a Double.
                    double xpt = Double.parseDouble(x);
                    double ypt = Double.parseDouble(y);

                    if (fieldsplit2 > 0) {
                        // There was one separator after the y value, now
                        // look for another separator.
                        int fieldsplit3 = line.indexOf(",");

                        if (fieldsplit3 == -1) {
                            fieldsplit3 = line.indexOf(" ");
                        }

                        //if (fieldsplit3 == -1) {
                        //    fieldsplit2 = line.indexOf("\t"); // a tab
                        //}
                        if (fieldsplit3 > 0) {
                            // We have more numbers, assume that this is
                            // an error bar
                            String yl = (line.substring(0, fieldsplit3)).trim();
                            String yh = (line.substring(fieldsplit3 + 1))
                                    .trim();
                            double yLowEB = Double.parseDouble(yl);
                            double yHighEB = Double.parseDouble(yh);
                            connected = _addLegendIfNecessary(connected);
                            addPointWithErrorBars(_currentdataset, xpt, ypt,
                                    yLowEB, yHighEB, connected);
                            return true;
                        } else {
                            // It is unlikely that we have a fieldsplit2 >0
                            // but not fieldsplit3 >0, but just in case:
                            connected = _addLegendIfNecessary(connected);
                            addPoint(_currentdataset, xpt, ypt, connected);
                            return true;
                        }
                    } else {
                        // There were no more fields, so this is
                        // a regular pt.
                        connected = _addLegendIfNecessary(connected);
                        addPoint(_currentdataset, xpt, ypt, connected);
                        return true;
                    }
                } catch (NumberFormatException e) {
                    log.warn("ignoring", e);
                    // ignore if format is bogus.
                }
            }
        }

        return false;
    }

    /** Reset a scheduled redraw tasks.
     */
    @Override
    protected void _resetScheduledTasks() {
        Runnable redraw = new RunnableExceptionCatcher(new Runnable() {
            @Override
            public void run() {
                _scheduledBinsToAdd.clear();
                _scheduledBinsToErase.clear();
            }
        });
        synchronized (this) {
            deferIfNecessary(redraw);
        }
    }

    /** Perform a scheduled redraw.
     */
    @Override
    protected void _scheduledRedraw() {
        if (_needPlotRefill || _needBinRedraw) {
            Runnable redraw = new RunnableExceptionCatcher(new Runnable() {
                @Override
                public void run() {
                    ArrayList<Integer> scheduledBinsToAdd = new ArrayList<Integer>();
                    for (int i = 0; i < _scheduledBinsToAdd.size(); ++i) {
                        scheduledBinsToAdd.add(_scheduledBinsToAdd.get(i));
                        _scheduledBinsToAdd.set(i, 0);
                    }
                    ArrayList<Integer> scheduledBinsToErase = new ArrayList<Integer>();
                    for (int i = 0; i < _scheduledBinsToErase.size(); ++i) {
                        scheduledBinsToErase.add(_scheduledBinsToErase.get(i));
                        _scheduledBinsToErase.set(i, 0);
                    }
                    _needBinRedraw = false;
                    if (_needPlotRefill) {
                        fillPlot();
                        _needPlotRefill = false;
                    } else {
                        Graphics graphics = getGraphics();
                        if (graphics != null) {
                            {
                                int nbrOfDataSets = _scheduledBinsToAdd.size();
                                for (int i = 0; i < nbrOfDataSets; ++i) {
                                    int nbrOfBins = _bins.get(i).size();
                                    int nbrOfBinsToAdd = scheduledBinsToAdd
                                            .get(i);
                                    for (int binIndex = nbrOfBins
                                            - nbrOfBinsToAdd; binIndex < nbrOfBins; ++binIndex) {
                                        assert binIndex >= 0;
                                        _drawBin(graphics, i, binIndex);
                                    }
                                }
                            }
                            {
                                int nbrOfDataSets = _scheduledBinsToErase
                                        .size();
                                for (int i = 0; i < nbrOfDataSets; ++i) {
                                    int nbrOfBinsToErase = scheduledBinsToErase
                                            .get(i);
                                    for (int binIndex = 0; binIndex < nbrOfBinsToErase; ++binIndex) {
                                        _eraseFirstBin(i);
                                    }
                                }
                            }
                        }
                    }
                }
            });
            synchronized (this) {
                deferIfNecessary(redraw);
            }
        }
    }

    /** If the graphics argument is an instance of Graphics2D, then set
     *  the current stroke to the specified width.  Otherwise, do nothing.
     *  @param graphics The graphics object.
     *  @param width The width.
     */
    protected void _setWidth(Graphics graphics, float width) {
        _width = width;

        // For historical reasons, the API here only assumes Graphics
        // objects, not Graphics2D.
        if (graphics instanceof Graphics2D) {
            // We cache the two most common cases.
            if (width == 1f) {
                ((Graphics2D) graphics).setStroke(_LINE_STROKE1);
            } else if (width == 2f) {
                ((Graphics2D) graphics).setStroke(_LINE_STROKE2);
            } else {
                ((Graphics2D) graphics).setStroke(new BasicStroke(width,
                        BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            }
        }
    }

    /** Write plot information to the specified output stream in
     *  the "old syntax," which predates PlotML.
     *  Derived classes should override this method to first call
     *  the parent class method, then add whatever additional information
     *  they wish to add to the stream.
     *  It is not synchronized, so its caller should be.
     *  @param output A buffered print writer.
     *  @deprecated
     */
    @Override
    @Deprecated
    protected void _writeOldSyntax(PrintWriter output) {
        super._writeOldSyntax(output);

        // NOTE: NumSets is obsolete, so we don't write it.
        if (_reuseDatasets) {
            output.println("ReuseDatasets: on");
        }

        if (!_connected) {
            output.println("Lines: off");
        }

        if (_bars) {
            output.println("Bars: " + _barWidth + ", " + _barOffset);
        }

        // Write the defaults for formats that can be controlled by dataset
        if (_impulses) {
            output.println("Impulses: on");
        }

        switch (_marks) {
        case 1:
            output.println("Marks: points");
            break;

        case 2:
            output.println("Marks: dots");
            break;

        case 3:
            output.println("Marks: various");
            break;

        case 4:
            output.println("Marks: pixels");
            break;
        }

        for (int dataset = 0; dataset < _points.size(); dataset++) {
            // Write the dataset directive
            String legend = getLegend(dataset);

            if (legend != null) {
                output.println("DataSet: " + getLegend(dataset));
            } else {
                output.println("DataSet:");
            }

            // Write dataset-specific format information
            Format fmt = _formats.get(dataset);

            if (!fmt.impulsesUseDefault) {
                if (fmt.impulses) {
                    output.println("Impulses: on");
                } else {
                    output.println("Impulses: off");
                }
            }

            if (!fmt.lineStyleUseDefault) {
                output.println("lineStyle: " + fmt.lineStyle);
            }

            if (!fmt.marksUseDefault) {
                switch (fmt.marks) {
                case 0:
                    output.println("Marks: none");
                    break;

                case 1:
                    output.println("Marks: points");
                    break;

                case 2:
                    output.println("Marks: dots");
                    break;

                case 3:
                    output.println("Marks: various");
                    break;

                case 4:
                    output.println("Marks: pixels");
                    break;
                }
            }

            // Write the data
            ArrayList<PlotPoint> pts = _points.get(dataset);

            for (int pointnum = 0; pointnum < pts.size(); pointnum++) {
                PlotPoint pt = pts.get(pointnum);

                if (!pt.connected) {
                    output.print("move: ");
                }

                if (pt.errorBar) {
                    output.println(pt.x + ", " + pt.y + ", " + pt.yLowEB + ", "
                            + pt.yHighEB);
                } else {
                    output.println(pt.x + ", " + pt.y);
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** The current dataset. */
    protected int _currentdataset = -1;

    /** An indicator of the marks style.  See _parseLine method for
     * interpretation.
     */
    protected volatile int _marks;

    /** A vector of datasets. */
    protected ArrayList<ArrayList<PlotPoint>> _points = new ArrayList<ArrayList<PlotPoint>>();

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////

    /* Add a legend if necessary, return the value of the connected flag.
     */
    private boolean _addLegendIfNecessary(boolean connected) {
        if ((!_sawFirstDataSet || (_currentdataset < 0)) && !_reuseDatasets) {
            // We did not set a DataSet line, but
            // we did get called with -<digit> args and
            // we did not see reusedatasets: yes
            _sawFirstDataSet = true;
            _currentdataset++;
        }

        if (!_sawFirstDataSet && (getLegend(_currentdataset) == null)) {
            // We did not see a "DataSet" string yet,
            // nor did we call addLegend().
            _firstInSet = true;
            _sawFirstDataSet = true;
            addLegend(_currentdataset, "Set " + _currentdataset);
        }

        if (_firstInSet && !_reuseDatasets) {
            connected = false;
            _firstInSet = false;
        }

        return connected;
    }

    /* In the specified data set, add the specified x, y point to the
     * plot.  Data set indices begin with zero.  If the dataset
     * argument is less than zero, throw an IllegalArgumentException
     * (a runtime exception).  If it refers to a data set that does
     * not exist, create the data set.  The fourth argument indicates
     * whether the point should be connected by a line to the previous
     * point.  However, this argument is ignored if setConnected() has
     * been called with a false argument.  In that case, a point is never
     * connected to the previous point.  That argument is also ignored
     * if the point is the first in the specified dataset.
     * The point is drawn on the screen only if is visible.
     * Otherwise, it is drawn the next time paintComponent() is called.
     *
     * This is not synchronized, so the caller should be.  Moreover, this
     * should only be called in the event dispatch thread. It should only
     * be called via deferIfNecessary().
     */
    private void _addPoint(int dataset, double x, double y, double yLowEB,
            double yHighEB, boolean connected, boolean errorBar) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;

        _checkDatasetIndex(dataset);

        if (_xlog) {
            if (x <= 0.0) {
                log.error("Can't plot non-positive X values "
                        + "when the logarithmic X axis value is specified: "
                        + x);
                return;
            }

            x = Math.log(x) * _LOG10SCALE;
        }

        if (_ylog) {
            if (y <= 0.0) {
                log.error("Can't plot non-positive Y values "
                        + "when the logarithmic Y axis value is specified: "
                        + y);
                return;
            }

            y = Math.log(y) * _LOG10SCALE;

            if (errorBar) {
                if ((yLowEB <= 0.0) || (yHighEB <= 0.0)) {
                    log.error("Can't plot non-positive Y values "
                                    + "when the logarithmic Y axis value is specified: "
                                    + y);
                    return;
                }

                yLowEB = Math.log(yLowEB) * _LOG10SCALE;
                yHighEB = Math.log(yHighEB) * _LOG10SCALE;
            }
        }

        ArrayList<Bin> bins = _bins.get(dataset);
        ArrayList<PlotPoint> points = _points.get(dataset);

        // If X persistence has been set, then delete any old points.
        if (_xPersistence > 0.0) {
            int numToDelete = 0;
            int nbrOfBins = bins.size();

            while (numToDelete < nbrOfBins) {
                Bin old = bins.get(numToDelete);

                if ((x - points.get(old.firstPointIndex()).originalx) <= _xPersistence) {
                    break;
                }

                numToDelete++;
            }

            numToDelete = Math.min(numToDelete, nbrOfBins - 1);
            //We want to keep at least one bin.

            if (!_timedRepaint()) {
                for (int i = 0; i < numToDelete; i++) {
                    // Again, we are in the event thread, so this is safe...
                    _eraseFirstBin(dataset);
                }
            } else {
                _scheduleBinRedrawRemove(dataset, numToDelete);
            }
        }

        // Get the new size after deletions.
        int size = points.size();

        PlotPoint pt = new PlotPoint();

        // Original value of x before wrapping.
        pt.originalx = x;

        // Modify x if wrapping.
        if (_wrap) {
            double width = _wrapHigh - _wrapLow;

            if (x < _wrapLow) {
                x += (width * Math.floor(1.0 + ((_wrapLow - x) / width)));
            } else if (x > _wrapHigh) {
                x -= (width * Math.floor(1.0 + ((x - _wrapHigh) / width)));

                // NOTE: Could quantization errors be a problem here?
                if (Math.abs(x - _wrapLow) < 0.00001) {
                    x = _wrapHigh;
                }
            }
        }

        boolean needPlotRefill = false;

        // For auto-ranging, keep track of min and max.

        if (x < _xBottom) {
            if (_automaticRescale() && _xTop != -Double.MAX_VALUE
                    && _xBottom != Double.MAX_VALUE) {
                needPlotRefill = true;
                _xBottom = x - (_xTop - _xBottom);
            } else {
                _xBottom = x;
            }
        }

        if (x > _xTop) {
            if (_automaticRescale() && _xTop != -Double.MAX_VALUE
                    && _xBottom != Double.MAX_VALUE) {
                needPlotRefill = true;
                _xTop = x + _xTop - _xBottom;
            } else {
                _xTop = x;
            }
        }

        if (y < _yBottom) {
            if (_automaticRescale() && _yTop != -Double.MAX_VALUE
                    && _yBottom != Double.MAX_VALUE) {
                needPlotRefill = true;
                _yBottom = y - (_yTop - _yBottom);
            } else {
                _yBottom = y;
            }
        }

        if (y > _yTop) {
            if (_automaticRescale() && _yTop != -Double.MAX_VALUE
                    && _yBottom != Double.MAX_VALUE) {
                needPlotRefill = true;
                _yTop = y + _yTop - _yBottom;
            } else {
                _yTop = y;
            }
        }

        pt.x = x;
        pt.y = y;
        pt.connected = connected && _isConnected(dataset);

        if (errorBar) {
            if (yLowEB < _yBottom) {
                _yBottom = yLowEB;
            }

            if (yLowEB > _yTop) {
                _yTop = yLowEB;
            }

            if (yHighEB < _yBottom) {
                _yBottom = yHighEB;
            }

            if (yHighEB > _yTop) {
                _yTop = yHighEB;
            }

            pt.yLowEB = yLowEB;
            pt.yHighEB = yHighEB;
            pt.errorBar = true;
        }

        // If this is the first point in the dataset, clear the connected bit.
        if (size == 0) {
            pt.connected = false;
        } else if (_wrap) {
            // Do not connect points if wrapping...
            PlotPoint old = points.get(size - 1);

            if (old.x > x) {
                pt.connected = false;
            }
        }

        points.add(pt);

        int nbrOfBins = dataset < _bins.size() ? _bins.get(dataset).size() : 0;
        _addPointToBin(dataset, pt, size);

        boolean binAdded = _bins.get(dataset).size() != nbrOfBins;

        // If points persistence has been set, then delete first bin if there are to many points
        //      However we don't want to delete all bins...
        if (_pointsPersistence > 0) {
            if (size > _pointsPersistence && bins.size() > 2) {
                // Again, we are in the event thread, so this is safe...
                if (!_timedRepaint()) {
                    _eraseFirstBin(dataset);
                } else {
                    _scheduleBinRedrawRemove(dataset, 1);
                }
            }
        }

        // Draw the point on the screen only if the plot is showing.
        Graphics graphics = getGraphics();

        // Need to check that graphics is not null because plot may have
        // been dismissed.
        if (_showing && (graphics != null)) {
            if (((_pointsPersistence > 0) || (_xPersistence > 0.0))
                    && isDoubleBuffered()) {
                // NOTE: Double buffering has a bug in Java (in at least
                // version 1.3) where there is a one pixel alignment problem
                // that prevents XOR drawing from working correctly.
                // XOR drawing is used for live plots, and if double buffering
                // is turned on, then cruft is left on the screen whenever the
                // fill or zoom functions are used.
                // Here, if it hasn't been done already, we turn off double
                // buffering on this panel and all its parents for which this
                // is possible.  Note that we could do this globally using
                //
                // RepaintManager repaintManager
                //        = RepaintManager.currentManager(this);
                // repaintManager.setDoubleBufferingEnabled(false);
                //
                // However, that turns off double buffering in all windows
                // of the application, which means that other windows that only
                // work properly with double buffering (such as vergil windows)
                // will not work.
                //
                // NOTE: This fix creates another problem...
                // If there are other widgets besides the plotter in the
                // same top-level window, and they implement double
                // buffering (which they will by default), then they
                // need to be opaque or drawing artifacts will appear
                // upon exposure events.  The workaround is simple:
                // Make these other objects opaque, and set their
                // background color appropriately.
                //
                // See:
                // <pre>
                // http://developer.java.sun.com/developer/bugParade/bugs/
                //     4188795.html
                //     4204551.html
                //     4295712.htm
                // </pre>
                //
                // Since we are assured of being in the event dispatch thread,
                // we can simply execute this.
                setDoubleBuffered(false);

                Component parent = getParent();

                while (parent != null) {
                    if (parent instanceof JComponent) {
                        ((JComponent) parent).setDoubleBuffered(false);
                    }

                    parent = parent.getParent();
                }
            }

            assert _bins.get(dataset).size() > 0;

            if (!_timedRepaint()) {
                // Again, we are in the event thread, so this is safe...
                _drawBin(graphics, dataset, _bins.get(dataset).size() - 1);
            } else {
                if (needPlotRefill) {
                    _needPlotRefill = true;
                } else {
                    _scheduleBinRedrawAdd(dataset, binAdded);
                }
            }
        }

        if (_wrap && (Math.abs(x - _wrapHigh)) < 0.00001) {
            // Plot a second point at the low end of the range.
            _addPoint(dataset, _wrapLow, y, yLowEB, yHighEB, false, errorBar);
        }
    }

    /** Add point to the corresponding Bin. If the point fits into the last bin
     * (the same xpos) it will be added to this one, otherwise a new bin will
     * be created
     */
    private void _addPointToBin(int dataset, PlotPoint point, int pointIndex) {
        ArrayList<Bin> bins = _bins.get(dataset); //we could move this out of this function (for performance)

        // Use long for positions because these numbers can be quite large
        // (when we are zoomed out a lot).

        // _drawPlot should have been called to fill in _xscale and _yscale

        long xpos = _ulx + (long) ((point.x - _xMin) * _xscale);
        long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
        int nbrOfBins = bins.size();
        //Cached since it came out in JProfiler (everything becomes costly if you
        //  do it a lot of times)

        Bin lastBin = nbrOfBins > 0 ? bins.get(nbrOfBins - 1) : null;
        //Cached since it came out in JProfiler (everything becomes costly if you do
        //  it a lot of times)

        if (nbrOfBins == 0 || lastBin == null || lastBin.xpos != xpos) {
            // Does not fall within last bin => add one bin
            // nbrOfBins += 1;
            lastBin = new Bin(xpos, dataset);
            bins.add(lastBin);
        }
        lastBin.addPoint(point, pointIndex, ypos);
    }

    /* Clear the plot of all data points.  If the argument is true, then
     * reset all parameters to their initial conditions, including
     * the persistence, plotting format, and axes formats.
     * For the change to take effect, you must call repaint().
     *
     * This is not synchronized, so the caller should be.  Moreover, this
     * should only be called in the event dispatch thread. It should only
     * be called via deferIfNecessary().
     */
    private void _clear(boolean format) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        super.clear(format);
        _currentdataset = -1;
        _points.clear();
        for (ArrayList<Bin> data : _bins) {
            data.clear();
        }
        _bins.clear();
        _prevxpos.clear();
        _prevypos.clear();
        _prevErasedxpos.clear();
        _prevErasedypos.clear();
        _maxDataset = -1;
        _firstInSet = true;
        _sawFirstDataSet = false;
        _xyInvalid = true;
        _resetScheduledTasks();

        if (format) {
            _showing = false;

            // Reset format controls
            _formats.clear();
            _marks = 0;
            _pointsPersistence = 0;
            _xPersistence = 0;
            _bars = false;
            _barWidth = 0.5;
            _barOffset = 0.05;
            _connected = true;
            _impulses = false;
            _reuseDatasets = false;
        }
    }

    /** Clear the plot of data points in the specified dataset.
     *  This calls repaint() to request an update of the display.
     *
     * This is not synchronized, so the caller should be.  Moreover, this
     * should only be called in the event dispatch thread. It should only
     * be called via deferIfNecessary().
     */
    private void _clear(int dataset) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;
        _checkDatasetIndex(dataset);
        _xyInvalid = true;

        ArrayList<PlotPoint> points = _points.get(dataset);

        points.clear();

        _points.set(dataset, points);
        _bins.get(dataset).clear();

        _lastPointWithExtraDot.clear();
        repaint();
    }

    /** Subdivide all points into different bins. A bin is represents a number of
     * points that are all displayed on the same x position. When calling this function
     * all existing bins will first be cleared.
     */
    private void _dividePointsIntoBins() {
        for (int i = 0; i < _scheduledBinsToAdd.size(); ++i) {
            _scheduledBinsToAdd.set(i, 0);
        }
        for (int i = 0; i < _scheduledBinsToErase.size(); ++i) {
            _scheduledBinsToErase.set(i, 0);
        }
        _needBinRedraw = false;

        _bins.clear();
        _pointInBinOffset.clear();
        int nbrOfDataSets = _points.size();
        for (int i = 0; i < nbrOfDataSets; ++i) {
            _bins.add(new ArrayList<Bin>());
            _pointInBinOffset.add(0);
        }

        for (int dataset = 0; dataset < nbrOfDataSets; ++dataset) {
            ArrayList<PlotPoint> points = _points.get(dataset);
            int numberOfPoints = points.size();
            for (int pointIndex = 0; pointIndex < numberOfPoints; ++pointIndex) {
                _addPointToBin(dataset, points.get(pointIndex), pointIndex);
            }
        }
    }

    /* Draw the points within a specific bin and associated lines, if any.
     * Note that paintComponent() should be called before
     * calling this method so that it calls _drawPlot(), which sets
     * _xscale and _yscale, and subdivides the points into different Bins. Note that this does not check the dataset
     * index.  It is up to the caller to do that.
     *
     * Note that this method is not synchronized, so the caller should be.
     * Moreover this method should always be called from the event thread
     * when being used to write to the screen.
     */
    private void _drawBin(Graphics graphics, int dataset, int binIndex) {
        // BRDebug log.info("_drawBin");

        _setColorForDrawing(graphics, dataset, false);

        if (_lineStyles) {
            int lineStyle = dataset % _LINE_STYLES_ARRAY.length;
            setLineStyle(_LINE_STYLES_ARRAY[lineStyle], dataset);
        }

        assert dataset < _bins.size();
        ArrayList<Bin> bins = _bins.get(dataset);
        assert binIndex < bins.size();
        Bin bin = bins.get(binIndex);
        long xpos = bin.xpos;

        if (!bin.needReplot()) {
            return;
        }

        boolean connectedFlag = getConnected();

        int startPosition = bin.nextPointToPlot();
        int endPosition = bin.afterLastPointIndex();

        ArrayList<PlotPoint> points = _points.get(dataset);

        // Check to see whether the dataset has a marks directive
        int marks = _marks;

        // Draw decorations that may be specified on a per-dataset basis
        Format fmt = _formats.get(dataset);

        if (!fmt.marksUseDefault) {
            marks = fmt.marks;
        }

        if (_markDisconnections && marks == 0 && endPosition > startPosition
                && startPosition > 0) {
            PlotPoint previousPoint = points.get(startPosition - 1);
            if (!(connectedFlag && points.get(startPosition).connected)) {

                // This point is not connected with the previous one.
                // We want to put a dot each end of the at each segment.
                // If the previous one was connected no dot was drawn.
                // We will now add this extra dot for the previous point.

                if (connectedFlag && previousPoint.connected) {
                    if (_lastPointWithExtraDot.get(dataset) != previousPoint) {
                        long prevypos = _prevypos.get(dataset);
                        long prevxpos = _prevxpos.get(dataset);
                        // BRDebug log.info("Plotting point:" + prevxpos + ", " + prevypos +  ", position :" + (startPosition-1) + ", previous");
                        _drawPoint(graphics, dataset, prevxpos, prevypos, true,
                                2 /*dots*/);
                    } else {
                        // BRDebug log.info("Skipping point");

                        // We already painted this dot in the _drawplot code. No need
                        // to draw the same point again here.
                        // Now reset the flag:
                        _lastPointWithExtraDot.put(dataset, null);
                    }
                }
            } else {
                if (_lastPointWithExtraDot.get(dataset) == previousPoint) {
                    long prevypos = _prevypos.get(dataset);
                    long prevxpos = _prevxpos.get(dataset);
                    // BRDebug log.error.println("Erasing point:" + prevxpos + ", " + prevypos +  ", position :" + (startPosition-1) + ", previous");

                    // We keep track of the last dot that has been add to be able to
                    // remove the dot again in case an extra point was added afterwards.
                    // The erasing happens by drawing the point again with the same color (EXOR)

                    _setColorForDrawing(graphics, dataset, true);
                    _drawPoint(graphics, dataset, prevxpos, prevypos, true, 2 /*dots*/);
                    _resetColorForDrawing(graphics, true);
                    _setColorForDrawing(graphics, dataset, false);
                }
            }
        }

        if (connectedFlag && bin.needConnectionWithPreviousBin()) {
            Bin previousBin = bins.get(binIndex - 1);
            _drawLine(graphics, dataset, xpos, bin.firstYPos(),
                    previousBin.xpos, previousBin.lastYPos(), true,
                    _DEFAULT_WIDTH);
        }

        if (connectedFlag && bin.isConnected() && bin.rangeChanged()
                && bin.minYPos() != bin.maxYPos()) {
            _drawLine(graphics, dataset, xpos, bin.minYPos(), xpos, bin
                    .maxYPos(), true, _DEFAULT_WIDTH);
        }

        if ((fmt.impulsesUseDefault && _impulses)
                || (!fmt.impulsesUseDefault && fmt.impulses)) {
            long prevypos = _prevypos.get(dataset);
            long prevxpos = _prevxpos.get(dataset);

            for (int i = startPosition; i < endPosition; ++i) {
                PlotPoint point = points.get(i);
                long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                if (prevypos != ypos || prevxpos != xpos) {
                    _drawImpulse(graphics, xpos, ypos, true);
                    prevypos = ypos;
                    prevxpos = xpos;
                }
            }
        }

        {
            long prevypos = _prevypos.get(dataset);
            long prevxpos = _prevxpos.get(dataset);

            for (int i = startPosition; i < endPosition; ++i) {
                PlotPoint point = points.get(i);

                // I a point is not connected, we mark it with a dot.
                if (_marks != 0
                        || (_markDisconnections && !(connectedFlag && point.connected))) {
                    long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                    if (prevypos != ypos || prevxpos != xpos) {
                        int updatedMarks = marks;
                        if (!(connectedFlag && point.connected) && marks == 0) {
                            updatedMarks = 2; // marking style: dots
                        }
                        // BRDebug log.info("Plotting point:" + xpos + ", " + ypos +  ", position :" + (i) + ", current");
                        _drawPoint(graphics, dataset, xpos, ypos, true,
                                updatedMarks);
                        prevypos = ypos;
                        prevxpos = xpos;
                    }
                }

            }
        }
        if (_bars) {
            long prevypos = _prevypos.get(dataset);
            long prevxpos = _prevxpos.get(dataset);
            for (int i = startPosition; i < endPosition; ++i) {
                PlotPoint point = points.get(i);
                long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                if (prevypos != ypos || prevxpos != xpos) {
                    _drawBar(graphics, dataset, xpos, ypos, true);
                    prevypos = ypos;
                    prevxpos = xpos;
                }
            }
        }

        if (bin.errorBar()) {
            long prevypos = _prevypos.get(dataset);
            long prevxpos = _prevxpos.get(dataset);
            for (int i = startPosition; i < endPosition; ++i) {
                PlotPoint point = points.get(i);
                if (point.errorBar) {
                    long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                    if (prevypos != ypos || prevxpos != xpos) {
                        _drawErrorBar(
                                graphics,
                                dataset,
                                xpos,
                                _lry
                                        - (long) ((point.yLowEB - _yMin) * _yscale),
                                _lry
                                        - (long) ((point.yHighEB - _yMin) * _yscale),
                                true);
                        prevypos = ypos;
                        prevxpos = xpos;

                    }
                }
            }
        }

        _prevxpos.set(dataset, xpos);
        _prevypos.set(dataset, bin.lastYPos());

        bin.resetDisplayStateAfterPlot();

        _resetColorForDrawing(graphics, false);
    }

    /** Put a mark corresponding to the specified dataset at the
     *  specified x and y position. The mark is drawn in the current
     *  color. What kind of mark is drawn depends on the marks
     *  argument and the dataset argument. If the fourth argument is
     *  true, then check the range and plot only points that
     *  are in range.
     *  This method should be called only from the event dispatch thread.
     *  It is not synchronized, so its caller should be.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param xpos The x position.
     *  @param ypos The y position.
     *  @param clip If true, then do not draw outside the range.
     *  @param marks The marks that have to be used for plotting the point.
     */
    private void _drawPoint(Graphics graphics, int dataset, long xpos,
            long ypos, boolean clip, final int marks) {
        // BRDebug log.info("_drawPoint, " + xpos + ", " + ypos);

        // If the point is not out of range, draw it.
        boolean pointinside = (ypos <= _lry) && (ypos >= _uly)
                && (xpos <= _lrx) && (xpos >= _ulx);

        if (!clip || pointinside) {
            int xposi = (int) xpos;
            int yposi = (int) ypos;

            // If the point is out of range, and being drawn, then it is
            // probably a legend point.  When printing in black and white,
            // we want to use a line rather than a point for the legend.
            // (So that line patterns are visible). The only exception is
            // when the marks style uses distinct marks, or if there is
            // no line being drawn.
            // NOTE: It is unfortunate to have to test the class of graphics,
            // but there is no easy way around this that I can think of.
            if (!pointinside && (marks != 3) && _isConnected(dataset)
                    && ((graphics instanceof EPSGraphics) || !_usecolor)) {
                // Use our line styles.
                _drawLine(graphics, dataset, xposi - 6, yposi, xposi + 6,
                        yposi, false, _width);
            } else {
                // Color display.  Use normal legend.
                switch (marks) {
                case 0:

                    // If no mark style is given, draw a filled rectangle.
                    // This is used, for example, to draw the legend.
                    graphics.fillRect(xposi - 6, yposi - 6, 6, 6);
                    break;

                case 1:

                    // points -- use 3-pixel ovals.
                    graphics.fillOval(xposi - 1, yposi - 1, 3, 3);
                    break;

                case 2:

                    // dots
                    graphics.fillOval(xposi - _radius, yposi - _radius,
                            _diameter, _diameter);
                    break;

                case 3:

                    // various
                    int[] xpoints;

                    // various
                    int[] ypoints;

                    // Points are only distinguished up to _MAX_MARKS data sets.
                    int mark = dataset % _MAX_MARKS;

                    switch (mark) {
                    case 0:

                        // filled circle
                        graphics.fillOval(xposi - _radius, yposi - _radius,
                                _diameter, _diameter);
                        break;

                    case 1:

                        // cross
                        graphics.drawLine(xposi - _radius, yposi - _radius,
                                xposi + _radius, yposi + _radius);
                        graphics.drawLine(xposi + _radius, yposi - _radius,
                                xposi - _radius, yposi + _radius);
                        break;

                    case 2:

                        // square
                        graphics.drawRect(xposi - _radius, yposi - _radius,
                                _diameter, _diameter);
                        break;

                    case 3:

                        // filled triangle
                        xpoints = new int[4];
                        ypoints = new int[4];
                        xpoints[0] = xposi;
                        ypoints[0] = yposi - _radius;
                        xpoints[1] = xposi + _radius;
                        ypoints[1] = yposi + _radius;
                        xpoints[2] = xposi - _radius;
                        ypoints[2] = yposi + _radius;
                        xpoints[3] = xposi;
                        ypoints[3] = yposi - _radius;
                        graphics.fillPolygon(xpoints, ypoints, 4);
                        break;

                    case 4:

                        // diamond
                        xpoints = new int[5];
                        ypoints = new int[5];
                        xpoints[0] = xposi;
                        ypoints[0] = yposi - _radius;
                        xpoints[1] = xposi + _radius;
                        ypoints[1] = yposi;
                        xpoints[2] = xposi;
                        ypoints[2] = yposi + _radius;
                        xpoints[3] = xposi - _radius;
                        ypoints[3] = yposi;
                        xpoints[4] = xposi;
                        ypoints[4] = yposi - _radius;
                        graphics.drawPolygon(xpoints, ypoints, 5);
                        break;

                    case 5:

                        // circle
                        graphics.drawOval(xposi - _radius, yposi - _radius,
                                _diameter, _diameter);
                        break;

                    case 6:

                        // plus sign
                        graphics.drawLine(xposi, yposi - _radius, xposi, yposi
                                + _radius);
                        graphics.drawLine(xposi - _radius, yposi, xposi
                                + _radius, yposi);
                        break;

                    case 7:

                        // filled square
                        graphics.fillRect(xposi - _radius, yposi - _radius,
                                _diameter, _diameter);
                        break;

                    case 8:

                        // triangle
                        xpoints = new int[4];
                        ypoints = new int[4];
                        xpoints[0] = xposi;
                        ypoints[0] = yposi - _radius;
                        xpoints[1] = xposi + _radius;
                        ypoints[1] = yposi + _radius;
                        xpoints[2] = xposi - _radius;
                        ypoints[2] = yposi + _radius;
                        xpoints[3] = xposi;
                        ypoints[3] = yposi - _radius;
                        graphics.drawPolygon(xpoints, ypoints, 4);
                        break;

                    case 9:

                        // filled diamond
                        xpoints = new int[5];
                        ypoints = new int[5];
                        xpoints[0] = xposi;
                        ypoints[0] = yposi - _radius;
                        xpoints[1] = xposi + _radius;
                        ypoints[1] = yposi;
                        xpoints[2] = xposi;
                        ypoints[2] = yposi + _radius;
                        xpoints[3] = xposi - _radius;
                        ypoints[3] = yposi;
                        xpoints[4] = xposi;
                        ypoints[4] = yposi - _radius;
                        graphics.fillPolygon(xpoints, ypoints, 5);
                        break;
                    }

                    break;

                case 4:

                    // bigdots
                    //graphics.setColor(_marksColor);
                    if (graphics instanceof Graphics2D) {
                        Object obj = ((Graphics2D) graphics)
                                .getRenderingHint(RenderingHints.KEY_ANTIALIASING);
                        ((Graphics2D) graphics).setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        graphics.fillOval(xposi - 4, yposi - 4, 8, 8);
                        ((Graphics2D) graphics).setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING, obj);
                    } else {
                        graphics.fillOval(xposi - 4, yposi - 4, 8, 8);
                    }
                    break;

                case 5:

                    // If the mark style is pixels, draw a filled rectangle.
                    graphics.fillRect(xposi, yposi, 1, 1);
                    break;

                default:
                    // none
                }
            }
        }
    }

    /* Erase the points within the first bin in the given dataset.  If
     * lines are being drawn, also erase the line to the next points.
     *
     * This is not synchronized, so the caller should be.  Moreover, this
     * should only be called in the event dispatch thread. It should only
     * be called via deferIfNecessary().
     */
    private void _eraseFirstBin(int dataset) {
        // Ensure replot of offscreen buffer.
        _plotImage = null;

        _checkDatasetIndex(dataset);

        // Plot has probably been dismissed.  Return.
        Graphics graphics = getGraphics();

        ArrayList<PlotPoint> points = _points.get(dataset);
        ArrayList<Bin> bins = _bins.get(dataset);
        Bin bin = bins.get(0);
        int nbrOfBins = bins.size();
        assert nbrOfBins > 0;

        long xpos = bin.xpos;
        int startPosition = bin.firstPointIndex();
        assert startPosition >= 0;
        int endPosition = bin.afterLastPointIndex();
        assert endPosition > startPosition; //Otherwise there is nothing to remove

        // Need to check that graphics is not null because plot may have
        // been dismissed.
        if (_showing && (graphics != null)) {
            _setColorForDrawing(graphics, dataset, false);
            //First clear bin itself
            long minYPos = bin.minYPos();
            long maxYPos = bin.maxYPos();
            boolean connectedFlag = getConnected();

            if (connectedFlag && bin.isConnected() && minYPos != maxYPos) {
                _drawLine(graphics, dataset, xpos, minYPos, xpos, maxYPos,
                        true, _DEFAULT_WIDTH);
            }

            // Erase line to the next bin, if appropriate.
            if (nbrOfBins > 1) { //We have more than one bin
                Bin nextBin = bins.get(1);
                long nextx = nextBin.xpos;

                // NOTE: I have no idea why I have to give this point backwards.
                if (connectedFlag && nextBin.isConnectedWithPreviousBin()) {
                    _drawLine(graphics, dataset, nextx, nextBin.firstYPos(),
                            xpos, bin.lastYPos(), true, 2f);
                }
            }

            // Draw decorations that may be specified on a per-dataset basis
            Format fmt = _formats.get(dataset);

            if ((fmt.impulsesUseDefault && _impulses)
                    || (!fmt.impulsesUseDefault && fmt.impulses)) {
                long prevypos = _prevErasedypos.get(dataset);
                long prevxpos = _prevErasedxpos.get(dataset);
                for (int i = startPosition; i < endPosition; ++i) {
                    PlotPoint point = points.get(i);
                    long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                    if (prevypos != ypos || prevxpos != xpos) {
                        _drawImpulse(graphics, xpos, ypos, true);
                        prevypos = ypos;
                        prevxpos = xpos;
                    }
                }
            }

            // Check to see whether the dataset has a marks directive
            int marks = _marks;

            if (!fmt.marksUseDefault) {
                marks = fmt.marks;
            }
            {
                long prevypos = _prevErasedypos.get(dataset);
                long prevxpos = _prevErasedxpos.get(dataset);

                for (int i = startPosition; i < endPosition; ++i) {
                    PlotPoint point = points.get(i);
                    if (marks != 0 || !(connectedFlag && point.connected)) {
                        long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                        if (prevypos != ypos || prevxpos != xpos) {
                            int updatedMarks = marks;
                            if (!(connectedFlag && point.connected)
                                    && marks == 0) {
                                updatedMarks = 2; // marking style: dots
                            }
                            // BRDebug log.info("Erasing point:" + xpos + ", " + ypos +  ", position :" + (i) +", current");

                            _drawPoint(graphics, dataset, xpos, ypos, true,
                                    updatedMarks);
                            prevypos = ypos;
                            prevxpos = xpos;
                        }
                    }
                }
            }

            if (_markDisconnections && marks == 0
                    && endPosition > startPosition
                    && endPosition < points.size()) {
                PlotPoint point = points.get(endPosition - 1);
                if ((connectedFlag && point.connected)) {

                    // This point is not connected with the previous one.
                    // We want to put a dot each end of the at each segment.
                    // If the previous one was connected no dot was drawn.
                    // We will now add this extra dot for the previous point.
                    PlotPoint nextPoint = points.get(endPosition);
                    if (!(connectedFlag && nextPoint.connected)) {
                        long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                        // BRDebug log.info("Erasing point:" + xpos + ", " + ypos +  ", position :" + (endPosition-1) + ", previous");
                        _drawPoint(graphics, dataset, xpos, ypos, true, 2 /*dots*/);
                    }
                }
            }

            if (_bars) {
                long prevypos = _prevErasedypos.get(dataset);
                long prevxpos = _prevErasedxpos.get(dataset);

                for (int i = startPosition; i < endPosition; ++i) {
                    PlotPoint point = points.get(i);
                    long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                    if (prevypos != ypos || prevxpos != xpos) {
                        _drawBar(graphics, dataset, xpos, ypos, true);
                        prevypos = ypos;
                        prevxpos = xpos;
                    }
                }
            }

            if (bin.errorBar()) {
                long prevypos = _prevErasedypos.get(dataset);
                long prevxpos = _prevErasedxpos.get(dataset);

                for (int i = startPosition; i < endPosition; ++i) {
                    PlotPoint point = points.get(i);
                    if (point.errorBar) {
                        long ypos = _lry - (long) ((point.y - _yMin) * _yscale);
                        if (prevypos != ypos || prevxpos != xpos) {
                            _drawErrorBar(
                                    graphics,
                                    dataset,
                                    xpos,
                                    _lry
                                            - (long) ((point.yLowEB - _yMin) * _yscale),
                                    _lry
                                            - (long) ((point.yHighEB - _yMin) * _yscale),
                                    true);
                            prevypos = ypos;
                            prevxpos = xpos;
                        }
                    }
                }
            }

            _resetColorForDrawing(graphics, false);
        }

        // The following is executed whether the plot is showing or not.
        // Remove the bin from the model.

        if (nbrOfBins > 1) {
            Bin nextBin = bins.get(1);
            nextBin.setNotConnectedWithPreviousBin();
        }

        if (nbrOfBins == 1) {
            //No bins left in the end
            _prevxpos.set(dataset, _INITIAL_PREVIOUS_VALUE);
            _prevypos.set(dataset, _INITIAL_PREVIOUS_VALUE);
        }

        // If a point is at the maximum or minimum x or y boundary,
        // then flag that boundary needs to be recalculated next time
        // fillPlot() is called.
        if (xpos == _xBottom || xpos == _xTop || bin.minYPos() == _yBottom
                || bin.maxYPos() == _yTop) {
            _xyInvalid = true;
        }

        //Delete points and bin
        assert startPosition == 0; //No actually necessary in this code, but it should be valid
        for (int i = startPosition; i < endPosition; ++i) {
            points.remove(startPosition);
        }
        assert bin.firstPointIndex() >= 0;

        _pointInBinOffset.set(dataset, _pointInBinOffset.get(dataset)
                + bin.afterLastPointIndex() - bin.firstPointIndex());
        //FIXME? Warning: Could overflow for scopes with a really large history...
        //  (the points aren't kept in memory, since it is only here where it goes wrong.
        //  We could check for overflow and in this case reset the _pointInBinOffset to
        //  zero, recalculate all bins, and repaint everything.

        //This code is actually only checking some invariants. Not revelant in
        //      production code
        if (nbrOfBins > 1) {
            Bin nextBin = bins.get(1);
            assert nextBin.firstPointIndex() >= 0; //otherwise out of box
            assert nextBin.firstPointIndex() == 0;
            //This is a combination of two things: first of all we are deleting the first bin and secondly all points should be in a bin
            //      => the first point in the first bin (once this one has deleted) has to be the first point of all points

        }

        _prevErasedxpos.set(dataset, xpos);
        _prevErasedypos.set(dataset, bin.lastYPos());

        bins.remove(0);
    }

    /* Erase the point at the given index in the given dataset.  If
     * lines are being drawn, these lines are erased and if necessary new
     * ones will be drawn.
     *
     * This is not synchronized, so the caller should be.  Moreover, this
     * should only be called in the event dispatch thread. It should only
     * be called via deferIfNecessary().
     */
    private void _erasePoint(int dataset, int index) {
        _points.get(dataset).remove(index);
        repaint();
    }

    /* Rescale so that the data that is currently plotted just fits.
     * This overrides the base class method to ensure that the protected
     * variables _xBottom, _xTop, _yBottom, and _yTop are valid.
     * This method calls repaint(), which causes the display
     * to be updated.
     *
     * This is not synchronized, so the caller should be.  Moreover, this
     * should only be called in the event dispatch thread. It should only
     * be called via deferIfNecessary().
     */
    private void _fillPlot() {
        if (_xyInvalid) {
            // Recalculate the boundaries based on currently visible data
            _xBottom = Double.MAX_VALUE;
            _xTop = -Double.MAX_VALUE;
            _yBottom = Double.MAX_VALUE;
            _yTop = -Double.MAX_VALUE;

            for (int dataset = 0; dataset < _points.size(); dataset++) {
                ArrayList<PlotPoint> points = _points.get(dataset);

                for (int index = 0; index < points.size(); index++) {
                    PlotPoint pt = points.get(index);

                    if (pt.x < _xBottom) {
                        _xBottom = pt.x;
                    }

                    if (pt.x > _xTop) {
                        _xTop = pt.x;
                    }

                    if (pt.y < _yBottom) {
                        _yBottom = pt.y;
                    }

                    if (pt.y > _yTop) {
                        _yTop = pt.y;
                    }
                }
            }
        }

        _xyInvalid = false;

        // If this is a bar graph, then make sure the Y range includes 0
        if (_bars) {
            if (_yBottom > 0.0) {
                _yBottom = 0.0;
            }

            if (_yTop < 0.0) {
                _yTop = 0.0;
            }
        }

        super.fillPlot();
    }

    // Return true if the specified dataset is connected by default.
    private boolean _isConnected(int dataset) {
        if (dataset < 0) {
            return _connected;
        }

        _checkDatasetIndex(dataset);

        Format fmt = _formats.get(dataset);

        if (fmt.connectedUseDefault) {
            return _connected;
        } else {
            return fmt.connected;
        }
    }

    /** Reset the color for drawing. This typically needs to happen after having drawn
     *  a bin or erasing one.
     *  @param graphics The graphics context.
     *  @param forceExorWithBackground Restore the paint made back from exor mode
     */
    private void _resetColorForDrawing(Graphics graphics,
            boolean forceExorWithBackground) {
        // Restore the color, in case the box gets redrawn.
        graphics.setColor(_foreground);

        if ((_pointsPersistence > 0) || (_xPersistence > 0.0)
                || forceExorWithBackground) {
            // Restore paint mode in case axes get redrawn.
            graphics.setPaintMode();
        }
    }

    /** Schedule a bin to be (re)drawn due to the addition of bin.
     */
    private void _scheduleBinRedrawAdd(int dataset, boolean binAdded) {
        while (_scheduledBinsToAdd.size() <= dataset) {
            _scheduledBinsToAdd.add(0);
        }
        int previousCount = _scheduledBinsToAdd.get(dataset);
        if (binAdded || previousCount == 0) {
            _scheduledBinsToAdd.set(dataset, previousCount + 1);
            _needBinRedraw = true;
        }
    }

    /** Schedule a bin to be (re)drawn due to the removal of a bin.
     */
    private void _scheduleBinRedrawRemove(int dataset, int nbrOfElementsToErase) {
        while (_scheduledBinsToErase.size() <= dataset) {
            _scheduledBinsToErase.add(0);
        }
        _scheduledBinsToErase.set(dataset, Math.max(nbrOfElementsToErase,
                _scheduledBinsToErase.get(dataset)));
        _needBinRedraw = true;
    }

    /** Set the color for drawing. This typically needs to happen before drawing
     *  a bin or erasing one.
     *  @param graphics The graphics context.
     *  @param dataset The index of the dataset.
     *  @param forceExorWithBackground Force to go into exor mode.
     */
    private void _setColorForDrawing(Graphics graphics, int dataset,
            boolean forceExorWithBackground) {
        if ((_pointsPersistence > 0) || (_xPersistence > 0.0)
                || forceExorWithBackground) {
            // To allow erasing to work by just redrawing the points.
            if (_background == null) {
                // java.awt.Component.setBackground(color) says that
                // if the color "parameter is null then this component
                // will inherit the  background color of its parent."
                graphics.setXORMode(getBackground());
            } else {
                graphics.setXORMode(_background);
            }
        }

        // Set the color
        if (_usecolor) {
            int color = dataset % _colors.length;
            graphics.setColor(_colors[color]);
        } else {
            graphics.setColor(_foreground);
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    /** True when disconnections should be marked.*/
    private boolean _markDisconnections = false;

    /** @serial Offset per dataset in x axis units. */
    private volatile double _barOffset = 0.05;

    /** @serial True if this is a bar plot. */
    private boolean _bars = false;

    /** @serial Width of a bar in x axis units. */
    private volatile double _barWidth = 0.5;

    /**
     * An arraylist of the bins in the plot. A bin is represents a number of points that are all displayed on the same x position.
     * A bin is meant to avoid superfluous drawings of lines. So instaed of having to draw
     * a line between each point, you can draw a line between the minimum and maximum and maximum
     * position.
     */
    private ArrayList<ArrayList<Bin>> _bins = new ArrayList<ArrayList<Bin>>();

    /** @serial True if the points are connected. */
    private boolean _connected = true;

    /** @serial Give the diameter of a point for efficiency. */
    private int _diameter = 6;

    /** The initial default width.
     */
    private static final float _DEFAULT_WIDTH = 2f;

    // Half of the length of the error bar horizontal leg length;
    private static final int _ERRORBAR_LEG_LENGTH = 5;

    /** @serial Is this the first datapoint in a set? */
    private boolean _firstInSet = true;

    /** @serial Format information on a per data set basis. */
    private ArrayList<Format> _formats = new ArrayList<Format>();

    /** Cached copy of graphics, needed to reset when we are exporting
     *  to EPS.
     */
    private Graphics _graphics = null;

    /** @serial True if this is an impulse plot. */
    private boolean _impulses = false;

    /** Initial value for elements in _prevx and _prevy that indicate
     *  we have not yet seen data.
     */
    private static final Long _INITIAL_PREVIOUS_VALUE = Long.MIN_VALUE;

    // We keep track of the last dot that has been add to be able to
    // remove the dot again in case an extra point was added afterwards.
    private HashMap<Integer, PlotPoint> _lastPointWithExtraDot = new HashMap<Integer, PlotPoint>();

    // A stroke of width 1.
    private static final BasicStroke _LINE_STROKE1 = new BasicStroke(1f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    // A stroke of width 2.
    private static final BasicStroke _LINE_STROKE2 = new BasicStroke(2f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);

    /** True if different line styles should be used. */
    private boolean _lineStyles = false;

    /** True if different line styles should be used. */
    private static String[] _LINE_STYLES_ARRAY = { "solid", "dotted", "dashed",
            "dotdashed", "dotdotdashed" };

    /** @serial The highest data set used. */
    private int _maxDataset = -1;

    // Maximum number of different marks
    // NOTE: There are 11 colors in the base class.  Combined with 10
    // marks, that makes 110 unique signal identities.
    private static final int _MAX_MARKS = 10;

    // True when a bin has been changed and it need to be repainted
    // by the next scheduled repaint.
    private boolean _needBinRedraw = false;

    // True when a the plot need to be refilled
    // by the next scheduled repaint.
    private boolean _needPlotRefill = false;

    /**
     * Points in bins have an absolute index within the virtual array
     *  of all points that once existed in Plot. This is done to avoid
     *  having to change all bins when points in the beginning are removed.
     *  The offset _pointInBinOffset is a positive number that denotes the
     *  difference between the index of the point in the current point arraylist
     *  and the virtual one containing all points.
     */
    private ArrayList<Integer> _pointInBinOffset = new ArrayList<Integer>();

    /** @serial Number of points to persist for. */
    private int _pointsPersistence = 0;

    /** @serial Information about the previously plotted point. */
    private ArrayList<Long> _prevxpos = new ArrayList<Long>();

    /** @serial Information about the previously plotted point. */
    private ArrayList<Long> _prevypos = new ArrayList<Long>();

    /** @serial Information about the previously erased point. */
    private ArrayList<Long> _prevErasedxpos = new ArrayList<Long>();

    /** @serial Information about the previously erased point. */
    private ArrayList<Long> _prevErasedypos = new ArrayList<Long>();

    /** @serial Give the radius of a point for efficiency. */
    private int _radius = 3;

    /** @serial True if we saw 'reusedatasets: on' in the file. */
    private boolean _reuseDatasets = false;

    /** @serial Have we seen a DataSet line in the current data file? */
    private boolean _sawFirstDataSet = false;

    // _scheduledBinsToAdd a a list a bins that should be added by the scheduled
    // repaint.
    private ArrayList<Integer> _scheduledBinsToAdd = new ArrayList<Integer>();

    // _scheduledBinsToAdd a a list a bins that should be erased by the scheduled
    // repaint.
    private ArrayList<Integer> _scheduledBinsToErase = new ArrayList<Integer>();

    /** @serial Set by _drawPlot(), and reset by clear(). */
    private boolean _showing = false;

    /** @serial Persistence in units of the horizontal axis. */
    private double _xPersistence = 0.0;

    /** @serial Flag indicating validity of _xBottom, _xTop,
     *  _yBottom, and _yTop.
     */
    private boolean _xyInvalid = true;

    /** The width of the current stroke.  Only effective if the
     *  Graphics is a Graphics2D.
     *
     */
    private float _width = _DEFAULT_WIDTH;

    ///////////////////////////////////////////////////////////////////
    ////                         inner classes                     ////

    /**
     * A bin is represents a number of points that are all displayed on the same x position.
     * A bin is meant to avoid superfluous drawings of lines. So instaed of having to draw
     * a line between each point, you can draw a line between the minimum and maximum and maximum
     * y position.
    */
    private class Bin {
        public Bin(long xPos, int dataset) {
            _dataset = dataset;
            xpos = xPos;
        }

        /**
         * Add a point to the bin, with a certain pointIndex (the index in the array of all points in a Plot),
         *      and a certain ypos.
         * Precondition: The xpos of the point should be same as other points already within the bin
         */
        public void addPoint(PlotPoint point, int pointIndex, long ypos) {
            int absolutePointIndex = pointIndex
                    + _pointInBinOffset.get(_dataset);
            //The absolute point index is a index in the list of
            //  all points that once existed in the plot

            if (_maxYPos < ypos) {
                _maxYPos = ypos;
                _rangeChanged = true;
            }
            if (_minYPos > ypos) {
                _minYPos = ypos;
                _rangeChanged = true;
            }

            if (_firstPointIndex == -1) {
                _needConnectionWithPreviousBin = point.connected;
                _firstYPos = ypos;
                _firstPointIndex = absolutePointIndex;
                _nextPointToPlot = _firstPointIndex;
            } else {
                _isConnected |= point.connected;
                // if one point is connected within the bin, all points will be (it is difficult to do this otherwise)

                assert _afterLastPointIndex == absolutePointIndex; //Bin intervals should be contiguous intervals
            }

            _afterLastPointIndex = absolutePointIndex + 1;
            _lastYPos = ypos;

            _errorBar |= point.errorBar;
        }

        /**
         * Return the position after the last point of the range of points within the bin
         * This index is the index within the current points of the plot.
         */
        public int afterLastPointIndex() {
            assert _firstPointIndex != -1;
            return _afterLastPointIndex - _pointInBinOffset.get(_dataset);
        }

        /**
         *  Return true in case there is one point that needs an error bar, otherwise false
         */
        public boolean errorBar() {
            return _errorBar;
        }

        /**
         * Return the position of the first point of the range of points within the bin
         * This index is the index within the current points of the plot.
         */
        public int firstPointIndex() {
            assert _firstPointIndex != -1;
            return _firstPointIndex - _pointInBinOffset.get(_dataset);
        }

        /**
         *  Return the y position of the first added point in the bin.
         */
        // Precondition: only valid in case there is at least one point
        public long firstYPos() {
            assert _firstPointIndex != -1;
            return _firstYPos;
        }

        /**
         *  Return the minimum y position of the bin.
         * Precondition: only valid in case there is at least one point
         */
        public long minYPos() {
            assert _firstPointIndex != -1;
            return _minYPos;
        }

        /**
         * Return the y position of the last added point in the bin.
         * Precondition: only valid in case there is at least one point
         */
        public long lastYPos() {
            assert _firstPointIndex != -1;
            return _lastYPos;
        }

        /**
         * Return the maximum y position of the bin.
         * Precondition: only valid in case there is at least one point
         */
        public long maxYPos() {
            assert _firstPointIndex != -1;
            return _maxYPos;
        }

        /**
         * Return whether a line should be drawn with the previous bin.
         * After you have reset the display state, the boolean return false
         */
        public boolean needConnectionWithPreviousBin() {
            return _needConnectionWithPreviousBin;
        }

        /**
         * Return true a line has been drawn to the previous bin.
         */
        public boolean isConnectedWithPreviousBin() {
            return _isConnectedWithPreviousBin;
        }

        public boolean isConnected() {
            return _isConnected;
        }

        /**
         * Return true when the bin should be plotted (again)
         */
        public boolean needReplot() {
            return _needConnectionWithPreviousBin || _rangeChanged
                    || _nextPointToPlot != _afterLastPointIndex;
        }

        /**
         * Return the position of the next point of the bin that should be plotted
         * This index is the index within the current points of the plot.
         */
        public int nextPointToPlot() {
            return _nextPointToPlot - _pointInBinOffset.get(_dataset);
        }

        /**
         * Return true when the rangeChanged
         */
        public boolean rangeChanged() {
            return _rangeChanged;
        }

        /**
         * Reset the plot state for this bin when you have
         * plotted this bin/
         */
        public void resetDisplayStateAfterPlot() {
            if (_needConnectionWithPreviousBin) {
                _isConnectedWithPreviousBin = true;
                _needConnectionWithPreviousBin = false;
            }
            _rangeChanged = false;
            _nextPointToPlot = _afterLastPointIndex;
        }

        /**
         * Disconnect this bin with the previous bin.
         */
        public void setNotConnectedWithPreviousBin() {
            _needConnectionWithPreviousBin = false;
            _isConnectedWithPreviousBin = false;
            _points.get(_dataset).get(
                    _firstPointIndex - _pointInBinOffset.get(_dataset)).connected = false;
        }

        public final long xpos;

        private int _afterLastPointIndex = 0;

        private int _dataset = 0;

        // _errorBar is true in case there is one point that needs an error bar, otherwise false
        private boolean _errorBar = false;

        private int _firstPointIndex = -1;

        private long _firstYPos = java.lang.Long.MIN_VALUE;

        private boolean _isConnected = false;
        private boolean _isConnectedWithPreviousBin = false;

        private long _lastYPos = java.lang.Long.MIN_VALUE;

        private long _maxYPos = java.lang.Long.MIN_VALUE;
        private long _minYPos = java.lang.Long.MAX_VALUE;

        // Indicate whether a line has to be added with previous bin or not
        // Once the line has been drawn, the boolean should be switched to false
        private boolean _needConnectionWithPreviousBin = false;

        private boolean _rangeChanged = false;
        private int _nextPointToPlot = 0;
    }

    private static class Format implements Serializable {
        // FindBugs suggests making this class static so as to decrease
        // the size of instances and avoid dangling references.

        private static final long serialVersionUID = 1L;

        // Indicate whether the current dataset is connected.
        public boolean connected;

        // Indicate whether the above variable should be ignored.
        public boolean connectedUseDefault = true;

        // Indicate whether a stem plot should be drawn for this data set.
        // This is ignored unless the following variable is set to false.
        public boolean impulses;

        // Indicate whether the above variable should be ignored.
        public boolean impulsesUseDefault = true;

        // The stroke used for lines
        // This is ignored unless strokeUseDefault is true
        public BasicStroke lineStroke;

        // The name of the stroke, see Plot.setLineStyle()
        // This is ignored unless strokeUseDefault is true
        public String lineStyle;

        // Indicate whether lineStroke should be used
        public boolean lineStyleUseDefault = true;

        // Indicate what type of mark to use.
        // This is ignored unless the following variable is set to false.
        public int marks;

        // Indicate whether the above variable should be ignored.
        public boolean marksUseDefault = true;
    }
}
