/* A panel for controlling the format of a plotter.

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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

///////////////////////////////////////////////////////////////////
//// PlotFormatter

/**

 PlotFormatter is a panel that controls the format of a plotter object
 passed to the constructor.

 @see Plot
 @see PlotBox
 @author Edward A. Lee
 @version $Id: PlotFormatter.java 57040 2010-01-27 20:52:32Z cxh $
 @since Ptolemy II 1.0
 @Pt.ProposedRating Yellow (eal)
 @Pt.AcceptedRating Red (cxh)
 */
public class PlotFormatter extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Construct a plot formatter for the specified plot object.
     */
    public PlotFormatter(PlotBox plot) {
        super();
        _plot = plot;

        setLayout(new BorderLayout());
        _wideQuery = new Query();
        add(_wideQuery, BorderLayout.WEST);
        _narrowQuery = new Query();
        add(_narrowQuery, BorderLayout.EAST);

        // Populate the wide query.
        _wideQuery.setTextWidth(20);
        _originalTitle = plot.getTitle();
        _wideQuery.addLine("title", "Title", _originalTitle);

        _originalCaptions = plot.getCaptions();
        StringBuffer captionsString = new StringBuffer();
        for (Enumeration<String> captions = _originalCaptions.elements(); captions
                .hasMoreElements();) {
            if (captionsString.length() > 0) {
                captionsString.append('\n');
            }
            captionsString.append(captions.nextElement());
        }
        _wideQuery.addTextArea("caption", "Caption", captionsString.toString());

        _originalXLabel = plot.getXLabel();
        _wideQuery.addLine("xlabel", "X Label", _originalXLabel);
        _originalYLabel = plot.getYLabel();
        _wideQuery.addLine("ylabel", "Y Label", _originalYLabel);
        _originalXRange = plot.getXRange();
        _wideQuery.addLine("xrange", "X Range", "" + _originalXRange[0] + ", "
                + _originalXRange[1]);
        _originalYRange = plot.getYRange();
        _wideQuery.addLine("yrange", "Y Range", "" + _originalYRange[0] + ", "
                + _originalYRange[1]);

        String[] marks = { "none", "points", "dots", "various", "bigdots",
                "pixels" };
        _originalMarks = "none";

        if (plot instanceof Plot) {
            _originalMarks = ((Plot) plot).getMarksStyle();
            _wideQuery.addRadioButtons("marks", "Marks", marks, _originalMarks);
        }

        _originalXTicks = plot.getXTicks();
        _originalXTicksSpec = "";

        if (_originalXTicks != null) {
            StringBuffer buffer = new StringBuffer();
            Vector<Double> positions = _originalXTicks[0];
            Vector<String> labels = _originalXTicks[1];

            for (int i = 0; i < labels.size(); i++) {
                if (buffer.length() > 0) {
                    buffer.append(", ");
                }

                buffer.append(labels.elementAt(i).toString());
                buffer.append(" ");
                buffer.append(positions.elementAt(i).toString());
            }

            _originalXTicksSpec = buffer.toString();
        }

        _wideQuery.addLine("xticks", "X Ticks", _originalXTicksSpec);

        _originalYTicks = plot.getYTicks();
        _originalYTicksSpec = "";

        if (_originalYTicks != null) {
            StringBuffer buffer = new StringBuffer();
            Vector<Double> positions = _originalYTicks[0];
            Vector<String> labels = _originalYTicks[1];

            for (int i = 0; i < labels.size(); i++) {
                if (buffer.length() > 0) {
                    buffer.append(", ");
                }

                buffer.append(labels.elementAt(i).toString());
                buffer.append(" ");
                buffer.append(positions.elementAt(i).toString());
            }

            _originalYTicksSpec = buffer.toString();
        }

        _wideQuery.addLine("yticks", "Y Ticks", _originalYTicksSpec);

        _originalGrid = plot.getGrid();
        _narrowQuery.addCheckBox("grid", "Grid", _originalGrid);
        _originalStems = false;
        _originalConnected = null;

        if (plot instanceof Plot) {
            _originalStems = ((Plot) plot).getImpulses();
            _narrowQuery.addCheckBox("stems", "Stems", _originalStems);
            _saveConnected();
            _narrowQuery.addCheckBox("connected", "Connect", ((Plot) plot)
                    .getConnected());
        }

        _originalColor = plot.getColor();
        _narrowQuery.addCheckBox("color", "Use Color", _originalColor);

        if (plot instanceof Plot) {
            // This method is also called by Histogram,
            //  and Histogram does not have getLineStyles()
            _originalLineStyles = ((Plot) plot).getLineStyles();
            _narrowQuery.addCheckBox("lineStyles", "Use Line Styles",
                    _originalLineStyles);
        }

        // FIXME: setXLog() and setYLog() cause problems with
        // dropped data if they are toggled after data is read in.
        // This is because the log axis facility modifies the datasets
        // in addPlotPoint() in Plot.java.  When this is fixed
        // we can add the XLog and YLog facility to the Format menu
        //
        // _originalXLog = plot.getXLog();
        //_narrowQuery.addCheckBox("xlog", "X Log", _originalXLog);
        //if (_originalXTicks != null) {
        //    _narrowQuery.setBoolean("xlog", false);
        //    _narrowQuery.setEnabled("xlog", false);
        //}
        // _originalYLog = plot.getYLog();
        //_narrowQuery.addCheckBox("ylog", "Y Log", _originalYLog);
        //if (_originalYTicks != null) {
        //    _narrowQuery.setBoolean("ylog", false);
        //    _narrowQuery.setEnabled("ylog", false);
        //}
        // Attach listeners.
        _wideQuery.addQueryListener(new QueryListener() {
            @Override
            public void changed(String name) {
                if (name.equals("title")) {
                    _plot.setTitle(_wideQuery.getStringValue("title"));
                } else if (name.equals("caption")) {
                    _plot.clearCaptions();
                    String newCaption = _wideQuery.getStringValue("caption");
                    String[] captionsArray = newCaption.split("\\n");
                    for (int i = 0; i < captionsArray.length; i++) {
                        _plot.read("captions: " + captionsArray[i]);
                    }
                } else if (name.equals("xlabel")) {
                    _plot.setXLabel(_wideQuery.getStringValue("xlabel"));
                } else if (name.equals("ylabel")) {
                    _plot.setYLabel(_wideQuery.getStringValue("ylabel"));
                } else if (name.equals("xrange")) {
                    _plot
                            .read("XRange: "
                                    + _wideQuery.getStringValue("xrange"));
                } else if (name.equals("xticks")) {
                    String spec = _wideQuery.getStringValue("xticks").trim();
                    _plot.read("XTicks: " + spec);

                    // FIXME: log axis format temporarily
                    // disabled, see above.
                    // if (spec.equals("")) {
                    //    _narrowQuery.setEnabled("xlog", true);
                    // } else {
                    //    _narrowQuery.setBoolean("xlog", false);
                    //    _narrowQuery.setEnabled("xlog", false);
                    // }
                } else if (name.equals("yticks")) {
                    String spec = _wideQuery.getStringValue("yticks").trim();
                    _plot.read("YTicks: " + spec);

                    // FIXME: log axis format temporarily
                    // disabled, see above.
                    // if (spec.equals("")) {
                    //    _narrowQuery.setEnabled("ylog", true);
                    // } else {
                    //    _narrowQuery.setBoolean("ylog", false);
                    //    _narrowQuery.setEnabled("ylog", false);
                    // }
                } else if (name.equals("yrange")) {
                    _plot
                            .read("YRange: "
                                    + _wideQuery.getStringValue("yrange"));
                } else if (name.equals("marks")) {
                    ((Plot) _plot).setMarksStyle(_wideQuery
                            .getStringValue("marks"));
                }

                _plot.repaint();
            }
        });

        _narrowQuery.addQueryListener(new QueryListener() {
            @Override
            public void changed(String name) {
                if (name.equals("grid")) {
                    _plot.setGrid(_narrowQuery.getBooleanValue("grid"));
                } else if (name.equals("stems")) {
                    ((Plot) _plot).setImpulses(_narrowQuery
                            .getBooleanValue("stems"));
                    _plot.repaint();
                } else if (name.equals("color")) {
                    _plot.setColor(_narrowQuery.getBooleanValue("color"));

                    // FIXME: log axis format temporarily
                    // disabled, see above.
                    // } else if (name.equals("xlog")) {
                    //    _plot.setXLog(_narrowQuery.getBooleanValue("xlog"));
                    // } else if (name.equals("ylog")) {
                    //    _plot.setYLog(_narrowQuery.getBooleanValue("ylog"));
                } else if (name.equals("connected")) {
                    _setConnected(_narrowQuery.getBooleanValue("connected"));
                } else if (name.equals("lineStyles")) {
                    ((Plot) _plot).setLineStyles(_narrowQuery
                            .getBooleanValue("lineStyles"));
                }

                _plot.repaint();
            }
        });
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Apply currently specified values to the associated plot.
     *  This requests a repaint of the plot.
     */
    public void apply() {
        // Apply current values.
        _plot.setTitle(_wideQuery.getStringValue("title"));
        _plot.setXLabel(_wideQuery.getStringValue("xlabel"));
        _plot.setYLabel(_wideQuery.getStringValue("ylabel"));
        _plot.read("XRange: " + _wideQuery.getStringValue("xrange"));
        _plot.read("YRange: " + _wideQuery.getStringValue("yrange"));
        _plot.setGrid(_narrowQuery.getBooleanValue("grid"));
        _plot.setColor(_narrowQuery.getBooleanValue("color"));
        // FIXME: log axis format temporarily disable, see above.
        // _plot.setXLog(_narrowQuery.getBooleanValue("xlog"));
        // _plot.setYLog(_narrowQuery.getBooleanValue("ylog"));
        if (_plot instanceof Plot) {
            // This method is also called by Histogram,
            //  and Histogram does not have lineStyles
            Plot cplot = (Plot) _plot;
            cplot.setLineStyles(_narrowQuery.getBooleanValue("lineStyles"));
            cplot.setMarksStyle(_wideQuery.getStringValue("marks"));
            cplot.setImpulses(_narrowQuery.getBooleanValue("stems"));
            _setConnected(_narrowQuery.getBooleanValue("connected"));
        }

        // FIXME: log axis format temporarily disable, see above.
        // String spec = _wideQuery.getStringValue("xticks").trim();
        // _plot.read("XTicks: " + spec);
        // if (spec.equals("")) {
        //    _narrowQuery.setEnabled("xlog", true);
        // } else {
        //    _narrowQuery.setBoolean("xlog", false);
        //    _narrowQuery.setEnabled("xlog", false);
        // }
        // spec = _wideQuery.getStringValue("yticks").trim();
        // _plot.read("YTicks: " + spec);
        // if (spec.equals("")) {
        //    _narrowQuery.setEnabled("ylog", true);
        // } else {
        //    _narrowQuery.setBoolean("ylog", false);
        //    _narrowQuery.setEnabled("ylog", false);
        // }
        _plot.repaint();
    }

    /** Open a format control window as a top-level, modal dialog.
     */
    public void openModal() {
        String[] buttons = { "Apply", "Cancel" };

        // NOTE: If the plot is in a top-level container that is a Frame
        // (as opposed to an applet), then tell the dialog that the Frame
        // owns the dialog.
        Container toplevel = _plot.getTopLevelAncestor();
        Frame frame = null;

        if (toplevel instanceof Frame) {
            frame = (Frame) toplevel;
        }

        ComponentDialog dialog = new ComponentDialog(frame, "Set plot format",
                this, buttons);

        if (dialog.buttonPressed().equals("Apply")) {
            apply();
        } else {
            restore();
        }
    }

    /** Restore the original configuration of the plot, and request a
     *  a redraw.
     */
    public void restore() {
        // Restore _original values.
        _plot.setTitle(_originalTitle);
        _plot.setCaptions(_originalCaptions);
        _plot.setXLabel(_originalXLabel);
        _plot.setYLabel(_originalYLabel);
        _plot.setXRange(_originalXRange[0], _originalXRange[1]);
        _plot.setYRange(_originalYRange[0], _originalYRange[1]);
        _plot.setGrid(_originalGrid);
        _plot.setColor(_originalColor);
        // FIXME: log axis format temporarily disable, see above.
        // _plot.setXLog(_originalXLog);
        // _plot.setYLog(_originalYLog);
        if (_plot instanceof Plot) {
            // This method is also called by Histogram,
            //  and Histogram does not have lineStyles
            Plot cplot = (Plot) _plot;
            cplot.setLineStyles(_originalLineStyles);
            cplot.setMarksStyle(_originalMarks);
            cplot.setImpulses(_originalStems);
            _restoreConnected();
        }

        // FIXME: log axis format temporarily disabled, see above.
        // _plot.read("XTicks: " + _originalXTicksSpec);
        // if (_originalXTicksSpec.equals("")) {
        //    _narrowQuery.setEnabled("xlog", true);
        // } else {
        //   _narrowQuery.setBoolean("xlog", false);
        //    _narrowQuery.setEnabled("xlog", false);
        // }
        // _plot.read("YTicks: " + _originalYTicksSpec);
        // if (_originalYTicksSpec.equals("")) {
        //    _narrowQuery.setEnabled("ylog", true);
        // } else {
        //    _narrowQuery.setBoolean("ylog", false);
        //    _narrowQuery.setEnabled("ylog", false);
        // }
        _plot.repaint();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected variables               ////

    /** @serial The plot object controlled by this formatter. */
    protected final PlotBox _plot;

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////
    // Save the current connected state of all the point currently in the
    // plot.  NOTE: This method reaches into the protected members of
    // the Plot class, taking advantage of the fact that this class is
    // in the same package.
    private void _saveConnected() {
        ArrayList<ArrayList<PlotPoint>> points = ((Plot) _plot)._points;
        _originalConnected = new boolean[points.size()][];
        _originalPoints = new PlotPoint[points.size()][];

        for (int dataset = 0; dataset < points.size(); dataset++) {
            ArrayList<PlotPoint> pts = points.get(dataset);
            _originalConnected[dataset] = new boolean[pts.size()];
            _originalPoints[dataset] = new PlotPoint[pts.size()];

            for (int i = 0; i < pts.size(); i++) {
                PlotPoint pt = pts.get(i);
                _originalConnected[dataset][i] = pt.connected;
                _originalPoints[dataset][i] = pt;
            }
        }
    }

    // Set the current connected state of all the point in the
    // plot.  NOTE: This method reaches into the protected members of
    // the Plot class, taking advantage of the fact that this class is
    // in the same package.
    private void _setConnected(boolean value) {
        //ArrayList<ArrayList<PlotPoint>> points = ((Plot) _plot)._points;

        // Make sure the default matches.
        ((Plot) _plot).setConnected(value);

        // We don't change the individual points anymore, but when the
        // we plot points we'll both look at the global connected state and
        // the one of the individual points (both will be and'ed).
        /*
        boolean[][] result = new boolean[points.size()][];

        for (int dataset = 0; dataset < points.size(); dataset++) {
            ArrayList<PlotPoint> pts = points.get(dataset);
            result[dataset] = new boolean[pts.size()];

            boolean first = true;

            for (int i = 0; i < pts.size(); i++) {
                PlotPoint pt = pts.get(i);
                pt.connected = value && !first;
                first = false;
            }
        }
        */
    }

    // Restore the connected state of all the point that were in the
    // plot when their connected state was saved.
    // NOTE: This method reaches into the protected members of
    // the plot class, taking advantage of the fact that this class is
    // in the same package.
    private void _restoreConnected() {
        for (int dataset = 0; dataset < _originalPoints.length; dataset++) {
            for (int i = 0; i < _originalPoints[dataset].length; i++) {
                PlotPoint pt = _originalPoints[dataset][i];
                pt.connected = _originalConnected[dataset][i];
            }
        }
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    // Query widgets.
    private Query _wideQuery;

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    // Query widgets.
    private Query _narrowQuery;

    // Original configuration of the plot.
    private String _originalTitle;

    // Original configuration of the plot.
    private Vector<String> _originalCaptions;

    // Original configuration of the plot.
    private String _originalXLabel;

    // Original configuration of the plot.
    private String _originalYLabel;

    // Original configuration of the plot.
    private String _originalMarks;

    // Original configuration of the plot.
    private String _originalXTicksSpec;

    // Original configuration of the plot.
    private String _originalYTicksSpec;

    private double[] _originalXRange;

    private double[] _originalYRange;

    @SuppressWarnings("rawtypes")
    private Vector[] _originalXTicks;

    @SuppressWarnings("rawtypes")
    private Vector[] _originalYTicks;

    private boolean _originalGrid;

    private boolean _originalLineStyles;

    private boolean _originalStems;

    private boolean _originalColor;

    private boolean[][] _originalConnected;

    private PlotPoint[][] _originalPoints;
}
