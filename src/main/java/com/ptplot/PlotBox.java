/* A labeled box for signal plots.

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

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.Timer;


// TO DO:
//   - Augment getColorByName to support a full complement of colors
//     (get the color list from Tycho).
///////////////////////////////////////////////////////////////////
//// PlotBox

/**
 A labeled box within which to place a data plot.
 <p>A title, X and Y axis labels, tick marks, and a legend are all
 supported.  Zooming in and out is supported.  To zoom in, click and
 hold mouse button 1 and drag the mouse downwards to draw a box.  To
 zoom out, click and hold mouse button1 and drag the mouse upward.
 <p>
 The box can be configured either through a file with commands or
 through direct invocation of the public methods of the class.
 <p>
 When calling the methods, in most cases the changes will not
 be visible until paintComponent() has been called.  To request that this
 be done, call repaint().
 <p>
 A small set of key bindings are provided for convenience.
 They are:
 <ul>
 <li> Cntrl-c: Export the plot to the clipboard (in PlotML).
 <li> D: Dump the plot to standard output (in PlotML).
 <li> E: Export the plot to standard output in EPS format.
 <li> F: Fill the plot.
 <li> H or ?: Display a simple help message.
 <li> Cntrl-D or Q: quit
 </ul>
 These commands are provided in a menu by the PlotFrame class.
 Note that exporting to the clipboard is not allowed in applets
 (it used to be), so this will result in an error message.
 <p>
 At this time, the two export commands produce encapsulated postscript
 tuned for black-and-white printers.  In the future, more formats may
 supported.
 Exporting to the clipboard and to standard output, in theory,
 is allowed for applets, unlike writing to a file. Thus, these
 key bindings provide a simple mechanism to obtain a high-resolution
 image of the plot from an applet, suitable for incorporation in
 a document. However, in some browsers, exporting to standard out
 triggers a security violation.  You can use the JDK appletviewer instead.
 <p>
 To read commands from a file or URL, the preferred technique is
 to use one of the classes in the plotml package.  That package
 supports both PlotML, an XML extension for plots, and a historical
 file format specific to ptplot.  The historical file format is
 understood by the read() method in this class.
 The syntax of the historical format, documented below, is rudimentary,
 and will probably not be extended as ptplot evolves.  Nonetheless,
 we document it here since it is directly supported by this class.
 <p>
 The historical format for the file allows any number
 commands, one per line.  Unrecognized commands and commands with
 syntax errors are ignored.  Comments are denoted by a line starting
 with a pound sign "#".  The recognized commands include:
 <pre>
 TitleText: <i>string</i>
 XLabel: <i>string</i>
 YLabel: <i>string</i>
 </pre>
 These commands provide a title and labels for the X (horizontal) and Y
 (vertical) axes.
 A <i>string</i> is simply a sequence of characters, possibly
 including spaces.  There is no need here to surround them with
 quotation marks, and in fact, if you do, the quotation marks will
 be included in the labels.
 <p>
 The ranges of the X and Y axes can be optionally given by commands like:
 <pre>
 XRange: <i>min</i>, <i>max</i>
 YRange: <i>min</i>, <i>max</i>
 </pre>
 The arguments <i>min</i> and <i>max</i> are numbers, possibly
 including a sign and a decimal point. If they are not specified,
 then the ranges are computed automatically from the data and padded
 slightly so that datapoints are not plotted on the axes.
 <p>
 The tick marks for the axes are usually computed automatically from
 the ranges.  Every attempt is made to choose reasonable positions
 for the tick marks regardless of the data ranges (powers of
 ten multiplied by 1, 2, or 5 are used).  However, they can also be
 specified explicitly using commands like:
 <pre>
 XTicks: <i>label position, label position, ...</i>
 YTicks: <i>label position, label position, ...</i>
 </pre>
 A <i>label</i> is a string that must be surrounded by quotation
 marks if it contains any spaces.  A <i>position</i> is a number
 giving the location of the tick mark along the axis.  For example,
 a horizontal axis for a frequency domain plot might have tick marks
 as follows:
 <pre>
 XTicks: -PI -3.14159, -PI/2 -1.570795, 0 0, PI/2 1.570795, PI 3.14159
 </pre>
 Tick marks could also denote years, months, days of the week, etc.
 <p>
 The X and Y axes can use a logarithmic scale with the following commands:
 <pre>
 XLog: on
 YLog: on
 </pre>
 The grid labels represent powers of 10.  Note that if a logarithmic
 scale is used, then the values must be positive.  Non-positive values
 will be silently dropped.  Note further that when using logarithmic
 axes that the log of input data is taken as the data is added to the plot.
 This means that <pre>XLog: on</pre> or <pre>YLog: on</pre> should
 appear before any data.  Also, the value of the XTicks, YTicks,
 XRange or YRange directives should be in log units.
 So, <pre>XTicks: 1K 3</pre> will display the string <pre>1K</pre>
 at the 1000 mark.
 <p>
 By default, tick marks are connected by a light grey background grid.
 This grid can be turned off with the following command:
 <pre>
 Grid: off
 </pre>
 It can be turned back on with
 <pre>
 Grid: on
 </pre>
 Also, by default, the first ten data sets are shown each in a unique color.
 The use of color can be turned off with the command:
 <pre>
 Color: off
 </pre>
 It can be turned back on with
 <pre>
 Color: on
 </pre>
 Finally, the rather specialized command
 <pre>
 Wrap: on
 </pre>
 enables wrapping of the X (horizontal) axis, which means that if
 a point is added with X out of range, its X value will be modified
 modulo the range so that it lies in range. This command only has an
 effect if the X range has been set explicitly. It is designed specifically
 to support oscilloscope-like behavior, where the X value of points is
 increasing, but the display wraps it around to left. A point that lands
 on the right edge of the X range is repeated on the left edge to give
 a better sense of continuity. The feature works best when points do land
 precisely on the edge, and are plotted from left to right, increasing
 in X.
 <p>
 All of the above commands can also be invoked directly by calling the
 the corresponding public methods from some Java procedure.
 <p>
 This class uses features of JDK 1.2, and hence if used in an applet,
 it can only be viewed by a browser that supports JDK 1.2, or a plugin.

 @author Edward A. Lee, Christopher Brooks, Contributors: Jun Wu (jwu@inin.com.au), William Wu, Robert Kroeger, Tom Peachey, Bert Rodiers

 @version $Id: PlotBox.java 59396 2010-10-06 01:29:54Z cxh $
 @since Ptolemy II 0.2
 @Pt.ProposedRating Yellow (cxh)
 @Pt.AcceptedRating Yellow (cxh)
 */
@SuppressWarnings("PMD")
public class PlotBox extends JPanel implements Printable {
    private static final long serialVersionUID = 1L;

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());
    ///////////////////////////////////////////////////////////////////
    ////                         constructor                       ////

    /** Construct a plot box with a default configuration. */
    public PlotBox() {
        // If we make this transparent, the background shows through.
        // However, we assume that the user will set the background.
        // NOTE: A component is transparent by default (?).
        // setOpaque(false);
        setOpaque(true);

        // Create a right-justified layout with spacing of 2 pixels.
        setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
        addMouseListener(new ZoomListener());
        addKeyListener(new CommandListener());
        addMouseMotionListener(new DragListener());

        // This is something we want to do only once...
        _measureFonts();

        // Request the focus so that key events are heard.
        // NOTE: no longer needed?
        // requestFocus();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Add a line to the caption (displayed at below graph) .
      * @param captionLine The string to be added.
      * @see #getCaptions()
      */
    public synchronized void addCaptionLine(String captionLine) {
        // Caption code contributed by Tom Peachey.
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;
        _captionStrings.addElement(captionLine);
    }

    /** Add a legend (displayed at the upper right) for the specified
     *  data set with the specified string.  Short strings generally
     *  fit better than long strings.  If the string is empty, or the
     *  argument is null, then no legend is added.
     *  @param dataset The dataset index.
     *  @param legend The label for the dataset.
     *  @see #renameLegend(int, String)
     */
    public synchronized void addLegend(int dataset, String legend) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        if ((legend == null) || legend.equals("")) {
            return;
        }

        _legendStrings.addElement(legend);
        _legendDatasets.addElement(Integer.valueOf(dataset));
    }

    /** Specify a tick mark for the X axis.  The label given is placed
     *  on the axis at the position given by <i>position</i>. If this
     *  is called once or more, automatic generation of tick marks is
     *  disabled.  The tick mark will appear only if it is within the X
     *  range.
     *  <p>Note that if {@link #setXLog(boolean)} has been called, then
     *  the position value should be in log units.
     *  So, addXTick("1K", 3) will display the string <pre>1K</pre>
     *  at the 1000 mark.
     *  @param label The label for the tick mark.
     *  @param position The position on the X axis.
     */
    public synchronized void addXTick(String label, double position) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        if (_xticks == null) {
            _xticks = new Vector<Double>();
            _xticklabels = new Vector<String>();
        }

        _xticks.addElement(Double.valueOf(position));
        _xticklabels.addElement(label);
    }

    /** Specify a tick mark for the Y axis.  The label given is placed
     *  on the axis at the position given by <i>position</i>. If this
     *  is called once or more, automatic generation of tick marks is
     *  disabled.  The tick mark will appear only if it is within the Y
     *  range.
     *  <p>Note that if {@link #setYLog(boolean)} has been called, then
     *  the position value should be in log units.
     *  So, addYTick("1K", 3) will display the string <pre>1K</pre>
     *  at the 1000 mark.
     *  @param label The label for the tick mark.
     *  @param position The position on the Y axis.
     */
    public synchronized void addYTick(String label, double position) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        if (_yticks == null) {
            _yticks = new Vector<Double>();
            _yticklabels = new Vector<String>();
        }

        _yticks.addElement(Double.valueOf(position));
        _yticklabels.addElement(label);
    }

    /** If the argument is true, clear the axes.  I.e., set all parameters
     *  controlling the axes to their initial conditions.
     *  For the change to take effect, call repaint().  If the argument
     *  is false, do nothing.
     *  @param axes If true, clear the axes parameters.
     */
    public synchronized void clear(boolean axes) {
        // We need to repaint the offscreen buffer.
        _plotImage = null;

        _xBottom = Double.MAX_VALUE;
        _xTop = -Double.MAX_VALUE;
        _yBottom = Double.MAX_VALUE;
        _yTop = -Double.MAX_VALUE;

        if (axes) {
            // Protected members first.
            _yMax = 0;
            _yMin = 0;
            _xMax = 0;
            _xMin = 0;
            _xRangeGiven = false;
            _yRangeGiven = false;
            _originalXRangeGiven = false;
            _originalYRangeGiven = false;
            _rangesGivenByZooming = false;
            _xlog = false;
            _ylog = false;
            _grid = true;
            _wrap = false;
            _usecolor = true;

            // Private members next...
            _filespec = null;
            _xlabel = null;
            _ylabel = null;
            _title = null;
            _legendStrings = new Vector<String>();
            _legendDatasets = new Vector<Integer>();
            _xticks = null;
            _xticklabels = null;
            _yticks = null;
            _yticklabels = null;
        }
    }

    /** Clear all the captions.
     *  For the change to take effect, call repaint().
     *  @see #setCaptions(Vector)
     */
    public synchronized void clearCaptions() {
        // Changing caption means we need to repaint the offscreen buffer.
        _plotImage = null;
        _captionStrings = new Vector<String>();
    }

    /** Clear all legends.  This will show up on the next redraw.
     */
    public synchronized void clearLegends() {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _legendStrings = new Vector<String>();
        _legendDatasets = new Vector<Integer>();
    }

    /** If this method is called in the event thread, then simply
     * execute the specified action.  Otherwise,
     * if there are already deferred actions, then add the specified
     * one to the list.  Otherwise, create a list of deferred actions,
     * if necessary, and request that the list be processed in the
     * event dispatch thread.
     *
     * Note that it does not work nearly as well to simply schedule
     * the action yourself on the event thread because if there are a
     * large number of actions, then the event thread will not be able
     * to keep up.  By grouping these actions, we avoid this problem.
     *
     * This method is not synchronized, so the caller should be.
     * @param action The Runnable object to execute.
     */
    public void deferIfNecessary(Runnable action) {
        // In swing, updates to showing graphics must be done in the
        // event thread.  If we are in the event thread, then proceed.
        // Otherwise, queue a request or add to a pending request.
        if (EventQueue.isDispatchThread()) {
            action.run();
        } else {

            // Add the specified action to the list of actions to perform.
            _deferredActions.add(action);

            // If it hasn't already been requested, request that actions
            // be performed in the event dispatch thread.
            if (!_actionsDeferred) {
                Runnable doActions = new Runnable() {
                    @Override
                    public void run() {
                        _executeDeferredActions();
                    }
                };

                try {
                    _actionsDeferred = true;

                    // NOTE: Using invokeAndWait() here risks causing
                    // deadlock.  Don't do it!
                    SwingUtilities.invokeLater(doActions);
                } catch (Exception ex) {
                    log.warn("ignoring", ex);
                    // Ignore InterruptedException.
                    // Other exceptions should not occur.
                }

            }
        }
    }

    /** Destroy the plotter.  This method is usually
     *  called by PlotApplet.destroy().  It does
     *  various cleanups to reduce memory usage.
     */
    public void destroy() {
        clear(true);
        // Avoid leaking _timerTask;
        setAutomaticRescale(false);
        setTimedRepaint(false);

        // Remove the buttons
        if (_printButton != null) {
            ActionListener[] listeners = _printButton.getActionListeners();
            for (int i = 0; i < listeners.length; i++) {
                _printButton.removeActionListener(listeners[i]);
            }
            _printButton = null;
        }
        if (_resetButton != null) {
            ActionListener[] listeners = _resetButton.getActionListeners();
            for (int i = 0; i < listeners.length; i++) {
                _resetButton.removeActionListener(listeners[i]);
            }
            _resetButton = null;
        }
        if (_formatButton != null) {
            ActionListener[] listeners = _formatButton.getActionListeners();
            for (int i = 0; i < listeners.length; i++) {
                _formatButton.removeActionListener(listeners[i]);
            }
            _formatButton = null;
        }
        if (_fillButton != null) {
            ActionListener[] listeners = _fillButton.getActionListeners();
            for (int i = 0; i < listeners.length; i++) {
                _fillButton.removeActionListener(listeners[i]);
            }
            _fillButton = null;
        }

        removeAll();
    }

    /** Export a description of the plot.
     *  Currently, only EPS is supported.  But in the future, this
     *  may cause a dialog box to open to allow the user to select
     *  a format.  If the argument is null, then the description goes
     *  to the clipboard.  Otherwise, it goes to the specified file.
     *  To send it to standard output, use
     *  <code>System.out</code> as an argument.
     *  @param out An output stream to which to send the description.
     */
    public synchronized void export(OutputStream out) {
        try {
            EPSGraphics g = new EPSGraphics(out, _width, _height);
            _drawPlot(g, false);
            g.showpage();
        } catch (RuntimeException ex) {
            String message = "Export failed: " + ex.getMessage();
            JOptionPane.showMessageDialog(this, message,
                    "Ptolemy Plot Message", JOptionPane.ERROR_MESSAGE);

            // Rethrow the exception so that we don't report success,
            // and so the stack trace is displayed on standard out.
            throw (RuntimeException) ex.fillInStackTrace();
        }
    }

    // CONTRIBUTED CODE.
    // I wanted the ability to use the Plot object in a servlet and to
    // write out the resultant images. The following routines,
    // particularly exportImage(), permit this. I also had to make some
    // minor changes else where. Rob Kroeger, May 2001.
    // NOTE: This code has been modified by EAL to conform with Ptolemy II
    // coding style.

    /** Create a BufferedImage and draw this plot to it.
     *  The size of the returned image matches the current size of the plot.
     *  This method can be used, for
     *  example, by a servlet to produce an image, rather than
     *  requiring an applet to instantiate a PlotBox.
     *  @return An image filled by the plot.
     */
    public synchronized BufferedImage exportImage() {
        Rectangle rectangle = new Rectangle(_preferredWidth, _preferredHeight);
        return exportImage(new BufferedImage(rectangle.width, rectangle.height,
                BufferedImage.TYPE_INT_ARGB), rectangle,
                _defaultImageRenderingHints(), false);
    }

    /** Create a BufferedImage the size of the given rectangle and draw
     *  this plot to it at the position specified by the rectangle.
     *  The plot is rendered using anti-aliasing.
     *  @param rectangle The size of the plot. This method can be used, for
     *  example, by a servlet to produce an image, rather than
     *  requiring an applet to instantiate a PlotBox.
     *  @return An image containing the plot.
     */
    public synchronized BufferedImage exportImage(Rectangle rectangle) {
        return exportImage(new BufferedImage(rectangle.width, rectangle.height,
                BufferedImage.TYPE_INT_ARGB), rectangle,
                _defaultImageRenderingHints(), false);
    }

    /** Draw this plot onto the specified image at the position of the
     *  specified rectangle with the size of the specified rectangle.
     *  The plot is rendered using anti-aliasing.
     *  This can be used to paint a number of different
     *  plots onto a single buffered image.  This method can be used, for
     *  example, by a servlet to produce an image, rather than
     *  requiring an applet to instantiate a PlotBox.
     *  @param bufferedImage Image onto which the plot is drawn.
     *  @param rectangle The size and position of the plot in the image.
     *  @param hints Rendering hints for this plot.
     *  @param transparent Indicator that the background of the plot
     *   should not be painted.
     *  @return The modified bufferedImage.
     */
    public synchronized BufferedImage exportImage(BufferedImage bufferedImage,
            Rectangle rectangle, RenderingHints hints, boolean transparent) {
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.addRenderingHints(_defaultImageRenderingHints());

        if (!transparent) {
            graphics.setColor(Color.white); // set the background color
            graphics.fill(rectangle);
        }

        _drawPlot(graphics, false, rectangle);
        return bufferedImage;
    }

    /**        Draw this plot onto the provided image.
     *  This method does not paint the background, so the plot is
     *  transparent.  The plot fills the image, and is rendered
     *  using anti-aliasing.  This method can be used to overlay
     *  multiple plots on the same image, although you must use care
     *  to ensure that the axes and other labels are identical.
     *  Hence, it is usually better to simply combine data sets into
     *  a single plot.
     *  @param bufferedImage The image onto which to render the plot.
     *  @return The modified bufferedImage.
     */
    public synchronized BufferedImage exportImage(BufferedImage bufferedImage) {
        return exportImage(bufferedImage, new Rectangle(bufferedImage
                .getWidth(), bufferedImage.getHeight()),
                _defaultImageRenderingHints(), true);
    }

    /** Rescale so that the data that is currently plotted just fits.
     *  This is done based on the protected variables _xBottom, _xTop,
     *  _yBottom, and _yTop.  It is up to derived classes to ensure that
     *  variables are valid.
     *  This method calls repaint(), which eventually causes the display
     *  to be updated.
     */
    public synchronized void fillPlot() {
        // NOTE: These used to be _setXRange() and _setYRange() to avoid
        // confusing this with user-specified ranges.  But we want to treat
        // a fill command as a user specified range.
        // EAL, 6/12/00.
        setXRange(_xBottom, _xTop);
        setYRange(_yBottom, _yTop);
        repaint();

        // Reacquire the focus so that key bindings work.
        // NOTE: no longer needed?
        // requestFocus();
    }

    /** Get the captions.
     *  @return the captions
     *  @see #addCaptionLine(String)
     *  @see #setCaptions(Vector)
     */
    public Vector<String> getCaptions() {
        return _captionStrings;
    }

    /** Return whether the plot uses color.
     *  @return True if the plot uses color.
     */
    public boolean getColor() {
        return _usecolor;
    }

    /** Get the point colors.
     *  @return Array of colors
     *  @see #setColors(Color[])
     */
    public Color[] getColors() {
        return _colors;
    }

    /** Convert a color name into a Color. Currently, only a very limited
     *  set of color names is supported: black, white, red, green, and blue.
     *  @param name A color name, or null if not found.
     *  @return An instance of Color.
     */
    public static Color getColorByName(String name) {
        final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PlotBox.class);
        try {
            // Check to see if it is a hexadecimal
            if (name.startsWith("#")) {
                name = name.substring(1);
            }

            Color col = new Color(Integer.parseInt(name, 16));
            return col;
        } catch (NumberFormatException e) {
            log.warn("ignoring", e);
        }

        // FIXME: This is a poor excuse for a list of colors and values.
        // We should use a hash table here.
        // Note that Color decode() wants the values to start with 0x.
        String[][] names = { { "black", "00000" }, { "white", "ffffff" },
                { "red", "ff0000" }, { "green", "00ff00" },
                { "blue", "0000ff" } };

        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i][0])) {
                try {
                    Color col = new Color(Integer.parseInt(names[i][1], 16));
                    return col;
                } catch (NumberFormatException e) {
                    log.warn("ignoring", e);
                }
            }
        }

        return null;
    }

    /** Get the file specification that was given by setDataurl.
     *  This method is deprecated.  Use read() instead.
     *  @deprecated
     */
    @Deprecated
    public String getDataurl() {
        return _filespec;
    }

    /** Get the document base that was set by setDocumentBase.
     *  This method is deprecated.  Use read() instead.
     *  @deprecated
     */
    @Deprecated
    public URL getDocumentBase() {
        return _documentBase;
    }

    /** Return whether the grid is drawn.
     *  @return True if a grid is drawn.
     */
    public boolean getGrid() {
        return _grid;
    }

    /** Get the legend for a dataset, or null if there is none.
     *  The legend would have been set by addLegend().
     *  @param dataset The dataset index.
     *  @return The legend label, or null if there is none.
     */
    public synchronized String getLegend(int dataset) {
        int idx = _legendDatasets.indexOf(Integer.valueOf(dataset), 0);

        if (idx != -1) {
            return _legendStrings.elementAt(idx);
        } else {
            return null;
        }
    }

    /** Given a legend string, return the corresponding dataset or -1 if no
     *  legend was added with that legend string
     *  The legend would have been set by addLegend().
     *  @param legend The String naming the legend
     *  @return The legend dataset, or -1 if not found.
     *  @since Ptplot 5.2p1
     */
    public synchronized int getLegendDataset(String legend) {
        int index = _legendStrings.indexOf(legend);

        if (index == -1) {
            return -1;
        }

        return _legendDatasets.get(index).intValue();
    }

    /** If the size of the plot has been set by setSize(),
     *  then return that size.  Otherwise, return what the superclass
     *  returns (which is undocumented, but apparently imposes no maximum size).
     *  Currently (JDK 1.3), only BoxLayout pays any attention to this.
     *  @return The maximum desired size.
     */

    //     public synchronized Dimension getMaximumSize() {
    //         if (_sizeHasBeenSet) {
    //             return new Dimension(_preferredWidth, _preferredHeight);
    //         } else {
    //             return super.getMaximumSize();
    //         }
    //     }
    /** Get the minimum size of this component.
     *  This is simply the dimensions specified by setSize(),
     *  if this has been called.  Otherwise, return whatever the base
     *  class returns, which is undocumented.
     *  @return The minimum size.
     */

    //     public synchronized Dimension getMinimumSize() {
    //         if (_sizeHasBeenSet) {
    //             return new Dimension(_preferredWidth, _preferredHeight);
    //         } else {
    //             return super.getMinimumSize();
    //         }
    //     }
    /** Get the current plot rectangle.
     *  Note that Rectangle returned by this method is calculated
     *  from the values of {@link #_ulx}, {@link #_uly},
     *  {@link #_lrx} and {@link #_lry}.  The value passed in by
     *  setPlotRectangle() is not directly used, thus calling
     *  getPlotRectangle() may not return the same rectangle that
     *  was passed in with setPlotRectangle().
     *  @return Rectangle
     *  @see #setPlotRectangle(Rectangle)
     */
    public Rectangle getPlotRectangle() {
        return new Rectangle(_ulx, _uly, _lrx - _ulx, _lry - _uly);
    }

    /** Get the preferred size of this component.
     *  This is simply the dimensions specified by setSize(),
     *  if this has been called, or the default width and height
     *  otherwise (500 by 300).
     *  @return The preferred size.
     */
    @Override
    public synchronized Dimension getPreferredSize() {
        return new Dimension(_preferredWidth, _preferredHeight);
    }

    /** Get the title of the graph, or an empty string if there is none.
     *  @return The title.
     *  @see #setTitle(String)
     */
    public synchronized String getTitle() {
        if (_title == null) {
            return "";
        }

        return _title;
    }

    /** Get the range for X values of the data points registered so far.
     *  Usually, derived classes handle managing the range by checking
     *  each new point against the current range.
     *  @return An array of two doubles where the first element is the
     *  minimum and the second element is the maximum.
     *  @see #getXRange()
     */
    public synchronized double[] getXAutoRange() {
        double[] result = new double[2];
        result[0] = _xBottom;
        result[1] = _xTop;
        return result;
    }

    /** Get the label for the X (horizontal) axis, or null if none has
     *  been set.
     *  @return The X label.
     */
    public synchronized String getXLabel() {
        return _xlabel;
    }

    /** Return whether the X axis is drawn with a logarithmic scale.
     *  @return True if the X axis is logarithmic.
     */
    public boolean getXLog() {
        return _xlog;
    }

    /** Get the X range. If {@link #setXRange(double, double)} has been
     *  called, then this method returns the values passed in as
     *  arguments to setXRange(double, double).  If setXRange(double,
     *  double) has not been called, then this method returns the
     *  range of the data to be plotted, which might not be all of the
     *  data due to zooming.
     *  @return An array of two doubles where the first element is the
     *  minimum and the second element is the maximum.
     *  @see #getXAutoRange()
     */
    public synchronized double[] getXRange() {
        double[] result = new double[2];

        if (_xRangeGiven) {
            result[0] = _xlowgiven;
            result[1] = _xhighgiven;
        } else {
            // Have to first correct for the padding.
            result[0] = _xMin + ((_xMax - _xMin) * _padding);
            result[1] = _xMax - ((_xMax - _xMin) * _padding);
        }

        return result;
    }

    /** Get the X ticks that have been specified, or null if none.
     *  The return value is an array with two vectors, the first of
     *  which specifies the X tick locations (as instances of Double),
     *  and the second of which specifies the corresponding labels.
     *  @return The X ticks.
     */
    @SuppressWarnings("rawtypes")
    public synchronized Vector[] getXTicks() {
        if (_xticks == null) {
            return null;
        }

        Vector[] result = new Vector[2];
        result[0] = _xticks;
        result[1] = _xticklabels;
        return result;
    }

    /** Get the range for Y values of the data points registered so far.
     *  Usually, derived classes handle managing the range by checking
     *  each new point against the range.
     *  @return An array of two doubles where the first element is the
     *  minimum and the second element is the maximum.
     *  @see #getYRange()
     */
    public synchronized double[] getYAutoRange() {
        double[] result = new double[2];
        result[0] = _yBottom;
        result[1] = _yTop;
        return result;
    }

    /** Get the label for the Y (vertical) axis, or null if none has
     *  been set.
     *  @return The Y label.
     */
    public String getYLabel() {
        return _ylabel;
    }

    /** Return whether the Y axis is drawn with a logarithmic scale.
     *  @return True if the Y axis is logarithmic.
     */
    public boolean getYLog() {
        return _ylog;
    }

    /** Get the Y range. If {@link #setYRange(double, double)} has been
     *  called, then this method returns the values passed in as
     *  arguments to setYRange(double, double).  If setYRange(double,
     *  double) has not been called, then this method returns the
     *  range of the data to be plotted, which might not be all of the
     *  data due to zooming.
     *  @return An array of two doubles where the first element is the
     *  minimum and the second element is the maximum.
     *  @see #getYAutoRange()
     */
    public synchronized double[] getYRange() {
        double[] result = new double[2];

        if (_yRangeGiven) {
            result[0] = _ylowgiven;
            result[1] = _yhighgiven;
        } else {
            // Have to first correct for the padding.
            result[0] = _yMin + ((_yMax - _yMin) * _padding);
            result[1] = _yMax - ((_yMax - _yMin) * _padding);
        }

        return result;
    }

    /** Get the Y ticks that have been specified, or null if none.
     *  The return value is an array with two vectors, the first of
     *  which specifies the Y tick locations (as instances of Double),
     *  and the second of which specifies the corresponding labels.
     *  @return The Y ticks.
     */
    @SuppressWarnings("rawtypes")
    public synchronized Vector[] getYTicks() {
        if (_yticks == null) {
            return null;
        }

        Vector[] result = new Vector[2];
        result[0] = _yticks;
        result[1] = _yticklabels;
        return result;
    }

    /** Initialize the component, creating the fill button and parsing
     *  an input file, if one has been specified.  This is deprecated.
     *  Call setButtons() and read() instead.
     *  @deprecated
     */
    @Deprecated
    public void init() {
        setButtons(true);

        if (_filespec != null) {
            parseFile(_filespec, _documentBase);
        }
    }

    /** Paint the component contents, which in this base class is
     *  only the axes.
     *  @param graphics The graphics context.
     */
    @Override
    public synchronized void paintComponent(Graphics graphics) {
        //Modified by Hiroaki Oyaizu, August 9, 2014
        // - No longer uses a buffered image, all rendering done directly to Graphics

//        BufferedImage newPlotImage = _plotImage;
//
//        if (newPlotImage == null) {
//            Rectangle bounds = getBounds();
//            newPlotImage = new BufferedImage(bounds.width, bounds.height,
//                    BufferedImage.TYPE_3BYTE_BGR);
//            _plotImage = newPlotImage;
//
//            Graphics2D offScreenGraphics = newPlotImage.createGraphics();
//            super.paintComponent(offScreenGraphics);
//            _drawPlot(offScreenGraphics, true);
//        }

        // Blit the offscreen image onto the screen.
        //graphics.drawImage(newPlotImage, 0, 0, null);

        super.paintComponent(graphics);
        _drawPlot(graphics, true);

        // Acquire the focus so that key bindings work.
        // NOTE: no longer needed?
        // requestFocus();
    }

    /** Syntactic sugar for parseFile(filespec, documentBase).
     *  This method is deprecated.  Use read() to read the old file
     *  format, or use one of the classes in the plotml package to
     *  read the XML-based file format.
     *  @deprecated
     */
    @Deprecated
    public void parseFile(String filespec) {
        parseFile(filespec, null);
    }

    /** Open up the input file, which could be stdin, a URL, or a file.
     *  @deprecated This method is deprecated.  Use read() instead.
     */
    @Deprecated
    public synchronized void parseFile(String filespec, URL documentBase) {
        DataInputStream in = null;

        if ((filespec == null) || (filespec.length() == 0)) {
            // Open up stdin
            in = new DataInputStream(System.in);
        } else {
            try {
                URL url = null;

                if ((documentBase == null) && (_documentBase != null)) {
                    documentBase = _documentBase;
                }

                if (documentBase == null) {
                    url = new URL(filespec);
                } else {
                    try {
                        url = new URL(documentBase, filespec);
                    } catch (NullPointerException e) {
                        // If we got a NullPointerException, then perhaps we
                        // are calling this as an application, not as an applet
                        url = new URL(filespec);
                    }
                }

                in = new DataInputStream(url.openStream());
            } catch (MalformedURLException e) {
                try {
                    // Just try to open it as a file.
                    in = new DataInputStream(new FileInputStream(filespec));
                } catch (FileNotFoundException me) {
                    _errorMsg = new String[2];
                    _errorMsg[0] = "File not found: " + filespec;
                    _errorMsg[1] = me.getMessage();
                    return;
                } catch (SecurityException me) {
                    _errorMsg = new String[2];
                    _errorMsg[0] = "Security Exception: " + filespec;
                    _errorMsg[1] = me.getMessage();
                    return;
                }
            } catch (IOException ioe) {
                _errorMsg = new String[3];
                _errorMsg[0] = "Failure opening URL: ";
                _errorMsg[1] = " " + filespec;
                _errorMsg[2] = ioe.getMessage();
                return;
            }
        }

        // At this point, we've opened the data source, now read it in
        try {
            BufferedReader din = new BufferedReader(new InputStreamReader(in));
            String line = din.readLine();

            while (line != null) {
                _parseLine(line);
                line = din.readLine();
            }
            din.close();
        } catch (MalformedURLException e) {
            _errorMsg = new String[2];
            _errorMsg[0] = "Malformed URL: " + filespec;
            _errorMsg[1] = e.getMessage();
            return;
        } catch (IOException e) {
            _errorMsg = new String[2];
            _errorMsg[0] = "Failure reading data: " + filespec;
            _errorMsg[1] = e.getMessage();
            _errorMsg[1] = e.getMessage();
        } finally {
            try {
                in.close();
            } catch (IOException me) {
                log.warn("ignoring", me);
            }
        }
    }

    /** Print the plot to a printer, represented by the specified graphics
     *  object.
     *  @param graphics The context into which the page is drawn.
     *  @param format The size and orientation of the page being drawn.
     *  @param index The zero based index of the page to be drawn.
     *  @return PAGE_EXISTS if the page is rendered successfully, or
     *   NO_SUCH_PAGE if pageIndex specifies a non-existent page.
     *  @exception PrinterException If the print job is terminated.
     */
    @Override
    public synchronized int print(Graphics graphics, PageFormat format,
            int index) throws PrinterException {

        if (graphics == null) {
            return Printable.NO_SUCH_PAGE;
        }

        // We only print on one page.
        if (index >= 1) {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D graphics2D = (Graphics2D) graphics;

        // Scale the printout to fit the pages.
        // Contributed by Laurent ETUR, Schlumberger Riboud Product Center
        double scalex = format.getImageableWidth() / getWidth();
        double scaley = format.getImageableHeight() / getHeight();
        double scale = Math.min(scalex, scaley);
        graphics2D.translate((int) format.getImageableX(), (int) format
                .getImageableY());
        graphics2D.scale(scale, scale);
        _drawPlot(graphics, true);
        return Printable.PAGE_EXISTS;
    }

    /** Read commands and/or plot data from an input stream in the old
     *  (non-XML) file syntax.
     *  To update the display, call repaint(), or make the plot visible with
     *  setVisible(true).
     *  <p>
     *  To read from standard input, use:
     *  <pre>
     *     read(System.in);
     *  </pre>
     *  To read from a url, use:
     *  <pre>
     *     read(url.openStream());
     *  </pre>
     *  To read a URL from within an applet, use:
     *  <pre>
     *     URL url = new URL(getDocumentBase(), urlSpec);
     *     read(url.openStream());
     *  </pre>
     *  Within an application, if you have an absolute URL, use:
     *  <pre>
     *     URL url = new URL(urlSpec);
     *     read(url.openStream());
     *  </pre>
     *  To read from a file, use:
     *  <pre>
     *     read(new FileInputStream(filename));
     *  </pre>
     *  @param in The input stream.
     *  @exception IOException If the stream cannot be read.
     */
    public synchronized void read(InputStream in) throws IOException {
        try {
            // NOTE: I tried to use exclusively the jdk 1.1 Reader classes,
            // but they provide no support like DataInputStream, nor
            // support for URL accesses.  So I use the older classes
            // here in a strange mixture.
            BufferedReader din = new BufferedReader(new InputStreamReader(in));

            try {
                String line = din.readLine();

                while (line != null) {
                    _parseLine(line);
                    line = din.readLine();
                }
            } finally {
                din.close();
            }
        } catch (IOException e) {
            _errorMsg = new String[2];
            _errorMsg[0] = "Failure reading input data.";
            _errorMsg[1] = e.getMessage();
            throw e;
        }
    }

    /** Read a single line command provided as a string.
     *  The commands can be any of those in the ASCII file format.
     *  @param command A command.
     */
    public synchronized void read(String command) {
        _parseLine(command);
    }

    /** Remove the legend (displayed at the upper right) for the specified
     *  data set. If the dataset is not found, nothing will occur.
     *  The PlotBox must be repainted in order for this to take effect.
     *  @param dataset The dataset index.
     */
    public synchronized void removeLegend(int dataset) {
        final int len = _legendDatasets.size();
        int foundIndex = -1;
        boolean found = false;

        for (int i = 0; (i < len) && !found; ++i) {
            if (_legendDatasets.get(i).intValue() == dataset) {
                foundIndex = i;
                found = true;
            }
        }

        if (found) {
            _legendDatasets.remove(foundIndex);
            _legendStrings.remove(foundIndex);
        }
    }

    /** Rename a legend.
     *  @param dataset The dataset of the legend to be renamed.
     *  If there is no dataset with this value, then nothing happens.
     *  @param newName  The new name of legend.
     *  @see #addLegend(int, String)
     */
    public synchronized void renameLegend(int dataset, String newName) {
        int index = _legendDatasets.indexOf(Integer.valueOf(dataset), 0);

        if (index != -1) {
            _legendStrings.setElementAt(newName, index);

            // Changing legend means we need to repaint the offscreen buffer.
            _plotImage = null;
        }
    }

    /** Reset the X and Y axes to the ranges that were first specified
     *  using setXRange() and setYRange(). If these methods have not been
     *  called, then reset to the default ranges.
     *  This method calls repaint(), which eventually causes the display
     *  to be updated.
     */
    public synchronized void resetAxes() {
        setXRange(_originalXlow, _originalXhigh);
        setYRange(_originalYlow, _originalYhigh);
        repaint();
    }

    /** Do nothing in this base class. Derived classes might want to override
     *  this class to give an example of their use.
     */
    public void samplePlot() {
        // Empty default implementation.
    }

    /**
     * Set automatic rescale. Automatic rescaling is enabled
     * when automaticRescale equals true and disabled when
     * automaticRescale equals false.
     * @param automaticRescale The boolean that specifies whether
     * plots should be automatic rescaled.
     */
    public void setAutomaticRescale(boolean automaticRescale) {
        _automaticRescale = automaticRescale;
        if (automaticRescale) {
            if (_timerTask == null) {
                _timerTask = new TimedRepaint();
            }
            _timerTask.addListener(this);
        } else if (!_timedRepaint) {
            _resetScheduledTasks();
            if (_timerTask != null) {
                _timerTask.removeListener(this);
                _timerTask = null;
            }
        }
    }

    /** Set the background color.
     *  @param background The background color.
     */
    @Override
    public synchronized void setBackground(Color background) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _background = background;
        super.setBackground(_background);
    }

    /** Move and resize this component. The new location of the top-left
     *  corner is specified by x and y, and the new size is specified by
     *  width and height. This overrides the base class method to make
     *  a record of the new size.
     *  @param x The new x-coordinate of this component.
     *  @param y The new y-coordinate of this component.
     *  @param width The new width of this component.
     *  @param height The new height of this component.
     */
    @Override
    public synchronized void setBounds(int x, int y, int width, int height) {
        _width = width;
        _height = height;

        // Resizing the component means we need to redraw the buffer.
        _plotImage = null;

        super.setBounds(x, y, _width, _height);
    }

    /** If the argument is true, make a fill button visible at the upper
     *  right.  This button auto-scales the plot.
     *  NOTE: The button may infringe on the title space,
     *  if the title is long.  In an application, it is preferable to provide
     *  a menu with the fill command.  This way, when printing the plot,
     *  the printed plot will not have a spurious button.  Thus, this method
     *  should be used only by applets, which normally do not have menus.
     *  This method should only be called from within the event dispatch
     *  thread, since it interacts with swing.
     *  @see #destroy()
     */
    public synchronized void setButtons(boolean visible) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        // Netbeans wants this, otherwise the icons won't appear.
        ClassLoader classLoader = getClass().getClassLoader();
        if (_printButton == null) {
            // Load the image by using the absolute path to the gif.
            URL img = null;
            try {
                // FindBugs: Usage of GetResource may be unsafe if
                // class is extended
                img = FileUtilities.nameToURL(
                        "$CLASSPATH/ptolemy/plot/img/print.gif", null,
                        classLoader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (img != null) {
                ImageIcon printIcon = new ImageIcon(img);
                _printButton = new JButton(printIcon);
                _printButton.setBorderPainted(false);
            } else {
                // Backup in case something goes wrong with the
                // class loader.
                _printButton = new JButton("P");
            }

            // FIXME: If we failed to get an image, then the letter "P"
            // Is not likely to fit into a 20x20 button.
            _printButton.setPreferredSize(new Dimension(20, 20));
            _printButton.setToolTipText("Print the plot.");
            _printButton.addActionListener(new ButtonListener());
            add(_printButton);
        }

        _printButton.setVisible(visible);

        if (_resetButton == null) {
            // Load the image by using the absolute path to the gif.
            URL img = null;
            try {
                // FindBugs: Usage of GetResource may be unsafe if
                // class is extended
                img = FileUtilities.nameToURL(
                        "$CLASSPATH/ptolemy/plot/img/reset.gif", null,
                        classLoader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (img != null) {
                ImageIcon resetIcon = new ImageIcon(img);
                _resetButton = new JButton(resetIcon);
                _resetButton.setBorderPainted(false);
            } else {
                // Backup in case something goes wrong with the
                // class loader.
                _resetButton = new JButton("R");
            }

            // FIXME: If we failed to get an image, then the letter "R"
            // Is not likely to fit into a 20x20 button.
            _resetButton.setPreferredSize(new Dimension(20, 20));
            _resetButton
                    .setToolTipText("Reset X and Y ranges to their original values");
            _resetButton.addActionListener(new ButtonListener());
            add(_resetButton);
        }

        _resetButton.setVisible(visible);

        if (_formatButton == null) {
            // Load the image by using the absolute path to the gif.
            URL img = null;
            try {
                // FindBugs: Usage of GetResource may be unsafe if
                // class is extended
                img = FileUtilities.nameToURL(
                        "$CLASSPATH/ptolemy/plot/img/format.gif", null,
                        classLoader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (img != null) {
                ImageIcon formatIcon = new ImageIcon(img);
                _formatButton = new JButton(formatIcon);
                _formatButton.setBorderPainted(false);
            } else {
                // Backup in case something goes wrong with the
                // class loader.
                _formatButton = new JButton("S");
            }

            // FIXME: If we failed to get an image, then the letter "S"
            // Is not likely to fit into a 20x20 button.
            _formatButton.setPreferredSize(new Dimension(20, 20));
            _formatButton.setToolTipText("Set the plot format");
            _formatButton.addActionListener(new ButtonListener());
            add(_formatButton);
        }

        _formatButton.setVisible(visible);

        if (_fillButton == null) {
            // Load the image by using the absolute path to the gif.
            URL img = null;
            try {
                // FindBugs: Usage of GetResource may be unsafe if
                // class is extended
                img = FileUtilities.nameToURL(
                        "$CLASSPATH/ptolemy/plot/img/fill.gif", null,
                        classLoader);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (img != null) {
                ImageIcon fillIcon = new ImageIcon(img);
                _fillButton = new JButton(fillIcon);
                _fillButton.setBorderPainted(false);
            } else {
                // Backup in case something goes wrong with the
                // class loader.
                _fillButton = new JButton("F");
            }

            // FIXME: If we failed to get an image, then the letter "F"
            // Is not likely to fit into a 20x20 button.
            _fillButton.setPreferredSize(new Dimension(20, 20));
            _fillButton.setToolTipText("Rescale the plot to fit the data");
            _fillButton.addActionListener(new ButtonListener());
            add(_fillButton);
        }

        _fillButton.setVisible(visible);

        // Request the focus so that key events are heard.
        // NOTE: no longer needed?
        // requestFocus();
    }

    /** Set the strings of the caption.
     *  @param captionStrings A Vector where each element contains a String
     *  that is one line of the caption.
     *  @see #getCaptions()
     *  @see #clearCaptions()
     */
    public void setCaptions(Vector<String> captionStrings) {
        // Changing caption means we need to repaint the offscreen buffer.
        _plotImage = null;
        _captionStrings = captionStrings;
    }

    /** If the argument is false, draw the plot without using color
     *  (in black and white).  Otherwise, draw it in color (the default).
     *  @param useColor False to draw in back and white.
     */
    public synchronized void setColor(boolean useColor) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _usecolor = useColor;
    }

    /** Set the point colors.  Note that the default colors have been
     *  carefully selected to maximize readability and that it is easy
     *  to use colors that result in a very ugly plot.
     *  @param colors Array of colors to use in succession for data sets.
     *  @see #getColors()
     */
    public synchronized void setColors(Color[] colors) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _colors = colors;
    }

    /** Set the file to read when init() is called.
     *  This method is deprecated.  Use read() instead.
     *  @deprecated
     */
    @Deprecated
    public void setDataurl(String filespec) {
        _filespec = filespec;
    }

    /** Set the document base to used when init() is called to read a URL.
     *  This method is deprecated.  Use read() instead.
     *  @deprecated
     */
    @Deprecated
    public void setDocumentBase(URL documentBase) {
        _documentBase = documentBase;
    }

    /** Set the foreground color.
     *  @param foreground The foreground color.
     */
    @Override
    public synchronized void setForeground(Color foreground) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _foreground = foreground;
        super.setForeground(_foreground);
    }

    /** Control whether the grid is drawn.
     *  @param grid If true, a grid is drawn.
     */
    public synchronized void setGrid(boolean grid) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _grid = grid;
    }

    /** Set the label font, which is used for axis labels and legend labels.
     *  The font names understood are those understood by
     *  java.awt.Font.decode().
     *  @param name A font name.
     */
    public synchronized void setLabelFont(String name) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _labelFont = Font.decode(name);
        _labelFontMetrics = getFontMetrics(_labelFont);
    }

    /** Set the plot rectangle inside the axes.  This method
     *  can be used to create two plots that share the same axes.
     *  @param rectangle Rectangle space inside axes.
     *  @see #getPlotRectangle()
     */
    public synchronized void setPlotRectangle(Rectangle rectangle) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _specifiedPlotRectangle = rectangle;
    }

    /** Set the size of the plot.  This overrides the base class to make
     *  it work.  In particular, it records the specified size so that
     *  getMinimumSize() and getPreferredSize() return the specified value.
     *  However, it only works if the plot is placed in its own JPanel.
     *  This is because the JPanel asks the contained component for
     *  its preferred size before determining the size of the panel.
     *  If the plot is placed directly in the content pane of a JApplet,
     *  then, mysteriously, this method has no effect.
     *  @param width The width, in pixels.
     *  @param height The height, in pixels.
     */
    @Override
    public synchronized void setSize(int width, int height) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _width = width;
        _height = height;
        _preferredWidth = width;
        _preferredHeight = height;

        //_sizeHasBeenSet = true;
        super.setSize(width, height);
    }

    /**
     * Set repainting with a certain fixed refresh rate. This timed
     * repainting is enabled when timedRepaint equals true and
     * disabled when timedRepaint equals false.
     * @param timedRepaint The boolean that specifies whether
     * repainting should happen with a certain fixed refresh rate.
     */
    public void setTimedRepaint(boolean timedRepaint) {
        _timedRepaint = timedRepaint;
        if (timedRepaint) {
            if (_timerTask == null) {
                _timerTask = new TimedRepaint();
            }
            _timerTask.addListener(this);
        } else if (!_automaticRescale) {
            if (_timerTask != null) {
                _timerTask.removeListener(this);
                _timerTask = null;
            }
            _resetScheduledTasks();
        }
    }

    /** Set the title of the graph.
     *  @param title The title.
     *  @see #getTitle()
     */
    public synchronized void setTitle(String title) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _title = title;
    }

    /** Set the title font.
     *  The font names understood are those understood by
     *  java.awt.Font.decode().
     *  @param name A font name.
     */
    public synchronized void setTitleFont(String name) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _titleFont = Font.decode(name);
        _titleFontMetrics = getFontMetrics(_titleFont);
    }

    /** Specify whether the X axis is wrapped.
     *  If it is, then X values that are out of range are remapped
     *  to be in range using modulo arithmetic. The X range is determined
     *  by the most recent call to setXRange() (or the most recent zoom).
     *  If the X range has not been set, then use the default X range,
     *  or if data has been plotted, then the current fill range.
     *  @param wrap If true, wrapping of the X axis is enabled.
     */
    public synchronized void setWrap(boolean wrap) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _wrap = wrap;

        if (!_xRangeGiven) {
            if (_xBottom > _xTop) {
                // have nothing to go on.
                setXRange(0, 0);
            } else {
                setXRange(_xBottom, _xTop);
            }
        }

        _wrapLow = _xlowgiven;
        _wrapHigh = _xhighgiven;
    }

    /** Set the label for the X (horizontal) axis.
     *  @param label The label.
     */
    public synchronized void setXLabel(String label) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _xlabel = label;
    }

    /** Specify whether the X axis is drawn with a logarithmic scale.
     *  If you would like to have the X axis drawn with a
     *  logarithmic axis, then setXLog(true) should be called before
     *  adding any data points.
     *  @param xlog If true, logarithmic axis is used.
     */
    public synchronized void setXLog(boolean xlog) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _xlog = xlog;
    }

    /** Set the X (horizontal) range of the plot.  If this is not done
     *  explicitly, then the range is computed automatically from data
     *  available when the plot is drawn.  If min and max
     *  are identical, then the range is arbitrarily spread by 1.
     *  <p>Note that if {@link #setXLog(boolean)} has been called, then
     *  the min and max values should be in log units.
     *  @param min The left extent of the range.
     *  @param max The right extent of the range.
     */
    public synchronized void setXRange(double min, double max) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _xRangeGiven = true;
        _xlowgiven = min;
        _xhighgiven = max;
        _setXRange(min, max);
    }

    /** Set the label for the Y (vertical) axis.
     *  @param label The label.
     */
    public synchronized void setYLabel(String label) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _ylabel = label;
    }

    /** Specify whether the Y axis is drawn with a logarithmic scale.
     *  If you would like to have the Y axis drawn with a
     *  logarithmic axis, then setYLog(true) should be called before
     *  adding any data points.
     *  @param ylog If true, logarithmic axis is used.
     */
    public synchronized void setYLog(boolean ylog) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _ylog = ylog;
    }

    /** Set the Y (vertical) range of the plot.  If this is not done
     *  explicitly, then the range is computed automatically from data
     *  available when the plot is drawn.  If min and max are identical,
     *  then the range is arbitrarily spread by 0.1.
     *  <p>Note that if {@link #setYLog(boolean)} has been called, then
     *  the min and max values should be in log units.
     *  @param min The bottom extent of the range.
     *  @param max The top extent of the range.
     */
    public synchronized void setYRange(double min, double max) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _yRangeGiven = true;
        _ylowgiven = min;
        _yhighgiven = max;
        _setYRange(min, max);
    }

    /** Write the current data and plot configuration to the
     *  specified stream in PlotML syntax.  PlotML is an XML
     *  extension for plot data.  The written information is
     *  standalone, in that it includes the DTD (document type
     *  definition).  This makes is somewhat verbose.  To get
     *  smaller files, use the two argument version of write().
     *  The output is buffered, and is flushed and
     *  closed before exiting.  Derived classes should override
     *  writeFormat and writeData rather than this method.
     *  @param out An output stream.
     */
    public void write(OutputStream out) {
        write(out, null);
    }

    /** Write the current data and plot configuration to the
     *  specified stream in PlotML syntax.  PlotML is an XML
     *  scheme for plot data. The URL (relative or absolute) for the DTD is
     *  given as the second argument.  If that argument is null,
     *  then the PlotML PUBLIC DTD is referenced, resulting in a file
     *  that can be read by a PlotML parser without any external file
     *  references, as long as that parser has local access to the DTD.
     *  The output is buffered, and is flushed and
     *  closed before exiting.  Derived classes should override
     *  writeFormat and writeData rather than this method.
     *  @param out An output stream.
     *  @param dtd The reference (URL) for the DTD, or null to use the
     *   PUBLIC DTD.
     */
    public synchronized void write(OutputStream out, String dtd) {
        write(new OutputStreamWriter(out), dtd);
    }

    /** Write the current data and plot configuration to the
     *  specified stream in PlotML syntax.  PlotML is an XML
     *  scheme for plot data. The URL (relative or absolute) for the DTD is
     *  given as the second argument.  If that argument is null,
     *  then the PlotML PUBLIC DTD is referenced, resulting in a file
     *  that can be read by a PlotML parser without any external file
     *  references, as long as that parser has local access to the DTD.
     *  The output is buffered, and is flushed before exiting.
     *  @param out An output writer.
     *  @param dtd The reference (URL) for the DTD, or null to use the
     *   PUBLIC DTD.
     */
    public synchronized void write(Writer out, String dtd) {
        // Auto-flush is disabled.
        PrintWriter output = new PrintWriter(new BufferedWriter(out), false);

        if (dtd == null) {
            output.println("<?xml version=\"1.0\" standalone=\"yes\"?>");
            output
                    .println("<!DOCTYPE plot PUBLIC \"-//UC Berkeley//DTD PlotML 1//EN\"");
            output
                    .println("    \"http://ptolemy.eecs.berkeley.edu/xml/dtd/PlotML_1.dtd\">");
        } else {
            output.println("<?xml version=\"1.0\" standalone=\"no\"?>");
            output.println("<!DOCTYPE plot SYSTEM \"" + dtd + "\">");
        }

        output.println("<plot>");
        output.println("<!-- Ptolemy plot, version " + PTPLOT_RELEASE
                + " , PlotML format. -->");
        writeFormat(output);
        writeData(output);
        output.println("</plot>");
        output.flush();

        // NOTE: We used to close the stream, but if this is part
        // of an exportMoML operation, that is the wrong thing to do.
        // if (out != System.out) {
        //    output.close();
        // }
    }

    /** Write plot data information to the specified output stream in PlotML.
     *  In this base class, there is no data to write, so this method
     *  returns without doing anything.
     *  @param output A buffered print writer.
     */
    public synchronized void writeData(PrintWriter output) {
    }

    /** Write plot format information to the specified output stream in PlotML.
     *  Derived classes should override this method to first call
     *  the parent class method, then add whatever additional format
     *  information they wish to add to the stream.
     *  @param output A buffered print writer.
     */
    public synchronized void writeFormat(PrintWriter output) {
        // NOTE: If you modify this, you should change the _DTD variable
        // accordingly.
        if (_title != null) {
            output.println("<title>" + _title + "</title>");
        }

        if (_captionStrings != null) {
            for (Enumeration<String> captions = _captionStrings.elements(); captions
                    .hasMoreElements();) {
                String captionLine = captions.nextElement();
                output.println("<caption>" + captionLine + "</caption>");
            }
        }

        if (_xlabel != null) {
            output.println("<xLabel>" + _xlabel + "</xLabel>");
        }

        if (_ylabel != null) {
            output.println("<yLabel>" + _ylabel + "</yLabel>");
        }

        if (_xRangeGiven) {
            output.println("<xRange min=\"" + _xlowgiven + "\" max=\""
                    + _xhighgiven + "\"/>");
        }

        if (_yRangeGiven) {
            output.println("<yRange min=\"" + _ylowgiven + "\" max=\""
                    + _yhighgiven + "\"/>");
        }

        if ((_xticks != null) && (_xticks.size() > 0)) {
            output.println("<xTicks>");

            int last = _xticks.size() - 1;

            for (int i = 0; i <= last; i++) {
                output.println("  <tick label=\""
                        + _xticklabels.elementAt(i) + "\" position=\""
                        + _xticks.elementAt(i) + "\"/>");
            }

            output.println("</xTicks>");
        }

        if ((_yticks != null) && (_yticks.size() > 0)) {
            output.println("<yTicks>");

            int last = _yticks.size() - 1;

            for (int i = 0; i <= last; i++) {
                output.println("  <tick label=\""
                        + _yticklabels.elementAt(i) + "\" position=\""
                        + _yticks.elementAt(i) + "\"/>");
            }

            output.println("</yTicks>");
        }

        if (_xlog) {
            output.println("<xLog/>");
        }

        if (_ylog) {
            output.println("<yLog/>");
        }

        if (!_grid) {
            output.println("<noGrid/>");
        }

        if (_wrap) {
            output.println("<wrap/>");
        }

        if (!_usecolor) {
            output.println("<noColor/>");
        }
    }

    /** Write the current data and plot configuration to the
     *  specified stream in the old PtPlot syntax.
     *  The output is buffered, and is flushed and
     *  closed before exiting.  Derived classes should override
     *  _writeOldSyntax() rather than this method.
     *  @param out An output stream.
     *  @deprecated
     */
    @Deprecated
    public synchronized void writeOldSyntax(OutputStream out) {
        // Auto-flush is disabled.
        PrintWriter output = new PrintWriter(new BufferedOutputStream(out),
                false);
        _writeOldSyntax(output);
        output.flush();

        // Avoid closing standard out.
        if (out != System.out) {
            output.close();
        }
    }

    /** Zoom in or out to the specified rectangle.
     *  This method calls repaint().
     *  @param lowx The low end of the new X range.
     *  @param lowy The low end of the new Y range.
     *  @param highx The high end of the new X range.
     *  @param highy The high end of the new Y range.
     */
    public synchronized void zoom(double lowx, double lowy, double highx,
            double highy) {
        setXRange(lowx, highx);
        setYRange(lowy, highy);
        repaint();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public variables                  ////
    public static final String PTPLOT_RELEASE = "5.8";

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /**
     * Return whether rescaling of the plot should happen
     * automatic.
     * @return True when rescaling of the plot should happen
     * automatic.
     */
    protected boolean _automaticRescale() {
        return _automaticRescale;
    }

    /** Draw the axes using the current range, label, and title information.
     *  If the second argument is true, clear the display before redrawing.
     *  This method is called by paintComponent().  To cause it to be called
     *  you would normally call repaint(), which eventually causes
     *  paintComponent() to be called.
     *  <p>
     *  Note that this is synchronized so that points are not added
     *  by other threads while the drawing is occurring.  This method
     *  should be called only from the event dispatch thread, consistent
     *  with swing policy.
     *  @param graphics The graphics context.
     *  @param clearfirst If true, clear the plot before proceeding.
     */
    protected synchronized void _drawPlot(Graphics graphics, boolean clearfirst) {
        Rectangle bounds = getBounds();
        _drawPlot(graphics, clearfirst, bounds);
    }

    /** Draw the axes using the current range, label, and title information,
     *  at the size of the specified rectangle.
     *  If the second argument is true, clear the display before redrawing.
     *  This method is called by paintComponent().  To cause it to be called
     *  you would normally call repaint(), which eventually causes
     *  paintComponent() to be called.
     *  <p>
     *  Note that this is synchronized so that points are not added
     *  by other threads while the drawing is occurring.  This method
     *  should be called only from the event dispatch thread, consistent
     *  with swing policy.
     *  @param graphics The graphics context.
     *  @param clearfirst If true, clear the plot before proceeding.
     *  @param drawRect A specification of the size.
     */
    protected synchronized void _drawPlot(Graphics graphics,
            boolean clearfirst, Rectangle drawRect) {
        // Ignore if there is no graphics object to draw on.
        if (graphics == null) {
            return;
        }

        Graphics2D antiAlias = (Graphics2D)graphics;
        antiAlias.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        antiAlias.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        antiAlias.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        graphics.setPaintMode();

        /* NOTE: The following seems to be unnecessary with Swing...
         if (clearfirst) {
         // NOTE: calling clearRect() here permits the background
         // color to show through, but it messes up printing.
         // Printing results in black-on-black title and axis labels.
         graphics.setColor(_background);
         graphics.drawRect(0, 0, drawRect.width, drawRect.height);
         graphics.setColor(Color.black);
         }
         */

        // If an error message has been set, display it and return.
        if (_errorMsg != null) {
            int fheight = _labelFontMetrics.getHeight() + 2;
            int msgy = fheight;
            graphics.setColor(Color.black);

            for (String a_errorMsg : _errorMsg) {
                graphics.drawString(a_errorMsg, 10, msgy);
                msgy += fheight;
                log.error(a_errorMsg);
            }

            return;
        }

        // Make sure we have an x and y range
        if (!_xRangeGiven) {
            if (_xBottom > _xTop) {
                // have nothing to go on.
                _setXRange(0, 0);
            } else {
                _setXRange(_xBottom, _xTop);
            }
        }

        if (!_yRangeGiven) {
            if (_yBottom > _yTop) {
                // have nothing to go on.
                _setYRange(0, 0);
            } else {
                _setYRange(_yBottom, _yTop);
            }
        }

        // If user specified a plot rectangle, compute
        // a working plot rectangle which lies inside the
        // drawRect at the user specified coordinates
        Rectangle workingPlotRectangle = null;

        if (_specifiedPlotRectangle != null) {
            workingPlotRectangle = new Rectangle(Math.max(0,
                    _specifiedPlotRectangle.x), Math.max(0,
                    _specifiedPlotRectangle.y), Math.min(drawRect.width,
                    _specifiedPlotRectangle.width), Math.min(drawRect.height,
                    _specifiedPlotRectangle.height));
        }

        // Vertical space for title, if appropriate.
        // NOTE: We assume a one-line title.
        int titley = 0;
        int titlefontheight = _titleFontMetrics.getHeight();

        if (_title == null) {
            // NOTE: If the _title is null, then set it to the empty
            // string to solve the problem where the fill button overlaps
            // the legend if there is no title.  The fix here would
            // be to modify the legend printing text so that it takes
            // into account the case where there is no title by offsetting
            // just enough for the button.
            _title = "";
        }

        if ((_title != null) || (_yExp != 0)) {
            titley = titlefontheight + _topPadding;
        }

        int captionHeight = (_captionStrings.size())
                * (_captionFontMetrics.getHeight());
        if (captionHeight > 0) {
            captionHeight += 5; //extra padding
        }

        // Number of vertical tick marks depends on the height of the font
        // for labeling ticks and the height of the window.
        Font previousFont = graphics.getFont();
        graphics.setFont(_labelFont);
        graphics.setColor(_foreground); // foreground color not set here  --Rob.

        int labelheight = _labelFontMetrics.getHeight();
        int halflabelheight = labelheight / 2;

        // Draw scaling annotation for x axis.
        // NOTE: 5 pixel padding on bottom.
        int ySPos = drawRect.height - captionHeight - 5;
        int xSPos = drawRect.width - _rightPadding;

        if (_xlog) {
            _xExp = (int) Math.floor(_xtickMin);
        }

        if ((_xExp != 0) && (_xticks == null)) {
            String superscript = Integer.toString(_xExp);
            xSPos -= _superscriptFontMetrics.stringWidth(superscript);
            graphics.setFont(_superscriptFont);

            if (!_xlog) {
                graphics
                        .drawString(superscript, xSPos, ySPos - halflabelheight);
                xSPos -= _labelFontMetrics.stringWidth("x10");
                graphics.setFont(_labelFont);
                graphics.drawString("x10", xSPos, ySPos);
            }

            // NOTE: 5 pixel padding on bottom
            _bottomPadding = ((3 * labelheight) / 2) + 5;
        }

        // NOTE: 5 pixel padding on the bottom.
        if ((_xlabel != null)
                && (_bottomPadding < (captionHeight + labelheight + 5))) {
            _bottomPadding = captionHeight + labelheight + 5;
        }

        // Compute the space needed around the plot, starting with vertical.
        // NOTE: padding of 5 pixels below title.
        if (workingPlotRectangle != null) {
            _uly = workingPlotRectangle.y;
        } else {
            _uly = titley + 5;
        }

        // NOTE: 3 pixels above bottom labels.
        if (workingPlotRectangle != null) {
            _lry = workingPlotRectangle.y + workingPlotRectangle.height;
        } else {
            _lry = drawRect.height - labelheight - _bottomPadding - 3;
        }

        int height = _lry - _uly;
        _yscale = height / (_yMax - _yMin);
        _ytickscale = height / (_ytickMax - _ytickMin);

        ////////////////// vertical axis
        // Number of y tick marks.
        // NOTE: subjective spacing factor.
        int ny = 2 + (height / (labelheight + 10));

        // Compute y increment.
        double yStep = _roundUp((_ytickMax - _ytickMin) / ny);

        // Compute y starting point so it is a multiple of yStep.
        double yStart = yStep * Math.ceil(_ytickMin / yStep);

        // NOTE: Following disables first tick.  Not a good idea?
        // if (yStart == _ytickMin) yStart += yStep;
        // Define the strings that will label the y axis.
        // Meanwhile, find the width of the widest label.
        // The labels are quantized so that they don't have excess resolution.
        int widesty = 0;

        // These do not get used unless ticks are automatic, but the
        // compiler is not smart enough to allow us to reference them
        // in two distinct conditional clauses unless they are
        // allocated outside the clauses.
        String[] ylabels = new String[ny];
        int[] ylabwidth = new int[ny];

        int ind = 0;

        if (_yticks == null) {
            Vector<Double> ygrid = null;

            if (_ylog) {
                ygrid = _gridInit(yStart, yStep, true, null);
            }

            // automatic ticks
            // First, figure out how many digits after the decimal point
            // will be used.
            int numfracdigits = _numFracDigits(yStep);

            // NOTE: Test cases kept in case they are needed again.
            // log.info("0.1 with 3 digits: " + _formatNum(0.1, 3));
            // log.info("0.0995 with 3 digits: " +
            //                    _formatNum(0.0995, 3));
            // log.info("0.9995 with 3 digits: " +
            //                    _formatNum(0.9995, 3));
            // log.info("1.9995 with 0 digits: " +
            //                    _formatNum(1.9995, 0));
            // log.info("1 with 3 digits: " + _formatNum(1, 3));
            // log.info("10 with 0 digits: " + _formatNum(10, 0));
            // log.info("997 with 3 digits: " + _formatNum(997, 3));
            // log.info("0.005 needs: " + _numFracDigits(0.005));
            // log.info("1 needs: " + _numFracDigits(1));
            // log.info("999 needs: " + _numFracDigits(999));
            // log.info("999.0001 needs: "+_numFracDigits(999.0001));
            // log.info("0.005 integer digits: " +
            //                    _numIntDigits(0.005));
            // log.info("1 integer digits: " + _numIntDigits(1));
            // log.info("999 integer digits: " + _numIntDigits(999));
            // log.info("-999.0001 integer digits: " +
            //                    _numIntDigits(999.0001));
            double yTmpStart = yStart;

            if (_ylog) {
                yTmpStart = _gridStep(ygrid, yStart, yStep, _ylog);
            }

            for (double ypos = yTmpStart; ypos <= _ytickMax; ypos = _gridStep(
                    ygrid, ypos, yStep, _ylog)) {
                // Prevent out of bounds exceptions
                if (ind >= ny) {
                    break;
                }

                String yticklabel;

                if (_ylog) {
                    yticklabel = _formatLogNum(ypos, numfracdigits);
                } else {
                    yticklabel = _formatNum(ypos, numfracdigits);
                }

                ylabels[ind] = yticklabel;

                int lw = _labelFontMetrics.stringWidth(yticklabel);
                ylabwidth[ind++] = lw;

                if (lw > widesty) {
                    widesty = lw;
                }
            }
        } else {
            // explicitly specified ticks
            Enumeration<String> nl = _yticklabels.elements();

            while (nl.hasMoreElements()) {
                String label = nl.nextElement();
                int lw = _labelFontMetrics.stringWidth(label);

                if (lw > widesty) {
                    widesty = lw;
                }
            }
        }

        // Next we do the horizontal spacing.
        if (workingPlotRectangle != null) {
            _ulx = workingPlotRectangle.x;
        } else {
            if (_ylabel != null) {
                _ulx = widesty + _labelFontMetrics.stringWidth("W")
                        + _leftPadding;
            } else {
                _ulx = widesty + _leftPadding;
            }
        }


        if (workingPlotRectangle != null) {
            _lrx = workingPlotRectangle.x + workingPlotRectangle.width;
        } else {
            //Modified by Hiroaki Oyaizu Aug 9, 2014
            //Moving the legend inside the plot box
//            _lrx = drawRect.width - legendwidth - _rightPadding;
            _lrx = drawRect.width - _rightPadding;
        }

        int width = _lrx - _ulx;
        _xscale = width / (_xMax - _xMin);

        _xtickscale = width / (_xtickMax - _xtickMin);

        // Background for the plotting rectangle.
        // Always use a white background because the dataset colors
        // were designed for a white background.
        //graphics.setColor(Color.white); //original code
        graphics.setColor(this._background);
        graphics.fillRect(_ulx, _uly, width, height);

        graphics.setColor(_foreground);
        graphics.drawRect(_ulx, _uly, width, height);

        // NOTE: subjective tick length.
        int tickLength = 5;
        int xCoord1 = _ulx + tickLength;
        int xCoord2 = _lrx - tickLength;

        if (_yticks == null) {
            // auto-ticks
            Vector<Double> ygrid = null;
            double yTmpStart = yStart;

            if (_ylog) {
                ygrid = _gridInit(yStart, yStep, true, null);
                yTmpStart = _gridStep(ygrid, yStart, yStep, _ylog);
                ny = ind;
            }

            ind = 0;

            // Set to false if we don't need the exponent
            boolean needExponent = _ylog;

            for (double ypos = yTmpStart; ypos <= _ytickMax; ypos = _gridStep(
                    ygrid, ypos, yStep, _ylog)) {
                // Prevent out of bounds exceptions
                if (ind >= ny) {
                    break;
                }

                int yCoord1 = _lry - (int) ((ypos - _ytickMin) * _ytickscale);

                // The lowest label is shifted up slightly to avoid
                // colliding with x labels.
                int offset = 0;

                if ((ind > 0) && !_ylog) {
                    offset = halflabelheight;
                }

                graphics.drawLine(_ulx, yCoord1, xCoord1, yCoord1);
                graphics.drawLine(_lrx, yCoord1, xCoord2, yCoord1);

                if (_grid && (yCoord1 != _uly) && (yCoord1 != _lry)) {
                    graphics.setColor(Color.lightGray);
                    graphics.drawLine(xCoord1, yCoord1, xCoord2, yCoord1);
                    graphics.setColor(_foreground);
                }

                // Check to see if any of the labels printed contain
                // the exponent.  If we don't see an exponent, then print it.
                if (_ylog && (ylabels[ind].indexOf('e') != -1)) {
                    needExponent = false;
                }

                // NOTE: 4 pixel spacing between axis and labels.
                graphics.drawString(ylabels[ind], _ulx - ylabwidth[ind++] - 4,
                        yCoord1 + offset);
            }

            if (_ylog) {
                // Draw in grid lines that don't have labels.
                Vector<Double> unlabeledgrid = _gridInit(yStart, yStep, false, ygrid);

                if (unlabeledgrid.size() > 0) {
                    // If the step is greater than 1, clamp it to 1 so that
                    // we draw the unlabeled grid lines for each
                    //integer interval.
                    double tmpStep = (yStep > 1.0) ? 1.0 : yStep;

                    for (double ypos = _gridStep(unlabeledgrid, yStart,
                            tmpStep, _ylog); ypos <= _ytickMax; ypos = _gridStep(
                            unlabeledgrid, ypos, tmpStep, _ylog)) {
                        int yCoord1 = _lry
                                - (int) ((ypos - _ytickMin) * _ytickscale);

                        if (_grid && (yCoord1 != _uly) && (yCoord1 != _lry)) {
                            graphics.setColor(Color.lightGray);
                            graphics.drawLine(_ulx + 1, yCoord1, _lrx - 1,
                                    yCoord1);
                            graphics.setColor(_foreground);
                        }
                    }
                }

                if (needExponent) {
                    // We zoomed in, so we need the exponent
                    _yExp = (int) Math.floor(yTmpStart);
                } else {
                    _yExp = 0;
                }
            }

            // Draw scaling annotation for y axis.
            if (_yExp != 0) {
                graphics.drawString("x10", 2, titley);
                graphics.setFont(_superscriptFont);
                graphics.drawString(Integer.toString(_yExp), _labelFontMetrics
                        .stringWidth("x10") + 2, titley - halflabelheight);
                graphics.setFont(_labelFont);
            }
        } else {
            // ticks have been explicitly specified
            Enumeration<Double> nt = _yticks.elements();
            Enumeration<String> nl = _yticklabels.elements();

            while (nl.hasMoreElements()) {
                String label = nl.nextElement();
                double ypos = (nt.nextElement()).doubleValue();

                if ((ypos > _yMax) || (ypos < _yMin)) {
                    continue;
                }

                int yCoord1 = _lry - (int) ((ypos - _yMin) * _yscale);
                int offset = 0;

                if (ypos < (_lry - labelheight)) {
                    offset = halflabelheight;
                }

                graphics.drawLine(_ulx, yCoord1, xCoord1, yCoord1);
                graphics.drawLine(_lrx, yCoord1, xCoord2, yCoord1);

                if (_grid && (yCoord1 != _uly) && (yCoord1 != _lry)) {
                    graphics.setColor(Color.lightGray);
                    graphics.drawLine(xCoord1, yCoord1, xCoord2, yCoord1);
                    graphics.setColor(_foreground);
                }

                // NOTE: 3 pixel spacing between axis and labels.
                graphics.drawString(label, _ulx
                        - _labelFontMetrics.stringWidth(label) - 3, yCoord1
                        + offset);
            }
        }

        //////////////////// horizontal axis
        int yCoord1 = _uly + tickLength;
        int yCoord2 = _lry - tickLength;
        int charwidth = _labelFontMetrics.stringWidth("8");

        if (_xticks == null) {
            // auto-ticks
            // Number of x tick marks.
            // Need to start with a guess and converge on a solution here.
            int nx = 10;
            double xStep = 0.0;
            int numfracdigits = 0;

            if (_xlog) {
                // X axes log labels will be at most 6 chars: -1E-02
                nx = 2 + (width / ((charwidth * 6) + 10));
            } else {
                // Limit to 10 iterations
                int count = 0;

                while (count++ <= 10) {
                    xStep = _roundUp((_xtickMax - _xtickMin) / nx);

                    // Compute the width of a label for this xStep
                    numfracdigits = _numFracDigits(xStep);

                    // Number of integer digits is the maximum of two endpoints
                    int intdigits = _numIntDigits(_xtickMax);
                    int inttemp = _numIntDigits(_xtickMin);

                    if (intdigits < inttemp) {
                        intdigits = inttemp;
                    }

                    // Allow two extra digits (decimal point and sign).
                    int maxlabelwidth = charwidth
                            * (numfracdigits + 2 + intdigits);

                    // Compute new estimate of number of ticks.
                    int savenx = nx;

                    // NOTE: 10 additional pixels between labels.
                    // NOTE: Try to ensure at least two tick marks.
                    nx = 2 + (width / (maxlabelwidth + 10));

                    if (((nx - savenx) <= 1) || ((savenx - nx) <= 1)) {
                        break;
                    }
                }
            }

            xStep = _roundUp((_xtickMax - _xtickMin) / nx);
            numfracdigits = _numFracDigits(xStep);

            // Compute x starting point so it is a multiple of xStep.
            double xStart = xStep * Math.ceil(_xtickMin / xStep);

            // NOTE: Following disables first tick.  Not a good idea?
            // if (xStart == _xMin) xStart += xStep;
            Vector<Double> xgrid = null;
            double xTmpStart = xStart;

            if (_xlog) {
                xgrid = _gridInit(xStart, xStep, true, null);

                //xgrid = _gridInit(xStart, xStep);
                xTmpStart = _gridRoundUp(xgrid, xStart);
            }

            // Set to false if we don't need the exponent
            boolean needExponent = _xlog;

            // Label the x axis.  The labels are quantized so that
            // they don't have excess resolution.
            for (double xpos = xTmpStart; xpos <= _xtickMax; xpos = _gridStep(
                    xgrid, xpos, xStep, _xlog)) {
                String xticklabel;

                if (_xlog) {
                    xticklabel = _formatLogNum(xpos, numfracdigits);

                    if (xticklabel.indexOf('e') != -1) {
                        needExponent = false;
                    }
                } else {
                    xticklabel = _formatNum(xpos, numfracdigits);
                }

                xCoord1 = _ulx + (int) ((xpos - _xtickMin) * _xtickscale);
                graphics.drawLine(xCoord1, _uly, xCoord1, yCoord1);
                graphics.drawLine(xCoord1, _lry, xCoord1, yCoord2);

                if (_grid && (xCoord1 != _ulx) && (xCoord1 != _lrx)) {
                    graphics.setColor(Color.lightGray);
                    graphics.drawLine(xCoord1, yCoord1, xCoord1, yCoord2);
                    graphics.setColor(_foreground);
                }

                int labxpos = xCoord1
                        - (_labelFontMetrics.stringWidth(xticklabel) / 2);

                // NOTE: 3 pixel spacing between axis and labels.
                graphics
                        .drawString(xticklabel, labxpos, _lry + 3 + labelheight);
            }

            if (_xlog) {
                // Draw in grid lines that don't have labels.
                // If the step is greater than 1, clamp it to 1 so that
                // we draw the unlabeled grid lines for each
                // integer interval.
                double tmpStep = (xStep > 1.0) ? 1.0 : xStep;

                // Recalculate the start using the new step.
                xTmpStart = tmpStep * Math.ceil(_xtickMin / tmpStep);

                Vector<Double> unlabeledgrid = _gridInit(xTmpStart, tmpStep, false,
                        xgrid);

                if (unlabeledgrid.size() > 0) {
                    for (double xpos = _gridStep(unlabeledgrid, xTmpStart,
                            tmpStep, _xlog); xpos <= _xtickMax; xpos = _gridStep(
                            unlabeledgrid, xpos, tmpStep, _xlog)) {
                        xCoord1 = _ulx
                                + (int) ((xpos - _xtickMin) * _xtickscale);

                        if (_grid && (xCoord1 != _ulx) && (xCoord1 != _lrx)) {
                            graphics.setColor(Color.lightGray);
                            graphics.drawLine(xCoord1, _uly + 1, xCoord1,
                                    _lry - 1);
                            graphics.setColor(_foreground);
                        }
                    }
                }

                if (needExponent) {
                    _xExp = (int) Math.floor(xTmpStart);
                    graphics.setFont(_superscriptFont);
                    graphics.drawString(Integer.toString(_xExp), xSPos, ySPos
                            - halflabelheight);
                    xSPos -= _labelFontMetrics.stringWidth("x10");
                    graphics.setFont(_labelFont);
                    graphics.drawString("x10", xSPos, ySPos);
                } else {
                    _xExp = 0;
                }
            }
        } else {
            // ticks have been explicitly specified
            Enumeration<Double> nt = _xticks.elements();
            Enumeration<String> nl = _xticklabels.elements();

            // Code contributed by Jun Wu (jwu@inin.com.au)
            double preLength = 0.0;

            while (nl.hasMoreElements()) {
                String label = nl.nextElement();
                double xpos = (nt.nextElement()).doubleValue();

                // If xpos is out of range, ignore.
                if ((xpos > _xMax) || (xpos < _xMin)) {
                    continue;
                }

                // Find the center position of the label.
                xCoord1 = _ulx + (int) ((xpos - _xMin) * _xscale);

                // Find  the start position of x label.
                int labxpos = xCoord1
                        - (_labelFontMetrics.stringWidth(label) / 2);

                // If the labels are not overlapped, proceed.
                if (labxpos > preLength) {
                    // calculate the length of the label
                    preLength = xCoord1
                            + (_labelFontMetrics.stringWidth(label) / 2) + 10;

                    // Draw the label.
                    // NOTE: 3 pixel spacing between axis and labels.
                    graphics.drawString(label, labxpos, _lry + 3 + labelheight);

                    // Draw the label mark on the axis
                    graphics.drawLine(xCoord1, _uly, xCoord1, yCoord1);
                    graphics.drawLine(xCoord1, _lry, xCoord1, yCoord2);

                    // Draw the grid line
                    if (_grid && (xCoord1 != _ulx) && (xCoord1 != _lrx)) {
                        graphics.setColor(Color.lightGray);
                        graphics.drawLine(xCoord1, yCoord1, xCoord1, yCoord2);
                        graphics.setColor(_foreground);
                    }
                }
            }
        }

        //////////////////// Draw title and axis labels now.
        // Center the title and X label over the plotting region, not
        // the window.
        graphics.setColor(_foreground);

        if (_title != null) {
            graphics.setFont(_titleFont);

            int titlex = _ulx
                    + ((width - _titleFontMetrics.stringWidth(_title)) / 2);
            graphics.drawString(_title, titlex, titley);
        }

        graphics.setFont(_labelFont);

        if (_xlabel != null) {
            int labelx = _ulx
                    + ((width - _labelFontMetrics.stringWidth(_xlabel)) / 2);
            graphics.drawString(_xlabel, labelx, ySPos);
        }

        int charcenter = 2 + (_labelFontMetrics.stringWidth("W") / 2);

        if (_ylabel != null) {
            int yl = _ylabel.length();

            if (graphics instanceof Graphics2D) {
                int starty = (_uly + ((_lry - _uly) / 2) + (_labelFontMetrics
                        .stringWidth(_ylabel) / 2))
                        - charwidth;
                Graphics2D g2d = (Graphics2D) graphics;

                // NOTE: Fudge factor so label doesn't touch axis labels.
                int startx = (charcenter + halflabelheight) - 2;
                g2d.rotate(Math.toRadians(-90), startx, starty);
                g2d.drawString(_ylabel, startx, starty);
                g2d.rotate(Math.toRadians(90), startx, starty);
            } else {
                // Not graphics 2D, no support for rotation.
                // Vertical label is fairly complex to draw.
                int starty = (_uly + ((_lry - _uly) / 2))
                        - (yl * halflabelheight) + labelheight;

                for (int i = 0; i < yl; i++) {
                    String nchar = _ylabel.substring(i, i + 1);
                    int cwidth = _labelFontMetrics.stringWidth(nchar);
                    graphics.drawString(nchar, charcenter - (cwidth / 2),
                            starty);
                    starty += labelheight;
                }
            }
        }

        graphics.setFont(_captionFont);
        int fontHt = _captionFontMetrics.getHeight();
        int yCapPosn = drawRect.height - captionHeight + 14;
        for (Enumeration<String> captions = _captionStrings.elements(); captions
                .hasMoreElements();) {
            String captionLine = captions.nextElement();
            int labelx = _ulx
                    + ((width - _captionFontMetrics.stringWidth(captionLine)) / 2);
            graphics.drawString(captionLine, labelx, yCapPosn);
            yCapPosn += fontHt;
        }
        graphics.setFont(previousFont);
    }

    /** Put a mark corresponding to the specified dataset at the
     *  specified x and y position.   The mark is drawn in the
     *  current color.  In this base class, a point is a
     *  filled rectangle 6 pixels across.  Note that marks greater than
     *  about 6 pixels in size will not look very good since they will
     *  overlap axis labels and may not fit well in the legend.   The
     *  <i>clip</i> argument, if <code>true</code>, states
     *  that the point should not be drawn if
     *  it is out of range.
     *
     *  Note that this method is not synchronized, so the caller should be.
     *  Moreover this method should always be called from the event thread
     *  when being used to write to the screen.
     *
     *  @param graphics The graphics context.
     *  @param dataset The index of the data set.
     *  @param xpos The X position.
     *  @param ypos The Y position.
     *  @param clip If true, do not draw if out of range.
     */
    protected void _drawPoint(Graphics graphics, int dataset, long xpos,
            long ypos, boolean clip) {
        // Ignore if there is no graphics object to draw on.
        if (graphics == null) {
            return;
        }

        boolean pointinside = (ypos <= _lry) && (ypos >= _uly)
                && (xpos <= _lrx) && (xpos >= _ulx);

        if (!pointinside && clip) {
            return;
        }

        graphics.fillRect((int) xpos - 6, (int) ypos - 6, 6, 6);
    }

    /** Display basic information in its own window.
     */
    protected void _help() {
        String message = "Ptolemy plot package\n"
                + "By: Edward A. Lee\n"
                + "and Christopher Brooks\n"
                + "Version "
                + PTPLOT_RELEASE
                + ", Build: $Id: PlotBox.java 59396 2010-10-06 01:29:54Z cxh $\n\n"
                + "Key bindings:\n"
                + "   Cntrl-c:  copy plot to clipboard (EPS format), if permitted\n"
                + "   D: dump plot data to standard out\n"
                + "   E: export plot to standard out (EPS format)\n"
                + "   F: fill plot\n"
                + "   H or ?: print help message (this message)\n"
                + "   Cntrl-D or Q: quit\n" + "For more information, see\n"
                + "http://ptolemy.eecs.berkeley.edu/java/ptplot\n";
        JOptionPane.showMessageDialog(this, message,
                "Ptolemy Plot Help Window", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Parse a line that gives plotting information.  In this base
     *  class, only lines pertaining to the title and labels are processed.
     *  Everything else is ignored. Return true if the line is recognized.
     *  It is not synchronized, so its caller should be.
     *  @param line A line of text.
     */
    protected boolean _parseLine(String line) {
        // If you modify this method, you should also modify write()
        // We convert the line to lower case so that the command
        // names are case insensitive.
        String lcLine = line.toLowerCase();

        if (lcLine.startsWith("#")) {
            // comment character
            return true;
        } else if (lcLine.startsWith("titletext:")) {
            setTitle((line.substring(10)).trim());
            return true;
        } else if (lcLine.startsWith("title:")) {
            // Tolerate alternative tag.
            setTitle((line.substring(6)).trim());
            return true;
        } else if (lcLine.startsWith("xlabel:")) {
            setXLabel((line.substring(7)).trim());
            return true;
        } else if (lcLine.startsWith("ylabel:")) {
            setYLabel((line.substring(7)).trim());
            return true;
        } else if (lcLine.startsWith("xrange:")) {
            int comma = line.indexOf(",", 7);

            if (comma > 0) {
                String min = (line.substring(7, comma)).trim();
                String max = (line.substring(comma + 1)).trim();

                try {
                    Double dmin = Double.valueOf(min);
                    Double dmax = Double.valueOf(max);
                    setXRange(dmin.doubleValue(), dmax.doubleValue());
                } catch (NumberFormatException e) {
                    log.warn("ignoring", e);
                    // ignore if format is bogus.
                }
            }

            return true;
        } else if (lcLine.startsWith("yrange:")) {
            int comma = line.indexOf(",", 7);

            if (comma > 0) {
                String min = (line.substring(7, comma)).trim();
                String max = (line.substring(comma + 1)).trim();

                try {
                    Double dmin = Double.valueOf(min);
                    Double dmax = Double.valueOf(max);
                    setYRange(dmin.doubleValue(), dmax.doubleValue());
                } catch (NumberFormatException e) {
                    log.warn("ignoring", e);
                    // ignore if format is bogus.
                }
            }

            return true;
        } else if (lcLine.startsWith("xticks:")) {
            // example:
            // XTicks "label" 0, "label" 1, "label" 3
            _parsePairs(line.substring(7), true);
            return true;
        } else if (lcLine.startsWith("yticks:")) {
            // example:
            // YTicks "label" 0, "label" 1, "label" 3
            _parsePairs(line.substring(7), false);
            return true;
        } else if (lcLine.startsWith("xlog:")) {
            if (lcLine.indexOf("off", 5) >= 0) {
                _xlog = false;
            } else {
                _xlog = true;
            }

            return true;
        } else if (lcLine.startsWith("ylog:")) {
            if (lcLine.indexOf("off", 5) >= 0) {
                _ylog = false;
            } else {
                _ylog = true;
            }

            return true;
        } else if (lcLine.startsWith("grid:")) {
            if (lcLine.indexOf("off", 5) >= 0) {
                _grid = false;
            } else {
                _grid = true;
            }

            return true;
        } else if (lcLine.startsWith("wrap:")) {
            if (lcLine.indexOf("off", 5) >= 0) {
                _wrap = false;
            } else {
                _wrap = true;
            }

            return true;
        } else if (lcLine.startsWith("color:")) {
            if (lcLine.indexOf("off", 6) >= 0) {
                _usecolor = false;
            } else {
                _usecolor = true;
            }

            return true;
        } else if (lcLine.startsWith("captions:")) {
            addCaptionLine(line.substring(10));
            return true;
        }

        return false;
    }

    /** Reset a scheduled redraw tasks. This base class does nothing.
     *  Derived classes should define the correct behavior.
     */
    protected void _resetScheduledTasks() {
        // This method should be implemented by the derived classes.
    }

    /** Perform a scheduled redraw. This base class does nothing.
     *  Derived classes should define the correct behavior.
     */
    protected void _scheduledRedraw() {
        // Does nothing on this level
    }

    /** Set the visibility of the Fill button.
     *  This is deprecated.  Use setButtons().
     *  @deprecated
     */
    @Deprecated
    protected void _setButtonsVisibility(boolean vis) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _printButton.setVisible(vis);
        _fillButton.setVisible(vis);
        _formatButton.setVisible(vis);
        _resetButton.setVisible(vis);
    }

    /** Set the padding multiple.
     *  The plot rectangle can be "padded" in each direction -x, +x, -y, and
     *  +y.  If the padding is set to 0.05 (and the padding is used), then
     *  there is 10% more length on each axis than set by the setXRange() and
     *  setYRange() methods, 5% in each direction.
     *  @param padding The padding multiple.
     */
    protected void _setPadding(double padding) {
        // Changing legend means we need to repaint the offscreen buffer.
        _plotImage = null;

        _padding = padding;
    }

    /**
     * Return whether repainting happens by a timed thread.
     * @return True when repainting happens by a timer thread.
     */
    protected boolean _timedRepaint() {
        return _timedRepaint || _automaticRescale;
    }

    /** Write plot information to the specified output stream in the
     *  old PtPlot syntax.
     *  Derived classes should override this method to first call
     *  the parent class method, then add whatever additional information
     *  they wish to add to the stream.
     *  It is not synchronized, so its caller should be.
     *  @param output A buffered print writer.
     *  @deprecated
     */
    @Deprecated
    protected void _writeOldSyntax(PrintWriter output) {
        output.println("# Ptolemy plot, version 2.0");

        if (_title != null) {
            output.println("TitleText: " + _title);
        }

        if (_captionStrings != null) {
            for (Enumeration<String> captions = _captionStrings.elements(); captions
                    .hasMoreElements();) {
                String captionLine = captions.nextElement();
                output.println("Caption: " + captionLine);
            }
        }

        if (_xlabel != null) {
            output.println("XLabel: " + _xlabel);
        }

        if (_ylabel != null) {
            output.println("YLabel: " + _ylabel);
        }

        if (_xRangeGiven) {
            output.println("XRange: " + _xlowgiven + ", " + _xhighgiven);
        }

        if (_yRangeGiven) {
            output.println("YRange: " + _ylowgiven + ", " + _yhighgiven);
        }

        if ((_xticks != null) && (_xticks.size() > 0)) {
            output.print("XTicks: ");

            int last = _xticks.size() - 1;

            for (int i = 0; i < last; i++) {
                output.print("\"" + _xticklabels.elementAt(i) + "\" "
                        + _xticks.elementAt(i) + ", ");
            }

            output.println("\"" + _xticklabels.elementAt(last) + "\" "
                    + _xticks.elementAt(last));
        }

        if ((_yticks != null) && (_yticks.size() > 0)) {
            output.print("YTicks: ");

            int last = _yticks.size() - 1;

            for (int i = 0; i < last; i++) {
                output.print("\"" + _yticklabels.elementAt(i) + "\" "
                        + _yticks.elementAt(i) + ", ");
            }

            output.println("\"" + _yticklabels.elementAt(last) + "\" "
                    + _yticks.elementAt(last));
        }

        if (_xlog) {
            output.println("XLog: on");
        }

        if (_ylog) {
            output.println("YLog: on");
        }

        if (!_grid) {
            output.println("Grid: off");
        }

        if (_wrap) {
            output.println("Wrap: on");
        }

        if (!_usecolor) {
            output.println("Color: off");
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** The maximum y value of the range of the data to be plotted. */
    protected transient volatile double _yMax = 0;

    /** The minimum y value of the range of the data to be plotted. */
    protected transient volatile double _yMin = 0;

    /** The maximum x value of the range of the data to be plotted. */
    protected transient volatile double _xMax = 0;

    /** The minimum y valud of the range of the data to be plotted. */
    protected transient volatile double _xMin = 0;

    /** The factor we pad by so that we don't plot points on the axes.
     */
    protected volatile double _padding = 0.05;

    /** True if the x range have been given. */
    protected transient boolean _xRangeGiven = false;

    /** True if the y range have been given. */
    protected transient boolean _yRangeGiven = false;

    /** True if the ranges were given by zooming. */
    protected transient boolean _rangesGivenByZooming = false;

    /** The given X and Y ranges.
     * If they have been given the top and bottom of the x and y ranges.
     * This is different from _xMin and _xMax, which actually represent
     * the range of data that is plotted.  This represents the range
     * specified (which may be different due to zooming).
     */
    protected double _xlowgiven;

    /** The given X and Y ranges.
     * If they have been given the top and bottom of the x and y ranges.
     * This is different from _xMin and _xMax, which actually represent
     * the range of data that is plotted.  This represents the range
     * specified (which may be different due to zooming).
     */
    protected double _xhighgiven;

    /** The given X and Y ranges.
     * If they have been given the top and bottom of the x and y ranges.
     * This is different from _xMin and _xMax, which actually represent
     * the range of data that is plotted.  This represents the range
     * specified (which may be different due to zooming).
     */
    protected double _ylowgiven;

    /** The given X and Y ranges.
     * If they have been given the top and bottom of the x and y ranges.
     * This is different from _xMin and _xMax, which actually represent
     * the range of data that is plotted.  This represents the range
     * specified (which may be different due to zooming).
     */
    protected double _yhighgiven;

    /** The minimum X value registered so for, for auto ranging. */
    protected double _xBottom = Double.MAX_VALUE;

    /** The maximum X value registered so for, for auto ranging. */
    protected double _xTop = -Double.MAX_VALUE;

    /** The minimum Y value registered so for, for auto ranging. */
    protected double _yBottom = Double.MAX_VALUE;

    /** The maximum Y value registered so for, for auto ranging. */
    protected double _yTop = -Double.MAX_VALUE;

    /** Whether to draw the axes using a logarithmic scale. */
    protected boolean _xlog = false;

    /** Whether to draw the axes using a logarithmic scale. */
    protected boolean _ylog = false;

    // For use in calculating log base 10. A log times this is a log base 10.
    protected static final double _LOG10SCALE = 1 / Math.log(10);

    /** Whether to draw a background grid. */
    protected boolean _grid = true;

    /** Whether to wrap the X axis. */
    protected boolean _wrap = false;

    /** The high range of the X axis for wrapping. */
    protected double _wrapHigh;

    /** The low range of the X axis for wrapping. */
    protected double _wrapLow;

    /** Color of the background, settable from HTML. */
    protected Color _background = Color.white;

    /** Color of the foreground, settable from HTML. */
    protected Color _foreground = Color.black;

    /** Top padding.
     *  Derived classes can increment these to make space around the plot.
     */
    protected int _topPadding = 10;

    /** Bottom padding.
     *  Derived classes can increment these to make space around the plot.
     */
    protected int _bottomPadding = 5;

    /** Right padding.
     *  Derived classes can increment these to make space around the plot.
     */
    protected int _rightPadding = 10;

    /** Left padding.
     *  Derived classes can increment these to make space around the plot.
     */
    protected int _leftPadding = 10;

    // The naming convention is: "_ulx" = "upper left x", where "x" is
    // the horizontal dimension.

    /** The x value of the upper left corner of the plot rectangle in pixels.
     *  Given a mouse click at x0, to convert to data coordinates, use:
     *  (_xMin + (x0 - _ulx) / _xscale).
     */
    protected int _ulx = 1;

    /** The y value of the upper left corner of the plot rectangle in pixels.
     *  Given a mouse click at y0, to convert to data coordinates, use:
     *  (_yMax - (y0 - _uly) / _yscale).
     */
    protected int _uly = 1;

    /** The x value of the lower right corner of
     * the plot rectangle in pixels. */
    protected int _lrx = 100;

    /** The y value of the lower right corner of
     * the plot rectangle in pixels. */
    protected int _lry = 100;

    /** User specified plot rectangle, null if none specified.
     *  @see #setPlotRectangle(Rectangle)
     */
    protected Rectangle _specifiedPlotRectangle = null;

    /** Scaling used for the vertical axis in plotting points.
     *  The units are pixels/unit, where unit is the units of the Y axis.
     */
    protected double _yscale = 1.0;

    /** Scaling used for the horizontal axis in plotting points.
     *  The units are pixels/unit, where unit is the units of the X axis.
     */
    protected double _xscale = 1.0;

    /** Indicator whether to use _colors. */
    protected volatile boolean _usecolor = true;

    // Default _colors, by data set.
    // There are 11 colors so that combined with the
    // 10 marks of the Plot class, we can distinguish 110
    // distinct data sets.
    static protected Color[] _colors = { new Color(0xff0000), // red
            new Color(0x0000ff), // blue
            new Color(0x00aaaa), // cyan-ish
            new Color(0x000000), // black
            new Color(0xffa500), // orange
            new Color(0x53868b), // cadetblue4
            new Color(0xff7f50), // coral
            new Color(0x45ab1f), // dark green-ish
            new Color(0x90422d), // sienna-ish
            new Color(0xa0a0a0), // grey-ish
            new Color(0x14ff14), // green-ish
    };

    /** Width and height of component in pixels. */
    protected int _width = 500;

    /** Width and height of component in pixels. */
    protected int _height = 300;

    /** Width and height of component in pixels. */
    protected int _preferredWidth = 500;

    /** Width and height of component in pixels. */
    protected int _preferredHeight = 300;

    /** Indicator that size has been set. */

    //protected boolean _sizeHasBeenSet = false;
    /** The document base we use to find the _filespec.
     * NOTE: Use of this variable is deprecated.  But it is made available
     * to derived classes for backward compatibility.
     * FIXME: Sun's appletviewer gives an exception if this is protected.
     * Why?? So we make it temporarily public.
     */
    public URL _documentBase = null;

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////

    /*
     * Draw the legend in the upper right corner and return the width
     * (in pixels)  used up.  The arguments give the upper right corner
     * of the region where the legend should be placed.
     */
    @SuppressWarnings("unused")
    private int _drawLegend(Graphics graphics, int urx, int ury) {
        // Ignore if there is no graphics object to draw on.
        if (graphics == null) {
            return 0;
        }

        // FIXME: consolidate all these for efficiency
        Font previousFont = graphics.getFont();
        graphics.setFont(_labelFont);

        int spacing = _labelFontMetrics.getHeight();

        Enumeration<String> v = _legendStrings.elements();
        Enumeration<Integer> i = _legendDatasets.elements();
        int ypos = ury + spacing;
        int maxwidth = 0;

        while (v.hasMoreElements()) {
            String legend = v.nextElement();

            // NOTE: relies on _legendDatasets having the same num. of entries.
            int dataset = i.nextElement().intValue();

            if (dataset >= 0) {
                if (_usecolor) {
                    // Points are only distinguished up to the number of colors
                    int color = dataset % _colors.length;
                    graphics.setColor(_colors[color]);
                }

                _drawPoint(graphics, dataset, urx - 3, ypos - 3, false);

                graphics.setColor(_foreground);

                int width = _labelFontMetrics.stringWidth(legend);

                if (width > maxwidth) {
                    maxwidth = width;
                }

                graphics.drawString(legend, urx - 15 - width, ypos);
                ypos += spacing;
            }
        }

        graphics.setFont(previousFont);
        return 22 + maxwidth; // NOTE: subjective spacing parameter.
    }

    // Execute all actions pending on the deferred action list.
    // The list is cleared and the _actionsDeferred variable is set
    // to false, even if one of the deferred actions fails.
    // This method should only be invoked in the event dispatch thread.
    // It is synchronized, so the integrity of the deferred actions list
    // is ensured, since modifications to that list occur only in other
    // synchronized methods.
    private synchronized void _executeDeferredActions() {
        try {
            for (Runnable action : _deferredActions) {
                action.run();
            }
        } finally {
            _actionsDeferred = false;
            _deferredActions.clear();
        }
    }

    /*
     * Return the number as a String for use as a label on a
     * logarithmic axis.
     * Since this is a log plot, number passed in will not have too many
     * digits to cause problems.
     * If the number is an integer, then we print 1e<num>.
     * If the number is not an integer, then print only the fractional
     * components.
     */
    private String _formatLogNum(double num, int numfracdigits) {
        String results;
        int exponent = (int) num;

        // Determine the exponent, prepending 0 or -0 if necessary.
        if ((exponent >= 0) && (exponent < 10)) {
            results = "0" + exponent;
        } else {
            if ((exponent < 0) && (exponent > -10)) {
                results = "-0" + (-exponent);
            } else {
                results = Integer.toString(exponent);
            }
        }

        // Handle the mantissa.
        if (num >= 0.0) {
            if ((num - (int) (num)) < 0.001) {
                results = "1e" + results;
            } else {
                results = _formatNum(Math.pow(10.0, (num - (int) num)),
                        numfracdigits);
            }
        } else {
            if ((-num - (int) (-num)) < 0.001) {
                results = "1e" + results;
            } else {
                results = _formatNum(Math.pow(10.0, (num - (int) num)) * 10,
                        numfracdigits);
            }
        }

        return results;
    }

    /*
     * Return a string for displaying the specified number
     * using the specified number of digits after the decimal point.
     * NOTE: java.text.NumberFormat in Netscape 4.61 has a bug
     * where it fails to round numbers instead it truncates them.
     * As a result, we don't use java.text.NumberFormat, instead
     * We use the method from Ptplot1.3
     */
    private String _formatNum(double num, int numfracdigits) {
        // When java.text.NumberFormat works under Netscape,
        // uncomment the next block of code and remove
        // the code after it.
        // Ptplot developers at UCB can access a test case at:
        // http://ptolemy.eecs.berkeley.edu/~ptII/ptIItree/ptolemy/plot/adm/trunc/trunc-jdk11.html
        // The plot will show two 0.7 values on the x axis if the bug
        // continues to exist.
        //if (_numberFormat == null) {
        //   // Cache the number format so that we don't have to get
        //    // info about local language etc. from the OS each time.
        //    _numberFormat = NumberFormat.getInstance();
        //}
        //_numberFormat.setMinimumFractionDigits(numfracdigits);
        //_numberFormat.setMaximumFractionDigits(numfracdigits);
        //return _numberFormat.format(num);
        // The section below is from Ptplot1.3
        // First, round the number.
        double fudge = 0.5;

        if (num < 0.0) {
            fudge = -0.5;
        }

        String numString = Double.toString(num
                + (fudge * Math.pow(10.0, -numfracdigits)));

        // Next, find the decimal point.
        int dpt = numString.lastIndexOf(".");
        StringBuffer result = new StringBuffer();

        if (dpt < 0) {
            // The number we are given is an integer.
            if (numfracdigits <= 0) {
                // The desired result is an integer.
                result.append(numString);
                return result.toString();
            }

            // Append a decimal point and some zeros.
            result.append(".");

            for (int i = 0; i < numfracdigits; i++) {
                result.append("0");
            }

            return result.toString();
        } else {
            // There are two cases.  First, there may be enough digits.
            int shortby = numfracdigits - (numString.length() - dpt - 1);

            if (shortby <= 0) {
                int numtocopy = dpt + numfracdigits + 1;

                if (numfracdigits == 0) {
                    // Avoid copying over a trailing decimal point.
                    numtocopy -= 1;
                }

                result.append(numString.substring(0, numtocopy));
                return result.toString();
            } else {
                result.append(numString);

                for (int i = 0; i < shortby; i++) {
                    result.append("0");
                }

                return result.toString();
            }
        }
    }

    /*
     * Determine what values to use for log axes.
     * Based on initGrid() from xgraph.c by David Harrison.
     */
    private Vector<Double> _gridInit(double low, double step, boolean labeled,
            Vector<Double> oldgrid) {
        // How log axes work:
        // _gridInit() creates a vector with the values to use for the
        // log axes.  For example, the vector might contain
        // {0.0 0.301 0.698}, which could correspond to
        // axis labels {1 1.2 1.5 10 12 15 100 120 150}
        //
        // _gridStep() gets the proper value.  _gridInit is cycled through
        // for each integer log value.
        //
        // Bugs in log axes:
        // * Sometimes not enough grid lines are displayed because the
        // region is small.  This bug is present in the oriignal xgraph
        // binary, which is the basis of this code.  The problem is that
        // as ratio gets closer to 1.0, we need to add more and more
        // grid marks.
        Vector<Double> grid = new Vector<Double>(10);

        //grid.addElement(Double.valueOf(0.0));
        double ratio = Math.pow(10.0, step);
        int ngrid = 1;

        if (labeled) {
            // Set up the number of grid lines that will be labeled
            if (ratio <= 3.5) {
                if (ratio > 2.0) {
                    ngrid = 2;
                } else if (ratio > 1.26) {
                    ngrid = 5;
                } else if (ratio > 1.125) {
                    ngrid = 10;
                } else {
                    ngrid = (int) Math.rint(1.0 / step);
                }
            }
        } else {
            // Set up the number of grid lines that will not be labeled
            if (ratio > 10.0) {
                ngrid = 1;
            } else if (ratio > 3.0) {
                ngrid = 2;
            } else if (ratio > 2.0) {
                ngrid = 5;
            } else if (ratio > 1.125) {
                ngrid = 10;
            } else {
                ngrid = 100;
            }

            // Note: we should keep going here, but this increases the
            // size of the grid array and slows everything down.
        }

        int oldgridi = 0;

        for (int i = 0; i < ngrid; i++) {
            double gridval = (i * 1.0) / ngrid * 10;
            double logval = _LOG10SCALE * Math.log(gridval);

            if (logval == Double.NEGATIVE_INFINITY) {
                logval = 0.0;
            }

            // If oldgrid is not null, then do not draw lines that
            // were already drawn in oldgrid.  This is necessary
            // so we avoid obliterating the tick marks on the plot borders.
            if ((oldgrid != null) && (oldgridi < oldgrid.size())) {
                // Cycle through the oldgrid until we find an element
                // that is equal to or greater than the element we are
                // trying to add.
                while ((oldgridi < oldgrid.size())
                        && (oldgrid.elementAt(oldgridi)
                                .doubleValue() < logval)) {
                    oldgridi++;
                }

                if (oldgridi < oldgrid.size()) {
                    // Using == on doubles is bad if the numbers are close,
                    // but not exactly equal.
                    if (Math.abs(oldgrid.elementAt(oldgridi)
                            .doubleValue()
                            - logval) > 0.00001) {
                        grid.addElement(Double.valueOf(logval));
                    }
                } else {
                    grid.addElement(Double.valueOf(logval));
                }
            } else {
                grid.addElement(Double.valueOf(logval));
            }
        }

        // _gridCurJuke and _gridBase are used in _gridStep();
        _gridCurJuke = 0;

        if (low == -0.0) {
            low = 0.0;
        }

        _gridBase = Math.floor(low);

        double x = low - _gridBase;

        // Set gridCurJuke so that the value in grid is greater than
        // or equal to x.  This sets us up to process the first point.
        for (_gridCurJuke = -1; ((_gridCurJuke + 1) < grid.size())
                && (x >= grid.elementAt(_gridCurJuke + 1)
                        .doubleValue()); _gridCurJuke++) ;

        return grid;
    }

    /*
     * Round pos up to the nearest value in the grid.
     */
    private double _gridRoundUp(Vector<Double> grid, double pos) {
        double x = pos - Math.floor(pos);
        int i;

        for (i = 0; (i < grid.size()) && (x >= grid.elementAt(i).doubleValue()); i++);

        if (i >= grid.size()) {
            return pos;
        } else {
            return Math.floor(pos) + grid.elementAt(i).doubleValue();
        }
    }

    /*
     * Used to find the next value for the axis label.
     * For non-log axes, we just return pos + step.
     * For log axes, we read the appropriate value in the grid Vector,
     * add it to _gridBase and return the sum.  We also take care
     * to reset _gridCurJuke if necessary.
     * Note that for log axes, _gridInit() must be called before
     * calling _gridStep().
     * Based on stepGrid() from xgraph.c by David Harrison.
     */
    private double _gridStep(Vector<Double> grid, double pos, double step,
            boolean logflag) {
        if (logflag) {
            if (++_gridCurJuke >= grid.size()) {
                _gridCurJuke = 0;
                _gridBase += Math.ceil(step);
            }

            if (_gridCurJuke >= grid.size()) {
                return pos + step;
            }

            return _gridBase
                    + grid.elementAt(_gridCurJuke).doubleValue();
        } else {
            return pos + step;
        }
    }

    /*
     * Measure the various fonts.  You only want to call this once.
     */
    private void _measureFonts() {
        // We only measure the fonts once, and we do it from addNotify().
        // For maintainability, keep the fonts alphabetized here.
        if (_captionFont == null) {
            _captionFont = new Font("Helvetica", Font.PLAIN, 12);
        }

        if (_labelFont == null) {
            _labelFont = new Font("Helvetica", Font.PLAIN, 12);
        }

        if (_superscriptFont == null) {
            _superscriptFont = new Font("Helvetica", Font.PLAIN, 9);
        }

        if (_titleFont == null) {
            _titleFont = new Font("Helvetica", Font.BOLD, 14);
        }

        _captionFontMetrics = getFontMetrics(_captionFont);
        _labelFontMetrics = getFontMetrics(_labelFont);
        _superscriptFontMetrics = getFontMetrics(_superscriptFont);
        _titleFontMetrics = getFontMetrics(_titleFont);
    }

    /*
     * Return the number of fractional digits required to display the
     * given number.  No number larger than 15 is returned (if
     * more than 15 digits are required, 15 is returned).
     */
    private int _numFracDigits(double num) {
        int numdigits = 0;

        while ((numdigits <= 15) && (num != Math.floor(num))) {
            num *= 10.0;
            numdigits += 1;
        }

        return numdigits;
    }

    /*
     * Return the number of integer digits required to display the
     * given number.  No number larger than 15 is returned (if
     * more than 15 digits are required, 15 is returned).
     */
    private int _numIntDigits(double num) {
        int numdigits = 0;

        while ((numdigits <= 15) && ((int) num != 0.0)) {
            num /= 10.0;
            numdigits += 1;
        }

        return numdigits;
    }

    /*
     * Parse a string of the form: "word num, word num, word num, ..."
     * where the word must be enclosed in quotes if it contains spaces,
     * and the number is interpreted as a floating point number.  Ignore
     * any incorrectly formatted fields.  I <i>xtick</i> is true, then
     * interpret the parsed string to specify the tick labels on the x axis.
     * Otherwise, do the y axis.
     */
    private void _parsePairs(String line, boolean xtick) {
        // Clear current ticks first.
        if (xtick) {
            _xticks = null;
            _xticklabels = null;
        } else {
            _yticks = null;
            _yticklabels = null;
        }

        int start = 0;
        boolean cont = true;

        while (cont) {
            int comma = line.indexOf(",", start);
            String pair = null;

            if (comma > start) {
                pair = (line.substring(start, comma)).trim();
            } else {
                pair = (line.substring(start)).trim();
                cont = false;
            }

            int close = -1;
            int open = 0;

            if (pair.startsWith("\"")) {
                close = pair.indexOf("\"", 1);
                open = 1;
            } else {
                close = pair.indexOf(" ");
            }

            if (close > 0) {
                String label = pair.substring(open, close);
                String index = (pair.substring(close + 1)).trim();

                try {
                    double idx = (Double.valueOf(index)).doubleValue();

                    if (xtick) {
                        addXTick(label, idx);
                    } else {
                        addYTick(label, idx);
                    }
                } catch (NumberFormatException e) {
                    log.error("Warning from PlotBox: "
                            + "Unable to parse ticks: " + e.getMessage());

                    // ignore if format is bogus.
                }
            }

            start = comma + 1;
            comma = line.indexOf(",", start);
        }
    }

    /** Return a default set of rendering hints for image export, which
     *  specifies the use of anti-aliasing.
     */
    private RenderingHints _defaultImageRenderingHints() {
        RenderingHints hints = new RenderingHints(null);
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        return hints;
    }

    /*
     * Given a number, round up to the nearest power of ten
     * times 1, 2, or 5.
     *
     * Note: The argument must be strictly positive.
     */
    private double _roundUp(double val) {
        int exponent = (int) Math.floor(Math.log(val) * _LOG10SCALE);
        val *= Math.pow(10, -exponent);

        if (val > 5.0) {
            val = 10.0;
        } else if (val > 2.0) {
            val = 5.0;
        } else if (val > 1.0) {
            val = 2.0;
        }

        val *= Math.pow(10, exponent);
        return val;
    }

    /*
     * Internal implementation of setXRange, so that it can be called when
     * autoranging.
     */
    private void _setXRange(double min, double max) {
        // We check to see if the original range has been given here
        // because if we check in setXRange(), then we will not catch
        // the case where we have a simple plot file that consists of just
        // data points
        //
        // 1. Create a file that consists of two data points
        //   1 1
        //   2 3
        // 2. Start up plot on it
        // $PTII/bin/ptplot foo.plt
        // 3. Zoom in
        // 4. Hit reset axes
        // 5. The bug is that the axes do not reset to the initial settings
        // Changing the range means we have to replot.
        _plotImage = null;

        if (!_originalXRangeGiven) {
            _originalXlow = min;
            _originalXhigh = max;
            _originalXRangeGiven = true;
        }

        // If values are invalid, try for something reasonable.
        if (min > max) {
            min = -1.0;
            max = 1.0;
        } else if (min == max) {
            min -= 1.0;
            max += 1.0;
        }

        //if (_xRangeGiven) {
        // The user specified the range, so don't pad.
        //    _xMin = min;
        //    _xMax = max;
        //} else {
        // Pad slightly so that we don't plot points on the axes.
        _xMin = min - ((max - min) * _padding);
        _xMax = max + ((max - min) * _padding);

        //}
        // Find the exponent.
        double largest = Math.max(Math.abs(_xMin), Math.abs(_xMax));
        _xExp = (int) Math.floor(Math.log(largest) * _LOG10SCALE);

        // Use the exponent only if it's larger than 1 in magnitude.
        if ((_xExp > 1) || (_xExp < -1)) {
            double xs = 1.0 / Math.pow(10.0, _xExp);
            _xtickMin = _xMin * xs;
            _xtickMax = _xMax * xs;
        } else {
            _xtickMin = _xMin;
            _xtickMax = _xMax;
            _xExp = 0;
        }
    }

    /*
     * Internal implementation of setYRange, so that it can be called when
     * autoranging.
     */
    private void _setYRange(double min, double max) {
        // See comment in _setXRange() about why this is necessary.
        // Changing the range means we have to replot.
        _plotImage = null;

        if (!_originalYRangeGiven) {
            _originalYlow = min;
            _originalYhigh = max;
            _originalYRangeGiven = true;
        }

        // If values are invalid, try for something reasonable.
        if (min > max) {
            min = -1.0;
            max = 1.0;
        } else if (min == max) {
            min -= 0.1;
            max += 0.1;
        }

        //if (_yRangeGiven) {
        // The user specified the range, so don't pad.
        //    _yMin = min;
        //    _yMax = max;
        //} else {
        // Pad slightly so that we don't plot points on the axes.
        _yMin = min - ((max - min) * _padding);
        _yMax = max + ((max - min) * _padding);

        //}
        // Find the exponent.
        double largest = Math.max(Math.abs(_yMin), Math.abs(_yMax));
        _yExp = (int) Math.floor(Math.log(largest) * _LOG10SCALE);

        // Use the exponent only if it's larger than 1 in magnitude.
        if ((_yExp > 1) || (_yExp < -1)) {
            double ys = 1.0 / Math.pow(10.0, _yExp);
            _ytickMin = _yMin * ys;
            _ytickMax = _yMax * ys;
        } else {
            _ytickMin = _yMin;
            _ytickMax = _yMax;
            _yExp = 0;
        }
    }

    /*
     *  Zoom in or out based on the box that has been drawn.
     *  The argument gives the lower right corner of the box.
     *  This method is not synchronized because it is called within
     *  the UI thread, and making it synchronized causes a deadlock.
     *  @param x The final x position.
     *  @param y The final y position.
     */
    void _zoom(int x, int y) {
        // FIXME: This is friendly because Netscape 4.0.3 cannot access it if
        // it is private!
        // NOTE: Due to a bug in JDK 1.1.7B, the BUTTON1_MASK does
        // not work on mouse drags, thus we have to use this variable
        // to determine whether we are actually zooming. It is used only
        // in _zoomBox, since calling this method is properly masked.
        _zooming = false;

        Graphics graphics = getGraphics();

        // Ignore if there is no graphics object to draw on.
        if (graphics == null) {
            return;
        }

        if ((_zoomin == true) && (_drawn == true)) {
            if ((_zoomxn != -1) || (_zoomyn != -1)) {
                // erase previous rectangle.
                int minx = Math.min(_zoomx, _zoomxn);
                int maxx = Math.max(_zoomx, _zoomxn);
                int miny = Math.min(_zoomy, _zoomyn);
                int maxy = Math.max(_zoomy, _zoomyn);
                graphics.setXORMode(_boxColor);
                graphics.drawRect(minx, miny, maxx - minx, maxy - miny);
                graphics.setPaintMode();

                // constrain to be in range
                if (y > _lry) {
                    y = _lry;
                }

                if (y < _uly) {
                    y = _uly;
                }

                if (x > _lrx) {
                    x = _lrx;
                }

                if (x < _ulx) {
                    x = _ulx;
                }

                // NOTE: ignore if total drag less than 5 pixels.
                if ((Math.abs(_zoomx - x) > 5) && (Math.abs(_zoomy - y) > 5)) {
                    double a = _xMin + ((_zoomx - _ulx) / _xscale);
                    double b = _xMin + ((x - _ulx) / _xscale);

                    // NOTE: It used to be that it was problematic to set
                    // the X range here because it conflicted with the wrap
                    // mechanism.  But now the wrap mechanism saves the state
                    // of the X range when the setWrap() method is called,
                    // so this is safe.
                    // EAL 6/12/00.
                    if (a < b) {
                        setXRange(a, b);
                    } else {
                        setXRange(b, a);
                    }

                    a = _yMax - ((_zoomy - _uly) / _yscale);
                    b = _yMax - ((y - _uly) / _yscale);

                    if (a < b) {
                        setYRange(a, b);
                    } else {
                        setYRange(b, a);
                    }
                }

                repaint();
            }
        } else if ((_zoomout == true) && (_drawn == true)) {
            // Erase previous rectangle.
            graphics.setXORMode(_boxColor);

            int x_diff = Math.abs(_zoomx - _zoomxn);
            int y_diff = Math.abs(_zoomy - _zoomyn);
            graphics.drawRect(_zoomx - 15 - x_diff, _zoomy - 15 - y_diff,
                    30 + (x_diff * 2), 30 + (y_diff * 2));
            graphics.setPaintMode();

            // Calculate zoom factor.
            double a = (Math.abs(_zoomx - x)) / 30.0;
            double b = (Math.abs(_zoomy - y)) / 30.0;
            double newx1 = _xMax + ((_xMax - _xMin) * a);
            double newx2 = _xMin - ((_xMax - _xMin) * a);

            // NOTE: To limit zooming out to the fill area, uncomment this...
            // if (newx1 > _xTop) newx1 = _xTop;
            // if (newx2 < _xBottom) newx2 = _xBottom;
            double newy1 = _yMax + ((_yMax - _yMin) * b);
            double newy2 = _yMin - ((_yMax - _yMin) * b);

            // NOTE: To limit zooming out to the fill area, uncomment this...
            // if (newy1 > _yTop) newy1 = _yTop;
            // if (newy2 < _yBottom) newy2 = _yBottom;
            zoom(newx2, newy2, newx1, newy1);
            repaint();
        } else if (_drawn == false) {
            repaint();
        }

        _drawn = false;
        _zoomin = _zoomout = false;
        _zoomxn = _zoomyn = _zoomx = _zoomy = -1;
    }

    /*
     *  Draw a box for an interactive zoom box.  The starting point (the
     *  upper left corner of the box) is taken
     *  to be that specified by the startZoom() method.  The argument gives
     *  the lower right corner of the box.  If a previous box
     *  has been drawn, erase it first.
     *  This method is not synchronized because it is called within
     *  the UI thread, and making it synchronized causes a deadlock.
     *  @param x The x position.
     *  @param y The y position.
     */
    void _zoomBox(int x, int y) {
        // FIXME: This is friendly because Netscape 4.0.3 cannot access it if
        // it is private!
        // NOTE: Due to a bug in JDK 1.1.7B, the BUTTON1_MASK does
        // not work on mouse drags, thus we have to use this variable
        // to determine whether we are actually zooming.
        if (!_zooming) {
            return;
        }

        Graphics graphics = getGraphics();

        // Ignore if there is no graphics object to draw on.
        if (graphics == null) {
            return;
        }

        // Bound the rectangle so it doesn't go outside the box.
        if (y > _lry) {
            y = _lry;
        }

        if (y < _uly) {
            y = _uly;
        }

        if (x > _lrx) {
            x = _lrx;
        }

        if (x < _ulx) {
            x = _ulx;
        }

        // erase previous rectangle, if there was one.
        if (((_zoomx != -1) || (_zoomy != -1))) {
            // Ability to zoom out added by William Wu.
            // If we are not already zooming, figure out whether we
            // are zooming in or out.
            if ((_zoomin == false) && (_zoomout == false)) {
                if (y < _zoomy) {
                    _zoomout = true;

                    // Draw reference box.
                    graphics.setXORMode(_boxColor);
                    graphics.drawRect(_zoomx - 15, _zoomy - 15, 30, 30);
                } else if (y > _zoomy) {
                    _zoomin = true;
                }
            }

            if (_zoomin == true) {
                // Erase the previous box if necessary.
                if (((_zoomxn != -1) || (_zoomyn != -1)) && (_drawn == true)) {
                    int minx = Math.min(_zoomx, _zoomxn);
                    int maxx = Math.max(_zoomx, _zoomxn);
                    int miny = Math.min(_zoomy, _zoomyn);
                    int maxy = Math.max(_zoomy, _zoomyn);
                    graphics.setXORMode(_boxColor);
                    graphics.drawRect(minx, miny, maxx - minx, maxy - miny);
                }

                // Draw a new box if necessary.
                if (y > _zoomy) {
                    _zoomxn = x;
                    _zoomyn = y;

                    int minx = Math.min(_zoomx, _zoomxn);
                    int maxx = Math.max(_zoomx, _zoomxn);
                    int miny = Math.min(_zoomy, _zoomyn);
                    int maxy = Math.max(_zoomy, _zoomyn);
                    graphics.setXORMode(_boxColor);
                    graphics.drawRect(minx, miny, maxx - minx, maxy - miny);
                    _drawn = true;
                    return;
                } else {
                    _drawn = false;
                }
            } else if (_zoomout == true) {
                // Erase previous box if necessary.
                if (((_zoomxn != -1) || (_zoomyn != -1)) && (_drawn == true)) {
                    int x_diff = Math.abs(_zoomx - _zoomxn);
                    int y_diff = Math.abs(_zoomy - _zoomyn);
                    graphics.setXORMode(_boxColor);
                    graphics.drawRect(_zoomx - 15 - x_diff, _zoomy - 15
                            - y_diff, 30 + (x_diff * 2), 30 + (y_diff * 2));
                }

                if (y < _zoomy) {
                    _zoomxn = x;
                    _zoomyn = y;

                    int x_diff = Math.abs(_zoomx - _zoomxn);
                    int y_diff = Math.abs(_zoomy - _zoomyn);
                    graphics.setXORMode(_boxColor);
                    graphics.drawRect(_zoomx - 15 - x_diff, _zoomy - 15
                            - y_diff, 30 + (x_diff * 2), 30 + (y_diff * 2));
                    _drawn = true;
                    return;
                } else {
                    _drawn = false;
                }
            }
        }

        graphics.setPaintMode();
    }

    /*
     *  Set the starting point for an interactive zoom box (the upper left
     *  corner).
     *  This method is not synchronized because it is called within
     *  the UI thread, and making it synchronized causes a deadlock.
     *  @param x The x position.
     *  @param y The y position.
     */
    void _zoomStart(int x, int y) {
        // FIXME: This is friendly because Netscape 4.0.3 cannot access it if
        // it is private!
        // constrain to be in range
        if (y > _lry) {
            y = _lry;
        }

        if (y < _uly) {
            y = _uly;
        }

        if (x > _lrx) {
            x = _lrx;
        }

        if (x < _ulx) {
            x = _ulx;
        }

        _zoomx = x;
        _zoomy = y;
        _zooming = true;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    /** Indicator of whether actions are deferred. */
    private volatile boolean _actionsDeferred = false;

    // True when repainting happens by a timer thread.
    private boolean _automaticRescale = false;

    /** List of deferred actions. */
    private LinkedList<Runnable> _deferredActions = new LinkedList<Runnable>();

    /** The file to be opened. */
    private String _filespec = null;

    // Call setXORMode with a hardwired color because
    // _background does not work in an application,
    // and _foreground does not work in an applet.
    // NOTE: For some reason, this comes out blue, which is fine...
    private static final Color _boxColor = Color.orange;

    /** The range of the plot as labeled
     * (multiply by 10^exp for actual range.
     */
    private double _ytickMax = 0.0;

    /** The range of the plot as labeled
     * (multiply by 10^exp for actual range.
     */
    private double _ytickMin = 0.0;

    /** The range of the plot as labeled
     * (multiply by 10^exp for actual range.
     */
    private double _xtickMax = 0.0;

    /** The range of the plot as labeled
     * (multiply by 10^exp for actual range.
     */
    private double _xtickMin = 0.0;

    /** The power of ten by which the range numbers should
     *  be multiplied.
     */
    private int _yExp = 0;

    /** The power of ten by which the range numbers should
     *  be multiplied.
     */
    private int _xExp = 0;

    /** Scaling used in making tick marks. */
    private double _ytickscale = 0.0;

    /** Scaling used in making tick marks. */
    private double _xtickscale = 0.0;

    /** Caption font information. */
    private Font _captionFont = null;

    /** Font information. */
    private Font _labelFont = null;

    /** Font information. */
    private Font _superscriptFont = null;

    /** Font information. */
    private Font _titleFont = null;

    /** Caption font metric information. */
    private FontMetrics _captionFontMetrics = null;

    /** FontMetric information. */
    private FontMetrics _labelFontMetrics = null;

    /** FontMetric information. */
    private FontMetrics _superscriptFontMetrics = null;

    /** FontMetric information. */
    private FontMetrics _titleFontMetrics = null;

    // Number format cache used by _formatNum.
    // See the comment in _formatNum for more information.
    // private transient NumberFormat _numberFormat = null;
    // Used for log axes. Index into vector of axis labels.
    private transient int _gridCurJuke = 0;

    // Used for log axes.  Base of the grid.
    private transient double _gridBase = 0.0;

    // An array of strings for reporting errors.
    private transient String[] _errorMsg;

    /** The title and label strings. */
    private String _xlabel;

    /** The title and label strings. */
    private String _ylabel;

    /** The title and label strings. */
    private String _title;

    /** Caption information. */
    private Vector<String> _captionStrings = new Vector<String>();

    /** Legend information. */
    private Vector<String> _legendStrings = new Vector<String>();

    /** Legend information. */
    private Vector<Integer> _legendDatasets = new Vector<Integer>();

    /** If XTicks or YTicks are given/ */
    private Vector<Double> _xticks = null;

    /** If XTicks or YTicks are given/ */
    private Vector<String> _xticklabels = null;

    /** If XTicks or YTicks are given/ */
    private Vector<Double> _yticks = null;

    /** If XTicks or YTicks are given/ */
    private Vector<String> _yticklabels = null;

    // A button for filling the plot
    private transient JButton _fillButton = null;

    // A button for formatting the plot
    private transient JButton _formatButton = null;

    // Indicator of whether X and Y range has been first specified.
    boolean _originalXRangeGiven = false;

    // Indicator of whether X and Y range has been first specified.
    boolean _originalYRangeGiven = false;

    // First values specified to setXRange() and setYRange().
    double _originalXlow = 0.0;

    // First values specified to setXRange() and setYRange().
    double _originalXhigh = 0.0;

    // First values specified to setXRange() and setYRange().
    double _originalYlow = 0.0;

    // First values specified to setXRange() and setYRange().
    double _originalYhigh = 0.0;

    // An offscreen buffer for improving plot performance.
    protected transient BufferedImage _plotImage = null;

    // A button for printing the plot
    private transient JButton _printButton = null;

    // A button for filling the plot
    private transient JButton _resetButton = null;

    // True when repainting should be performed by a timed thread.
    private boolean _timedRepaint = false;

    // The timer task that does the repainting.
    static private TimedRepaint _timerTask = null;

    // Variables keeping track of the interactive zoom box.
    // Initialize to impossible values.
    private transient int _zoomx = -1;

    private transient int _zoomy = -1;

    private transient int _zoomxn = -1;

    private transient int _zoomyn = -1;

    // Control whether we are zooming in or out.
    private transient boolean _zoomin = false;

    private transient boolean _zoomout = false;

    private transient boolean _drawn = false;

    private transient boolean _zooming = false;

    // NOTE: It is unfortunate to have to include the DTD here, but there
    // seems to be no other way to ensure that the generated data exactly
    // matches the DTD.
    //    private static final String _DTD =
    //    "<!-- PlotML DTD, created by Edward A. Lee.\n"
    //    + "   See http://ptolemy.eecs.berkeley.edu/java/ptplot -->\n"
    //    + "<!ELEMENT plot (barGraph | bin | dataset | default | noColor | \n"
    //    + "        noGrid | title | wrap | xLabel | xLog | xRange | xTicks | yLabel | \n"
    //    + " yLog | yRange | yTicks)*>\n"
    //    + "  <!ELEMENT barGraph EMPTY>\n"
    //    + "    <!ATTLIST barGraph width CDATA #IMPLIED>\n"
    //    + "    <!ATTLIST barGraph offset CDATA #IMPLIED>\n"
    //    + "  <!ELEMENT bin EMPTY>\n"
    //    + "    <!ATTLIST bin width CDATA #IMPLIED>\n"
    //    + "    <!ATTLIST bin offset CDATA #IMPLIED>\n"
    //    + "  <!ELEMENT dataset (m | move | p | point)*>\n"
    //    + "    <!ATTLIST dataset connected (yes | no) #IMPLIED>\n"
    //    + "    <!ATTLIST dataset marks (none | dots | points | various) #IMPLIED>\n"
    //    + "    <!ATTLIST dataset name CDATA #IMPLIED>\n"
    //    + "    <!ATTLIST dataset stems (yes | no) #IMPLIED>\n"
    //    + "  <!ELEMENT default EMPTY>\n"
    //    + "    <!ATTLIST default connected (yes | no) \"yes\">\n"
    //    + "    <!ATTLIST default marks (none | dots | points | various) \"none\">\n"
    //    + "    <!ATTLIST default stems (yes | no) \"no\">\n"
    //    + "  <!ELEMENT noColor EMPTY>\n"
    //    + "  <!ELEMENT noGrid EMPTY>\n"
    //    + "  <!ELEMENT title (#PCDATA)>\n"
    //    + "  <!ELEMENT wrap EMPTY>\n"
    //    + "  <!ELEMENT xLabel (#PCDATA)>\n"
    //    + "  <!ELEMENT xLog EMPTY>\n"
    //    + "  <!ELEMENT xRange EMPTY>\n"
    //    + "    <!ATTLIST xRange min CDATA #REQUIRED>\n"
    //    + "    <!ATTLIST xRange max CDATA #REQUIRED>\n"
    //    + "  <!ELEMENT xTicks (tick)+>\n"
    //    + "  <!ELEMENT yLabel (#PCDATA)>\n"
    //    + "  <!ELEMENT yLog EMPTY>\n"
    //    + "  <!ELEMENT yRange EMPTY>\n"
    //    + "    <!ATTLIST yRange min CDATA #REQUIRED>\n"
    //    + "    <!ATTLIST yRange max CDATA #REQUIRED>\n"
    //    + "  <!ELEMENT yTicks (tick)+>\n"
    //    + "    <!ELEMENT tick EMPTY>\n"
    //    + "      <!ATTLIST tick label CDATA #REQUIRED>\n"
    //    + "      <!ATTLIST tick position CDATA #REQUIRED>\n"
    //    + "    <!ELEMENT m EMPTY>\n"
    //    + "      <!ATTLIST m x CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST m x CDATA #REQUIRED>\n"
    //    + "      <!ATTLIST m lowErrorBar CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST m highErrorBar CDATA #IMPLIED>\n"
    //    + "    <!ELEMENT move EMPTY>\n"
    //    + "      <!ATTLIST move x CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST move x CDATA #REQUIRED>\n"
    //    + "      <!ATTLIST move lowErrorBar CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST move highErrorBar CDATA #IMPLIED>\n"
    //    + "    <!ELEMENT p EMPTY>\n"
    //    + "      <!ATTLIST p x CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST p x CDATA #REQUIRED>\n"
    //    + "      <!ATTLIST p lowErrorBar CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST p highErrorBar CDATA #IMPLIED>\n"
    //    + "    <!ELEMENT point EMPTY>\n"
    //    + "      <!ATTLIST point x CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST point x CDATA #REQUIRED>\n"
    //    + "      <!ATTLIST point lowErrorBar CDATA #IMPLIED>\n"
    //    + "      <!ATTLIST point highErrorBar CDATA #IMPLIED>";
    ///////////////////////////////////////////////////////////////////
    ////                         inner classes                     ////
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getSource() == _fillButton) {
                fillPlot();
            } else if (event.getSource() == _printButton) {
                // If you are using $PTII/bin/vergil, under bash,
                // set this property:
                // export JAVAFLAGS=-Dptolemy.ptII.print.platform=CrossPlatform
                // and then run $PTII/bin/vergil
                if (StringUtilities.getProperty("ptolemy.ptII.print.platform")
                        .equals("CrossPlatform")) {
                    _printCrossPlatform();
                } else {
                    _printNative();
                }
            } else if (event.getSource() == _resetButton) {
                resetAxes();
            } else if (event.getSource() == _formatButton) {
                PlotFormatter fmt = new PlotFormatter(PlotBox.this);
                fmt.openModal();
            }
        }

        private void _printCrossPlatform() {
            // FIXME:  Code duplication with PlotFrame and Top.
            // See PlotFrame for notes.
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            PrinterJob job = PrinterJob.getPrinterJob();

            // No need to call pageDialog, printDialog has that same tab
            // rbeyer@LPL.Arizona.EDU: Get the Page Format and use it.
            //PageFormat format = job.pageDialog(aset);
            //job.setPrintable(PlotBox.this, format);

            job.setPrintable(PlotBox.this);

            if (job.printDialog(aset)) {
                try {
                    job.print(aset);
                } catch (Exception ex) {
                    Component ancestor = getTopLevelAncestor();
                    JOptionPane.showMessageDialog(ancestor,
                            "Printing failed:\n" + ex.toString(),
                            "Print Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        private void _printNative() {
            // FIXME:  Code duplication with PlotFrame and Top.
            // See PlotFrame for notes.

            // Native printing used not honor the user's
            // choice of portrait vs. landscape.

            PrinterJob job = PrinterJob.getPrinterJob();
            PageFormat pageFormat = job.pageDialog(job.defaultPage());
            job.setPrintable(PlotBox.this, pageFormat);

            if (job.printDialog()) {
                try {
                    // job.print() eventually
                    // calls PlotBox.print(Graphics, PageFormat)
                    job.print();
                } catch (Exception ex) {
                    Component ancestor = getTopLevelAncestor();
                    JOptionPane.showMessageDialog(ancestor,
                            "Printing failed:\n" + ex.toString(),
                            "Print Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public class ZoomListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent event) {
            requestFocus();
        }

        @Override
        public void mouseEntered(MouseEvent event) {
        }

        @Override
        public void mouseExited(MouseEvent event) {
        }

        @Override
        public void mousePressed(MouseEvent event) {
            // http://developer.java.sun.com/developer/bugParade/bugs/4072703.html
            // BUTTON1_MASK still not set for MOUSE_PRESSED events
            // suggests:
            // Workaround
            //   Assume that a press event with no modifiers must be button 1.
            //   This has the serious drawback that it is impossible to be sure
            //   that button 1 hasn't been pressed along with one of the other
            //   buttons.
            // This problem affects Netscape 4.61 under Digital Unix and
            // 4.51 under Solaris
            //
            // Mac OS X 10.5: we want BUTTON1_MASK set and BUTTON3_MASK not set
            // so that when we edit a dataset we don't get the zoom box
            // https://chess.eecs.berkeley.edu/bugzilla/show_bug.cgi?id=300
            if (((event.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
                    && ((event.getModifiers() & InputEvent.BUTTON3_MASK) == 0)
                    || (event.getModifiers() == 0)) {
                PlotBox.this._zoomStart(event.getX(), event.getY());
            }
            // Want to convert from mouse presses to data points?
            // Comment out the if clause above and uncomment this println:
            //log.info("PlotBox.mousePressed(): ("
            //                       + event.getX() + ", " + event.getY()
            //                       + ") = ("
            //                       + (_xMin + ((event.getX() - _ulx) / _xscale))
            //                       + ", "
            //                       + (_yMax - ((event.getY() - _uly) / _yscale))
            //                 + ")");
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            if (((event.getModifiers() & InputEvent.BUTTON1_MASK) != 0)
                    && ((event.getModifiers() & InputEvent.BUTTON3_MASK) == 0)
                    || (event.getModifiers() == 0)) {
                PlotBox.this._zoom(event.getX(), event.getY());
            }
        }
    }

    public class DragListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent event) {
            // NOTE: Due to a bug in JDK 1.1.7B, the BUTTON1_MASK does
            // not work on mouse drags.  It does work on MouseListener
            // methods, so those methods set a variable _zooming that
            // is used by _zoomBox to determine whether to draw a box.
            if ((event.getModifiers() & InputEvent.BUTTON1_MASK) != 0
                    && ((event.getModifiers() & InputEvent.BUTTON3_MASK) == 0)) {
                PlotBox.this._zoomBox(event.getX(), event.getY());
            }
        }

        @Override
        public void mouseMoved(MouseEvent event) {
        }
    }

    class CommandListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();

            switch (keycode) {
            case KeyEvent.VK_CONTROL:
                _control = true;
                break;

            case KeyEvent.VK_SHIFT:
                _shift = true;
                break;

            case KeyEvent.VK_C:

                if (_control) {
                    // The "null" sends the output to the clipboard.
                    export(null);

                    String message = "Encapsulated PostScript (EPS) "
                            + "exported to clipboard.";
                    JOptionPane.showMessageDialog(PlotBox.this, message,
                            "Ptolemy Plot Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                break;

            case KeyEvent.VK_D:

                if (!_control && _shift) {
                    write(System.out);

                    String message = "Plot data sent to standard out.";
                    JOptionPane.showMessageDialog(PlotBox.this, message,
                            "Ptolemy Plot Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                if (_control) {
                    // xgraph and many other Unix apps use Control-D to exit
                    StringUtilities.exit(1);
                }

                break;

            case KeyEvent.VK_E:

                if (!_control && _shift) {
                    export(System.out);

                    String message = "Encapsulated PostScript (EPS) "
                            + "exported to standard out.";
                    JOptionPane.showMessageDialog(PlotBox.this, message,
                            "Ptolemy Plot Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                break;

            case KeyEvent.VK_F:

                if (!_control && _shift) {
                    fillPlot();
                }

                break;

            case KeyEvent.VK_H:

                if (!_control && _shift) {
                    _help();
                }

                break;

            case KeyEvent.VK_Q:

                if (!_control) {
                    // xv uses q to quit.
                    StringUtilities.exit(1);
                }

                break;

            case KeyEvent.VK_SLASH:

                if (_shift) {
                    // Question mark is SHIFT-SLASH
                    _help();
                }

                break;

            default:
                // None
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keycode = e.getKeyCode();

            switch (keycode) {
            case KeyEvent.VK_CONTROL:
                _control = false;
                break;

            case KeyEvent.VK_SHIFT:
                _shift = false;
                break;

            default:
                // None
            }
        }

        // The keyTyped method is broken in jdk 1.1.4.
        // It always gets "unknown key code".
        @Override
        public void keyTyped(KeyEvent e) {
        }

        private boolean _control = false;

        private boolean _shift = false;
    }

    /**
     * TimedRepaint is a timer thread that will schedule a
     * redraw each _REPAINT_TIME_INTERVAL milliseonds.
     */
    private static class TimedRepaint extends Timer {
        static int _REPAINT_TIME_INTERVAL = 30;

        public synchronized void addListener(PlotBox plotBox) {
            _listeners.add(plotBox);
            if (_listeners.size() == 1) {
                scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            // synchronized (this) to avoid changes to
                            // _listeners while repainting.
                            for (PlotBox plot : _listeners) {
                                if (plot._timedRepaint()) {
                                    plot._scheduledRedraw();
                                }
                            }
                        }
                    }
                }, 0, _REPAINT_TIME_INTERVAL);
            }
        }

        public synchronized void removeListener(PlotBox plotBox) {
            _listeners.remove(plotBox);
            if (_listeners.isEmpty()) {
                purge();
            }
        }

        private Set<PlotBox> _listeners = new HashSet<PlotBox>();
    }
}
